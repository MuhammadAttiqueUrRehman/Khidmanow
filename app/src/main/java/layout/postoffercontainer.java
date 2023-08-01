package layout;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aic.khidmanow.MainActivity;
import com.aic.khidmanow.R;
import com.aic.khidmanow.postTabPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class postoffercontainer extends Fragment {


    public postoffercontainer() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.postoffercontainer, container, false);
        MainActivity.bottomNavigationView.setVisibility(View.GONE);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.offer_vouchers));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SRC_IN);
//        mtoolbar.setNavigationIcon(R.drawable.ico_back);
        TabLayout mtablayout = (TabLayout) v.findViewById(R.id.tablayout);
        mtablayout.addTab(mtablayout.newTab().setText(getString(R.string.offer_lookup)), 0, true);
        mtablayout.addTab(mtablayout.newTab().setText(getString(R.string.voucher_lookup)), 1, false);
        mtablayout.setTabGravity(mtablayout.GRAVITY_FILL);
        //   onAttachToParentFragment(getParentFragment());
        final ViewPager viewpager = (ViewPager) v.findViewById(R.id.tabviewpager);
        postTabPagerAdapter pageradapter = new postTabPagerAdapter(getChildFragmentManager(), mtablayout.getTabCount());
        viewpager.setAdapter(pageradapter);
        viewpager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mtablayout));

        mtablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_account();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });
        return v;
    }
}
