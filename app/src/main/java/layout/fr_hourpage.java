package layout;


import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aic.khidmanow.Album;
import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.IntroManager;
import com.aic.khidmanow.R;
import com.aic.khidmanow.buttonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_hourpage extends Fragment {
    //  private Toolbar mtoolbar;
    //  private TextView mTitle,mSubTitle;
    RecyclerView recyclerView;
    //   private questionAdapter serviceadapter;
    View v;
    //  ArrayList<detailitemVO> detailList = new ArrayList<detailitemVO>();
    IntroManager intromanager;
    HMCoreData hmcoredata;
    buttonAdapter adapter;
    ArrayList<Album> albumList;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("Debugging Menu", "Loaded Menu");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public fr_hourpage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        intromanager = new IntroManager(getActivity());
        hmcoredata = new HMCoreData(getActivity());
        v = inflater.inflate(R.layout.fragment_fr_hourpage, container, false);
        TextView quest = (TextView) v.findViewById(R.id.quest1);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);

        // next=(Button) v.findViewById(R.id.btnnext);

        //Quick Icons
        albumList = new ArrayList<Album>() {
        };
        adapter = new buttonAdapter(getContext(), albumList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new fr_hourpage.GridSpacingItemDecoration(2, dpTopx(0), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        try {
            JSONObject myjson = new JSONObject(intromanager.getServiceOP());
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(myjson.getString("itemname"));
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(myjson.getString("unitprice"));
            JSONArray json_array = myjson.getJSONArray("question1");
            Log.i("Debugging", "Hour Array" + json_array.toString());
            for (int i = 0; i < json_array.length(); i++) {
                JSONObject objects = json_array.getJSONObject(i);

                if (objects.getString("narrationtype").equals("Q")) {
                    quest.setText(objects.getString("narration"));
                } else {
                    Log.i("Debugging", "Hour item  " + objects.getString("narration"));
                    Album a = new Album(objects.getString("narrationcode"), 0, objects.getString("narration"), "");
                    albumList.add(a);
                }

            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }


      /*  mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("itemcode", intromanager.getItemCode());
                Fragment myFragment= new fr_servicedetail();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean bool = hmcoredata.checkForm(getActivity(),intromanager.getQAOption1());
                if (!bool){
                    return;
                }
                Fragment myFragment;
                JSONObject myjson = null;
                JSONArray newArray = null;
                try {
                    myjson = new JSONObject(intromanager.getServiceOP());
                    newArray = myjson.getJSONArray("question2");
                } catch (JSONException e) {
                    e.printStackTrace();
                    hmcoredata.showToast(getActivity(), e.getMessage());
                    return;
                }
                if (newArray.length() == 0) {
                    myFragment = new fr_book_addinfo();
                } else {
                    myFragment = new fr_qapage2();
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });*/


        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        int id = items.getItemId();
        intromanager.setPageSequence(intromanager.getPageSequence() - 1);
        if (id == android.R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(items);
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

}
