package layout;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aic.khidmanow.AsyncResponse;
import com.aic.khidmanow.HMAppVariables;
import com.aic.khidmanow.HMConstants;
import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.HMDataAccess;
import com.aic.khidmanow.HMOwnException;
import com.aic.khidmanow.IntroManager;
import com.aic.khidmanow.MainActivity;
import com.aic.khidmanow.R;
import com.aic.khidmanow.Util;
//import com.facebook.AccessToken;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.GraphRequest;
//import com.facebook.GraphResponse;
//import com.facebook.Profile;
//import com.facebook.login.LoginManager;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class fr_login extends Fragment implements AsyncResponse {
//    LoginManager loginmanager;
//    LoginButton btnfblogin;
    Button btnlogin, btnforgotpin, btnregister, btnfirst;
    ImageButton btnloginclose;
    ProgressBar progressBar8;
    HMCoreData myDB;
    HMAppVariables appVariables = new HMAppVariables();
    Toolbar mtoolbar;
    TextView mTitle;
    EditText txtpin;
    com.bachors.prefixinput.EditText txtlogin;
    IntroManager intromanager;
  //  CallbackManager callbackmanager;
    private String mstrloginid, mstrpin, mstrfbid;
    private String sfirstname, slastname, semail, sfbid;
    List<String> permissionNeeds;
    View v;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("debugging Menu", "Loaded Menu");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        printKeyHash();
        MainActivity.bottomNavigationView.setVisibility(View.GONE);
       // callbackmanager = CallbackManager.Factory.create();
        intromanager = new IntroManager(getActivity());
        permissionNeeds = Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends");
      //  FacebookSdk.sdkInitialize(getActivity());
        try {
            myDB = new HMCoreData(getContext());
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return null;
        }
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fr_login, container, false);

        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.login);
        mtoolbar.setNavigationIcon(R.drawable.ico_cross);

      //  btnfblogin = (LoginButton) v.findViewById(R.id.btnfblogin);
        btnlogin = (Button) v.findViewById(R.id.btnlogin);
        btnforgotpin = (Button) v.findViewById(R.id.btnforgotpin);
        btnregister = (Button) v.findViewById(R.id.btnregister);
        btnfirst = (Button) v.findViewById(R.id.btnfirst);
        progressBar8 = (ProgressBar) v.findViewById(R.id.progressBar8);
        txtlogin = v.findViewById(R.id.txtlogin);
        txtpin = (EditText) v.findViewById(R.id.txtpin);
       // btnfblogin.setReadPermissions(permissionNeeds);
       // btnfblogin.setFragment(this);
        sfbid = "";
        if (intromanager.getFirst3()) {
            btnfirst.setVisibility(View.VISIBLE);
        }


        txtlogin.setText(intromanager.getMobile());
        Log.i("debugging", "FCM Key  " + intromanager.getFCMKey());

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intromanager.setFuncCallId("");
                btnfirst.setVisibility(View.GONE);
                if (intromanager.getFirst3()) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                } else {
                  /*  if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
                    {
                        Log.i("debugging", "First Time Key 3");
                        getActivity().getSupportFragmentManager().popBackStack();
                    } else {*/
                    Log.i("debugging", "First Time Key 4xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                    Fragment myFragment = new fr_homepage();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                    // }
                }
            }
        });

        btnfirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intromanager.setFuncCallId("");
                btnfirst.setVisibility(View.GONE);
                if (intromanager.getFirst3()) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                } else {
                  /*  if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
                    {
                        Log.i("debugging", "First Time Key 3");
                        getActivity().getSupportFragmentManager().popBackStack();
                    } else {*/
                    Log.i("debugging", "First Time Key 4xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                    Fragment myFragment = new fr_homepage();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                    // }
                }
            }
        });

        txtlogin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
              /*  if (hasFocus) {
                    txtlogin.setPrefix(HMConstants.mobilePrefix);
                } else {
                    if (txtlogin.length() < HMConstants.mobilePrefix.length() + 1)
                        txtlogin.setPrefix("");
                }*/
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtlogin.length() < 12) {
                    txtlogin.setError(getString(R.string.enter_mobile_number));
                    return;
                } else if (txtpin.length() != 4) {
                    txtpin.setError(getString(R.string.enter_4_digit_pin));
                    return;
                }
                //Call Login service
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                loginApp(txtlogin.getText().toString(), txtpin.getText().toString(), "", "");
            }
        });

        btnforgotpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtlogin.length() < 12) {
                    txtlogin.setError("Enter Mobile Number");
                    return;
                }

                intromanager.setMobile(txtlogin.getText().toString());
                appVariables.umobilenumber = txtlogin.getText().toString();
                try {
                    appVariables = myDB.updateUserData(appVariables);
                } catch (Exception e) {
                    e.printStackTrace();
                    myDB.showToast(getActivity(), e.getMessage());
                    return;
                }
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", HMConstants.appmerchantid);
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("mobile", txtlogin.getText());
                    jsonMain.put("emailid", "");
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.showToast(getActivity(), e.getMessage());
                    return;
                }
                String[] myTaskParams = {"/SSMSendEnrolmentToken", jsonParam.toString()};
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    Log.i("debugging", jsonParam.toString());
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar8, "forgotpin");
                    aasyncTask.delegate = (AsyncResponse) fr_login.this;
                    aasyncTask.execute(myTaskParams);
                } catch (Exception e) {
                    e.printStackTrace();
                    myDB.showToast(getActivity(), e.getMessage());
                    return;
                }
            }
        });

        btnregister.setOnClickListener(v -> {
            Fragment myFragment = new fr_register();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        });

//        btnfblogin.registerCallback(callbackmanager,
//                new FacebookCallback<LoginResult>() {
//
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        AccessToken accessToken = loginResult.getAccessToken();
//                        Profile profile = Profile.getCurrentProfile();
//                        // App code
//                        Log.i("debugging", "FB Login AccessToken " + accessToken.getToken());
//                        Log.i("debugging", "FB Login UserId " + loginResult.getAccessToken().getUserId());
//                        Log.i("debugging", "FB Login Status " + loginResult.toString());
//
//                        // Facebook Email address
//                        GraphRequest request = GraphRequest.newMeRequest(
//                                loginResult.getAccessToken(),
//                                new GraphRequest.GraphJSONObjectCallback() {
//                                    @Override
//                                    public void onCompleted(
//                                            JSONObject object,
//                                            GraphResponse response) {
//
//                                        try {
//                                            Log.i("LoginActivity Response ", response.toString());
//                                            Log.i("Email = ", " " + object.toString());
//                                            String s1 = object.getString("name");
//                                            String[] words = s1.split("\\s");//splits the string based on whitespace
//                                            sfirstname = words[0];
//                                            slastname = words[1];
//                                            semail = object.getString("email");
//                                            sfbid = object.getString("id");
//
//                                            //Check if user exists
//                                            JSONObject jsonParam = new JSONObject();
//                                            JSONObject jsonMain = new JSONObject();
//                                            try {
//                                                jsonMain.put("merchantcode", HMConstants.appmerchantid);
//                                                jsonMain.put("mdevice", intromanager.getDevice());
//                                                jsonMain.put("customer", object.getString("id"));
//                                                jsonParam.put("indata", jsonMain);
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                                myDB.showToast(getActivity(), e.getMessage());
//                                                return;
//                                            }
//                                            String[] myTaskParams = {"/SSMcheckFBUserExist", jsonParam.toString()};
//                                            //  new HMDataAccess().execute(myTaskParams);
//                                            try {
//                                                Log.i("debugging", jsonParam.toString());
//                                                HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar8, "validatefbexist");
//                                                aasyncTask.delegate = (AsyncResponse) fr_login.this;
//                                                aasyncTask.execute(myTaskParams);
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                                myDB.showToast(getActivity(), e.getMessage());
//                                                return;
//                                            }
//
//
////                                            Fragment myFragment = new fr_register_fb();
////                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
//
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                            myDB.showToast(getActivity(), e.getMessage());
//                                            return;
//                                        }
//
//
//                                    }
//                                });
//                        Bundle parameters = new Bundle();
//                        parameters.putString("fields", "id,name,email,gender, birthday");
//                        request.setParameters(parameters);
//                        request.executeAsync();
//
//
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // App code
//                        Log.i("debugging", "FB Login Cancelled");
//                        return;
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        // App code
//                        Log.i("debugging", "FB Login Error" + exception.getMessage());
//                        myDB.showToast(getActivity(), exception.getMessage());
//                        return;
//                    }
//                });
        return v;
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        callbackmanager.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        Log.i("debugging", output);
        Log.i("debugging", handle);
        //Write local SQL
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");

        Log.i("debugging", "login_output :" + output);
        output = new Util().processJsonForLanguage(output, getContext());

        Map<String, String> statuscode;
        String statuscodekey;
        String statusdesckey;
        statuscode = myDB.statusValues(output);
        statuscodekey = (String) statuscode.get("statuscode");
        statusdesckey = (String) statuscode.get("statusdesc");
        JSONObject myjson = new JSONObject(output);
        if (handle == "validateuser") {
            //Write local SQL

            //myDB.showToast(this, statusdesckey);
            if (!statuscodekey.toString().equals("000")) {
                if (sfbid.length() > 0) {
                   // loginmanager.getInstance().logOut();
                }
                myDB.showToast(getActivity(), statusdesckey);
                return;
            } else {
                Log.i("debugging Login Result", "Updating SQLLite");
                MainActivity.slogged = true;
                appVariables = myDB.getUserData();
                intromanager.setGender(myjson.getString("gender"));
                appVariables.applat = Double.parseDouble(myjson.getString("userlat"));
                appVariables.applon = Double.parseDouble(myjson.getString("userlon"));

                appVariables.ucity = myjson.getString("city");
                appVariables.ucountry = myjson.getString("country");
                appVariables.ucurrency = myjson.getString("currency");
                appVariables.ualcohol = myjson.getString("alcohol");
                appVariables.uareaname = myjson.getString("areaname");
                appVariables.uautolocation = myjson.getString("autolocation");
                appVariables.ubirthdate = myjson.getString("dateofbirth");
                appVariables.ucertificate = myjson.getString("certificate");
                appVariables.ucityname = myjson.getString("residentcityname");
                appVariables.ucountryname = myjson.getString("residentcountryname");
                appVariables.ucuspic = myjson.getString("imageurl");
                appVariables.ucusqr = myjson.getString("qrurl");
                appVariables.ucustomerid = myjson.getString("customerid");
//                Log.i("debugging", "id = " + appVariables.ucustomerid + "   --   " + myjson.getString("customerid"));
                appVariables.ucustomername = myjson.getString("customername");
                appVariables.udisplaybirthdate = myjson.getString("displaydateofbirth");
                appVariables.uemailid = myjson.getString("emailid");
                appVariables.ufbid = myjson.getString("fbid");
                appVariables.ufirstname = myjson.getString("firstname");
                appVariables.ufullname = myjson.getString("fullname");
                appVariables.uhomecountry = myjson.getString("homecountry");
                appVariables.uhomecountryname = myjson.getString("homecountryname");
                appVariables.uinitdate = myjson.getString("initdate");
                appVariables.ulastname = myjson.getString("lastname");
                appVariables.ulocationname = myjson.getString("locationname");
                appVariables.ulogged = true;
                appVariables.umagicnumber = myjson.getString("magicnumber");
                appVariables.umaxspend = myjson.getDouble("maxspend");
                appVariables.umemberexpiry = myjson.getString("memberexpiry");
                appVariables.umobilenumber = myjson.getString("mobilenumber");
                appVariables.unationalitycode = myjson.getString("nationality");
                appVariables.upinnumber = myjson.getString("pinnumber");
                appVariables.upointvalue = myjson.getDouble("pointvalue");
                appVariables.upushoffer = myjson.getString("pushoffer");
                appVariables.uremindexpiry = myjson.getString("remindexpiry");
                appVariables.uresidentcity = myjson.getString("residentcity");
                appVariables.uresidentcountry = myjson.getString("homecountry");
                appVariables.uresidentcountryname = myjson.getString("residentcountryname");
                appVariables.usegmentcode = myjson.getString("segmentcode");
                appVariables.usegmentimage = myjson.getString("segmentimage");
                appVariables.usegmentname = myjson.getString("segmentname");
                appVariables.ushowprofile = myjson.getString("showprofile");
                appVariables.uspend = myjson.getDouble("spend");
                appVariables.sendNotification = myjson.getString("sendnotification");
                appVariables.sendMailer = myjson.getString("sendmailer");
                appVariables.sendSMS = myjson.getString("sendsms");
                intromanager.setMobile("+" + myjson.getString("mobilenumber"));
                appVariables = myDB.updateUserData(appVariables);
            }

            Log.i("debugging", "ImageURL " + myjson.getString("imageurl"));

            if (intromanager.getBookTime().equals("")) {
                MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Fragment myFragment = new fr_homepage();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            } else {
                //   getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Fragment myFragment = new fr_book_time();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        } else if (handle == "forgotpin") {

            if (!statuscodekey.toString().equals("000")) {
                myDB.showToast(getActivity(), statusdesckey);
                return;
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("isnew", "N");
                bundle.putString("tokenref", myjson.getString("referencenumber"));
                MainActivity.customermobile = txtlogin.getText().toString();
                Fragment myFragment = new fr_validation();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        } else if (handle == "validatefbexist") {

            if (statuscodekey.toString().equals("060")) {
                //   v.setVisibility(View.INVISIBLE);
                loginApp("", "", sfbid, "FB");

            } else if (statuscodekey.toString().equals("000")) {
                Bundle bundle = new Bundle();
                bundle.putString("sfirstname", sfirstname);
                bundle.putString("slastname", slastname);
                bundle.putString("semail", semail);
                bundle.putString("sfbid", sfbid);
                Log.i("debugging", "FB Data" + sfirstname + " " + slastname + " " + semail + " " + sfbid);
                Fragment myFragment = new fr_register_fb();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();

            } else {
                myDB.showToast(getActivity(), statusdesckey);
                return;
            }
        }
    }

    private void printKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo("com.aic.khidmanow", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.i("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.i("KeyHash:", e.toString());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        int id = items.getItemId();
        if (id == android.R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }

    private void loginApp(String loginid, String password, String fbid, String loginmode) {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();

        try {
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("reference", intromanager.getFCMKey());
            jsonMain.put("customer", loginid);
            jsonMain.put("password", password);
            jsonMain.put("fbuserid", fbid);
            jsonMain.put("loginmode", loginmode);
            jsonMain.put("fbaccesstoken", "");
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
        String[] myTaskParams = {"/SSMvalidateUser", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        Log.i("debugging", "Login Parameters " + jsonParam.toString());
        try {
            HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar8, "validateuser");
            aasyncTask.delegate = (AsyncResponse) fr_login.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
    }
}
