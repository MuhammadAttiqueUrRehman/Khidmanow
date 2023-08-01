package layout;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.aic.khidmanow.timeLineAdapter;
import com.aic.khidmanow.timeLineVO;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class fr_myorders extends Fragment implements AsyncResponse {
    private static LinearLayout fillcart;
    private static LinearLayout emptycart;
    private timeLineAdapter adapter;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    private TextView mTitle;
    private RecyclerView recyclerView;
    private ProgressBar progressBar4x;
    private Boolean forceupdate = false;
    private MenuInflater inflater;
    private IntroManager intromanager;
    ArrayList<timeLineVO> ar = new ArrayList<timeLineVO>();

    public fr_myorders() {
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
        if (forceupdate) {
            forceupdate = false;
        }
        myDB = new HMCoreData(getContext());
        intromanager = new IntroManager(getActivity());
        try {
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return null;
        }

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fr_myorders, container, false);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.booking_timeline);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);
        recyclerView = (RecyclerView) v.findViewById(R.id.bookinglist);
        progressBar4x = (ProgressBar) v.findViewById(R.id.progressBar4x);
        fillcart = (LinearLayout) v.findViewById(R.id.fillcart);
        emptycart = (LinearLayout) v.findViewById(R.id.emptycart);

        try {
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            myDB.showToast(getContext(), e.getMessage());
            return null;
        }
        fetchData();

        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        MainActivity.bottomNavigationView.setSelectedItemId(R.id.nav_home);
        return true;
        /*int id=items.getItemId();
        if (id == android.R.id.home){
            if( getActivity().getSupportFragmentManager().getBackStackEntryCount()>0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(items);*/
    }

    private void fetchData() {
        if (!forceupdate) {
            JSONObject jsonParam = new JSONObject();
            JSONObject jsonMain = new JSONObject();
            try {
                jsonMain.put("merchantcode", HMConstants.appmerchantid);
                jsonMain.put("mdevice", intromanager.getDevice());
                jsonMain.put("customer", appVariables.ucustomerid);
                jsonMain.put("certificate", appVariables.ucertificate);
                jsonParam.put("indata", jsonMain);
            } catch (JSONException e) {
                e.printStackTrace();
                myDB.showToast(getContext(), e.getMessage());
                return;
            }
            Log.i("debugging", "Booking Timeline Input :" + jsonParam.toString());
            String[] myTaskParams = {"/SSMBookingTimeLine", jsonParam.toString()};
            //  new HMDataAccess().execute(myTaskParams);
            try {
                HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar4x, "SSMBookingTimeLine");
                aasyncTask.delegate = (AsyncResponse) fr_myorders.this;
                aasyncTask.execute(myTaskParams);
            } catch (Exception e) {
                e.printStackTrace();
                myDB.showToast(getContext(), e.getMessage());
                return;
            }
            forceupdate = true;
        }
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");

        Log.i("debugging", "Booking Timeline Output :" + output);
        output = new Util().processJsonForLanguage(output, getContext());


        adapter = new timeLineAdapter(getActivity(), ar, R.layout.timeline_card);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        JSONObject myjson = new JSONObject(output);
        JSONArray json_array_item = myjson.getJSONArray("timeline");

        ar.clear();
        for (int i = 0; i < json_array_item.length(); i++) {
            JSONObject objects = json_array_item.getJSONObject(i);
            //  public timeLineVO(String orderdate,String ordernumber,String orderdes,String status,String statusdes,String vendorid,String price,String vendorname,String maxstatus)
            timeLineVO y = new timeLineVO(objects.getString("logdatetime"), objects.getString("orderid"), objects.getString("itemname"), objects.getString("logstatus"), objects.getString("logstatusdes"), objects.getString("vendorcode"), objects.getString("finalprice"), objects.getString("vendorname"), objects.getString("maxstatus"), objects.getString("remarks"), objects.getString("materialcharges"), objects.getString("materialgst"), objects.getString("vouchertype"), Float.parseFloat(objects.getString("sgstbasic")), Float.parseFloat(objects.getString("cgstbasic")), Float.parseFloat(objects.getString("payableamount")), objects.getString("vendormobile"), objects.getString("customermobile"),
                    objects.getString("multichoicequestion"), objects.getString("categorycode"));
            y.setItem_detail(objects.getJSONArray("itemdetail").toString());
            ar.add(y);
        }
        adapter.notifyDataSetChanged();

        if (json_array_item.length() == 0) {
            fillcart.setVisibility(View.INVISIBLE);
            emptycart.setVisibility(View.VISIBLE);
        } else {
            fillcart.setVisibility(View.VISIBLE);
            emptycart.setVisibility(View.INVISIBLE);
        }
    }
}
