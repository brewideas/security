package com.infius.proximitysecurity.utilities;

public interface AppConstants {
    long SPLASH_TIMEOUT = 1000;
    String QR_CODE_ID = "qr_code_id";

    String MOB_NO_REG_EX = "^([6,7,8,9]{1}+[0-9]{9})$";
    String EMAIL_REG_EX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    int QR_KEY_WHITE = 0xFFFFFFFF;
    int QR_KEY_BLACK = 0xFF000000;
    public static final int START_INDEX_ZERO = 0;
    public static final int START_INDEX_ONE = 1;
    public static final int START_INDEX_TWO = 2;
    public static final int START_INDEX_THREE = 3;

    String COMPLETED_ONBOARDING_PREF_NAME = "onBoarding";
    int TEN_MB = 10 * 1024 * 1024;

    int REQUEST_CODE_PICK_CONTACT = 1001;
    int REQUEST_CODE_LOGIN = 1002;
    String SP_IS_LOGGEDIN = "is_logged_in";
    String SP_PROFILE_PIC_URL = "profile_pic";
    String STATUS_SUCCESS = "SUCCESS";
    String GUEST_LIST_TYPE = "guest_list_type";

    int TYPE_HISTORY = 1;
    int TYPE_PRESENT = 2;
    int TYPE_UPCOMING = 3;
    int TYPE_PREFERRED = 4;
    String GUEST_TYPE_PREFERRED = "PREFERRED";
    String GUEST_TYPE_NORMAL = "NORMAL";

    String GUEST_LIST_PARAM_HISTORY = "HISTORY";
    String GUEST_LIST_PARAM_PREFERRED = "PREFERRED";
    String GUEST_LIST_PARAM_UPCOMING = "FUTURE";
    String GUEST_LIST_PARAM_PRESENT = "PRESENT";
    int PROXIMITY_QR_LENGTH = 38;
    String QR_CODE_DATA = "qr_data";

}
