package com.louislopez.intercorpchallenge.app.core;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.louislopez.intercorpchallenge.R;

/**
 * Created by louislopez on 14,August,2019
 * MDP Consulting,
 * Peru, Lima.
 */
public class BaseAppCompat  extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }

    protected void nextActivity(Class<?> activity) {
        this.nextActivity(activity, false);
    }

    protected void nextData(Class<?> activity, Bundle bundle) {
        this.nextData(activity, bundle, false);
    }

    public void nextActivity(Class<?> activity, boolean notDestroy) {
        this.startActivity(new Intent(this, activity));
        if(!notDestroy) {
            this.finish();
        }

    }

    public void nextData(Class<?> activity, Bundle bundle, boolean notDestroy) {
        Intent intent = new Intent(this, activity);
        intent.putExtras(bundle);
        this.startActivity(intent);
        if(!notDestroy) {
            this.finish();
        }
    }

    protected void resultActivity(Class<?> activity, int code){
        this.startActivityForResult(new Intent(this, activity),code);
    }

    protected void resultActivityData(Class<?> activity, int code, Bundle bundle){
        Intent intent = new Intent(this, activity);
        intent.putExtras(bundle);
        this.startActivityForResult(intent,code);
    }

    public void snackBarFail(String msj, View rlaContent){
        Snackbar snackbar = Snackbar
                .make(rlaContent, msj, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.c_error));
        snackbar.show();

    }

    public void snackBarSucceso(String msj, View rlaContent){
        Snackbar snackbar = Snackbar
                .make(rlaContent, msj, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.c_ok));
        snackbar.show();

    }
}
