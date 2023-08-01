package layout;


import android.os.Bundle;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_vendoradmitotp extends Fragment implements AsyncResponse {
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

    public fr_vendoradmitotp() {
        // Required empty public constructor
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
        View v = inflater.inflate(R.layout.fragment_fr_vendoradmitotp, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.otp_validation);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.otp);
        mtoolbar.setNavigationIcon(R.drawable.ico_cross);
        btnvalidate = (Button) v.findViewById(R.id.btnvalidate);
        txtcode = (EditText) v.findViewById(R.id.txtvalidationcode);
        progressBar9 = (ProgressBar) v.findViewById(R.id.progressBar9);


        btnvalidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtcode.length() == 0) {
                    myDB.showToast(getContext(), getString(R.string.please_enter_4_digit_number_from_vendor));
                    return;
                }
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                if (getArguments().getString("categorycode").equals("114")) {
                    try {
                        jsonMain.put("merchantcode", HMConstants.appmerchantid);
                        jsonMain.put("mdevice", intromanager.getDevice());
                        jsonMain.put("reference", getArguments().getString("orderid"));
                        jsonMain.put("idnumber", txtcode.getText());
                        jsonMain.put("orderid", getArguments().getString("orderid"));
                        jsonParam.put("indata", jsonMain);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        myDB.showToast(getActivity(), e.getMessage());
                        return;
                    }

                    String[] myTaskParams = {"/SSMBookingCheckIn", jsonParam.toString()};

                    try {
                        Log.i("Debugging", jsonParam.toString());
                        HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar9, "vendoradmitotp");
                        aasyncTask.delegate = (AsyncResponse) fr_vendoradmitotp.this;
                        aasyncTask.execute(myTaskParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                        myDB.showToast(getActivity(), e.getMessage());
                        return;
                    }
                } else {
                    try {
                        jsonMain.put("merchantcode", HMConstants.appmerchantid);
                        jsonMain.put("mdevice", intromanager.getDevice());
                        jsonMain.put("reference", getArguments().getString("orderid"));
                        jsonMain.put("idnumber", txtcode.getText());
                        jsonMain.put("orderid", getArguments().getString("orderid"));
                        jsonParam.put("indata", jsonMain);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        myDB.showToast(getActivity(), e.getMessage());
                        return;
                    }
                    String[] myTaskParams = {"/SSMValidateToken", jsonParam.toString()};
                    try {
                        Log.i("Debugging", jsonParam.toString());
                        HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar9, "vendoradmitotp");
                        aasyncTask.delegate = (AsyncResponse) fr_vendoradmitotp.this;
                        aasyncTask.execute(myTaskParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                        myDB.showToast(getActivity(), e.getMessage());
                        return;
                    }
                }
            }
        });

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_myorders();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        return v;
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        Log.i("debugging", "order vendor Output :" + output);
        output = new Util().processJsonForLanguage(output, getContext());

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
            String message = "";
            Bundle bundle = new Bundle();
            if (getArguments().getString("categorycode").equals("114")) {
                message = getString(R.string.you_have_successfully_checked) + getArguments().getString("vendorname");
            } else {
                message = getString(R.string.you_have_successfully_authorized) + " " + getArguments().getString("vendorname") + " " + getString(R.string.to_carry_out_inspection);
            }
            bundle.putString("vendorname", message);
            Fragment myFragment = new fr_admitotp();
            myFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        }
    }
}
