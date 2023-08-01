package layout;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.aic.khidmanow.AsyncResponse;
import com.aic.khidmanow.HMConstants;
import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.HMDataAccess;
import com.aic.khidmanow.HMOwnException;
import com.aic.khidmanow.IntroManager;
import com.aic.khidmanow.MainActivity;
import com.aic.khidmanow.R;
import com.aic.khidmanow.Util;
import com.aic.khidmanow.placesActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_book_toaddress extends Fragment {
    private Button button;
    int PLACE_PICKER_REQUEST = 1;
    private Toolbar mtoolbar;
    private TextView mTitle, mSubTitle;
    private View v;
    private RadioButton reglocation, newlocation;
    Button next;
    IntroManager introManager;
    String address;
    HMCoreData myDB;
    ProgressBar progressbar4a;
    Fragment myFragment;
    private static final int PLACES_RETURN_RESULT = 1;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("debugging Menu", "Loaded Menu");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public fr_book_toaddress() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        introManager = new IntroManager(getActivity());
        myDB = new HMCoreData(getActivity());
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fr_book_toaddress, container, false);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.destination_address);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);

        new Util().setUpStepsBar(v, introManager.getPageCount(), introManager.getStepNo());

        //  previous= v.findViewById(R.id.btnprev);
        next = v.findViewById(R.id.btnnext);
        reglocation = v.findViewById(R.id.reglocation);
        newlocation = v.findViewById(R.id.newlocation);
        progressbar4a = v.findViewById(R.id.progressBar4a);
      //  adapter.setClickEvent(this);
        reglocation.setChecked(true);
        //mark address type
        if (introManager.getAddressType().equals("R")) {
            reglocation.setChecked(true);
        } else if (introManager.getAddressType().equals("N")) {
            newlocation.setChecked(true);
        }
        JSONObject myjson = null;
        try {
            myjson = new JSONObject(introManager.getServiceOP());
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(myjson.getString("itemname"));
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(myjson.getString("unitprice"));
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return null;
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("debugging", "Geo Cordinates " + introManager.getLatLon());
                Log.i("debugging", "Address Landmark " + introManager.getLocation());
                Log.i("debugging", "Registered City " + introManager.getCity());
                Log.i("debugging", "New city " + introManager.getCityOnce());
                if (reglocation.isChecked()) {
                    introManager.setAddressType("R");
                    introManager.setGeoLocation(introManager.getLatLon());
                    introManager.setAddressLandMark(introManager.getLocation());
                    introManager.setCityOnce(introManager.getCity());
                    changePage();
//                    Fragment myFragment = new fr_book_time();
//                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                } else {
                    introManager.setAddressType("N");
                    Intent intent = new Intent(getActivity(), placesActivity.class);
                    startActivityForResult(intent,PLACES_RETURN_RESULT);
                }


            }
        });

        introManager.setAddressType("N");
        Intent intent = new Intent(getActivity(), placesActivity.class);
        startActivityForResult(intent,PLACES_RETURN_RESULT);
        return v;
    }

       @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        int id = items.getItemId();
        introManager.stepNoDecrement();
        introManager.setPageSequence(introManager.getPageSequence() + 1);
        if (id == android.R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }

    private void changePage() {

        try {
            JSONObject myjson = new JSONObject(introManager.getServiceOP());
            JSONArray json_array_pageflow = myjson.getJSONArray("pageflow");
            if ((json_array_pageflow.length()) == introManager.getPageSequence()) {
                if (!MainActivity.slogged) {
                    Log.i("debugging", "Came to Login Form " + MainActivity.slogged);
                    Fragment myFragment = new fr_login();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                } else {
                    Fragment myFragment = new fr_book_checkout();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                }
            }
            for (int i = 0; i < json_array_pageflow.length(); i++) {
                JSONObject objects = json_array_pageflow.getJSONObject(i);
                if (introManager.getPageSequence() == i) {
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
            introManager.stepNoIncrement();
            introManager.setPageSequence(introManager.getPageSequence() + 1);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        } catch (JSONException e) {
            e.printStackTrace();
            // myDB.showToast(getActivity(), e.getMessage());
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            myDB.showToast(getActivity(), "Error Fetching Results");
            return;
        }
        if (data != null) {
            if (requestCode == PLACES_RETURN_RESULT) {
                changePage();
                return;
            }
        }
    }
}
