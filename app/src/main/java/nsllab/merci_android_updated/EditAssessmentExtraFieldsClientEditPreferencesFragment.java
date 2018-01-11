package nsllab.merci_android_updated;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

/**
 * Created by Moallim on 12/13/2017.
 */

public class EditAssessmentExtraFieldsClientEditPreferencesFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    protected Context context;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.edit_assessment_extra_fields_client_preferences, rootKey);
        context = getActivity();
        updateSummaries();
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
        if(pref != null && sharedPreferences != null) {
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
}
