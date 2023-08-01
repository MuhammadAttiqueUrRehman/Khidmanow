package layout;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aic.khidmanow.IntroManager;
import com.aic.khidmanow.MainActivity;
import com.aic.khidmanow.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_confirmcouponactivate extends Fragment {
    private TextView lblorder,lblmessage,mTitle;
    private Button btnhome;
    private Toolbar mtoolbar;
    IntroManager intromanager;
    public fr_confirmcouponactivate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        intromanager = new IntroManager(getActivity());
        View v = inflater.inflate(R.layout.fragment_fr_confirmcouponactivate, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.offer_activated);
        lblorder = (TextView) v.findViewById(R.id.lblorder);
        lblmessage = (TextView) v.findViewById(R.id.lblmessage);
        btnhome = (Button) v.findViewById(R.id.btnhome);
        lblorder.setText(getArguments().getString("coupon"));
        lblmessage.setText(getArguments().getString("message"));

        //go home
        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intromanager.setFuncCallId("");
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });



        return v;
    }

}
