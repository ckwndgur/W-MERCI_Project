package nsllab.merci_android_updated;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Moallim on 12/11/2017.
 */

public class NetworkSettingsActivity extends AppCompatActivity implements ConnectivityStatusControllerInterface {
    protected Context context;
    protected FragmentManager fragmentManager;
    protected NetworkSettingsPreferencesFragment networkSettingsPreferencesFragment;
    protected NetworkConnectivityStatusFragment networkConnectivityStatusFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_settings_activity);

        context = this;
        fragmentManager = getSupportFragmentManager();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        networkConnectivityStatusFragment = new NetworkConnectivityStatusFragment();
        fragmentManager.beginTransaction().replace(R.id.network_connectivity_status_fragment, networkConnectivityStatusFragment, "network_connectivity_status_fragment").commit();
        networkSettingsPreferencesFragment = new NetworkSettingsPreferencesFragment();
        fragmentManager.beginTransaction().replace(R.id.network_settings_preferences_fragment, networkSettingsPreferencesFragment, "network_settings_perferences_fragment").commit();
    }

    @Override
    public void updateConnectivityStatus(String status) {
        networkConnectivityStatusFragment.updateNetworkConnectivityStatus(status);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
