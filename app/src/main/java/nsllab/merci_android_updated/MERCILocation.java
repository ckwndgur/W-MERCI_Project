package nsllab.merci_android_updated;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.Date;

/**
 * Created by Moallim on 12/13/2017.
 */

public class MERCILocation implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    protected Context context;
    protected GoogleApiClient googleApiClient;
    protected LocationRequest locationRequest;
    protected Location currentLocation;
    protected String lastUpdateTime;
    protected LocationCallbackInterface locationCallbackInterface;
    protected static final int ACCESS_FINE_LOCATION = 159;
    protected static final int PLAY_SERVICES_REQUEST = 158;
    protected static final int REQUEST_CHECK_SETTINGS = 157;
    protected static final int DEFAULT_ZOOM = 16;
    protected static final String ON_CONNECTED = "ON_CONNECTED";
    protected static final String ON_CONNECTION_FAILED = "ON_CONNECTION_FAILED";
    protected static final String ON_LOCATION_CHANGED = "ON_LOCATION_CHANGED";
    protected static final String ON_REQUEST_PERMITTED = "ON_REQUEST_PERMITTED";
    protected static final String ON_SECURITY_EXCEPTION = "ON_SECURITY_EXCEPTION";

    public MERCILocation(Context context, LocationCallbackInterface locationCallbackInterface){
        this.context = context;
        this.locationCallbackInterface = locationCallbackInterface;
        if(checkPlayServices())
            buildGoogleApiClient();
    }

    public void connect(){
        googleApiClient.connect();
    }
    public void disconnect(){
        if (isConnected()) {
            stopLocationUpdates();
        }
        googleApiClient.disconnect();
    }
    public Boolean isConnected(){
        return googleApiClient.isConnected();
    }
    public void resumeLocationUpdates(){
        if (googleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }
    public void pauseLocationUpdates(){
        if (googleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }


    public void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        locationRequest = new LocationRequest();
    }

    public boolean checkPlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            googleApiAvailability.getErrorDialog((Activity) context, resultCode, PLAY_SERVICES_REQUEST).show();
            return false;
        }
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            if (currentLocation == null) {
                currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                lastUpdateTime = java.text.DateFormat.getTimeInstance().format(new Date());
                locationCallbackInterface.onLocationListener(ON_CONNECTED);
            }
        } catch (SecurityException e){
            locationCallbackInterface.onLocationListener(ON_SECURITY_EXCEPTION);
        }
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        locationCallbackInterface.onLocationListener(ON_CONNECTION_FAILED);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null)
            currentLocation = location;
        locationCallbackInterface.onLocationListener(ON_LOCATION_CHANGED);
    }

    protected void startLocationUpdates() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
                        } else {
                            locationRequest.setInterval(10000);
                            locationRequest.setFastestInterval(5000);
                            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, MERCILocation.this);
                            locationCallbackInterface.onLocationListener(ON_REQUEST_PERMITTED);
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult((Activity) context, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {}
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    public Double[] getLastPosition(){
        if(currentLocation != null) {
            return new Double[]{currentLocation.getLatitude(), currentLocation.getLongitude()};
        } else
            return new Double[]{ 37.5650172, 126.8494671 };
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            switch (requestCode){
                case MERCILocation.ACCESS_FINE_LOCATION:
                    locationCallbackInterface.onLocationListener(ON_REQUEST_PERMITTED);
                    break;
            }
        } else {
            Toast.makeText(context, R.string.location_permission_error, Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
