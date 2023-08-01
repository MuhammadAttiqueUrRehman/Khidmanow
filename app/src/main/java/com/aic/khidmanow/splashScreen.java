package com.aic.khidmanow;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import layout.utils.LocalManager;

public class splashScreen extends Activity {
    private static int SPLASH_TIMEOUT = 3000;
    String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        uuid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//        LocalManager.setNewLocale(this, LocalManager.ARABIC);
//        HMConstants.alertheader = getString(R.string.app_title);
        new Handler().postDelayed(new Runnable() {
            //Showing splash screen with a timer, this will show the brand logo
            @Override
            public void run() {
                Intent i = new Intent(splashScreen.this, Main2Activity.class);
                startActivity(i);
                //close splash screen
                finish();
            }
        }, SPLASH_TIMEOUT);


    }
}
