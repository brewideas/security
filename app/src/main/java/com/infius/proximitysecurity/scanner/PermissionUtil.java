package com.infius.proximitysecurity.scanner;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

/**
 * Utility class that wraps access to the runtime permissions API in M and provides basic helper
 * methods.
 */
public abstract class PermissionUtil {

    public static final int REQUEST_PERMISSION_SHOW_RATIONALE = 1;
    public static final int REQUEST_PERMISSION_DO_NOT_SHOW_RATIONALE = 0;

    /**
     * Id to identify a permission request.
     */
    public static final int REQUEST_CODE_CAMERA = 51;
    public static final int REQUEST_CODE_PHONE_STATE = 52;
    public static final int REQUEST_CODE_READ_CONTACTS = 53;
    public static final int REQUEST_CODE_GET_ACCOUNTS = 54;
    public static final int REQUEST_CODE_READ_SMS = 55;
    public static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 56;
    public static final int REQUEST_CODE_ACCESS_LOCATION = 57;

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     *
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     */
    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }
        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if permission is denied, can show rationale and if permission has been been asked more than once and user has denied the permission with "Never ask again" checked
     *
     * @param permissions
     * @param grantResults
     * @param requestedPermission
     * @param activity
     * @return -1 = Unknown state.
     */
    public static int shouldShowRequestPermissionRationaleState(String[] permissions, int[] grantResults, String requestedPermission, Activity activity) {
        for (int i = 0, len = permissions.length; i < len; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
                if (!showRationale) {
                    // user denied flagging NEVER ASK AGAIN
                    // you can either enable some fall back,
                    // disable features of your app
                    // or open another dialog explaining
                    // again the permission and directing to
                    // the app setting
                    return REQUEST_PERMISSION_DO_NOT_SHOW_RATIONALE;

                } else if (requestedPermission.equals(permission)) {
                    // user denied WITHOUT never ask again
                    // this is a good place to explain the user
                    // why you need the permission and ask if he want
                    // to accept it (the rationale)

                    return REQUEST_PERMISSION_SHOW_RATIONALE;
                }
                return -1;
            }
        }
        return -1;
    }

    public static boolean isVersionMarshmallowAndAbove() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    public static boolean isVersionNougatAndAbove() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.M);
    }

    // Required to get device identifier
    public static boolean checkReadPhoneStatePermission(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    // Required to get device identifier
    public static void requestReadPhoneStatePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_PHONE_STATE);
    }

    // Required to get camera
    public static boolean checkCameraPermission(Activity activity) {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestCameraPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
    }

    public void showDialogForPermissionSettings(String message) {

    }

    public static void openAppSettingPage(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    public static boolean checkReadContactsPermission(Activity activity) {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestReadContactsPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
    }

    public static boolean checkPhoneStatePermission(Activity activity) {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestWriteExternalPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
    }

    public static boolean checkWriteExternalStoragePermission(Activity activity) {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkGetAccountsPermission(Activity activity) {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED;
    }


    public static boolean checkReadSmsPermission(Context activity) {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestAccessLocationPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ACCESS_LOCATION);
    }

    public static boolean checkAccessLocationPermission(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkWriteContactPermission(Activity activity) {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CONTACTS)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkTelephonyPermission(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }
}

