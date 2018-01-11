package nsllab.merci_android_updated;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Moallim on 12/13/2017.
 */

public class EditAssessmentMapFragment extends Fragment implements OnMapReadyCallback, LocationCallbackInterface {
    protected Context context;
    protected FragmentManager fragmentManager;
    protected MapView mapView;
    protected GoogleMap googleMap;
    protected MERCILocation location;
    protected MERCIMiddleware middleware;
    protected MarkerOptions currentLocationMarkerOptions;
    protected Marker currentLocationMarker;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_map_fragment, container, false);

        context = getActivity();
        fragmentManager = getFragmentManager();
        location = new MERCILocation(context, this);
        middleware = new MERCIMiddleware(context);

        mapView = view.findViewById(R.id.show_map_view);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        location.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            this.googleMap = googleMap;
            this.googleMap.setMyLocationEnabled(true);
            updateMapUI();
            Double[] point = location.getLastPosition();
            LatLng currentPosition = new LatLng(point[0], point[1]);

            currentLocationMarkerOptions = new MarkerOptions().position(currentPosition).title(getCurrentLocationMarkerTitle()).snippet("");
            currentLocationMarker = this.googleMap.addMarker(currentLocationMarkerOptions);
            middleware.updateCurrentLocationMiddleware(point[0], point[1]);

            CameraPosition cameraPosition = new CameraPosition.Builder().target(currentPosition).zoom(location.DEFAULT_ZOOM).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } catch (SecurityException e){
            return;
        }
    }

    @Override
    public void onLocationListener(String id) {
        switch (id){
            case MERCILocation.ON_CONNECTED:
                mapView.getMapAsync(this);
                break;
            case MERCILocation.ON_LOCATION_CHANGED:
                Double[] point = location.getLastPosition();
                LatLng currentPosition = new LatLng(point[0], point[1]);
                currentLocationMarker.setPosition(currentPosition);
                middleware.updateCurrentLocationMiddleware(point[0], point[1]);
                break;
            case MERCILocation.ON_CONNECTION_FAILED:
                Toast.makeText(context, R.string.cannot_connect_to_google_api, Toast.LENGTH_SHORT).show();
                break;
            case MERCILocation.ON_REQUEST_PERMITTED:
                try {
                    MapsInitializer.initialize(context);
                } catch (Exception e){
                    Toast.makeText(context, R.string.map_loading_error, Toast.LENGTH_SHORT).show();
                }
                break;
            case MERCILocation.ON_SECURITY_EXCEPTION:
                Toast.makeText(context, R.string.location_permission_error, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        location.resumeLocationUpdates();
        if(currentLocationMarker != null){
            MarkerOptions markerOptions = getMarker(getCurrentLocationMarkerTitle(), currentLocationMarker.getPosition(), null);
            currentLocationMarker.remove();
            googleMap.addMarker(markerOptions);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        location.pauseLocationUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onStart() {
        super.onStart();
        location.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        location.disconnect();
    }

    public void updateMapUI(){
        UiSettings uisettings = googleMap.getUiSettings();
        uisettings.setZoomGesturesEnabled(true);
        uisettings.setZoomControlsEnabled(true);
        uisettings.setCompassEnabled(true);
    }

    public String getCurrentLocationMarkerTitle(){
        Map<String, String> assessmentMap = new HashMap<>();
        switch (middleware.getCurrentAssessmentDetailsMiddleware(assessmentMap)){
            case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                return assessmentMap.get("Name");
            case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                return getString(R.string.empty);
        }
        return getString(R.string.empty);
    }

    public MarkerOptions getMarker(String title, LatLng position, String snippet){
        return new MarkerOptions().position(position).title(title).snippet(snippet);
    }
}
