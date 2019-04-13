package ru.stairenx.nvsutimetable.activity;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import ru.stairenx.nvsutimetable.ConstantsNVSU;
import ru.stairenx.nvsutimetable.R;
import ru.stairenx.nvsutimetable.database.DatabaseAction;

public class LoginActivity extends AppCompatActivity {

    private LinearLayout layoutGroup;
    private ImageView imageView;
    private EditText subgroup;
    private CheckBox checkBox;
    private Button submit;
    private LinearLayout lCheck;
    private Spinner faculty;
    private Spinner group;
    private String[] facultyArray = {"Факультет ->",ConstantsNVSU.facultet_gumanitar,ConstantsNVSU.facultet_inginer_tech,ConstantsNVSU.facultet_dop_edu,ConstantsNVSU.facultet_it,ConstantsNVSU.facultet_disign,ConstantsNVSU.facultet_pedagog,ConstantsNVSU.facultet_sport,ConstantsNVSU.facultet_eco,ConstantsNVSU.facultet_finance};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initLayoutFaculty();
        imageView = findViewById(R.id.vector_animation);
        animateImageView(imageView);
        DatabaseAction.setContext(getApplicationContext());

    }

    private void initView(){
        faculty = findViewById(R.id.spinner_faculty);
        layoutGroup = findViewById(R.id.layout_spinner_group);
        lCheck = findViewById(R.id.layout_check);
        group = findViewById(R.id.spinner_group);
        subgroup = findViewById(R.id.subgroup);
        checkBox = findViewById(R.id.check_subgroup);
        submit = findViewById(R.id.btn_add_user);
    }

    private void initLayoutFaculty(){
        layoutGroup.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.castom_spinner_item,R.id.spinner_item_text,facultyArray);
        faculty.setAdapter(adapter);
        faculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0 :
                        layoutGroup.setVisibility(View.GONE);
                        lCheck.setVisibility(View.INVISIBLE);
                        break;
                    case 1 :
                        initLayoutGroup(ConstantsNVSU.ARRAY_FACULTET_GUMANITAR);
                        break;
                    case 2 :
                        initLayoutGroup(ConstantsNVSU.ARRAY_FACULTET_INGINER_TECH);
                        break;
                    case 3 :
                        initLayoutGroup(ConstantsNVSU.ARRAY_FACULTET_DOP_EDU);
                        break;
                    case 4 :
                        initLayoutGroup(ConstantsNVSU.ARRAY_FACULTET_IT);
                        break;
                    case 5 :
                        initLayoutGroup(ConstantsNVSU.ARRAY_FACULTET_DISIGN);
                        break;
                    case 6 :
                        initLayoutGroup(ConstantsNVSU.ARRAY_FACULTET_PEDAGOG);
                        break;
                    case 7 :
                        initLayoutGroup(ConstantsNVSU.ARRAY_FACULTET_SPORT);
                        break;
                    case 8 :
                        initLayoutGroup(ConstantsNVSU.ARRAY_FACULTET_ECO);
                        break;
                    case 9 :
                        initLayoutGroup(ConstantsNVSU.ARRAY_FACULTET_FINANCE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initLayoutGroup(String[] array){
        layoutGroup.setVisibility(View.VISIBLE);
        submit.setVisibility(View.VISIBLE);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.castom_spinner_item,R.id.spinner_item_text,array);
        group.setAdapter(adapter);
        lCheck.setVisibility(View.VISIBLE);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    subgroup.setVisibility(View.VISIBLE);
                }else{
                    subgroup.setVisibility(View.INVISIBLE);
                }
            }
        });

        save();
    }

    private void save(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                String f;
                String g;
                String s;
                f = faculty.getSelectedItem().toString();
                g = group.getSelectedItem().toString();
                s = subgroup.getText().toString();
                if(s.equals("")){
                    s = "0";
                }
                DatabaseAction.setContext(getApplicationContext());
                DatabaseAction.addUser(f, g, s);
                intent.putExtra("group", g);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showCountGroup(){
        int count;
        count = ConstantsNVSU.ARRAY_FACULTET_GUMANITAR.length
                + ConstantsNVSU.ARRAY_FACULTET_INGINER_TECH.length
                + ConstantsNVSU.ARRAY_FACULTET_DOP_EDU.length
                + ConstantsNVSU.ARRAY_FACULTET_IT.length
                + ConstantsNVSU.ARRAY_FACULTET_DISIGN.length
                + ConstantsNVSU.ARRAY_FACULTET_PEDAGOG.length
                + ConstantsNVSU.ARRAY_FACULTET_SPORT.length
                + ConstantsNVSU.ARRAY_FACULTET_ECO.length
                + ConstantsNVSU.ARRAY_FACULTET_FINANCE.length;
        Toast.makeText(this, "В универе" + String.valueOf(count) + " групп", Toast.LENGTH_SHORT).show();
    }
    private static void animateImageView(ImageView imageView) {
        final Drawable drawable = imageView.getDrawable();
        if (drawable instanceof Animatable) {
            Animatable animatable = ((Animatable) drawable);
            animatable.start();
        }
    }

}
