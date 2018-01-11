package nsllab.merci_android_updated;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Moallim on 12/13/2017.
 */

public class EditAssessmentDetailsIndividualFragment extends Fragment implements View.OnClickListener {
    protected Context context;
    protected FragmentManager fragmentManager;
    protected EditAssessmentDetailsMediaBtnsFragment editAssessmentDetailsMediaBtnsFragment;
    protected EditAssessmentClientStatusAndResidencyTypePerferencesFragment editAssessmentClientStatusAndResidencyTypePreferencesFragment;
    protected EditAssessmentTheRestPreferencesFragment editAssessmentTheRestPreferencesFragment;
    protected Button mediaBtn;
    protected Button clientBtn;
    protected Button affectedaddressBtn;
    protected Button temporaryaddressBtn;
    protected Button ownerBtn;
    protected static final String CLIENT = "CLIENT";
    protected static final String AFFECTED_ADDRESS = "AFFECTED_ADDRESS";
    protected static final String TEMPORARY_ADDRESS = "TEMPORARY_ADDRESS";
    protected static final String OWNER = "OWNER";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_assessment_details_fragment, container, false);
        context = getActivity();
        fragmentManager = getFragmentManager();
        editAssessmentDetailsMediaBtnsFragment = new EditAssessmentDetailsMediaBtnsFragment();

        mediaBtn = view.findViewById(R.id.associate_media_btn);
        clientBtn = view.findViewById(R.id.client_btn);
        affectedaddressBtn = view.findViewById(R.id.affectedaddress_btn);
        temporaryaddressBtn = view.findViewById(R.id.temporaryaddress_btn);
        ownerBtn = view.findViewById(R.id.owner_btn);
        mediaBtn.setOnClickListener(this);
        clientBtn.setOnClickListener(this);
        affectedaddressBtn.setOnClickListener(this);
        temporaryaddressBtn.setOnClickListener(this);
        ownerBtn.setOnClickListener(this);

        editAssessmentClientStatusAndResidencyTypePreferencesFragment = new EditAssessmentClientStatusAndResidencyTypePerferencesFragment();
        editAssessmentTheRestPreferencesFragment = new EditAssessmentTheRestPreferencesFragment();
        fragmentManager.beginTransaction().replace(R.id.edit_assessment_client_status_and_residency_type_preferences_fragment, editAssessmentClientStatusAndResidencyTypePreferencesFragment, "edit_assessment_client_status_and_residency_type_preferences_fragment").commit();
        fragmentManager.beginTransaction().replace(R.id.edit_assessment_the_rest_preferences_fragment, editAssessmentTheRestPreferencesFragment, "edit_assessment_the_rest_preferences_fragment").commit();

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, EditAssessmentExtraFieldsActivity.class);
        switch (v.getId()){
            case R.id.client_btn:
                intent.putExtra("TYPE", CLIENT);
                startActivity(intent);
                break;
            case R.id.affectedaddress_btn:
                intent.putExtra("TYPE", AFFECTED_ADDRESS);
                startActivity(intent);
                break;
            case R.id.temporaryaddress_btn:
                intent.putExtra("TYPE", TEMPORARY_ADDRESS);
                startActivity(intent);
                break;
            case R.id.owner_btn:
                intent.putExtra("TYPE", OWNER);
                startActivity(intent);
                break;
            case R.id.associate_media_btn:
                editAssessmentDetailsMediaBtnsFragment.show(fragmentManager, editAssessmentDetailsMediaBtnsFragment.getTag());
                break;
        }
    }
}
