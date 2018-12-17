package com.infius.proximitysecurity.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.vision.text.TextBlock;
import com.infius.proximitysecurity.R;
import com.infius.proximitysecurity.activities.DisplayGuestDetailsActivity;
import com.infius.proximitysecurity.model.DataModel;
import com.infius.proximitysecurity.model.GuestHistoryModel;
import com.infius.proximitysecurity.model.QRInfoResponse;
import com.infius.proximitysecurity.scanner.BarcodeTrackerFactory;
import com.infius.proximitysecurity.scanner.CameraSource;
import com.infius.proximitysecurity.scanner.CameraSourcePreview;
import com.infius.proximitysecurity.scanner.PermissionUtil;
import com.infius.proximitysecurity.listeners.ScannerDetectionListener;
import com.infius.proximitysecurity.utilities.ApiRequestHelper;
import com.infius.proximitysecurity.utilities.AppConstants;
import com.infius.proximitysecurity.utilities.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kshemendra on 08/09/16.
 */


public class ScannerFragment extends Fragment {
    private static final String TAG = ScannerFragment.class.getSimpleName();
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    private static final int RC_HANDLE_GMS = 9001;
    private static final int REQUEST_PERMISSION_SETTING = 401;
    private static final String NO_OF_CLICKS = "no_of_clicks";
    private static final int CLICKS_THRESHOLD = 3;
    private ImageView flashIcon, galleryIcon, ivMyQr, mCrossIcon, mBackIcon;
    private View rootView;
    private RelativeLayout noPermissionLayout;
    private RelativeLayout parentLayout;
    private Button enableCameraBtn;
    private Dialog mProgressDialog;
    private boolean askedForPermission = false;
    private boolean mFragmentVisible;
    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;
    private boolean flashOFF = true;
    private boolean shouldHandleRes = true;

    private static final int frameOffset = 30;
    private static final int MIN_FRAME_WIDTH = 240;
    private static final int MIN_FRAME_HEIGHT = 240;
    private static final int MAX_FRAME_WIDTH = 675; // = 5/8 * 1920
    private static final int MAX_FRAME_HEIGHT = 675;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.scanner_fragment, container, false);
        this.rootView = rootView;
        mPreview = (CameraSourcePreview) rootView.findViewById(R.id.preview);
//        initBalance();
        noPermissionLayout = (RelativeLayout) rootView.findViewById(R.id.capture_permission_denied_layout);
        enableCameraBtn = (Button) rootView.findViewById(R.id.capture_enable_camera_btn);
        /*RecentScansHelper recentScansHelper = RecentScansHelper.getInstance();
        ArrayList<IJRDataModel> ijrDataModels = recentScansHelper.getRecentScansList(getActivity());*/
        enableCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableCamera();
            }
        });

        initSettings();

        return rootView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void initSettings() {
        askedForPermission = true;
        checkForCameraPermission();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public float dpToPixel(int dp) {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int density = dm.densityDpi;
        return density * dp / 160;
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        mFragmentVisible = visible;
        if (visible) {
            askedForPermission = true;
            checkForCameraPermission();
        } else if (isResumed() && !visible) {
        } else if (!isResumed() && !visible) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PermissionUtil.REQUEST_CODE_CAMERA) {
            Map<String, Object> map = new HashMap<>();
            map.put("app_permissions_and_status", "camera=true");
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                map.put("app_permissions_and_status", "camera=false");
            }
            if (PermissionUtil.verifyPermissions(grantResults)) {
                setNoPermissionLayoutVisibility(View.GONE);
                createCameraSource();
            } else {
                int rationalState = PermissionUtil.shouldShowRequestPermissionRationaleState(permissions, grantResults, Manifest.permission.CAMERA, getActivity());
                if (rationalState == PermissionUtil.REQUEST_PERMISSION_DO_NOT_SHOW_RATIONALE) {
                    if (getActivity() == null || getActivity().isFinishing()) {
                        return;
                    }
                }
                setNoPermissionLayoutVisibility(View.VISIBLE);
            }
        }
    }

    private void setNoPermissionLayoutVisibility(int visibility) {
        if (noPermissionLayout != null) {
            noPermissionLayout.setVisibility(visibility);
        }
    }


    private void checkForCameraPermission() {
        if (!isCameraPermtted()) {
            setNoPermissionLayoutVisibility(View.VISIBLE);
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    PermissionUtil.REQUEST_CODE_CAMERA);
        } else {
            createCameraSource();
        }
    }


    private void createCameraSource() {
        Context context = getActivity().getApplicationContext();
        // A barcode detector is created to track barcodes.  An associated multi-processor instance
        // is set to receive the barcode detection results, track the barcodes, and maintain
        // graphics for each barcode on screen.  The factory is used by the multi-processor to
        // create a separate tracker instance for each barcode.
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).setBarcodeFormats(Barcode.QR_CODE).build();
        ScannerDetectionListener listener = getScannerDetectionListener();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(listener);
//        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
//        textRecognizer.setProcessor(new OcrDetectorProcessor(listener));
        barcodeDetector.setProcessor(
                new MultiProcessor.Builder<>(barcodeFactory).build());
        if (!barcodeDetector.isOperational()) {
            // Note: The first time that an app using the barcode or face API is installed on a
            // device, GMS will download a native libraries to the device in order to do detection.
            // Usually this completes before the app is run for the first time.  But if that
            // download has not yet completed, then the above call will not detect any barcodes
            // and/or faces.
            //
            // isOperational() can be used to check if the required native libraries are currently
            // available.  The detectors will automatically become operational once the library
            // downloads complete on device.
            Log.w(TAG, "Detector dependencies are not yet available.");
            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
//            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;
//
//            if (hasLowStorage) {
//                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
//                Log.w(TAG, getString(R.string.low_storage_error));
//            }
        }
        // Creates and starts the camera.  Note that this uses a higher resolution in comparison
        // to other detection examples to enable the barcode detector to detect small barcodes
        // at long distances.
        int width = getScreenWidth();
        int height = getScreenHeight();
        ArrayList<Detector<?>> detectors = new ArrayList<>();
        detectors.add(barcodeDetector);
//        detectors.add(textRecognizer);
        CameraSource.Builder builder = new CameraSource.Builder(getActivity().getApplicationContext(), detectors)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(width, height)
                .setRequestedFps(15.0f);
        // make sure that auto focus is an available option
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            builder = builder.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        mCameraSource = builder
                .setFlashMode(!flashOFF ? Camera.Parameters.FLASH_MODE_TORCH : null).build();
    }


    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private void requestCameraPermission() {
        final String[] permissions = new String[]{Manifest.permission.CAMERA};
        if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(getActivity(), permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }
        final Activity thisActivity = getActivity();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };
    }

    private void enableCamera() {
        requestPermissions(new String[]{Manifest.permission.CAMERA},
                PermissionUtil.REQUEST_CODE_CAMERA);
    }


    /**
     * Restarts the camera.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (!askedForPermission && !isCameraPermtted()) {
            setNoPermissionLayoutVisibility(View.VISIBLE);
        }
        if (isCameraPermtted()) {
            setNoPermissionLayoutVisibility(View.GONE);
        }
        askedForPermission = false;
        initViews();
        startCameraSource();
//        setMobileIconVisibility();
    }

    @Override
    public void onStart() {
        super.onStart();
        shouldHandleRes = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        shouldHandleRes = false;
    }

    private void initViews() {
        int rc = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
        } else {
            requestCameraPermission();
        }
    }

    private boolean isCameraPermtted() {
        return !(PermissionUtil.isVersionMarshmallowAndAbove() && !PermissionUtil.checkCameraPermission(getActivity()));
    }


    /**
     * Stops the camera.
     */
    @Override
    public void onPause() {
        super.onPause();
//        shouldHandleRes = false;
        if (mPreview != null) {
            mPreview.stop();
        }
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPreview != null) {
            mPreview.release();
        }
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() throws SecurityException {
        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getActivity().getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), code, RC_HANDLE_GMS);
            dlg.show();
        }
        if (mCameraSource != null) {
            try {
                Log.e(TAG, "Camera Started");
                mPreview.start(mCameraSource);
            } catch (Exception e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    private void stopCamera() {
        mPreview.stop();
        if (mCameraSource != null) {
            mCameraSource.release();
            mCameraSource = null;
        }
    }

    private final int PAYTM_DYNAMIC_QR_LENGTH = 24;

    /**
     * Handles the result received from scanning the qr-code.
     * Firt it checks the type of QR Code if its a dynamic QR Type or deeplink and
     * then handles accordingly
     */
    public void handleResult(String qrData) {
        Log.e(TAG, "Scan result received " + qrData);
        if (!TextUtils.isEmpty(qrData) && qrData.length() == AppConstants.PROXIMITY_QR_LENGTH) {
            showProgressDialog();
            ApiRequestHelper.requestQRInfo(getActivity(), qrData, new Response.Listener<DataModel>() {
                @Override
                public void onResponse(DataModel response) {
                    hideProgressDialog();
                    boolean error = false;
                    if (response instanceof QRInfoResponse) {
                        if (AppConstants.STATUS_SUCCESS.equalsIgnoreCase(((QRInfoResponse) response).getStatus())) {
                            Toast.makeText(getActivity(), ((QRInfoResponse) response).getMessage(), Toast.LENGTH_SHORT).show();
                            openGuestDetailActivity((QRInfoResponse) response);
                        } else {
                            error = true;
                        }
                    } else {
                        error = true;
                    }

                    if (error) {
                        shouldHandleRes = true;
                        Toast.makeText(getActivity(), R.string.not_proximity_qr, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideProgressDialog();
                    shouldHandleRes = true;
                    if (error != null && !TextUtils.isEmpty(error.getMessage())) {
                        Toast.makeText(getActivity(), R.string.not_proximity_qr, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), R.string.not_proximity_qr, Toast.LENGTH_SHORT).show();
            shouldHandleRes = true;
        }
    }

    private void openGuestDetailActivity(QRInfoResponse data) {
        Intent intent = new Intent(getActivity(), DisplayGuestDetailsActivity.class);
        intent.putExtra(AppConstants.QR_CODE_DATA, data);
        startActivity(intent);
        getActivity().finish();
    }



    /**
     * Checks if a particular application package is installed on device.
     * Supplied with string parameter as paytm package name.
     */
    private boolean isAppInstalled(String packageNameUri) {
        PackageManager pm = getActivity().getPackageManager();
        List<PackageInfo> packageInfoList = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        if (packageInfoList != null) {
            for (PackageInfo packageInfo : packageInfoList) {
                String packageName = packageInfo.packageName;
                if (packageNameUri.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public ScannerDetectionListener getScannerDetectionListener() {
        return new ScannerDetectionListener() {
            @Override
            synchronized public void onBarcodeDetected(final Barcode item) {
                final String val = item.rawValue;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (shouldHandleRes) {
                            shouldHandleRes = false;
                            if (item.format == Barcode.QR_CODE) {
                                handleResult(val);
                            }
                        }
                    }
                });
            }

            @Override
            synchronized public void onTextDetected(SparseArray<TextBlock> items) {
                String mobileNumber = null;
                if (shouldHandleRes) {
                    for (int i = 0; i < items.size(); ++i) {
                        TextBlock item = items.valueAt(i);
                        if (!TextUtils.isEmpty(mobileNumber)) {
                            shouldHandleRes = false;
                            break;
                        }
                    }
                }
            }
        };
    }

    public int getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        return height;
    }

    private int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return width;
    }

    protected void showProgressDialog() {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = Utils.getProgressDialog(getActivity());
            }
            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void hideProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
