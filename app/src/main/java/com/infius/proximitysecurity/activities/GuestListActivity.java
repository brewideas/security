package com.infius.proximitysecurity.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.infius.proximitysecurity.R;
import com.infius.proximitysecurity.adapters.PrimaryGuestAdapter;
import com.infius.proximitysecurity.model.DataModel;
import com.infius.proximitysecurity.model.Guest;
import com.infius.proximitysecurity.model.GuestHistoryModel;
import com.infius.proximitysecurity.utilities.ApiRequestHelper;
import com.infius.proximitysecurity.utilities.AppConstants;
import com.infius.proximitysecurity.utilities.Utils;

public class GuestListActivity extends AppCompatActivity implements View.OnClickListener {

    int mType;
    private Dialog mProgressDialog;
    private ImageView backBtn, emptyList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_list);
        if (getIntent() != null && getIntent().hasExtra(AppConstants.GUEST_LIST_TYPE)) {
            mType = getIntent().getIntExtra(AppConstants.GUEST_LIST_TYPE, AppConstants.TYPE_HISTORY);
        }
        initViews();
        fetchGuestList();
    }

    private void initViews() {
        backBtn = (ImageView) findViewById(R.id.back_btn);
        recyclerView = (RecyclerView) findViewById(R.id.guest_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(GuestListActivity.this));
        emptyList = (ImageView) findViewById(R.id.empty_list_illustration);
        backBtn.setOnClickListener(this);
    }

    private void fetchGuestList() {
        showProgressDialog();
        ApiRequestHelper.requestGuestList(this, getParamName(), new Response.Listener<DataModel>() {
            @Override
            public void onResponse(DataModel response) {
                boolean error = false;
                if (response instanceof GuestHistoryModel) {
                    hideProgressDialog();
                    if (AppConstants.STATUS_SUCCESS.equalsIgnoreCase(((GuestHistoryModel) response).getStatus())) {
                        setData((GuestHistoryModel) response);
                    } else {
                        error = true;
                    }
                } else {
                    error = true;
                }
                if (error) {
                    emptyList.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                emptyList.setVisibility(View.VISIBLE);
                hideProgressDialog();
                if (error != null) {
                    Toast.makeText(GuestListActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setData(GuestHistoryModel response) {
        if (response.getGuestsList().size() > 0) {
            PrimaryGuestAdapter adapter = new PrimaryGuestAdapter(GuestListActivity.this, response.getGuestsList());
            recyclerView.setAdapter(adapter);
        } else {
            emptyList.setVisibility(View.VISIBLE);
        }
    }

    private String getParamName() {
        if (mType == AppConstants.TYPE_HISTORY){
            return AppConstants.GUEST_LIST_PARAM_HISTORY;
        } else if (mType == AppConstants.TYPE_PREFERRED){
            return AppConstants.GUEST_LIST_PARAM_PREFERRED;
        } else if (mType == AppConstants.TYPE_PRESENT){
            return AppConstants.GUEST_LIST_PARAM_PRESENT;
        } else if (mType == AppConstants.TYPE_UPCOMING){
            return AppConstants.GUEST_LIST_PARAM_UPCOMING;
        }
        return null;
    }

    protected void showProgressDialog() {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = Utils.getProgressDialog(this);
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_btn) {
            GuestListActivity.this.finish();
        }
    }
}
