package com.infius.proximitysecurity.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infius.proximitysecurity.R;
import com.infius.proximitysecurity.adapters.DrawerAdapter;
import com.infius.proximitysecurity.listeners.DrawerItemActionListener;
import com.infius.proximitysecurity.model.DrawerItem;
import com.infius.proximitysecurity.utilities.ProfileUtils;
import com.infius.proximitysecurity.utilities.Utils;

import java.util.ArrayList;

public abstract class BaseDrawerActivity extends AppCompatActivity implements View.OnClickListener, DrawerItemActionListener {
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolBar;
    private String mTitle;
    private String mDrawerTitle;
    private LinearLayout loginView;
    private RelativeLayout profileView;
    TextView btnLogin, profileName, property;
    ImageView profilePic;
    ImageView editProfile;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_drawer_layout);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_menu);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        // Set the adapter for the list view
        mDrawerList.setAdapter(createDrawerAdapter());

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mTitle = mDrawerTitle = getTitle().toString();
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,
                mToolBar,/* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        initView();
    }

    private void initView() {
        profileView = (RelativeLayout)findViewById(R.id.profile_view);
        loginView = (LinearLayout)findViewById(R.id.login_view);

        profilePic = (ImageView) findViewById(R.id.profile_pic);
        editProfile = (ImageView) findViewById(R.id.edit_profile_btn);
        property = (TextView) findViewById(R.id.property_number_drawer);
        profileName = (TextView) findViewById(R.id.profile_name);

        btnLogin = (TextView) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        profileView.setOnClickListener(this);
        setProfilData();
    }

    public void setProfilData() {
        if (Utils.isUserLoggedIn(this)) {
            profileView.setVisibility(View.VISIBLE);
            loginView.setVisibility(View.GONE);
            setUserData();
        } else {
            findViewById(R.id.login_view).setVisibility(View.VISIBLE);
            findViewById(R.id.profile_view).setVisibility(View.GONE);
        }
    }

    private void setUserData() {
        String userName = ProfileUtils.getUserName(BaseDrawerActivity.this);
        String ownerShipType = ProfileUtils.getOwnershipType(BaseDrawerActivity.this);
        String propertyName = ProfileUtils.getPropertyName(BaseDrawerActivity.this);

        if (ProfileUtils.getProfileImageUrl(BaseDrawerActivity.this).equalsIgnoreCase("mock")) {
            profilePic.setImageResource(R.drawable.pic);
        }

        if (!TextUtils.isEmpty(userName)) {
            profileName.setText(userName);
        }

        if (!TextUtils.isEmpty(propertyName)) {
            property.setVisibility(View.VISIBLE);
            property.setText(propertyName + " (" + ownerShipType +  ")");
        }
    }

    private DrawerAdapter createDrawerAdapter() {
        ArrayList<DrawerItem> list = new ArrayList<>();

        DrawerItem maintenance = new DrawerItem();
        maintenance.setIcon(R.drawable.ic_maintenance);
        maintenance.setItemName(getString(R.string.maintenance));
        list.add(maintenance);

        DrawerItem notifSettings = new DrawerItem();
        notifSettings.setIcon(R.drawable.ic_maintenance);
        notifSettings.setItemName(getString(R.string.notif_settings));
        list.add(notifSettings);

        DrawerItem changePassword = new DrawerItem();
        changePassword.setIcon(R.drawable.ic_change_password);
        changePassword.setItemName(getString(R.string.change_password));
        list.add(changePassword);

        DrawerItem tnc = new DrawerItem();
        tnc.setIcon(R.drawable.ic_term_condition);
        tnc.setItemName(getString(R.string.tnc));
        list.add(tnc);

        DrawerItem privacyPolicy = new DrawerItem();
        privacyPolicy.setIcon(R.drawable.ic_privacy_policy);
        privacyPolicy.setItemName(getString(R.string.privacy_policy));
        list.add(privacyPolicy);

        DrawerItem contactus = new DrawerItem();
        contactus.setIcon(R.drawable.ic_contact);
        contactus.setItemName(getString(R.string.contact_us));
        list.add(contactus);

        DrawerItem logout = new DrawerItem();
        logout.setIcon(R.drawable.ic_logout);
        logout.setItemName(getString(R.string.logout));
        logout.setAction(1);
        list.add(logout);


        DrawerAdapter drawerAdapter = new DrawerAdapter(this, R.layout.drawer_list_item, list, this);
        return drawerAdapter;
    }


    @Override
    public void onClick(View view) {
        closeDrawer();
        if (view.getId() == R.id.btn_login) {
            Utils.login(BaseDrawerActivity.this);
        } else if (view.getId() == R.id.profile_view) {

        }
    }

    private void closeDrawer() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

}
