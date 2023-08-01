package layout;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.aic.khidmanow.detailitemVO;
import com.aic.khidmanow.questionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class fr_book_time extends Fragment implements AsyncResponse {
    questionAdapter serviceadapter;
    CalendarView cv_appoint_date;
    ImageButton bookdate;
    Toolbar mtoolbar;
    TextView mTitle, mSubTitle;
    View v;
    String first;
    RecyclerView recyclerView;
    Button btnnext;
    EditText txtlandmark, addressloc;
    String bookingdate, bd;
    IntroManager introManager;
    HMCoreData hmcoredata;
    Fragment myFragment;
    LinearLayout ln, la;
    ArrayList<detailitemVO> detailList = new ArrayList<detailitemVO>();
    ProgressBar progressBar4a;
    HMAppVariables appVariables = new HMAppVariables();

    // RadioButton time08_10,time10_12,time12_14,time14_16,time16_18,time18_20,time20_22;
    public fr_book_time() {
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            hmcoredata = new HMCoreData(getActivity());
            appVariables = hmcoredata.getUserData();
        } catch (HMOwnException e) {
            e.printStackTrace();
        }
        introManager = new IntroManager(getActivity());
        v = inflater.inflate(R.layout.fragment_fr_book_time, container, false);

        new Util().setUpStepsBar(v, introManager.getPageCount(), introManager.getStepNo());
//        ((AppCompatImageView) v.findViewById(R.id.iv_step_01)).setImageResource(R.drawable.ic_step_done);
//        ((AppCompatImageView) v.findViewById(R.id.iv_step_02)).setImageResource(R.drawable.ic_step_done);
//        ((AppCompatImageView) v.findViewById(R.id.iv_step_03)).setImageResource(R.drawable.ic_step_done);
//        ((AppCompatImageView) v.findViewById(R.id.iv_step_04)).setImageResource(R.drawable.ic_step_done);

        ln = v.findViewById(R.id.addlabel);
        la = v.findViewById(R.id.appsch);
        if (introManager.getShowAddress().equals("1")) {
            ln.setVisibility(v.GONE);
        }
        if (introManager.getDisableSchedule().equals("1")) {
            la.setVisibility(v.GONE);
        }
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);
        btnnext = v.findViewById(R.id.btnnext);
        txtlandmark = v.findViewById(R.id.txtAddinfo);
        addressloc = v.findViewById(R.id.addressloc);
        progressBar4a = v.findViewById(R.id.progressBar4a);
        addressloc.setEnabled(false);
        cv_appoint_date = v.findViewById(R.id.cv_appoint_date);
        bookdate = v.findViewById(R.id.btnlocation);
        serviceadapter = new questionAdapter(getActivity(), (ArrayList<detailitemVO>) detailList, R.layout.time_radio_option, 10, introManager.getBookTime());
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(serviceadapter);
        final Calendar mcurrentTime = Calendar.getInstance();
        final int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
        final int month = mcurrentTime.get(Calendar.MONTH);
        final int year = mcurrentTime.get(Calendar.YEAR);
        String sday = "00" + Integer.toString(day);
        String smonth = "00" + Integer.toString(month + 1);
        String syear = Integer.toString(year);
        sday = sday.substring(sday.length() - 2);
        smonth = smonth.substring(smonth.length() - 2);

        if (introManager.getBookTime().length() < 10) {
            addressloc.setText(sday + "-" + smonth + "-" + syear);
            bookingdate = sday + ":" + smonth + ":" + syear;
            bd = sday + smonth + syear;
        } else {
            String bdt = introManager.getBookTime().substring(0, 10);
            bookingdate = bdt.substring(0, 2) + ":" + bdt.substring(3, 5) + ":" + bdt.substring(6, 10);
            bd = bdt.substring(0, 2) + bdt.substring(3, 5) + bdt.substring(6, 10);
            bdt = bdt.substring(0, 2) + "-" + bdt.substring(3, 5) + "-" + bdt.substring(6, 10);
            Log.i("Set Formatted Date", bdt);
            Log.i("Set raw Date", bd);
            addressloc.setText(bdt);
            Log.i("Get Data for Hour", introManager.getBookTime());
            Log.i("Get Data for Hour", introManager.getBookTime().substring(11, 13));
            txtlandmark.setText(introManager.getLandmark());
        }

        if (!(introManager.getLocationType().equals("2"))) {
            JSONObject myjson = null;
            detailList.clear();
            try {
                myjson = new JSONObject(introManager.getServiceOP());
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(myjson.getString("itemname"));
                ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(myjson.getString("unitprice"));
                Log.i("debugging", "Disable Status inner " + introManager.getDisableSchedule());
                Log.i("debugging", "QA option 1 inner" + introManager.getQAOption1());
                if (introManager.getDisableSchedule().equals("")) {
                    if ((!introManager.getQAOption1().equals("2950"))) {
                        JSONArray myArray = myjson.getJSONArray("appointmentlist");
                        for (int i = 0; i < myArray.length(); i++) {
                            JSONObject objects = myArray.getJSONObject(i);
                            detailitemVO a = new detailitemVO(objects.getString("appointmentname"), objects.getString("appointmentcode"), objects.getString("appointmentavailibility"), Integer.parseInt(objects.getString("appointmentfree")));
                            detailList.add(a);
                            Log.i("Debugging", "Appointment inner " + a.toString());
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            serviceadapter.notifyDataSetChanged();
        } else {
            Log.i("Debugging", "Disable Status " + introManager.getDisableSchedule());
            Log.i("Debugging", "QA option 1 " + introManager.getQAOption1());
            callAppointmentList();
        }

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first = "";
                if (bookingdate.contains("null") || bookingdate.contains("NULL")) {
                    hmcoredata.showToast(getActivity(), getString(R.string.invalid_booking_date));
                    return;
                }
                if (introManager.getShowAddress().equals("")) {
                    if (txtlandmark.getText().toString().trim().equals("")) {
                        txtlandmark.setError(getString(R.string.invalid_address_landmark));
                        return;
                    }
                    if (txtlandmark.getText().toString().trim().length() == 0) {
                        txtlandmark.setError(getString(R.string.enter_landmark));
                        return;
                    }
                }

                introManager.setLandmark(txtlandmark.getText().toString());
                final Calendar mcurrentTime = Calendar.getInstance();
                final int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
                final int month = mcurrentTime.get(Calendar.MONTH);
                final int year = mcurrentTime.get(Calendar.YEAR);
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                String cday = "00" + Integer.toString(day);
                String cmonth = "00" + Integer.toString(month + 1);
                String cyear = Integer.toString(year);
                cday = cday.substring(cday.length() - 2);
                cmonth = cmonth.substring(cmonth.length() - 2);

                if (introManager.getDisableSchedule().equals("")) {
                    if ((!introManager.getQAOption1().equals("2950"))) {
                        if (introManager.getBookTime().equals("")) {
                            hmcoredata.showToast(getActivity(), getString(R.string.select_a_appointment_time));
                            return;
                        }

                        Log.i("Debugging", "Booking Date " + introManager.getBookTime());
                        Log.i("Debugging", "Date Check Selected" + bd);
                        //Check for same date and 4 hours advance booking selection
                        int bt = Integer.parseInt(introManager.getBookTime().substring(0, 2));
                        Log.i("Debugging", "time all " + introManager.getBookTime());
                        Log.i("Debugging", "time Selected" + introManager.getBookTime().substring(0, 2));
                        Log.i("Debugging", "Original day" + bd.substring(0, 2));
                        Log.i("Selected day", cday);
                        if (cday.equals(bd.substring(0, 2))) {
                            if ((hour - introManager.getPriorAppointment()) > bt) {
                                hmcoredata.showToast(getActivity(), getString(R.string.you_have_selected_a_past_time));
                                return;
                            }
                            if ((hour + introManager.getPriorAppointment()) > bt) {
                                hmcoredata.showToast(getActivity(), getString(R.string.you_have_to_select_appointment , introManager.getPriorAppointment()));
                                return;
                            }
                        }

                        if (introManager.getLocationType().equals("2")) {
                            checkScheduleExists();
                        } else {
                            nextPage();
                        }
                    } else {
                        introManager.setBookTime("00:00:00");
                        nextPage();
                    }
                } else {
                    introManager.setBookTime("00:00:00");
                    nextPage();
                }


            }
            //    }
            //  }
        });

        bookdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                final int day = c.get(Calendar.DAY_OF_MONTH);
                final int month = c.get(Calendar.MONTH);
                final int year = c.get(Calendar.YEAR);

                //     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                DatePickerDialog StartTime = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year1, int monthOfYear, int dayOfMonth) {
                        String sday = "00" + Integer.toString(dayOfMonth);
                        String smonth = "00" + Integer.toString(monthOfYear + 1);
                        String syear = Integer.toString(year1);
                        sday = sday.substring(sday.length() - 2);
                        smonth = smonth.substring(smonth.length() - 2);
                        bookingdate = sday + ":" + smonth + ":" + syear;
                        bd = sday + smonth + syear;
                        addressloc.setText(sday + "-" + smonth + "-" + syear);
//                        addressloc.setText(bd);
                        if ((introManager.getLocationType().equals("2"))) {
                            callAppointmentList();
                        }
                    }
                }, year, month, day);
                StartTime.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                StartTime.show();
                //    }
                Log.i("debugging", "Second Call " + introManager.getLocationType());


            }
        });

        Log.d("debugging", "sequence =  " + introManager.getPageSequence());
        Log.d("debugging", "step =  " + introManager.getStepNo());

        setCalendar();

        return v;
    }

    private void setCalendar() {
        Calendar today = Calendar.getInstance();
        cv_appoint_date.setMinDate(today.getTimeInMillis());

        cv_appoint_date.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                bookingdate = dayOfMonth + ":" + (((month + 1) < 10) ? "0" + (month + 1) : (month + 1)) + ":" + year;
                bd = dayOfMonth + "" + (((month + 1) < 10) ? "0" + (month + 1) : (month + 1)) + "" + year;
//                addressloc.setText(bd);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        int id = items.getItemId();
        introManager.stepNoDecrement();
        introManager.setPageSequence(introManager.getPageSequence() - 1);
        if (id == android.R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }


    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        output = new Util().processJsonForLanguage(output, getContext());
        JSONObject myjson = new JSONObject(output);
        if (handle == "SSMCheckBookingExists") {
            if (myjson.getString("statuscode").equals("108")) {
                hmcoredata.showToast(getActivity(), myjson.getString("statusdesc"));
                return;
            } else {

                nextPage();

            }

        } else if (handle == "SSMAppointmentList") {
            detailList.clear();
            try {
                JSONArray myArray = myjson.getJSONArray("appointmentlist");
                for (int i = 0; i < myArray.length(); i++) {
                    JSONObject objects = myArray.getJSONObject(i);
                    detailitemVO a = new detailitemVO(objects.getString("appointmentname"), objects.getString("appointmentcode"), objects.getString("appointmentavailibility"), Integer.parseInt(objects.getString("appointmentfree")));
                    detailList.add(a);
                    Log.i("Debugging", "Appointment Item :" + a.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            serviceadapter.notifyDataSetChanged();
        }
    }


    private void callAppointmentList() {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("mdevice", introManager.getDevice());
            jsonMain.put("reference", introManager.getItemCode());
            jsonMain.put("fromdate", bd);
            jsonMain.put("outlet", introManager.getCarryLocation());
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        Log.i("Debugging", "Appointment Request :" + jsonParam.toString());
        String[] myTaskParams = {"/SSMAppointmentList", jsonParam.toString()};
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar4a, "SSMAppointmentList");
            aasyncTask.delegate = (AsyncResponse) fr_book_time.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void checkScheduleExists() {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("customer", appVariables.ucustomerid);
            jsonMain.put("certificate", appVariables.ucertificate);
            jsonMain.put("mdevice", introManager.getDevice());
            jsonMain.put("reference", introManager.getBookTime().substring(0, 2) + introManager.getBookTime().substring(3, 5));
            jsonMain.put("fromdate", bd);
            jsonMain.put("itemcode", introManager.getItemCode());
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        Log.i("Debugging", "Appointment Request :" + jsonParam.toString());
        String[] myTaskParams = {"/SSMCheckBookingExists", jsonParam.toString()};
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar4a, "SSMCheckBookingExists");
            aasyncTask.delegate = (AsyncResponse) fr_book_time.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void nextPage() {
        introManager.setBookTime(bookingdate + ":" + introManager.getBookTime());
        try {
            JSONObject myjson = new JSONObject(introManager.getServiceOP());
            JSONArray json_array_pageflow = myjson.getJSONArray("pageflow");
            Log.i("debugging", "Checking Page Flow  " + Integer.toString(json_array_pageflow.length()) + " " + Integer.toString(introManager.getPageSequence()));
            if ((json_array_pageflow.length() == introManager.getPageSequence())) {
                if (!MainActivity.slogged) {
                    Log.i("debugging", "Came to Login Form " + MainActivity.slogged);
                    myFragment = new fr_login();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                    return;
                } else {
                    myFragment = new fr_book_checkout();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                    return;
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
}
