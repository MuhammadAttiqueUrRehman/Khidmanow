package layout;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class fr_register extends Fragment implements AsyncResponse {
    private EditText txtfirstname, txtlastname, txtemail, txtnewpin;
    private Button btnnext;
    //   private ImageButton btnloginclose;
    private ProgressBar progressBar11;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    private TextView mTitle;
    private Switch tcswitch;
    private IntroManager intromanager;
    EditText txtmobile;

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
        //   getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
        View v = inflater.inflate(R.layout.fragment_fr_register, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.register);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);
        //   btnloginclose=(ImageButton) v.findViewById(R.id.btnloginclose);
        btnnext = (Button) v.findViewById(R.id.btncreateaccount);
        txtfirstname = (EditText) v.findViewById(R.id.txtfirstname);
        txtlastname = (EditText) v.findViewById(R.id.txtlastname);
        txtmobile = v.findViewById(R.id.txtmobile);
        txtemail = (EditText) v.findViewById(R.id.txtemail);
        txtnewpin = (EditText) v.findViewById(R.id.txtnewpin);
        tcswitch = (Switch) v.findViewById(R.id.switchtc);
        progressBar11 = (ProgressBar) v.findViewById(R.id.progressBar11);


        tcswitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_tandc();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();

            }
        });

        txtmobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
            /*    if (hasFocus) {
                    txtmobile.setPrefix(HMConstants.mobilePrefix);
                } else {
                    if (txtmobile.length() < HMConstants.mobilePrefix.length() + 1)
                        txtmobile.setPrefix("");
                }*/
            }
        });


        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtfirstname.getText().toString().trim().length() == 0) {
                    txtfirstname.setError(getString(R.string.enter_first_name));
                    return;
                }

                if (txtlastname.getText().toString().trim().length() == 0) {
                    txtlastname.setError(getString(R.string.enter_last_name));
                    return;
                }

                if (txtmobile.getText().toString().trim().length() < 12) {
                    txtmobile.setError(getString(R.string.enter_a_valid_mobile_number));
                    return;
                }

                if (txtemail.getText().toString().trim().length() == 0) {
                    txtemail.setError(getString(R.string.enter_a_valid_email_address));
                    return;
                }

                if (txtnewpin.length() != 4) {
                    txtnewpin.setError(getString(R.string.enter_4_digit_pin));
                    return;
                }

                if (!myDB.isValidEmailId(txtemail.getText().toString().trim())) {
                    txtemail.setError(getString(R.string.invalid_email_address));
                    return;
                }

                if (!tcswitch.isChecked()) {
                    myDB.showToast(getActivity(), getString(R.string.accept_terms_amp_conditions));
                    return;
                }

                /*
                  merchantcode = indata.merchantcode
            mobile = indata.mobile
            email = indata.email
            device = indata.mdevice
                */

                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", HMConstants.appmerchantid);
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("mobile", txtmobile.getText());
                    jsonMain.put("email", txtemail.getText());
                    jsonMain.put("email", txtemail.getText());
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.showToast(getActivity(), e.getMessage());
                    return;
                }
                String[] myTaskParams = {"/SSMSendEnrolmentToken", jsonParam.toString()};
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    Log.i("Debugging", jsonParam.toString());
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar11, "forgotpin");
                    aasyncTask.delegate = (AsyncResponse) fr_register.this;
                    aasyncTask.execute(myTaskParams);
                } catch (Exception e) {
                    e.printStackTrace();
                    myDB.showToast(getActivity(), e.getMessage());
                    return;
                }

            }
        });


        return v;
    }


    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        Log.i("Debugging", output);
        Log.i("Debugging", handle);
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");

        Log.i("debugging", "register_output :" + output);
        output = new Util().processJsonForLanguage(output, getContext());

        Map<String, String> statuscode;
        String statuscodekey;
        String statusdesckey;
        statuscode = myDB.statusValues(output);
        statuscodekey = (String) statuscode.get("statuscode");
        statusdesckey = (String) statuscode.get("statusdesc");
        JSONObject myjson = new JSONObject(output);
        if (!statuscodekey.toString().equals("000")) {
            myDB.showToast(getActivity(), statusdesckey);
            return;
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("bfirstname", String.valueOf(txtfirstname.getText()));
            bundle.putString("blastname", String.valueOf(txtlastname.getText()));
            bundle.putString("bmobile", String.valueOf(txtmobile.getText()));
            bundle.putString("bemail", String.valueOf(txtemail.getText()));
            bundle.putString("bpin", String.valueOf(txtnewpin.getText()));
            bundle.putString("bfbid", "");
            bundle.putString("tokenref", myjson.getString("referencenumber"));
            bundle.putString("isnew", "Y");
            Fragment myFragment = new fr_validation();
            myFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
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


}
