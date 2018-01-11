package nsllab.merci_android_updated;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Moallim on 12/11/2017.
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, ConnectivityStatusControllerInterface {
    protected Context context;
    protected FragmentManager fragmentManager;
    protected Toolbar homeToolbar;
    protected NavigationView homeNavigationView;
    protected DrawerLayout homeDrawerLayout;
    protected ActionBarDrawerToggle homeToggle;
    protected SyncBtnsFragment syncBtnsFragment;
    protected RelativeLayout syncBtn;
    protected TextView usernameLabel;
    protected TextView lastSyncLabel;
    protected TextView fragmentTitle;
    protected Fragment oldFragment;
    protected Fragment newFragment;
    protected MERCIMiddleware middleware;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        context = this;
        fragmentManager = getSupportFragmentManager();
        middleware = new MERCIMiddleware(context);
        syncBtnsFragment = new SyncBtnsFragment();
        homeToolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        homeDrawerLayout = (DrawerLayout) findViewById(R.id.home_drawer_layout);
        homeToggle = new ActionBarDrawerToggle(this, homeDrawerLayout, homeToolbar, R.string.home_label, R.string.home_label);
        homeDrawerLayout.addDrawerListener(homeToggle);
        homeToggle.syncState();

        homeNavigationView = (NavigationView) findViewById(R.id.home_navigation_view);
        homeNavigationView.setNavigationItemSelectedListener(this);
        usernameLabel = homeNavigationView.getHeaderView(0).findViewById(R.id.side_navigation_username_label);
        lastSyncLabel = homeNavigationView.getHeaderView(0).findViewById(R.id.side_navigation_last_sync_label);

        syncBtn = (RelativeLayout) findViewById(R.id.sync_btn);
        syncBtn.setOnClickListener(this);

        Class fragmentClass = HomeIndexFragment.class;
        try {
            newFragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e){
            Toast.makeText(context, getString(R.string.internal_error), Toast.LENGTH_SHORT).show();
        }
        fragmentManager.beginTransaction().replace(R.id.home_fragment_content, newFragment, "current_fragment").commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        homeDrawerLayout.closeDrawer(GravityCompat.START);
        oldFragment = fragmentManager.findFragmentById(R.id.home_fragment_content);
        newFragment = null;
        try {
            switch (item.getItemId()){
                case R.id.reset_cache:
                    switch (middleware.resentCacheMiddleware()){
                        case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                        case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                            Intent intent = new Intent(context, LangActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                            finish();
                            break;
                    }
                    return true;
                case R.id.sign_out:
                    signOut();
                    return true;
                case R.id.map:
                    newFragment = HomeMapFragment.class.newInstance();
                    break;
                case R.id.about:
                    newFragment = HomeAboutFragment.class.newInstance();
                    break;
                case R.id.network_settings:
                    newFragment = HomeNetworkSettingsFragment.class.newInstance();
                    break;
                default:
                    newFragment = HomeIndexFragment.class.newInstance();
                    break;
            }
            if(!(oldFragment.getClass().isInstance(newFragment))) {
                fragmentManager.beginTransaction().replace(R.id.home_fragment_content, newFragment).commit();
            }
        } catch (Exception e){
            Toast.makeText(context, getString(R.string.internal_error), Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void updateConnectivityStatus(String status) {
        NetworkConnectivityStatusFragment networkConnectivityStatusFragment = (NetworkConnectivityStatusFragment) fragmentManager.findFragmentById(R.id.network_connectivity_status_fragment);
        networkConnectivityStatusFragment.updateNetworkConnectivityStatus(status);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sync_btn:
                syncBtnsFragment.show(fragmentManager, syncBtnsFragment.getTag());
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUsernameAndLastSyncLabels();
    }

    public void toggleSyncBtn(Boolean status){
        syncBtn = (RelativeLayout) findViewById(R.id.sync_btn);
        if(status)
            syncBtn.setVisibility(View.VISIBLE);
        else
            syncBtn.setVisibility(View.INVISIBLE);
    }

    public void setFragmentTitle(String title){
        fragmentTitle = (TextView) findViewById(R.id.fragmentTitle);
        fragmentTitle.setText(title);
    }

    public void refreshList(){
        if(newFragment instanceof HomeIndexFragment){
            HomeIndexFragment fragment = (HomeIndexFragment) newFragment;
            fragment.refreshList();
        }
    }

    public void updateUsernameAndLastSyncLabels(){
        Map<String, String> resultMap = new HashMap<>();
        switch (middleware.getUsernameAndLastSyncDateMiddleware(resultMap)){
            case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
                usernameLabel.setText(resultMap.get("username"));
                lastSyncLabel.setText(resultMap.get("last_sync"));
                break;
            case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                signOut();
                break;
        }
    }

    public void signOut(){
        switch (middleware.signOutMiddleware()){
            case MERCIMiddleware.MIDDLEWARE_FUNC_SUCCEED:
            case MERCIMiddleware.MIDDLEWARE_FUNC_FAILED:
                Intent intent = new Intent(context, LangActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                finish();
                break;
        }
    }
}
