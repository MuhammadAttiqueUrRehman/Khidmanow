package layout;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_refer extends Fragment implements AsyncResponse {
    private ImageButton btnemail;
    private Toolbar mtoolbar;
    private TextView mTitle, aboutinfo;
    private ProgressBar ProgressBar13;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private IntroManager intromanager;
    Intent shareIntent;

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
        myDB = new HMCoreData(getContext());
        intromanager = new IntroManager(getActivity());
        View v = inflater.inflate(R.layout.fragment_fr_refer, container, false);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.refer_a_friend);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);
        aboutinfo = (TextView) v.findViewById(R.id.taboutinfo);
        btnemail = (ImageButton) v.findViewById(R.id.btnemail);
        ProgressBar13 = (ProgressBar) v.findViewById(R.id.progressBar13);
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("reference", HMConstants.shareapp);
            //jsonMain.put("reference", HMConstants.contactUS);
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
        }
        Log.i("Debugging", "Booking Timeline Input :" + jsonParam.toString());
        String[] myTaskParams = {"/SSMGeneralContentDisplay", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), ProgressBar13, "SSMGeneralContentDisplay");
            aasyncTask.delegate = (AsyncResponse) fr_refer.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
        }

        btnemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressBar13.setVisibility(View.VISIBLE);
                String mmobile = intromanager.getMobile();
                Log.i("Debugging", "Logged Mobile " + mmobile);
                String mlink = intromanager.getSharingLink() + "referdevice=" + intromanager.getNewDevice() + "&mobile=" + mmobile + "&linktype=DOWNLOAD&productcode=";
                Log.i("Debugging", "Came inside on create options menu 111111111111111111111111 " + mlink);

                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLink(Uri.parse(mlink))
                        .setDynamicLinkDomain(HMConstants.pagelink)
                        .setIosParameters(
                                new DynamicLink.IosParameters.Builder("com.aicios.khidmanow")
                                        .setAppStoreId(intromanager.getIOSStoreURL())
                                        .setMinimumVersion("1")
                                        .build())
                        .setAndroidParameters(
                                new DynamicLink.AndroidParameters.Builder("com.aic.khidmanow")
                                        .setMinimumVersion(1)
                                        .build())
                        .setGoogleAnalyticsParameters(
                                new DynamicLink.GoogleAnalyticsParameters.Builder()
                                        .setSource("khidmanowinstall")
                                        .setMedium("androidapp")
                                        .setCampaign("individual-refer")
                                        .build())
                        .setSocialMetaTagParameters(
                                new DynamicLink.SocialMetaTagParameters.Builder()
                                        .setImageUrl(Uri.parse("http://www.khidmanow.com/un_logo.png"))
                                        .setTitle("khidmanow Marketplace App")
                                        .setDescription("Click link to install the app")
                                        .build())
                        .buildShortDynamicLink()
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<ShortDynamicLink>() {
                            @Override
                            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                if (task.isSuccessful()) {
                                    // Short link created
                                    Uri shortLink = task.getResult().getShortLink();
                                    Log.i("debugging", "Success 333333333333333333333333333 " + shortLink);
                                    Uri flowchartLink = task.getResult().getPreviewLink();
                                    try {
                                        shareIntent = new Intent(Intent.ACTION_SEND);
                                        shareIntent.setType("text/plain");
                                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "KhidmaNow App");
                                        String shareMessage = intromanager.getSharingText() + "  " + shortLink.toString();
                                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                                        startActivity(Intent.createChooser(shareIntent, "Share Via"));
                                        ProgressBar13.setVisibility(View.GONE);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Log.i("debugging", "Error 222222222222222222222222222222 " + task.getException().toString());
                                    Log.i("debugging", "Error 222222222222222222222222222222 " + task.getException().getMessage());
                                    Log.i("debugging", "Error 222222222222222222222222222222 " + task.getException().getStackTrace());
                                }
                            }
                        });
            }
        });

        return v;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        MainActivity.bottomNavigationView.setSelectedItemId(R.id.nav_home);
        return true;
    }


    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");

        Log.i("debugging", "FAQ Input :" + output);
        output = new Util().processJsonForLanguage(output, getContext());

        JSONObject myjson = new JSONObject(output);
        String mt = myjson.getString("longtext").replace("rn", "");
        mt = mt.replace("'", "\"");
        aboutinfo.setText(Html.fromHtml(mt), TextView.BufferType.SPANNABLE);
    }
}
