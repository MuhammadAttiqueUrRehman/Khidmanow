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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aic.khidmanow.AsyncResponse;
import com.aic.khidmanow.HMAppVariables;
import com.aic.khidmanow.HMConstants;
import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.HMDataAccess;
import com.aic.khidmanow.HMOwnException;
import com.aic.khidmanow.MainActivity;
import com.aic.khidmanow.R;
import com.aic.khidmanow.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class fr_change_pin extends Fragment implements AsyncResponse {
    private EditText txtcode;
    private Button btnvalidate;
    private ProgressBar progressBar15;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    private TextView mTitle;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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
        myDB = new HMCoreData(getContext());
        try {
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return null;
        }
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fr_change_pin, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.change_pin));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);
        btnvalidate = (Button) v.findViewById(R.id.btnvalidate);
        txtcode = (EditText) v.findViewById(R.id.txtcurpin);
        progressBar15 = (ProgressBar) v.findViewById(R.id.progressBar15);

        btnvalidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtcode.length() == 0) {
                    myDB.showToast(getContext(), getString(R.string.please_enter_current_4_digit_pin));
                    return;
                }
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", HMConstants.appmerchantid);
                    jsonMain.put("mdevice", appVariables.udevicecomboined);
                    jsonMain.put("customer", appVariables.ucustomerid);
                    jsonMain.put("certificate", appVariables.ucertificate);
                    jsonMain.put("pin", txtcode.getText());
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.showToast(getActivity(), e.getMessage());
                    return;
                }
                String[] myTaskParams = {"/SSMvalidatePassword", jsonParam.toString()};
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    Log.i("Debugging", jsonParam.toString());
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar15, "SSMvalidatePassword");
                    aasyncTask.delegate = (AsyncResponse) fr_change_pin.this;
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
        Map<String, String> statuscode;
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");

        Log.i("debugging", "FAQ Input :" + output);
        output = new Util().processJsonForLanguage(output, getContext());

        String statuscodekey;
        String statusdesckey;
        //Write local SQL
        JSONObject myjson = new JSONObject(output);
        statuscode = myDB.statusValues(output);
        statuscodekey = (String) statuscode.get("statuscode");
        statusdesckey = (String) statuscode.get("statusdesc");
        if (!statuscodekey.toString().equals("000")) {
            myDB.showToast(getActivity(), getString(R.string.current_pin_incorrect));
            return;
        } else {
            MainActivity.customermobile = appVariables.umobilenumber;
            Fragment myFragment = new fr_reset_pin();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
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


