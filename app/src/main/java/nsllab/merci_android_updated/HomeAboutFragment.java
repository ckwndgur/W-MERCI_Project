package nsllab.merci_android_updated;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Moallim on 12/13/2017.
 */

public class HomeAboutFragment extends Fragment {
    protected HomeActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_about_fragment, container, false);
        activity = (HomeActivity) getActivity();
        activity.setFragmentTitle(getString(R.string.about_merci_label));
        return view;
    }
}
