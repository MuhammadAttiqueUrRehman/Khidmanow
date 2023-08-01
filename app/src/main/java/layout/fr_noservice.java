package layout;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aic.khidmanow.MainActivity;
import com.aic.khidmanow.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_noservice extends Fragment {
Button btnretry;

    public fr_noservice() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_fr_noservice, container, false);
        btnretry=(Button) v.findViewById(R.id.btnretry);


        btnretry.setOnClickListener(new View.OnClickListener() {
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
