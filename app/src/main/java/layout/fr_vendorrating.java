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
import android.widget.RatingBar;

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
public class fr_vendorrating extends Fragment implements AsyncResponse {
    // Inflate the layout for this fragment
    private ProgressBar progressBar9;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    IntroManager intromanager;
    private EditText txtAddInfo;
    private RatingBar starrating;
    private Button btnhome;

    public fr_vendorrating() {
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
        View v = inflater.inflate(R.layout.fragment_fr_vendorrating, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.vendor_rating);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);
        txtAddInfo = (EditText) v.findViewById(R.id.txtAddinfo);
        starrating = (RatingBar) v.findViewById(R.id.ratingstar);
        btnhome = (Button) v.findViewById(R.id.btnhome);
        progressBar9 = (ProgressBar) v.findViewById(R.id.progressBar9);

        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtAddInfo.length() == 0) {
                    myDB.showToast(getContext(), getString(R.string.please_enter_few_comments));
                    return;
                }

                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", HMConstants.appmerchantid);
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("producttype", "2");
                    jsonMain.put("comments", txtAddInfo.getText());
                    jsonMain.put("outlet", getArguments().getString("vendorid"));
                    jsonMain.put("starrating", starrating.getRating() <= 1 ? "1" : starrating.getRating());
                    jsonMain.put("customer", appVariables.ucustomerid);
                    jsonMain.put("certificate", appVariables.ucertificate);
                    jsonMain.put("orderid", getArguments().getString("orderid"));
                    jsonMain.put("coupon", getArguments().getString("charges"));
                    jsonMain.put("reference", getArguments().getString("actiontype"));
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.showToast(getActivity(), e.getMessage());
                    return;
                }
                String[] myTaskParams = {"/SSMAddRating", jsonParam.toString()};
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    Log.i("Debugging", jsonParam.toString());
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar9, "SSMAddRating");
                    aasyncTask.delegate = (AsyncResponse) fr_vendorrating.this;
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

        Log.i("debugging", "FAQ Input :" + output);
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
        } else if (handle.equals("SSMAddRating")) {
            Bundle bundle = new Bundle();
            bundle.putString("actiontype", getArguments().getString("actiontype"));
            bundle.putString("vendorid", getArguments().getString("vendorid"));
            bundle.putString("orderid", getArguments().getString("orderid"));
            bundle.putString("itemname", getArguments().getString("itemname"));
            bundle.putString("charges", getArguments().getString("charges"));
            bundle.putString("walletbalance", myjson.getString("walletbalance"));
            bundle.putString("totalreceivable", myjson.getString("totalreceivable"));
            bundle.putString("invalidcouponmessage", myjson.getString("invalidcouponmessage"));

            if (Double.parseDouble(myjson.getString("totalreceivable")) == 0) {
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", HMConstants.appmerchantid);
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("customer", appVariables.ucustomerid);
                    jsonMain.put("certificate", appVariables.ucertificate);
                    jsonMain.put("orderid", getArguments().getString("orderid"));
                    jsonMain.put("status", getArguments().getString("actiontype"));
                    jsonMain.put("channelref", HMConstants.appchannelref);
                    jsonMain.put("outlet", getArguments().getString("vendorid"));
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.showToast(getActivity(), e.getMessage());
                    return;
                }
                String[] myTaskParams = {"/SSMZeropayment", jsonParam.toString()};
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    Log.i("Debugging", jsonParam.toString());
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar9, "SSMZeropayment");
                    aasyncTask.delegate = (AsyncResponse) fr_vendorrating.this;
                    aasyncTask.execute(myTaskParams);
                } catch (Exception e) {
                    e.printStackTrace();
                    myDB.showToast(getActivity(), e.getMessage());
                    return;
                }


            } else {
                Fragment myFragment = new fr_checkout();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        } else if (handle.equals("SSMZeropayment")) {

            Log.i("Debugging", "Came to Zero Payments - aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            Bundle bundle = new Bundle();
            bundle.putString("paymentref", getString(R.string.free_service));
            bundle.putString("orderref", getString(R.string.order_ref) + getArguments().getString("orderid"));
            bundle.putString("message", getString(R.string.your_complimentary_free));
            Fragment myFragment = new fr_checkout_confirm_payment();
            myFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        }
    }

}

