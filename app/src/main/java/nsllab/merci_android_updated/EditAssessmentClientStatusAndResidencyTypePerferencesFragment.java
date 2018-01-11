package nsllab.merci_android_updated;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.MultiSelectListPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Moallim on 12/13/2017.
 */

public class EditAssessmentClientStatusAndResidencyTypePerferencesFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    protected Context context;
    protected MERCIMiddleware middleware;
    protected ListPreference clientstatusid;
    protected MultiSelectListPreference residencytype;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.edit_asseessment_client_status_and_residency_type_preferences, rootKey);

        context = getActivity();
        middleware = new MERCIMiddleware(context);

        clientstatusid = (ListPreference) findPreference(getString(R.string.individual_edit_assessment_clientstatusid_field));
        residencytype = (MultiSelectListPreference) findPreference(getString(R.string.individual_edit_assessment_residencytype_field));

        updateClientStatusPreference();
        updateResidencyTypePreference();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        if(pref instanceof MultiSelectListPreference && pref != null && sharedPreferences != null) {
            Log.d("d", "onSharedPreferenceChanged: " + sharedPreferences.getStringSet(key, null));
        } else if(pref instanceof EditTextPreference && pref != null && sharedPreferences != null) {
            pref.setSummary(sharedPreferences.getString(key, ""));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void updateClientStatusPreference(){
        Map<String, String> clientstatusidMap = new HashMap<>();
        switch (middleware.getClientStatusPreference(clientstatusidMap)){
            case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                Collection<String> values = clientstatusidMap.values();
                Collection<String> keys = clientstatusidMap.keySet();
                clientstatusid.setEntries(values.toArray(new CharSequence[values.size()]));
                clientstatusid.setEntryValues(keys.toArray(new CharSequence[keys.size()]));
                break;
            case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
            case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                break;
        }
    }

    public void updateResidencyTypePreference(){
        Map<String, String> residencytypeMap = new HashMap<>();
        switch (middleware.getRecidencyTypePreference(residencytypeMap)){
            case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                Collection<String> values = residencytypeMap.values();
                Collection<String> keys = residencytypeMap.keySet();
                residencytype.setEntries(values.toArray(new CharSequence[values.size()]));
                residencytype.setEntryValues(keys.toArray(new CharSequence[keys.size()]));
                break;
            case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
            case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                break;
        }
    }
}
