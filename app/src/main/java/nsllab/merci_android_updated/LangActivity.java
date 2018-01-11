package nsllab.merci_android_updated;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Moallim on 12/11/2017.
 */

public class LangActivity extends AppCompatActivity implements View.OnClickListener {
    protected Context context;
    protected RelativeLayout langBtn;
    protected TextView langLabel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lang_activity);

        context = this;
        langBtn = (RelativeLayout) findViewById(R.id.langBtn);
        langLabel = (TextView) findViewById(R.id.LangLabel);
        registerForContextMenu(langBtn);
        invalidateOptionsMenu();
        langBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.langBtn:
                openContextMenu(langBtn);
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.langs, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.krn){
            Toast.makeText(this, R.string.not_supported_yet, Toast.LENGTH_SHORT).show();
            return false;
        }
        langLabel.setText(item.getTitle());
        Intent intent = new Intent(LangActivity.this, LoginActivity.class);
        startActivity(intent);
        return super.onContextItemSelected(item);
    }
}
