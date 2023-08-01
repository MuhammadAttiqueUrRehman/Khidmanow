package layout;


import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.aic.khidmanow.R;
import com.aic.khidmanow.Util;
import com.aic.khidmanow.listAdapterProd;
import com.aic.khidmanow.reviewVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class fr_product_list extends Fragment implements AsyncResponse {
    private static LinearLayout fillcart;
    private static LinearLayout emptycart;
    private listAdapterProd adapter;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    private TextView mTitle;
    private TextView mSubTitle;
    private RecyclerView recyclerView;
    IntroManager intromanager;
    private ProgressBar progressBar4;
    private View v;
    private Boolean forceupdate = false;
    ArrayList<String> ar = new ArrayList<String>();
    ArrayList<reviewVO> reviewList = new ArrayList<reviewVO>();

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
        Log.i("DebuggingPop", "Came to Product List New");
        intromanager = new IntroManager(getActivity());
        if (forceupdate) {
            forceupdate = false;
        }
        myDB = new HMCoreData(getContext());

        try {
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return null;
        }
        v = inflater.inflate(R.layout.fragment_fr_product_list, container, false);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
//        mtoolbar.setNavigationIcon(R.drawable.ico_back);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);

        recyclerView = (RecyclerView) v.findViewById(R.id.productlist);
        progressBar4 = (ProgressBar) v.findViewById(R.id.progressBar4);
        fillcart = (LinearLayout) v.findViewById(R.id.fillcart);
        emptycart = (LinearLayout) v.findViewById(R.id.emptycart);

        fetchData();

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_homepage();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        return v;

    }



 /*   @Override
    public boolean onOptionsItemSelected(MenuItem items){
        Log.i("Debugging", "Option Menu xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx ");
        int id=items.getItemId();
        if (id == android.R.id.home){
             if( getActivity().getSupportFragmentManager().getBackStackEntryCount()>0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }*/

    private void fetchData() {
        if (!forceupdate) {

            JSONObject jsonParam = new JSONObject();
            JSONObject jsonMain = new JSONObject();
            try {
                jsonMain.put("mdevice", intromanager.getDevice());
                jsonMain.put("merchantcode", HMConstants.appmerchantid);
                jsonMain.put("service", getArguments().getString("subcategory"));
                jsonParam.put("indata", jsonMain);

                Log.i("Debugging", "Retrieiving Sub Category " + getArguments().getString("subcategory"));
                Log.i("Debugging", "JSON input " + jsonParam.toString());

            } catch (JSONException e) {
                e.printStackTrace();
                myDB.showToast(getContext(), e.getMessage());
                return;
            }
            String[] myTaskParams = {"/SSMServiceitemList", jsonParam.toString()};
            //  new HMDataAccess().execute(myTaskParams);
            try {
                HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar4, "SSMServiceitemList");
                aasyncTask.delegate = (AsyncResponse) fr_product_list.this;
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

        adapter = new listAdapterProd(getContext(), ar, ccy);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        JSONObject myjson = new JSONObject(output);
        JSONArray json_array_item = myjson.getJSONArray("serviceitemlist");
        JSONArray json_array_vendor = myjson.getJSONArray("customerreview");
        ar.clear();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(myjson.getString("subcategoryname"));
        //    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(myjson.getString("availableproviders") + " providers    " + myjson.getString("starrating") + "  Rating  by " + myjson.getString("availableproviders") + " Customers");
        for (int i = 0; i < json_array_item.length(); i++) {
            JSONObject objects = json_array_item.getJSONObject(i);
            ar.add(String.valueOf(objects));

        }

        if (json_array_item.length() == 0) {
            fillcart.setVisibility(View.INVISIBLE);
            emptycart.setVisibility(View.VISIBLE);
        } else {
            fillcart.setVisibility(View.VISIBLE);
            emptycart.setVisibility(View.INVISIBLE);
        }
        adapter.notifyDataSetChanged();


     /*   //Vendor Review carousal
        coradapter = new revCustomerAdapter(getActivity(), (ArrayList<reviewVO>) reviewList, R.layout.vendor_review_box);
        recyclerView = v.findViewById(R.id.recycler_review);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(coradapter);
        reviewList.clear();
        Log.i("Debugging", "JSON Vendor" + json_array_vendor);

            for (int x = 0; x < json_array_vendor.length() ; x++) {
                JSONObject object1 = json_array_vendor.getJSONObject(x);
                reviewVO a = new reviewVO(object1.getString("outletimage"), json_array_vendor.length(), object1.getString("outletcode"), object1.getString("outletname"), object1.getInt("numberstar"), object1.getInt("numberofreviews"), object1.getString("comment"), object1.getString("customername"), object1.getString("city"),object1.getString("reviewdate"),object1.getString("servicecategory"));
                reviewList.add(a);
            }


        coradapter.notifyDataSetChanged();*/
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;


        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spacing = spacing;
            this.spanCount = spanCount;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }

                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }

            }
        }

    }

    private int dpTopx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


}
