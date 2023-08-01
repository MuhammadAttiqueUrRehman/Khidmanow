package layout;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.MainActivity;
import com.aic.khidmanow.R;


public class fr_checkout_confirm extends Fragment {
    TextView lblorder, lblcoupon;
    Button btnhome;
    Toolbar mtoolbar;
    String message, message1;
    HMCoreData myDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fr_checkout_confirm, container, false);
        MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.booking_confirmation);
        mtoolbar.setNavigationIcon(R.drawable.ico_cross);
        lblorder = (TextView) v.findViewById(R.id.lblorder);
        lblcoupon = (TextView) v.findViewById(R.id.lblcoupon);
        btnhome = (Button) v.findViewById(R.id.btnhome);
        myDB = new HMCoreData(getActivity());
        myDB.deleteCartAll();
        Log.i("Debugging", "Order id :" + getArguments().getString("orderid"));
        Log.i("Debugging", "deliverydate :" + getArguments().getString("deliverydate"));

         /*   message="Your booking # " + getArguments().getString("orderid") + " is placed successfully on  "+ getArguments().getString("deliverydate") + ". A vendor will contact you to confirm the location before the visit on the booked date.";
            if ((!getArguments().getString("couponcode").equals(""))){
                message1="The coupon number " + getArguments().getString("couponcode") + " is successfully redeemed and will be adjusted on the final payment of the approved order only.";
                lblcoupon.setText(message1);
            } else{
                lblcoupon.setVisibility(View.GONE);
            }*/
        lblcoupon.setText(getArguments().getString("message1"));
        lblorder.setText(getArguments().getString("message"));

        //go home
        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_myorders();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return v;
    }


}
