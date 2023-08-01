package layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.app.Activity.RESULT_OK;

import com.aic.khidmanow.AsyncResponse;
import com.aic.khidmanow.HMAppVariables;
import com.aic.khidmanow.HMConstants;
import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.HMDataAccess;
import com.aic.khidmanow.HMOwnException;
import com.aic.khidmanow.IntroManager;
import com.aic.khidmanow.R;
import com.aic.khidmanow.Util;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Objects;

import layout.utils.LocalManager;

public class fr_checkout extends Fragment implements AsyncResponse, CallbackPaymentInterface {
    private ImageView imgwallet, imgcard, imgcash;
    private TextView balwallet, totalamount;
    private ProgressBar progressBar20;
    private RelativeLayout pay1, pay3, pay4;
    private IntroManager intromanager;
    private HMCoreData myDB;
    private HMAppVariables hmAppVariables = new HMAppVariables();
    private String transactionref;
    private Toolbar mtoolbar;

    View rl_notification;
    ImageView iv_close;
    TextView tv_noti_text;

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
        myDB = new HMCoreData(getActivity());

        try {
            hmAppVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return null;
        }

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fr_checkout, container, false);
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

        rl_notification = v.findViewById(R.id.rl_notification);
        iv_close = (ImageView) v.findViewById(R.id.iv_close);
        tv_noti_text = (TextView) v.findViewById(R.id.tv_noti_text);

        transactionref = getArguments().getString("orderid");

        String invalidcouponmessage = getArguments().getString("invalidcouponmessage");
        if (invalidcouponmessage != null && invalidcouponmessage.length() > 0) {
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rl_notification.setVisibility(View.GONE);
                }
            });
            tv_noti_text.setText(invalidcouponmessage);
            rl_notification.setVisibility(View.VISIBLE);
        }

        //final Float totamt=Float.parseFloat(intromanager.getVisitingCharges()) + (Float.parseFloat(intromanager.getVisitingCharges())*((intromanager.getSGST()/100)+(intromanager.getCGST()/100)));
        final Float totamt = Float.parseFloat(getArguments().getString("totalreceivable"));
        Float balwalamt = Float.parseFloat(getArguments().getString("walletbalance"));

        totalamount.setText(getString(R.string.total) + " :" + getString(R.string.Rs) + " " + String.format("%.02f", totamt));
        balwallet.setText(getString(R.string.balance) + " :" + getString(R.string.Rs) + " " + String.format("%.02f", balwalamt));

        pay4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//                                        alert.setTitle(HMConstants.alertheader);
                alert.setTitle(R.string.app_title);
                alert.setMessage(R.string.you_have_selected_to_make_payment).setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                JSONObject jsonParam = new JSONObject();
                                JSONObject jsonMain = new JSONObject();
                                try {
                                    jsonMain.put("merchantcode", HMConstants.appmerchantid);
                                    jsonMain.put("mdevice", intromanager.getDevice());
                                    jsonMain.put("orderid", getArguments().getString("orderid"));
                                    jsonMain.put("netamount", getArguments().getString("totalreceivable"));
                                    jsonMain.put("customer", hmAppVariables.ucustomerid);
                                    jsonParam.put("indata", jsonMain);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    myDB.showToast(getActivity(), e.getMessage());
                                    return;
                                }
                                String[] myTaskParams = {"/SSMPayTabsCheckSum2", jsonParam.toString()};
                                //  new HMDataAccess().execute(myTaskParams);
                                try {
                                    Log.i("debugging", jsonParam.toString());
                                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar20, "SSMPayTabsCheckSum");
                                    aasyncTask.delegate = (AsyncResponse) fr_checkout.this;
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
                                    jsonMain.put("netamount", getArguments().getString("totalreceivable"));
                                    jsonMain.put("outlet", getArguments().getString("vendorid"));
                                    jsonMain.put("orderid", transactionref);
                                    jsonMain.put("fbtoken", intromanager.getFCMKey());
                                    jsonMain.put("status", getArguments().getString("actiontype"));
                                    jsonParam.put("indata", jsonMain);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    myDB.showToast(getActivity(), e.getMessage());
                                    return;
                                }

                                String[] myTaskParams = {"/SSMpayByWallet", jsonParam.toString()};
                                //  new HMDataAccess().execute(myTaskParams);
                                try {
                                    Log.i("debugging", jsonParam.toString());
                                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar20, "paywallet");
                                    aasyncTask.delegate = (AsyncResponse) fr_checkout.this;
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
                                JSONObject jsonParam = new JSONObject();
                                JSONObject jsonMain = new JSONObject();
                                try {
                                    jsonMain.put("merchantcode", HMConstants.appmerchantid);
                                    jsonMain.put("mdevice", intromanager.getDevice());
                                    jsonMain.put("certificate", hmAppVariables.ucertificate);
                                    jsonMain.put("customer", hmAppVariables.ucustomerid);
                                    jsonMain.put("channelref", HMConstants.appchannelref);
                                    jsonMain.put("netamount", getArguments().getString("totalreceivable"));
                                    jsonMain.put("outlet", getArguments().getString("vendorid"));
                                    jsonMain.put("orderid", transactionref);
                                    jsonMain.put("fbtoken", intromanager.getFCMKey());
                                    jsonMain.put("status", getArguments().getString("actiontype"));
                                    jsonParam.put("indata", jsonMain);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    myDB.showToast(getContext(), e.getMessage());
                                    return;
                                }

                                String[] myTaskParams = {"/SSMpayByCOD", jsonParam.toString()};
                                //  new HMDataAccess().execute(myTaskParams);
                                try {
                                    Log.i("debugging", jsonParam.toString());
                                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar20, "paycash");
                                    aasyncTask.delegate = (AsyncResponse) fr_checkout.this;
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

        return v;

    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");

        Log.i("debugging", "checkout Input :" + output);
        output = new Util().processJsonForLanguage(output, getContext());

        Map<String, String> statuscode;
        String statuscodekey;
        String statusdesckey;
        statuscode = myDB.statusValues(output);
        statuscodekey = (String) statuscode.get("statuscode");
        statusdesckey = (String) statuscode.get("statusdesc");
        JSONObject myjson = new JSONObject(output);
        Log.i("debugging", "Checksum Output : " + output);
        if (!statuscodekey.toString().equals("000")) {
            myDB.showToast(getActivity(), statusdesckey);
            return;
        } else {
            if (handle == "SSMPayTabsCheckSum") {
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
                Bundle bundle = new Bundle();
                bundle.putString("paymentref", statuscode.get("paymentref"));
                bundle.putString("walletbalance", String.format("%.02f", Float.parseFloat(myjson.getString("walletbalance"))));
                bundle.putString("orderref", getString(R.string.payment_ref) + statuscode.get("paymentref"));
                bundle.putString("message", getString(R.string.thank_you_for_the_order) + hmAppVariables.ucurrency + " " + String.format("%.02f", Float.parseFloat(myjson.getString("walletbalance"))));
                Fragment myFragment = new fr_checkout_confirm_payment();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();

            } else if (handle == "paycash") {
                Bundle bundle = new Bundle();
                bundle.putString("paymentref", statuscode.get("paymentref"));
                bundle.putString("orderref", getString(R.string.order_ref) + statuscode.get("paymentref"));
                bundle.putString("message", getString(R.string.thank_you_for_the_order_your_payment));
                Fragment myFragment = new fr_checkout_confirm_payment();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            } else if (handle == "paypaytm") {
                Bundle bundle = new Bundle();
                bundle.putString("paymentref", statuscode.get("paymentref"));
                bundle.putString("orderref", getString(R.string.order_ref) + getArguments().getString("orderid"));
                bundle.putString("message", getString(R.string.thank_you_for_the_order_your_payment_is) + statuscode.get("paymentref"));
                Fragment myFragment = new fr_checkout_confirm_payment();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
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

        String lang = LocalManager.getLanguagePref(getContext());
        if (lang.equals(LocalManager.ENGLISH)) {
            setNewLocale(getActivity(), LocalManager.ENGLISH);
        } else {
            setNewLocale(getActivity(), LocalManager.ARABIC);
        }

        if (data == null) {
            myDB.showToast(getActivity(), getString(R.string.payment_activity_cancelled));
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
//                    jsonMain.put("merchantcode", HMConstants.appmerchantid);
//                    jsonMain.put("mdevice", intromanager.getDevice());
//                    jsonMain.put("certificate", hmAppVariables.ucertificate);
//                    jsonMain.put("customer", hmAppVariables.ucustomerid);
//                    jsonMain.put("channelref", HMConstants.appchannelref);
//                    jsonMain.put("netamount", getArguments().getString("totalreceivable"));
//                    jsonMain.put("outlet", getArguments().getString("vendorid"));
//                    jsonMain.put("orderid", transactionref);
//                    jsonMain.put("paymentref", data.getStringExtra(PaymentParams.TRANSACTION_ID));
//                    jsonMain.put("reference", data.getStringExtra(PaymentParams.TOKEN) + "," + data.getStringExtra(PaymentParams.RESULT_MESSAGE));
//                    jsonMain.put("fbtoken", intromanager.getFCMKey());
//                    jsonMain.put("status", getArguments().getString("actiontype"));
//                    jsonParam.put("indata", jsonMain);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    myDB.showToast(getContext(), e.getMessage());
//                    return;
//                }
//
//                String[] myTaskParams = {"/SSMpayByPayTM", jsonParam.toString()};
//                //  new HMDataAccess().execute(myTaskParams);
//                try {
//                    Log.i("debugging", jsonParam.toString());
//                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar20, "paypaytm");
//                    aasyncTask.delegate = (AsyncResponse) fr_checkout.this;
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
////            myDB.alertBox(requireContext(), "this is the one" + " " + data.getStringExtra(PaymentParams.RESPONSE_CODE) + "  " + data.getStringExtra(PaymentParams.RESULT_MESSAGE));
//        }

    }

    private void setNewLocale(Activity mContext, @LocalManager.LocaleDef String language) {
        LocalManager.setNewLocale(mContext, language);
//        Intent intent = mContext.getIntent();
//        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
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
        Log.i("Debugging", paymentSdkTransactionDetails.toString());

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
            String[] myTaskParams = {"/SSMpayByPayTM", jsonParam.toString()};
            //  new HMDataAccess().execute(myTaskParams);
            try {
                Log.i("Debugging", jsonParam.toString());
                HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar20, "paypaytm");
                aasyncTask.delegate = (AsyncResponse) fr_checkout.this;
                aasyncTask.execute(myTaskParams);
            } catch (Exception e) {
                e.printStackTrace();
                myDB.showToast(getActivity(), e.getMessage());
                return;
            }
        }
        else if (Objects.equals(paymentSdkTransactionDetails.getPaymentResult().getResponseCode(), "401") &&
                Boolean.FALSE.equals(paymentSdkTransactionDetails.isAuthorized())){
            myDB.alertBox(getActivity(), "Payment is declined due to wrong information...!");
        }
        else if (Objects.equals(paymentSdkTransactionDetails.getPaymentResult().getResponseCode(), "305") &&
                Boolean.FALSE.equals(paymentSdkTransactionDetails.isAuthorized())){
            myDB.alertBox(getActivity(), "Payment is declined due to wrong information...!");
        }

        else if (Objects.equals(paymentSdkTransactionDetails.getPaymentResult().getResponseCode(), "310") &&
                Boolean.FALSE.equals(paymentSdkTransactionDetails.isAuthorized())){
            myDB.alertBox(getActivity(), "Payment is cancelled...!");
        }
        else {
            myDB.alertBox(getActivity(), "Payment is not successful. Please try again later!");
        }
    }
}
