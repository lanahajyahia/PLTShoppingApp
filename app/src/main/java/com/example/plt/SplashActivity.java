package com.example.plt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.plt.Common.Common;
import com.example.plt.Login.LoginActivity;
import com.example.plt.Models.User;
import com.example.plt.SignUp.RegisterActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {

    ProgressBar progressBar;
    LinearLayout layout_signin;
    Button signin_btn, register_btn;

    private FirebaseDatabase database;
    private DatabaseReference userTable;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();

        if (Common.isConnectedToInternet(this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    layout_signin.setVisibility(View.VISIBLE);

                    signin_btn.setVisibility(View.VISIBLE);
                    register_btn.setVisibility(View.VISIBLE);

                    signin_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class
                            ));
                        }
                    });
                    register_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(SplashActivity.this, RegisterActivity.class
                            ));
                        }
                    });

                }
            } ,3000);

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SplashActivity.this, "check your wifi connection", Toast.LENGTH_LONG).show();
                }
            } ,2000);


        }

    }

    private void init() {


        layout_signin = findViewById(R.id.layout_signinbar);
        signin_btn = findViewById(R.id.Signin_btn_splash);
        register_btn = findViewById(R.id.signup_btn_splash);

        signin_btn.setVisibility(View.INVISIBLE);
        register_btn.setVisibility(View.INVISIBLE);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#DF2F6C"), android.graphics.PorterDuff.Mode.SRC_ATOP);
        progressBar.setVisibility(View.VISIBLE);

        Paper.init(this);

        String UserPhoneKey = Paper.book().read(Common.USER_KEY);
        String UserPasswordKey = Paper.book().read(Common.PSWD_KEY);

        if (UserPhoneKey != null && UserPasswordKey != null) {
            if (!UserPhoneKey.isEmpty() && !UserPasswordKey.isEmpty()) {
                login(UserPhoneKey, UserPasswordKey);

//                loadingBar.setTitle("Already Logged in");
//                loadingBar.setMessage("Please wait.....");
//                loadingBar.setCanceledOnTouchOutside(false);
//                loadingBar.show();
            }
        }


    }

    private void login(final String phone, final String password) {
        database = FirebaseDatabase.getInstance();
        userTable = database.getReference("User");

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        } else {
            mDialog = new ProgressDialog(this);
            mDialog.setMessage("logging in...");
            mDialog.show();

            userTable.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //check if user not exist
                    mDialog.dismiss();
                    if (dataSnapshot.child(phone).exists()) {
                        //get user from data
                        User user = dataSnapshot.child(phone).getValue(User.class);
                        user.setPhone(phone);
                        if (user.getPassword().equals(password)) {
                            Intent homeIntent = new Intent(SplashActivity.this, BottomNivBarActivity.class);
                            Common.currentUser = user;
                            startActivity(homeIntent);
                            finish();

                        } else {
                        }
                    } else {
                        mDialog.dismiss();
                        Toast.makeText(SplashActivity.this, "user not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }


}
