package com.example.croop.singleton;

import com.example.croop.model.PhoneVerification;

public class PhoneAuthenticationSimpleton {
    private static PhoneAuthenticationSimpleton instance;
    private PhoneVerification phoneVerification;

    private PhoneAuthenticationSimpleton() {}

    public static synchronized PhoneAuthenticationSimpleton getInstance() {
        if (instance == null) {
            instance = new PhoneAuthenticationSimpleton();
        }
        return instance;
    }

    public PhoneVerification getPhoneVerification() {
        return phoneVerification;
    }

    public void setPhoneVerification(PhoneVerification phoneVerification) {
        this.phoneVerification = phoneVerification;
    }
}

