package layout;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aic.khidmanow.Album;
import com.aic.khidmanow.AlbumsAdapter;
import com.aic.khidmanow.AsyncResponse;
import com.aic.khidmanow.HMAppVariables;
import com.aic.khidmanow.HMConstants;
import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.HMDataAccess;
import com.aic.khidmanow.HMOwnException;
import com.aic.khidmanow.IntroManager;
import com.aic.khidmanow.MainActivity;
import com.aic.khidmanow.R;
import com.aic.khidmanow.corCustomAdapter;
import com.aic.khidmanow.imageSwipe;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import layout.utils.LocalManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_homepage extends Fragment {
    public IntroManager intromanager;
    public static Boolean slogged = false;
    String jcarecall = "";
    public String uuid, xuuid, statuscodekey, statusdesckey;
    //    ImageButton login,btncart;
    ImageButton login;
    RecyclerView recyclerView;
    TextView label1, label2, label3, label4, label5, label6, label7, label8, label9, label10, label11, label12, label2a;
    AlbumsAdapter adapter;
    corCustomAdapter coradapter;
    ArrayList<Album> albumList;
    ViewPager viewPager;
    imageSwipe imageswipe;
    HMCoreData myDB;
    public Map statuscode;
    public View v;
    HMAppVariables hmAppVariables;
    ScrollView mainscroll;
    public ShareActionProvider mShareActionProvider;
    //    public static TextView textCartItemCount;
    private TextView btn_language_toggle;

    View rl_notification;
    ImageView iv_close;
    TextView tv_noti_text;

    public fr_homepage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        super.onOptionsItemSelected(items);
        Fragment myFragment;
        int id = items.getItemId();
        Log.i("debugging", "In Homepage Cart " + Integer.toString(id));
        switch (id) {

            case R.id.action_search:
                myFragment = new fr_product_search();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                break;
            case R.id.action_language:
                myFragment = new fr_fix_language();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                break;
////                btn_language_toggle.performLongClick();
//  /*              String lang = LocalManager.getLanguagePref(getContext());
//                if (lang.equals(LocalManager.ENGLISH)) {
//                    setNewLocale(getActivity(), LocalManager.ARABIC);
////                    LocalManager.setNewLocale(getContext(), LocalManager.ARABIC);
//                } else {
//                    setNewLocale(getActivity(), LocalManager.ENGLISH);
////                    LocalManager.setNewLocale(getContext(), LocalManager.ENGLISH);
//                }*/
////                updateLanguageToServer(lang.equals(LocalManager.ENGLISH) ? "E" : "A");

            case R.id.action_call:
                startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + intromanager.getSupportCall())));
                break;
            case R.id.action_share:
                if (!MainActivity.slogged) {
                    intromanager.setFuncCallId("fr_refer");
                    myFragment = new fr_login();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                    break;
                } else {
                    myFragment = new fr_refer();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                    break;
                }
            default:
                return super.onOptionsItemSelected(items);
        }
        return super.onOptionsItemSelected(items);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.search_menu, menu);
//        final MenuItem menuItemLang = menu.findItem(R.id.action_language);
//        menuItemLang.setTitle(R.string.language_name);
  /*     final MenuItem menuItem = menu.findItem(R.id.action_cart);
      View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        if (myDB.getItemCount()>0) {
           textCartItemCount.setText(String.valueOf(myDB.getItemCount()));
        }*/
        return;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fr_homepage, container, false);
        MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
        hmAppVariables = new HMAppVariables();
        intromanager = new IntroManager(getActivity());
        intromanager.setFirst3(false);
        intromanager.setOfferCode("");
        intromanager.setServiceOP("");
        intromanager.setGeoLocation("");
        intromanager.setBookTime("");
        intromanager.setItemCode("");
        intromanager.setServiceImage1("");
        intromanager.setServiceImage2("");
        intromanager.setServiceImage3("");
        intromanager.setServiceImage4("");
        intromanager.setAdditionalInfo("");
        intromanager.setQAOption1("0000");
        intromanager.setQAOption2("0000");
        intromanager.setQAOption3("0000");
        intromanager.setQAOption4("0000");
        intromanager.setQAOption5("0000");
        intromanager.setSubcategory("");
        intromanager.setQtyDesc("");
        intromanager.setQty(1);
        intromanager.setProviderCount(0);
        intromanager.setServiceRating(0);
        intromanager.setPersonLimit(0);
        intromanager.setMultiChoicePage("");
        intromanager.setPriorAppointment(0);
        intromanager.setAdvBookingDays(0);
        intromanager.setMultiChoiceQuestion("");
        intromanager.setDisableSchedule("");
        intromanager.setLocationType("");
        intromanager.setPricingType("");
        intromanager.setVisitingCharges("");
        intromanager.setShowEndDate("");
        intromanager.setShowPaymentPage("");
        intromanager.setPageSequence(0);
        intromanager.setShowAddress("");
        intromanager.setEndDate("");
        intromanager.setCarryLocation("");
        intromanager.setMultiFormType("");
        intromanager.setItemName("");
        intromanager.setProfile(false);
        intromanager.setAddressType("");
        intromanager.setCityOnce("");
        try {
            myDB = new HMCoreData(getContext());
            hmAppVariables = myDB.getUserData();
            MainActivity.slogged = hmAppVariables.ulogged;
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return null;
        }

        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(HMConstants.alertheader);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(intromanager.getCityName());
        // ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(intromanager.getLocation());
        mtoolbar.setNavigationIcon(R.drawable.ico_login);
        mainscroll = (ScrollView) v.findViewById(R.id.mainscroll);
        if (MainActivity.slogged) {
            mtoolbar.setNavigationIcon(null);

        }


        btn_language_toggle = (TextView) v.findViewById(R.id.btn_language_toggle);

        viewPager = (ViewPager) v.findViewById(R.id.pager);
//        btncart = v.findViewById(R.id.btncart);
        recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        label1 = (TextView) v.findViewById(R.id.label1);
        label2 = (TextView) v.findViewById(R.id.label2);
        label2a = (TextView) v.findViewById(R.id.label2a);
        label3 = (TextView) v.findViewById(R.id.label3);
        label4 = (TextView) v.findViewById(R.id.label4);
        label5 = (TextView) v.findViewById(R.id.label5);
        label6 = (TextView) v.findViewById(R.id.label6);
        label7 = (TextView) v.findViewById(R.id.label7);
        label8 = (TextView) v.findViewById(R.id.label8);
        label9 = (TextView) v.findViewById(R.id.label9);
        label10 = (TextView) v.findViewById(R.id.label10);
        label11 = (TextView) v.findViewById(R.id.label11);
        label12 = (TextView) v.findViewById(R.id.label12);

        rl_notification = v.findViewById(R.id.rl_notification);
        iv_close = (ImageView) v.findViewById(R.id.iv_close);
        tv_noti_text = (TextView) v.findViewById(R.id.tv_noti_text);

        /*textCartItemCount = v.findViewById(R.id.cart_badge);
        if (myDB.getItemCount() > 0) {
            textCartItemCount.setText(Integer.toString(myDB.getItemCount()));
        }*/

        //^PC or MOBILE^OS Version^Browser Version^IP Address^GeoLocation (Null if not enabled)^device height^device width^Screen resolution^IMEI or MAC Address^
        try {
            renderData(v);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return null;
        }

        //subtract item
        /*btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_book_checkout();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });*/

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_login();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        v.findViewById(R.id.v_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0,
                        new fr_product_search()).addToBackStack(null).commitAllowingStateLoss();
            }
        });


        String lang = LocalManager.getLanguagePref(getContext());
//        if (lang.equals(LocalManager.ENGLISH)) {
        btn_language_toggle.setText("Language " + getString(R.string.language_name));
//        } else {
//            btn_language_toggle.setText("Language " + getString(R.string.language_name));
//        }

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
                updateLanguageToServer(lang.equals(LocalManager.ENGLISH) ? "E" : "A");
            }
        });

        v.findViewById(R.id.fab_whatsapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String contact = "+00 9876543210"; // use country code with your phone number
                String url = "https://api.whatsapp.com/send?phone=" + jcarecall;
                try {
                    PackageManager pm = requireActivity().getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(requireContext(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        return v;
    }

    private void showDialog(String image_url, String title, String message) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_message);
        Picasso.get()
                .load(image_url)
                .placeholder(R.drawable.haal_meer_large)
                .fit()
                .noFade()
                .into(((AppCompatImageView) dialog.findViewById(R.id.iv_image)));
        ((TextView) dialog.findViewById(R.id.tv_title)).setText(title);
        ((TextView) dialog.findViewById(R.id.tv_message)).setText(message);
        dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void updateLanguageToServer(String language) {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("customer", hmAppVariables.ucustomerid);
            jsonMain.put("certificate", hmAppVariables.ucertificate);
            jsonMain.put("language", language);
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
        }
        Log.i("debugging", "Language Input :" + jsonParam.toString());
        String[] myTaskParams = {"/SSMChangeLanguage", jsonParam.toString()};
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), new ProgressBar(getContext()), "SSMChangeLanguage");
            aasyncTask.delegate = new AsyncResponse() {
                @Override
                public void processFinish(String output, String handle) throws HMOwnException, JSONException {
                    Log.i("debugging", "Language Output :" + output);
//                    output = output.substring(1, output.length() - 1);
//                    output = output.replace("\\", "");
//                    output = new Util().processJsonForLanguage(output, getContext());

                }
            };
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
        }
    }

    private void setNewLocale(Activity mContext, @LocalManager.LocaleDef String language) {
        LocalManager.setNewLocale(mContext, language);
        Intent intent = mContext.getIntent();
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void renderData(View v) throws JSONException {
        Date date = new Date();
        Log.i("Debugging", "In Homepage " + date.toString());

        JSONObject myjson = (JSONObject) new JSONObject(intromanager.getHomeOP());
        JSONArray json_array = (JSONArray) myjson.getJSONArray("quicksubcategory");
        JSONArray json_array1 = myjson.getJSONArray("categories");
        JSONObject objects;
        JSONObject objects1;
        JSONArray json_array2;

        jcarecall = myjson.getString("jcarecall");
     /*   if (MainActivity.firsttime.length() == 0) {
            intromanager.setVisitingCharges(myjson.getString("visitingcharge"));
            hmAppVariables.udevicecomboined = intromanager.getDevice();
            hmAppVariables.appenrolmetnamount = myjson.getDouble("supEnrolAmount");
            hmAppVariables.appgoogleapikey = myjson.getString("googleapikey");
            hmAppVariables.applocationshort = myjson.getString("appad_location_short");
            hmAppVariables.applocationlong = myjson.getString("sharetext");
            hmAppVariables.appmerchantid = myjson.getString("merchantcode");
            hmAppVariables.appurl = HMConstants.appurl;
            hmAppVariables.ucurrency = myjson.getString("supEnrolCurrency");
            if (!hmAppVariables.ulogged) {
                hmAppVariables.ucity = myjson.getString("citycode");
                hmAppVariables.ucityname = myjson.getString("citycode");
                hmAppVariables.ucountryname = myjson.getString("countrycode");
                hmAppVariables.ucountry = myjson.getString("countrycode");
            }
            try {
                hmAppVariables = myDB.updateUserData(hmAppVariables);
            } catch (Exception) {
                e.printStackTrace();
            }
            intromanager.setSharingText(myjson.getString("sharetext"));
            intromanager.setSharingURL(myjson.getString("sharepagelink"));
            intromanager.setSharingLink(myjson.getString("sharelink"));
            MainActivity.firsttime = "1";
        }*/


        //Main Slider
        final int dotscount;
        final ImageView[] dots;
        LinearLayout dotspanel;

        dotspanel = (LinearLayout) v.findViewById(R.id.dotspanel);

        try {
            imageswipe = new imageSwipe(this.getContext(), intromanager.getHomeOP());
            viewPager.setAdapter(imageswipe);


        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
        dotscount = imageswipe.getCount();
        dots = new ImageView[dotscount];
        for (int i = 0; i < dotscount; i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.ico_inactivedot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            dotspanel.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.ico_activedot));


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.ico_inactivedot));

                }
                dots[position].setImageDrawable(getResources().getDrawable(R.drawable.ico_activedot));

            }
        });


        //Quick Icons
        albumList = new ArrayList<Album>() {
        };
        adapter = new AlbumsAdapter(getContext(), albumList);
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 4);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, dpTopx(0), true));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        for (int i = 0; i < json_array.length(); i++) {
            objects = json_array.getJSONObject(i);
            Album a = new Album(objects.getString("subcategoryname"), json_array.length(), objects.getString("subcategorycode"), objects.getString("imageiconurl"));
            albumList.add(a);
        }
        adapter.notifyDataSetChanged();

        //Other Sub categories - Booking and Reservation
        albumList = new ArrayList<Album>() {
        };
        coradapter = new corCustomAdapter(getActivity(), (ArrayList<Album>) albumList, R.layout.horizontal_scroll_14);
        recyclerView = v.findViewById(R.id.my_recycler1a);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(coradapter);
        for (int i = 0; i < json_array1.length(); i++) {
            objects = json_array1.getJSONObject(i);

            if (objects.getString("categorycode").equals("114")) {
                label2a.setText(objects.getString("categoryname"));
                json_array2 = objects.getJSONArray("subcategory");
                label2a.setVisibility(View.VISIBLE);
                for (int x = 0; x < json_array2.length(); x++) {
                    objects1 = json_array2.getJSONObject(x);
                    Album a = new Album(objects1.getString("subcategoryname"), json_array2.length(), objects1.getString("subcategorycode"), objects1.getString("imagelargeurl"));
                    albumList.add(a);
                }
                Log.i("Debugging", "Album Size " + albumList.size());
                coradapter.notifyDataSetChanged();
              /*  if(albumList.size()==0){
                    recyclerView.setVisibility(GONE);
                    label2.setVisibility(GONE);
                }*/
            }
        }

        //Other Sub categories - Maintenance Services
        albumList = new ArrayList<Album>() {
        };
        coradapter = new corCustomAdapter(getActivity(), (ArrayList<Album>) albumList, R.layout.horizontal_scroll_1);
        recyclerView = v.findViewById(R.id.my_recycler1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(coradapter);
        for (int i = 0; i < json_array1.length(); i++) {
            objects = json_array1.getJSONObject(i);

            if (objects.getString("categorycode").equals("101")) {
                json_array2 = objects.getJSONArray("subcategory");
                label2.setText(objects.getString("categoryname"));
                label2.setVisibility(View.VISIBLE);
                for (int x = 0; x < json_array2.length(); x++) {
                    objects1 = json_array2.getJSONObject(x);
                    Album a = new Album(objects1.getString("subcategoryname"), json_array2.length(), objects1.getString("subcategorycode"), objects1.getString("imagelargeurl"));
                    albumList.add(a);
                }
                Log.i("Debugging", "Album Size " + albumList.size());
                coradapter.notifyDataSetChanged();
              /*  if(albumList.size()==0){
                    recyclerView.setVisibility(GONE);
                    label2.setVisibility(GONE);
                }*/
            }
        }

        //Other Sub categories - Cleaning Services
        albumList = new ArrayList<Album>() {
        };

        coradapter = new corCustomAdapter(getActivity(), (ArrayList<Album>) albumList, R.layout.horizontal_scroll_2);
        recyclerView = v.findViewById(R.id.my_recycler2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(coradapter);
        for (int i = 0; i < json_array1.length(); i++) {
            objects = json_array1.getJSONObject(i);


            if (objects.getString("categorycode").equals("102")) {
                Log.i("Debugging", "JSON Type " + objects.getString("categoryname") + "  " + objects.getString("categorycode"));
                json_array2 = objects.getJSONArray("subcategory");
                label3.setText(objects.getString("categoryname"));
                label3.setVisibility(View.VISIBLE);
                for (int x = 0; x < json_array2.length(); x++) {
                    objects1 = json_array2.getJSONObject(x);
                    Log.i("Debugging", "JSON Type " + objects1.getString("subcategoryname") + " " + json_array2.length() + " " + objects1.getString("subcategorycode") + " " + objects1.getString("imagelargeurl"));
                    Album a = new Album(objects1.getString("subcategoryname"), json_array2.length(), objects1.getString("subcategorycode"), objects1.getString("imagelargeurl"));
                    albumList.add(a);
                }
                coradapter.notifyDataSetChanged();
                Log.i("Debugging", "Album Size 3 " + albumList.size());
            /*    if(albumList.size()==0){
                    recyclerView.setVisibility(GONE);
                    label3.setVisibility(GONE);
                }*/
            }

        }

        //Other Sub categories - Vehicle Services
        albumList = new ArrayList<Album>() {
        };

        coradapter = new corCustomAdapter(getActivity(), (ArrayList<Album>) albumList, R.layout.horizontal_scroll_3);
        recyclerView = v.findViewById(R.id.my_recycler3);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(coradapter);
        for (int i = 0; i < json_array1.length(); i++) {
            objects = json_array1.getJSONObject(i);
            if (objects.getString("categorycode").equals("103")) {
                Log.i("Debugging", "JSON Type " + objects.getString("categoryname") + "  " + objects.getString("categorycode"));
                json_array2 = objects.getJSONArray("subcategory");
                label4.setText(objects.getString("categoryname"));
                label4.setVisibility(View.VISIBLE);
                for (int x = 0; x < json_array2.length(); x++) {
                    objects1 = json_array2.getJSONObject(x);
                    Log.i("Debugging", "JSON Type " + objects1.getString("subcategoryname") + " " + json_array2.length() + " " + objects1.getString("subcategorycode") + " " + objects1.getString("imagelargeurl"));
                    Album a = new Album(objects1.getString("subcategoryname"), json_array2.length(), objects1.getString("subcategorycode"), objects1.getString("imagelargeurl"));
                    albumList.add(a);
                }
                coradapter.notifyDataSetChanged();
                Log.i("Debugging", "Album Size 4 " + albumList.size());
          /*      if(albumList.size()==0){
                    recyclerView.setVisibility(GONE);
                    label4.setVisibility(GONE);
                }*/
            }

        }

        //Other Sub categories - beautycare Services
        albumList = new ArrayList<Album>() {
        };

        coradapter = new corCustomAdapter(getActivity(), (ArrayList<Album>) albumList, R.layout.horizontal_scroll_4);
        recyclerView = v.findViewById(R.id.my_recycler4);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(coradapter);
        for (int i = 0; i < json_array1.length(); i++) {
            objects = json_array1.getJSONObject(i);
            if (objects.getString("categorycode").equals("109")) {
                Log.i("Debugging", "JSON Type " + objects.getString("categoryname") + "  " + objects.getString("categorycode"));
                json_array2 = objects.getJSONArray("subcategory");
                label5.setText(objects.getString("categoryname"));
                label5.setVisibility(View.VISIBLE);
                for (int x = 0; x < json_array2.length(); x++) {
                    objects1 = json_array2.getJSONObject(x);
                    Log.i("Debugging", "JSON Type " + objects1.getString("subcategoryname") + " " + json_array2.length() + " " + objects1.getString("subcategorycode") + " " + objects1.getString("imagelargeurl"));
                    Album a = new Album(objects1.getString("subcategoryname"), json_array2.length(), objects1.getString("subcategorycode"), objects1.getString("imagelargeurl"));
                    albumList.add(a);
                }
                coradapter.notifyDataSetChanged();
                Log.i("Debugging", "Album Size 5 " + albumList.size());
              /*  if(albumList.size()==0){
                    recyclerView.setVisibility(GONE);
                    label5.setVisibility(GONE);
                }*/
            }

        }

        //Other Sub categories - Electronic Services
        albumList = new ArrayList<Album>() {
        };

        coradapter = new corCustomAdapter(getActivity(), (ArrayList<Album>) albumList, R.layout.horizontal_scroll_5);
        recyclerView = v.findViewById(R.id.my_recycler5);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(coradapter);
        for (int i = 0; i < json_array1.length(); i++) {
            objects = json_array1.getJSONObject(i);
            if (objects.getString("categorycode").equals("104")) {
                Log.i("Debugging", "JSON Type " + objects.getString("categoryname") + "  " + objects.getString("categorycode"));
                json_array2 = objects.getJSONArray("subcategory");
                label6.setText(objects.getString("categoryname"));
                label6.setVisibility(View.VISIBLE);
                for (int x = 0; x < json_array2.length(); x++) {
                    objects1 = json_array2.getJSONObject(x);
                    Album a = new Album(objects1.getString("subcategoryname"), json_array2.length(), objects1.getString("subcategorycode"), objects1.getString("imagelargeurl"));
                    albumList.add(a);
                }
                coradapter.notifyDataSetChanged();
                Log.i("Debugging", "Album Size 6 " + albumList.size());
             /*   if(albumList.size()==0){
                    recyclerView.setVisibility(GONE);
                    label6.setVisibility(GONE);
                }*/
            }

        }

        //Other Sub categories - packing & moving Services
        albumList = new ArrayList<Album>() {
        };
        for (int m = 0; m < albumList.size(); m++) {
            Log.i("Debugging", "Album list 6 removing ");
            albumList.remove(m);
        }
        coradapter = new corCustomAdapter(getActivity(), (ArrayList<Album>) albumList, R.layout.horizontal_scroll_6);
        recyclerView = v.findViewById(R.id.my_recycler6);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(coradapter);
        for (int i = 0; i < json_array1.length(); i++) {
            objects = json_array1.getJSONObject(i);
            if (objects.getString("categorycode").equals("108")) {
                Log.i("Debugging", "JSON Type " + objects.getString("categoryname") + "  " + objects.getString("categorycode"));
                json_array2 = objects.getJSONArray("subcategory");
                label7.setText(objects.getString("categoryname"));
                label7.setVisibility(View.VISIBLE);
                for (int x = 0; x < json_array2.length(); x++) {
                    objects1 = json_array2.getJSONObject(x);
                    Album a = new Album(objects1.getString("subcategoryname"), json_array2.length(), objects1.getString("subcategorycode"), objects1.getString("imagelargeurl"));
                    albumList.add(a);
                }
                coradapter.notifyDataSetChanged();
                Log.i("Debugging", "Album Size 7 " + albumList.size());
             /*   if(albumList.size()==0){
                    recyclerView.setVisibility(GONE);
                    label7.setVisibility(GONE);
                }*/
            }

        }

        //Other Sub categories - Home improvements Services
        albumList = new ArrayList<Album>() {
        };
        coradapter = new corCustomAdapter(getActivity(), (ArrayList<Album>) albumList, R.layout.horizontal_scroll_7);
        recyclerView = v.findViewById(R.id.my_recycler7);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(coradapter);
        for (int i = 0; i < json_array1.length(); i++) {
            objects = json_array1.getJSONObject(i);
            if (objects.getString("categorycode").equals("106")) {
                Log.i("Debugging", "JSON Type " + objects.getString("categoryname") + "  " + objects.getString("categorycode"));
                json_array2 = objects.getJSONArray("subcategory");
                label8.setText(objects.getString("categoryname"));
                label8.setVisibility(View.VISIBLE);
                for (int x = 0; x < json_array2.length(); x++) {
                    objects1 = json_array2.getJSONObject(x);
                    Album a = new Album(objects1.getString("subcategoryname"), json_array2.length(), objects1.getString("subcategorycode"), objects1.getString("imagelargeurl"));
                    albumList.add(a);
                }
                coradapter.notifyDataSetChanged();
                Log.i("Debugging", "Album Size 8 " + albumList.size());
               /* if(albumList.size()==0){
                    recyclerView.setVisibility(GONE);
                    label8.setVisibility(GONE);
                }*/
            }

        }

        //Other Sub categories - Events & Holidays Services
        albumList = new ArrayList<Album>() {
        };
        coradapter = new corCustomAdapter(getActivity(), (ArrayList<Album>) albumList, R.layout.horizontal_scroll_8);
        recyclerView = v.findViewById(R.id.my_recycler8);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(coradapter);
        for (int i = 0; i < json_array1.length(); i++) {
            objects = json_array1.getJSONObject(i);
            if (objects.getString("categorycode").equals("113")) {
                json_array2 = objects.getJSONArray("subcategory");
                label9.setText(objects.getString("categoryname"));
                label9.setVisibility(View.VISIBLE);
                for (int x = 0; x < json_array2.length(); x++) {
                    objects1 = json_array2.getJSONObject(x);
                    Album a = new Album(objects1.getString("subcategoryname"), json_array2.length(), objects1.getString("subcategorycode"), objects1.getString("imagelargeurl"));
                    albumList.add(a);
                }
                coradapter.notifyDataSetChanged();
                Log.i("Debugging", "Album Size 9 " + albumList.size());
              /*  if(albumList.size()==0){
                    recyclerView.setVisibility(GONE);
                    label9.setVisibility(GONE);
                }*/
            }

        }

        //Other Sub categories - Tutor Services
        albumList = new ArrayList<Album>() {
        };
        coradapter = new corCustomAdapter(getActivity(), (ArrayList<Album>) albumList, R.layout.horizontal_scroll_9);
        recyclerView = v.findViewById(R.id.my_recycler9);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(coradapter);
        for (int i = 0; i < json_array1.length(); i++) {
            objects = json_array1.getJSONObject(i);
            if (objects.getString("categorycode").equals("115")) {
                Log.i("Debugging", "JSON Type " + objects.getString("categoryname") + "  " + objects.getString("categorycode"));
                json_array2 = objects.getJSONArray("subcategory");
                label10.setText(objects.getString("categoryname"));
                label10.setVisibility(View.VISIBLE);
                for (int x = 0; x < json_array2.length(); x++) {
                    objects1 = json_array2.getJSONObject(x);
                    Album a = new Album(objects1.getString("subcategoryname"), json_array2.length(), objects1.getString("subcategorycode"), objects1.getString("imagelargeurl"));
                    albumList.add(a);
                }
                coradapter.notifyDataSetChanged();
                Log.i("Debugging", "Album Size 10 " + albumList.size());
             /*   if(albumList.size()==0){
                    recyclerView.setVisibility(GONE);
                    label10.setVisibility(GONE);
                }*/
            }

        }

        //Other Sub categories - Business & Document Services
        albumList = new ArrayList<Album>() {
        };
        coradapter = new corCustomAdapter(getActivity(), (ArrayList<Album>) albumList, R.layout.horizontal_scroll_10);
        recyclerView = v.findViewById(R.id.my_recycler10);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(coradapter);
        for (int i = 0; i < json_array1.length(); i++) {
            objects = json_array1.getJSONObject(i);
            if (objects.getString("categorycode").equals("107")) {
                Log.i("Debugging", "JSON Type " + objects.getString("categoryname") + "  " + objects.getString("categorycode"));
                json_array2 = objects.getJSONArray("subcategory");
                label11.setText(objects.getString("categoryname"));
                label11.setVisibility(View.VISIBLE);
                for (int x = 0; x < json_array2.length(); x++) {
                    objects1 = json_array2.getJSONObject(x);
                    Album a = new Album(objects1.getString("subcategoryname"), json_array2.length(), objects1.getString("subcategorycode"), objects1.getString("imagelargeurl"));
                    albumList.add(a);
                }
                coradapter.notifyDataSetChanged();
                Log.i("Debugging", "Album Size 11 " + albumList.size());
               /* if(albumList.size()==0){
                    recyclerView.setVisibility(GONE);
                    label11.setVisibility(GONE);
                }*/
            }

        }

        //Other Sub categories - Maintenance Package Services
        albumList = new ArrayList<Album>() {
        };
        coradapter = new corCustomAdapter(getActivity(), (ArrayList<Album>) albumList, R.layout.horizontal_scroll_11);
        recyclerView = v.findViewById(R.id.my_recycler11);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(coradapter);
        for (int i = 0; i < json_array1.length(); i++) {
            objects = json_array1.getJSONObject(i);
            if (objects.getString("categorycode").equals("117")) {
                Log.i("Debugging", "JSON Type " + objects.getString("categoryname") + "  " + objects.getString("categorycode"));
                json_array2 = objects.getJSONArray("subcategory");
                label12.setText(objects.getString("categoryname"));
                label12.setVisibility(View.VISIBLE);
                for (int x = 0; x < json_array2.length(); x++) {
                    objects1 = json_array2.getJSONObject(x);
                    Album a = new Album(objects1.getString("subcategoryname"), json_array2.length(), objects1.getString("subcategorycode"), objects1.getString("imagelargeurl"));
                    albumList.add(a);
                }
                coradapter.notifyDataSetChanged();
                Log.i("Debugging", "Album Size 12 " + albumList.size());
             /*   if(albumList.size()==0){
                    recyclerView.setVisibility(GONE);
                    label12.setVisibility(GONE);
                }*/
            }

        }
        Log.i("Debugging", "In Homepage End " + date.toString());


        if (!intromanager.getFuncCallId().equals("")) {
            Fragment myFragment = null;
            if (intromanager.getFuncCallId().equals("fr_refer")) {
                myFragment = new fr_refer();
            } else if (intromanager.getFuncCallId().equals("fr_myorders")) {
                myFragment = new fr_myorders();
            } else if (intromanager.getFuncCallId().equals("fr_vendorreview")) {
                myFragment = new fr_vendorreview();
            } else if (intromanager.getFuncCallId().equals("fr_unfeedback")) {
                myFragment = new fr_unfeedback();
            } else if (intromanager.getFuncCallId().equals("fr_change_pin")) {
                myFragment = new fr_change_pin();
            } else if (intromanager.getFuncCallId().equals("fr_wallet")) {
                myFragment = new fr_wallet();
            } else if (intromanager.getFuncCallId().equals("fr_profile")) {
                myFragment = new fr_profile();
            } else if (intromanager.getFuncCallId().equals("fr_settings")) {
                myFragment = new fr_settings();
            } else if (intromanager.getFuncCallId().equals("fr_messages")) {
                myFragment = new fr_messages();
            } else if (intromanager.getFuncCallId().equals("fr_myorders_history")) {
                myFragment = new fr_myorders_history();
            } else if (intromanager.getFuncCallId().equals("fr_refererdashboard")) {
                myFragment = new fr_refererdashboard();
            } else if (intromanager.getFuncCallId().equals("fr_voucherlist")) {
                myFragment = new postoffercontainer();
            }
            intromanager.setFuncCallId("");
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        }

        if (MainActivity.firstrun) {
            if (myjson.getInt("shownotify") == 1) {
                iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rl_notification.setVisibility(View.GONE);
                    }
                });
                tv_noti_text.setText(myjson.getString("notifymessage"));
                rl_notification.setVisibility(View.VISIBLE);
            }

            if (myjson.getInt("showpopup") == 1) {
                showDialog(myjson.getString("popupimage"), myjson.getString("popuptitle"),
                        myjson.getString("popupmessage"));
            }
            MainActivity.firstrun = false;
        }
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
   /* public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            Log.i("Debugging",Environment.getExternalStorageDirectory().toString());
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
