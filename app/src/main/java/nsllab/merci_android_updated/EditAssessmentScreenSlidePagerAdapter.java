package nsllab.merci_android_updated;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Moallim on 12/13/2017.
 */

public class EditAssessmentScreenSlidePagerAdapter extends FragmentPagerAdapter {
    protected String type;
    protected Context context;
    private final int NUM_PAGES = 2;
    public EditAssessmentScreenSlidePagerAdapter(FragmentManager fm, Context context, String type) {
        super(fm);
        this.type = type;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                switch (type){
                    case AssessmentBriefModel.Infrastructure:
                    case AssessmentBriefModel.Business:
                    case AssessmentBriefModel.Individual:
                    default:
                        return new EditAssessmentDetailsIndividualFragment();
                }
            case 1:
                return new EditAssessmentMapFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getString(R.string.details_label);
            case 1:
                return context.getString(R.string.map_label);
            default:
                return null;
        }
    }
}
