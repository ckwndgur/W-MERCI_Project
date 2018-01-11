package nsllab.merci_android_updated;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Moallim on 12/11/2017.
 */

public class NetworkConnectivityStatusFragment extends Fragment {
    protected Context context;
    protected TextView networkStatusLabel;
    protected LinearLayout networkStatusToolbar;
    protected static final String ONLINE = "ONLINE";
    protected static final String OFFLINE = "OFFLINE";
    protected static final String LOADING = "LOADING";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.network_connectivity_status_fragment, container, false);

        context = getActivity();
        networkStatusLabel = (TextView) view.findViewById(R.id.network_status_label);
        networkStatusToolbar = (LinearLayout) view.findViewById(R.id.network_status_toolbar);
        return view;
    }

    public void updateNetworkConnectivityStatus(String status){
        switch (status){
            case ONLINE:
                networkStatusLabel.setText(R.string.online);
                networkStatusToolbar.setBackgroundResource(R.color.colorGreenOnline);
                break;
            case OFFLINE:
                networkStatusLabel.setText(R.string.offline);
                networkStatusToolbar.setBackgroundResource(R.color.colorGoldOffline);
                break;
            case LOADING:
            default:
                networkStatusLabel.setText(R.string.connecting);
                networkStatusToolbar.setBackgroundResource(R.color.colorGrey);
                break;
        }
    }
}
