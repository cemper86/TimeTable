package ru.stairenx.nvsutimetable.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import ru.stairenx.nvsutimetable.activity.LoginActivity;
import ru.stairenx.nvsutimetable.R;

public class StudentLoginFragment extends Fragment {
    private static  final int LAYOUT = R.layout.fragment_student_login;
    private View view;
    public static EditText editTextGroup;
    public static EditText editTextSubGroup;
    public static CheckBox checkBoxSubGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        initEditTexts();
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
                if(editTextGroup.length() > 3){
                    LoginActivity.buttonStudent.setText("Принять");
                }
                else
                    LoginActivity.buttonStudent.setText("СТУДЕНТ");
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
}
