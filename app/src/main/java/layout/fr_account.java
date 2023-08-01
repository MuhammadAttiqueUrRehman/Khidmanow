package layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
//import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import layout.utils.LocalManager;


public class fr_account extends Fragment implements AsyncResponse {
    //LoginManager loginmanager;
    private CircleImageView pimage;
    private TextView username;
    private ScrollView scrlview;
    private ImageButton login, btnrefund, btnorderhistory, btnrewards, btnprofile,
            btnmessage, btnredeemcoupon, btnchangepin, btnfaq, btnfeedback, btncontactus,
            btnsettings, btntandc, btndataprivacy, btnversion, logout, btnrefer, btnreferral;
    private Button btn_language_toggle;
    private String c_handle = "";
    private ProgressBar progressBar13;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private IntroManager intromanager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        intromanager = new IntroManager(getActivity());
        myDB = new HMCoreData(getContext());
        try {
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return null;
        }
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_fr_account, container, false);
        btn_language_toggle = (Button) v.findViewById(R.id.btn_language_toggle);
        login = (ImageButton) v.findViewById(R.id.btnlogin);
        scrlview = (ScrollView) v.findViewById(R.id.accscroll);
        username = (TextView) v.findViewById(R.id.txtusername);
        pimage = (CircleImageView) v.findViewById(R.id.profileimage);
        progressBar13 = (ProgressBar) v.findViewById(R.id.progressBar13);
        btnorderhistory = (ImageButton) v.findViewById(R.id.btnorderhistory);
        btnrewards = (ImageButton) v.findViewById(R.id.btnrewards);
        btnrefund = (ImageButton) v.findViewById(R.id.btnrefundterms);
        btnprofile = (ImageButton) v.findViewById(R.id.btnprofile);
        btnmessage = (ImageButton) v.findViewById(R.id.btnmessage);
        logout = (ImageButton) v.findViewById(R.id.logout);
        btnredeemcoupon = (ImageButton) v.findViewById(R.id.btnredeemcoupon);
        btnchangepin = (ImageButton) v.findViewById(R.id.btnchangepin);
        btnfaq = (ImageButton) v.findViewById(R.id.btnfaq);
        btnfeedback = (ImageButton) v.findViewById(R.id.btnfeedback);
        btncontactus = (ImageButton) v.findViewById(R.id.btncontactus);
        btnsettings = (ImageButton) v.findViewById(R.id.btnsettings);
        btntandc = (ImageButton) v.findViewById(R.id.btntandc);
        btndataprivacy = (ImageButton) v.findViewById(R.id.btndataprivacy);
        btnversion = (ImageButton) v.findViewById(R.id.btnversion);
        btnrefer = (ImageButton) v.findViewById(R.id.btnrefer);
        btnreferral = (ImageButton) v.findViewById(R.id.btnreferral);
        MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);


        if (MainActivity.slogged) {
            Log.i("Debugging Menu", appVariables.ucuspic);
            login.setVisibility(View.GONE);
            logout.setVisibility(View.VISIBLE);
            username.setText(getString(R.string.welcome) + appVariables.ufirstname + "!");
            if (!appVariables.ucuspic.contains("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
                byte[] decodedString = Base64.decode(appVariables.ucuspic, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                pimage.setImageBitmap(decodedByte);
            }

            /*Picasso.with(getContext())
                    .load(myjson.getString("itemimage"))
                    .placeholder(R.drawable.haal_meer_large)
                    .fit()
                    .centerCrop()
                    .noFade()
                    .into(pimage);*/
        } else {
            login.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
        }

        btnredeemcoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.slogged) {
                    intromanager.setFuncCallId("fr_voucherlist");
                    Fragment myFragment = new fr_login();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                    return;
                }
                Fragment myFragment = new postoffercontainer();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btnrefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.slogged) {
                    intromanager.setFuncCallId("fr_refererdashboard");
                    Fragment myFragment = new fr_login();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                    return;
                }
                Fragment myFragment = new fr_refererdashboard();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btnorderhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.slogged) {
                    intromanager.setFuncCallId("fr_myorders_history");
                    Fragment myFragment = new fr_login();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                    return;
                }
                Fragment myFragment = new fr_myorders_history();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btnmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.slogged) {
                    intromanager.setFuncCallId("fr_messages");
                    Fragment myFragment = new fr_login();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                    return;
                }
                Fragment myFragment = new fr_messages();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

      /*  btnrecurorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.slogged) {
                    Fragment myFragment = new fr_login();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                    return;
                }
                Fragment myFragment = new fr_recurring_order();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });*/
        btnsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.slogged) {
                    intromanager.setFuncCallId("fr_settings");
                    Fragment myFragment = new fr_login();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                    return;
                }
                Fragment myFragment = new fr_settings();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.slogged) {
                    intromanager.setFuncCallId("fr_profile");
                    Fragment myFragment = new fr_login();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                    return;
                }
                Fragment myFragment = new fr_profile();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

      /*  btnmyitems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.slogged) {
                    Fragment myFragment = new fr_login();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                    return;
                }
                Fragment myFragment = new fr_myitems();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });*/

        btnfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.slogged) {
                    intromanager.setFuncCallId("fr_unfeedback");
                    Fragment myFragment = new fr_login();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                    return;
                }
                Fragment myFragment = new fr_unfeedback();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btnreferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_referral_benefit();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btnfaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_faq();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_login();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btnrefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_refundpolicy();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btnversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_version();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btndataprivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_data_privacy();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c_handle = "logout";
                logmeout();
            }
        });

        btntandc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_tandc();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btncontactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_contactus();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btnchangepin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.slogged) {
                    intromanager.setFuncCallId("fr_change_pin");
                    Fragment myFragment = new fr_login();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                    return;
                }
                Fragment myFragment = new fr_change_pin();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btnrewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.slogged) {
                    intromanager.setFuncCallId("fr_wallet");
                    Fragment myFragment = new fr_login();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                    return;
                }
                Fragment myFragment = new fr_wallet();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        String lang = LocalManager.getLanguagePref(getContext());
        if (lang.equals(LocalManager.ENGLISH)) {
            btn_language_toggle.setText(R.string.language_name);
        } else {
            btn_language_toggle.setText(R.string.language_name);
        }

        btn_language_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lang = LocalManager.getLanguagePref(getContext());
                if (lang.equals(LocalManager.ENGLISH)) {
                    setNewLocale(getActivity(), LocalManager.ARABIC);
//                    LocalManager.setNewLocale(getContext(), LocalManager.ARABIC);
                } else {
                    setNewLocale(getActivity(), LocalManager.ENGLISH);
//                    LocalManager.setNewLocale(getContext(), LocalManager.ENGLISH);
                }
            }
        });


        return v;
    }

    private void setNewLocale(Activity mContext, @LocalManager.LocaleDef String language) {
        LocalManager.setNewLocale(mContext, language);
        Intent intent = mContext.getIntent();
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }




    private void logmeout() {
        Log.i("Debugging", "Fcaebook Id check when logout " + appVariables.ufbid);
        if (!appVariables.ufbid.equals("")) {
           // loginmanager.getInstance().logOut();
        }
        //Call Logout service
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("customer", appVariables.ucustomerid);
            jsonMain.put("certificate", appVariables.ucertificate);
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
        String[] myTaskParams = {"/SSMlogmeout", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            Log.i("Debugging", jsonParam.toString());
            HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar13, "logmeout");
            aasyncTask.delegate = (AsyncResponse) fr_account.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        Map<String, String> statuscode;
        String statuscodekey;
        String statusdesckey;
        Log.i("Debugging Login Result", output);
        //Write local SQL
        JSONObject myjson = new JSONObject(output);
        statuscode = myDB.statusValues(output);
        statuscodekey = (String) statuscode.get("statuscode");
        statusdesckey = (String) statuscode.get("statusdesc");
        if (!statuscodekey.toString().equals("000")) {
            myDB.showToast(getActivity(), statusdesckey);
            return;
        }
        else {
            MainActivity.slogged = false;
            appVariables = myDB.getUserData();
            appVariables.ulogged = false;
            appVariables.ualcohol = "";
            appVariables.uautolocation = "";
            appVariables.ubirthdate = "";
            appVariables.ucertificate = "";
            appVariables.ucuspic = "";
            appVariables.ucusqr = "";
            appVariables.ucustomerid = "";
            appVariables.ucustomername = "";
            appVariables.udisplaybirthdate = "";
            appVariables.uemailid = "";
            appVariables.ufbid = "";
            appVariables.ufirstname = "";
            appVariables.ufullname = "";
            appVariables.uhomecountry = "";
            appVariables.uhomecountryname = "";
            appVariables.uinitdate = "";
            appVariables.ulastname = "";
            appVariables.umagicnumber = "";
            appVariables.umaxspend = 0;
            appVariables.umemberexpiry = "";
            appVariables.unationalitycode = "";
            appVariables.upinnumber = "";
            appVariables.upointvalue = 0;
            appVariables.upushoffer = "";
            appVariables.uremindexpiry = "";
            appVariables.uresidentcity = "";
            appVariables.uresidentcountry = "";
            appVariables.uresidentcountryname = "";
            appVariables.usegmentcode = "";
            appVariables.usegmentimage = "";
            appVariables.usegmentname = "";
            appVariables.ushowprofile = "";
            appVariables.uspend = 0;

            //User Data restored even after logout
           /* appVariables.ucity=myjson.getString("city");
            appVariables.ucountry=myjson.getString("country");
            appVariables.ucurrency=myjson.getString("currency");
            appVariables.ulocationname=myjson.getString("locationname");
            appVariables.uareaname=myjson.getString("areaname");
            appVariables.umobilenumber=myjson.getString("mobilenumber");
            appVariables.applat= Double.parseDouble(myjson.getString("userlat"));
            appVariables.applon= Double.parseDouble(myjson.getString("userlon"));*/


            appVariables = myDB.updateUserData(appVariables);
        }

        //Print all appvariables
        Log.i("Debugging", appVariables.udevicecomboined);
        Log.i("Debugging", appVariables.appcustomeremailbody);
        Log.i("Debugging", appVariables.appcustomeremailid);
        Log.i("Debugging", appVariables.appcustomeremailsubject);
        Log.i("Debugging", appVariables.appcustomertelephone);
        Log.i("Debugging", String.valueOf(appVariables.appenrolmetnamount));
        Log.i("Debugging", appVariables.appgoogleapikey);
        Log.i("Debugging", String.valueOf(appVariables.applat));
        Log.i("Debugging", appVariables.applocationshort);
        Log.i("Debugging", appVariables.applocationlong);
        Log.i("Debugging", String.valueOf(appVariables.applon));
        Log.i("Debugging", appVariables.appmerchantid);
        Log.i("Debugging", appVariables.appsocialmessagelong);
        Log.i("Debugging", appVariables.appsocialmessageshort);
        Log.i("Debugging", appVariables.appurl);
        Log.i("Debugging", appVariables.ucity);
        Log.i("Debugging", appVariables.ucountry);
        Log.i("Debugging", appVariables.ucurrency);
        Log.i("Debugging", appVariables.appdeviceexist);
        Log.i("Debugging", appVariables.ualcohol);
        Log.i("Debugging", appVariables.uareaname);
        Log.i("Debugging", appVariables.uautolocation);
        Log.i("Debugging", appVariables.ubirthdate);
        Log.i("Debugging", appVariables.ucertificate);
        Log.i("Debugging", appVariables.ucityname);
        Log.i("Debugging", appVariables.ucountryname);
        Log.i("Debugging", appVariables.ucuspic);
        Log.i("Debugging", appVariables.ucusqr);
        Log.i("Debugging", appVariables.ucustomerid);
        Log.i("Debugging", appVariables.ucustomername);
        Log.i("Debugging", appVariables.udisplaybirthdate);
        Log.i("Debugging", appVariables.uemailid);
        Log.i("Debugging", appVariables.ufbid);
        Log.i("Debugging", appVariables.ufirstname);
        Log.i("Debugging", appVariables.ufullname);
        Log.i("Debugging", appVariables.uhomecountry);
        Log.i("Debugging", appVariables.uhomecountryname);
        Log.i("Debugging", appVariables.uinitdate);
        Log.i("Debugging", appVariables.ulastname);
        Log.i("Debugging", appVariables.ulocationname);
        Log.i("Debugging", String.valueOf(appVariables.ulogged));
        Log.i("Debugging", appVariables.umagicnumber);
        Log.i("Debugging", String.valueOf(appVariables.umaxspend));
        Log.i("Debugging", appVariables.umemberexpiry);
        Log.i("Debugging", appVariables.umobilenumber);
        Log.i("Debugging", appVariables.unationalitycode);
        Log.i("Debugging", appVariables.upinnumber);
        Log.i("Debugging", String.valueOf(appVariables.upointvalue));
        Log.i("Debugging", appVariables.upushoffer);
        Log.i("Debugging", appVariables.uremindexpiry);
        Log.i("Debugging", appVariables.uresidentcity);
        Log.i("Debugging", appVariables.uresidentcountry);
        Log.i("Debugging", appVariables.uresidentcountryname);
        Log.i("Debugging", appVariables.usegmentcode);
        Log.i("Debugging", appVariables.usegmentimage);
        Log.i("Debugging", appVariables.usegmentname);
        Log.i("Debugging", appVariables.ushowprofile);
        Log.i("Debugging", String.valueOf(appVariables.uspend));
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}


