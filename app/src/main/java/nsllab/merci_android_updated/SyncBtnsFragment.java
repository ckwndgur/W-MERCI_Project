package nsllab.merci_android_updated;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.android.volley.error.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Moallim on 12/12/2017.
 */

public class SyncBtnsFragment extends BottomSheetDialogFragment implements View.OnClickListener, VolleyCallbackInterface, UIRunnableInterface {
    protected Context context;
    protected HomeActivity activity;
    protected MERCINetworkAPI api;
    protected ProgressDialog progressDialog;
    protected MERCIMiddleware middleware;
    protected Button syncAssessmentsBtn;
    protected Button syncAllBtn;
    protected List<Object[]> assessmentsWithTypesList = new ArrayList<>();
    protected int[] assessmentsSyncedCount = new int[]{0, 0};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sync_btns_fragment, container, false);
        context = getActivity();
        activity = (HomeActivity) getActivity();
        progressDialog = new ProgressDialog(context);
        api = new MERCINetworkAPI(context);
        middleware = new MERCIMiddleware(context);
        syncAssessmentsBtn = view.findViewById(R.id.sync_assessments_btn);
        syncAssessmentsBtn.setOnClickListener(this);
        syncAllBtn = view.findViewById(R.id.sync_all_btn);
        syncAllBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sync_assessments_btn:
                if(!api.isNetworkConnected()){
                    Toast.makeText(context, getString(R.string.network_connection_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                assessmentsWithTypesList = new ArrayList<>();
                assessmentsSyncedCount = new int[]{0, 0};
                switch (middleware.getAssessmentsToSyncMiddleware(assessmentsWithTypesList)){
                    case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                        progressDialog.setMessage(String.format("%s %d/%d", getString(R.string.syncing_assessments), assessmentsSyncedCount[0], assessmentsWithTypesList.size()));
                        progressDialog.show();
                        if(assessmentsWithTypesList.size() < 1) {
                            MERCIEvents.handler(this, MERCIEvents.ON_SYNC_ASSESSMENTS_SUCCEED);
                            return;
                        }
                        api.syncAssessments(assessmentsWithTypesList, this);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                        Toast.makeText(context, R.string.no_incident_selected, Toast.LENGTH_SHORT).show();
                        getDialog().dismiss();
                        break;
                    case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                        MERCIEvents.handler(this, MERCIEvents.ON_JSON_ERROR);
                        break;
                }
                break;
            case R.id.sync_all_btn:
                break;
        }
    }

    @Override
    public void onRequestSuccess(String GUID, JSONObject result, Map dict) {
        switch (GUID){
            case MERCINetworkAPI.SYNC_ASSESSMENTS_GUID:
                assessmentsSyncedCount[0]++;
                if(assessmentsSyncedCount[0] + assessmentsSyncedCount[1] < assessmentsWithTypesList.size())
                    progressDialog.setMessage(String.format("%s %s/%s", getString(R.string.syncing_assessments), assessmentsSyncedCount[0], assessmentsWithTypesList.size()));
                else
                    MERCIEvents.handler(this, MERCIEvents.ON_SYNC_ASSESSMENTS_SUCCEED);
                break;
            case MERCINetworkAPI.GET_ASSESSMENTS_GUID:
                MERCIEvents.handler(this, MERCIEvents.ON_REFRESH_ASSESSMENTS_SUCCEED, MERCIEvents._200, result.toString());
                break;
        }
    }

    @Override
    public void onRequestError(String GUID, VolleyError result, Map dict) {
        switch (GUID){
            case MERCINetworkAPI.SYNC_ASSESSMENTS_GUID:
                assessmentsSyncedCount[1]++;
                if(assessmentsSyncedCount[0] + assessmentsSyncedCount[1] >= assessmentsWithTypesList.size())
                    MERCIEvents.handler(this, MERCIEvents.ON_SYNC_ASSESSMENTS_FAILED);
                break;
            case MERCINetworkAPI.GET_ASSESSMENTS_GUID:
                MERCIEvents.handler(this, MERCIEvents.ON_REFRESH_ASSESSMENTS_FAILED);
                break;
        }
    }

    @Override
    public void onRunnable(String id, String extras) {
        switch (id){
            case MERCIEvents.ON_SYNC_ASSESSMENTS_SUCCEED:
                Toast.makeText(context, R.string.syncing_assessments_succeeded, Toast.LENGTH_SHORT).show();
                refreshAssessmentsList();
                break;
            case MERCIEvents.ON_SYNC_ASSESSMENTS_FAILED:
                Toast.makeText(context, String.format("%s %d/%d", getString(R.string.syncing_assessment_failed), assessmentsSyncedCount[0], assessmentsWithTypesList.size()), Toast.LENGTH_SHORT).show();
                refreshAssessmentsList();
                break;
            case MERCIEvents.ON_JSON_ERROR:
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(context, R.string.json_parsing_error, Toast.LENGTH_SHORT).show();
                break;
            case MERCIEvents.ON_REFRESH_ASSESSMENTS_SUCCEED:
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
                switch (middleware.updateAssessmentsListMiddleware(extras)){
                    case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                        activity.refreshList();
                        middleware.setLastSyncDateMiddleware();
                        activity.updateUsernameAndLastSyncLabels();
                        break;
                    case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                        MERCIEvents.handler(this, MERCIEvents.ON_JSON_ERROR);
                        break;
                }
                getDialog().dismiss();
                break;
            case MERCIEvents.ON_REFRESH_ASSESSMENTS_FAILED:
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(context, R.string.retrieving_assessments_failed, Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
                break;
        }
    }

    public void refreshAssessmentsList(){
        Map<String, String> dict = new HashMap();
        switch (middleware.getInspectorAndIncidentIdsMiddleware(dict)){
            case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                progressDialog.setMessage(getString(R.string.retrieving_assessments));
                api.getAssessments(dict.get("inspector_id"), dict.get("incident_id"), this);
                break;
            case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                MERCIEvents.handler(this, MERCIEvents.ON_REFRESH_ASSESSMENTS_FAILED);
                break;
            case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                MERCIEvents.handler(this, MERCIEvents.ON_JSON_ERROR);
                break;
        }
    }
}
