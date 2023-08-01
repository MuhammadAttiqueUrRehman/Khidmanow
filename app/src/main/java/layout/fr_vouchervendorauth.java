package layout;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.aic.khidmanow.R;
import com.aic.khidmanow.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_vouchervendorauth extends Fragment implements AsyncResponse {
    private EditText txtorderid, txtpin;
    private Button btnvalidate;
    private ProgressBar progressBar10;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    private TextView mTitle;

    public fr_vouchervendorauth() {
        // Required empty public constructor
    }

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
        myDB = new HMCoreData(getContext());
        try {
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return null;
        }
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fr_vouchervendorauth, container, false);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.redeem_voucher));
        mtoolbar.setNavigationIcon(R.drawable.ico_back);
        btnvalidate = (Button) v.findViewById(R.id.btnvalidate);
        txtorderid = (EditText) v.findViewById(R.id.txtorderid);
        txtpin = (EditText) v.findViewById(R.id.txtpin);
        progressBar10 = (ProgressBar) v.findViewById(R.id.progressBar10);

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_account();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btnvalidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtorderid.length() == 0) {
                    myDB.showToast(getContext(), getString(R.string.please_enter_orderid));
                    return;
                }
                if (txtpin.length() != 4) {
                    myDB.showToast(getContext(), getString(R.string.please_enter_4_digit_vendor_pin));
                    return;
                }
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();

                try {
                    jsonMain.put("merchantcode", HMConstants.appmerchantid);
                    jsonMain.put("mdevice", appVariables.udevicecomboined);
                    jsonMain.put("customer", appVariables.ucustomerid);
                    jsonMain.put("certificate", appVariables.ucertificate);
                    jsonMain.put("orderid", txtorderid.getText().toString());
                    jsonMain.put("coupon", getArguments().getString("couponcode"));
                    jsonMain.put("coupontype", getArguments().getString("isfav"));
                    jsonMain.put("pin", txtpin.getText().toString());
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.showToast(getActivity(), e.getMessage());
                    return;
                }
                String[] myTaskParams = {"/SSMRedeemVoucher", jsonParam.toString()};
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    Log.i("debugging", jsonParam.toString());
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar10, "SSMRedeemVoucher");
                    aasyncTask.delegate = (AsyncResponse) fr_vouchervendorauth.this;
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
        Log.i("debugging", "Service Detail Output :" + output);
        output = output.replace("\\n", "");
        output = output.replace("\\", "");
        output = new Util().processJsonForLanguage(output, getContext());
        Map<String, String> statuscode;
        String statuscodekey;
        String statusdesckey;
        statuscode = myDB.statusValues(output);
        statuscodekey = (String) statuscode.get("statuscode");
        statusdesckey = (String) statuscode.get("statusdesc");
        JSONObject myjson = new JSONObject(output);
        if (handle == "SSMRedeemVoucher") {
            if (!statuscodekey.toString().equals("000")) {
                myDB.showToast(getActivity(), statusdesckey);
                return;
            } else {
                String message = "Voucher redeemed successfully";
                Bundle bundle = new Bundle();
                bundle.putString("message", message);
                bundle.putString("coupon", "");
                Fragment myFragment = new fr_confirmcouponactivate();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        }
    }
}
