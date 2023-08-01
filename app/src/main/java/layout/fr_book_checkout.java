package layout;


import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.text.HtmlCompat;
import androidx.core.widget.NestedScrollView;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aic.khidmanow.AsyncResponse;
import com.aic.khidmanow.HMAppVariables;
import com.aic.khidmanow.HMConstants;
import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.HMDataAccess;
import com.aic.khidmanow.HMOwnException;
import com.aic.khidmanow.HMProductList;
import com.aic.khidmanow.IntroManager;
import com.aic.khidmanow.MainActivity;
import com.aic.khidmanow.R;
import com.aic.khidmanow.Util;
import com.aic.khidmanow.cartAdapterProd;
import com.aic.khidmanow.couponDialog;
import com.aic.khidmanow.crossCartAdapter;
import com.aic.khidmanow.multiHolder;
import com.aic.khidmanow.providerInfo;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import layout.utils.LocalManager;

import static android.view.View.GONE;


public class fr_book_checkout extends Fragment implements AsyncResponse, couponDialog.OnFragmentInteractionListener, cartAdapterProd.ClickEvent, crossCartAdapter.ClickEvent {
    LinearLayout.LayoutParams params, params1;
    LinearLayout servicedet;
    public static LinearLayout qa6, fillcart, emptycart;
    HMCoreData myDB;
    HMAppVariables appVariables = new HMAppVariables();
    Toolbar mtoolbar;
    TextView mTitle, mSubTitle, itemname, itemsubname;
    ProgressBar progressBar4a;
    View v;
    ImageView img;
    EditText coupon;
    View rl_directnego;
    TextView detailQ1, detailQ2, detailQ3, detailQ4, detailQ5, detailR1, detailR2, detailR3, detailR4, detailR5, multiQ6;
    TextView charge_type, taxdesc, taxamount, totaldesc, totalamount, disclaimer, disclaimer1, totprice, multiR6;
    IntroManager intromanager;
    Button booknow, btnshop;
    ImageButton btnbook1, btnsearch;
    NestedScrollView accscroll;
    HMProductList hmProductList = new HMProductList();
    String estimate = "", mitem = "";
    public Double dtaxamount = 0.0, dtotalamount = 0.0, walletbalance = 0.0, mqtyprice = 0.0, mprice = 0.0;
    public static Double dpriceamount = 0.0;
    providerInfo providerinfo;
    RecyclerView recyclerView, crossrecycle;
    cartAdapterProd adapter;
    crossCartAdapter crossadapter;
    ArrayList<String> ar = new ArrayList<String>();
    ArrayList<String> ar1 = new ArrayList<String>();
    String ccy = appVariables.ucurrency;
//    DecimalFormat formatter = new DecimalFormat("#,###,###.##");

    public fr_book_checkout() {
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
        // Inflate the layout for this fragment
        intromanager = new IntroManager(getActivity());
        providerinfo = new providerInfo(getActivity());
        intromanager.setCouponRef("");
        try {
            myDB = new HMCoreData(getActivity());
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return null;
        }

        v = inflater.inflate(R.layout.fragment_fr_book_checkout, container, false);
        MainActivity.bottomNavigationView.setVisibility(View.GONE);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.verify_order);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);
        img = v.findViewById(R.id.detailimage);
        itemname = v.findViewById(R.id.itemname);
        qa6 = v.findViewById(R.id.qa6);
        fillcart = v.findViewById(R.id.fillcart);
        emptycart = v.findViewById(R.id.emptycart);
        servicedet = v.findViewById(R.id.servicedet);
        progressBar4a = (ProgressBar) v.findViewById(R.id.progressBar4a);
        booknow = (Button) v.findViewById(R.id.btnbook);
        btnshop = (Button) v.findViewById(R.id.btnshop);
        btnbook1 = (ImageButton) v.findViewById(R.id.btnclear);
        btnsearch = (ImageButton) v.findViewById(R.id.btnsearch);
        accscroll = (NestedScrollView) v.findViewById(R.id.accscroll);
        detailQ1 = (TextView) v.findViewById(R.id.detailQ1);
        detailQ2 = (TextView) v.findViewById(R.id.detailQ2);
        detailQ3 = (TextView) v.findViewById(R.id.detailQ3);
        detailQ4 = (TextView) v.findViewById(R.id.detailQ4);
        detailQ5 = (TextView) v.findViewById(R.id.detailQ5);
        detailR1 = (TextView) v.findViewById(R.id.detailR1);
        detailR2 = (TextView) v.findViewById(R.id.detailR2);
        detailR3 = (TextView) v.findViewById(R.id.detailR3);
        detailR4 = (TextView) v.findViewById(R.id.detailR4);
        detailR5 = (TextView) v.findViewById(R.id.detailR5);
        multiQ6 = (TextView) v.findViewById(R.id.multiQ6);
        multiR6 = (TextView) v.findViewById(R.id.multiR6);
        qa6 = v.findViewById(R.id.qa6);
        rl_directnego = v.findViewById(R.id.rl_directnego);
        charge_type = (TextView) v.findViewById(R.id.charge_type);
        taxdesc = (TextView) v.findViewById(R.id.taxdesc);
        taxamount = (TextView) v.findViewById(R.id.taxamount);
        totaldesc = (TextView) v.findViewById(R.id.totaldesc);
        totalamount = (TextView) v.findViewById(R.id.totalamount);
        disclaimer = (TextView) v.findViewById(R.id.disclaimer);
        disclaimer1 = (TextView) v.findViewById(R.id.disclaimer1);
        totprice = (TextView) v.findViewById(R.id.totprice);
        recyclerView = (RecyclerView) v.findViewById(R.id.checkoutcart);

        coupon = (EditText) v.findViewById(R.id.coupon);
        coupon.setFocusable(false);
        coupon.setClickable(true);

        Log.i("debugging", "Address :" + intromanager.getAddressLandMark());
        Log.i("debugging", "Lat Lon :" + intromanager.getLatLon());
        Log.i("debugging", "City :" + intromanager.getCityOnce());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new fr_book_checkout.GridSpacingItemDecoration(1, dpTopx(10), true));
        recyclerView.addItemDecoration(new fr_book_checkout.GridSpacingItemDecoration(1, dpTopx(0), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new cartAdapterProd(getContext(), ar, ccy);
//        recyclerView.setAdapter(adapter);
        myDB.deleteCartService("M");
        if (intromanager.getItemCode().equals("") && myDB.getItemCount() == 0) {
            fillcart.setVisibility(View.GONE);
            emptycart.setVisibility(View.VISIBLE);
        } else if (intromanager.getItemCode().equals("")) {
            refreshCart();
        } else {

            fillcart.setVisibility(View.VISIBLE);
            emptycart.setVisibility(View.GONE);
            booknow.setVisibility(View.VISIBLE);
            //    if (intromanager.getCityOnce().length() == 0 || intromanager.getGeoLocation().length() == 0){
            //        myDB.showToast(getActivity(),"Unable to get the city code. Please re-setup the app and set your location again to complete the order. For assistance, please call us on " + HMConstants.appcustomertelephone );
            //    }
            if (getArguments() != null) {
                coupon.setText(getArguments().getString("itemcode"));
            }
            JSONObject jsonParam = new JSONObject();
            JSONObject jsonMain = new JSONObject();
            try {
                jsonMain.put("merchantcode", HMConstants.appmerchantid);
                jsonMain.put("mdevice", intromanager.getDevice());
                jsonMain.put("customer", appVariables.ucustomerid);
                jsonMain.put("certificate", appVariables.ucertificate);
                jsonMain.put("catalogcode", intromanager.getQAOption1() + intromanager.getQAOption2() + intromanager.getQAOption3() + intromanager.getQAOption4() + intromanager.getQAOption5());
                jsonMain.put("itemcode", intromanager.getItemCode());
                jsonMain.put("city", intromanager.getCityOnce());
                jsonMain.put("geolocation", intromanager.getGeoLocation().equals("") ? "0.0,0.0" : intromanager.getGeoLocation());
                jsonMain.put("sameaddress", (intromanager.getAddressType().equals("R") ? "1" : ""));
                jsonMain.put("qty", intromanager.getQty() == 0 ? 1 : intromanager.getQty());
                jsonParam.put("indata", jsonMain);
            } catch (JSONException e) {
                e.printStackTrace();
                myDB.showToast(getContext(), e.getMessage());
                return null;
            }
            Log.i("debugging", "Service Submit Detail Input :" + jsonParam.toString());
            String[] myTaskParams = {"/SSMSubmitServiceDetail", jsonParam.toString()};
            try {
                HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar4a, "SSMSubmitServiceDetail");
                aasyncTask.delegate = (AsyncResponse) fr_book_checkout.this;
                aasyncTask.execute(myTaskParams);
            } catch (Exception e) {
                e.printStackTrace();
                myDB.showToast(getActivity(), e.getMessage());
                return null;
            }
        }

        //Get Item List
        //  fetchData(v,"");


        btnshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_homepage();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });


        btnbook1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coupon.setText("");
                intromanager.setCouponRef("");
            }
        });

    /*    mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment= new fr_book_time();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });*/
        coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couponDialog bs = new couponDialog();
                bs.show(getActivity().getSupportFragmentManager(), "My Voucher Wallet");
                bs.setTargetFragment(fr_book_checkout.this, 1);
            }
        });

        booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((intromanager.getCouponRef().equals("")) && (coupon.getText().length() > 0)) {
                    myDB.showToast(getActivity(), getString(R.string.invalid_offer_selection));
                    return;
                }

                sendOrder();
                /*if (intromanager.getPricingType().equals("6")) {
                    sendOrder();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putDouble("totalamount", dtotalamount);
                    bundle.putDouble("walletbalance", walletbalance);
                    Fragment myFragment = new fr_pay_checkout();
                    myFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                }*/
            }
        });
        accscroll.setVisibility(GONE);
        return v;
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        Log.i("debugging", "Submit Service  Detail Input :" + output);
        output = new Util().processJsonForLanguage(output, getContext());
        Map<String, String> statuscode;
        String statuscodekey;
        String statusdesckey;
        statuscode = myDB.statusValues(output);
        statuscodekey = (String) statuscode.get("statuscode");
        statusdesckey = (String) statuscode.get("statusdesc");
        JSONObject object1 = new JSONObject(output);

        if (statuscodekey.toString().equals("100")) {
            Fragment myFragment = new fr_noservice();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        } else if (!statuscodekey.toString().equals("000")) {
            myDB.showToast(getActivity(), statusdesckey);
            return;
        } else if (handle == "SSMSubmitServiceDetail") {
            JSONArray myarray = object1.getJSONArray("catalog");
            JSONObject myjson = myarray.getJSONObject(0);
            itemname.setText(myjson.getString("itemname"));
            walletbalance = Double.parseDouble(myjson.getString("walletbalance"));

            if ((myjson.getString("categorycode").equals("114")) || myjson.getString("question1").length() == 0) {
                servicedet.setVisibility(View.GONE);
            } else {
                servicedet.setVisibility(View.VISIBLE);
            }

            if (myjson.has("directnego"))
                charge_type.setText(myjson.getString("directnego"));
            else
                rl_directnego.setVisibility(GONE);


            Picasso.get()
                    .load(myjson.getString("itemimage"))
                    .placeholder(R.drawable.haal_meer_large)
                    .fit()
                    .centerCrop()
                    .noFade()
                    .into(img);

            if (myjson.getString("question1").length() == 0) {
                detailQ1.setVisibility(View.GONE);
                detailR1.setVisibility(View.GONE);
            }
            if (myjson.getString("question2").length() == 0) {
                detailQ2.setVisibility(View.GONE);
                detailR2.setVisibility(View.GONE);
            }
            if (myjson.getString("question3").length() == 0) {
                detailQ3.setVisibility(View.GONE);
                detailR3.setVisibility(View.GONE);
            }
            if (myjson.getString("question4").length() == 0) {
                detailQ4.setVisibility(View.GONE);
                detailR4.setVisibility(View.GONE);
            }
            if (myjson.getString("question5").length() == 0) {
                detailQ5.setVisibility(View.GONE);
                detailR5.setVisibility(View.GONE);
            }

            if (intromanager.getMultiChoiceQuestion().equals("") && (!intromanager.getMultiFormType().equals("0"))) {
                qa6.setVisibility(View.GONE);
            } else {
                Log.i("debugging", "here is = " + myjson.getString("categorycode") + "  this");
                Log.i("debugging", "here is = " + myjson.getString("question1").length() + "  this");
                Log.i("debugging", "here is = " + intromanager.getMultiChoiceQuestion() + "  this");
                multiQ6.setText(intromanager.getMultiChoiceQuestion());

                for (int i = 0; i < providerinfo.getAllServiceItems().size(); i++) {
                    multiHolder m = providerinfo.getAllServiceItems().get(i);
                    mitem = mitem + m.getName() + "\n";
                }
                multiR6.setText(mitem);

                servicedet.setVisibility(View.VISIBLE);
            }
            mitem = "";
            if ((intromanager.getMultiFormType().equals("1"))) {
                for (int i = 0; i < providerinfo.getAllServiceItems().size(); i++) {
                    multiHolder m = providerinfo.getAllServiceItems().get(i);
                    mitem = mitem + m.getName() + "  " + m.getTotal() + "\n";
                    Log.i("debugging", "Items :" + mitem);
                    mprice += m.getTotal();
                }
                mqtyprice = mprice;
            }

            if ((intromanager.getMultiFormType().equals("2"))) {
                for (int i = 0; i < providerinfo.getAllServiceItems().size(); i++) {
                    multiHolder m = providerinfo.getAllServiceItems().get(i);
                    mitem = mitem + m.getName() + "  " + m.getAmount() + "*" + m.getQty() + "=" + m.getTotal() + "\n";
                    Log.i("debugging", "Items :" + mitem);
                    mqtyprice += m.getTotal();
                }
            }
            detailQ1.setText(myjson.getString("question1"));
            detailR1.setText(myjson.getString("response1"));
            detailQ2.setText(myjson.getString("question2"));
            detailR2.setText(myjson.getString("response2"));
            detailQ3.setText(myjson.getString("question3"));
            detailR3.setText(myjson.getString("response3"));
            detailQ4.setText(myjson.getString("question4"));
            detailR4.setText(myjson.getString("response4"));
            detailQ5.setText(myjson.getString("question5"));
            detailR5.setText(myjson.getString("response5"));

            estimate = myjson.getString("directnego");
            coupon.setText(myjson.getString("offercode"));
            if (myjson.getString("offercode").length() > 0) {
                intromanager.setCouponRef("C");
            }

            Log.i("debugging", "SGST Input :" + myjson.getString("sgstbasic"));
            Log.i("debugging", "CGST Create Order Input :" + myjson.getString("cgstbasic"));

            dpriceamount = Double.parseDouble(myjson.getString("catalogprice"));
            intromanager.setTotalAmount(Float.parseFloat(dpriceamount.toString()));
            /*if (intromanager.getPricingType().equals("1")) {
//                dpriceamount = Double.parseDouble(intromanager.getVisitingCharges()) + (Double.parseDouble(intromanager.getVisitingCharges()) * .05);
                dpriceamount = Double.parseDouble(intromanager.getVisitingCharges());
            } else if (intromanager.getPricingType().equals("2")) {
                dpriceamount = Double.parseDouble(myjson.getString("catalogprice"));
            } else if (intromanager.getPricingType().equals("3")) {
                dpriceamount = mqtyprice + mprice;
            } else if (intromanager.getPricingType().equals("4")) {
                dpriceamount = mqtyprice + mprice + Double.parseDouble(myjson.getString("catalogprice"));
            } else if (intromanager.getPricingType().equals("5")) {
                dpriceamount = mprice;
            } else if (intromanager.getPricingType().equals("6")) {
                dpriceamount = 0.0;
            } else if (intromanager.getPricingType().equals("7")) {
                dpriceamount = mprice + Double.parseDouble(myjson.getString("catalogprice"));
            }*/


            if (myjson.getString("directnego").equals("1")) {
                hmProductList.itemname = intromanager.getItemName() + "\n" + mitem + "\n" + "Actual Quote will be provided after inspection";
                Log.i("debugging", "Items Price :" + intromanager.getVisitingCharges());
                hmProductList.unitprice = intromanager.getVisitingCharges();
            } else {
                hmProductList.itemname = intromanager.getItemName() + "\n" + mitem;
                hmProductList.unitprice = Double.toString(dpriceamount);
                Log.i("debugging", "Items Price :" + hmProductList.unitprice);
            }

            Log.i("debugging", "Items List :" + hmProductList.itemname);


            hmProductList.availableqty = "1";
            hmProductList.itemcode = intromanager.getItemCode();
            hmProductList.itemdescription = "";
            hmProductList.manufacturercode = "";
            hmProductList.manufacturername = "";
            hmProductList.categorycode = "";
            hmProductList.categoryname = "";
            hmProductList.subcategorycode = "";
            hmProductList.subcategoryname = "";
            hmProductList.sizedetail = "";
            hmProductList.colordetail = "";
            hmProductList.measurementtyp = "";
            hmProductList.measurementunit = "";
            hmProductList.rewardpercent = "";
            hmProductList.barcode = "";
            hmProductList.imagesmallurl = "";
            hmProductList.imagelargeurl = "";
            hmProductList.isfav = "";
            hmProductList.recurring = "M";
            hmProductList.isoffer = "";
            hmProductList.offerprice = "";
            hmProductList.offerdescription = "";
            hmProductList.itemtype = HMConstants.itemtypeproduct;
            hmProductList.outletcode = "";

            Log.i("debugging", "Question 1 length:" + myjson.getString("question1").length());
            Log.i("debugging", "Question 2 length:" + myjson.getString("question2").length());
            Log.i("debugging", "Question 3 length:" + myjson.getString("question3").length());
            Log.i("debugging", "Question 4 length:" + myjson.getString("question4").length());
            Log.i("debugging", "Question 5 length:" + myjson.getString("question5").length());
            try {
                myDB.addCart(hmProductList);
                String count;
                count = String.valueOf(myDB.getItemCount());
                Log.i("debugging", "Item in Cart Add :" + count);

                /*if (fr_itemlist.textCartItemCount != null) {
                    fr_itemlist.textCartItemCount.setText(count);
                } else if (fr_homepage.textCartItemCount != null) {
                    fr_homepage.textCartItemCount.setText(count);
                }*/

            } catch (HMOwnException e) {
                e.printStackTrace();
                myDB.alertBox(getActivity(), e.getMessage());
                return;
            }

            StringBuilder str_disclaimer = new StringBuilder(myjson.has("disclaimer") ?
                    HtmlCompat.fromHtml(myjson.getString("disclaimer"), HtmlCompat.FROM_HTML_MODE_LEGACY)
                            + "\n\n" : "");
           /* for (int i = 1; i < +5; i++) {
                str_disclaimer.append(myjson.has("disclaimer" + i) ?
                        HtmlCompat.fromHtml(myjson.getString("disclaimer" + i), HtmlCompat.FROM_HTML_MODE_LEGACY)
                                + "\n\n" : "");
            }*/
            disclaimer.setText(str_disclaimer.toString());


            refreshCart();


        } else if (handle == "SSMCreateServiceOrder") {
            FragmentTransaction ftr = getActivity().getSupportFragmentManager().beginTransaction();
            String message = "", message1 = "";
            Bundle bundle = new Bundle();

            message = getString(R.string.your_booking) + object1.getString("orderid") + getString(R.string.placed_sucessfully) + object1.getString("deliverydate") + getString(R.string.a_vendor_will);
            if ((!object1.getString("couponcode").equals(""))) {
                message1 = getString(R.string.the_coupon_number) + object1.getString("couponcode") + getString(R.string.is_successfullt_redeemed);
            }
            bundle.putString("message", message);
            bundle.putString("message1", message1);
            Fragment myFragment = new fr_checkout_confirm();
            myFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        }

        accscroll.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFragmentInteraction(String info, String info1) {
        Log.i("debugging", "Got final Data" + info + "   " + info1);
        coupon.setText("");
        if (info1.equals("FRESER")) {
            if (intromanager.getQty() > 1) {
                myDB.showToast(getActivity(), "Coupon is valid for one service only");
                return;
            } else {
                coupon.setText(intromanager.getOfferCode());
            }
        } else {
            coupon.setText(intromanager.getOfferCode());
        }
    }

    private void sendOrder() {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        JSONArray servicejson = new JSONArray();
        JSONArray servicejson1 = new JSONArray();
        JSONObject jsonDoc;
        JSONArray documentjson = new JSONArray();

        try {
            //Multi choice
            for (int i = 0; i < providerinfo.getAllServiceItems().size(); i++) {
                jsonDoc = new JSONObject();
                multiHolder m = providerinfo.getAllServiceItems().get(i);
                jsonDoc.put("multiitem", m.getTitle());
                jsonDoc.put("multiname", m.getName());
                jsonDoc.put("multiqty", m.getQty());
                jsonDoc.put("multiprice", m.getAmount());
                jsonDoc.put("multitotal", m.getTotal());
                servicejson.put(jsonDoc);
            }


            //Multi choice again
            for (int i = 0; i < providerinfo.getAllServiceItems().size(); i++) {
                jsonDoc = new JSONObject();
                multiHolder m = providerinfo.getAllServiceItems().get(i);
                jsonDoc.put("itmmltcod", m.getTitle());
                jsonDoc.put("itmmltnam", m.getName());
                jsonDoc.put("itmmltqty", m.getQty());
                jsonDoc.put("itmmltprc", m.getAmount());
                jsonDoc.put("itmmltamt", m.getTotal());
                jsonDoc.put("itmmltunt", "UN");
                servicejson1.put(jsonDoc);
            }


        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
        try {
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("customer", appVariables.ucustomerid);
            jsonMain.put("certificate", appVariables.ucertificate);
            jsonMain.put("channelref", HMConstants.appchannelref);
            jsonMain.put("certificate", appVariables.ucertificate);
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("deliverydate", intromanager.getBookTime());
            jsonMain.put("coupon", intromanager.getOfferCode());
            jsonMain.put("coupontype", intromanager.getCouponRef());
            jsonMain.put("catalogcode", intromanager.getQAOption1() + intromanager.getQAOption2() + intromanager.getQAOption3() + intromanager.getQAOption4() + intromanager.getQAOption5());
            jsonMain.put("itemcode", intromanager.getItemCode());
            jsonMain.put("managedservice", intromanager.getPricingType());
            jsonMain.put("city", intromanager.getCityOnce());
            jsonMain.put("address", intromanager.getLandmark() + " " + intromanager.getAddressLandMark());
            jsonMain.put("geolocation", intromanager.getGeoLocation().equals("") ? "0.0,0.0" : intromanager.getGeoLocation());
            jsonMain.put("additionalinfo", intromanager.getAdditionalInfo());
            jsonMain.put("fbtoken", intromanager.getFCMKey());
            jsonMain.put("sameaddress", (intromanager.getAddressType().equals("R") ? "1" : ""));
            jsonMain.put("qty", intromanager.getQty() == 0 ? 1 : intromanager.getQty());
            jsonMain.put("multichoice", servicejson);
            jsonMain.put("multiorderdata", servicejson1);
            jsonMain.put("itmmltqus", intromanager.getMultiChoiceQuestion());
            jsonMain.put("itmEndDat", intromanager.getBookEndDate());
            jsonMain.put("itmVndLoc", intromanager.getCarryLocation());
            jsonMain.put("itmAdvPmt", "0");
            jsonMain.put("itmadvref", "");
            jsonMain.put("image1", intromanager.getServiceImage1().length() > 0 ? HMConstants.imagetype + intromanager.getServiceImage1() : "");
            jsonMain.put("image2", intromanager.getServiceImage2().length() > 0 ? HMConstants.imagetype + intromanager.getServiceImage2() : "");
            jsonMain.put("image3", intromanager.getServiceImage3().length() > 0 ? HMConstants.imagetype + intromanager.getServiceImage3() : "");
            jsonMain.put("image4", intromanager.getServiceImage4().length() > 0 ? HMConstants.imagetype + intromanager.getServiceImage4() : "");
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return;
        }
        Log.i("debugging", "Service Create Order Input :" + jsonParam.toString());
        String[] myTaskParams = {"/SSMCreateServiceOrder", jsonParam.toString()};
        try {
            HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar4a, "SSMCreateServiceOrder");
            aasyncTask.delegate = (AsyncResponse) fr_book_checkout.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
    }

    @Override
    public void clickEventItem() {
        refreshCart();
        //   fetchData(v,"");
    /*    try {
        taxamount.setText(getString(R.string.Rs) + " " + formatter.format(myDB.getTotalAmount()*.05));
        dtotalamount = myDB.getTotalAmount()+(myDB.getTotalAmount()*.05);
        totalamount.setText(getString(R.string.Rs) + " " +  formatter.format(myDB.getTotalAmount()+(myDB.getTotalAmount()*.05)));
        } catch (HMOwnException e) {
            e.printStackTrace();
        }*/
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


    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        int id = items.getItemId();
//        intromanager.setPageSequence(intromanager.getPageSequence() - 1);
        if (id == android.R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }

    private void refreshCart() {
        ar.clear();
        try {
            JSONObject myjson = myDB.getcartItemJson();

            JSONArray json_array = null;

            json_array = myjson.getJSONArray("cartlist");


            for (int i = 0; i < json_array.length(); i++) {
                JSONObject objects = json_array.getJSONObject(i);

                ar.add(String.valueOf(objects));
                Log.i("debugging", "Items in Cart :" + objects.toString());
            }

            if (ar.size() > 0)
                try {
                    final JSONObject myjson_ = new JSONObject(ar.get(0));
//                    Log.i("debugging", "Items in Cart :" + myjson_.getString("price"));
                    Double f = (Double.parseDouble(myjson_.getString("itemqty"))
                            * Double.parseDouble(myjson_.getString("price"))) + mqtyprice;
                    String formattedString = String.format("%.02f", f);
//                    totprice.setText(getString(R.string.total) + " : " + ccy + " " + formattedString);
                    totprice.setText(ccy + " " + formattedString);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("debugging", "Exception ");
                }

            if (myDB.getItemCount() == 0) {
                booknow.setVisibility(View.GONE);
            } else {
                booknow.setVisibility(View.VISIBLE);
            }
            adapter.notifyDataSetChanged();
            adapter.setClickEvent(this);
            //----Compute Totals-------------
            taxdesc.setText(R.string.vat_5p);
//            taxamount.setText(getString(R.string.Rs) + " " + formatter.format((myDB.getTotalAmount() + mqtyprice) * .05));
//            taxamount.setText(getString(R.string.Rs) + " " + String.format("%.02f", (myDB.getTotalAmount() + mqtyprice) * .05));
            taxamount.setText(String.format("%.02f", (myDB.getTotalAmount() + mqtyprice) * .05));
            dtotalamount = myDB.getTotalAmount() + (myDB.getTotalAmount() * .05);//dpriceamount+Double.parseDouble(myjson.getString("sgst"))+Double.parseDouble(myjson.getString("cgst"));
            totaldesc.setText(R.string.total_amount);
            Log.d("debugging", "total = " + myDB.getTotalAmount() + "\nmqtyprice = " + mqtyprice + "\ntax = " + (myDB.getTotalAmount() * .05));
//            totalamount.setText(getString(R.string.Rs) + " " + formatter.format(myDB.getTotalAmount() + mqtyprice + (myDB.getTotalAmount() * .05)));
            if (LocalManager.getLanguagePref(requireContext()) == LocalManager.ENGLISH)
                totalamount.setText(getString(R.string.Rs) + " " + String.format("%.02f", myDB.getTotalAmount() + mqtyprice + ((myDB.getTotalAmount() + mqtyprice) * .05)));
            else
                totalamount.setText(String.format("%.02f", myDB.getTotalAmount() + mqtyprice + ((myDB.getTotalAmount() + mqtyprice) * .05)) + " " + getString(R.string.Rs));
            /*disclaimer.setText(getString(R.string.a_visiting_charges) + getString(R.string.Rs) + " " + intromanager.getVisitingCharges() + getString(R.string.plus_vat_should_be));
            disclaimer1.setText(R.string.the_voucher_discount_will_be);*/
            //-----end Compute Totals--------------

        } catch (JSONException e) {
            fillcart.setVisibility(View.GONE);
            emptycart.setVisibility(View.VISIBLE);
            booknow.setVisibility(View.GONE);
            e.printStackTrace();
        } catch (HMOwnException e) {
            fillcart.setVisibility(View.GONE);
            emptycart.setVisibility(View.VISIBLE);
            booknow.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }


   /* private void fetchData(View v,String q){
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
        Log.i("debugging Menu",jsonParam.toString());
        String[] myTaskParams = {"/SSMProductList", jsonParam.toString()};
        try {
            HMDataAccess aasyncTask = new HMDataAccess(getContext(), progressBar4a, "productlist");
            aasyncTask.delegate = (AsyncResponse) fr_book_checkout.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.alertBox(getContext(), e.getMessage());
        }


    }*/


}
