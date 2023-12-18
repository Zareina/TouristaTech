package com.zareinaflameniano.sample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        TextView Forgot = findViewById(R.id.tvForgot);
        TextView Register = findViewById(R.id.tvRegister);
        EditText Username = findViewById(R.id.ptUsername);
        EditText Password = findViewById(R.id.ptPass);
        Button btnLogin = findViewById(R.id.btnLogin);
        ProgressBar Bar = findViewById(R.id.pbLogIn);
        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = Username.getText().toString();
                String password = Password.getText().toString();

                if (username.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                    Username.setError("Please enter email or username");
                    Username.requestFocus();
                } else if (password.isEmpty() || password.length() < 6) {
                    Password.setError("Please enter your Password");
                    Password.requestFocus();
                } else {
                    // All fields are valid, proceed with login
                    btnLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bar.setVisibility(View.VISIBLE);
                            mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LogInActivity.this, "User has successfully logged in!", Toast.LENGTH_LONG).show();
                                        Bar.setVisibility(View.GONE);
                                        startActivity(new Intent(LogInActivity.this, MainActivity.class));
                                    } else {
                                        Toast.makeText(LogInActivity.this, "User failed to log in", Toast.LENGTH_LONG).show();
                                        Bar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                    });
                }

            }
        });
    }

    public void  ForgotClicked(View v){
        startActivity(new Intent(LogInActivity.this , ForgotPasswordActivity.class));
    }

    public void RegisterClicked(View v){
        startActivity(new Intent(LogInActivity.this, SignUpActivity.class ));
    }

}