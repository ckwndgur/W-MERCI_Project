package nsllab.merci_android_updated;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.error.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Moallim on 12/12/2017.
 */

public class HomeIndexFragment extends Fragment implements View.OnClickListener, VolleyCallbackInterface, UIRunnableInterface {
    protected HomeActivity activity;
    protected Context context;
    protected ProgressDialog progressDialog;
    protected NewAssessmentBtnsFragment newAssessmentBtnsFragment;
    protected FragmentManager fragmentManager;
    protected RelativeLayout incidentSelectBtn;
    protected FloatingActionButton fab;
    protected HomeIndexAssessmentListFragment homeIndexAssessmentListFragment;
    protected MERCINetworkAPI api;
    protected MERCIMiddleware middleware;
    protected List<Map<String, String>> incidentsList;
    protected Map<String, String> requestsStatus;
    protected TextView selectedIncidentLabel;
    protected static final String GET_RELATED_DATA_PENDING = "GET_RELATED_DATA_PENDING";
    protected static final String GET_RELATED_DATA_NETWORK_ERROR = "GET_RELATED_DATA_NETWORK_ERROR";
    protected static final String GET_RELATED_DATA_JSON_PARSE_ERROR = "GET_RELATED_DATA_JSON_PARSE_ERROR";
    protected static final String GET_RELATED_DATA_DONE = "GET_RELATED_DATA_DONE";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.home_index_fragment, container, false);

        context = getActivity();
        activity = (HomeActivity) context;
        api = new MERCINetworkAPI(context);
        middleware = new MERCIMiddleware(context);
        progressDialog = new ProgressDialog(context);
        newAssessmentBtnsFragment = new NewAssessmentBtnsFragment();
        fragmentManager = getFragmentManager();
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        incidentsList = new ArrayList();
        requestsStatus = new HashMap();
        homeIndexAssessmentListFragment = new HomeIndexAssessmentListFragment();
        incidentSelectBtn = view.findViewById(R.id.select_incident);
        incidentSelectBtn.setOnClickListener(this);
        selectedIncidentLabel = view.findViewById(R.id.selected_incident_label);
        fragmentManager.beginTransaction().replace(R.id.assessment_list_fragment, homeIndexAssessmentListFragment, "assessment_list_fragment").commit();
        activity.setFragmentTitle(getString(R.string.app_name));

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                newAssessmentBtnsFragment.show(fragmentManager, newAssessmentBtnsFragment.getTag());
                break;
            case R.id.select_incident:
                requestRelatedData();
                break;
        }
    }

    @Override
    public void onRequestSuccess(String GUID, JSONObject result, Map dict) {
        switch (GUID){
            case MERCINetworkAPI.GET_INCIDENT_GUID:
                incidentsList = new ArrayList();
                switch (middleware.relatedDataIncidentMiddleware(result, incidentsList)){
                    case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_DONE);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_NETWORK_ERROR);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                        requestsStatus.put(GUID, GET_RELATED_DATA_JSON_PARSE_ERROR);
                        break;
                }
                break;
            case MERCINetworkAPI.GET_INSPECTOR_GUID:
                switch (middleware.relatedDataInspectorMiddleware(result)){
                    case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_DONE);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_NETWORK_ERROR);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                        requestsStatus.put(GUID, GET_RELATED_DATA_JSON_PARSE_ERROR);
                        break;
                }
                break;
            case MERCINetworkAPI.GET_ISLANDS_GUID:
                switch (middleware.relatedDataIslandMiddleware(result)){
                    case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_DONE);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_NETWORK_ERROR);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                        requestsStatus.put(GUID, GET_RELATED_DATA_JSON_PARSE_ERROR);
                        break;
                }
                break;
            case MERCINetworkAPI.GET_STATES_GUID:
                switch (middleware.relatedDataStatesMiddleware(result)){
                    case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_DONE);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_NETWORK_ERROR);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                        requestsStatus.put(GUID, GET_RELATED_DATA_JSON_PARSE_ERROR);
                        break;
                }
                break;
            case MERCINetworkAPI.GET_AGENCY_TYPE_GUID:
                switch (middleware.relatedDataAgencyTypeMiddleware(result)){
                    case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_DONE);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_NETWORK_ERROR);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                        requestsStatus.put(GUID, GET_RELATED_DATA_JSON_PARSE_ERROR);
                        break;
                }
                break;
            case MERCINetworkAPI.GET_CLIENTS_STATUS_GUID:
                switch (middleware.relatedDataClientStatusMiddleware(result)){
                    case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_DONE);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_NETWORK_ERROR);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                        requestsStatus.put(GUID, GET_RELATED_DATA_JSON_PARSE_ERROR);
                        break;
                }
                break;
            case MERCINetworkAPI.GET_DAMAGE_LOCATION_GUID:
                switch (middleware.relatedDataDamageLocationMiddleware(result)){
                    case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_DONE);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_NETWORK_ERROR);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                        requestsStatus.put(GUID, GET_RELATED_DATA_JSON_PARSE_ERROR);
                        break;
                }
                break;
            case MERCINetworkAPI.GET_DAMAGE_SEVERITY_GUID:
                switch (middleware.relatedDataDamageSeverityMiddleware(result)){
                    case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_DONE);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_NETWORK_ERROR);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                        requestsStatus.put(GUID, GET_RELATED_DATA_JSON_PARSE_ERROR);
                        break;
                }
                break;
            case MERCINetworkAPI.GET_FEDERAL_JURISDICTION_GUID:
                switch (middleware.relatedDataFederalJurisdictionMiddleware(result)){
                    case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_DONE);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_NETWORK_ERROR);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                        requestsStatus.put(GUID, GET_RELATED_DATA_JSON_PARSE_ERROR);
                        break;
                }
                break;
            case MERCINetworkAPI.GET_INCIDENT_TYPE_GUID:
                switch (middleware.relatedDataIncidentTypeMiddleware(result)){
                    case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_DONE);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_NETWORK_ERROR);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                        requestsStatus.put(GUID, GET_RELATED_DATA_JSON_PARSE_ERROR);
                        break;
                }
                break;
            case MERCINetworkAPI.GET_INSURANCE_TYPE_GUID:
                switch (middleware.relatedDataInsuranceTypeMiddleware(result)){
                    case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_DONE);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_NETWORK_ERROR);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                        requestsStatus.put(GUID, GET_RELATED_DATA_JSON_PARSE_ERROR);
                        break;
                }
                break;
            case MERCINetworkAPI.GET_NOTIFICATION_DEPARTMENT_GUID:
                switch (middleware.relatedDataNotificationDepartmentMiddleware(result)){
                    case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_DONE);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_NETWORK_ERROR);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                        requestsStatus.put(GUID, GET_RELATED_DATA_JSON_PARSE_ERROR);
                        break;
                }
                break;
            case MERCINetworkAPI.GET_FOLLOW_UP_TYPE_GUID:
                switch (middleware.relatedDataFollowupTypeMiddleware(result)){
                    case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_DONE);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_NETWORK_ERROR);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                        requestsStatus.put(GUID, GET_RELATED_DATA_JSON_PARSE_ERROR);
                        break;
                }
                break;
            case MERCINetworkAPI.GET_RESIDENCY_TYPE_GUID:
                switch (middleware.relatedDataResidencyTypeMiddleware(result)){
                    case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_DONE);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                        requestsStatus.put(GUID, GET_RELATED_DATA_NETWORK_ERROR);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                        requestsStatus.put(GUID, GET_RELATED_DATA_JSON_PARSE_ERROR);
                        break;
                }
                break;
        }
        switch (checkRequestsStatus(requestsStatus)){
            case GET_RELATED_DATA_PENDING:
                break;
            case GET_RELATED_DATA_DONE:
                MERCIEvents.handler(this, MERCIEvents.ON_LOAD_RELATED_DATA_SUCCEED);
                break;
            case GET_RELATED_DATA_NETWORK_ERROR:
                MERCIEvents.handler(this, MERCIEvents.ON_LOAD_RELATED_DATA_FAILED);
                break;
            case GET_RELATED_DATA_JSON_PARSE_ERROR:
                MERCIEvents.handler(this, MERCIEvents.ON_JSON_ERROR);
                break;
        }
    }

    @Override
    public void onRequestError(String GUID, VolleyError result, Map dict) {
        requestsStatus.put(GUID, GET_RELATED_DATA_NETWORK_ERROR);
        MERCIEvents.handler(this, MERCIEvents.ON_LOAD_RELATED_DATA_FAILED);
    }

    @Override
    public void onRunnable(String id, String extras) {
        if(progressDialog.isShowing())
            progressDialog.dismiss();
        switch (id){
            case MERCIEvents.ON_LOAD_RELATED_DATA_SUCCEED:
                activity.openContextMenu(incidentSelectBtn);
                break;
            case MERCIEvents.ON_JSON_ERROR:
            case MERCIEvents.ON_LOAD_RELATED_DATA_FAILED:
                Toast.makeText(activity, R.string.loading_incidents_failed, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        for (int i = 0; i < incidentsList.size(); i++)
            menu.add(incidentsList.get(i).get("Name"));
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Map<String, String> incidentMap = new HashMap<>();
        switch (middleware.setCurrentIncidentMiddleware(incidentsList, item.getTitle(), incidentMap)){
            case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                selectedIncidentLabel.setText(incidentMap.get("Name"));
                homeIndexAssessmentListFragment.refreshList();
                break;
            case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
            case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                Toast.makeText(activity, R.string.could_not_select_incident, Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        activity.toggleSyncBtn(false);
        unregisterForContextMenu(incidentSelectBtn);
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.toggleSyncBtn(true);
        Map<String, String> incidentMap = new HashMap();
        switch (middleware.getCurrentIncidentMiddleware(incidentMap)){
            case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                selectedIncidentLabel.setText(incidentMap.get("Name"));
                break;
            case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
            case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                selectedIncidentLabel.setText(getString(R.string.no_incident_selected));
                break;
        }
        registerForContextMenu(incidentSelectBtn);
    }


    public void requestRelatedData(){
        if(!api.isNetworkConnected()){
            Toast.makeText(context, getString(R.string.network_connection_error), Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage(getString(R.string.loading_incidents));
        progressDialog.show();
        api.getRelatedData(this, requestsStatus);
    }

    protected String checkRequestsStatus(Map map){
        Collection values = map.values();
        if (values.contains(GET_RELATED_DATA_PENDING))
            return GET_RELATED_DATA_PENDING;
        else if (values.contains(GET_RELATED_DATA_NETWORK_ERROR))
            return GET_RELATED_DATA_NETWORK_ERROR;
        else if(values.contains(GET_RELATED_DATA_JSON_PARSE_ERROR))
            return GET_RELATED_DATA_JSON_PARSE_ERROR;
        else
            return GET_RELATED_DATA_DONE;
    }

    public void refreshList(){
        homeIndexAssessmentListFragment.refreshList();
    }
}
