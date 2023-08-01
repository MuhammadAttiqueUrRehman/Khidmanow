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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aic.khidmanow.AsyncResponse;
import com.aic.khidmanow.HMAppVariables;
import com.aic.khidmanow.HMConstants;
import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.HMDataAccess;
import com.aic.khidmanow.HMOwnException;
import com.aic.khidmanow.R;
import com.aic.khidmanow.Util;
import com.aic.khidmanow.messageAdapterProd;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class fr_messages extends Fragment implements AsyncResponse {
    public static LinearLayout fillcart;
    public static LinearLayout emptycart;
    private messageAdapterProd adapter;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    private TextView mTitle;
    private RecyclerView recyclerView;
    private Boolean forceupdate = false;
    private ProgressBar progressBar4;
    View v;
    MenuInflater inflater;
    MaterialSearchView searchview;
    ArrayList<String> ar = new ArrayList<String>();

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("Debugging Menu", "Loaded Menu");
        // inflater =   ((AppCompatActivity)getActivity()).getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        MenuItem item = (MenuItem) menu.findItem(R.id.menu_search);
        searchview.setMenuItem(item);
        // searchview.showSearch(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("Debugging Menu", "Loaded Menu");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fr_messages, container, false);

       /* Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        mTitle=(TextView) mtoolbar.findViewById(R.id.toolbar_title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(79,7,0), PorterDuff.Mode.SRC_IN);

        mTitle.setText("My Notifications");*/
        searchview = (MaterialSearchView) v.findViewById(R.id.search_view);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.my_notifications));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);
//        mtoolbar.setNavigationIcon(R.drawable.ico_back);


        /*Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        mTitle=(TextView) mtoolbar.findViewById(R.id.toolbar_title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.argb(1,79,7,0), PorterDuff.Mode.SRC_IN);
        searchview =(MaterialSearchView) v.findViewById(R.id.search_view);
        mTitle.setText("My Notifications");*/


        recyclerView = (RecyclerView) v.findViewById(R.id.messqagelist);
        progressBar4 = (ProgressBar) v.findViewById(R.id.progressBar4);
        fillcart = (LinearLayout) v.findViewById(R.id.fillcart);
        emptycart = (LinearLayout) v.findViewById(R.id.emptycart);

        fetchData();

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_account();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });


        searchview.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {

            @Override
            public void onSearchViewShown() {
                //  searchview.clearFocus();
            }

            @Override
            public void onSearchViewClosed() {
                try {
                    searchLocal("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                searchview.hideKeyboard(v);
            }
        });

        searchview.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //     if (query.length()>0){
                Log.i("DebuggingPop", "Inside Submit");
                Log.i("DebuggingPop", "Showing Query" + query);
                try {
                    searchLocal(query);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                searchview.hideKeyboard(v);
                //    }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    //        Log.i("DebuggingPop", "Inside Empty");
                    Log.i("DebuggingPop", "Inside Change");
                    try {
                        searchLocal(newText);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

      /*  btnRecur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forceupdate=false;
                FragmentTransaction ftr = getActivity().getSupportFragmentManager().beginTransaction();
                ftr.detach(fr_messages.this).attach(fr_messages.this).commitAllowingStateLoss();
            }
        });*/

        return v;
    }


    private void searchLocal(String txt) throws HMOwnException {
        ArrayList<String> ar1 = new ArrayList<String>();
        String ccy = appVariables.ucurrency;

        adapter = new messageAdapterProd(getContext(), ar1, ccy);
        recyclerView.setAdapter(adapter);
        Log.i("DebuggingPop", "Query Length " + txt.length());

        if (txt.length() == 0) {
            for (String item : ar) {
                ar1.add(item);
            }
        } else {
            for (String item : ar) {

                if (item.toUpperCase().contains(txt.toUpperCase())) {
                    ar1.add(item);
                }
            }
        }
        Log.i("DebuggingPop", "data size " + ar1.size());
        if (ar1.size() == 0) {
            fillcart.setVisibility(View.INVISIBLE);
            emptycart.setVisibility(View.VISIBLE);
        } else {
            fillcart.setVisibility(View.VISIBLE);
            emptycart.setVisibility(View.INVISIBLE);
        }
        adapter.notifyDataSetChanged();
    }


  /*  @Override
    public boolean onOptionsItemSelected(MenuItem items){
        int id=items.getItemId();
        if (id == android.R.id.home){
            if( getActivity().getSupportFragmentManager().getBackStackEntryCount()>0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }*/


    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");

        Log.i("debugging", "notification output :" + output);
        output = new Util().processJsonForLanguage(output, getContext());

        String ccy = appVariables.ucurrency;
        adapter = new messageAdapterProd(getContext(), ar, ccy);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        JSONObject myjson = new JSONObject(output);
        JSONArray json_array = myjson.getJSONArray("customermessages");

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

    private void fetchData() {
        ar.clear();
        if (!forceupdate) {
            JSONObject jsonParam = new JSONObject();
            JSONObject jsonMain = new JSONObject();
            try {
                jsonMain.put("mdevice", appVariables.udevicecomboined);
                jsonMain.put("merchantcode", HMConstants.appmerchantid);
                jsonMain.put("customer", appVariables.ucustomerid);
                jsonMain.put("certificate", appVariables.ucertificate);
                jsonParam.put("indata", jsonMain);

            } catch (JSONException e) {
                e.printStackTrace();
                myDB.showToast(getContext(), e.getMessage());
                return;
            }
            String[] myTaskParams = {"/SSMarchiveHistoryCustomer", jsonParam.toString()};
            Log.i("DebuggingPop", "Message Input Query " + jsonParam.toString());
            //  new HMDataAccess().execute(myTaskParams);
            try {
                HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar4, "SSMarchiveHistoryCustomer");
                aasyncTask.delegate = (AsyncResponse) fr_messages.this;
                aasyncTask.execute(myTaskParams);
            } catch (Exception e) {
                e.printStackTrace();
                myDB.showToast(getContext(), e.getMessage());
                return;
            }
            forceupdate = true;
        }
    }


}
