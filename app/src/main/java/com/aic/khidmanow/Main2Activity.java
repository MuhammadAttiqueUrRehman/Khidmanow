package com.aic.khidmanow;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import layout.fr_address_alert;
import layout.fr_walkthrough_slide;
import layout.utils.BaseActivity;

//public class Main2Activity extends AppCompatActivity {
public class Main2Activity extends BaseActivity {
    private Fragment fragment;
    private IntroManager intomanager;
    private HMAppVariables appVariables;
    private HMCoreData myDB;
    private IntroManager intromanager;

    public void showFragment(Fragment fr) {
        Fragment myFragment = fr;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place, myFragment).addToBackStack(null).commitAllowingStateLoss();
    }


    //  private long lastPressedTime;
    //   private static final int PERIOD = 2000;

    /*  @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {
         if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
             switch (event.getAction()) {
                 case KeyEvent.ACTION_DOWN:
                     if (event.getDownTime() - lastPressedTime < PERIOD) {
                         finish();
                     } else {
                         myDB.showToast(getApplicationContext(), "Press again to exit.");
                         lastPressedTime = event.getEventTime();
                     }
                     return true;
             }
         }
         return false;
     }*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intomanager = new IntroManager(this);
        appVariables = new HMAppVariables();
        myDB = new HMCoreData(this);
        String uuid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        String xuuid = HMConstants.deviceplatform + "^" + HMConstants.deviceversion + "^null^" + intomanager.getLatLon() + "^" + HMConstants.screenHeight + "^" + HMConstants.screenWidth + "^" + uuid + "^" + HMConstants.devicemodel + "^" + HMConstants.devicemanufacturer + "^";
        intomanager.setDevice(xuuid);

        boolean show_sloder = getIntent().getBooleanExtra("show_sloder", false);

        setContentView(R.layout.activity_main2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //  ProgressBar progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        FirebaseMessaging.getInstance().subscribeToTopic(HMConstants.alertheader);
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i("debugging", "Token " + token);
        intromanager = new IntroManager(this);
        intromanager.setFCMKey(token);
        intomanager.setProfile(false);
        intomanager.setAddressType("");
//        intomanager.setFirst1(true);
        if (intomanager.getFirst1() || show_sloder) {

            try {
                appVariables = myDB.updateUserData(appVariables);
            } catch (Exception e) {
                e.printStackTrace();
                myDB.showToast(this, e.getMessage());
                return;
            }
            intomanager.setFirst1(false);
            fragment = new fr_walkthrough_slide();
            showFragment(fragment);
        } else if (intomanager.getFirst2() || intomanager.getLocation().equals("") || intomanager.getLocation().equals("nnn")) {

            if (!isLocationEnabled(this)) {
                statusCheck();
            }
            intomanager.setFirst2(false);
            fragment = new fr_address_alert();
            showFragment(fragment);
        } else {
            intomanager.setFuncCallId("");
            Intent intent = new Intent(Main2Activity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.your_device_location)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
