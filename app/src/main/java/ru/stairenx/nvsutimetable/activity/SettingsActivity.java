package ru.stairenx.nvsutimetable.activity;

import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.stairenx.nvsutimetable.R;
import ru.stairenx.nvsutimetable.database.DatabaseAction;
import ru.stairenx.nvsutimetable.item.UserItem;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbarSettings;
    private EditText et_group;
    private EditText et_subgroup;
    private Button submit;
    private TextView faculty;
    private TextView group;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initToolbar();
        initFoarmSetting();
        //initInformation();
    }

    private void initToolbar(){
        toolbarSettings = (Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbarSettings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initInformation(){
        faculty = findViewById(R.id.name_faculty);
        group = findViewById(R.id.name_group);
        DatabaseAction.setContext(getApplicationContext());
        List<UserItem> info = DatabaseAction.getUserInformation("");
        faculty.setText(info.get(0).getFaculty());
        group.setText(info.get(0).getGroup());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: finish(); break;
        }
        if(item.getItemId()==android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    private void initFoarmSetting(){
        et_group = findViewById(R.id.edit_text_group);
        et_subgroup = findViewById(R.id.edit_text_subgroup);
        //et_group.setHint("Ваша группа: "+MainActivity.group);
        submit = findViewById(R.id.button_accept_change);
        saveInformation(submit);
    }

    private void saveInformation(Button btn){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gr = et_group.getText().toString();
                if(!gr.equals("")){
                    //MainActivity.group = gr;
                    //DatabaseAction.setContext(getApplicationContext());
                    //DatabaseAction.changeUserGroup(gr);
                    Toast.makeText(SettingsActivity.this, "Группа изменена на "+ gr, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
