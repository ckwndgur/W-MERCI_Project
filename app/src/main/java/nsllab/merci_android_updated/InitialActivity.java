package nsllab.merci_android_updated;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Moallim on 12/11/2017.
 */

public class InitialActivity extends AppCompatActivity implements UIRunnableInterface {
    protected Context context;
    protected MERCIMiddleware middleware;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_activity);

        context = this;
        middleware = new MERCIMiddleware(context);
        MERCIEvents.handler(this, MERCIEvents.ON_INITIATED);
        //middleware.resentCacheMiddleware();
    }

    @Override
    public void onRunnable(String id, String extras) {
        switch (id){
            case MERCIEvents.ON_INITIATED:
                switch (middleware.initiateMiddleware()){
                    case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                        Intent intent1 = new Intent(InitialActivity.this, HomeActivity.class);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        startActivity(intent1);
                        finish();
                        break;
                    case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                        Intent intent2 = new Intent(InitialActivity.this, LangActivity.class);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        startActivity(intent2);
                        finish();
                        break;
                }
                break;
        }
    }
}
