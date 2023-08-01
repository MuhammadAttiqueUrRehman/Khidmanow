package layout;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_referral_benefit extends Fragment implements AsyncResponse {
    private Toolbar mtoolbar;
    private TextView mTitle, aboutinfo;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private IntroManager intromanager;
    private ProgressBar progressBar13;

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
        myDB = new HMCoreData(getContext());
        intromanager = new IntroManager(getActivity());
        View v = inflater.inflate(R.layout.fragment_fr_referral_benefit, container, false);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.referral_benifits);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);
        aboutinfo = (TextView) v.findViewById(R.id.taboutinfo);
        progressBar13 = (ProgressBar) v.findViewById(R.id.progressBar13);

        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("reference", HMConstants.referincentive);
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return null;
        }
        Log.i("Debugging", "Booking Timeline Input :" + jsonParam.toString());
        String[] myTaskParams = {"/SSMGeneralContentDisplay", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar13, "SSMGeneralContentDisplay");
            aasyncTask.delegate = (AsyncResponse) fr_referral_benefit.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return null;
        }
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
        Log.i("debugging", "output :" + output);
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\\\\\\"", "'");
        output = output.replace("\\", "");
        output = new Util().processJsonForLanguage(output, getContext());
        JSONObject myjson = (JSONObject) new JSONObject(output);
        String mt = myjson.getString("longtext").replace("rn", "");
        mt = mt.replace("'", "\"");
        aboutinfo.setText(Html.fromHtml(mt), TextView.BufferType.SPANNABLE);
    }

}
