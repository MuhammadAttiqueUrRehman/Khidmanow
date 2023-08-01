package layout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import layout.utils.LocalManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.aic.khidmanow.MainActivity;
import com.aic.khidmanow.R;


public class fr_fix_language extends Fragment {
    RadioButton r_eng,r_arb;
    Button okbutton;

    public fr_fix_language() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_fr_fix_language, container, false);
        r_eng=v.findViewById(R.id.btn_eng);
        r_arb=v.findViewById(R.id.btn_ar);
        okbutton=v.findViewById(R.id.okbutton);
        String lang = LocalManager.getLanguagePref(getContext());
        if (lang.equals(LocalManager.ENGLISH)) {
            r_arb.setChecked(true);
            r_eng.setChecked(false);
        } else{
            r_arb.setChecked(false);
            r_eng.setChecked(true);
        }

        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (r_eng.isChecked()) {
                    LocalManager.setNewLocale(getContext(), LocalManager.ENGLISH);
                    v.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    MainActivity.bottomNavigationView.getMenu().clear();
                    MainActivity.bottomNavigationView.inflateMenu(R.menu.bottom_navigation);
                    MainActivity.bottomNavigationView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                } else {
                    LocalManager.setNewLocale(getContext(), LocalManager.ARABIC);
                    v.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                    MainActivity.bottomNavigationView.getMenu().clear();
                    MainActivity.bottomNavigationView.inflateMenu(R.menu.bottom_navigation);
                    MainActivity.bottomNavigationView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                }


                Fragment myFragment = new fr_homepage();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        return v;
    }




}
