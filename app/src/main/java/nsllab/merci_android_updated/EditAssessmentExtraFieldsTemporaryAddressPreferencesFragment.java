package nsllab.merci_android_updated;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.widget.Toast;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Moallim on 12/13/2017.
 */

public class EditAssessmentExtraFieldsTemporaryAddressPreferencesFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
    protected Context context;
    protected SharedPreferences prefs;
    protected MERCIMiddleware middleware;
    protected ListPreference stateid;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.edit_assessment_extra_fields_temporary_address_preferences, rootKey);
        context = getActivity();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        middleware = new MERCIMiddleware(context);
        stateid = (ListPreference) findPreference(getString(R.string.individual_edit_assessment_temporaryaddress__stateid_field));
        updateSummaries();
        updateStatesPreference();
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        if(pref instanceof EditTextPreference && pref != null && sharedPreferences != null) {
            pref.setSummary(sharedPreferences.getString(key, ""));
        }
    }

    public void updateSummaries(){
        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++){
            Preference p = getPreferenceScreen().getPreference(i);
            if(p instanceof EditTextPreference){
                EditTextPreference editTextPref = (EditTextPreference) p;
                p.setSummary(editTextPref.getText());
            }
        }
    }

    public void updateStatesPreference(){
        Map<String, String> stateidMap = new HashMap<>();
        switch (middleware.getStateidPreference(stateidMap)){
            case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                Collection<String> values = stateidMap.values();
                Collection<String> keys = stateidMap.keySet();
                stateid.setEntries(values.toArray(new CharSequence[values.size()]));
                stateid.setEntryValues(keys.toArray(new CharSequence[keys.size()]));
                break;
            case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
            case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                break;
        }
    }
}
