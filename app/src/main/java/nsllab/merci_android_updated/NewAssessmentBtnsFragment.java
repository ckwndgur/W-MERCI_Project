package nsllab.merci_android_updated;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Moallim on 12/12/2017.
 */

public class NewAssessmentBtnsFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    protected Context context;
    protected HomeActivity activity;
    protected MERCINetworkAPI api;
    protected Button individualAssessment;
    protected Button bussinessAssessment;
    protected Button infrastructureAssessment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_assessment_btns_fragment, container, false);
        context = getActivity();
        activity = (HomeActivity) getActivity();
        api = new MERCINetworkAPI(context);

        individualAssessment = view.findViewById(R.id.individual_assessment_btn);
        bussinessAssessment = view.findViewById(R.id.business_assessment_btn);
        infrastructureAssessment = view.findViewById(R.id.infrastructure_assessment_btn);
        individualAssessment.setOnClickListener(this);
        bussinessAssessment.setOnClickListener(this);
        infrastructureAssessment.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.individual_assessment_btn:
                getDialog().dismiss();
                Intent intent = new Intent(activity, EditAssessmentActivity.class);
                intent.putExtra("TYPE", AssessmentBriefModel.Individual);
                startActivity(intent);
                break;
            case R.id.business_assessment_btn:
            case R.id.infrastructure_assessment_btn:
                Toast.makeText(context, getString(R.string.not_supported_yet), Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
                break;
        }
    }
}
