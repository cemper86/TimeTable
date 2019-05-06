package ru.stairenx.nvsutimetable;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private Button btn;
    private EditText group;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        group = findViewById(R.id.numGroup);
        btn = findViewById(R.id.btn);
        initToolbar();
        initBtn();
    }

    private void initToolbar(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initBtn(){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gr = group.getText().toString();
                ConstantsNVSU.group = gr;
                Toast.makeText(ProfileActivity.this, "Группа "+ ConstantsNVSU.group, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
