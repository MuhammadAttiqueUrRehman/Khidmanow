package layout;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.aic.khidmanow.AsyncResponse;
import com.aic.khidmanow.HMAppVariables;
import com.aic.khidmanow.HMConstants;
import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.HMDataAccess;
import com.aic.khidmanow.HMOwnException;
import com.aic.khidmanow.IntroManager;
import com.aic.khidmanow.R;
import com.aic.khidmanow.Util;
import com.aic.khidmanow.voucherLookupAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Tab2 extends Fragment implements AsyncResponse, View.OnClickListener, voucherLookupAdapter.ClickEvent {

    private static LinearLayout fillcart;
    private static LinearLayout emptycart;
    private voucherLookupAdapter adapter;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    IntroManager intromanager;
    ProgressBar progressBar4;
    View v;
    ArrayList<String> ar = new ArrayList<String>();
    RecyclerView recyclerView;


    public Tab2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        intromanager = new IntroManager(getActivity());
        try {
            myDB = new HMCoreData(getActivity());
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            myDB.showToast(getContext(), e.getMessage());
            return null;
        }
        View v = inflater.inflate(R.layout.fragment_tab2, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.servicelist);
        progressBar4 = (ProgressBar) v.findViewById(R.id.progressBar4);
        fillcart = (LinearLayout) v.findViewById(R.id.fillcart);
        emptycart = (LinearLayout) v.findViewById(R.id.emptycart);
        onAttachToParentFragment(getParentFragment());
        fetchServices();
        return v;
    }


    private void fetchServices() {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("certificate", appVariables.ucertificate);
            jsonMain.put("customer", appVariables.ucustomerid);
            jsonMain.put("category", intromanager.getItemCode());
            jsonMain.put("measuretype", "1"); //20% discount
            jsonMain.put("materialamount", intromanager.getTotalAmount());
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return;
        }
        String[] myTaskParams = {"/SSMmywalletvouchers", jsonParam.toString()};
        Log.i("Debugging", "Offer  Input :" + jsonParam.toString());
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar4, "SSMmywalletvouchers");
            aasyncTask.delegate = (AsyncResponse) Tab2.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return;
        }
    }


    @Override
    public void processFinish(String output, String mview) throws HMOwnException, JSONException {

        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        String ccy = appVariables.ucurrency;
        Log.i("Debugging", "Offer Output :" + output);
        output = new Util().processJsonForLanguage(output, getContext());
        adapter = new voucherLookupAdapter(getContext(), ar, Tab2.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setAdapter(adapter);

        /*recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                v.onTouchEvent(event);
                return true;
            }//`enter code here`
        });*/

        JSONObject myjson = new JSONObject(output);
        JSONArray json_array = myjson.getJSONArray("mywalletvouchers");
        ar.clear();
        for (int i = 0; i < json_array.length(); i++) {
            JSONObject objects = json_array.getJSONObject(i);

            ar.add(String.valueOf(objects));

        }

        if (json_array.length() == 0) {
            fillcart.setVisibility(View.INVISIBLE);
            emptycart.setVisibility(View.VISIBLE);
        } else {
            fillcart.setVisibility(View.VISIBLE);
            emptycart.setVisibility(View.INVISIBLE);

        }

        adapter.notifyDataSetChanged();
        adapter.setClickEvent(this);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String info, String info1);

        public void onBottomSheet();
    }

    OnFragmentInteractionListener oFlistener;

    @Override
    public void onClick(View v) {

    }

    @Override
    public void clickEventItem(String value, String value1) {
        Log.i("Debugging", "Got Click Data" + value + "   " + value1);
        if (!value.equals("")) {
            intromanager.setCouponRef("V");
            oFlistener.onFragmentInteraction(value, value1);
            oFlistener.onBottomSheet();
        }
    }

    public void onAttachToParentFragment(Fragment fragment) {
        try {
            oFlistener = (OnFragmentInteractionListener) fragment;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnFragmentInteractionListener");
        }
    }

 /*   public void setOnFragmentInteractionListener(OnFragmentInteractionListener event) {
        this.oFlistener = event;
    }

   @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            Log.i("Debugging","Came to Attach Fragment function");
            oFlistener = (OnFragmentInteractionListener) getTargetFragment();
        }catch(ClassCastException e) {
            throw new ClassCastException(context.toString()  + e.getMessage().toString() + " " + e.getLocalizedMessage().toString() + " " + e.getStackTrace().toString());
        }
    }*/
  /*  @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        try {
            Log.i("Debugging","Came to Attach Fragment function");
            oFlistener = (OnFragmentInteractionListener) getContext();
        }catch(ClassCastException e) {
            throw new ClassCastException(fragment.toString()  + e.getMessage().toString() + " " + e.getLocalizedMessage().toString() + " " + e.getStackTrace().toString());
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        oFlistener = null;
    }


}
