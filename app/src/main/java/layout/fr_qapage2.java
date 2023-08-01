package layout;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.IntroManager;
import com.aic.khidmanow.MainActivity;
import com.aic.khidmanow.R;
import com.aic.khidmanow.detailitemVO;
import com.aic.khidmanow.questionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_qapage2 extends Fragment {
    Toolbar mtoolbar;
    TextView mTitle,mSubTitle;
    RecyclerView recyclerView;
    questionAdapter serviceadapter;
    View v;
    Button next;
    ArrayList<detailitemVO> detailList = new ArrayList<detailitemVO>();
    IntroManager intromanager;
    HMCoreData hmcoredata;
    Fragment myFragment;
    RelativeLayout rl;
    TextView quest;
    EditText addressloc;
    ImageButton bookdate;
    String bookingdate, bd;
    public fr_qapage2() {
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
        intromanager=new IntroManager(getActivity());
        hmcoredata = new HMCoreData(getActivity());
        v = inflater.inflate(R.layout.fragment_fr_qapage2, container, false);
        quest = v.findViewById(R.id.quest1);
        rl = v.findViewById(R.id.dateset);
        bookdate= v.findViewById(R.id.btnlocation);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        intromanager=new IntroManager(getActivity());
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255,255,255), PorterDuff.Mode.SRC_IN);
        next=(Button) v.findViewById(R.id.btnnext);
        bookdate= v.findViewById(R.id.btnlocation);
        final Calendar mcurrentTime = Calendar.getInstance();
        final int day=mcurrentTime.get(Calendar.DAY_OF_MONTH);
        final int month=mcurrentTime.get(Calendar.MONTH);
        final int year=mcurrentTime.get(Calendar.YEAR);
        String sday="00"+Integer.toString(day);
        String smonth="00"+Integer.toString(month+1);
        String syear=Integer.toString(year);
        sday=sday.substring(sday.length()-2);
        smonth=smonth.substring(smonth.length()-2);

        Log.i("SHow End Date", intromanager.getShowEndDate());
        if (intromanager.getShowEndDate().equals("1")) {
            rl.setVisibility(v.VISIBLE);

            if (intromanager.getEndDate().equals("")) {
                addressloc.setText(sday + "-" + smonth + "-" + syear);
                bookingdate = sday + ":" + smonth + ":" + syear;
                bd = sday + smonth + syear;
            } else {
                String bdt = intromanager.getEndDate().substring(0, 10);
                bookingdate = bdt.substring(0, 2) + ":" + bdt.substring(3, 5) + ":" + bdt.substring(6, 10);
                bd = bdt.substring(0, 2) + bdt.substring(3, 5) + bdt.substring(6, 10);
                bdt = bdt.substring(0, 2) + "-" + bdt.substring(3, 5) + "-" + bdt.substring(6, 10);
             //   Log.i("Set Formatted Date", bdt);
             //   Log.i("Set raw Date", bd);
                addressloc.setText(bdt);
              //  Log.i("Get Data for Hour", intromanager.getEndDate());
              //  Log.i("Get Data for Hour", intromanager.getEndDate().substring(11, 13));
            }
        }
        serviceadapter = new questionAdapter(getActivity(), (ArrayList<detailitemVO>) detailList,R.layout.radio_option,2,intromanager.getQAOption2());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(serviceadapter);
        detailList.clear();
        JSONObject myjson = null;
        try {
            myjson = new JSONObject(intromanager.getServiceOP());
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(myjson.getString("itemname"));
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(myjson.getString("unitprice"));
            JSONArray myArray = myjson.getJSONArray("question2");
            for (int i = 0; i < myArray.length(); i++) {
                JSONObject objects = myArray.getJSONObject(i);
                if (objects.getString("narrationtype").equals("Q")) {
                    quest.setText(objects.getString("narration"));
                } else {
                    detailitemVO a = new detailitemVO(objects.getString("narration"), objects.getString("narrationcode"), objects.getString("narrationcode"),0);
                    detailList.add(a);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            hmcoredata.showToast(getActivity(), e.getMessage());
            return null;
        }

            bookdate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    final Calendar c = Calendar.getInstance();
                    final int day=c.get(Calendar.DAY_OF_MONTH);
                    final int month=c.get(Calendar.MONTH);
                    final int year=c.get(Calendar.YEAR);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        DatePickerDialog StartTime = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker view, int year1, int monthOfYear, int dayOfMonth) {
                                String sday = "00" + Integer.toString(dayOfMonth);
                                String smonth = "00" + Integer.toString(monthOfYear+1);
                                String syear = Integer.toString(year1);
                                sday = sday.substring(sday.length() - 2);
                                smonth = smonth.substring(smonth.length() - 2);
                                bookingdate = sday + ":" + smonth + ":" + syear;
                                bd = sday + smonth + syear;
                                addressloc.setText(sday + "-" + smonth + "-" + syear);
                            }
                        }, year, month,day);
                        StartTime.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        StartTime.show();
                    }
                }
            });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intromanager.getShowEndDate().equals("1") && intromanager.getQAOption2().equals("3134")) {
                    hmcoredata.showToast(getActivity(),"Select End Date");
                    return;
                }

                Boolean bool = hmcoredata.checkForm(getActivity(),intromanager.getQAOption2());
                if (!bool){
                    return;
                }

                try {
                    JSONObject myjson = new JSONObject(intromanager.getServiceOP());
                    JSONArray json_array_pageflow = myjson.getJSONArray("pageflow");
                    if ((json_array_pageflow.length())==intromanager.getPageSequence()){
                        if (!MainActivity.slogged){
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
                            }else if (objects.getString("pagecode").equals("fr_book_address")) {
                                myFragment = new fr_book_address();
                            }else if (objects.getString("pagecode").equals("fr_book_toaddress")) {
                                myFragment = new fr_book_toaddress();
                            } else if (objects.getString("pagecode").equals("fr_book_addinfotext")) {
                                myFragment = new fr_bookaddinfotext();
                            } else if (objects.getString("pagecode").equals("fr_book_addinfo")) {
                                myFragment = new fr_book_addinfo();
                            }  else if (objects.getString("pagecode").equals("fr_book_qty")) {
                                myFragment = new fr_book_qty();
                            }  else if (objects.getString("pagecode").equals("fr_book_time")) {
                                myFragment = new fr_book_time();
                            }

                        }
                    }
                    intromanager.setPageSequence(intromanager.getPageSequence()+1);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                }catch (JSONException e) {
                    e.printStackTrace();
                    // myDB.showToast(getActivity(), e.getMessage());
                }
            }
        });

    return v;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem items){
        int id=items.getItemId();
        intromanager.setPageSequence(intromanager.getPageSequence()-1);
        if (id == android.R.id.home){
            if( getActivity().getSupportFragmentManager().getBackStackEntryCount()>0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }
}
