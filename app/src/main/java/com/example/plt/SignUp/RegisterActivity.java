package com.example.plt.SignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.plt.BottomNivBarActivity;
import com.example.plt.Login.LoginActivity;
import com.example.plt.Models.User;
import com.example.plt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Button createAccountButton;
    private EditText phoneInput;
    private EditText passwordInput;
    private EditText firstNameInput;
    private EditText lastNameInput,edtSecureCode;

    private ProgressDialog mDialog;
    private Animation shake;

    private FirebaseDatabase database;
    private DatabaseReference userTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
    }

    private void init() {
        createAccountButton = findViewById(R.id.register_btn);
        phoneInput = findViewById(R.id.edtRegPhone);
        passwordInput = findViewById(R.id.edtRegPassword);
        firstNameInput = findViewById(R.id.edtRegLastname);
        lastNameInput = findViewById(R.id.edtRegFirstname);
        edtSecureCode=findViewById(R.id.edt_secureCode);

        database = FirebaseDatabase.getInstance();
        userTable = database.getReference("User");

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String phone = phoneInput.getText().toString();
        String password = passwordInput.getText().toString();
        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();

        if (TextUtils.isEmpty(firstName)) {
            shake(firstNameInput);
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(lastName)) {
            shake(lastNameInput);
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            shake(phoneInput);
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            shake(passwordInput);
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        } else {

            mDialog = new ProgressDialog(this);
            mDialog.setMessage("signing up...");
            mDialog.show();
            userTable.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(phoneInput.getText().toString()).exists()) {
                        mDialog.dismiss();
                        accountExistsAlert();
                        Toast.makeText(RegisterActivity.this, "user exist.", Toast.LENGTH_SHORT).show();
                    } else {
                        User user = new User(firstNameInput.getText().toString(), lastNameInput.getText().toString()
                                ,passwordInput.getText().toString(),edtSecureCode.getText().toString());
                        userTable.child(phoneInput.getText().toString()).setValue(user);
                        Toast.makeText(RegisterActivity.this, "sign up succeful.", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


    private void accountExistsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Failed to sign up")
                .setMessage("An account with this phone number already exists.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void shake(EditText editText) {
        shake = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.shake);
        editText.startAnimation(shake);
    }
}
