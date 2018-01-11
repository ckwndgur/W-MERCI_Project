package nsllab.merci_android_updated;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Moallim on 12/13/2017.
 */

public class EditAssessmentDetailsMediaBtnsFragment extends BottomSheetDialogFragment implements View.OnClickListener, LocationCallbackInterface {
    protected Context context;
    protected Activity activity;
    protected MERCIMiddleware middleware;
    protected MERCINetworkAPI api;
    protected Button addPhotoBtn;
    protected Button addVideoBtn;
    protected MERCILocation location;
    protected static final int REQUEST_IMAGE_CAPTURE = 156;
    protected static final int REUQEST_GALLERY_SELECT = 155;
    protected Double[] currentPosition;
    protected Map<String, String> mediaMetadata = new HashMap<>();
    protected String filename;
    protected String absoluteFilePath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_assessment_details_media_btns_fragment, container, false);
        context = getActivity();
        activity = getActivity();
        middleware = new MERCIMiddleware(context);
        api = new MERCINetworkAPI(context);
        location = new MERCILocation(context, this);

        addPhotoBtn = view.findViewById(R.id.add_photo_btn);
        addPhotoBtn.setOnClickListener(this);
        addVideoBtn = view.findViewById(R.id.add_video_btn);
        addVideoBtn.setOnClickListener(this);
        setHasOptionsMenu(true);
        registerForContextMenu(addPhotoBtn);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_photo_btn:
                switch (middleware.getInspectorAndIncidentAndAssessmentIdsMiddleware(mediaMetadata)){
                    case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                        activity.openContextMenu(addPhotoBtn);
                        break;
                    case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                    case MERCIMiddleware.MIDDLEWARE_JSON_ERROR:
                        Toast.makeText(context, R.string.could_not_generate_media_metadata, Toast.LENGTH_SHORT).show();
                        getDialog().dismiss();
                        break;
                }
                break;
            case R.id.add_video_btn:
                Toast.makeText(context, R.string.not_supported_by_the_server, Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.media, menu);
        MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onContextItemSelected(item);
                return true;
            }
        };
        for(int i = 0; i < menu.size(); i++)
            menu.getItem(i).setOnMenuItemClickListener(listener);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.capture_from_camera:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(activity.getPackageManager()) != null)
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                break;
            case R.id.select_from_gallery:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            File destination = createImageFile();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (IOException e){
                Log.d("TAG", "onActivityResult: " + e.getMessage());
            }
            if(destination != null){
                //Uri imageURI = FileProvider.getUriForFile(this, )
            }
            //Log.d("asf", "onActivityResult: " + imageBitmap);
        } else if(requestCode == REUQEST_GALLERY_SELECT && resultCode == RESULT_OK){

        }
    }

    private File createImageFile(){
        filename = java.util.UUID.randomUUID().toString();
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, filename + ".jpg");
        absoluteFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onLocationListener(String id) {
        switch (id){
            case MERCILocation.ON_CONNECTED:
                currentPosition = location.getLastPosition();
                break;
            case MERCILocation.ON_LOCATION_CHANGED:
                currentPosition = location.getLastPosition();
                break;
            case MERCILocation.ON_CONNECTION_FAILED:
                Toast.makeText(context, R.string.cannot_connect_to_google_api, Toast.LENGTH_SHORT).show();
                break;
            case MERCILocation.ON_REQUEST_PERMITTED:
                currentPosition = location.getLastPosition();
                break;
            case MERCILocation.ON_SECURITY_EXCEPTION:
                Toast.makeText(context, R.string.location_permission_error, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        location.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResume() {
        super.onResume();
        location.resumeLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        location.pauseLocationUpdates();
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
}
