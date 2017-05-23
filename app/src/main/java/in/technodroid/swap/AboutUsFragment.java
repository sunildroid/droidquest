package in.technodroid.swap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by IBM_ADMIN on 8/18/2015.
 */
public class AboutUsFragment extends Fragment {

    TextView tvLicense;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.about_us,null);
        tvLicense= (TextView) v.findViewById(R.id.tvTC);
        tvLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                LicenseFragment dialogFragment = new LicenseFragment ();
                dialogFragment.show(fm, "Sample Fragment");
            }
        });

        return v;
    }
}
