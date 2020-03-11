package com.example.plt.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plt.BottomNivBarActivity;
import com.example.plt.Common.Common;
import com.example.plt.Models.User;
import com.example.plt.R;
import com.example.plt.SignUp.RegisterActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText phoneInput;
    private EditText passwordInput;
    private CheckBox chkBoxRememberMe;

    private TextView txtFotgetPass;
    private Animation shake;

    private ProgressDialog loadingBar;

    private FirebaseDatabase database;
    private DatabaseReference userTable;

    ProgressDialog mDialog;

    private final static String PARENT_DB_NAME = "Users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        loginButton = findViewById(R.id.login_btn);
        phoneInput = findViewById(R.id.edtLoginPhone);
        passwordInput = findViewById(R.id.edtLoginPass);
        chkBoxRememberMe = findViewById(R.id.remember_me_chkb);
        txtFotgetPass=findViewById(R.id.txtForgetPass);

        Paper.init(this);

        database = FirebaseDatabase.getInstance();
        userTable = database.getReference("User");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        txtFotgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this,"wrong code try again!",Toast.LENGTH_SHORT).show();
                showForgetPassDialog();
            }
        });
    }

    private void showForgetPassDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forget Password");
        builder.setMessage("enter your secure code");


        LayoutInflater inflater = this.getLayoutInflater();
        View forget_view =inflater.inflate(R.layout.forget_password_layout,null);

        builder.setView(forget_view);
        builder.setIcon(R.drawable.ic_security);

        final EditText edtPhone = forget_view.findViewById(R.id.edtPhone_forgetpass);
        final EditText edtsecureCode = forget_view.findViewById(R.id.edt_secureCode_forgetpass);

       builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               userTable.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       User user = dataSnapshot.child(edtPhone.getText().toString())
                               .getValue(User.class);
                       if (user.getSecureCode().equals(edtsecureCode.getText().toString())) {
                           Toast.makeText(LoginActivity.this, "YOUR PASS: " + user.getPassword()
                                   , Toast.LENGTH_LONG).show();
                       } else {
                           Toast.makeText(LoginActivity.this, "wrong code try again!"
                                   , Toast.LENGTH_SHORT).show();
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });
           }
       });

       builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {

           }
       });
       builder.show();
        }


    private void loginUser() {
        String phone = phoneInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (chkBoxRememberMe.isChecked()) {
            Paper.book().write(Common.USER_KEY, phone);
            Paper.book().write(Common.PSWD_KEY, password);
        }

        if (TextUtils.isEmpty(phone)) {
            shake(phoneInput);
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            shake(passwordInput);
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        } else {
            mDialog = new ProgressDialog(LoginActivity.this);
            mDialog.setMessage("logging in...");
            mDialog.show();

            accountAccess(phone, password);

        }

    }

    private void accountAccess(final String phone, final String password) {

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
                        Intent homeIntent = new Intent(LoginActivity.this, BottomNivBarActivity.class);
                        Common.currentUser = user;
                        startActivity(homeIntent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "incorent pass", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "user not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void shake(EditText editText) {
        shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
        editText.startAnimation(shake);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();    }
}
