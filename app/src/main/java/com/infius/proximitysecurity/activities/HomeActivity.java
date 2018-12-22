package com.infius.proximitysecurity.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.infius.proximitysecurity.R;
import com.infius.proximitysecurity.model.DrawerItem;
import com.infius.proximitysecurity.utilities.AppConstants;
import com.infius.proximitysecurity.utilities.ProfileUtils;
import com.infius.proximitysecurity.utilities.Utils;

import java.io.File;

public class HomeActivity extends BaseDrawerActivity {
    private ActionBar mActionBar;
    FrameLayout mContentFrame;
    TextView sendAlertButton, greetingMessage, propertyName;

    RelativeLayout scanGuest, scanStaff, upcomingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentFrame = (FrameLayout) findViewById(R.id.content_frame);
        mContentFrame.addView(getLayoutInflater().inflate(R.layout.activity_home, null));
        setupToolbar();

        initViews();
    }

    private void initViews() {
        sendAlertButton = (TextView) findViewById(R.id.send_alert_button);

        greetingMessage = (TextView) findViewById(R.id.greeting);
        propertyName = (TextView) findViewById(R.id.property_number_home);
        profilePic = (ImageView) findViewById(R.id.profile_pic_home);
        scanGuest = (RelativeLayout) findViewById(R.id.layout_guest_qr_scan);
        scanStaff = (RelativeLayout) findViewById(R.id.layout_staff_qr_scan);
        upcomingList = (RelativeLayout) findViewById(R.id.layout_upcoming_visitor);

        scanStaff.setOnClickListener(this);
        scanGuest.setOnClickListener(this);
        upcomingList.setOnClickListener(this);
        sendAlertButton.setOnClickListener(this);

        setData();
    }

    private void setupToolbar() {
        mActionBar = getSupportActionBar();
//        mActionBar.setDisplayHomeAsUpEnabled(true);
//        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.toolbar_layout);
    }

    @Override
    public void onItemClick(DrawerItem item) {
        if (item.getAction() == 1) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            prefs.edit().putBoolean(AppConstants.SP_IS_LOGGEDIN, false).commit();
            setProfilData();
            setData();
        }
    }

    public void setData() {
        super.setProfilData();
        if (Utils.isUserLoggedIn(HomeActivity.this)) {
            String userName = ProfileUtils.getUserName(HomeActivity.this);
            String ownerShipType = ProfileUtils.getOwnershipType(HomeActivity.this);
            String property = ProfileUtils.getPropertyName(HomeActivity.this);

            if (ProfileUtils.getProfileImageUrl(HomeActivity.this).equalsIgnoreCase("mock")) {
                profilePic.setVisibility(View.VISIBLE);
                profilePic.setImageResource(R.drawable.pic);
            }

            if (!TextUtils.isEmpty(userName)) {
                greetingMessage.setText(getString(R.string.greeting_message, userName));
            }

            if (!TextUtils.isEmpty(property)) {
                propertyName.setVisibility(View.VISIBLE);
                propertyName.setText(property + " (" + ownerShipType + ")");
            }

        } else {
            greetingMessage.setText(getString(R.string.greeting_message, ""));
            propertyName.setVisibility(View.GONE);
            profilePic.setVisibility(View.GONE);
        }
    }

    private Uri mFileUri;
    File mFile;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case AppConstants.REQUEST_CODE_LOGIN:
                if (resultCode == RESULT_OK) {
                   setData();
                }
                break;
            case IMAGE_FROM_GALLERY_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Toast.makeText(this, "Gallery Image Success", Toast.LENGTH_SHORT).show();
                         mFileUri = data.getData();
                         mFile = new File(getRealPathFromURI(mFileUri));
                         if(mFile != null && profilePic != null){
                             BitmapFactory.Options options = new BitmapFactory.Options();
                             Bitmap myBitmap = BitmapFactory.decodeFile(mFile.getAbsolutePath());

                             // down sizing image as it throws OutOfMemory Exception for larger
                             // images
                             options.inSampleSize = 8;

                             profilePic.setImageBitmap(myBitmap);
                         }

                        //launchUploadActivity();
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.layout_guest_qr_scan) {
            if (!Utils.isUserLoggedIn(HomeActivity.this)) {
                Utils.login(HomeActivity.this);
            } else {
                Intent intent = new Intent(HomeActivity.this, ScannerActivity.class);
                startActivity(intent);
            }
        } else if (view.getId() == R.id.layout_staff_qr_scan) {
            if (!Utils.isUserLoggedIn(HomeActivity.this)) {
                Utils.login(HomeActivity.this);
            } else {
                Intent intent = new Intent(HomeActivity.this, ScannerActivity.class);
                startActivity(intent);
            }
        } else if (view.getId() == R.id.layout_upcoming_visitor) {
            if (!Utils.isUserLoggedIn(HomeActivity.this)) {
                Utils.login(HomeActivity.this);
            } else {
                Intent intent = new Intent(HomeActivity.this, GuestListActivity.class);
                intent.putExtra(AppConstants.GUEST_LIST_TYPE, AppConstants.TYPE_HISTORY);
                startActivity(intent);
            }
        } else if (view.getId() == R.id.send_alert_button) {
            if (!Utils.isUserLoggedIn(HomeActivity.this)) {
                Utils.login(HomeActivity.this);
            }
        }
    }
    private static final int IMAGE_FROM_GALLERY_REQUEST_CODE = 0;
    public void uploadNewImage(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    // Explain to the user why we need to read the contacts
                }
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return;
            }
            else {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_FROM_GALLERY_REQUEST_CODE);
            }
        }
        else{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_FROM_GALLERY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_FROM_GALLERY_REQUEST_CODE);

            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    private String getRealPathFromURI(Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();
            cursor = this.getContentResolver().query(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);

            cursor.moveToFirst();
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            cursor.close();

            return path;
        }
        else
        {
            return uri.getPath();
        }
    }
}