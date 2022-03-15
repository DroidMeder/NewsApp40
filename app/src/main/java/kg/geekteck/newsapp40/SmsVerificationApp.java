package kg.geekteck.newsapp40;

import android.app.Application;

import kg.geekteck.newsapp40.helper.AppSignatureHelper;

public class SmsVerificationApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
        //appSignatureHelper.getAppSignatures();
    }
}
