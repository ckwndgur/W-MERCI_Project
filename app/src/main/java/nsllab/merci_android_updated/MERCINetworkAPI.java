package nsllab.merci_android_updated;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.ParseError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by Moallim on 12/11/2017.
 */

public class MERCINetworkAPI {
    protected RequestQueue queue;
    protected MERCIStore store;
    protected Context context;
    protected static final int DEFAULT_TIME_OUT = 30;
    protected static final String SIGN_IN_GUID = "51ed8b3b-7548-408f-b11a-331811c66baa";
    protected static final String CHECK_CONNECTIVITY_GUID = "bc134cd5-674e-405a-aada-53a38efb463f";
    protected static final String GET_INCIDENT_GUID = "51ed8b3b-7548-408f-b11a-331811c66baa";
    protected static final String GET_INSPECTOR_GUID = "77e658e3-ec76-4dd5-ae7b-031f4eaa0b27";
    protected static final String GET_ISLANDS_GUID = "0bce0bd0-d216-4339-a5a1-8aa3034f252b";
    protected static final String GET_STATES_GUID = "6228abfd-5219-46c0-9404-e1b96112aeae";
    protected static final String GET_AGENCY_TYPE_GUID = "fdc7ba43-8498-47ae-8af2-fe28b8d15125";
    protected static final String GET_CLIENTS_STATUS_GUID = "28b8555f-d671-423d-afe8-c7e71652ba4b";
    protected static final String GET_DAMAGE_LOCATION_GUID = "da35794a-5732-4a0f-86e6-8a535889edbd";
    protected static final String GET_DAMAGE_SEVERITY_GUID = "98c1d434-9e47-4f75-8f67-424d901784c9";
    protected static final String GET_FEDERAL_JURISDICTION_GUID = "5c0d0431-afc9-4fe4-ad10-3a7fcd7a20a9";
    protected static final String GET_INCIDENT_TYPE_GUID = "6664cc8f-4c2c-4135-9608-e676eecdce20";
    protected static final String GET_INSURANCE_TYPE_GUID = "841b72e7-22dd-4604-8d7d-33f9fbe59d6d";
    protected static final String GET_NOTIFICATION_DEPARTMENT_GUID = "3c7f1581-ef7e-4c52-b853-9060d9552d21";
    protected static final String GET_FOLLOW_UP_TYPE_GUID = "8ac644a4-af39-4069-ab02-648463f74d72";
    protected static final String GET_RESIDENCY_TYPE_GUID = "e2b6635e-ba65-45e8-8bc6-0102a2f7964d";
    protected static final String SYNC_ASSESSMENTS_GUID = "a72adaca-60d1-475c-9d14-b4551ce3f70d";
    protected static final String GET_ASSESSMENTS_GUID = "17f6a3bb-cf28-4437-82c8-f9f8b43f0855";

    public MERCINetworkAPI(Context context){
        this.context = context;
        this.queue = Volley.newRequestQueue(context.getApplicationContext());
        this.store = new MERCIStore(context);
    }

    public static JsonObjectRequest getJSON(final String guid, String url, final VolleyCallbackInterface callback){
        return getJSON(guid, url, null, callback);
    }

    public static JsonObjectRequest getJSON(final String guid, String url, final Map dict, final VolleyCallbackInterface callback){
        Log.d("D", "getJSON: " + url);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("D", "onResponse: " + response.toString());
                callback.onRequestSuccess(guid, response, dict);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("D", "onResponse: " + error.getMessage());
                callback.onRequestError(guid, error, dict);
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return req;
    }

    public static JsonObjectRequest postJSON(final String guid, String url, JSONObject json, final VolleyCallbackInterface callback){
        return postJSON(guid, url, json, null, callback);
    }

    public static JsonObjectRequest postJSON(final String guid, String url, JSONObject json, final Map dict, final VolleyCallbackInterface callback){
        Log.d("D", "postJSON: " + url);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("D", "onResponse: " + response.toString());
                callback.onRequestSuccess(guid, response, dict);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("D", "onResponse: " + error.getMessage());
                callback.onRequestError(guid, error, dict);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String data = String.format("{ response: %s }", new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET)).split("\n")[0]);
                    return Response.success(new JSONObject(data),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return req;
    }

    public Boolean isNetworkConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public String getBaseURL(){
        String server_ip = store.getString(R.string.server_ip_field);
        String ip_port = store.getString(R.string.ip_port_field);
        String virtual_directory = store.getString(R.string.virtual_directory_field);
        return "http://" + server_ip + ":" + ip_port + "/" + virtual_directory + "/data/EDAData.aspx";
    }

    public void getSignIn(CharSequence username, VolleyCallbackInterface volleyCallbackInterface){
        String url = String.format("%s?type=login&username=%s", getBaseURL(), username);
        JsonObjectRequest req1 = getJSON(SIGN_IN_GUID, url, volleyCallbackInterface);
        queue.add(req1);
    }

    public DefaultRetryPolicy getRetryPolicy(){
        String timeout_secs = store.getString(R.string.timeout_secs_field);
        return new DefaultRetryPolicy(
                parseInt(timeout_secs, DEFAULT_TIME_OUT),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );
    }

    public void getServerConnectivity(VolleyCallbackInterface volleyCallbackInterface){
        String url = String.format("%s?type=login&username=", getBaseURL());
        JsonObjectRequest req1 = getJSON(CHECK_CONNECTIVITY_GUID, url, volleyCallbackInterface);
        queue.add(req1);
    }

    public void getRelatedData(VolleyCallbackInterface volleyCallbackInterface, Map requestsStatus){
        String baseURL = getBaseURL();
        requestsStatus.put(GET_INCIDENT_GUID, HomeIndexFragment.GET_RELATED_DATA_PENDING);
        requestsStatus.put(GET_INSPECTOR_GUID, HomeIndexFragment.GET_RELATED_DATA_PENDING);
        requestsStatus.put(GET_ISLANDS_GUID, HomeIndexFragment.GET_RELATED_DATA_PENDING);
        requestsStatus.put(GET_STATES_GUID, HomeIndexFragment.GET_RELATED_DATA_PENDING);
        requestsStatus.put(GET_AGENCY_TYPE_GUID, HomeIndexFragment.GET_RELATED_DATA_PENDING);
        requestsStatus.put(GET_CLIENTS_STATUS_GUID, HomeIndexFragment.GET_RELATED_DATA_PENDING);
        requestsStatus.put(GET_DAMAGE_LOCATION_GUID, HomeIndexFragment.GET_RELATED_DATA_PENDING);
        requestsStatus.put(GET_DAMAGE_SEVERITY_GUID, HomeIndexFragment.GET_RELATED_DATA_PENDING);
        requestsStatus.put(GET_FEDERAL_JURISDICTION_GUID, HomeIndexFragment.GET_RELATED_DATA_PENDING);
        requestsStatus.put(GET_INCIDENT_TYPE_GUID, HomeIndexFragment.GET_RELATED_DATA_PENDING);
        requestsStatus.put(GET_INSURANCE_TYPE_GUID, HomeIndexFragment.GET_RELATED_DATA_PENDING);
        requestsStatus.put(GET_NOTIFICATION_DEPARTMENT_GUID, HomeIndexFragment.GET_RELATED_DATA_PENDING);
        requestsStatus.put(GET_FOLLOW_UP_TYPE_GUID, HomeIndexFragment.GET_RELATED_DATA_PENDING);
        requestsStatus.put(GET_RESIDENCY_TYPE_GUID, HomeIndexFragment.GET_RELATED_DATA_PENDING);
        JsonObjectRequest req01 = getJSON(GET_INCIDENT_GUID, String.format("%s?type=incident", baseURL), volleyCallbackInterface);
        JsonObjectRequest req02 = getJSON(GET_INSPECTOR_GUID, String.format("%s?type=inspector", baseURL), volleyCallbackInterface);
        JsonObjectRequest req03 = getJSON(GET_ISLANDS_GUID, String.format("%s?type=islands", baseURL), volleyCallbackInterface);
        JsonObjectRequest req04 = getJSON(GET_STATES_GUID, String.format("%s?type=states", baseURL), volleyCallbackInterface);
        JsonObjectRequest req05 = getJSON(GET_AGENCY_TYPE_GUID, String.format("%s?type=agencytype", baseURL), volleyCallbackInterface);
        JsonObjectRequest req06 = getJSON(GET_CLIENTS_STATUS_GUID, String.format("%s?type=clientstatus", baseURL), volleyCallbackInterface);
        JsonObjectRequest req07 = getJSON(GET_DAMAGE_LOCATION_GUID, String.format("%s?type=damagelocation", baseURL), volleyCallbackInterface);
        JsonObjectRequest req08 = getJSON(GET_DAMAGE_SEVERITY_GUID, String.format("%s?type=damageseverity", baseURL), volleyCallbackInterface);
        JsonObjectRequest req09 = getJSON(GET_FEDERAL_JURISDICTION_GUID, String.format("%s?type=federaljurisdiction", baseURL), volleyCallbackInterface);
        JsonObjectRequest req10 = getJSON(GET_INCIDENT_TYPE_GUID, String.format("%s?type=incidenttype", baseURL), volleyCallbackInterface);
        JsonObjectRequest req11 = getJSON(GET_INSURANCE_TYPE_GUID, String.format("%s?type=insurancetype", baseURL), volleyCallbackInterface);
        JsonObjectRequest req12 = getJSON(GET_NOTIFICATION_DEPARTMENT_GUID, String.format("%s?type=notificationdepartment", baseURL), volleyCallbackInterface);
        JsonObjectRequest req13 = getJSON(GET_FOLLOW_UP_TYPE_GUID, String.format("%s?type=followuptype", baseURL), volleyCallbackInterface);
        JsonObjectRequest req14 = getJSON(GET_RESIDENCY_TYPE_GUID, String.format("%s?type=residencytype", baseURL), volleyCallbackInterface);
        queue.add(req01);
        queue.add(req02);
        queue.add(req03);
        queue.add(req04);
        queue.add(req05);
        queue.add(req06);
        queue.add(req07);
        queue.add(req08);
        queue.add(req09);
        queue.add(req10);
        queue.add(req11);
        queue.add(req12);
        queue.add(req13);
        queue.add(req14);
    }

    public void syncAssessments(List<Object[]> assessmentsWithTypesList, VolleyCallbackInterface volleyCallbackInterface){
        String baseURL = getBaseURL();
        String URLIndividual = String.format("%s?type=individual", baseURL);
        String URLBusiness = String.format("%s?type=business", baseURL);
        String URLInfrastructure = String.format("%s?type=infrastructure", baseURL);
        for (int i = 0; i <assessmentsWithTypesList.size(); i++){
            Object[] assessmentWithType = assessmentsWithTypesList.get(i);
            switch (assessmentWithType[0].toString()){
                case AssessmentBriefModel.Individual:
                    JsonObjectRequest req1 = postJSON(SYNC_ASSESSMENTS_GUID, URLIndividual, (JSONObject)assessmentWithType[1], volleyCallbackInterface);
                    queue.add(req1);
                    break;
                case AssessmentBriefModel.Business:
                    JsonObjectRequest req2 = postJSON(SYNC_ASSESSMENTS_GUID, URLBusiness, (JSONObject)assessmentWithType[1], volleyCallbackInterface);
                    queue.add(req2);
                    break;
                case AssessmentBriefModel.Infrastructure:
                    JsonObjectRequest req3 = postJSON(SYNC_ASSESSMENTS_GUID, URLInfrastructure, (JSONObject)assessmentWithType[1], volleyCallbackInterface);
                    queue.add(req3);
                    break;
            }
        }
    }

    public void getAssessments(String inspector_id, String incident_id, VolleyCallbackInterface volleyCallbackInterface){
        String URLIndividual = String.format("%s?type=assessment&inspector_id=%s&incident_id=%s", getBaseURL(), inspector_id, incident_id);
        JsonObjectRequest req1 = getJSON(GET_ASSESSMENTS_GUID, URLIndividual, volleyCallbackInterface);
        queue.add(req1);
    }

    public static int parseInt(String str, int _default){
        int number = 0;
        try {
            number = str != null? Integer.parseInt(str): _default;
        } catch (NumberFormatException e) {
            number = _default;
        } finally {
            return number;
        }
    }
}
