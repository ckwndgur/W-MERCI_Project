package nsllab.merci_android_updated;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Moallim on 12/11/2017.
 */

public class MERCIStore {
    protected SharedPreferences prefs;
    protected SharedPreferences.Editor editor;
    protected Context context;

    public MERCIStore(Context context){
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
    }

    public Set<String> getStringSet(int key){
        return prefs.getStringSet(context.getString(key), new HashSet<String>());
    }

    public String getString(int key){
        return prefs.getString(context.getString(key), "");
    }

    public Boolean getBoolean(int key){
        return prefs.getBoolean(context.getString(key), false);
    }

    public int getInt(int key){
        return parseInt(prefs.getString(context.getString(key), "0"), 0);
    }

    public Float getFloat(int key){
        return prefs.getFloat(context.getString(key), 0);
    }

    public static int parseInt(String str, int _default){
        int number = 0;
        try {
            if (str != null)
                number = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            number = _default;
        } finally {
            return number;
        }
    }

    public void putString(int key, String value){
        editor.putString(context.getString(key), value);
        editor.commit();
    }

    public void putInt(int key, int value){
        editor.putInt(context.getString(key), value);
        editor.commit();
    }

    public void putFloat(int key, double value){
        editor.putFloat(context.getString(key), (float) value);
        editor.commit();
    }

    public void putBoolean(int key, boolean value){
        editor.putBoolean(context.getString(key), value);
        editor.commit();
    }

    public void putStringSet(int key, Set<String> values){
        editor.putStringSet(context.getString(key), values);
        editor.commit();
    }

    public void resetAll(){
        resetAuthenticationPrefs();
        resetNetworkPrefs();
        resetSupportiveDataPrefs();
        resetCurrentPrefs();
        resetIndividualEditAssessmentPrefs();
    }

    public void resetAuthenticationPrefs(){
        editor.remove(context.getString(R.string.username_field));
        editor.commit();
    }

    public void resetNetworkPrefs(){
        editor.remove(getString(R.string.server_ip_field));
        editor.remove(getString(R.string.ip_port_field));
        editor.remove(getString(R.string.virtual_directory_field));
        editor.remove(getString(R.string.timeout_secs_field));
        editor.commit();
    }

    public void resetSupportiveDataPrefs(){
        editor.remove(getString(R.string.incidents_field));
        editor.remove(getString(R.string.inspectors_field));
        editor.remove(getString(R.string.islands_field));
        editor.remove(getString(R.string.states_field));
        editor.remove(getString(R.string.agency_type_field));
        editor.remove(getString(R.string.client_status_field));
        editor.remove(getString(R.string.damage_location_field));
        editor.remove(getString(R.string.damage_severity_field));
        editor.remove(getString(R.string.federal_jurisdiction_field));
        editor.remove(getString(R.string.incident_type_field));
        editor.remove(getString(R.string.insurance_type_field));
        editor.remove(getString(R.string.notification_department_field));
        editor.remove(getString(R.string.followup_type_field));
        editor.remove(getString(R.string.residency_type_field));
        editor.commit();
    }

    public void resetCurrentPrefs(){
        editor.remove(context.getString(R.string.current_assessments_list_field));
        editor.remove(context.getString(R.string.current_incident_field));
        editor.remove(context.getString(R.string.current_inspector_field));
        editor.remove(context.getString(R.string.current_last_sync_timestamp_field));
        editor.commit();
    }

    public void resetIndividualEditAssessmentPrefs(){
        editor.remove(context.getString(R.string.individual_edit_assessment_affectedperson__firstname_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_affectedperson__lastname_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_affectedperson__homephonenumber_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_affectedperson__cellphonenumber_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_affectedperson__workphonenumber_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_affectedperson__email_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_affectedaddress__street_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_affectedaddress__city_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_affectedaddress__stateid_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_affectedaddress__state_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_affectedaddress__zip_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_clientstatusid_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_residencytype_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_temporaryaddress__street_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_temporaryaddress__city_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_temporaryaddress__stateid_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_temporaryaddress__state_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_temporaryaddress__zip_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_owner__firstname_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_owner__lastname_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_owner__homephonenumber_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_owner__cellphonenumber_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_owner__workphonenumber_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_owner__email_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_insurancetypeids_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_insurancedescription_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_islandid_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_waterheightininches_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_ishabitable_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_damagelocation_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_damageseverity_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_damagedescription_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_damagecosthome_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_damagecostappliances_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_damagecostelectronics_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_damagecostautoboat_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_damagecostfurniture_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_damagecoststructure_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_damagecostbedding_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_damagecostmisc_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_followuptypeids_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_followupdescription_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_comments_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_longitude_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_latitude_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_id_field));
        editor.remove(context.getString(R.string.individual_edit_assessment_timestamp_field));
        editor.commit();
    }
}
