package layout;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
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
import android.widget.ProgressBar;
import android.widget.Switch;

import com.aic.khidmanow.AsyncResponse;
import com.aic.khidmanow.HMAppVariables;
import com.aic.khidmanow.HMConstants;
import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.HMDataAccess;
import com.aic.khidmanow.HMOwnException;
import com.aic.khidmanow.IntroManager;
import com.aic.khidmanow.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class fr_settings extends Fragment implements AsyncResponse {
    private ProgressBar progressBar9;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    IntroManager intromanager;
    private Button btnsave;
    private Switch switchnotification, switchnewsletter, switchsms;

    public fr_settings() {
        // Required empty public constructor
    }

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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fr_settings, container, false);
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

        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getContext().getString(R.string.settings));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);
        progressBar9 = (ProgressBar) v.findViewById(R.id.progressBar9);
        btnsave = (Button) v.findViewById(R.id.btnhome);
        switchnotification = (Switch) v.findViewById(R.id.switchnotification);
        switchnewsletter = (Switch) v.findViewById(R.id.switchnewsletter);
        switchsms = (Switch) v.findViewById(R.id.switchsms);

        switchnotification.setChecked(appVariables.sendNotification.equals("1") ? true : false);
        switchnewsletter.setChecked(appVariables.sendMailer.equals("1") ? true : false);
        switchsms.setChecked(appVariables.sendSMS.equals("1") ? true : false);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", HMConstants.appmerchantid);
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("customer", appVariables.ucustomerid);
                    jsonMain.put("certificate", appVariables.ucertificate);
                    jsonMain.put("sendnotification", switchnotification.isChecked() ? "1" : "0");
                    jsonMain.put("sendmailer", switchnewsletter.isChecked() ? "1" : "0");
                    jsonMain.put("sendsms", switchsms.isChecked() ? "1" : "0");
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.showToast(getActivity(), e.getMessage());
                    return;
                }
                String[] myTaskParams = {"/SSMCustomerSettings", jsonParam.toString()};
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    Log.i("Debugging", jsonParam.toString());
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar9, "SSMCustomerSettings");
                    aasyncTask.delegate = (AsyncResponse) fr_settings.this;
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
    public boolean onOptionsItemSelected(MenuItem items) {
        int id = items.getItemId();
        if (id == android.R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(items);
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
            appVariables.sendNotification = switchnotification.isChecked() ? "1" : "0";
            appVariables.sendMailer = switchnewsletter.isChecked() ? "1" : "0";
            appVariables.sendSMS = switchsms.isChecked() ? "1" : "0";
            appVariables = myDB.updateUserData(appVariables);
            Bundle bundle = new Bundle();
            bundle.putString(getContext().getString(R.string.title), getContext().getString(R.string.settings_acknoeled));
            bundle.putString(getContext().getString(R.string.message), getContext().getString(R.string.settings_changed));
            Fragment myFragment = new fr_settings_confirm();
            myFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        }
    }
}
