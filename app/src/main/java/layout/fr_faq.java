package layout;

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
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.aic.khidmanow.AsyncResponse;
import com.aic.khidmanow.HMConstants;
import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.HMDataAccess;
import com.aic.khidmanow.HMOwnException;
import com.aic.khidmanow.IntroManager;
import com.aic.khidmanow.R;
import com.aic.khidmanow.ThreeLevelListAdapter;
import com.aic.khidmanow.Util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.String.valueOf;


public class fr_faq extends Fragment implements AsyncResponse {
    private Toolbar mtoolbar;
    private HMCoreData myDB;
    private ProgressBar progressBar13;
    private ExpandableListView expandableListView;
    private IntroManager intromanager;

    public fr_faq() {
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
        // Inflate the layout for this fragment
        myDB = new HMCoreData(getContext());
        intromanager = new IntroManager(getActivity());
        View v = inflater.inflate(R.layout.fragment_fr_faq, container, false);
        expandableListView = (ExpandableListView) v.findViewById(R.id.expandible_listview);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("KhidmaNow F A Q");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.khidmanow_faq);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);
        progressBar13 = (ProgressBar) v.findViewById(R.id.progressBar13);

        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("category", HMConstants.faqType);
            Log.d("debugging", "faq params = " + jsonMain.toString());
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return null;
        }
        Log.i("Debugging", "Booking Timeline Input :" + jsonParam.toString());
        String[] myTaskParams = {"/SSMfaqGroupList", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar13, "SSMfaqGroupList");
            aasyncTask.delegate = (AsyncResponse) fr_faq.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return null;
        }
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        Log.i("Debugging", "Option Menu");
        int id = items.getItemId();
        if (id == android.R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        JSONObject objects;
        String[] a1 = new String[0];
//        String[] a2 = new String[0];
//        String[] a3 = new String[0];
//        String[] a4 = new String[0];
//        String[] a5 = new String[0];
//        String[] a6 = new String[0];
//        String[] a7 = new String[0];
//        String[] a8 = new String[0];
        //  String[] a8=new String[0];

        String[] q1 = new String[0];
//        String[] q2 = new String[0];
//        String[] q3 = new String[0];
//        String[] q4 = new String[0];
//        String[] q5 = new String[0];
//        String[] q6 = new String[0];
//        String[] q7 = new String[0];
//        String[] q8 = new String[0];
        //  String[] q8=new String[0];

        LinkedHashMap<String, String[]> thirdLevelq1 = new LinkedHashMap<>();
//        LinkedHashMap<String, String[]> thirdLevelq2 = new LinkedHashMap<>();
//        LinkedHashMap<String, String[]> thirdLevelq3 = new LinkedHashMap<>();
//        LinkedHashMap<String, String[]> thirdLevelq4 = new LinkedHashMap<>();
//        LinkedHashMap<String, String[]> thirdLevelq5 = new LinkedHashMap<>();
//        LinkedHashMap<String, String[]> thirdLevelq6 = new LinkedHashMap<>();
//        LinkedHashMap<String, String[]> thirdLevelq7 = new LinkedHashMap<>();
//        LinkedHashMap<String, String[]> thirdLevelq8 = new LinkedHashMap<>();
        //  LinkedHashMap<String, String[]> thirdLevelq8 = new LinkedHashMap<>();
        //   ArrayList<ArrayList<String>> QuestionList = new ArrayList<>();

        List<String[]> secondLevel = new ArrayList<>();
        List<LinkedHashMap<String, String[]>> data = new ArrayList<>();

        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        Log.i("debugging", "FAQ Input :" + output);
        output = new Util().processJsonForLanguage(output, getContext());
        JSONObject myjson = new JSONObject(output);
        Boolean first;
        JSONArray json_array_faq = myjson.getJSONArray("faqlist");
        String[] parent = new String[json_array_faq.length()];

        for (int i = 0; i < json_array_faq.length(); i++) {
            objects = json_array_faq.getJSONObject(i);
            JSONArray json_array_question = objects.getJSONArray("groupQA");
            JSONObject object1;
//            if (objects.getString("groupnamename").equals("1001")) {
//            parent[i] = objects.getString("groupcode");
            parent[i] = objects.getString("groupnamename");
            q1 = new String[json_array_question.length()];
            for (int x = 0; x < json_array_question.length(); x++) {
                a1 = new String[1];
                object1 = json_array_question.getJSONObject(x);
                q1[x] = object1.getString("question");
                a1[0] = object1.getString("answer");
                thirdLevelq1.put(object1.getString("question"), a1);
                Log.i("debugging", "Group name, QA:" + "1001" + "  " + object1.getString("question") + "  " + object1.getString("answer"));
            }
            secondLevel.add(q1);
            data.add(thirdLevelq1);
//            }
        }

        /*for (int i = 0; i < json_array_faq.length(); i++) {
            objects = json_array_faq.getJSONObject(i);
            JSONArray json_array_question = objects.getJSONArray("groupQA");
            JSONObject object1;
            if (objects.getString("groupnamename").equals("1002")) {
                parent[1] = objects.getString("groupcode");
                q2 = new String[json_array_question.length()];
                for (int x = 0; x < json_array_question.length(); x++) {
                    a2 = new String[1];
                    object1 = json_array_question.getJSONObject(x);
                    q2[x] = object1.getString("question");
                    a2[0] = object1.getString("answer");
                    thirdLevelq2.put(object1.getString("question"), a2);
                    Log.i("debugging", "Group name, QA:" + "1002" + "  " + object1.getString("question") + "  " + object1.getString("answer"));
                }
            }
        }

        for (int i = 0; i < json_array_faq.length(); i++) {
            objects = json_array_faq.getJSONObject(i);
            JSONArray json_array_question = objects.getJSONArray("groupQA");
            JSONObject object1;
            if (objects.getString("groupnamename").equals("1003")) {
                parent[2] = objects.getString("groupcode");
                q3 = new String[json_array_question.length()];
                for (int x = 0; x < json_array_question.length(); x++) {
                    a3 = new String[1];
                    object1 = json_array_question.getJSONObject(x);
                    q3[x] = object1.getString("question");
                    a3[0] = object1.getString("answer");
                    thirdLevelq3.put(object1.getString("question"), a3);
                    Log.i("debugging", "Group name, QA:" + "1003" + "  " + object1.getString("question") + "  " + object1.getString("answer"));
                }
            }
        }


        for (int i = 0; i < json_array_faq.length(); i++) {
            objects = json_array_faq.getJSONObject(i);
            JSONArray json_array_question = objects.getJSONArray("groupQA");
            JSONObject object1;
            if (objects.getString("groupnamename").equals("1004")) {
                parent[3] = objects.getString("groupcode");
                q4 = new String[json_array_question.length()];
                for (int x = 0; x < json_array_question.length(); x++) {
                    a4 = new String[1];
                    object1 = json_array_question.getJSONObject(x);
                    q4[x] = object1.getString("question");
                    a4[0] = object1.getString("answer");
                    thirdLevelq4.put(object1.getString("question"), a4);
                    Log.i("debugging", "Group name, QA:" + "1004" + "  " + object1.getString("question") + "  " + object1.getString("answer"));
                }
            }
        }

        for (int i = 0; i < json_array_faq.length(); i++) {
            objects = json_array_faq.getJSONObject(i);
            JSONArray json_array_question = objects.getJSONArray("groupQA");
            JSONObject object1;
            if (objects.getString("groupnamename").equals("1005")) {
                parent[4] = objects.getString("groupcode");
                q5 = new String[json_array_question.length()];
                for (int x = 0; x < json_array_question.length(); x++) {
                    a5 = new String[1];
                    object1 = json_array_question.getJSONObject(x);
                    q5[x] = object1.getString("question");
                    a5[0] = object1.getString("answer");
                    thirdLevelq5.put(object1.getString("question"), a5);
                    Log.i("debugging", "Group name, QA:" + "1005" + "  " + object1.getString("question") + "  " + object1.getString("answer"));
                }

            }
        }

        for (int i = 0; i < json_array_faq.length(); i++) {
            objects = json_array_faq.getJSONObject(i);
            JSONArray json_array_question = objects.getJSONArray("groupQA");
            JSONObject object1;
            if (objects.getString("groupnamename").equals("1006")) {
                parent[5] = objects.getString("groupcode");
                q6 = new String[json_array_question.length()];
                for (int x = 0; x < json_array_question.length(); x++) {
                    a6 = new String[1];
                    object1 = json_array_question.getJSONObject(x);
                    q6[x] = object1.getString("question");
                    a6[0] = object1.getString("answer");
                    thirdLevelq6.put(object1.getString("question"), a6);
                    Log.i("debugging", "Group name, QA:" + "1006" + "  " + object1.getString("question") + "  " + object1.getString("answer"));
                }
            }
        }

        for (int i = 0; i < json_array_faq.length(); i++) {
            objects = json_array_faq.getJSONObject(i);
            JSONArray json_array_question = objects.getJSONArray("groupQA");
            JSONObject object1;
            if (objects.getString("groupnamename").equals("1007")) {
                parent[6] = objects.getString("groupcode");
                q7 = new String[json_array_question.length()];
                for (int x = 0; x < json_array_question.length(); x++) {
                    a7 = new String[1];
                    object1 = json_array_question.getJSONObject(x);
                    q7[x] = object1.getString("question");
                    a7[0] = object1.getString("answer");
                    thirdLevelq7.put(object1.getString("question"), a7);
                    Log.i("debugging", "Group name, QA:" + "1007" + "  " + object1.getString("question") + "  " + object1.getString("answer"));
                }
            }
        }

        for (int i = 0; i < json_array_faq.length(); i++) {
            objects = json_array_faq.getJSONObject(i);
            JSONArray json_array_question = objects.getJSONArray("groupQA");
            JSONObject object1;
            if (objects.getString("groupnamename").equals("1008")) {
                parent[7] = objects.getString("groupcode");
                q8 = new String[json_array_question.length()];
                for (int x = 0; x < json_array_question.length(); x++) {
                    a8 = new String[1];
                    object1 = json_array_question.getJSONObject(x);
                    q8[x] = object1.getString("question");
                    a8[0] = object1.getString("answer");
                    thirdLevelq8.put(object1.getString("question"), a8);
                    Log.i("debugging", "Group name, QA:" + "1007" + "  " + object1.getString("question") + "  " + object1.getString("answer"));
                }
            }
        }*/

 /*       for (int i = 0; i < json_array_faq.length(); i++) {
            objects = json_array_faq.getJSONObject(i);
            JSONArray json_array_question = objects.getJSONArray("groupQA");
            JSONObject object1;
            if(objects.getString("groupnamename").equals("1008")) {
                parent[7]=objects.getString("groupcode");
                q8 = new String[json_array_question.length()];
                for (int x = 0; x < json_array_question.length(); x++) {
                    a8=new String[1];
                    object1 = new JSONObject();
                    object1 = json_array_question.getJSONObject(x);
                    q8[x] = object1.getString("question");
                    a8[0] = object1.getString("answer");
                    thirdLevelq8.put(object1.getString("question"),a8);
                    Log.i("debugging","Group name, QA:" + "1008" + "  " + object1.getString("question") + "  " + object1.getString("answer"));
                }
            }
        }*/


//        secondLevel.add(q1);
//        secondLevel.add(q2);
//        secondLevel.add(q3);
//        secondLevel.add(q4);
//        secondLevel.add(q5);
//        secondLevel.add(q6);
//        secondLevel.add(q7);
//        secondLevel.add(q8);
//        data.add(thirdLevelq1);
//        data.add(thirdLevelq2);
//        data.add(thirdLevelq3);
//        data.add(thirdLevelq4);
//        data.add(thirdLevelq5);
//        data.add(thirdLevelq6);
//        data.add(thirdLevelq7);
//        data.add(thirdLevelq8);


        //passing three level of information to constructor
        ThreeLevelListAdapter threeLevelListAdapterAdapter = new ThreeLevelListAdapter(getActivity(), parent, secondLevel, data);
        expandableListView.setAdapter(threeLevelListAdapterAdapter);
        // expandableListView.expandGroup(0);
        //  expandableListView.expandGroup(1);
        //   expandableListView.expandGroup(2);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    expandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
    }
}
