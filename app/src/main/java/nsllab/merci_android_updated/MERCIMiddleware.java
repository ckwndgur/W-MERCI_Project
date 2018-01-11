package nsllab.merci_android_updated;

import android.content.Context;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Moallim on 12/12/2017.
 */

public class MERCIMiddleware {
    protected MERCIStore store;
    protected Context context;
    protected static final String MIDDLEWARE_JSON_ERROR = "MIDDLEWARE_JSON_ERROR";
    protected static final String MIDDLEWARE_FUNC_SUCCEED = "MIDDLEWARE_FUNC_SUCCEED";
    protected static final String MIDDLEWARE_FUNC_FAILED = "MIDDLEWARE_FUNC_FAILED";

    public MERCIMiddleware(Context context){
        this.context = context;
        this.store = new MERCIStore(context);
    }

    public String initiateMiddleware(){
        String username = store.getString(R.string.username_field);
        if(username.equals(null) || username.equals("")){
            return MIDDLEWARE_FUNC_FAILED;
        } else {
            return MIDDLEWARE_FUNC_SUCCEED;
        }
    }

    public String signInMiddleware(JSONObject result, String username){
        try {
            String auth = result.getString("authenticated");
            if(auth.equals("1")){
                store.putString(R.string.username_field, username);
                return MIDDLEWARE_FUNC_SUCCEED;
            } else {
                return MIDDLEWARE_FUNC_FAILED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String checkConnectivityMiddleware(JSONObject result){
        try {
            String auth = result.getString("authenticated");
            if(auth.equals("3")){
                return MIDDLEWARE_FUNC_SUCCEED;
            } else {
                return MIDDLEWARE_FUNC_FAILED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_FUNC_FAILED;
        }
    }

    public String relatedDataIncidentMiddleware(JSONObject result, List<Map<String, String>> array){
        try {
            JSONArray incidentsArray = result.getJSONArray("results");
            List<Map<String, String>> incidentsArrayMap = new ArrayList<>();
            for(int i = 0; i < incidentsArray.length(); i++){
                JSONObject incidentJSON = incidentsArray.getJSONObject(i);
                if(incidentJSON == null) {
                    return MIDDLEWARE_FUNC_FAILED;
                } else {
                    Map incidentMap = new HashMap();
                    incidentMap.put("Id", incidentJSON.getString("Id"));
                    incidentMap.put("Name", incidentJSON.getString("Name"));
                    incidentMap.put("OpenDate",incidentJSON.getString("OpenDate"));
                    incidentMap.put("CloseDate", incidentJSON.getString("CloseDate"));
                    incidentMap.put("Description", incidentJSON.getString("Description"));
                    incidentMap.put("AffectedIslands", incidentJSON.getString("AffectedIslands"));
                    incidentMap.put("IncidentType_Id", incidentJSON.getString("IncidentType_Id"));
                    incidentMap.put("IncidentType", incidentJSON.getString("IncidentType"));
                    incidentMap.put("IncidentStatus", incidentJSON.getString("IncidentStatus"));
                    incidentMap.put("DamageCostTotal", incidentJSON.getString("DamageCostTotal"));
                    incidentMap.put("JSON", incidentJSON.toString());
                    incidentsArrayMap.add(incidentMap);
                }
            }
            array.addAll(incidentsArrayMap);
            store.putString(R.string.incidents_field, result.toString());
            return MIDDLEWARE_FUNC_SUCCEED;
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String relatedDataInspectorMiddleware(JSONObject result){
        try {
            String username = store.getString(R.string.username_field);
            JSONArray inspectorsArray = result.getJSONArray("results");
            List<Map<String, String>> inspectorsArrayMap = new ArrayList<>();
            for (int i = 0; i < inspectorsArray.length(); i++) {
                JSONObject inspectorJSON = inspectorsArray.getJSONObject(i);
                if(inspectorJSON == null) {
                    return MIDDLEWARE_FUNC_FAILED;
                } else {
                    Map<String, String> inspectorMap = new HashMap();
                    inspectorMap.put("Id", inspectorJSON.getString("Id"));
                    inspectorMap.put("Version", inspectorJSON.getString("Version"));
                    inspectorMap.put("Person_FirstName", inspectorJSON.getString("Person_FirstName"));
                    inspectorMap.put("Person_LastName", inspectorJSON.getString("Person_LastName"));
                    inspectorMap.put("Person_WorkPhoneNumber", inspectorJSON.getString("Person_WorkPhoneNumber"));
                    inspectorMap.put("Person_CellPhoneNumber", inspectorJSON.getString("Person_CellPhoneNumber"));
                    inspectorMap.put("Person_Email", inspectorJSON.getString("Person_Email"));
                    inspectorMap.put("AssignedPhoneSerial", inspectorJSON.getString("AssignedPhoneSerial"));
                    inspectorMap.put("IsActive",inspectorJSON.getString("IsActive"));
                    inspectorMap.put("Username", inspectorJSON.getString("Username"));
                    inspectorMap.put("AccessLevel", inspectorJSON.getString("AccessLevel"));
                    inspectorMap.put("IsInspector", inspectorJSON.getString("IsInspector"));
                    inspectorMap.put("Password", inspectorJSON.getString("Password"));
                    inspectorMap.put("role_id", inspectorJSON.getString("role_id"));
                    inspectorMap.put("role",inspectorJSON.getString("role"));
                    inspectorMap.put("Status", inspectorJSON.getString("Status"));
                    inspectorMap.put("JSON", inspectorJSON.toString());
                    inspectorsArrayMap.add(inspectorMap);
                    if(inspectorMap.get("Username").equals(username)){
                        store.putString(R.string.current_inspector_field, inspectorMap.get("JSON"));
                        break;
                    }
                }
            }
            return MIDDLEWARE_FUNC_SUCCEED;
        }  catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String relatedDataIslandMiddleware(JSONObject result){
        try {
            if(result.getJSONArray("results") == null) {
                return MIDDLEWARE_JSON_ERROR;
            } else {
                store.putString(R.string.islands_field, result.toString());
                return MIDDLEWARE_FUNC_SUCCEED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String relatedDataStatesMiddleware(JSONObject result){
        try {
            if(result.getJSONArray("results") == null) {
                return MIDDLEWARE_JSON_ERROR;
            } else {
                store.putString(R.string.states_field, result.toString());
                return MIDDLEWARE_FUNC_SUCCEED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String relatedDataAgencyTypeMiddleware(JSONObject result){
        try {
            if(result.getJSONArray("results") == null) {
                return MIDDLEWARE_JSON_ERROR;
            } else {
                store.putString(R.string.agency_type_field, result.toString());
                return MIDDLEWARE_FUNC_SUCCEED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String relatedDataClientStatusMiddleware(JSONObject result){
        try {
            if(result.getJSONArray("results") == null) {
                return MIDDLEWARE_JSON_ERROR;
            } else {
                store.putString(R.string.client_status_field, result.toString());
                return MIDDLEWARE_FUNC_SUCCEED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String relatedDataDamageLocationMiddleware(JSONObject result){
        try {
            if(result.getJSONArray("results") == null) {
                return MIDDLEWARE_JSON_ERROR;
            } else {
                store.putString(R.string.damage_location_field, result.toString());
                return MIDDLEWARE_FUNC_SUCCEED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String relatedDataDamageSeverityMiddleware(JSONObject result){
        try {
            if(result.getJSONArray("results") == null) {
                return MIDDLEWARE_JSON_ERROR;
            } else {
                store.putString(R.string.damage_severity_field, result.toString());
                return MIDDLEWARE_FUNC_SUCCEED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String relatedDataFederalJurisdictionMiddleware(JSONObject result){
        try {
            if(result.getJSONArray("results") == null) {
                return MIDDLEWARE_JSON_ERROR;
            } else {
                store.putString(R.string.federal_jurisdiction_field, result.toString());
                return MIDDLEWARE_FUNC_SUCCEED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String relatedDataIncidentTypeMiddleware(JSONObject result){
        try {
            if(result.getJSONArray("results") == null) {
                return MIDDLEWARE_JSON_ERROR;
            } else {
                store.putString(R.string.incident_type_field, result.toString());
                return MIDDLEWARE_FUNC_SUCCEED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String relatedDataInsuranceTypeMiddleware(JSONObject result){
        try {
            if(result.getJSONArray("results") == null) {
                return MIDDLEWARE_JSON_ERROR;
            } else {
                store.putString(R.string.insurance_type_field, result.toString());
                return MIDDLEWARE_FUNC_SUCCEED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String relatedDataNotificationDepartmentMiddleware(JSONObject result){
        try {
            if(result.getJSONArray("results") == null) {
                return MIDDLEWARE_JSON_ERROR;
            } else {
                store.putString(R.string.notification_department_field, result.toString());
                return MIDDLEWARE_FUNC_SUCCEED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String relatedDataFollowupTypeMiddleware(JSONObject result){
        try {
            if(result.getJSONArray("results") == null) {
                return MIDDLEWARE_JSON_ERROR;
            } else {
                store.putString(R.string.followup_type_field, result.toString());
                return MIDDLEWARE_FUNC_SUCCEED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String relatedDataResidencyTypeMiddleware(JSONObject result){
        try {
            if(result.getJSONArray("results") == null) {
                return MIDDLEWARE_JSON_ERROR;
            } else {
                store.putString(R.string.residency_type_field, result.toString());
                return MIDDLEWARE_FUNC_SUCCEED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String signOutMiddleware(){
        store.resetAuthenticationPrefs();
        return MIDDLEWARE_FUNC_SUCCEED;
    }

    public String setCurrentIncidentMiddleware(List<Map<String, String>> incidentsList, CharSequence itemTitle, Map<String, String> incidentMap){
        Map<String, String> currentIncidentMap = new HashMap<>();
        for(int i = 0; i < incidentsList.size(); i++){
            if(incidentsList.get(i).get("Name").equals(itemTitle)) {
                incidentMap.put("Id", incidentsList.get(i).get("Id"));
                incidentMap.put("Name", incidentsList.get(i).get("Name"));
                currentIncidentMap = incidentsList.get(i);
                break;
            }
        }
        store.putString(R.string.current_incident_field, currentIncidentMap.get("JSON"));
        if(currentIncidentMap != null)
            return MIDDLEWARE_FUNC_SUCCEED;
        else
            return MIDDLEWARE_FUNC_FAILED;
    }

    public String getCurrentIncidentMiddleware(Map<String, String>  incidentMap){
        try {
            JSONObject currentIncidentJSON = new JSONObject(store.getString(R.string.current_incident_field));
            if(!currentIncidentJSON.has("Id") || !currentIncidentJSON.has("Name")) {
                return MIDDLEWARE_FUNC_FAILED;
            } else {
                incidentMap.put("Id", currentIncidentJSON.getString("Id"));
                incidentMap.put("Name", currentIncidentJSON.getString("Name"));
                return MIDDLEWARE_FUNC_SUCCEED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String getCurrentAssessmentDetailsMiddleware(Map<String, String> assessmentMap){
        String affectedpersonFirstName = store.getString(R.string.individual_edit_assessment_affectedperson__firstname_field);
        String affectedpersonLastName = store.getString(R.string.individual_edit_assessment_affectedperson__lastname_field);
        if(affectedpersonFirstName != "" && affectedpersonLastName != ""){
            assessmentMap.put("Name", String.format("%s %s", affectedpersonFirstName, affectedpersonLastName));
            return MIDDLEWARE_FUNC_SUCCEED;
        } else {
            return MIDDLEWARE_FUNC_FAILED;
        }
    }

    public String getClientStatusPreference(Map<String, String> clientstatusidMap){
        try {
            JSONObject clientstatusidJSON = new JSONObject(store.getString(R.string.client_status_field));
            JSONArray clientstatusidJSONArray = clientstatusidJSON.getJSONArray("results");
            if(clientstatusidJSON == null || clientstatusidJSONArray == null){
                return MIDDLEWARE_FUNC_FAILED;
            } else {
                for (int j = 0; j < clientstatusidJSONArray.length(); j++) {
                    JSONObject entry = clientstatusidJSONArray.getJSONObject(j);
                    clientstatusidMap.put(entry.getString("Id"), entry.getString("Value"));
                }
                return MIDDLEWARE_FUNC_SUCCEED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String getRecidencyTypePreference(Map<String, String> residencytypeMap){
        try {
            JSONObject residencytypeJSON = new JSONObject(store.getString(R.string.residency_type_field));
            JSONArray residencytypeJSONArray = residencytypeJSON.getJSONArray("results");
            if(residencytypeJSON == null || residencytypeJSONArray == null){
                return MIDDLEWARE_FUNC_FAILED;
            } else {
                for (int j = 0; j < residencytypeJSONArray.length(); j++) {
                    JSONObject entry = residencytypeJSONArray.getJSONObject(j);
                    residencytypeMap.put(entry.getString("Id"), entry.getString("Value"));
                }
                return MIDDLEWARE_FUNC_SUCCEED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String getIslandidPreference(Map<String, String> islandidMap){
        try {
            JSONObject islandidJSON = new JSONObject(store.getString(R.string.islands_field));
            JSONArray islandidJSONArray = islandidJSON.getJSONArray("results");
            if(islandidJSON == null || islandidJSONArray == null){
                return MIDDLEWARE_FUNC_FAILED;
            } else {
                for (int j = 0; j < islandidJSONArray.length(); j++) {
                    JSONObject entry = islandidJSONArray.getJSONObject(j);
                    islandidMap.put(entry.getString("Id"), entry.getString("Value"));
                }
                return MIDDLEWARE_FUNC_SUCCEED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String getInsurancetypeidsPreference(Map<String, String> insurancetypeidsMap){
        try {
            JSONObject insurancetypeidsJSON = new JSONObject(store.getString(R.string.insurance_type_field));
            JSONArray insurancetypeidsJSONArray = insurancetypeidsJSON.getJSONArray("results");
            if(insurancetypeidsJSON == null || insurancetypeidsJSONArray == null){
                return MIDDLEWARE_FUNC_FAILED;
            } else {
                for (int j = 0; j < insurancetypeidsJSONArray.length(); j++) {
                    JSONObject entry = insurancetypeidsJSONArray.getJSONObject(j);
                    insurancetypeidsMap.put(entry.getString("Id"), entry.getString("Value"));
                }
                return MIDDLEWARE_FUNC_SUCCEED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String getDamagelocationPreference(Map<String, String> damagelocationMap){
        try {
            JSONObject damagelocationJSON = new JSONObject(store.getString(R.string.damage_location_field));
            JSONArray damagelocationJSONArray = damagelocationJSON.getJSONArray("results");
            if(damagelocationJSON == null || damagelocationJSONArray == null){
                return MIDDLEWARE_FUNC_FAILED;
            } else {
                for (int j = 0; j < damagelocationJSONArray.length(); j++) {
                    JSONObject entry = damagelocationJSONArray.getJSONObject(j);
                    damagelocationMap.put(entry.getString("Id"), entry.getString("Value"));
                }
                return MIDDLEWARE_FUNC_SUCCEED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String getDamageseverityPreference(Map<String, String> damageseverityMap){
        try {
            JSONObject damageseverityJSON = new JSONObject(store.getString(R.string.damage_severity_field));
            JSONArray damageseverityJSONArray = damageseverityJSON.getJSONArray("results");
            if(damageseverityJSON == null || damageseverityJSONArray == null){
                return MIDDLEWARE_FUNC_FAILED;
            } else {
                for (int j = 0; j < damageseverityJSONArray.length(); j++) {
                    JSONObject entry = damageseverityJSONArray.getJSONObject(j);
                    damageseverityMap.put(entry.getString("Id"), entry.getString("Value"));
                }
                return MIDDLEWARE_FUNC_SUCCEED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String getFollowuptypeidsPreference(Map<String, String> followuptypeidsMap){
        try {
            JSONObject followuptypeidsJSON = new JSONObject(store.getString(R.string.followup_type_field));
            JSONArray followuptypeidsJSONArray = followuptypeidsJSON.getJSONArray("results");
            if(followuptypeidsJSON == null || followuptypeidsJSONArray == null){
                return MIDDLEWARE_FUNC_FAILED;
            } else {
                for (int j = 0; j < followuptypeidsJSONArray.length(); j++) {
                    JSONObject entry = followuptypeidsJSONArray.getJSONObject(j);
                    followuptypeidsMap.put(entry.getString("Id"), entry.getString("Value"));
                }
                return MIDDLEWARE_FUNC_SUCCEED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String getStateidPreference(Map<String, String> stateidMap){
        try {
            JSONObject stateidMapJSON = new JSONObject(store.getString(R.string.states_field));
            JSONArray stateidMapJSONArray = stateidMapJSON.getJSONArray("results");
            if(stateidMapJSON == null || stateidMapJSONArray == null){
                return MIDDLEWARE_FUNC_FAILED;
            } else {
                for (int j = 0; j < stateidMapJSONArray.length(); j++) {
                    JSONObject entry = stateidMapJSONArray.getJSONObject(j);
                    stateidMap.put(entry.getString("Id"), entry.getString("Value"));
                }
                return MIDDLEWARE_FUNC_SUCCEED;
            }
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public JSONObject contractIndividualAssessment(){
        JSONObject assessment = new JSONObject();
        try {
            JSONObject current_inspector = new JSONObject(store.getString(R.string.current_inspector_field));
            JSONObject current_incident = new JSONObject(store.getString(R.string.current_incident_field));

            assessment.put("assessmenttype", AssessmentBriefModel.Individual);
            assessment.put("guid", store.getString(R.string.individual_edit_assessment_id_field));
            assessment.put("timestamp", store.getString(R.string.individual_edit_assessment_timestamp_field));
            assessment.put("longitude", store.getFloat(R.string.individual_edit_assessment_longitude_field));
            assessment.put("latitude", store.getFloat(R.string.individual_edit_assessment_latitude_field));

            assessment.put("incidentid", current_incident.getString("Id"));
            assessment.put("incidenttype", current_incident.getString("IncidentType"));
            assessment.put("incidenttypeid", current_incident.getString("IncidentType_Id"));

            assessment.put("primaryinspectorid", current_inspector.getString("Id"));
            JSONArray inspectorids = new JSONArray();
            inspectorids.put(current_inspector.getString("Id"));
            assessment.put("inspectorids", inspectorids);

            assessment.put("damagecostbedding", store.getInt(R.string.individual_edit_assessment_damagecostbedding_field));
            assessment.put("damagecostmisc", store.getInt(R.string.individual_edit_assessment_damagecostmisc_field));
            assessment.put("damagecosthome", store.getInt(R.string.individual_edit_assessment_damagecosthome_field));
            assessment.put("damagecostelectronics", store.getInt(R.string.individual_edit_assessment_damagecostelectronics_field));
            assessment.put("damagecostautoboat", store.getInt(R.string.individual_edit_assessment_damagecostautoboat_field));
            assessment.put("damagecostappliances", store.getInt(R.string.individual_edit_assessment_damagecostappliances_field));
            assessment.put("damagecostfurniture", store.getInt(R.string.individual_edit_assessment_damagecostfurniture_field));
            assessment.put("damagecoststructure", store.getInt(R.string.individual_edit_assessment_damagecoststructure_field));
            assessment.put("damagecosttotal", assessment.getInt("damagecostbedding") + assessment.getInt("damagecostmisc") +
                    assessment.getInt("damagecosthome") + assessment.getInt("damagecostelectronics") +
                    assessment.getInt("damagecostautoboat") + assessment.getInt("damagecostappliances") +
                    assessment.getInt("damagecostfurniture") + assessment.getInt("damagecoststructure"));

            assessment.put("islandid", store.getString(R.string.individual_edit_assessment_islandid_field));

            JSONObject affectedperson = new JSONObject();
            affectedperson.put("firstname", store.getString(R.string.individual_edit_assessment_affectedperson__firstname_field));
            affectedperson.put("lastname", store.getString(R.string.individual_edit_assessment_affectedperson__lastname_field));
            affectedperson.put("homephonenumber", store.getString(R.string.individual_edit_assessment_affectedperson__homephonenumber_field));
            affectedperson.put("cellphonenumber", store.getString(R.string.individual_edit_assessment_affectedperson__cellphonenumber_field));
            affectedperson.put("workphonenumber", store.getString(R.string.individual_edit_assessment_affectedperson__workphonenumber_field));
            affectedperson.put("email", store.getString(R.string.individual_edit_assessment_affectedperson__email_field));
            assessment.put("affectedperson", affectedperson);

            assessment.put("ishabitable", store.getBoolean(R.string.individual_edit_assessment_ishabitable_field));
            assessment.put("followupdescription", store.getString(R.string.individual_edit_assessment_followupdescription_field));
            JSONArray damagelocationids = new JSONArray();
            Set<String> damagelocationidsSet = store.getStringSet(R.string.individual_edit_assessment_damagelocation_field);
            for(Iterator<String> it = damagelocationidsSet.iterator(); it.hasNext();){
                damagelocationids.put(it.next());
            }
            assessment.put("damagelocationids", damagelocationids);
            assessment.put("damagedescription", store.getString(R.string.individual_edit_assessment_damagedescription_field));
            assessment.put("notes", store.getString(R.string.individual_edit_assessment_comments_field));
            assessment.put("waterheightininches", store.getString(R.string.individual_edit_assessment_waterheightininches_field));
            JSONArray followuptypeids = new JSONArray();
            Set<String> followuptypeidsSet = store.getStringSet(R.string.individual_edit_assessment_followuptypeids_field);
            for(Iterator<String> it = followuptypeidsSet.iterator(); it.hasNext();){
                followuptypeids.put(it.next());
            }
            assessment.put("followuptypeids", followuptypeids);

            JSONObject temporaryaddress = new JSONObject();
            temporaryaddress.put("city", store.getString(R.string.individual_edit_assessment_temporaryaddress__city_field));
            temporaryaddress.put("street", store.getString(R.string.individual_edit_assessment_temporaryaddress__street_field));
            temporaryaddress.put("zip", store.getString(R.string.individual_edit_assessment_temporaryaddress__zip_field));
            temporaryaddress.put("stateid", store.getString(R.string.individual_edit_assessment_temporaryaddress__stateid_field));
            assessment.put("temporaryaddress", temporaryaddress);

            assessment.put("damageseverityid", store.getString(R.string.individual_edit_assessment_damageseverity_field));
            assessment.put("clientstatusid", store.getString(R.string.individual_edit_assessment_clientstatusid_field));

            JSONObject owner = new JSONObject();
            owner.put("firstname", store.getString(R.string.individual_edit_assessment_owner__firstname_field));
            owner.put("lastname", store.getString(R.string.individual_edit_assessment_owner__lastname_field));
            owner.put("homephonenumber", store.getString(R.string.individual_edit_assessment_owner__homephonenumber_field));
            owner.put("cellphonenumber", store.getString(R.string.individual_edit_assessment_owner__cellphonenumber_field));
            owner.put("workphonenumber", store.getString(R.string.individual_edit_assessment_owner__workphonenumber_field));
            owner.put("email", store.getString(R.string.individual_edit_assessment_owner__email_field));
            assessment.put("owner", owner);

            JSONArray residencytype = new JSONArray();
            Set<String> residencytypeSet = store.getStringSet(R.string.individual_edit_assessment_residencytype_field);
            for(Iterator<String> it = residencytypeSet.iterator(); it.hasNext();){
                residencytype.put(it.next());
            }
            assessment.put("residencytype", residencytype);

            JSONArray insurancetypeids = new JSONArray();
            Set<String> insurancetypeidsSet = store.getStringSet(R.string.individual_edit_assessment_insurancetypeids_field);
            for(Iterator<String> it = insurancetypeidsSet.iterator(); it.hasNext();){
                insurancetypeids.put(it.next());
            }
            assessment.put("insurancetypeids", insurancetypeids);

            JSONObject affectedaddress = new JSONObject();
            affectedaddress.put("city", store.getString(R.string.individual_edit_assessment_affectedaddress__city_field));
            affectedaddress.put("street", store.getString(R.string.individual_edit_assessment_affectedaddress__street_field));
            affectedaddress.put("zip", store.getString(R.string.individual_edit_assessment_affectedaddress__zip_field));
            affectedaddress.put("stateid", store.getString(R.string.individual_edit_assessment_affectedaddress__stateid_field));
            assessment.put("affectedaddress", affectedaddress);

        } catch (JSONException e){
            throw e;
        } finally {
            return assessment;
        }
    }

    public String updateCurrentLocationMiddleware(double lat, double lng){
        store.putFloat(R.string.individual_edit_assessment_latitude_field, lat);
        store.putFloat(R.string.individual_edit_assessment_longitude_field, lng);
        return MIDDLEWARE_FUNC_SUCCEED;
    }

    public String updateGUIDAndTimestampMiddleware(){
        String assessment_id = store.getString(R.string.individual_edit_assessment_id_field);
        if(assessment_id == null || assessment_id.equals(""))
            assessment_id = java.util.UUID.randomUUID().toString();
        store.putString(R.string.individual_edit_assessment_id_field, assessment_id);
        String assessment_timestamp = store.getString(R.string.individual_edit_assessment_timestamp_field);
        if(assessment_timestamp == null || assessment_timestamp == "")
            assessment_timestamp = AssessmentBriefModel.sdf.format(Calendar.getInstance().getTime()).toString();
        store.putString(R.string.individual_edit_assessment_timestamp_field, assessment_timestamp);
        return MIDDLEWARE_FUNC_SUCCEED;
    }

    public String expandIndividualAssessmentMiddleware(AssessmentBriefModel model, JSONObject assessment){
        try {
            store.putString(R.string.individual_edit_assessment_id_field, model.assessment_id);
            store.putString(R.string.individual_edit_assessment_timestamp_field, model.timestamp);
            store.putFloat(R.string.individual_edit_assessment_latitude_field, model.position[0]);
            store.putFloat(R.string.individual_edit_assessment_longitude_field, model.position[1]);

            store.putString(R.string.individual_edit_assessment_damagecostbedding_field, assessment.getString("damagecostbedding"));
            store.putString(R.string.individual_edit_assessment_damagecostmisc_field, assessment.getString("damagecostmisc"));
            store.putString(R.string.individual_edit_assessment_damagecosthome_field, assessment.getString("damagecosthome"));
            store.putString(R.string.individual_edit_assessment_damagecostelectronics_field, assessment.getString("damagecostelectronics"));
            store.putString(R.string.individual_edit_assessment_damagecostautoboat_field, assessment.getString("damagecostautoboat"));
            store.putString(R.string.individual_edit_assessment_damagecostappliances_field, assessment.getString("damagecostappliances"));
            store.putString(R.string.individual_edit_assessment_damagecostfurniture_field, assessment.getString("damagecostfurniture"));
            store.putString(R.string.individual_edit_assessment_damagecoststructure_field, assessment.getString("damagecoststructure"));

            String islandid = "";
            if(assessment.has("islandid"))
                islandid = assessment.getString("islandid");
            else if(assessment.has("island"))
                islandid = assessment.getString("island");

            store.putString(R.string.individual_edit_assessment_islandid_field, islandid);

            JSONObject affectedperson = assessment.getJSONObject("affectedperson");
            store.putString(R.string.individual_edit_assessment_affectedperson__firstname_field, affectedperson.getString("firstname"));
            store.putString(R.string.individual_edit_assessment_affectedperson__lastname_field, affectedperson.getString("lastname"));
            store.putString(R.string.individual_edit_assessment_affectedperson__homephonenumber_field, affectedperson.getString("homephonenumber"));
            store.putString(R.string.individual_edit_assessment_affectedperson__cellphonenumber_field, affectedperson.getString("cellphonenumber"));
            store.putString(R.string.individual_edit_assessment_affectedperson__workphonenumber_field, affectedperson.getString("workphonenumber"));
            store.putString(R.string.individual_edit_assessment_affectedperson__email_field, affectedperson.getString("email"));
            store.putBoolean(R.string.individual_edit_assessment_ishabitable_field, assessment.getBoolean("ishabitable"));
            store.putString(R.string.individual_edit_assessment_followupdescription_field, assessment.getString("followupdescription"));

            JSONArray damagelocationids = assessment.getJSONArray("damagelocationids");
            Set<String> damagelocationidsSet = new HashSet<>();
            for(int i = 0; i < damagelocationids.length(); i++){
                damagelocationidsSet.add(damagelocationids.getString(i));
            }
            store.putStringSet(R.string.individual_edit_assessment_damagelocation_field, damagelocationidsSet);

            store.putString(R.string.individual_edit_assessment_damagedescription_field, assessment.getString("damagedescription"));
            store.putString(R.string.individual_edit_assessment_comments_field, assessment.getString("notes"));
            store.putString(R.string.individual_edit_assessment_waterheightininches_field, assessment.getString("waterheightininches"));

            JSONArray followuptypeids = assessment.getJSONArray("followuptypeids");
            Set<String> followuptypeidsSet = new HashSet<>();
            for(int i = 0; i < followuptypeids.length(); i++){
                followuptypeidsSet.add(followuptypeids.getString(i));
            }
            store.putStringSet(R.string.individual_edit_assessment_followuptypeids_field, followuptypeidsSet);

            JSONObject temporaryaddress = assessment.getJSONObject("temporaryaddress");
            store.putString(R.string.individual_edit_assessment_temporaryaddress__city_field, temporaryaddress.getString("city"));
            store.putString(R.string.individual_edit_assessment_temporaryaddress__street_field, temporaryaddress.getString("street"));
            store.putString(R.string.individual_edit_assessment_temporaryaddress__zip_field, temporaryaddress.getString("zip"));
            store.putString(R.string.individual_edit_assessment_temporaryaddress__stateid_field, temporaryaddress.getString("stateid"));

            String damageseverityid = "";
            if(assessment.has("damageseverityid"))
                damageseverityid = assessment.getString("damageseverityid");
            else if(assessment.has("damageseverity"))
                damageseverityid = assessment.getString("damageseverity");
            store.putString(R.string.individual_edit_assessment_damageseverity_field, damageseverityid);

            String clientstatusid = "";
            if(assessment.has("clientstatusid"))
                clientstatusid = assessment.getString("clientstatusid");
            else if(assessment.has("clientstatus"))
                clientstatusid = assessment.getString("clientstatus");
            store.putString(R.string.individual_edit_assessment_clientstatusid_field, clientstatusid);

            JSONObject owner = assessment.getJSONObject("owner");
            store.putString(R.string.individual_edit_assessment_owner__firstname_field, owner.getString("firstname"));
            store.putString(R.string.individual_edit_assessment_owner__lastname_field, owner.getString("lastname"));
            store.putString(R.string.individual_edit_assessment_owner__homephonenumber_field, owner.getString("homephonenumber"));
            store.putString(R.string.individual_edit_assessment_owner__cellphonenumber_field, owner.getString("cellphonenumber"));
            store.putString(R.string.individual_edit_assessment_owner__workphonenumber_field, owner.getString("workphonenumber"));
            store.putString(R.string.individual_edit_assessment_owner__email_field, owner.getString("email"));

            JSONArray residencytype = assessment.getJSONArray("residencytype");
            Set<String> residencytypeSet = new HashSet<>();
            for(int i = 0; i < residencytype.length(); i++){
                residencytypeSet.add(residencytype.getString(i));
            }
            store.putStringSet(R.string.individual_edit_assessment_residencytype_field, residencytypeSet);

            JSONArray insurancetypeids = assessment.getJSONArray("insurancetypeids");
            Set<String> insurancetypeidsSet = new HashSet<>();
            for(int i = 0; i < insurancetypeids.length(); i++){
                insurancetypeidsSet.add(insurancetypeids.getString(i));
            }
            store.putStringSet(R.string.individual_edit_assessment_insurancetypeids_field, insurancetypeidsSet);

            JSONObject affectedaddress = assessment.getJSONObject("affectedaddress");
            store.putString(R.string.individual_edit_assessment_affectedaddress__city_field, affectedaddress.getString("city"));
            store.putString(R.string.individual_edit_assessment_affectedaddress__street_field, affectedaddress.getString("street"));
            store.putString(R.string.individual_edit_assessment_affectedaddress__zip_field, affectedaddress.getString("zip"));
            store.putString(R.string.individual_edit_assessment_affectedaddress__stateid_field, affectedaddress.getString("stateid"));

            return MIDDLEWARE_FUNC_SUCCEED;
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String retrieveAssessmentMiddleware(AssessmentBriefModel model){
        try {
            String assessmentsListString = store.getString(R.string.current_assessments_list_field);
            if(assessmentsListString == null || assessmentsListString == ""){
                assessmentsListString = "[]";
            }
            JSONArray assessmentsList = new JSONArray(assessmentsListString);
            for(int i = 0; i < assessmentsList.length(); i++){
                JSONObject assessment_x = assessmentsList.getJSONObject(i);
                if((assessment_x.has("id") && assessment_x.getString("id").equals(model.assessment_id)) || (assessment_x.has("guid") && assessment_x.getString("guid").equals(model.assessment_id))){
                    switch(model.type){
                        case AssessmentBriefModel.Individual:
                            return expandIndividualAssessmentMiddleware(model, assessment_x);
                        case AssessmentBriefModel.Business:
                        case AssessmentBriefModel.Infrastructure:
                            return MIDDLEWARE_FUNC_FAILED;
                    }
                }
            }
            return MIDDLEWARE_FUNC_FAILED;
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String storeAssessmentMiddleware(String assessmentType){
        try {
            JSONObject assessment = new JSONObject();
            String assessmentsListString = store.getString(R.string.current_assessments_list_field);
            if(assessmentsListString == null || assessmentsListString == ""){
                assessmentsListString = "[]";
            }
            JSONArray assessmentsList = new JSONArray(assessmentsListString);
            switch (assessmentType){
                case AssessmentBriefModel.Individual:
                    assessment = contractIndividualAssessment();
                    break;
                case AssessmentBriefModel.Business:
                case AssessmentBriefModel.Infrastructure:
                    return MIDDLEWARE_FUNC_FAILED;
            }
            for(int i = 0; i < assessmentsList.length(); i++){
                JSONObject assessment_x = assessmentsList.getJSONObject(i);
                if((assessment_x.has("id") && assessment_x.getString("id").equals(assessment.getString("guid"))) ||
                        (assessment_x.has("guid") && assessment_x.getString("guid").equals(assessment.getString("guid")))){
                    assessmentsList.remove(i);
                    break;
                }
            }
            assessmentsList.put(assessment);
            store.putString(R.string.current_assessments_list_field, assessmentsList.toString());
            return MIDDLEWARE_FUNC_SUCCEED;
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String getAssessmentsMiddleware(List<AssessmentBriefModel> models){
        try {
            JSONObject currentIncidentJSON = new JSONObject(store.getString(R.string.current_incident_field));
            if(!currentIncidentJSON.has("Id"))
                return MIDDLEWARE_FUNC_FAILED;
            String assessmentsListString = store.getString(R.string.current_assessments_list_field);
            if(assessmentsListString == null || assessmentsListString.equals("")){
                assessmentsListString = "[]";
            }
            JSONArray assessmentsList = new JSONArray(assessmentsListString);
            for(int i = 0; i < assessmentsList.length(); i++){
                JSONObject assessment = assessmentsList.getJSONObject(i);
                if(assessment.has("parentincident_id") && !assessment.getString("parentincident_id").equals(currentIncidentJSON.getString("Id")) ||
                        assessment.has("incidentid") && !assessment.getString("incidentid").equals(currentIncidentJSON.getString("Id")))
                    continue;

                JSONObject affectedperson = assessment.getJSONObject("affectedperson");
                String assessmentName = String.format("%s %s", affectedperson.getString("firstname"), affectedperson.getString("lastname"));
                String assessmentTimestamp = assessment.getString("timestamp");
                String assessmentType = assessment.getString("assessmenttype");
                String assessmentId = "";
                if(assessment.has("guid")){
                    assessmentId = assessment.getString("guid");
                } else {
                    assessmentId = assessment.getString("id");
                }
                Double[] position = new Double[]{assessment.getDouble("latitude"), assessment.getDouble("longitude")};
                AssessmentBriefModel assessmentModel = new AssessmentBriefModel(assessmentId, position, assessmentName, "", false, false, false, assessmentType, assessmentTimestamp);
                models.add(assessmentModel);
            }
            return MIDDLEWARE_FUNC_SUCCEED;
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String getAssessmentsToSyncMiddleware(List<Object[]> assessmentsWithTypesList){
        try {
            JSONObject currentIncidentJSON = new JSONObject(store.getString(R.string.current_incident_field));
            if(!currentIncidentJSON.has("Id"))
                return MIDDLEWARE_FUNC_FAILED;
            String assessmentsListString = store.getString(R.string.current_assessments_list_field);
            if(assessmentsListString == null || assessmentsListString == ""){
                assessmentsListString = "[]";
            }
            JSONArray assessmentsList = new JSONArray(assessmentsListString);
            for(int i = 0; i < assessmentsList.length(); i++){
                JSONObject assessment = assessmentsList.getJSONObject(i);
                if(assessment.has("incidentid") && !assessment.getString("incidentid").equals(currentIncidentJSON.getString("Id"))){
                    continue;
                }
                if(assessment.has("guid") && assessment.has("assessmenttype")){
                    String assessmenttype = assessment.getString("assessmenttype");
                    Object[] assessmentWithType = new Object[2];
                    assessmentWithType[0] = assessmenttype;
                    assessmentWithType[1] = assessment;
                    assessmentsWithTypesList.add(assessmentWithType);
                }
            }
            return MIDDLEWARE_FUNC_SUCCEED;
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String getInspectorAndIncidentIdsMiddleware(Map<String, String> dict){
        try {
            JSONObject currentInspectorJSON = new JSONObject(store.getString(R.string.current_inspector_field));
            JSONObject currentIncidentJSON = new JSONObject(store.getString(R.string.current_incident_field));
            if(!currentIncidentJSON.has("Id") || !currentInspectorJSON.has("Id"))
                return MIDDLEWARE_FUNC_FAILED;
            dict.put("inspector_id", currentInspectorJSON.getString("Id"));
            dict.put("incident_id", currentIncidentJSON.getString("Id"));
            return MIDDLEWARE_FUNC_SUCCEED;
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String updateAssessmentsListMiddleware(String result){
        try {
            JSONArray newAssessmentsList = new JSONObject(result).getJSONArray("results");
            String assessmentsListString = store.getString(R.string.current_assessments_list_field);
            if(assessmentsListString == null || assessmentsListString == ""){
                assessmentsListString = "[]";
            }
            JSONArray assessmentsList = new JSONArray(assessmentsListString);
            for(int i = 0; i < newAssessmentsList.length(); i++){
                JSONObject newAssessment = newAssessmentsList.getJSONObject(i);
                for(int j = 0; j < assessmentsList.length(); j++){
                    JSONObject assessment = assessmentsList.getJSONObject(j);
                    if(assessment.has("guid") && assessment.getString("guid").equals(newAssessment.getString("id"))){
                        assessmentsList.remove(j);
                    }
                    if(assessment.has("id") && assessment.getString("id").equals(newAssessment.getString("id"))){
                        assessmentsList.remove(j);
                    }
                }
            }
            for (int i = 0; i < assessmentsList.length(); i++)
                newAssessmentsList.put(assessmentsList.getJSONObject(i));
            store.putString(R.string.current_assessments_list_field, newAssessmentsList.toString());
            return MIDDLEWARE_FUNC_SUCCEED;
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }

    public String setLastSyncDateMiddleware(){
        String timestamp = AssessmentBriefModel.sdf.format(Calendar.getInstance().getTime());
        store.putString(R.string.current_last_sync_timestamp_field, timestamp);
        return MIDDLEWARE_FUNC_SUCCEED;
    }

    public String getUsernameAndLastSyncDateMiddleware(Map<String, String> resultMap) {
        String timestamp = store.getString(R.string.current_last_sync_timestamp_field);
        String username = store.getString(R.string.username_field);
        String syncTime;
        if(username == null || username.equals(""))
            return MIDDLEWARE_FUNC_FAILED;
        if(timestamp == null || timestamp.equals(""))
            syncTime = context.getString(R.string.not_synced_yet);
        else
            syncTime = String.format("%s: %s", context.getString(R.string.last_sync_at), timestamp);
        resultMap.put("last_sync", syncTime);
        resultMap.put("username", username);
        return MIDDLEWARE_FUNC_SUCCEED;
    }

    public String resetIndividualEditAssessmentPrefsMiddleware(){
        store.resetIndividualEditAssessmentPrefs();
        return MIDDLEWARE_FUNC_SUCCEED;
    }

    public String resentCacheMiddleware(){
        store.resetAll();
        return MIDDLEWARE_FUNC_SUCCEED;
    }

    public String getInspectorAndIncidentAndAssessmentIdsMiddleware(Map<String, String> dict){
        try {
            JSONObject currentInspectorJSON = new JSONObject(store.getString(R.string.current_inspector_field));
            JSONObject currentIncidentJSON = new JSONObject(store.getString(R.string.current_incident_field));
            String currentAssessmentId = store.getString(R.string.individual_edit_assessment_id_field);
            if(!currentIncidentJSON.has("Id") || !currentInspectorJSON.has("Id") || currentAssessmentId == null || currentAssessmentId == "")
                return MIDDLEWARE_FUNC_FAILED;
            dict.put("inspector_id", currentInspectorJSON.getString("Id"));
            dict.put("incident_id", currentIncidentJSON.getString("Id"));
            dict.put("assessment_id", currentAssessmentId);
            return MIDDLEWARE_FUNC_SUCCEED;
        } catch (JSONException e){
            return MIDDLEWARE_JSON_ERROR;
        }
    }
}