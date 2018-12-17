package com.infius.proximitysecurity.utilities;

import android.content.Context;
import android.preference.PreferenceManager;


public class ProfileUtils {
    public static String getUserName(Context context) {
        return "Ashutosh Sharma";
    }

    public static String getPropertyName(Context context) {
        return "Prateek Wisteria";
    }

    public static String getOwnershipType(Context context) {
        return "Security Chief";
    }

    public static String getProfileImageUrl(Context context) {
        String url = "mock";
        url = PreferenceManager.getDefaultSharedPreferences(context).getString(AppConstants.SP_PROFILE_PIC_URL, "mock");
        return url;
    }
}
