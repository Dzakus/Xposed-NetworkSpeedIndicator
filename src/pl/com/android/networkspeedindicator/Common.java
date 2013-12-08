package pl.com.android.networkspeedindicator;

import android.content.SharedPreferences;

import java.util.HashSet;

public class Common {

	public static final String PKG_NAME = "pl.com.android.networkspeedindicator";
	public static final String PREFERENCE_FILE = PKG_NAME + "_preferences";
	public static final String ACTION_SETTINGS_CHANGED = PKG_NAME + ".changed";

	public static final String KEY_HIDE_UNIT = "hide_unit";
	public static final String KEY_HIDE_INACTIVE = "hide_inactive";
	public static final String KEY_FORCE_UNIT = "force_unit";
	public static final String KEY_HIDE_NETWORK_TYPE = "hide_network_type";
	
	public static final boolean DEF_HIDE_UNIT = false;
	public static final boolean DEF_HIDE_INACTIVE = false;
	public static final int DEF_FORCE_UNIT = 0;
	public static final HashSet<String> DEF_HIDE_NETWORK_STATE = new HashSet<String>();

	public static int getPrefInt(SharedPreferences pref, String key, int def_value) {
		try {
			String value = pref.getString(key, String.valueOf(def_value));
			return Integer.parseInt(value);
		} catch (Exception e) {
			// Do nothing
		}
		return def_value;
	}

}
