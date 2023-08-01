package layout;


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
import com.aic.khidmanow.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_feedback_confirm extends Fragment {
    private Button btnhome;
    private Toolbar mtoolbar;
    private String message;
    private TextView lblorder;
    IntroManager intromanager;
    public fr_feedback_confirm() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        intromanager=new IntroManager(getActivity());
        View v = inflater.inflate(R.layout.fragment_fr_feedback_confirm, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getArguments().getString("confirmtitle"));
        mtoolbar.setNavigationIcon(R.drawable.ico_cross);
        btnhome = (Button) v.findViewById(R.id.btnhome);
        lblorder = (TextView) v.findViewById(R.id.lblorder);
        message=getArguments().getString("confirmmessage");
        //"We value your feedback and will definitely look into it.";
        lblorder.setText(message);

        //go home
        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intromanager.setFuncCallId("");
                Fragment myFragment = new fr_homepage();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intromanager.setFuncCallId("");
                Fragment myFragment = new fr_homepage();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        return v;
    }

}
