package layout;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.aic.khidmanow.AsyncResponse;
import com.aic.khidmanow.HMConstants;
import com.aic.khidmanow.HMCoreData;
import com.aic.khidmanow.HMDataAccess;
import com.aic.khidmanow.HMOwnException;
import com.aic.khidmanow.IntroManager;
import com.aic.khidmanow.MainActivity;
import com.aic.khidmanow.R;
import com.aic.khidmanow.placesActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_address_alert extends Fragment{
    private ProgressBar progressBar20;
    private Button button;

    public fr_address_alert() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fr_address_alert, container, false);
        button = (Button) v.findViewById(R.id.btnsetalert);
        progressBar20 = v.findViewById(R.id.progressBar20);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), placesActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }


}
