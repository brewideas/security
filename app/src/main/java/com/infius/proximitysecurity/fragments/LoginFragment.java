package com.infius.proximitysecurity.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.infius.proximitysecurity.R;
import com.infius.proximitysecurity.listeners.AuthEventListener;


public class LoginFragment extends Fragment implements View.OnClickListener {

    AuthEventListener listener;
    TextView login, forgotPassword, signup, mockLogin;
    ImageView displayPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_fragment, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        login = (TextView) rootView.findViewById(R.id.btn_login);
        forgotPassword = (TextView) rootView.findViewById(R.id.forgot_password);
        signup = (TextView) rootView.findViewById(R.id.sign_up);
        displayPassword = (ImageView) rootView.findViewById(R.id.eye);
        mockLogin = (TextView) rootView.findViewById(R.id.mock_login);

        login.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signup.setOnClickListener(this);
        displayPassword.setOnClickListener(this);
        mockLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
            loginApiCall();
        } else if (view.getId() == R.id.forgot_password) {
            listener.onForgotPasswordClicked();
        } else if (view.getId() == R.id.sign_up) {
            listener.onSignupClicked();
        } else if (view.getId() == R.id.eye) {

        } else if (view.getId() == R.id.mock_login) {
            listener.onLoginSuccess();
        }
    }

    private void loginApiCall() {

    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        listener = (AuthEventListener) activity;
    }
}
