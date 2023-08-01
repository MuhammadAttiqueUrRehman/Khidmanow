package layout;

import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.aic.khidmanow.R;
import com.aic.khidmanow.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import layout.utils.LocalManager;


public class fr_validation extends Fragment implements AsyncResponse {
    private String strtext, strisnew, strfirstname, strlastname, strmobile, stremail, strpin, strfbid;
    private TextView tv_timer;
    private EditText txtcode;
    private Button btnvalidate;
    private Button btnresendcode;
    private ImageButton btnloginclose;
    private ProgressBar progressBar9;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    private TextView mTitle;
    IntroManager intromanager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        intromanager = new IntroManager(getActivity());
        myDB = new HMCoreData(getContext());
        try {
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return null;
        }
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fr_validation, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.otp_validation);
        mtoolbar.setNavigationIcon(R.drawable.ico_cross);
        btnvalidate = (Button) v.findViewById(R.id.btnvalidate);
        btnresendcode = (Button) v.findViewById(R.id.btnresendcode);
        tv_timer = v.findViewById(R.id.tv_timer);
        txtcode = (EditText) v.findViewById(R.id.txtvalidationcode);
        progressBar9 = (ProgressBar) v.findViewById(R.id.progressBar9);
        strtext = getArguments().getString("tokenref");
        strisnew = getArguments().getString("isnew");
        strfirstname = getArguments().getString("bfirstname");
        strlastname = getArguments().getString("blastname");
        strmobile = getArguments().getString("bmobile");
        stremail = getArguments().getString("bemail");
        strpin = getArguments().getString("bpin");
        strfbid = getArguments().getString("bfbid");
        Log.i("Debugging", "Token Reference :" + strtext);


        btnvalidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtcode.length() == 0) {
                    myDB.showToast(getContext(), getString(R.string.please_enter_the_6_digit));
                    return;
                }
                if (!strisnew.equals("Y")) {
                    JSONObject jsonParam = new JSONObject();
                    JSONObject jsonMain = new JSONObject();
                    try {
                        jsonMain.put("merchantcode", HMConstants.appmerchantid);
                        jsonMain.put("mdevice", intromanager.getDevice());
                        jsonMain.put("reference", strtext);
                        jsonMain.put("idnumber", txtcode.getText());
                        jsonParam.put("indata", jsonMain);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        myDB.showToast(getActivity(), e.getMessage());
                        return;
                    }
                    String[] myTaskParams = {"/SSMValidateToken", jsonParam.toString()};
                    //  new HMDataAccess().execute(myTaskParams);
                    try {
                        Log.i("Debugging", jsonParam.toString());
                        HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar9, "forgotpin");
                        aasyncTask.delegate = (AsyncResponse) fr_validation.this;
                        aasyncTask.execute(myTaskParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                        myDB.showToast(getActivity(), e.getMessage());
                        return;
                    }
                } else {
                    enrolcustomer();
                }
            }
        });

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intromanager.setFuncCallId("");
                Fragment myFragment = new fr_homepage();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btnresendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //myDB.showToast(requireContext(), "clicked");
                tv_timer.setVisibility(View.VISIBLE);
                btnresendcode.setVisibility(View.GONE);
                new CountDownTimer(180000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        tv_timer.setText(getString(R.string.resend_otp_in_seconds1, millisUntilFinished / 1000));
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
//                mTextField.setText("done!");
                        tv_timer.setVisibility(View.GONE);
                        btnresendcode.setVisibility(View.VISIBLE);
                    }

                }.start();
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", HMConstants.appmerchantid);
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("mobile", (!strisnew.equals("Y")) ? intromanager.getMobile() : strmobile);
                    jsonMain.put("email", (!strisnew.equals("Y")) ? "" : stremail);
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.showToast(getActivity(), e.getMessage());
                    return;
                }
                String[] myTaskParams = {"/SSMSendEnrolmentToken", jsonParam.toString()};
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    Log.i("Debugging", jsonParam.toString());
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar9, "codevalidation");
                    aasyncTask.delegate = (AsyncResponse) fr_validation.this;
                    aasyncTask.execute(myTaskParams);
                } catch (Exception e) {
                    e.printStackTrace();
                    myDB.showToast(getActivity(), e.getMessage());
                    return;
                }
            }
        });
        tv_timer.setVisibility(View.VISIBLE);
        btnresendcode.setVisibility(View.GONE);
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                tv_timer.setText(getString(R.string.resend_otp_in_seconds1, millisUntilFinished / 1000));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
//                mTextField.setText("done!");
                tv_timer.setVisibility(View.GONE);
                btnresendcode.setVisibility(View.VISIBLE);
            }

        }.start();

        return v;
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        output = new Util().processJsonForLanguage(output, getContext());
        Map<String, String> statuscode;
        String statuscodekey;
        String statusdesckey;
        //Write local SQL
        JSONObject myjson = new JSONObject(output);
        statuscode = myDB.statusValues(output);
        statuscodekey = (String) statuscode.get("statuscode");
        statusdesckey = (String) statuscode.get("statusdesc");

        if (handle == "forgotpin") {
            if (!statuscodekey.toString().equals("000")) {
                myDB.showToast(getActivity(), statusdesckey);
                return;
            } else {
                if (!strisnew.equals("Y")) {
                    Fragment myFragment = new fr_reset_pin();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                } else {
                    //enrol
                    enrolcustomer();
                }
            }
        } else if (handle == "codevalidation") {
            if (!statuscodekey.equals("000")) {
                myDB.showToast(getActivity(), statusdesckey);
                return;
            } else {
                strtext = myjson.getString("referencenumber");
                myDB.showToast(getActivity(), getString(R.string.the_validation_has_been_re_sent));
            }
        } else if (handle == "firsttime") {
            if (!statuscodekey.equals("000")) {
                myDB.showToast(getActivity(), statusdesckey);
                return;
            } else {
                Bundle bundle = new Bundle();

                bundle.putString("strmobile", strmobile);
                bundle.putString("strpin", strpin);
                bundle.putString("strfbid", strfbid);
                Fragment myFragment = new fr_register_confirm();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        }
    }

    private void enrolcustomer() {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("firstname", strfirstname);
            jsonMain.put("lastname", strlastname);
            jsonMain.put("mobile", strmobile);
            jsonMain.put("email", stremail);
            jsonMain.put("pin", strpin);
            jsonMain.put("emirate", "");
            jsonMain.put("gender", "U");
            jsonMain.put("address", intromanager.getLocation().equals("") ? "nnn" : intromanager.getLocation());
            jsonMain.put("siriusmember", "");
            jsonMain.put("segment", "1000");
            jsonMain.put("fbuserid", strfbid);
            jsonMain.put("fbtoken", intromanager.getFCMKey());
            jsonMain.put("socialmedialogin", strfbid.length() > 0 ? "Y" : "");
            jsonMain.put("reference", strtext);
            jsonMain.put("otp", txtcode.getText());
            jsonMain.put("geolocation", intromanager.getLatLon());
            jsonMain.put("city", intromanager.getCity());
            jsonMain.put("language", LocalManager.getLanguagePref(getContext()).equals(LocalManager.ENGLISH) ? "E" : "A");
            jsonMain.put("additionalinfo", intromanager.getReferMobile().trim());
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
        String[] myTaskParams = {"/SSMfirsttime", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            Log.i("FirstTime", "Customer Register First Time " + jsonParam.toString());
            HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar9, "firsttime");
            aasyncTask.delegate = (AsyncResponse) fr_validation.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
    }

    private void loginfirsttime() {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("reference", intromanager.getFCMKey());
            jsonMain.put("customer", strmobile);
            jsonMain.put("password", strpin);
            jsonMain.put("fbuserid", strfbid);
            jsonMain.put("loginmode", strfbid.length() > 0 ? "FB" : "");
            jsonMain.put("fbaccesstoken", "");
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
        String[] myTaskParams = {"/SSMvalidateUser", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar9, "loginfirsttime");
            aasyncTask.delegate = (AsyncResponse) fr_validation.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
    }
}
