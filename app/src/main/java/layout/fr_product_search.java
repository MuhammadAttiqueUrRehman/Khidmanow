package layout;

import android.app.Activity;
import android.content.res.Resources;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
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
import com.aic.khidmanow.R;
import com.aic.khidmanow.Util;
import com.aic.khidmanow.listAdapterProd;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;



public class fr_product_search extends Fragment implements AsyncResponse{
    private static LinearLayout fillcart;
    private static LinearLayout emptycart;
    ProgressBar progressBar7;
    listAdapterProd adapter;
    IntroManager intromanager;
    HMCoreData myDB;
    HMAppVariables appVariables = new HMAppVariables();
    RecyclerView recyclerView;
    MenuInflater inflater;
    MaterialSearchView searchview;
    AppCompatActivity appCompatActivity;
    Activity activity;
    private TextView mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = new Activity();
        setHasOptionsMenu(true);
    }

    @Override
    public void processFinish(String output, String mview) throws HMOwnException, JSONException {
        recyclerView.removeAllViews();
        output=output.substring(1,output.length()-1);
        output=output.replace("\\", "");
        output = new Util().processJsonForLanguage(output, getContext());
        // myDB.showToast(getContext(),output);
        ArrayList<String> ar = new ArrayList<String>();
        String ccy = appVariables.ucurrency;
        adapter = new listAdapterProd(getContext(), ar,ccy);
        Log.i("Debugging", "Array Created");
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
      //  recyclerView.addItemDecoration(new fr_product_search.GridSpacingItemDecoration(1, dpTopx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        JSONObject myjson = new JSONObject(output);
        JSONArray json_array = myjson.getJSONArray("serviceitemlist");
        Log.i("Debugging", "JSON Created" + json_array.length());
        Log.i("Debugging", "JSON Created" + output);
        //   myDB.showToast(getContext(), String.valueOf(json_array.length()));
        for (int i = 0; i < json_array.length(); i++) {
            JSONObject objects = json_array.getJSONObject(i);
            ar.add(String.valueOf(objects));

        }
        if (json_array.length()==0) {
            fillcart.setVisibility(View.INVISIBLE);
            emptycart.setVisibility(View.VISIBLE);
            return;
        }else{
            fillcart.setVisibility(View.VISIBLE);
            emptycart.setVisibility(View.INVISIBLE);
        }


        adapter.notifyDataSetChanged();

    }


   @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       Log.i("Debugging Menu", "Loaded Menu1111111111111111111111111111111111");
      // inflater =   ((AppCompatActivity)getActivity()).getMenuInflater();
       inflater.inflate(R.menu.search_menu, menu);
       MenuItem item = (MenuItem) menu.findItem(R.id.action_search);
       searchview.setMenuItem(item);
       searchview.showSearch(true);
    }

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem items){
        Log.i("Debugging Menu", "Loaded Menu1111111111111111111111111111111111");
        int id=items.getItemId();
        if (id == android.R.id.home){
            Fragment myFragment = new fr_homepage();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }*/

  /* @Override
    public void onPrepareOptionsMenu(Menu menu) {
       Log.i("Debugging Menu", "Prepare Menu");
       super.onPrepareOptionsMenu(menu);
       searchview.requestFocus();
   }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        appCompatActivity = new AppCompatActivity();
        intromanager = new IntroManager(getActivity());
        myDB = new HMCoreData(getContext());
        try {
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(),e.getMessage());
            return null;
        }

       final View v = inflater.inflate(R.layout.fragment_fr_product_search, container,false);
       Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        mTitle=(TextView) mtoolbar.findViewById(R.id.toolbar_title);
       ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
       /// mtoolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        searchview =(MaterialSearchView) v.findViewById(R.id.search_view);
        progressBar7= (ProgressBar) v.findViewById(R.id.progressBar7);
        recyclerView = (RecyclerView) v.findViewById(R.id.productsearchlist);
        fillcart=(LinearLayout) v.findViewById(R.id.fillcart);
        emptycart=(LinearLayout) v.findViewById(R.id.emptycart);


        searchview.setOnSearchViewListener(new MaterialSearchView.SearchViewListener(){

            @Override
            public void onSearchViewShown() {
                Log.i("Debugging Menu", "Loaded Menu2222222222222222222222222222222222222222222");
            }

            @Override
            public void onSearchViewClosed() {
            Log.i("Debugging Menu", "Loaded Menu333333333333333333333333333333333333333333");
                intromanager.setFuncCallId("");
                Fragment myFragment = new fr_homepage();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                return;
            }
        });


        /*merchantcode = indata.merchantcode
        device = indata.mdevice
        itemname = indata.itemname
        cityid = indata.city
        subcategory = indata.service*/

        searchview.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
              return textSearch(query,v);
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return textSearch(newText,v);
            }
        });




        return v;
    }

    private boolean textSearch(String query,View v){
        if (query.length()<3){
            return true;
        }
     //   InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
     //   inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("mdevice",intromanager.getDevice());
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("itemname", query);
            jsonMain.put("city", intromanager.getCity());
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return false;
        }
        Log.i("Debugging Menu",jsonParam.toString());
        String[] myTaskParams = {"/SSMServiceitemList", jsonParam.toString()};
        try {
            HMDataAccess aasyncTask = new HMDataAccess(getContext(), progressBar7, "productlist");
            aasyncTask.delegate = (AsyncResponse) fr_product_search.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return false;
        }
        return true;
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
            int position=parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }

                outRect.bottom = spacing;
            }else{
                outRect.left =  column * spacing / spanCount;
                outRect.right = spacing-(column + 1) * spacing / spanCount;
                if(position >= spanCount){
                    outRect.top=spacing;
                }

            }
        }



    }

    private int dpTopx(int dp){
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,r.getDisplayMetrics()));
    }

}
