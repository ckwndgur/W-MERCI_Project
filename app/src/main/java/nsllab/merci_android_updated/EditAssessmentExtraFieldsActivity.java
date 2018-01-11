package nsllab.merci_android_updated;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Moallim on 12/13/2017.
 */

public class EditAssessmentExtraFieldsActivity extends AppCompatActivity {
    protected Context context;
    protected FragmentManager fragmentManager;
    protected Intent intent;
    protected TextView extraFieldsEditAssessmentLabel;
    protected String type;
    protected Toolbar editAssessmentExtraFieldsActionBar;
    protected EditAssessmentExtraFieldsClientEditPreferencesFragment clientPreferences;
    protected EditAssessmentExtraFieldsAffectedAddresreferencesFragment affectedaddressPreferences;
    protected EditAssessmentExtraFieldsTemporaryAddressPreferencesFragment temporaryaddressPreferences;
    protected EditAssessmentExtraFieldsOwnerPreferencesFragment ownerPreferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_assessment_extra_fields);
        context = this;
        intent = getIntent();
        type = intent.getStringExtra("TYPE");
        fragmentManager = getSupportFragmentManager();

        editAssessmentExtraFieldsActionBar = (Toolbar) findViewById(R.id.edit_assessment_extra_fields_action_bar);
        setSupportActionBar(editAssessmentExtraFieldsActionBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        extraFieldsEditAssessmentLabel = (TextView) findViewById(R.id.edit_assessment_extra_fields_label);
        extraFieldsEditAssessmentLabel.setText(type);

        EditAssessmentExtraFieldsClientEditPreferencesFragment clientPreferences = new EditAssessmentExtraFieldsClientEditPreferencesFragment();
        EditAssessmentExtraFieldsAffectedAddresreferencesFragment affectedaddressPreferences = new EditAssessmentExtraFieldsAffectedAddresreferencesFragment();
        EditAssessmentExtraFieldsTemporaryAddressPreferencesFragment temporaryaddressPreferences = new EditAssessmentExtraFieldsTemporaryAddressPreferencesFragment();
        EditAssessmentExtraFieldsOwnerPreferencesFragment ownerPreferences = new EditAssessmentExtraFieldsOwnerPreferencesFragment();

        switch (type){
            case EditAssessmentDetailsIndividualFragment.AFFECTED_ADDRESS:
                fragmentManager.beginTransaction().replace(R.id.extra_fields, affectedaddressPreferences, "extra_fields").commit();
                break;
            case EditAssessmentDetailsIndividualFragment.TEMPORARY_ADDRESS:
                fragmentManager.beginTransaction().replace(R.id.extra_fields, temporaryaddressPreferences, "extra_fields").commit();
                break;
            case EditAssessmentDetailsIndividualFragment.OWNER:
                fragmentManager.beginTransaction().replace(R.id.extra_fields, ownerPreferences, "extra_fields").commit();
                break;
            case EditAssessmentDetailsIndividualFragment.CLIENT:
                fragmentManager.beginTransaction().replace(R.id.extra_fields, clientPreferences, "extra_fields").commit();
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
