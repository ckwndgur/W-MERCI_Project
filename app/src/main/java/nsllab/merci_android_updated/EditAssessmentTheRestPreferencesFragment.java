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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Moallim on 12/13/2017.
 */

public class EditAssessmentTheRestPreferencesFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener  {
    protected Context context;
    protected MERCIMiddleware middleware;
    protected ListPreference islandid;
    protected MultiSelectListPreference insurancetypeids;
    protected MultiSelectListPreference damagelocation;
    protected ListPreference damageseverity;
    protected MultiSelectListPreference followuptypeids;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.edit_asseessment_the_rest_preferences, rootKey);
        context = getActivity();
        middleware = new MERCIMiddleware(context);
        islandid = (ListPreference) findPreference(getString(R.string.individual_edit_assessment_islandid_field));
        insurancetypeids = (MultiSelectListPreference) findPreference(getString(R.string.individual_edit_assessment_insurancetypeids_field));
        damagelocation = (MultiSelectListPreference) findPreference(getString(R.string.individual_edit_assessment_damagelocation_field));
        damageseverity = (ListPreference) findPreference(getString(R.string.individual_edit_assessment_damageseverity_field));
        followuptypeids = (MultiSelectListPreference) findPreference(getString(R.string.individual_edit_assessment_followuptypeids_field));

        updateSummaries();
        updateInsuranceTypePreference();
        updateIslandsPreference();
        updateDamageLocationPreference();
        updateDamageSeverityPreference();
        updateFollowUpTypePreference();
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
        if(pref instanceof MultiSelectListPreference && pref != null && sharedPreferences != null) {
            Log.d("d", "onSharedPreferenceChanged: " + sharedPreferences.getStringSet(key, null));
        } else if(pref instanceof EditTextPreference && pref != null && sharedPreferences != null) {
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

    public void updateIslandsPreference(){
        Map<String, String> islandidMap = new HashMap<>();
        switch (middleware.getIslandidPreference(islandidMap)){
            case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                Collection<String> values = islandidMap.values();
                Collection<String> keys = islandidMap.keySet();
                islandid.setEntries(values.toArray(new CharSequence[values.size()]));
                islandid.setEntryValues(keys.toArray(new CharSequence[keys.size()]));
                break;
            case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
            case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                break;
        }
    }

    public void updateInsuranceTypePreference(){
        Map<String, String> insurancetypeidsMap = new HashMap<>();
        switch (middleware.getInsurancetypeidsPreference(insurancetypeidsMap)){
            case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                Collection<String> values = insurancetypeidsMap.values();
                Collection<String> keys = insurancetypeidsMap.keySet();
                insurancetypeids.setEntries(values.toArray(new CharSequence[values.size()]));
                insurancetypeids.setEntryValues(keys.toArray(new CharSequence[keys.size()]));
                break;
            case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
            case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                break;
        }
    }

    public void updateDamageLocationPreference(){
        Map<String, String> damagelocationMap = new HashMap<>();
        switch (middleware.getDamagelocationPreference(damagelocationMap)){
            case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                Collection<String> values = damagelocationMap.values();
                Collection<String> keys = damagelocationMap.keySet();
                damagelocation.setEntries(values.toArray(new CharSequence[values.size()]));
                damagelocation.setEntryValues(keys.toArray(new CharSequence[keys.size()]));
                break;
            case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
            case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                break;
        }
    }

    public void updateDamageSeverityPreference(){
        Map<String, String> damageseverityMap = new HashMap<>();
        switch (middleware.getDamageseverityPreference(damageseverityMap)){
            case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                Collection<String> values = damageseverityMap.values();
                Collection<String> keys = damageseverityMap.keySet();
                damageseverity.setEntries(values.toArray(new CharSequence[values.size()]));
                damageseverity.setEntryValues(keys.toArray(new CharSequence[keys.size()]));
                break;
            case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
            case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                break;
        }
    }

    public void updateFollowUpTypePreference(){
        Map<String, String> followuptypeidsMap = new HashMap<>();
        switch (middleware.getFollowuptypeidsPreference(followuptypeidsMap)){
            case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                Collection<String> values = followuptypeidsMap.values();
                Collection<String> keys = followuptypeidsMap.keySet();
                followuptypeids.setEntries(values.toArray(new CharSequence[values.size()]));
                followuptypeids.setEntryValues(keys.toArray(new CharSequence[keys.size()]));
                break;
            case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
            case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                break;
        }
    }
}
