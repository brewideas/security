package com.infius.proximitysecurity.utilities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;

//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
//import com.infius.proximityuser.R;
//import com.infius.proximityuser.activities.AddGuestActivity;
//import com.infius.proximityuser.activities.AuthActivity;

import com.infius.proximitysecurity.R;
import com.infius.proximitysecurity.activities.AuthActivity;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 56;

    public static boolean isValidPhoneNo(String phoneNo) {
        Pattern p = Pattern.compile(AppConstants.MOB_NO_REG_EX);
        Matcher m = p.matcher(phoneNo);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static boolean isValidEmail(String email) {
        Pattern p = Pattern.compile(AppConstants.EMAIL_REG_EX);
        Matcher m = p.matcher(email);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static Dialog getProgressDialog(Activity activity) {

        try {
            if (activity != null) {
                Dialog progressDialog = new Dialog(activity);
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressDialog.setCancelable(false);
                progressDialog.setContentView(R.layout.lyt_progress_bar);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                return progressDialog;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

//    public static Bitmap generateQR(String inputData, int qrWidth, int qrHeight) throws Exception {
//        Map hints;
//        hints = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
//        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//        if (!TextUtils.isEmpty(inputData)) {
//            BitMatrix result;
//            try {
//                result = new MultiFormatWriter().encode(inputData, BarcodeFormat.QR_CODE, qrWidth, qrHeight, hints);
//            } catch (IllegalArgumentException iae) {
//                // Unsupported format
//                return null;
//            }
//
//            int width = result.getWidth();
//            int height = result.getHeight();
//            int[] pixels = new int[width * height];
//
//            for (int y = 0; y < height; y++) {
//                int offset = y * width;
//                // if (y < ymin || y > ymax) {
//                for (int x = 0; x < width; x++) {
//                    pixels[offset + x] = result.get(x, y) ? AppConstants.QR_KEY_BLACK : AppConstants.QR_KEY_WHITE;
//                }
//
//            }
//
//            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//            return bitmap;
//        } else {
//            return null;
//        }
//    }

    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dp * (metrics.densityDpi / 160f));
        return px;
    }

    public static boolean isVersionMarshmallowAndAbove() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    public static void requestWriteExternalPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
    }

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

    public static String removeSpaceAndBracket(String phoneNo) {
        if (!TextUtils.isEmpty(phoneNo)) {
            phoneNo = phoneNo.trim();
            phoneNo = phoneNo.replaceAll("[^\\d+]", "");
            phoneNo = phoneNo.replaceAll(" ", "");
            return phoneNo;
        } else {
            return "";
        }
    }

//    public static String filterMobileNumber(AddGuestActivity activity, String phoneNumber) {
//        if (!TextUtils.isEmpty(phoneNumber)) {
//            phoneNumber = phoneNumber.trim();
//            int startIndex = AppConstants.START_INDEX_ZERO;
//            phoneNumber = phoneNumber.replaceAll("[^\\d+]", "");
//            if (phoneNumber.startsWith(activity.getString(R.string.mobile_number_prefix_91))) {
//                startIndex = AppConstants.START_INDEX_THREE;
//            } else if (phoneNumber.startsWith(activity.getString(R.string.mobile_number_prefix_91_without_plus))) {
//                startIndex = AppConstants.START_INDEX_TWO;
//            } else if (phoneNumber.startsWith(activity.getString(R.string.mobile_number_prefix_0))) {
//                startIndex = AppConstants.START_INDEX_ONE;
//            }
//            phoneNumber = phoneNumber
//                    .substring(startIndex, phoneNumber.length()).trim()
//                    .replaceAll(" ", "");
//            return phoneNumber;
//        } else {
//            return "";
//        }
//    }

    private static final int MIN_DISK_CACHE_SIZE = 30 * 1024 * 1024; // 30 MB
    private static final int MAX_DISK_CACHE_SIZE = 100 * 1024 * 1024; // 100 MB

    public static boolean isOnBoardingCompleted(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(
                AppConstants.COMPLETED_ONBOARDING_PREF_NAME, true);
    }

    public static boolean isUserLoggedIn(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(AppConstants.SP_IS_LOGGEDIN, false);
    }

    public static long diskCacheSizeBytes(File dir, long minSize) {
        long size = minSize;
        try {
            StatFs statFs = new StatFs(dir.getAbsolutePath());
            long availableBytes = ((long) statFs.getBlockCount()) * statFs.getBlockSize();
            size = availableBytes / 50;
        } catch (IllegalArgumentException e) {
            Log.d("DeviceUtils", "Unable to calculate 2% of available disk space, defaulting to minimum");
        }

        // Bound inside min/max size for disk cache.
        return Math.max(Math.min(size, MAX_DISK_CACHE_SIZE), MIN_DISK_CACHE_SIZE);
    }

    public static int getOSVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static void login(AppCompatActivity activity) {
        Intent intent = new Intent(activity, AuthActivity.class);
        activity.startActivityForResult(intent, AppConstants.REQUEST_CODE_LOGIN);
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        return height;
    }

    public static String getTimeStamp(String dateInput, String timeInput) {

        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dateInput);
            str = outputFormat.format(date) + "T" + timeInput+".000";
//            str = outputFormat.format(date) + "T" + "04:24"+".000";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
