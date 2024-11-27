package com.example.croop.model;

public class PhoneVerification {
    private String verificationId;

    public PhoneVerification(){

    }

    public PhoneVerification(String verificationId){
        this.verificationId = verificationId;
    }

    public String getVerificationId(){
        return verificationId;
    }

    public void setVerificationId(String verificationId){
        this.verificationId = verificationId;
    }

}
