package ru.stairenx.nvsutimetable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import ru.stairenx.nvsutimetable.ConstantsNVSU;
import ru.stairenx.nvsutimetable.database.DatabaseAction;

public class SplashActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserGroup();
    }

    private void getUserGroup(){
        DatabaseAction.setContext(getApplicationContext());
        String group = DatabaseAction.getUserGroup();
        if(!group.equals(ConstantsNVSU.NONE)){
            intent = new Intent(this, MainActivity.class);
            intent.putExtra("group", group);
        }else{
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
        this.finish();
    }

}
