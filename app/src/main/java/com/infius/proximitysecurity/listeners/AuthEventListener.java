package com.infius.proximitysecurity.listeners;

public interface AuthEventListener {
    void onSignupClicked();
    void onLoginClicked();
    void onForgotPasswordClicked();
    void onChangePasswordClicked();
    void onLoginSuccess();
}
