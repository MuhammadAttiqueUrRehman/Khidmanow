package com.aic.khidmanow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.provider.Settings;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

import layout.fr_account;
import layout.fr_homepage;
import layout.fr_login;
import layout.fr_myorders;
import layout.fr_offers;
import layout.fr_servicedetail;
import layout.fr_vendorreview;
import layout.utils.BaseActivity;

//public class MainActivity extends AppCompatActivity implements AsyncResponse {
public class MainActivity extends BaseActivity implements AsyncResponse {
    public static String firsttime = "", firstOutput = "", customermobile = "", locaddress = "", deviceid = "", changeaddress = "";
    public static boolean firstrun = true;
    Fragment myFragment = new Fragment();
    private IntroManager intromanager;
    public static Boolean slogged = false;
    public static double slat = 0, slon = 0;
    public String uuid, xuuid, statuscodekey, statusdesckey;
    public Map statuscode;
    private HMCoreData myDB;
    private HMAppVariables appVariables;
    public static TextView textbadge1;
    public static BottomNavigationView bottomNavigationView;
    ProgressBar progresBar2;

    //  private long lastPressedTime;
    //  private static final int PERIOD = 2000;

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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fr_blank);
        progresBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        try {
            myDB = new HMCoreData(this);
            appVariables = myDB.getUserData();
            MainActivity.slogged = appVariables.ulogged;
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(this, e.getMessage());
            return;
        }
        FirebaseMessaging.getInstance().subscribeToTopic(HMConstants.alertheader);
        String token = FirebaseInstanceId.getInstance().getToken();

        Log.i("debugging", "Token " + token);
        intromanager = new IntroManager(this);
        if (!intromanager.getFCMKey().equals(token)) {
            Log.i("debugging", "Token " + token);
            intromanager.setFCMKey(token);
            if (appVariables.ucustomerid.length() > 0) {
                sendRegistrationToServer(token);
            }
            Log.i("debugging", "Token " + intromanager.getFCMKey());
        }
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        bottomNavigationView = findViewById(R.id.footerx);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        if (firsttime.length() == 0) {
            uuid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            intromanager.setNewDevice(uuid);
            xuuid = HMConstants.deviceplatform + "^" + HMConstants.deviceversion + "^null^" + intromanager.getLatLon() + "^" + HMConstants.screenHeight + "^" + HMConstants.screenWidth + "^" + uuid + "^" + HMConstants.devicemodel + "^" + HMConstants.devicemanufacturer + "^";
            intromanager.setDevice(xuuid);
            JSONObject jsonParam = new JSONObject();
            JSONObject jsonMain = new JSONObject();
            try {
                jsonMain.put("mdevice", intromanager.getDevice());
                jsonMain.put("merchantcode", HMConstants.appmerchantid);
                jsonMain.put("geolocation", intromanager.getLatLon());
                jsonParam.put("indata", jsonMain);
            } catch (JSONException e) {
                e.printStackTrace();
                myDB.showToast(this, e.getMessage());
                return;
            }
            String[] myTaskParams = {"/SSMInitialize", jsonParam.toString()};
            Log.i("Debugging", "Initialize :" + jsonParam.toString());
            try {
                HMDataAccess aasyncTask = new HMDataAccess(this, progresBar2, "SSMInitialize");
                aasyncTask.delegate = (AsyncResponse) this;
                aasyncTask.execute(myTaskParams);
            } catch (Exception e) {
                e.printStackTrace();
                myDB.showToast(this, e.getMessage());
                return;
            }

        } else {
            intromanager.setFuncCallId("");
            progresBar2.setVisibility(View.GONE);
            myFragment = new fr_homepage();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        }


        // checkForUpdates();


    }


    /*
        @Override
        public void onResume() {
            super.onResume();
            // ... your own onResume implementation
            checkForCrashes();
        }

        @Override
        public void onPause() {
            super.onPause();
            unregisterManagers();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            unregisterManagers();
        }

        private void checkForCrashes() {
            CrashManager.register(this);
        }

        private void checkForUpdates() {
            // Remove this for store builds!
            UpdateManager.register(this);
        }

        private void unregisterManagers() {
            UpdateManager.unregister();
        }
    */
    public static void showErrorPage(String error) {
        return;
    }

    private void addReferrer() {
        intromanager.setFirstTime(false);
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("reference", intromanager.getReferDevice());
            jsonMain.put("mobile", intromanager.getReferMobile().trim());
            jsonMain.put("customer", intromanager.getNewDevice());
            jsonMain.put("additionalinfo", HMConstants.faqType);
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(this, e.getMessage());
        }
        Log.i("Debugging", "Referral Data :" + jsonParam.toString());
        String[] myTaskParams = {"/SSMUpdateReferrer", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this, progresBar2, "SSMUpdateReferrer");
            aasyncTask.delegate = (AsyncResponse) this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(this, e.getMessage());
            return;
        }
    }


    private void sendRegistrationToServer(String refreshedToken) {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("customer", appVariables.ucustomerid);
            jsonMain.put("certificate", appVariables.ucertificate);
            jsonMain.put("reference", refreshedToken);
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(this, e.getMessage());
        }
        Log.i("Debugging", "Refresh key :" + jsonParam.toString());
        String[] myTaskParams = {"/SSMUpdateFCMKey", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this, progresBar2, "SSMUpdateFCMKey");
            aasyncTask.delegate = this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(this, e.getMessage());
            return;
        }
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        Log.i("Debugging", output + " //n Handle: " + handle);
//        output = new Util().processJsonForLanguage(output, this);
        JSONObject jObj = new JSONObject(output);
        if (!jObj.getString("statuscode").equals("000")) {
            myDB.showToast(this, statusdesckey);
            return;
        }
        if (handle.equals("SSMUpdateFCMKey")) {
            return;
        } else if (handle.equals("SSMUpdateReferrer")) {

            if (intromanager.getFirst3()) {
                progresBar2.setVisibility(View.INVISIBLE);
                myFragment = new fr_login();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            } else {
                progresBar2.setVisibility(View.INVISIBLE);
                intromanager.setFuncCallId("");
                myFragment = new fr_homepage();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                return;
            }
        } else if (handle.equals("SSMInitialize")) {
            intromanager.setHomeOP(output);
            intromanager.setSharingText(jObj.getString("sharetext"));
            intromanager.setSharingURL(jObj.getString("sharepagelink"));
            intromanager.setSharingLink(jObj.getString("sharelink"));
            intromanager.setVisitingCharges(jObj.getString("visitingcharge"));
            intromanager.setAppStoreURL(jObj.getString("appstorecustomerandroid"));
            intromanager.setIOSStoreURL(jObj.getString("appstorecustomerios"));
            intromanager.setSupportEmail(jObj.getString("jcareemail"));
            intromanager.setSupportCall(jObj.getString("jcarecall"));
            appVariables.udevicecomboined = intromanager.getDevice();
            appVariables.appmerchantid = jObj.getString("merchantcode");
            appVariables.appurl = HMConstants.appurl;
            appVariables.ucity = intromanager.getCity();
            appVariables.ucityname = intromanager.getCityName();
            appVariables.ucountryname = jObj.getString("countryname");
            appVariables.ucountry = jObj.getString("countrycode");
            try {
                appVariables = myDB.updateUserData(appVariables);
            } catch (Exception e) {
                e.printStackTrace();
                myDB.showToast(this, e.getMessage());
                Log.i("Debugging", e.getMessage());
                return;
            }
            //      }

            MainActivity.firsttime = "1";
            FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(getIntent())
                    .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                        @Override
                        public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                            // Get deep link from result (may be null if no link is found)
                            Uri deepLink = null;
                            if (pendingDynamicLinkData != null) {
                                deepLink = pendingDynamicLinkData.getLink();
                            }
                            if (deepLink != null && deepLink.getQueryParameter("mobile") != null && deepLink.getQueryParameter("referdevice") != null && deepLink.getQueryParameter("linktype") != null && deepLink.getQueryParameter("productcode") != null) {
                                Log.i("Debugging", "Deep link " + deepLink.toString());
                                intromanager.setReferMobile(deepLink.getQueryParameter("mobile"));
                                intromanager.setReferDevice(deepLink.getQueryParameter("referdevice"));

                                //Check if first time
                                if (intromanager.getFirstTime() && deepLink.getQueryParameter("linktype").equals("DOWNLOAD")) {
                                    addReferrer();
                                } else if (deepLink.getQueryParameter("linktype").equals("OFFER")) {
                                    Fragment myFragment = new fr_offers();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                                    return;
                                } else if (deepLink.getQueryParameter("linktype").equals("PRODUCT")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("itemcode", deepLink.getQueryParameter("productcode"));
                                    Fragment myFragment = new fr_servicedetail();
                                    myFragment.setArguments(bundle);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                                } else if (deepLink.getQueryParameter("linktype").equals("REGISTER")) {
                                    Fragment myFragment = new fr_login();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                                    return;
                                }
                            } else if (intromanager.getFirst3()) {
                                myFragment = new fr_login();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                            } else {
                                Date date = new Date();
                                Log.i("Debugging", "Going Straight to Homepage " + date.toString());
                                intromanager.setFuncCallId("");
                                progresBar2.setVisibility(View.INVISIBLE);
                                myFragment = new fr_homepage();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                            }
                        }
                    });


        }
    }

  /* public void setToolBar(int toolBarId, String titleText, int textTitleId) {
        Toolbar toolbar = (Toolbar) findViewById(toolBarId);
        TextView mTitle = (TextView) toolbar.findViewById(textTitleId);
        setSupportActionBar(toolbar);
        mTitle.setText(titleText);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }*/

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    int id = menuItem.getItemId();
                    switch (id) {

                        case R.id.nav_home:
                            intromanager.setFuncCallId("");

                            myFragment = new fr_homepage();
                            break;
                        case R.id.nav_offer:
                            myFragment = new fr_offers();
                            break;
                        case R.id.nav_order:
                            if (!MainActivity.slogged) {
                                intromanager.setFuncCallId("fr_myorders");
                                myFragment = new fr_login();
                                break;
                            } else {
                                myFragment = new fr_myorders();
                                break;
                            }
                        case R.id.nav_items:
//                            myFragment = new fr_itemlist();
                            myFragment = new fr_vendorreview();
                            break;
                       /*     if (!MainActivity.slogged) {
                                intromanager.setFuncCallId("fr_vendorreview");
                                myFragment = new fr_login();
                                break;
                            } else {
                                myFragment = new fr_vendorreview();
                                break;
                            }*/
                        case R.id.nav_user:
                            myFragment = new fr_account();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                    return true;
                }
            };
}