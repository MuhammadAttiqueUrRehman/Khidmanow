package layout;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.aic.khidmanow.detailServiceAdapter;
import com.aic.khidmanow.detailServiceAdapteri;
import com.aic.khidmanow.detailitemVO;
import com.aic.khidmanow.providerInfo;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_servicedetail extends Fragment implements AsyncResponse {
    HMCoreData myDB;
    HMAppVariables appVariables = new HMAppVariables();
    Toolbar mtoolbar;
    TextView mTitle, mSubTitle, itemname, itemsubname;
    RecyclerView recyclerView, recyclerView1, recyclerView2;
    detailServiceAdapter serviceadapter, serviceadapter1;
    detailServiceAdapteri serviceadapter2;
    ProgressBar progressBar4;
    View v;
    ArrayList<detailitemVO> detailList = new ArrayList<detailitemVO>();
    ArrayList<detailitemVO> detailList1 = new ArrayList<detailitemVO>();
    ArrayList<detailitemVO> detailList2 = new ArrayList<detailitemVO>();
    ImageView img;
    public IntroManager intromanager;
    NestedScrollView accscroll;
    Button booknow;
    JSONArray json_array_pageflow;
    Fragment myFragment;
    providerInfo providerinfo;

    public fr_servicedetail() {
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
        // Initialize item data
        MainActivity.bottomNavigationView.setVisibility(View.GONE);
        intromanager = new IntroManager(getActivity());
        providerinfo = new providerInfo(getActivity());
        intromanager.setOfferCode("");
        intromanager.setServiceOP("");
        intromanager.setGeoLocation("");
        intromanager.setAddressType("");
        intromanager.setBookTime("");
        intromanager.setItemCode("");
        intromanager.setServiceImage1("");
        intromanager.setServiceImage2("");
        intromanager.setServiceImage3("");
        intromanager.setServiceImage4("");
        intromanager.setAdditionalInfo("");
        intromanager.setQAOption1("0000");
        intromanager.setQAOption2("0000");
        intromanager.setQAOption3("0000");
        intromanager.setQAOption4("0000");
        intromanager.setQAOption5("0000");
        intromanager.setSubcategory("");
        intromanager.setQtyDesc("");
        intromanager.setQty(1);
        intromanager.setProviderCount(0);
        intromanager.setServiceRating(0);
        intromanager.setPersonLimit(0);
        intromanager.setMultiChoicePage("");
        intromanager.setPriorAppointment(0);
        intromanager.setAdvBookingDays(0);
        intromanager.setMultiChoiceQuestion("");
        intromanager.setDisableSchedule("");
        intromanager.setLocationType("");
        intromanager.setPricingType("");
        intromanager.setVisitingCharges("");
        intromanager.setShowEndDate("");
        intromanager.setShowPaymentPage("");
        intromanager.setPageSequence(0);
        intromanager.setShowAddress("");
        intromanager.setEndDate("");
        intromanager.setCarryLocation("");
        intromanager.setMultiFormType("");
        intromanager.setItemName("");
        providerinfo.clearAllData();
        providerinfo.clearServiceItem();
        /*dictionary.Add("personlimit", .itmNumLmt)
        dictionary.Add("multichoicepage", .itmMltChc)
        dictionary.Add("priorappointment", .itmPrrApp)
        dictionary.Add("advancebookingdays", .itmAdvDay)
        dictionary.Add("multichoicequestion", .itmMltQus)
        dictionary.Add("disableschedule", .itmDisSch)
        dictionary.Add("locationtype", .itmLocTyp)
        dictionary.Add("pricingtype", .itmComDsp)
        dictionary.Add("visitingcharges", .itmLedPrc)
        dictionary.Add("showenddate", .itmEndDat)
        dictionary.Add("showpaymentpage", .itmShwPmt)
        dictionary.Add("showaddress", .itmShwAdd)*/
       /* intromanager.setPage1("");
        intromanager.setPage2("");
        intromanager.setPage3("");
        intromanager.setPage4("");
        intromanager.setPage5("");
        intromanager.setPage6("");
        intromanager.setPage7("");
        intromanager.setPage8("");*/
        try {
            myDB = new HMCoreData(getActivity());
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return null;
        }
        v = inflater.inflate(R.layout.fragment_fr_servicedetail, container, false);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);
//        mtoolbar.setNavigationIcon(R.drawable.ico_back);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView1 = (RecyclerView) v.findViewById(R.id.recyclerView1);
        recyclerView2 = (RecyclerView) v.findViewById(R.id.recyclerView2);
        progressBar4 = (ProgressBar) v.findViewById(R.id.progressBar4);
        itemname = (TextView) v.findViewById(R.id.itemname);
        itemsubname = (TextView) v.findViewById(R.id.unitprice);
        img = (ImageView) v.findViewById(R.id.detailimage);
        booknow = (Button) v.findViewById(R.id.btnbook);
        accscroll = (NestedScrollView) v.findViewById(R.id.accscroll);
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("itemcode", getArguments().getString("itemcode"));
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
        }
        Log.i("debugging", "Service Detail Input :" + jsonParam.toString());
        String[] myTaskParams = {"/SSMServiceitemDetail", jsonParam.toString()};
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar4, "SSMServiceitemDetail");
            aasyncTask.delegate = (AsyncResponse) fr_servicedetail.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return null;
        }
        accscroll.setVisibility(GONE);

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("subcategory", intromanager.getSubcategory());
                myFragment = new fr_product_list();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });


        booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    JSONObject myjson = new JSONObject(intromanager.getServiceOP());
                    JSONArray json_array_pageflow = myjson.getJSONArray("pageflow");
                    Log.i("debugging", "Page Sequence Array :" + json_array_pageflow.toString());
                    Log.i("debugging", "Page Qty :" + json_array_pageflow.length());
                    intromanager.setPageCount(json_array_pageflow.length());
                    intromanager.setStepNo(0);
                    for (int i = 0; i < json_array_pageflow.length(); i++) {
                        JSONObject objects = json_array_pageflow.getJSONObject(i);
                        Log.i("debugging", "Page Sequence :" + intromanager.getPageSequence() + " Counter " + i);
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
                        myDB.showToast(getActivity(), "Page flow parameters not set. Unable to proceed with the next page flow");
                    } else {
                        intromanager.setPageSequence(intromanager.getPageSequence() + 1);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.showToast(getContext(), e.getMessage());
                }
            }
        });
        return v;
    }


    @Override
    public void processFinish(String output, String mview) throws HMOwnException, JSONException {
        // RecyclerView.LayoutManager mLayoutManager;
        JSONObject objects;
        Log.i("debugging", "Service Detail Input :" + output);
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        output = new Util().processJsonForLanguage(output, getContext());
        intromanager = new IntroManager(getActivity());
        intromanager.setServiceOP(output);
        JSONObject myjson = new JSONObject(output);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(myjson.getString("itemname"));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(myjson.getString("unitprice"));
        JSONArray json_array_servicedetails = myjson.getJSONArray("servicedetails");
        JSONArray json_array_servicepricing = myjson.getJSONArray("servicepricing");
        JSONArray json_array_serviceincludeexclude = myjson.getJSONArray("serviceincludeexclude");
        json_array_pageflow = myjson.getJSONArray("pageflow");
        intromanager.setItemCode(myjson.getString("itemcode"));
        intromanager.setItemName(myjson.getString("itemname"));
        intromanager.setSubcategory(myjson.getString("subservicecategorycode"));
        intromanager.setQtyDesc(myjson.getString("enableqty"));
//        Log.i("debugging", "Service Detail Input :" + myjson.getString("enableqty"));
        itemname.setText(myjson.getString("itemname"));
        itemsubname.setText(myjson.getString("unitprice"));

        intromanager.setProviderCount(Integer.parseInt(myjson.getString("providercount")));
        intromanager.setServiceRating(Integer.parseInt(myjson.getString("servicerating")));
        intromanager.setPersonLimit(Integer.parseInt(myjson.getString("personlimit")));
        intromanager.setMultiChoicePage(myjson.getString("multichoicepage"));
        intromanager.setPriorAppointment(Integer.parseInt(myjson.getString("priorappointment")));
        intromanager.setAdvBookingDays(Integer.parseInt(myjson.getString("advancebookingdays")));
        intromanager.setMultiChoiceQuestion(myjson.getString("multichoicequestion"));
        intromanager.setDisableSchedule(myjson.getString("disableschedule"));
        intromanager.setLocationType(myjson.getString("locationtype"));
        intromanager.setPricingType(myjson.getString("pricingtype"));
        intromanager.setVisitingCharges(myjson.getString("visitingcharges"));
        intromanager.setShowEndDate(myjson.getString("showenddate"));
        intromanager.setShowPaymentPage(myjson.getString("showpaymentpage"));
        intromanager.setShowAddress(myjson.getString("showaddress"));

        Picasso.get()
                .load(myjson.getString("itemimage"))
                .placeholder(R.drawable.haal_meer_large)
                .fit()
                .noFade()
                .centerCrop()
                .into(img);
        //Service Details
        detailList.clear();
        serviceadapter = new detailServiceAdapter(getActivity(), (ArrayList<detailitemVO>) detailList, R.layout.detail_service_box);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        // recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(serviceadapter);
        //  ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(myjson.getString("subcategoryname"));
        //  ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(myjson.getString("availableproviders") + " providers    " + myjson.getString("starrating") + "  Rating  by " + myjson.getString("availableproviders") + " Customers");
        //  ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(R.drawable.un_white);
        for (int i = 0; i < json_array_servicedetails.length(); i++) {
            objects = json_array_servicedetails.getJSONObject(i);
            detailitemVO a = new detailitemVO(objects.getString("detailquery"), objects.getString("detailresponse"), "", 0);
            detailList.add(a);
        }
        serviceadapter.notifyDataSetChanged();

        //Service Pricing
        detailList1.clear();
        serviceadapter1 = new detailServiceAdapter(getActivity(), (ArrayList<detailitemVO>) detailList1, R.layout.detail_service_box);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //  recyclerView1.setHasFixedSize(true);
        recyclerView1.setAdapter(serviceadapter1);
        //  ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(myjson.getString("subcategoryname"));
        //  ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(myjson.getString("availableproviders") + " providers    " + myjson.getString("starrating") + "  Rating  by " + myjson.getString("availableproviders") + " Customers");
        //  ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(R.drawable.un_white);
        for (int i = 0; i < json_array_servicepricing.length(); i++) {
            objects = json_array_servicepricing.getJSONObject(i);
            detailitemVO a = new detailitemVO(objects.getString("detailquery"), objects.getString("detailresponse"), "", 0);
            detailList1.add(a);
        }
        serviceadapter1.notifyDataSetChanged();

        //Service Pricing
        detailList2.clear();
        serviceadapter2 = new detailServiceAdapteri(getActivity(), (ArrayList<detailitemVO>) detailList2, R.layout.detail_service_box_i);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //  recyclerView2.setHasFixedSize(true);
        recyclerView2.setAdapter(serviceadapter2);
        //  ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(myjson.getString("subcategoryname"));
        //  ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(myjson.getString("availableproviders") + " providers    " + myjson.getString("starrating") + "  Rating  by " + myjson.getString("availableproviders") + " Customers");
        //  ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(R.drawable.un_white);
        for (int i = 0; i < json_array_serviceincludeexclude.length(); i++) {
            objects = json_array_serviceincludeexclude.getJSONObject(i);
            detailitemVO a = new detailitemVO(objects.getString("detailresponse"), objects.getString("incexctype"), "X", 0);
            detailList2.add(a);
        }
        serviceadapter2.notifyDataSetChanged();
        booknow.setVisibility(View.VISIBLE);
        accscroll.setVisibility(View.VISIBLE);
    }


}




