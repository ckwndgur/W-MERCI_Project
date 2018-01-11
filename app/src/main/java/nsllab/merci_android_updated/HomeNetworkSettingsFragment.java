package nsllab.merci_android_updated;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Moallim on 12/13/2017.
 */

public class HomeNetworkSettingsFragment extends Fragment {
    protected HomeActivity activity;
    protected FragmentManager fragmentManager;
    protected NetworkConnectivityStatusFragment networkConnectivityStatusFragment;
    protected NetworkSettingsPreferencesFragment networkSettingsPreferencesFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_network_settings_fragment, container, false);

        activity = (HomeActivity) getActivity();
        fragmentManager = getFragmentManager();

        networkConnectivityStatusFragment = new NetworkConnectivityStatusFragment();
        fragmentManager.beginTransaction().replace(R.id.network_connectivity_status_fragment, networkConnectivityStatusFragment, "network_connectivity_status_fragment").commit();
        networkSettingsPreferencesFragment = new NetworkSettingsPreferencesFragment();
        fragmentManager.beginTransaction().replace(R.id.network_settings_preferences_fragment, networkSettingsPreferencesFragment, "network_settings_preferences_fragment").commit();

        activity.setFragmentTitle(getString(R.string.network_settings_label));

        return view;
    }
}
