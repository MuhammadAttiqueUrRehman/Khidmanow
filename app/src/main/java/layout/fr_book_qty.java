package layout;


import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.IntroManager;
import com.aic.khidmanow.MainActivity;
import com.aic.khidmanow.R;
import com.aic.khidmanow.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_book_qty extends Fragment {
    IntroManager intromanager;
    HMCoreData myDB;
    Button next;
    TextView quest1;
    Spinner spinner;
    Fragment myFragment;

    public fr_book_qty() {
        // Required empty public constructor
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
        myDB = new HMCoreData(getActivity());
        View v = inflater.inflate(R.layout.fragment_fr_book_qty, container, false);
        TextView quest = v.findViewById(R.id.quest1);
        Toolbar mtoolbar = v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);

        new Util().setUpStepsBar(v, intromanager.getPageCount(), intromanager.getStepNo());

     /*   Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Set Units/Qty/Persons");
        mtoolbar.setNavigationIcon(R.drawable.ico_back);*/

        //  previous= v.findViewById(R.id.btnprev);
        next = v.findViewById(R.id.btnnext);
        quest1 = v.findViewById(R.id.quest1);
        quest1.setText(getString(R.string.number_of) + " " + intromanager.getQtyDesc());

        // Spinner element
        Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
        JSONObject myjson = null;
        try {
            myjson = new JSONObject(intromanager.getServiceOP());
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(myjson.getString("itemname"));
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(myjson.getString("unitprice"));
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return null;
        }

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("1");
        categories.add("2");
        categories.add("3");
        categories.add("4");
        categories.add("5");
        categories.add("6");
        categories.add("7");
        categories.add("8");
        categories.add("9");
        categories.add("10");
        categories.add("11");
        categories.add("12");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.preference_category, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.preference_category);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                intromanager.setQty(Integer.parseInt(parent.getSelectedItem().toString()));
                //  myDB.showToast(getActivity(),parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                intromanager.setQty(1);
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Fragment myFragment= new fr_book_address();
                //   getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                 /*<telerik:RadComboBoxItem runat="server" Text="Hourly" Value="fr_hourpage" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Q&A (Page 1)" Value="fr_qapage1" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Q&A (Page 2)" Value="fr_qapage2" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Q&A (Page 3)" Value="fr_qapage3" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Q&A (Page 4)" Value="fr_qapage4" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Q&A (Page 5)" Value="fr_qapage5" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Add info only" Value="fr_book_addinfotext" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Add Info with images" Value="fr_book_addinfo" />
			                                     <telerik:RadComboBoxItem runat="server" Text="End Date" Value="fr_book_enddate" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Customer or Other Location" Value="fr_book_address" />
			                                    <telerik:RadComboBoxItem runat="server" Text="Vendor Location" Value="fr_book_vendorlocation" />
			                                    <telerik:RadComboBoxItem runat="server" Text="Destination Location" Value="fr_book_toaddress" />
			                                    <telerik:RadComboBoxItem runat="server" Text="Multi choice Page only" Value="fr_book_multionly" />
			                                    <telerik:RadComboBoxItem runat="server" Text="Multi choice page with Price" Value="fr_book_multiPrice" />
			                                    <telerik:RadComboBoxItem runat="server" Text="Multi Choice Page with Qty and Price" Value="fr_book_multiPriceqty" />
			                                    <telerik:RadComboBoxItem runat="server" Text="Quantity Page" Value="fr_book_qty" />
			                                    <telerik:RadComboBoxItem runat="server" Text="Schedule/Appointment Page" Value="fr_book_time" />
			                                                                <telerik:RadComboBoxItem runat="server" Text="Payment Page" Value="fr_makepayment" />*/
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
                    // myDB.showToast(getActivity(), e.getMessage());
                }
            }
        });

     /*   mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment= new fr_book_addinfo();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment= new fr_book_addinfo();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });*/

        return v;
    }

}
