package com.infius.proximitysecurity.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infius.proximitysecurity.R;
import com.infius.proximitysecurity.model.DrawerItem;
import com.infius.proximitysecurity.utilities.AppConstants;
import com.infius.proximitysecurity.utilities.ProfileUtils;
import com.infius.proximitysecurity.utilities.Utils;


public class HomeActivity extends BaseDrawerActivity {
    private ActionBar mActionBar;
    FrameLayout mContentFrame;
    TextView sendAlertButton, greetingMessage, propertyName;
    RelativeLayout scanGuest, scanStaff, upcomingList;
    public static final int REQUEST_CROPPED_IMAGE = 300;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("HomeActivity","requestCode= " + requestCode);
        switch(requestCode){
            case AppConstants.REQUEST_CODE_LOGIN:
                if (resultCode == RESULT_OK) {
                   setData();
                }
                break;
            case REQUEST_CROPPED_IMAGE:
                Bitmap bitmap = (Bitmap) data.getParcelableExtra("BitmapImage");
                profilePic.setImageBitmap(bitmap);
                break;
            }//switch
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

    public void uploadNewImage(View view) {
        Intent cropImageIntent = new Intent(this,cropImage.class);
        startActivityForResult(cropImageIntent,REQUEST_CROPPED_IMAGE);

    }
}