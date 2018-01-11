package nsllab.merci_android_updated;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Moallim on 12/11/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, VolleyCallbackInterface, UIRunnableInterface {
    protected Context context;
    protected ProgressDialog progressDialog;
    protected MERCINetworkAPI api;
    protected MERCIMiddleware middleware;
    protected TextView username;
    protected Button loginBtn;
    protected RelativeLayout networkSettingsBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        context = this;
        api = new MERCINetworkAPI(context);
        middleware = new MERCIMiddleware(context);

        username = (TextView) findViewById(R.id.username);
        progressDialog = new ProgressDialog(context);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        networkSettingsBtn = (RelativeLayout) findViewById(R.id.networkSettingsBtn);
        networkSettingsBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginBtn:
                if(!api.isNetworkConnected()){
                    Toast.makeText(context, getString(R.string.network_connection_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                api.getSignIn(username.getText(), this);
                progressDialog.setMessage(getString(R.string.signing_in));
                progressDialog.show();
                break;
            case R.id.networkSettingsBtn:
                Intent intent = new Intent(LoginActivity.this, NetworkSettingsActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRequestSuccess(String GUID, JSONObject result, Map dict) {
        switch (GUID){
            case MERCINetworkAPI.SIGN_IN_GUID:
                switch(middleware.signInMiddleware(result, username.getText().toString())){
                    case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                        MERCIEvents.handler(this, MERCIEvents.ON_AUTHENTICATE_SUCCEED);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                        MERCIEvents.handler(this, MERCIEvents.ON_AUTHENTICATE_FAILED);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                        MERCIEvents.handler(this, MERCIEvents.ON_JSON_ERROR);
                        break;
                }
                break;
        }
    }

    @Override
    public void onRequestError(String GUID, VolleyError result, Map dict) {
        switch (GUID){
            case MERCINetworkAPI.SIGN_IN_GUID:
                MERCIEvents.handler(this, MERCIEvents.ON_AUTHENTICATE_FAILED);
                break;
        }
    }

    @Override
    public void onRunnable(String id, String extras) {
        if(progressDialog.isShowing())
            progressDialog.dismiss();
        switch (id){
            case MERCIEvents.ON_AUTHENTICATE_SUCCEED:
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case MERCIEvents.ON_AUTHENTICATE_FAILED:
                Toast.makeText(context, R.string.username_does_not_exist, Toast.LENGTH_SHORT).show();
                break;
            case MERCIEvents.ON_JSON_ERROR:
                Toast.makeText(context, R.string.check_network_settings, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
