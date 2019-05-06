package ru.stairenx.nvsutimetable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import ru.stairenx.nvsutimetable.ConstantsNVSU;
import ru.stairenx.nvsutimetable.database.DatabaseAction;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserGroup();
    }

    private void getUserGroup(){
        DatabaseAction.setContext(getApplicationContext());
        String group = DatabaseAction.getUserGroup();
        String subGroup = DatabaseAction.getUserSubgroup();
        if(!group.equals(ConstantsNVSU.NONE)){
            MainActivity.group = group;
            MainActivity.subGroup = subGroup;
            startActivity(new Intent(this, MainActivity.class));
        }else{
            startActivity(new Intent(this, LoginActivity.class));
        }
        this.finish();
    }

}
