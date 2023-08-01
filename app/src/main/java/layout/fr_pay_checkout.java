package layout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aic.khidmanow.AsyncResponse;
import com.aic.khidmanow.HMAppVariables;
import com.aic.khidmanow.HMConstants;
import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.HMDataAccess;
import com.aic.khidmanow.HMOwnException;
import com.aic.khidmanow.IntroManager;
import com.aic.khidmanow.R;
import com.aic.khidmanow.Util;
import com.aic.khidmanow.multiHolder;
import com.aic.khidmanow.providerInfo;
import com.payment.paymentsdk.PaymentSdkActivity;
import com.payment.paymentsdk.PaymentSdkConfigBuilder;
import com.payment.paymentsdk.integrationmodels.PaymentSdkBillingDetails;
import com.payment.paymentsdk.integrationmodels.PaymentSdkConfigurationDetails;
import com.payment.paymentsdk.integrationmodels.PaymentSdkError;
import com.payment.paymentsdk.integrationmodels.PaymentSdkLanguageCode;
import com.payment.paymentsdk.integrationmodels.PaymentSdkShippingDetails;
import com.payment.paymentsdk.integrationmodels.PaymentSdkTokenFormat;
import com.payment.paymentsdk.integrationmodels.PaymentSdkTokenise;
import com.payment.paymentsdk.integrationmodels.PaymentSdkTransactionDetails;
import com.payment.paymentsdk.integrationmodels.PaymentSdkTransactionType;
import com.payment.paymentsdk.sharedclasses.interfaces.CallbackPaymentInterface;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_pay_checkout extends Fragment implements AsyncResponse, CallbackPaymentInterface {
    ImageView imgwallet, imgcard, imgcash;
    TextView balwallet, totalamount;
    ProgressBar progressBar20;
    RelativeLayout pay1, pay3, pay4;
    IntroManager intromanager;
    HMCoreData myDB;
    HMAppVariables hmAppVariables = new HMAppVariables();
    String transactionref = "", payref = "";
    Toolbar mtoolbar;
    Double totamt = 0.0, advamt = 0.0;
    providerInfo providerinfo;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("Debugging Menu", "Loaded Menu");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public fr_pay_checkout() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        intromanager = new IntroManager(getActivity());
        providerinfo = new providerInfo(getActivity());
        myDB = new HMCoreData(getActivity());

        try {
            hmAppVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return null;
        }

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fr_pay_checkout, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.make_payment);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);
        totalamount = (TextView) v.findViewById(R.id.totalamount);
        balwallet = (TextView) v.findViewById(R.id.balwallet);
        pay1 = (RelativeLayout) v.findViewById(R.id.pay1);
        pay3 = (RelativeLayout) v.findViewById(R.id.pay3);
        pay4 = (RelativeLayout) v.findViewById(R.id.pay4);
        progressBar20 = (ProgressBar) v.findViewById(R.id.progressBar20);


        //final Float totamt=Float.parseFloat(intromanager.getVisitingCharges()) + (Float.parseFloat(intromanager.getVisitingCharges())*((intromanager.getSGST()/100)+(intromanager.getCGST()/100)));
        totamt = getArguments().getDouble("totalamount");
        Double balwalamt = getArguments().getDouble("walletbalance");
        fetchData();

        totalamount.setText(getString(R.string.total) + " :" + getString(R.string.Rs) + " " + String.format("%.02f", totamt));
        // balwallet.setText("Balance :" + getString(R.string.Rs) + " " + String.format("%.02f", balwalamt));

        pay4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(R.string.app_title);
                alert.setMessage(R.string.you_have_selected_to_make_payment).setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                JSONObject jsonParam = new JSONObject();
                                JSONObject jsonMain = new JSONObject();
                                try {
                                    jsonMain.put("merchantcode", HMConstants.appmerchantid);
                                    jsonMain.put("mdevice", intromanager.getDevice());
                                    jsonMain.put("channelref", HMConstants.appchannelref);
                                    jsonMain.put("netamount", totamt);
                                    jsonMain.put("customer", hmAppVariables.ucustomerid);
                                    jsonParam.put("indata", jsonMain);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    myDB.showToast(getActivity(), e.getMessage());
                                    return;
                                }
                                String[] myTaskParams = {"/SSMPayTabsCheckSumOrderBooking", jsonParam.toString()};
                                //  new HMDataAccess().execute(myTaskParams);
                                try {
                                    Log.i("Debugging", jsonParam.toString());
                                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar20, "SSMPayTabsCheckSum");
                                    aasyncTask.delegate = (AsyncResponse) fr_pay_checkout.this;
                                    aasyncTask.execute(myTaskParams);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    myDB.showToast(getActivity(), e.getMessage());
                                    return;
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                alert.show();
            }
        });


        pay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(R.string.app_title);
                alert.setMessage(R.string.you_have_seleted_to_make_payment_using_wallet).setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                JSONObject jsonParam = new JSONObject();
                                JSONObject jsonMain = new JSONObject();
                                try {
                                    jsonMain.put("merchantcode", HMConstants.appmerchantid);
                                    jsonMain.put("mdevice", intromanager.getDevice());
                                    jsonMain.put("certificate", hmAppVariables.ucertificate);
                                    jsonMain.put("customer", hmAppVariables.ucustomerid);
                                    jsonMain.put("channelref", HMConstants.appchannelref);
                                    jsonMain.put("netamount", totamt);
                                    jsonMain.put("orderid", transactionref);
                                    jsonMain.put("paymentref", payref);
                                    jsonMain.put("reference", "WALLET PAYMENT");
                                    jsonMain.put("fbtoken", intromanager.getFCMKey());
                                    jsonMain.put("deliverydate", intromanager.getBookTime());
                                    jsonMain.put("outlet", intromanager.getPricingType());

                                    jsonParam.put("indata", jsonMain);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    myDB.showToast(getActivity(), e.getMessage());
                                    return;
                                }

                                String[] myTaskParams = {"/SSMpayByWalletOrderBooking", jsonParam.toString()};
                                //  new HMDataAccess().execute(myTaskParams);
                                try {
                                    Log.i("Debugging", jsonParam.toString());
                                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar20, "paywallet");
                                    aasyncTask.delegate = (AsyncResponse) fr_pay_checkout.this;
                                    aasyncTask.execute(myTaskParams);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    myDB.showToast(getContext(), e.getMessage());
                                    return;
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                alert.show();

            }
        });

        pay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(R.string.app_title);
                alert.setMessage(R.string.you_have_seleted_payment_cash).setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sendOrder(transactionref);
                            }
                        })
                        .setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                alert.show();

            }
        });

        return v;
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");

        Log.i("debugging", "pay_checkout output :" + output);
        output = new Util().processJsonForLanguage(output, getContext());

        Map<String, String> statuscode;
        String statuscodekey;
        String statusdesckey;
        statuscode = myDB.statusValues(output);
        statuscodekey = (String) statuscode.get("statuscode");
        statusdesckey = (String) statuscode.get("statusdesc");
        JSONObject myjson = new JSONObject(output);
        Log.i("Debugging", "Checksum Output : " + output);
        if (!statuscodekey.toString().equals("000")) {
            myDB.showToast(getActivity(), statusdesckey);
            return;
        } else {
            if (handle == "SSMPayTabsCheckSum") {
//                transactionref = myjson.getString("orderid");
//                Intent in = new Intent(getContext(), PayTabActivity.class);
//                Log.i("MERCHANT_EMAIL", myjson.getString("emailid"));
//                Log.i("SECRET_KEY", myjson.getString("mid"));
//                Log.i("TRANSACTION_TITLE", myjson.getString("txntitle"));
//                Log.i("AMOUNT", myjson.getString("amount"));
//                Log.i("CURRENCY_CODE", myjson.getString("currencycode"));
//                Log.i("CUSTOMER_PHONE_NUMBER", myjson.getString("mobile"));
//                Log.i("CUSTOMER_EMAIL", myjson.getString("customeremail"));
//                Log.i("ORDER_ID", myjson.getString("payorder"));
//                Log.i("PRODUCT_NAME", myjson.getString("product"));
//                Log.i("ADDRESS_BILLING", myjson.getString("address"));
//                Log.i("CITY_BILLING", myjson.getString("city"));
//                Log.i("STATE_BILLING", myjson.getString("state"));
//
//                Log.i("COUNTRY_BILLING", myjson.getString("country"));
//                Log.i("POSTAL_CODE_BILLING", myjson.getString("postalcode"));


                Log.i("COUNTRY_BILLING", myjson.getString("country"));
                Log.i("POSTAL_CODE_BILLING", myjson.getString("postalcode"));


                PaymentSdkTokenise tokeniseType = PaymentSdkTokenise.MERCHANT_MANDATORY;
                PaymentSdkTransactionType transType = PaymentSdkTransactionType.SALE;
                PaymentSdkTokenFormat tokenFormat = new PaymentSdkTokenFormat.Hex32Format();
                PaymentSdkBillingDetails billingData = new PaymentSdkBillingDetails(
                        myjson.getString("city"),
                        myjson.getString("country"),
                        myjson.getString("emailid"),
                        myjson.getString("customername"),
                        myjson.getString("mobile"),
                        myjson.getString("state"),
                        myjson.getString("address"),
                        myjson.getString("postalcode")
                );

                PaymentSdkShippingDetails shippingData = new PaymentSdkShippingDetails(
                        myjson.getString("city"),
                        myjson.getString("country"),
                        myjson.getString("emailid"),
                        myjson.getString("customername"),
                        myjson.getString("mobile"),
                        myjson.getString("state"),
                        myjson.getString("address"),
                        myjson.getString("postalcode")
                );

                PaymentSdkConfigurationDetails configData = new PaymentSdkConfigBuilder("116133", myjson.getString("mid"), myjson.getString("clientkey"), myjson.getDouble("amount"), myjson.getString("currencycode"))
                        .setCartDescription(myjson.getString("product"))
                        .setLanguageCode(PaymentSdkLanguageCode.EN)
                        .setBillingData(billingData)
                        .setMerchantCountryCode(myjson.getString("country")) // ISO alpha 2
                        .setShippingData(shippingData)
                        .setCartId(myjson.getString("orderid"))
                        .setTransactionType(transType)
                        .showBillingInfo(false)
                        .showShippingInfo(true)
                        .forceShippingInfo(true)
                        .setScreenTitle(myjson.getString("txntitle"))
                        .setTokenise(tokeniseType,tokenFormat)
                        .build();
                PaymentSdkActivity.startCardPayment(getActivity(), configData, this);


//                in.putExtra(PaymentParams.MERCHANT_EMAIL, myjson.getString("emailid")); //this a demo account for testing the sdk
//                in.putExtra(PaymentParams.SECRET_KEY, myjson.getString("mid"));//Add your Secret Key Here
//                in.putExtra(PaymentParams.LANGUAGE, PaymentParams.ENGLISH);
//                in.putExtra(PaymentParams.TRANSACTION_TITLE, myjson.getString("txntitle"));
//                in.putExtra(PaymentParams.AMOUNT, myjson.getDouble("amount"));
//
//                in.putExtra(PaymentParams.CURRENCY_CODE, myjson.getString("currencycode"));
//                in.putExtra(PaymentParams.CUSTOMER_PHONE_NUMBER, myjson.getString("mobile"));
//                in.putExtra(PaymentParams.CUSTOMER_EMAIL, myjson.getString("customeremail"));
//                in.putExtra(PaymentParams.ORDER_ID, myjson.getString("payorder"));
//                in.putExtra(PaymentParams.PRODUCT_NAME, myjson.getString("product"));
//
////Billing Address
//                in.putExtra(PaymentParams.ADDRESS_BILLING, myjson.getString("address"));
//                in.putExtra(PaymentParams.CITY_BILLING, myjson.getString("city"));
//                in.putExtra(PaymentParams.STATE_BILLING, myjson.getString("state"));
//                in.putExtra(PaymentParams.COUNTRY_BILLING, myjson.getString("country"));
//                in.putExtra(PaymentParams.POSTAL_CODE_BILLING, myjson.getString("postalcode")); //Put Country Phone code if Postal code not available '00973'
//
////Shipping Address
//                in.putExtra(PaymentParams.ADDRESS_SHIPPING, myjson.getString("address"));
//                in.putExtra(PaymentParams.CITY_SHIPPING, myjson.getString("city"));
//                in.putExtra(PaymentParams.STATE_SHIPPING, myjson.getString("state"));
//                in.putExtra(PaymentParams.COUNTRY_SHIPPING, myjson.getString("country"));
//                in.putExtra(PaymentParams.POSTAL_CODE_SHIPPING, myjson.getString("postalcode")); //Put Country Phone code if Postal code not available '00973'
//
////Payment Page Style
//                in.putExtra(PaymentParams.PAY_BUTTON_COLOR, "@color/colorPrimary");
////Tokenization
//                in.putExtra(PaymentParams.IS_TOKENIZATION, true);
//                startActivityForResult(in, PaymentParams.PAYMENT_REQUEST_CODE);
            } else if (handle == "paywallet") {
                transactionref = myjson.getString("orderid");
                advamt = totamt;
                payref = myjson.getString("paymentref");
               /* Bundle bundle = new Bundle();
                bundle.putString("paymentref", statuscode.get("paymentref"));
                bundle.putString("walletbalance", String.format("%.02f", Float.parseFloat(myjson.getString("walletbalance"))));
                bundle.putString("orderref", "Payment Ref :" + statuscode.get("paymentref"));
                bundle.putString("message", "Thank you for the order.  Your payment is successfully done from your Wallet. Your current Wallet balance is " + hmAppVariables.ucurrency + " " + String.format("%.02f", Float.parseFloat(myjson.getString("walletbalance"))));
                Fragment myFragment = new fr_checkout_confirm_payment();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
*/
                sendOrder(transactionref);
            } else if (handle == "paycash") {
                advamt = 0.0;
               /* Bundle bundle = new Bundle();
                bundle.putString("paymentref", statuscode.get("paymentref"));
                bundle.putString("orderref", "Order Ref :" + statuscode.get("paymentref"));
                bundle.putString("message", "Thank you for the order.  Please make payment to the service staff.");
                Fragment myFragment = new fr_checkout_confirm_payment();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();*/
                sendOrder(transactionref);
            } else if (handle == "paypaytm") {
                advamt = totamt;
                /*Bundle bundle = new Bundle();
                bundle.putString("paymentref", statuscode.get("paymentref"));
                bundle.putString("orderref", "Order Ref :" + getArguments().getString("orderid"));
                bundle.putString("message", "Thank you for the order.  Your payment is successfully completed. The payment reference is  " + statuscode.get("paymentref"));
                Fragment myFragment = new fr_checkout_confirm_payment();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();*/
                sendOrder(transactionref);
            } else if (handle == "SSMCreateServiceOrder") {
                FragmentTransaction ftr = getActivity().getSupportFragmentManager().beginTransaction();
                String message = "", message1 = "";
                Bundle bundle = new Bundle();

                message = getString(R.string.your_booking) + myjson.getString("orderid") + " " + getString(R.string.placed_sucessfully) + " " + myjson.getString("deliverydate") + getString(R.string.a_vendor_will);
                if ((!myjson.getString("couponcode").equals(""))) {
                    message1 = getString(R.string.the_coupon_number) + " " + myjson.getString("couponcode") + " " + getString(R.string.is_successfullt_redeemed);
                }
                bundle.putString("message", message);
                bundle.putString("message1", message1);
                Fragment myFragment = new fr_checkout_confirm();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            } else if (handle == "SSMWalletStatement") {
                myjson = new JSONObject(output);
                // JSONArray json_array = myjson.getJSONArray("walletstatement");
                Float totamt = Float.parseFloat(myjson.getString("balance"));
                balwallet.setText(getString(R.string.balance) + " :" + getString(R.string.Rs) + " " + String.format("%.02f", totamt));
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        int id = items.getItemId();
        if (id == android.R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            myDB.showToast(getActivity(), "Payment activity cancelled");
            return;
        }
//        if (resultCode == RESULT_OK && requestCode == PaymentParams.PAYMENT_REQUEST_CODE) {
//            Log.e("RESPONSE_CODE", data.getStringExtra(PaymentParams.RESPONSE_CODE));
//            Log.e("TRANSACTION_ID", data.getStringExtra(PaymentParams.TRANSACTION_ID));
//
//            if (data.hasExtra(PaymentParams.TOKEN)
//                    && !data.getStringExtra(PaymentParams.TOKEN).isEmpty()) {
//                Log.i("TOKEN", data.getStringExtra(PaymentParams.TOKEN));
//                Log.i("CUSTOMER_EMAIL", data.getStringExtra(PaymentParams.CUSTOMER_EMAIL));
//                Log.i("CUSTOMER_PASSWORD", data.getStringExtra(PaymentParams.CUSTOMER_PASSWORD));
//                Log.i("RESULT_MESSAGE", data.getStringExtra(PaymentParams.RESULT_MESSAGE));
//            }
//            if (data.getStringExtra(PaymentParams.RESPONSE_CODE).equals("100")) {
//                JSONObject jsonParam = new JSONObject();
//                JSONObject jsonMain = new JSONObject();
//                try {
//
//                   /* merchantcode = indata.merchantcode
//                    customerid = indata.customer
//                    password = indata.certificate
//                    orderid = indata.orderid
//                    device = indata.mdevice
//                    channelref = indata.channelref
//                    outlet = indata.outlet
//                    orderamount = indata.netamount
//                    paymentref = indata.paymentref
//                    orderstatus = indata.status
//                    remark = indata.reference
//                    fcmkey = indata.fbtoken
//                    employeeid = indata.employeeid
//                    hondate = indata.deliverydate*/
//
//
//                    jsonMain.put("merchantcode", HMConstants.appmerchantid);
//                    jsonMain.put("mdevice", intromanager.getDevice());
//                    jsonMain.put("certificate", hmAppVariables.ucertificate);
//                    jsonMain.put("customer", hmAppVariables.ucustomerid);
//                    jsonMain.put("channelref", HMConstants.appchannelref);
//                    jsonMain.put("netamount", totamt);
//                    jsonMain.put("orderid", transactionref);
//                    jsonMain.put("paymentref", paymentSdkTransactionDetails.getCartID());
//                    jsonMain.put("reference",  paymentSdkTransactionDetails.getTransactionReference());
//                    jsonMain.put("fbtoken", intromanager.getFCMKey());
//                    jsonMain.put("deliverydate", intromanager.getBookTime());
//                    jsonMain.put("outlet", intromanager.getPricingType());
//                    jsonParam.put("indata", jsonMain);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    myDB.showToast(getContext(), e.getMessage());
//                    return;
//                }
//
//                String[] myTaskParams = {"/SSMpayByCardOrderBooking", jsonParam.toString()};
//                //  new HMDataAccess().execute(myTaskParams);
//                try {
//                    Log.i("Debugging", jsonParam.toString());
//                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar20, "paypaytm");
//                    aasyncTask.delegate = (AsyncResponse) fr_pay_checkout.this;
//                    aasyncTask.execute(myTaskParams);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    myDB.showToast(getActivity(), e.getMessage());
//                    return;
//                }
//            } else {
//                myDB.alertBox(requireContext(), getString(R.string.payment_could_not_be_completed) + " " + data.getStringExtra(PaymentParams.RESPONSE_CODE) + "  " + data.getStringExtra(PaymentParams.RESULT_MESSAGE));
//            }
//        } else {
//            myDB.alertBox(requireContext(), getString(R.string.payment_could_not_be_completed) + " " + data.getStringExtra(PaymentParams.RESPONSE_CODE) + "  " + data.getStringExtra(PaymentParams.RESULT_MESSAGE));
//        }

    }

    private void sendOrder(String order) {
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
                jsonDoc.put("itmMltCod", m.getTitle());
                jsonDoc.put("itmMltNam", m.getName());
                jsonDoc.put("itmMltQty", m.getQty());
                jsonDoc.put("itmMltPrc", m.getAmount());
                jsonDoc.put("itmMltamt", m.getTotal());
                jsonDoc.put("itmMltUnt", "UN");
                servicejson1.put(jsonDoc);
            }


        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
        Log.i("Debugging", "Multi Choice " + documentjson);
        try {
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("customer", hmAppVariables.ucustomerid);
            jsonMain.put("certificate", hmAppVariables.ucertificate);
            jsonMain.put("channelref", HMConstants.appchannelref);
            jsonMain.put("certificate", hmAppVariables.ucertificate);
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
            jsonMain.put("orderid", transactionref);
            jsonMain.put("itmmltqus", intromanager.getMultiChoiceQuestion());
            jsonMain.put("multichoice", servicejson);
            jsonMain.put("multiorderdata", servicejson1);
            jsonMain.put("itmadvref", payref);
            jsonMain.put("itmEndDat", intromanager.getBookEndDate());
            jsonMain.put("itmVndLoc", intromanager.getCarryLocation());
            jsonMain.put("itmAdvPmt", Double.toString(advamt));

//            jsonMain.put("image1", "");
//              jsonMain.put("image2", "");
//               jsonMain.put("image3", "");
//               jsonMain.put("image4", "");
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
        Log.i("Debugging", "Service Create Order Input :" + jsonParam.toString());
        String[] myTaskParams = {"/SSMCreateServiceOrder", jsonParam.toString()};
        try {
            HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar20, "SSMCreateServiceOrder");
            aasyncTask.delegate = (AsyncResponse) fr_pay_checkout.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
    }

    private void fetchData() {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("customer", hmAppVariables.ucustomerid);
            jsonMain.put("certificate", hmAppVariables.ucertificate);
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return;
        }
        String[] myTaskParams = {"/SSMWalletStatement", jsonParam.toString()};

        try {
            Log.i("Debugging", "Wallet Statement Input" + jsonParam.toString());
            HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar20, "SSMWalletStatement");
            aasyncTask.delegate = (AsyncResponse) fr_pay_checkout.this;
            aasyncTask.execute(myTaskParams);
            Log.i("Debugging", "Wallet Statement Input" + jsonParam.toString());
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return;
        }
    }

    @Override
    public void onError(@NonNull PaymentSdkError paymentSdkError) {
        myDB.alertBox(getContext(), paymentSdkError.toString());
    }

    @Override
    public void onPaymentCancel() {
        myDB.alertBox(getContext(), "Payment has been cancelled...");
    }

    @Override
    public void onPaymentFinish(@NonNull PaymentSdkTransactionDetails paymentSdkTransactionDetails) {
        if (Boolean.TRUE.equals(paymentSdkTransactionDetails.isAuthorized())){
            JSONObject jsonParam = new JSONObject();
            JSONObject jsonMain = new JSONObject();
            try {
                jsonMain.put("merchantcode", HMConstants.appmerchantid);
                jsonMain.put("mdevice", intromanager.getDevice());
                jsonMain.put("certificate", hmAppVariables.ucertificate);
                jsonMain.put("customer", hmAppVariables.ucustomerid);
                jsonMain.put("channelref", HMConstants.appchannelref);
                jsonMain.put("netamount", paymentSdkTransactionDetails.getCartAmount());
                jsonMain.put("outlet", getArguments().getString("vendorid"));
                jsonMain.put("orderid", transactionref);
                jsonMain.put("paymentref", paymentSdkTransactionDetails.getCartID());
                jsonMain.put("reference",  paymentSdkTransactionDetails.getTransactionReference());
                jsonMain.put("fbtoken", intromanager.getFCMKey());
                jsonMain.put("status", getArguments().getString("actiontype"));
                jsonParam.put("indata", jsonMain);
            } catch (JSONException e) {
                e.printStackTrace();
                myDB.showToast(getContext(), e.getMessage());
                return;
            }
            String[] myTaskParams = {"/SSMpayByCardOrderBooking", jsonParam.toString()};
            //  new HMDataAccess().execute(myTaskParams);
            try {
                Log.i("Debugging", jsonParam.toString());
                HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar20, "paypaytm");
                aasyncTask.delegate = (AsyncResponse) fr_pay_checkout.this;
                aasyncTask.execute(myTaskParams);
            } catch (Exception e) {
                e.printStackTrace();
                myDB.showToast(getActivity(), e.getMessage());
                return;
            }
        }
    }
}
