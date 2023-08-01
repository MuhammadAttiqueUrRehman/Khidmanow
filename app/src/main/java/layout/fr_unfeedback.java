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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_unfeedback extends Fragment implements AsyncResponse {
    private ProgressBar progressBar9;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    IntroManager intromanager;
    private EditText txtAddInfo;
    private RatingBar starrating;
    private Button btnhome, btnvendorfeedback;

    public fr_unfeedback() {
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
        intromanager = new IntroManager(getActivity());
        myDB = new HMCoreData(getContext());
        try {
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return null;
        }
        View v = inflater.inflate(R.layout.fragment_fr_unfeedback, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.khidmanow_rating);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);
        txtAddInfo = (EditText) v.findViewById(R.id.txtAddinfo);
        starrating = (RatingBar) v.findViewById(R.id.ratingstar);
        btnhome = (Button) v.findViewById(R.id.btnhome);
        btnvendorfeedback = (Button) v.findViewById(R.id.btnvendorfeedback);
        progressBar9 = (ProgressBar) v.findViewById(R.id.progressBar9);


        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtAddInfo.getText().toString().trim().equals("")) {
                    txtAddInfo.setError(getString(R.string.enter_few_comments_about_your));
                    return;
                }

                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", HMConstants.appmerchantid);
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("producttype", "1");
                    jsonMain.put("comments", txtAddInfo.getText());
                    jsonMain.put("outlet", HMConstants.appmerchantid);
                    jsonMain.put("starrating", starrating.getRating() <= 1 ? "1" : starrating.getRating());
                    jsonMain.put("customer", appVariables.ucustomerid);
                    jsonMain.put("certificate", appVariables.ucertificate);
                    jsonMain.put("orderid", appVariables.ucustomerid);
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.showToast(getActivity(), e.getMessage());
                    return;
                }
                String[] myTaskParams = {"/SSMUNRating", jsonParam.toString()};
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    Log.i("Debugging", jsonParam.toString());
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar9, "SSMUNRating");
                    aasyncTask.delegate = (AsyncResponse) fr_unfeedback.this;
                    aasyncTask.execute(myTaskParams);
                } catch (Exception e) {
                    e.printStackTrace();
                    myDB.showToast(getActivity(), e.getMessage());
                    return;
                }



            /*    merchantcode = indata.merchantcode
                customerid = indata.customer
                producttype = indata.producttype
                outletcode = indata.outlet
                rating = indata.starrating
                review = indata.comments
                certificate = indata.certificate
                device = indata.mdevice */
                //go home
            }
        });

        btnvendorfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_myorders_history();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
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
            Bundle bundle = new Bundle();
            bundle.putString("confirmtitle", getString(R.string.complain_acknowledgement));
            bundle.putString("confirmmessage", getString(R.string.we_value_your_feedback));
            Fragment myFragment = new fr_feedback_confirm();
            myFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        }
    }
}
