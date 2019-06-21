package ru.stairenx.nvsutimetable.fragments;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import ru.stairenx.nvsutimetable.R;
import ru.stairenx.nvsutimetable.activity.MainActivity;
import ru.stairenx.nvsutimetable.adapter.AppSharedPreferences;

public class StudentLoginFragment extends Fragment {
    private static  final int LAYOUT = R.layout.fragment_student_login;
    private View view;
    public EditText editTextGroup;
    public EditText editTextSubGroup;
    public CheckBox checkBoxSubGroup;
    public ImageButton buttonStudent;
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        initEditTexts();
        initButtons();
        imageView = view.findViewById(R.id.vector_animation);
        animateImageView(imageView);
        return view;
    }

    private void initEditTexts(){
        editTextGroup = (EditText) view.findViewById(R.id.edit_text_group_login_student);
        editTextSubGroup = (EditText) view.findViewById(R.id.edit_text_subgroup_login_student);
        editTextGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editTextGroup.length() == 4)
                    buttonStudent.setVisibility(View.VISIBLE);
                else
                    buttonStudent.setVisibility(View.INVISIBLE);
            }
        });
        checkBoxSubGroup = (CheckBox) view.findViewById(R.id.check_subgroup_login_student);
        checkBoxSubGroup.setChecked(false);
        checkBoxSubGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxSubGroup.isChecked()){
                    editTextSubGroup.setVisibility(View.VISIBLE);
                }
                else{
                    editTextSubGroup.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void initButtons(){
        buttonStudent = (ImageButton) view.findViewById(R.id.button_login_student_select);
        buttonStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInformation(editTextGroup.getText().toString(), editTextSubGroup.getText().toString());
            }
        });
    }

    private void saveInformation(String newGroup, String newSubGroup) {
        if(editTextSubGroup.length() == 0 || !checkBoxSubGroup.isChecked())
            newSubGroup="0";
        if(AppSharedPreferences.Group.saveGroup(getContext().getApplicationContext(),newGroup)&&
        AppSharedPreferences.Subgroup.saveSubgroup(getContext().getApplicationContext(),newSubGroup)) {
            startActivity(new Intent(view.getContext(), MainActivity.class));
        }else{
            Toast.makeText(getContext().getApplicationContext(), "Ошибка записи", Toast.LENGTH_SHORT).show();
            Log.d("---","Error. Запись preferences не произошла");
        }
    }

    private static void animateImageView(ImageView imageView) {
        final Drawable drawable = imageView.getDrawable();
        if (drawable instanceof Animatable) {
            Animatable animatable = ((Animatable) drawable);
            animatable.start();
        }
    }
}
