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
import android.widget.LinearLayout;
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
public class fr_qapage1 extends Fragment {
    Toolbar mtoolbar;
    LinearLayout lin1, lin2, lin3, lin4, lin5;
    TextView mTitle, mSubTitle, quest1, quest2, quest3, quest4, quest5;
    RecyclerView recyclerView1, recyclerView2, recyclerView3, recyclerView4, recyclerView5;
    questionAdapter serviceadapter1, serviceadapter2, serviceadapter3, serviceadapter4, serviceadapter5;
    View v;
    Button next;
    ArrayList<detailitemVO> detailList1 = new ArrayList<detailitemVO>();
    ArrayList<detailitemVO> detailList2 = new ArrayList<detailitemVO>();
    ArrayList<detailitemVO> detailList3 = new ArrayList<detailitemVO>();
    ArrayList<detailitemVO> detailList4 = new ArrayList<detailitemVO>();
    ArrayList<detailitemVO> detailList5 = new ArrayList<detailitemVO>();
    IntroManager intromanager;
    HMCoreData hmcoredata;
    Fragment myFragment;

    public fr_qapage1() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("debugging Menu", "Loaded Menu");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        intromanager = new IntroManager(getActivity());
        hmcoredata = new HMCoreData(getActivity());

        //  View v =  inflater.inflate(R.layout.fragment_fr_qapage1, container, false);
        v = inflater.inflate(R.layout.fragment_fr_qapage1, container, false);
        quest1 = (TextView) v.findViewById(R.id.quest1);
        quest2 = (TextView) v.findViewById(R.id.quest2);
        quest3 = (TextView) v.findViewById(R.id.quest3);
        quest4 = (TextView) v.findViewById(R.id.quest4);
        quest5 = (TextView) v.findViewById(R.id.quest5);

        new Util().setUpStepsBar(v, intromanager.getPageCount(), intromanager.getStepNo());

        lin1 = v.findViewById(R.id.lin1);
        lin2 = v.findViewById(R.id.lin2);
        lin3 = v.findViewById(R.id.lin3);
        lin4 = v.findViewById(R.id.lin4);
        lin5 = v.findViewById(R.id.lin5);

      /*  Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationIcon(R.drawable.ico_back);*/


        recyclerView1 = (RecyclerView) v.findViewById(R.id.recyclerView1);
        recyclerView2 = (RecyclerView) v.findViewById(R.id.recyclerView2);
        recyclerView3 = (RecyclerView) v.findViewById(R.id.recyclerView3);
        recyclerView4 = (RecyclerView) v.findViewById(R.id.recyclerView4);
        recyclerView5 = (RecyclerView) v.findViewById(R.id.recyclerView5);

        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);

        next = (Button) v.findViewById(R.id.btnnext);

        detailList1.clear();
        detailList2.clear();
        detailList3.clear();
        detailList4.clear();
        detailList5.clear();

        serviceadapter1 = new questionAdapter(getActivity(), (ArrayList<detailitemVO>) detailList1, R.layout.radio_option, 1, intromanager.getQAOption1());
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        recyclerView1.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView1.setAdapter(serviceadapter1);
        JSONObject myjson = null;
        try {
            myjson = new JSONObject(intromanager.getServiceOP());
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(myjson.getString("itemname"));
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(myjson.getString("unitprice"));
            JSONArray myArray = myjson.getJSONArray("question1");
            for (int i = 0; i < myArray.length(); i++) {
                lin1.setVisibility(View.VISIBLE);
                JSONObject objects = myArray.getJSONObject(i);
                if (objects.getString("narrationtype").equals("Q")) {
                    lin1.setVisibility(View.VISIBLE);
                    quest1.setText(objects.getString("narration"));
                } else {
                    Log.i("Debug", objects.getString("narrationtype") + " " + objects.getString("narration") + objects.getString("narrationcode") + objects.getString("narrationcode"));
                    detailitemVO a = new detailitemVO(objects.getString("narration"), objects.getString("narrationcode"), objects.getString("narrationcode"), 0);
                    detailList1.add(a);
                    Log.i("Debug", "First Item " + a.getDetailq() + " " + a.getDetailr());
                }
            }
            serviceadapter1.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        serviceadapter2 = new questionAdapter(getActivity(), (ArrayList<detailitemVO>) detailList2, R.layout.radio_option, 2, intromanager.getQAOption2());
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        recyclerView2.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView2.setAdapter(serviceadapter2);
        myjson = null;
        try {
            myjson = new JSONObject(intromanager.getServiceOP());
            JSONArray myArray = myjson.getJSONArray("question2");
            for (int i = 0; i < myArray.length(); i++) {
                lin2.setVisibility(View.VISIBLE);
                JSONObject objects = myArray.getJSONObject(i);
                if (objects.getString("narrationtype").equals("Q")) {
                    lin2.setVisibility(View.VISIBLE);
                    quest2.setText(objects.getString("narration"));
                } else {
                    Log.i("Debug", objects.getString("narrationtype") + " " + objects.getString("narration") + objects.getString("narrationcode") + objects.getString("narrationcode"));
                    detailitemVO a = new detailitemVO(objects.getString("narration"), objects.getString("narrationcode"), objects.getString("narrationcode"), 0);
                    detailList2.add(a);
                }
            }
            serviceadapter2.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        serviceadapter3 = new questionAdapter(getActivity(), (ArrayList<detailitemVO>) detailList3, R.layout.radio_option, 3, intromanager.getQAOption3());
        recyclerView3.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        recyclerView3.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView3.setAdapter(serviceadapter3);
        myjson = null;
        try {
            myjson = new JSONObject(intromanager.getServiceOP());
            JSONArray myArray = myjson.getJSONArray("question3");
            for (int i = 0; i < myArray.length(); i++) {
                lin3.setVisibility(View.VISIBLE);
                JSONObject objects = myArray.getJSONObject(i);
                if (objects.getString("narrationtype").equals("Q")) {
                    lin3.setVisibility(View.VISIBLE);
                    quest3.setText(objects.getString("narration"));
                } else {
                    detailitemVO a = new detailitemVO(objects.getString("narration"), objects.getString("narrationcode"), objects.getString("narrationcode"), 0);
                    detailList3.add(a);
                }
            }
            serviceadapter3.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        serviceadapter4 = new questionAdapter(getActivity(), (ArrayList<detailitemVO>) detailList4, R.layout.radio_option, 4, intromanager.getQAOption4());
        recyclerView4.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        recyclerView4.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView4.setAdapter(serviceadapter4);
        myjson = null;
        try {
            myjson = new JSONObject(intromanager.getServiceOP());
            JSONArray myArray = myjson.getJSONArray("question4");
            for (int i = 0; i < myArray.length(); i++) {
                lin4.setVisibility(View.VISIBLE);
                JSONObject objects = myArray.getJSONObject(i);
                if (objects.getString("narrationtype").equals("Q")) {
                    lin4.setVisibility(View.VISIBLE);
                    quest4.setText(objects.getString("narration"));
                } else {
                    detailitemVO a = new detailitemVO(objects.getString("narration"), objects.getString("narrationcode"), objects.getString("narrationcode"), 0);
                    detailList4.add(a);
                }
            }
            serviceadapter4.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        serviceadapter5 = new questionAdapter(getActivity(), (ArrayList<detailitemVO>) detailList5, R.layout.radio_option, 5, intromanager.getQAOption5());
        recyclerView5.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        recyclerView5.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView5.setAdapter(serviceadapter5);
        myjson = null;
        try {
            myjson = new JSONObject(intromanager.getServiceOP());
            JSONArray myArray = myjson.getJSONArray("question5");
            for (int i = 0; i < myArray.length(); i++) {
                lin5.setVisibility(View.VISIBLE);
                JSONObject objects = myArray.getJSONObject(i);
                if (objects.getString("narrationtype").equals("Q")) {
                    lin5.setVisibility(View.VISIBLE);
                    quest5.setText(objects.getString("narration"));
                } else {
                    detailitemVO a = new detailitemVO(objects.getString("narration"), objects.getString("narrationcode"), objects.getString("narrationcode"), 0);
                    detailList5.add(a);
                }
            }
            serviceadapter5.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean bool;
                if (lin1.getVisibility() == View.VISIBLE) {
                    bool = hmcoredata.checkForm(getActivity(), intromanager.getQAOption1());
                    if (!bool) {
                        return;
                    }
                }
                if (lin2.getVisibility() == View.VISIBLE) {
                    bool = hmcoredata.checkForm(getActivity(), intromanager.getQAOption2());
                    if (!bool) {
                        return;
                    }
                }
                if (lin3.getVisibility() == View.VISIBLE) {
                    bool = hmcoredata.checkForm(getActivity(), intromanager.getQAOption3());
                    if (!bool) {
                        return;
                    }
                }
                if (lin4.getVisibility() == View.VISIBLE) {
                    bool = hmcoredata.checkForm(getActivity(), intromanager.getQAOption4());
                    if (!bool) {
                        return;
                    }
                }
                if (lin5.getVisibility() == View.VISIBLE) {
                    bool = hmcoredata.checkForm(getActivity(), intromanager.getQAOption5());
                    if (!bool) {
                        return;
                    }
                }
                try {
                    JSONObject myjson = new JSONObject(intromanager.getServiceOP());
                    JSONArray json_array_pageflow = myjson.getJSONArray("pageflow");
                    if ((json_array_pageflow.length()) == intromanager.getPageSequence()) {
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
                    if (myFragment == null) {
                        hmcoredata.showToast(getActivity(), "Page flow parameters not set. Unable to proceed with the next page flow");
                    } else {
                        intromanager.stepNoIncrement();
                        intromanager.setPageSequence(intromanager.getPageSequence() + 1);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    hmcoredata.showToast(getActivity(), e.getMessage());
                }
            }
        });

        Log.d("debugging", "sequence =  " + intromanager.getPageSequence());
        Log.d("debugging", "step =  " + intromanager.getStepNo());

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
