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

import com.aic.khidmanow.MainActivity;
import com.aic.khidmanow.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_cancel extends Fragment {
    private TextView lblorder, lblcoupon;
    private Button btnhome;
    private Toolbar mtoolbar;
    private String message, message1;

    public fr_cancel() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fr_cancel, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.booking_cancelled);
        mtoolbar.setNavigationIcon(R.drawable.ico_cross);
        lblorder = (TextView) v.findViewById(R.id.lblorder);
        btnhome = (Button) v.findViewById(R.id.btnhome);

        Log.i("debugging", "Order id :" + getArguments().getString("orderid"));
        Log.i("debugging", "deliverydate :" + getArguments().getString("deliverydate"));

        message = getString(R.string.your_booking) + getArguments().getString("orderid") + getString(R.string.has_been_cancelled);


        lblorder.setText(message);

        //go home
        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_homepage();
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
