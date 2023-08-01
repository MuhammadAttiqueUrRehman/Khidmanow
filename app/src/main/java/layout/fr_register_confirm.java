package layout;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_register_confirm extends Fragment implements AsyncResponse {
    private TextView lblorder;
    private Button btnhome;
    private Toolbar mtoolbar;
    private String message, message1;
    private IntroManager intromanager;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private ProgressBar progressBar9;

    public fr_register_confirm() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        intromanager = new IntroManager(getActivity());
        myDB = new HMCoreData(getContext());
        try {
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return null;
        }
        View v = inflater.inflate(R.layout.fragment_fr_register_confirm, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.registration_confirm);
        progressBar9 = v.findViewById(R.id.progressBar9);
        lblorder = (TextView) v.findViewById(R.id.lblorder);
        btnhome = (Button) v.findViewById(R.id.btnhome);
        message = getString(R.string.welcome_you_are_now);

        lblorder.setText(message);

        //go home
        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginfirsttime();
            }
        });

        return v;
    }


    private void loginfirsttime() {
        Log.i("FirstTime", "Customer Register First Time ");
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("reference", intromanager.getFCMKey());
            jsonMain.put("customer", getArguments().getString("strmobile"));
            jsonMain.put("password", getArguments().getString("strpin"));
            jsonMain.put("fbuserid", getArguments().getString("strfbid"));
            jsonMain.put("loginmode", getArguments().getString("strfbid").length() > 0 ? "FB" : "");
            jsonMain.put("fbaccesstoken", "");
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
        String[] myTaskParams = {"/SSMvalidateUser", jsonParam.toString()};
        Log.i("FirstTime", "Customer Register First Time " + jsonParam.toString());
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar9, "loginfirsttime");
            aasyncTask.delegate = (AsyncResponse) fr_register_confirm.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }


    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        Map<String, String> statuscode;
        String statuscodekey;
        String statusdesckey;
        //Write local SQL
        JSONObject myjson = new JSONObject(output);
        statuscode = myDB.statusValues(output);
        statuscodekey = (String) statuscode.get("statuscode");
        statusdesckey = (String) statuscode.get("statusdesc");
        if (!statuscodekey.toString().equals("000")) {
            myDB.showToast(getActivity(), statusdesckey);
            return;
        } else {
            //myDB.showToast(this, statusdesckey);
            if (!statuscodekey.toString().equals("000")) {
                myDB.showToast(getActivity(), statusdesckey);
                return;
            } else {
                Log.i("Debugging Login Result", "Updating SQLLite");
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
                MainActivity.slogged = true;
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
                appVariables = myDB.updateUserData(appVariables);
                //  Intent intent = new Intent(getActivity(), MainActivity.class);
                //  startActivity(intent);
                //  getActivity().finish();
                if (intromanager.getBookTime().equals("")) {
                    MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                    getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    Fragment myFragment = new fr_homepage();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                } else {
                    //    getActivity().getSupportFragmentManager().popBackStackImmediate();
                    Fragment myFragment = new fr_book_time();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                }


            }
        }
    }
}
