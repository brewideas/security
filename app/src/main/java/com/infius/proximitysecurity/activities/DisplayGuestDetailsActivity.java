package com.infius.proximitysecurity.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.infius.proximitysecurity.R;
import com.infius.proximitysecurity.model.GuestVisit;
import com.infius.proximitysecurity.model.QRInfoResponse;
import com.infius.proximitysecurity.utilities.AppConstants;

public class DisplayGuestDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputEditText countGuest;
    TextInputEditText guestName;
    TextInputEditText guestPhoneNo;
    TextInputEditText guestEmail;
    TextInputEditText dateOfArrival;
    TextInputEditText inTime;
    TextInputEditText outTime;
    ImageView backBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_user_details);
        initViews();
        setListeners();
        setData();
    }

    private void setData() {
        if (getIntent() != null && getIntent().hasExtra(AppConstants.QR_CODE_DATA)) {
            QRInfoResponse qrInfoResponse = (QRInfoResponse) getIntent().getSerializableExtra(AppConstants.QR_CODE_DATA);
            GuestVisit visit = qrInfoResponse.getGuestsVisit();
            guestName.setText(visit.getName());
            guestPhoneNo.setText(visit.getMobile());
            guestEmail.setText(visit.getEmail());
            dateOfArrival.setText(visit.getExpectedIn());
            inTime.setText(visit.getExpectedIn());
            outTime.setText(visit.getExpectedOut());
        }
    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
    }

    private void initViews() {
        countGuest = (TextInputEditText) findViewById(R.id.edit_guest_count);
        guestName = (TextInputEditText) findViewById(R.id.edit_prim_guest_name);
        guestPhoneNo = (TextInputEditText) findViewById(R.id.edit_prim_guest_phone);
        guestEmail = (TextInputEditText) findViewById(R.id.edit_prim_guest_email);
        dateOfArrival = (TextInputEditText) findViewById(R.id.edit_expected_date);
        inTime = (TextInputEditText) findViewById(R.id.edit_in_time);
        outTime = (TextInputEditText) findViewById(R.id.edit_out_time);
        backBtn = (ImageView) findViewById(R.id.back_btn);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_btn) {
            onBackPressed();
        }
    }
}
