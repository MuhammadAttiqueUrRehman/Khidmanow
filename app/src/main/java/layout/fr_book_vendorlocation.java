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
import android.widget.Button;
import android.widget.TextView;

import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.IntroManager;
import com.aic.khidmanow.MainActivity;
import com.aic.khidmanow.R;
import com.aic.khidmanow.Util;
import com.aic.khidmanow.detailitemVO;
import com.aic.khidmanow.questionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_book_vendorlocation extends Fragment {
    private Toolbar mtoolbar;
    private TextView mTitle, mSubTitle;
    private RecyclerView recyclerView;
    private questionAdapter serviceadapter;
    private View v;
    Button next;
    ArrayList<detailitemVO> detailList = new ArrayList<detailitemVO>();
    private IntroManager intromanager;
    private HMCoreData hmcoredata;
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

    public fr_book_vendorlocation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        intromanager = new IntroManager(getActivity());
        hmcoredata = new HMCoreData(getActivity());

        View v = inflater.inflate(R.layout.fragment_fr_qapage1, container, false);
        v = inflater.inflate(R.layout.fragment_fr_book_vendorlocation, container, false);
        TextView quest = (TextView) v.findViewById(R.id.quest1);

        new Util().setUpStepsBar(v, intromanager.getPageCount(), intromanager.getStepNo());

        //     Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        //    ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        //   mtoolbar.setNavigationIcon(R.drawable.ico_back);


        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);

        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);

        next = (Button) v.findViewById(R.id.btnnext);
        serviceadapter = new questionAdapter(getActivity(), (ArrayList<detailitemVO>) detailList, R.layout.radio_option, 9, intromanager.getCarryLocation());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(serviceadapter);
        JSONObject myjson = null;
        detailList.clear();
        try {
            myjson = new JSONObject(intromanager.getServiceOP());
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(myjson.getString("itemname"));
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(myjson.getString("unitprice"));
            JSONArray myArray = myjson.getJSONArray("vendorlocationlist");
            Log.i("Debugging", "Qty = " + myArray.length());
            for (int i = 0; i < myArray.length(); i++) {
                JSONObject objects = myArray.getJSONObject(i);
                detailitemVO a = new detailitemVO(objects.getString("locationname"), objects.getString("locationcode"), objects.getString("locationcode"), 0);
                detailList.add(a);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

      /*  mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("itemcode", intromanager.getItemCode());
                Fragment myFragment= new fr_servicedetail();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });*/


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean bool = hmcoredata.checkForm(getActivity(), intromanager.getCarryLocation());
                if (!bool) {
                    return;
                }
          /*      Fragment myFragment;
                JSONObject myjson = null;
                JSONArray newArray = null;
                try {
                    myjson = new JSONObject(intromanager.getServiceOP());
                    newArray = myjson.getJSONArray("question2");
                } catch (JSONException e) {
                    e.printStackTrace();
                    hmcoredata.showToast(getActivity(), e.getMessage());
                    return;
                }
                if (newArray.length() == 0) {
                    myFragment = new fr_book_addinfo();
                } else {
                    myFragment = new fr_qapage2();
                }
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

}
