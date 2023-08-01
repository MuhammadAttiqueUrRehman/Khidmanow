package layout;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.aic.khidmanow.HMAppVariables;
import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.IntroManager;
import com.aic.khidmanow.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_voucherdetail extends Fragment {
    private HMCoreData myDB;
    private IntroManager intromanager;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    private TextView mTitle, mSubTitle, itemname, itemdescription, tandc, category, expiry, offervalidity, vouchercode;
    private ProgressBar progressBar4;
    private View v;
    private ScrollView accscroll;
    private ImageView img;
    private Button btnActivate;
    private String itemcode;
    private Switch switchtandc;

    public fr_voucherdetail() {
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
        v = inflater.inflate(R.layout.fragment_fr_voucherdetail, container, false);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.voucher_detail);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);
//        mtoolbar.setNavigationIcon(R.drawable.ico_back);
        btnActivate = (Button) v.findViewById(R.id.btnactivate);
        switchtandc = (Switch) v.findViewById(R.id.switchtc);
        itemname = (TextView) v.findViewById(R.id.itemname);
        //  itemsubname=(TextView) v.findViewById(R.id.itemsubname);
        category = (TextView) v.findViewById(R.id.category);
        img = (ImageView) v.findViewById(R.id.detailimage);
        itemdescription = (TextView) v.findViewById(R.id.itemdescription);
        tandc = (TextView) v.findViewById(R.id.tandc);
        expiry = (TextView) v.findViewById(R.id.expiry);
        offervalidity = (TextView) v.findViewById(R.id.offervalidity);
        vouchercode = (TextView) v.findViewById(R.id.vouchercode);
        accscroll = (ScrollView) v.findViewById(R.id.accscroll);
        progressBar4 = (ProgressBar) v.findViewById(R.id.progressBar4);
        itemname.setText(getArguments().getString("itemname"));
        category.setText(getArguments().getString("category"));
        itemdescription.setText(Html.fromHtml(getArguments().getString("itemdescription")), TextView.BufferType.SPANNABLE);
        tandc.setText(Html.fromHtml(getArguments().getString("itemtandc")), TextView.BufferType.SPANNABLE);
        Picasso.get()
                .load(getArguments().getString("itemimage"))
                .placeholder(R.drawable.haal_meer_large)
                .fit()
                .noFade()
                .centerCrop()
                .into(img);


  /*      bundle.putString("couponcode", myjson.getString("itemcode"));
        bundle.putString("itemname", myjson.getString("itemname"));
        bundle.putString("itemdescription", myjson.getString("itemdescription"));
        bundle.putString("itemtandc", myjson.getString("remark"));
        bundle.putString("itemimage", myjson.getString("imageurls"));
        bundle.putString("category", myjson.getString("category"));
        bundle.putString("itemprice", activity.getString(R.string.Rs) + " " +myjson.getString("offervalue"));
        bundle.putString("isfav", type);*/
      /*  JSONObject jsonParam = new JSONObject();
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
        }

        String[] myTaskParams = {"/SSMCouponList", jsonParam.toString()};
        Log.i("Debugging","Service Detail Input :" + jsonParam.toString());
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar4, "SSMCouponList");
            aasyncTask.delegate = (AsyncResponse) fr_voucherdetail.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
        }*/

        btnActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!switchtandc.isChecked()) {
//                    myDB.showToast(getContext(), "Please accept Terms & Conditions");
                    myDB.showToast(getContext(), getString(R.string.accept_terms_amp_conditions));
                    return;
                }
                FragmentActivity activity = (FragmentActivity) v.getContext();
                Bundle bundle = new Bundle();
                bundle.putString("couponcode", getArguments().getString("couponcode"));
                bundle.putString("itemname", getArguments().getString("itemname"));
                bundle.putString("itemdescription", getArguments().getString("itemdescription"));
                bundle.putString("itemtandc", getArguments().getString("itemtandc"));
                bundle.putString("itemimage", getArguments().getString("itemimage"));
                bundle.putString("category", getArguments().getString("category"));
                bundle.putString("itemprice", getArguments().getString("itemprice"));
                bundle.putString("isfav", getArguments().getString("isfav"));
                Fragment myFragment = new fr_vouchervendorauth();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();

            /*    JSONObject jsonParam = new JSONObject();
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
                }

                String[] myTaskParams = {"/SSMissuecoupon", jsonParam.toString()};
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    Log.i("Debugging", jsonParam.toString());
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar4, "SSMissuecoupon");
                    aasyncTask.delegate = (AsyncResponse) fr_voucherdetail.this;
                    aasyncTask.execute(myTaskParams);
                } catch (Exception e) {
                    e.printStackTrace();
                    myDB.showToast(getContext(), e.getMessage());
                }*/


            }
        });

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new postoffercontainer();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        return v;
    }


}
