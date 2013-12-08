package pl.com.android.networkspeedindicator;

import android.content.res.XResources;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;

public class Module implements IXposedHookInitPackageResources {

	public static final String PKG_NAME_SYSTEM_UI = "com.android.systemui";

	private static Map<String, String> mLayouts;
	static {
		Map<String, String> tmpMap = new HashMap<String, String>();
		tmpMap.put("tw_super_status_bar", "statusIcons");
		tmpMap.put("status_bar", "statusIcons");

		mLayouts = Collections.unmodifiableMap(tmpMap);
	}

	@Override
	public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
		if (!resparam.packageName.equals(PKG_NAME_SYSTEM_UI))
			return;
		XResources res = resparam.res;

		final Entry<String, String> layoutInfo = findLayoutInfo(res);
		if (layoutInfo == null)
			return;

		res.hookLayout(PKG_NAME_SYSTEM_UI, "layout", layoutInfo.getKey(), new XC_LayoutInflated() {

			@Override
			public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
				XposedBridge.log("Hook!");
				FrameLayout root = (FrameLayout) liparam.view;
				LinearLayout system_icon_area = (LinearLayout) root.findViewById(liparam.res.getIdentifier(
						layoutInfo.getValue(), "id", PKG_NAME_SYSTEM_UI));
				TextView clock = (TextView) root.findViewById(liparam.res.getIdentifier("clock", "id",
						PKG_NAME_SYSTEM_UI));

				TrafficView trafficView = new TrafficView(root.getContext());
				trafficView.setLayoutParams(clock.getLayoutParams());
				trafficView.setSingleLine(true);
				trafficView.setTextColor(clock.getCurrentTextColor());
				trafficView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

				system_icon_area.addView(trafficView, 0);
			}
		});
	}

	public static Entry<String, String> findLayoutInfo(XResources res) {
		Iterator<Entry<String, String>> iterator = mLayouts.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			if (res.getIdentifier(entry.getKey(), "layout", PKG_NAME_SYSTEM_UI) != 0
					&& res.getIdentifier(entry.getValue(), "id", PKG_NAME_SYSTEM_UI) != 0)
				return entry;
		}

		return null;
	}

}
