package layout;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import android.widget.TextView;

import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.IntroManager;
import com.aic.khidmanow.MainActivity;
import com.aic.khidmanow.R;
import com.aic.khidmanow.detailitemVO;
import com.aic.khidmanow.questionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fr_qapage5.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fr_qapage5#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fr_qapage5 extends Fragment {


    Toolbar mtoolbar;
    TextView mTitle,mSubTitle;
    RecyclerView recyclerView;
    questionAdapter serviceadapter;
    View v;
    Button previous,next;
    ArrayList<detailitemVO> detailList = new ArrayList<detailitemVO>();
    IntroManager intromanager;
    HMCoreData hmcoredata;
    Fragment myFragment;

    public fr_qapage5() {
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
        intromanager=new IntroManager(getActivity());
        hmcoredata = new HMCoreData(getActivity());
        View v =  inflater.inflate(R.layout.fragment_fr_qapage1, container, false);
        v = inflater.inflate(R.layout.fragment_fr_qapage5, container, false);
        TextView quest = (TextView) v.findViewById(R.id.quest1);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        intromanager=new IntroManager(getActivity());
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255,255,255), PorterDuff.Mode.SRC_IN);

     /*   Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        mtoolbar.setNavigationIcon(R.drawable.ico_back);*/

        previous=(Button) v.findViewById(R.id.btnprev);
        next=(Button) v.findViewById(R.id.btnnext);
        serviceadapter = new questionAdapter(getActivity(), (ArrayList<detailitemVO>) detailList,R.layout.radio_option,5,intromanager.getQAOption5());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(serviceadapter);
        detailList.clear();
        JSONObject myjson = null;
        try {
            myjson = new JSONObject(intromanager.getServiceOP());
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(myjson.getString("itemname"));
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(myjson.getString("unitprice"));
            JSONArray myArray = myjson.getJSONArray("question5");
            for (int i = 0; i < myArray.length(); i++) {
                JSONObject objects = myArray.getJSONObject(i);
                if (objects.getString("narrationtype").equals("Q")) {
                    quest.setText(objects.getString("narration"));
                } else {
                    detailitemVO a = new detailitemVO(objects.getString("narration"), objects.getString("narrationcode"), objects.getString("narrationcode"),0);
                    detailList.add(a);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            hmcoredata.showToast(getActivity(), e.getMessage());
            return null;
        }

    /*    mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean bool = hmcoredata.checkForm(getActivity(),intromanager.getQAOption5());
                if (!bool){
                    return;
                }
                Fragment myFragment= new fr_qapage4();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });*/


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean bool = hmcoredata.checkForm(getActivity(),intromanager.getQAOption5());
                if (!bool){
                    return;
                }
                  /*<telerik:RadComboBoxItem runat="server" Text="Hourly" Value="fr_hourpage" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Q&A (Page 1)" Value="fr_qapage1" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Q&A (Page 2)" Value="fr_qapage2" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Q&A (Page 3)" Value="fr_qapage3" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Q&A (Page 4)" Value="fr_qapage4" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Q&A (Page 5)" Value="fr_qapage5" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Add info only" Value="fr_book_addinfotext" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Add Info with images" Value="fr_book_addinfo" />
			                                     <telerik:RadComboBoxItem runat="server" Text="End Date" Value="fr_book_enddate" />
			                                  <telerik:RadComboBoxItem runat="server" Text="Customer or Other Location" Value="fr_book_address" />
			                                    <telerik:RadComboBoxItem runat="server" Text="Vendor Location" Value="fr_book_vendorlocation" />
			                                    <telerik:RadComboBoxItem runat="server" Text="Destination Location" Value="fr_book_toaddress" />
			                                    <telerik:RadComboBoxItem runat="server" Text="Multi choice Page only" Value="fr_book_multionly" />
			                                    <telerik:RadComboBoxItem runat="server" Text="Multi choice page with Price" Value="fr_book_multiPrice" />
			                                    <telerik:RadComboBoxItem runat="server" Text="Multi Choice Page with Qty and Price" Value="fr_book_multiPriceqty" />
			                                    <telerik:RadComboBoxItem runat="server" Text="Quantity Page" Value="fr_book_qty" />
			                                    <telerik:RadComboBoxItem runat="server" Text="Schedule/Appointment Page" Value="fr_book_time" />
			                                                                <telerik:RadComboBoxItem runat="server" Text="Payment Page" Value="fr_makepayment" />*/
                try {
                    JSONObject myjson = new JSONObject(intromanager.getServiceOP());
                    JSONArray json_array_pageflow = myjson.getJSONArray("pageflow");
                    if ((json_array_pageflow.length())==intromanager.getPageSequence()){
                        if (!MainActivity.slogged){
                            Log.i("Debugging", "Came to Login Form " + MainActivity.slogged);
                            Fragment myFragment = new fr_login();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                        } else {
                            Fragment myFragment = new fr_book_checkout();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                        }
                    }
                    for (int i = 0; i < json_array_pageflow.length(); i++) {
                        JSONObject objects = json_array_pageflow.getJSONObject(i);
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
                            }else if (objects.getString("pagecode").equals("fr_book_address")) {
                                myFragment = new fr_book_address();
                            }else if (objects.getString("pagecode").equals("fr_book_toaddress")) {
                                myFragment = new fr_book_toaddress();
                            } else if (objects.getString("pagecode").equals("fr_book_addinfotext")) {
                                myFragment = new fr_bookaddinfotext();
                            } else if (objects.getString("pagecode").equals("fr_book_addinfo")) {
                                myFragment = new fr_book_addinfo();
                            }  else if (objects.getString("pagecode").equals("fr_book_qty")) {
                                myFragment = new fr_book_qty();
                            }  else if (objects.getString("pagecode").equals("fr_book_time")) {
                                myFragment = new fr_book_time();
                            }

                        }
                    }
                    intromanager.setPageSequence(intromanager.getPageSequence()+1);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                }catch (JSONException e) {
                    e.printStackTrace();
                    // myDB.showToast(getActivity(), e.getMessage());
                }
            }
        });

        /*previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean bool = hmcoredata.checkForm(getActivity(),intromanager.getQAOption5());
                if (!bool){
                    return;
                }
                Fragment myFragment= new fr_qapage4();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });*/
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem items){
        int id=items.getItemId();
        intromanager.setPageSequence(intromanager.getPageSequence()-1);
        if (id == android.R.id.home){
            if( getActivity().getSupportFragmentManager().getBackStackEntryCount()>0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }
}
