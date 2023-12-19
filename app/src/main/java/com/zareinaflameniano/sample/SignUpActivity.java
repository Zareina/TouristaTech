package com.zareinaflameniano.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends Activity {
    private FirebaseAuth mAuth;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText ptFirst = findViewById(R.id.ptFirst);
        EditText ptLast = findViewById(R.id.ptLast);
        EditText ptEmail = findViewById(R.id.ptEmail);
        EditText ptPassword = findViewById(R.id.ptPassword);
        EditText ptConfirm = findViewById(R.id.ptConfirm);
        EditText ptPhone = findViewById(R.id.ptPhone);
        Button btnCreate = findViewById(R.id.btnCreate);
        bar = findViewById(R.id.pbSignUp);
        mAuth = FirebaseAuth.getInstance();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String First = ptFirst.getText().toString();
                String Last = ptLast.getText().toString();
                String Email = ptEmail.getText().toString();
                String Password = ptPassword.getText().toString();
                String Confirm = ptConfirm.getText().toString();
                String Phone = ptPhone.getText().toString();

                if (First.isEmpty()) {
                    ptFirst.setError("Please enter First Name");
                    ptFirst.requestFocus();
                } else if (Last.isEmpty()) {
                    ptLast.setError("Please enter Last Name");
                    ptLast.requestFocus();
                } else if (Email.isEmpty()) {
                    ptEmail.setError("Please enter valid Email");
                    ptEmail.requestFocus();
                } else if (Password.isEmpty() || Password.length() < 6) {
                    ptPassword.setError("Please set at least six characters");
                    ptPassword.requestFocus();
                } else if (!Password.equals(Confirm)) {
                    ptConfirm.setError("Passwords don't match");
                    ptConfirm.requestFocus();
                } else if (Phone.isEmpty()) {
                    ptPhone.setError("Please enter your mobile number");
                    ptPhone.requestFocus();
                } else {
                    // All fields are valid, proceed with account creation
                    bar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        User user = new User(First, Last, Email, Password, Phone);

                                        FirebaseDatabase.getInstance().getReference("User")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(SignUpActivity.this, "User Registration Successfully!", Toast.LENGTH_LONG).show();
                                                            bar.setVisibility(View.GONE);
                                                            startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
                                                        } else {
                                                            Toast.makeText(SignUpActivity.this, "User Failed to Register", Toast.LENGTH_LONG).show();
                                                            bar.setVisibility(View.GONE);
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "User Failed to Register", Toast.LENGTH_LONG).show();
                                        bar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
            }
        });
    }
}
