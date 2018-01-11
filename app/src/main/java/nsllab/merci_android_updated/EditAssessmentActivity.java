package nsllab.merci_android_updated;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Moallim on 12/12/2017.
 */

public class EditAssessmentActivity extends AppCompatActivity implements View.OnClickListener {
    protected Context context;
    protected Intent intent;
    protected TabLayout tabLayout;
    protected ViewPager viewPager;
    protected PagerAdapter pagerAdapter;
    protected FragmentManager fragmentManager;
    protected TextView editAssessmentLabel;
    protected Toolbar editAssessmentActionBar;
    protected RelativeLayout saveAssessmentBtn;
    protected String fragmentType;
    protected MERCIMiddleware middleware;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_assessment_activity);

        context = this;
        intent = getIntent();
        fragmentManager = getSupportFragmentManager();
        middleware = new MERCIMiddleware(context);
        middleware.updateGUIDAndTimestampMiddleware();

        editAssessmentActionBar = (Toolbar) findViewById(R.id.edit_assessment_action_bar);
        setSupportActionBar(editAssessmentActionBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentType = intent.getStringExtra("TYPE");

        saveAssessmentBtn = (RelativeLayout) findViewById(R.id.save_individual_assessment_btn);
        saveAssessmentBtn.setOnClickListener(this);
        editAssessmentLabel = (TextView) findViewById(R.id.edit_assessment_label);

        tabLayout = (TabLayout) findViewById(R.id.Assessment_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.assessment_view_pager);
        pagerAdapter = new EditAssessmentScreenSlidePagerAdapter(fragmentManager, context, fragmentType);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setFragmentTitle();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_individual_assessment_btn:
                switch (middleware.storeAssessmentMiddleware(fragmentType)){
                    case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                        Toast.makeText(context, R.string.assessment_has_been_cached, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        break;
                    case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                    case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                        break;
                }
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
            middleware.resetIndividualEditAssessmentPrefsMiddleware();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    public void setFragmentTitle(){
        switch (fragmentType){
            case AssessmentBriefModel.Business:
                editAssessmentLabel.setText(R.string.business_assessment);
                break;
            case AssessmentBriefModel.Infrastructure:
                editAssessmentLabel.setText(R.string.infrastructure_assessment);
                break;
            case AssessmentBriefModel.Individual:
            default:
                editAssessmentLabel.setText(R.string.individual_assessment);
                break;
        }
    }
}
