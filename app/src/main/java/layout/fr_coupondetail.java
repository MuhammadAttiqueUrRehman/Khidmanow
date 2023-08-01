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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Switch;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_coupondetail extends Fragment implements AsyncResponse {
    private HMCoreData myDB;
    private IntroManager intromanager;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    private TextView mTitle, mSubTitle, itemname, itemsubname, itemdescription, tandc, category;
    private ProgressBar progressBar4;
    private View v;
    private ScrollView accscroll;
    private ImageView img;
    private Button btnActivate;
    private String itemcode;
    private Switch switchtandc;

    public fr_coupondetail() {
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
        try {
            myDB = new HMCoreData(getActivity());
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return null;
        }
        v = inflater.inflate(R.layout.fragment_fr_coupondetail, container, false);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.offer_detail);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);
        itemname = (TextView) v.findViewById(R.id.itemname);
        itemsubname = (TextView) v.findViewById(R.id.itemsubname);
        category = (TextView) v.findViewById(R.id.category);
        img = (ImageView) v.findViewById(R.id.detailimage);
        itemdescription = (TextView) v.findViewById(R.id.itemdescription);
        tandc = (TextView) v.findViewById(R.id.tandc);
        accscroll = (ScrollView) v.findViewById(R.id.accscroll);
        progressBar4 = (ProgressBar) v.findViewById(R.id.progressBar4);
        btnActivate = (Button) v.findViewById(R.id.btnactivate);
        switchtandc = (Switch) v.findViewById(R.id.switchtc);
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        accscroll.setVisibility(GONE);
        try {
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("segment", "");
            jsonMain.put("service", "2");
            jsonMain.put("reference", getArguments().getString("itemcode"));
            jsonParam.put("indata", jsonMain);


        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return null;
        }
        String[] myTaskParams = {"/SSMCouponList", jsonParam.toString()};
        Log.i("Debugging", "Service Detail Input :" + jsonParam.toString());
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar4, "SSMCouponList");
            aasyncTask.delegate = (AsyncResponse) fr_coupondetail.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return null;
        }


        btnActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!switchtandc.isChecked()) {
                    myDB.showToast(getContext(), getString(R.string.accept_terms_amp_conditions));
                    return;
                }
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", HMConstants.appmerchantid);
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("certificate", appVariables.ucertificate);
                    jsonMain.put("customer", appVariables.ucustomerid);
                    jsonMain.put("reference", itemcode);
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.showToast(getContext(), e.getMessage());
                    return;
                }

                String[] myTaskParams = {"/SSMissuecoupon", jsonParam.toString()};
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    Log.i("Debugging", jsonParam.toString());
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar4, "SSMissuecoupon");
                    aasyncTask.delegate = (AsyncResponse) fr_coupondetail.this;
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
        Log.i("Debugging", "Service Detail Output :" + output);
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
        if (handle == "SSMCouponList") {
            JSONArray json_array = myjson.getJSONArray("offerlist");
            JSONObject objects = json_array.getJSONObject(0);
            Log.i("Debugging", "Service Detail Output :" + output);
            itemname.setText(objects.getString("itemname"));
            category.setText(objects.getString("category"));
            itemsubname.setText(getString(R.string.value) + " " + getString(R.string.Rs) + " " + objects.getString("offervalue"));
            itemdescription.setText(Html.fromHtml(objects.getString("itemdescription")), TextView.BufferType.SPANNABLE);
            tandc.setText(Html.fromHtml(objects.getString("remark")), TextView.BufferType.SPANNABLE);
            Picasso.get()
                    .load(objects.getString("imageurls"))
                    .placeholder(R.drawable.haal_meer_large)
                    .fit()
                    .noFade()
                    .centerCrop()
                    .into(img);
            //Service Details
            if (objects.getString("issuetype").equals("Bulk")) {
                switchtandc.setVisibility(View.VISIBLE);
                btnActivate.setVisibility(View.VISIBLE);
            }
            itemcode = objects.getString("itemcode");
            accscroll.setVisibility(View.VISIBLE);
        } else if (handle == "SSMissuecoupon") {
            if (!statuscodekey.toString().equals("000")) {
                myDB.showToast(getActivity(), statusdesckey);
                return;
            } else {
                String coupon = myjson.getString("couponname");
                String message = getString(R.string.voucher_no) + myjson.getString("transactionref") + getString(R.string.is_successfully_activited);
                Bundle bundle = new Bundle();
                bundle.putString("message", message);
                bundle.putString("coupon", coupon);
                Fragment myFragment = new fr_confirmcouponactivate();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
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
