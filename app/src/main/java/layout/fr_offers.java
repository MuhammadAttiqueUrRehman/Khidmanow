package layout;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.aic.khidmanow.couponAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class fr_offers extends Fragment implements AsyncResponse {

    private static LinearLayout fillcart;
    private static LinearLayout emptycart;
    private couponAdapter adapter;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    private TextView mTitle;
    private RecyclerView recyclerView;
    public static TextView textbadge2;
    private ImageButton btnShopping;
    private String strtext;
    private ProgressBar progressBar4;
    private View v;
    private Boolean forceupdate = false;
    MenuInflater inflater;
    //  MaterialSearchView searchview;
    ArrayList<String> ar = new ArrayList<String>();
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

        v = inflater.inflate(R.layout.fragment_fr_offers, container, false);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        mTitle = (TextView) mtoolbar.findViewById(R.id.toolbar_title);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);
        mtoolbar.setTitle(R.string.deals_and_offers);
        recyclerView = (RecyclerView) v.findViewById(R.id.productlist);
        progressBar4 = (ProgressBar) v.findViewById(R.id.progressBar4);
        fillcart = (LinearLayout) v.findViewById(R.id.fillcart);
        emptycart = (LinearLayout) v.findViewById(R.id.emptycart);

        try {
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return null;
        }


        fetchData();


        return v;

    }


    private void fetchData() {
        if (!forceupdate) {

            JSONObject jsonParam = new JSONObject();
            JSONObject jsonMain = new JSONObject();
            try {
                jsonMain.put("mdevice", intromanager.getDevice());
                jsonMain.put("merchantcode", HMConstants.appmerchantid);
                jsonMain.put("segment", "");
                jsonMain.put("service", "1");
                jsonMain.put("reference", "");
                jsonParam.put("indata", jsonMain);


            } catch (JSONException e) {
                e.printStackTrace();
                myDB.showToast(getContext(), e.getMessage());
                return;
            }
            String[] myTaskParams = {"/SSMCouponList", jsonParam.toString()};
            Log.i("Debugging", "Offer  Input :" + jsonParam.toString());
            try {
                HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar4, "SSMCouponList");
                aasyncTask.delegate = (AsyncResponse) fr_offers.this;
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
    public void processFinish(String output, String mview) throws HMOwnException, JSONException {

        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        output = new Util().processJsonForLanguage(output, getContext());
        String ccy = appVariables.ucurrency;
        Log.i("Debugging", "Offer Output :" + output);
        adapter = new couponAdapter(getContext(), ar, ccy);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        JSONObject myjson = new JSONObject(output);
        JSONArray json_array = myjson.getJSONArray("offerlist");
        ar.clear();
        for (int i = 0; i < json_array.length(); i++) {
            JSONObject objects = json_array.getJSONObject(i);

            ar.add(String.valueOf(objects));

        }

        if (json_array.length() == 0) {
            fillcart.setVisibility(View.INVISIBLE);
            emptycart.setVisibility(View.VISIBLE);
        } else {
            fillcart.setVisibility(View.VISIBLE);
            emptycart.setVisibility(View.INVISIBLE);

        }

        adapter.notifyDataSetChanged();

    }


}
