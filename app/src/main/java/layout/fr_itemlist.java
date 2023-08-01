package layout;


import android.app.Activity;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.aic.khidmanow.listItemProd;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_itemlist extends Fragment implements AsyncResponse {
    private static LinearLayout fillcart;
    private static LinearLayout emptycart;
    ProgressBar progressBar7;
    listItemProd adapter;
    HMCoreData myDB;
    HMAppVariables appVariables = new HMAppVariables();
    RecyclerView recyclerView;
    MenuInflater inflater;
    MaterialSearchView searchview;
    AppCompatActivity appCompatActivity;
    Activity activity;
    TextView mTitle;
    IntroManager intromanager;
//    ImageButton btncart;
//    public static TextView textCartItemCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = new Activity();
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        super.onOptionsItemSelected(items);
        Fragment myFragment;
        int id = items.getItemId();
        Log.i("Debugging", "In Homepage Cart " + Integer.toString(id));
        switch (id) {
            case R.id.home:
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getActivity().getSupportFragmentManager().popBackStack();
                break;
            default:
                return super.onOptionsItemSelected(items);
        }
        return super.onOptionsItemSelected(items);
    }

    @Override
    public void processFinish(String output, String mview) throws HMOwnException, JSONException {
        // myDB.alertBox(getContext(),output);
        recyclerView.removeAllViews();
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        output = new Util().processJsonForLanguage(output, getContext());
        ArrayList<String> ar = new ArrayList<String>();
        String ccy = appVariables.ucurrency;
        adapter = new listItemProd(getContext(), ar, ccy);
        Log.i("Debugging", "Array Created");
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new fr_itemlist.GridSpacingItemDecoration(1, dpTopx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        JSONObject myjson = new JSONObject(output);
        JSONArray json_array = myjson.getJSONArray("itemlist");
        Log.i("Debugging", "JSON Created" + json_array.length());
        Log.i("Debugging", "JSON Created" + output);
        //   myDB.alertBox(getContext(), String.valueOf(json_array.length()));
        for (int i = 0; i < json_array.length(); i++) {
            JSONObject objects = json_array.getJSONObject(i);
            ar.add(String.valueOf(objects));
            Log.i("Debugging", "JSON Created" + objects.toString());
        }
        if (json_array.length() == 0) {
            fillcart.setVisibility(View.INVISIBLE);
            emptycart.setVisibility(View.VISIBLE);
            return;
        } else {
            fillcart.setVisibility(View.VISIBLE);
            emptycart.setVisibility(View.INVISIBLE);
        }


        adapter.notifyDataSetChanged();

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("Debugging Menu", "Loaded Menu");
        // inflater =   ((AppCompatActivity)getActivity()).getMenuInflater();
        inflater.inflate(R.menu.search1_menu, menu);
      /*  final MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        if (myDB.getItemCount()>0) {
            textCartItemCount.setText(String.valueOf(myDB.getItemCount()));
        }*/
        MenuItem item = (MenuItem) menu.findItem(R.id.action_search);
        searchview.setMenuItem(item);
        //  searchview.showSearch(false);
    }

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
        } catch (HMOwnException e) {
            myDB.showToast(getActivity(), e.getMessage());
        }
        final View v = inflater.inflate(R.layout.fragment_fr_itemlist, container, false);
        MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.shop_mater);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);
        searchview = (MaterialSearchView) v.findViewById(R.id.search_view);
        progressBar7 = (ProgressBar) v.findViewById(R.id.progressBar7);
        recyclerView = (RecyclerView) v.findViewById(R.id.productsearchlist);
        fillcart = (LinearLayout) v.findViewById(R.id.fillcart);
        emptycart = (LinearLayout) v.findViewById(R.id.emptycart);
//        btncart = v.findViewById(R.id.btncart);
        fetchData(v, "");
        /*textCartItemCount = v.findViewById(R.id.cart_badge);
        if (myDB.getItemCount()>0) {
            textCartItemCount.setText(Integer.toString(myDB.getItemCount()));
        }*/
        searchview.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //On show
            }

            @Override
            public void onSearchViewClosed() {
                //
            }
        });

        searchview.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchData(v, query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        //subtract item
        /*btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_book_checkout();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });*/

        return v;
    }

    private void fetchData(View v, String q) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("reference", q);
            jsonMain.put("category", "");
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.alertBox(getContext(), e.getMessage());
        }
        Log.i("Debugging Menu", jsonParam.toString());
        String[] myTaskParams = {"/SSMProductList", jsonParam.toString()};
        try {
            HMDataAccess aasyncTask = new HMDataAccess(getContext(), progressBar7, "productlist");
            aasyncTask.delegate = (AsyncResponse) fr_itemlist.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.alertBox(getContext(), e.getMessage());
        }


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
