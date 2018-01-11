package nsllab.merci_android_updated;


import com.android.volley.error.VolleyError;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Moallim on 12/11/2017.
 */

public interface VolleyCallbackInterface {
    void onRequestSuccess(String GUID, JSONObject result, Map dict);
    void onRequestError(String GUID, VolleyError result, Map dict);
}
