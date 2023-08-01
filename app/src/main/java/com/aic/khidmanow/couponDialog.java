package com.aic.khidmanow;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import layout.Tab1;
import layout.Tab2;


public class couponDialog extends DialogFragment implements Tab1.OnFragmentInteractionListener, Tab2.OnFragmentInteractionListener {
    //    BottomSheetDialogFragment
    // BottomSheetListener mListener;
    // BottomSheetListener mListener;
    ImageButton btnclose;
    private BottomSheetBehavior mBehavior;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
//        View rootView = View.inflate(getContext(), R.layout.coupon_dialog, null);

        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.coupon_dialog, container, false);

        TabLayout mtablayout = (TabLayout) v.findViewById(R.id.tablayout);
        btnclose = (ImageButton) v.findViewById(R.id.closedialog);
        mtablayout.addTab(mtablayout.newTab().setText(R.string.offer_lookup), 0, true);
        mtablayout.addTab(mtablayout.newTab().setText(R.string.voucher_lookup), 1, false);
        mtablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //   onAttachToParentFragment(getParentFragment());
        final ViewPager viewpager = (ViewPager) v.findViewById(R.id.tabviewpager);
        tabPagerAdapter pageradapter = new tabPagerAdapter(getChildFragmentManager(), mtablayout.getTabCount());
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

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        /*Window window = getDialog().getWindow();

        WindowManager.LayoutParams lp = wind.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        getDialog().getWindow().setAttributes(lp);

        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);*/
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onBottomSheet() {
        dismiss();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String info, String info1);
    }

    OnFragmentInteractionListener oClistener;

    @Override
    public void onFragmentInteraction(String value, String value1) {
        oClistener.onFragmentInteraction(value, value1);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            Log.i("Debugging", "Came to Attach Fragment function");
            oClistener = (OnFragmentInteractionListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BottomSheetListener");
        }
    }

/*    // In the child fragment.
    public void onAttachToParentFragment(Fragment fragment)
    {
        try
        {
            oClistener = (OnFragmentInteractionListener) fragment;

        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnFragmentInteractionListener");
        }
    }*/
}

