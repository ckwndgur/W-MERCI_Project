package nsllab.merci_android_updated;

import android.os.Handler;

/**
 * Created by Moallim on 12/11/2017.
 */

public class MERCIEvents {
    protected static final int _200 = 200;
    protected static final String ON_INITIATED = "ON_INITIATED";
    protected static final String ON_AUTHENTICATE_SUCCEED = "ON_AUTHENTICATE_SUCCEED";
    protected static final String ON_AUTHENTICATE_FAILED = "ON_AUTHENTICATE_FAILED";
    protected static final String ON_JSON_ERROR = "ON_JSON_ERROR";
    protected static final String ON_NETWORK_ERROR = "ON_NETWORK_ERROR";
    protected static final String ON_NETWORK_PARAMETERS_CORRECT = "ON_NETWORK_PARAMETERS_CORRECT";
    protected static final String ON_NETWORK_PARAMETERS_ERROR = "ON_NETWORK_PARAMETERS_ERROR";
    protected static final String ON_LOAD_RELATED_DATA_SUCCEED = "ON_LOAD_RELATED_DATA_SUCCEED";
    protected static final String ON_LOAD_RELATED_DATA_FAILED = "ON_LOAD_RELATED_DATA_FAILED";
    protected static final String ON_SYNC_ASSESSMENTS_SUCCEED = "ON_SYNC_ASSESSMENTS_SUCCEED";
    protected static final String ON_SYNC_ASSESSMENTS_FAILED = "ON_SYNC_ASSESSMENTS_FAILED";
    protected static final String ON_SYNC_ASSESSMENTS_JSON_ERROR = "ON_SYNC_ASSESSMENTS_JSON_ERROR";
    protected static final String ON_REFRESH_ASSESSMENTS_SUCCEED = "ON_REFRESH_ASSESSMENTS_SUCCEED";
    protected static final String ON_REFRESH_ASSESSMENTS_FAILED = "ON_REFRESH_ASSESSMENTS_FAILED";

    public static void handler(UIRunnableInterface runnable, String id){
        handler(runnable, id, _200, null);
    }

    public static void handler(final UIRunnableInterface runnable, final String id, int interval, final String extras){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runnable.onRunnable(id, extras);
            }
        }, interval);
    }
}
