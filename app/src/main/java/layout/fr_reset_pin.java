package layout;

import android.app.AlertDialog;

import androidx.fragment.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class fr_reset_pin extends Fragment implements AsyncResponse {
    private Button btnvalidate;
    private ProgressBar progressBar10;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    private TextView mTitle;
    private EditText txtpin;
    IntroManager intromanager;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("Debugging Menu", "Loaded Menu");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        View v = inflater.inflate(R.layout.fragment_fr_reset_pin, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.reset_pin);
        mtoolbar.setNavigationIcon(R.drawable.ico_cross);
        btnvalidate = (Button) v.findViewById(R.id.btnvalidate);
        progressBar10 = (ProgressBar) v.findViewById(R.id.progressBar10);
        txtpin = (EditText) v.findViewById(R.id.txtnewpin);
        //  btnloginclose=(ImageButton) v.findViewById(R.id.btnloginclose);

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intromanager.setFuncCallId("");
                Fragment myFragment = new fr_homepage();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });


        btnvalidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtpin.length() == 0) {
                    myDB.showToast(getContext(), getString(R.string.please_enter_a_valid_4_digit));
                    return;
                }
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", HMConstants.appmerchantid);
                    jsonMain.put("reference", intromanager.getFCMKey());
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("customer", intromanager.getMobile().equals("") ? appVariables.umobilenumber : intromanager.getMobile());
                    jsonMain.put("idnumber", txtpin.getText());
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.showToast(getActivity(), e.getMessage());
                    return;
                }
                String[] myTaskParams = {"/SSMsavePIN", jsonParam.toString()};
                try {
                    Log.i("Debugging", jsonParam.toString());
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar10, "resetpin");
                    aasyncTask.delegate = (AsyncResponse) fr_reset_pin.this;
                    aasyncTask.execute(myTaskParams);
                } catch (Exception e) {
                    e.printStackTrace();
                    myDB.showToast(getActivity(), e.getMessage());
                    return;
                }
            }
        });
        return v;
    }


    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");

        Log.i("debugging", "FAQ Input :" + output);
        output = new Util().processJsonForLanguage(output, getContext());

        Map<String, String> statuscode;
        String statuscodekey;
        String statusdesckey;
        Log.i("Debugging Login Result", output);
        //Write local SQL
        JSONObject myjson = new JSONObject(output);
        statuscode = myDB.statusValues(output);
        statuscodekey = (String) statuscode.get("statuscode");
        statusdesckey = (String) statuscode.get("statusdesc");
        if (!statuscodekey.toString().equals("000")) {
            myDB.showToast(getActivity(), statusdesckey);
            return;
        } else if (handle.equals("validateuser")) {

            if (!statuscodekey.toString().equals("000")) {
                myDB.showToast(getActivity(), statusdesckey);
                return;
            } else {
                Log.i("Debugging Login Result", "Updating SQLLite");
                MainActivity.slogged = true;
                appVariables = myDB.getUserData();

                intromanager.setGender(myjson.getString("gender"));
                appVariables.applat = Double.parseDouble(myjson.getString("userlat"));
                appVariables.applon = Double.parseDouble(myjson.getString("userlon"));
                appVariables.ucity = myjson.getString("city");
                appVariables.ucountry = myjson.getString("country");
                appVariables.ucurrency = myjson.getString("currency");
                //           appVariables.appdeviceexist=muuid;
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
            }
            //     Intent intent = new Intent(getActivity(), MainActivity.class);
            //     startActivity(intent);
            //     getActivity().finish();
            if (intromanager.getBookTime().equals("")) {
                MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Fragment myFragment = new fr_homepage();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            } else {
                //     getActivity().getSupportFragmentManager().popBackStackImmediate();
                Fragment myFragment = new fr_book_time();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }


        } else {
            //MainActivity.customermobile="";
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
//            alert.setTitle(HMConstants.alertheader);
            alert.setTitle(getContext().getString(R.string.app_title));
            alert.setMessage(getString(R.string.new_pin_set));
            alert.setCancelable(false);
            alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    JSONObject jsonParam = new JSONObject();
                    JSONObject jsonMain = new JSONObject();

/*                merchantcode = indata.merchantcode
                customerid = indata.customer
                password = indata.password
                device = indata.mdevice
                fbuserid = indata.fbuserid
                fbtoken = indata.fbaccesstoken
                loginmode = indata.loginmode*/

                    try {
                        jsonMain.put("merchantcode", HMConstants.appmerchantid);
                        jsonMain.put("reference", intromanager.getFCMKey());
                        jsonMain.put("mdevice", intromanager.getDevice());
                        jsonMain.put("customer", intromanager.getMobile().equals("") ? appVariables.umobilenumber : intromanager.getMobile());
                        jsonMain.put("password", txtpin.getText());
                        jsonMain.put("fbuserid", "");
                        jsonMain.put("loginmode", "");
                        jsonMain.put("fbaccesstoken", "");
                        jsonParam.put("indata", jsonMain);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        myDB.showToast(getActivity(), e.getMessage());
                        return;
                    }
                    String[] myTaskParams = {"/SSMvalidateUser", jsonParam.toString()};
                    Log.i("Debugging", jsonParam.toString());
                    try {
                        HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar10, "validateuser");
                        aasyncTask.delegate = (AsyncResponse) fr_reset_pin.this;
                        aasyncTask.execute(myTaskParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                        myDB.showToast(getActivity(), e.getMessage());
                        return;
                    }
                    //Fragment myFragment = new fr_login();
                    //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                    // Intent intent = new Intent(getActivity(), MainActivity.class);
                    // startActivity(intent);
                }
            });
            alert.show();
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
}
