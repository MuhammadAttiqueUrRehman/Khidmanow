package layout;


import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.aic.khidmanow.AsyncResponse;
import com.aic.khidmanow.HMAppVariables;
import com.aic.khidmanow.HMConstants;
import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.HMDataAccess;
import com.aic.khidmanow.HMOwnException;
import com.aic.khidmanow.IntroManager;
import com.aic.khidmanow.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class fr_register_fb extends Fragment implements AsyncResponse {
    private Button btnnext;
    private ProgressBar progressBar11;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    private Switch tcswitch;
    private IntroManager intromanager;
    private String strfirstname,strlastname,strfbid,stremail;
    EditText txtmobile;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public fr_register_fb() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //   getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        intromanager=new IntroManager(getActivity());
        myDB = new HMCoreData(getContext());
        try {
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(),e.getMessage());
            return null;
        }
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fr_register_fb, container,false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Register Via Facebook");

       // btnloginclose=(ImageButton) v.findViewById(R.id.btnloginclose);
        btnnext=(Button) v.findViewById(R.id.btncreateaccount);
        txtmobile= v.findViewById(R.id.txtmobile);
        tcswitch=(Switch) v.findViewById(R.id.switchtc);
        progressBar11= (ProgressBar) v.findViewById(R.id.progressBar11);
        strfirstname = getArguments().getString("sfirstname");
        strlastname = getArguments().getString("slastname");
        stremail = getArguments().getString("semail");
        strfbid=getArguments().getString("sfbid");
       // txtmobile.requestFocus();

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
              /*  if (hasFocus) {
                    txtmobile.setPrefix(HMConstants.mobilePrefix);
                } else {
                    if (txtmobile.length()<HMConstants.mobilePrefix.length()+1)
                        txtmobile.setPrefix("");
                }*/
            }
        });

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strfirstname.trim().length()==0){
                    myDB.showToast(getActivity(),"Enter first name");
                    return;
                }

                if (strlastname.trim().length()==0){
                    myDB.showToast(getActivity(),"Enter last name");
                    return;
                }

                if (txtmobile.getText().toString().trim().length()<12){
                    txtmobile.setError("Enter a valid mobile number (9715xxxxxxxxx");
                    return;
                }

                if (stremail.trim().length()==0){
                    myDB.showToast(getActivity(),"Enter valid email address");
                    return;
                }

                if (!tcswitch.isChecked()){
                    myDB.showToast(getActivity(),"Accept Terms & Conditions");
                    return;
                }
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                     jsonMain.put("merchantcode", HMConstants.appmerchantid);
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("mobile", txtmobile.getText());
                    jsonMain.put("email", stremail);
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.showToast(getActivity(), e.getMessage());
                    return;
                }
                String[] myTaskParams = {"/SSMSendEnrolmentToken", jsonParam.toString()};
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    Log.i("Debugging",jsonParam.toString());
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar11, "forgotpin");
                    aasyncTask.delegate = (AsyncResponse) fr_register_fb.this;
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
        output=output.substring(1,output.length()-1);
        output=output.replace("\\", "");
        Map<String, String> statuscode;
        String statuscodekey;
        String statusdesckey;
        statuscode = myDB.statusValues(output);
        statuscodekey = statuscode.get("statuscode");
        statusdesckey = statuscode.get("statusdesc");
        JSONObject myjson = new JSONObject(output);
        if (!statuscodekey.equals("000")) {
            myDB.showToast(getActivity(), statusdesckey);
            return;
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("bfirstname", String.valueOf(strfirstname));
            bundle.putString("blastname", String.valueOf(strlastname));
            bundle.putString("bmobile", String.valueOf(txtmobile.getText()));
            bundle.putString("bemail", String.valueOf(stremail));
            bundle.putString("bfbid", strfbid);
            bundle.putString("bpin", "");
            bundle.putString("tokenref", myjson.getString("referencenumber"));
            bundle.putString("isnew", "Y");
            Fragment myFragment = new fr_validation();
            myFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        }
    }
}
