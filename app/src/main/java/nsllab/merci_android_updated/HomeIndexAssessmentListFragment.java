package nsllab.merci_android_updated;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moallim on 12/13/2017.
 */

public class HomeIndexAssessmentListFragment extends Fragment implements ItemClickRecyclerViewInterface {
    protected Context context;
    protected FragmentManager fragmentManager;
    protected RecyclerView recyclerView;
    protected RecyclerView.Adapter adapter;
    protected RecyclerView.LayoutManager layoutManager;
    protected MERCIMiddleware middleware;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_index_assessment_list_fragment, container, false);
        context = getActivity();
        fragmentManager = getFragmentManager();
        middleware = new MERCIMiddleware(context);
        recyclerView = view.findViewById(R.id.assessment_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    protected List<AssessmentBriefModel> sampleData(){
        List<AssessmentBriefModel> list = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            list.add(new AssessmentBriefModel("", null, "hey", "details", true, true, true, "", ""));
        }
        return list;
    }

    public List<AssessmentBriefModel> getData(){
        List<AssessmentBriefModel> assessmentBriefModelList = new ArrayList<>();
        switch (middleware.getAssessmentsMiddleware(assessmentBriefModelList)){
            case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                break;
            case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
            case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                Toast.makeText(context, R.string.could_not_refresh_assessments_list, Toast.LENGTH_SHORT).show();
                break;
        }
        return assessmentBriefModelList;
    }

    @Override
    public void onItemClickRecyclerView(AssessmentBriefModel model) {
        switch (middleware.retrieveAssessmentMiddleware(model)){
            case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                Intent intent = new Intent(context, EditAssessmentActivity.class);
                intent.putExtra("TYPE", model.type);
                startActivity(intent);
                break;
            case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
            case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    public void refreshList(){
        adapter = new AssessmentBriefAdapter(getData(), this);
        recyclerView.setAdapter(adapter);
    }
}
