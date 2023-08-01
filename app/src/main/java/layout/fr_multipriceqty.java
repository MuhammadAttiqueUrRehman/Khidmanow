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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.aic.khidmanow.HMAppVariables;
import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.HMOwnException;
import com.aic.khidmanow.IntroManager;
import com.aic.khidmanow.MainActivity;
import com.aic.khidmanow.R;
import com.aic.khidmanow.Util;
import com.aic.khidmanow.multiHolder;
import com.aic.khidmanow.providerInfo;
import com.aic.khidmanow.serviceList;
import com.aic.khidmanow.spinClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_multipriceqty extends Fragment {
    Button btnnext, btnprev;
    RecyclerView recyclerview;
    HMCoreData myDB;
    HMAppVariables appVariables = new HMAppVariables();
    Toolbar mtoolbar;
    IntroManager intromanager;
    ArrayList<spinClass> servicelist = new ArrayList<spinClass>();
    TextView quest1;
    providerInfo providerinfo;
    serviceList adapter;
    Fragment myFragment;

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
        int id = items.getItemId();
        intromanager.stepNoDecrement();
        intromanager.setPageSequence(intromanager.getPageSequence() - 1);
        if (id == android.R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }


    public fr_multipriceqty() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        intromanager = new IntroManager(getActivity());
        providerinfo = new providerInfo(getActivity());
        myDB = new HMCoreData(getContext());
        try {
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            myDB.showToast(getActivity(), e.getMessage());
        }

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fr_multipriceqty, container, false);
        mtoolbar = v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.select_requirements);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        new Util().setUpStepsBar(v, intromanager.getPageCount(), intromanager.getStepNo());

        btnnext = v.findViewById(R.id.btnnext);
        quest1 = v.findViewById(R.id.quest1);
        recyclerview = v.findViewById(R.id.multilist);

        try {
            adapter = new serviceList(getContext(), servicelist, "2");
        } catch (HMOwnException e) {
            e.printStackTrace();
        }
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(adapter);
        servicelist.clear();
        intromanager.setMultiFormType("2");
        JSONObject myjson = null;
        JSONArray json_array_item = null;
        try {
            myjson = new JSONObject(intromanager.getServiceOP());
            json_array_item = myjson.getJSONArray("multchoicelist");


            Boolean mc = false;
            for (int i = 0; i < json_array_item.length(); i++) {
                JSONObject objects = json_array_item.getJSONObject(i);
                for (int m = 0; m < providerinfo.getAllServiceItems().size(); m++) {
                    if (providerinfo.getAllServiceItems().get(m).equals(objects.getString("multichoicename"))) {
                        mc = true;
                        break;
                    } else {
                        mc = false;
                    }
                }
                spinClass spin = new spinClass(objects.getString("multichoicecode"), objects.getString("multichoicename"), i, mc, Double.parseDouble(objects.getString("multichoiceprice")), Integer.parseInt(objects.getString("multichoiceqty")));
                servicelist.add(spin);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        final providerInfo providerinfo = new providerInfo(requireContext());
        for (int i = 0; i < providerinfo.getAllServiceItems().size(); i++) {
            multiHolder n = providerinfo.getAllServiceItems().get(i);
//            mitem = mitem + n.getName() + "  " + n.getAmount() + "*" + n.getQty() + "=" + n.getTotal() + "\n";
            //  Log.i("debugging","Items :" + mitem);
            for (spinClass item : servicelist) {
                if (n.getName().equals(item.getName())) {
                    item.setQuantity(n.getQty());
                    break;
                }
            }
        }

        adapter.notifyDataSetChanged();

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveAddInfo();
                if (providerinfo.getAllServiceItems().size() == 0) {
                    myDB.showToast(getContext(), getString(R.string.select_atleast_one));
                    return;
                }
            /*    Log.i("Debugging","Service Info " +  providerinfo.getAllServiceItems());
                FragmentTransaction ftr = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment myFragment= new fr_providerregister3();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();*/

                try {
                    JSONObject myjson = new JSONObject(intromanager.getServiceOP());
                    JSONArray json_array_pageflow = myjson.getJSONArray("pageflow");
                    if ((json_array_pageflow.length()) == intromanager.getPageSequence()) {
                        if (!MainActivity.slogged) {
                            Log.i("Debugging", "Came to Login Form " + MainActivity.slogged);
                            Fragment myFragment = new fr_login();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                        } else {
                            Fragment myFragment = new fr_book_checkout();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                        }
                    }
                    for (int i = 0; i < json_array_pageflow.length(); i++) {
                        JSONObject objects = json_array_pageflow.getJSONObject(i);
                        if (intromanager.getPageSequence() == i) {
                            if (objects.getString("pagecode").equals("fr_qapage1")) {
                                myFragment = new fr_qapage1();
                            } else if (objects.getString("pagecode").equals("fr_book_vendorlocation")) {
                                myFragment = new fr_book_vendorlocation();
                            } else if (objects.getString("pagecode").equals("fr_book_multionly")) {
                                myFragment = new fr_multionly();
                            } else if (objects.getString("pagecode").equals("fr_book_multiPrice")) {
                                myFragment = new fr_multiprice();
                            } else if (objects.getString("pagecode").equals("fr_book_multiPriceqty")) {
                                myFragment = new fr_multipriceqty();
                            } else if (objects.getString("pagecode").equals("fr_book_address")) {
                                myFragment = new fr_book_address();
                            } else if (objects.getString("pagecode").equals("fr_book_toaddress")) {
                                myFragment = new fr_book_toaddress();
                            } else if (objects.getString("pagecode").equals("fr_book_addinfotext")) {
                                myFragment = new fr_bookaddinfotext();
                            } else if (objects.getString("pagecode").equals("fr_book_addinfo")) {
                                myFragment = new fr_book_addinfo();
                            } else if (objects.getString("pagecode").equals("fr_book_qty")) {
                                myFragment = new fr_book_qty();
                            } else if (objects.getString("pagecode").equals("fr_book_time")) {
                                myFragment = new fr_book_time();
                            }

                        }
                    }
                    intromanager.stepNoIncrement();
                    intromanager.setPageSequence(intromanager.getPageSequence() + 1);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //    myDB.showToast(getActivity(), e.getMessage());
                }
            }
        });

        return v;
    }

}
