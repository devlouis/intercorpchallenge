package com.louislopez.intercorpchallenge.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.louislopez.intercorpchallenge.R;
import com.louislopez.intercorpchallenge.app.core.BaseAppCompat;
import com.louislopez.intercorpchallenge.utils.LogUtils;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseAppCompat implements View.OnClickListener {
    private String TAG = this.getClass().getSimpleName();
    @BindView(R.id.btnLoginFacebook)
    Button btnLoginFacebook;

    @BindView(R.id.vLoading)
    RelativeLayout vLoading;

    FirebaseAuth mAuth;
    LoginManager mLoginManager;
    AccessTokenTracker mAccessTokenTracker;
    CallbackManager mFacebookCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initUI();
    }

    public void initUI(){
        mAuth = FirebaseAuth.getInstance();
        setupFacebook();
        updateFacebookButtonUI();
        btnLoginFacebook.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoginFacebook:
                handleFacebookLogin();
                break;

        }

    }


    private void setupFacebook() {


        //FacebookSdk.sdkInitialize(getApplicationContext());

        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                updateFacebookButtonUI();
            }
        };

        mLoginManager = LoginManager.getInstance();
        mFacebookCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                LogUtils.v(TAG, "facebook:onSuccess:" + loginResult);
                //updateFacebookButtonUI();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                LogUtils.v(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                LogUtils.v(TAG, "facebook:onError " +  error);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void updateFacebookButtonUI(){
            if (AccessToken.getCurrentAccessToken() != null){
                btnLoginFacebook.setText("Logout");
                //tviUserName.setText("Hello Friend");
            }else{
                btnLoginFacebook.setText(getResources().getString(R.string.ingresa_con_facebook));
                //tviUserName.setText("Hello Anonymous");
            }
        }

     private void handleFacebookLogin(){
        if (AccessToken.getCurrentAccessToken() != null){
            mLoginManager.logOut();
        }else{
            vLoading.setVisibility(View.VISIBLE);
            mAccessTokenTracker.startTracking();
            mLoginManager.logInWithReadPermissions(LoginActivity.this, Arrays.asList("email","public_profile"));
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        LogUtils.v(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Inicio sesión correctamente.
                            LogUtils.v(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            LogUtils.v(TAG, "FirebaseUser: " + user.getEmail());
                            Bundle bundle = new Bundle();
                            bundle.putString("email", user.getEmail() == null ? "" : user.getEmail());
                            bundle.putString("Uid", user.getUid() == null ? "" : user.getUid());
                            nextData(RegisterActivity.class, bundle, true);
                            //updateUI(user);
                        } else {
                            // Si el inicio de sesión falla, mostrar mensaje al usuario.
                            LogUtils.v(TAG, "signInWithCredential:failure "+ task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onStop() {
        super.onStop();
        vLoading.setVisibility(View.GONE);
    }
}

