package nsllab.merci_android_updated;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.error.VolleyError;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Moallim on 12/11/2017.
 */

public class NetworkSettingsPreferencesFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, VolleyCallbackInterface, UIRunnableInterface {
    protected Context context;
    protected ConnectivityStatusControllerInterface connectivity;
    protected MERCINetworkAPI api;
    protected MERCIMiddleware middleware;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.network_settings_preferences, rootKey);

        context = getActivity();
        connectivity = (ConnectivityStatusControllerInterface) context;
        api = new MERCINetworkAPI(context);
        middleware = new MERCIMiddleware(context);

        resetPreferences();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        if(pref != null && sharedPreferences != null) {
            pref.setSummary(sharedPreferences.getString(key, ""));
            requestCheckConnectivity();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        requestCheckConnectivity();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onRequestSuccess(String GUID, JSONObject result, Map dict) {
        switch (GUID){
            case MERCINetworkAPI.CHECK_CONNECTIVITY_GUID:
                switch (middleware.checkConnectivityMiddleware(result)){
                    case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                        connectivity.updateConnectivityStatus(NetworkConnectivityStatusFragment.LOADING);
                        MERCIEvents.handler(this, MERCIEvents.ON_NETWORK_PARAMETERS_CORRECT);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                        connectivity.updateConnectivityStatus(NetworkConnectivityStatusFragment.LOADING);
                        MERCIEvents.handler(this, MERCIEvents.ON_NETWORK_PARAMETERS_ERROR);
                        break;
                }
                break;
        }
    }

    @Override
    public void onRequestError(String GUID, VolleyError result, Map dict) {
        connectivity.updateConnectivityStatus(NetworkConnectivityStatusFragment.LOADING);
        MERCIEvents.handler(this, MERCIEvents.ON_NETWORK_PARAMETERS_ERROR);
    }

    @Override
    public void onRunnable(String id, String extras) {
        switch (id){
            case MERCIEvents.ON_NETWORK_PARAMETERS_CORRECT:
                connectivity.updateConnectivityStatus(NetworkConnectivityStatusFragment.ONLINE);
                break;
            case MERCIEvents.ON_NETWORK_PARAMETERS_ERROR:
                connectivity.updateConnectivityStatus(NetworkConnectivityStatusFragment.OFFLINE);
                break;
        }
    }

    public void resetPreferences(){
        PreferenceManager.setDefaultValues(getActivity(), R.xml.network_settings_preferences, false);
        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++){
            Preference p = getPreferenceScreen().getPreference(i);
            if(p instanceof EditTextPreference){
                EditTextPreference editTextPref = (EditTextPreference) p;
                p.setSummary(editTextPref.getText());
            }
        }
    }

    public void requestCheckConnectivity(){
        if(!api.isNetworkConnected()){
            Toast.makeText(context, getString(R.string.network_connection_error), Toast.LENGTH_SHORT).show();
            MERCIEvents.handler(this, MERCIEvents.ON_NETWORK_ERROR);
            return;
        }
        api.getServerConnectivity(this);
    }
}
