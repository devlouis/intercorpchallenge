package com.louislopez.intercorpchallenge.app.activity;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.louislopez.intercorpchallenge.R;
import com.louislopez.intercorpchallenge.app.core.BaseAppCompat;
import com.louislopez.intercorpchallenge.data.entity.UserEntity;
import com.louislopez.intercorpchallenge.utils.LogUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RegisterActivity extends BaseAppCompat implements DatePickerDialog.OnDateSetListener, DatePickerDialog.OnDismissListener {
    private String TAG = this.getClass().getSimpleName();
    @BindView(R.id.eteName)
    TextInputEditText eteName;

    @BindView(R.id.eteLastName)
    TextInputEditText eteLastName;

    @BindView(R.id.eteAge)
    TextInputEditText eteAge;

    @BindView(R.id.eteBirthday)
    TextInputEditText eteBirthday;

    @BindView(R.id.textInputLayout_Birthday)
    TextInputLayout textInputLayout_Birthday;

    @BindView(R.id.btnSendData)
    Button btnSendData;

    String email = "";
    String Uid = "";

    Boolean isDataSet = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        //btnSendData.setOnClickListener(this);
        getBundle();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            eteBirthday.setShowSoftInputOnFocus(false);
        }


        btnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm())
                    sendDataFireBase();
                else
                    snackBarFail("Datos incompletos", btnSendData);
            }
        });

        textInputLayout_Birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                showDatePickerDialog();
            }
        });

        eteBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus) {
                    showDatePickerDialog();
                }


            }
        });
    }

    public boolean validateForm(){
        if (eteName.getText().toString().trim().isEmpty()){
            return false;
        }else if (eteLastName.getText().toString().trim().isEmpty()){
            return false;
        }else if (eteAge.getText().toString().trim().isEmpty()){
            return false;
        }else if (eteBirthday.getText().toString().trim().isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    public void sendDataFireBase(){
        FirebaseDatabase  database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");
        DatabaseReference usersRef = ref.child("users");

        Map<String, UserEntity> users = new HashMap<>();
        users.put(Uid, new UserEntity(email,eteName.getText().toString(), eteLastName.getText().toString(),eteAge.getText().toString(),eteBirthday.getText().toString()));
        ref.setValue(users);

        snackBarSucceso("Datos enviados", btnSendData);
    }

    public void getBundle(){
        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email");
        Uid = bundle.getString("Uid");
        LogUtils.v(TAG, "email: " + email);
    }

    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setOnDismissListener(this);
        datePickerDialog.show();
        hideSoftKeyboard(eteBirthday);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date =  dayOfMonth + "/" + month + "/" + year;
        eteBirthday.clearFocus();
        eteBirthday.setText(date);
    }



    private void hideSoftKeyboard(View v){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
            eteBirthday.clearFocus();
    }
}
