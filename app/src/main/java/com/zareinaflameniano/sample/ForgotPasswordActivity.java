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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText eTEmail;
    ProgressBar pbReset;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        eTEmail = findViewById(R.id.eTEnterEmail);
        Button btnReset = findViewById(R.id.btnReset);
        pbReset = findViewById(R.id.pbReset);
        mAuth = FirebaseAuth.getInstance();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tEmail = eTEmail.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(tEmail).matches()){
                    eTEmail.setError("Please enter valid Email");
                    eTEmail.requestFocus();
                    return;
                }
                pbReset.setVisibility(View.VISIBLE);
                mAuth.sendPasswordResetEmail(tEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotPasswordActivity.this, "Please check your Email to Reset Password",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ForgotPasswordActivity.this, LogInActivity.class));
                            pbReset.setVisibility(View.GONE);
                        }
                        else {
                            Toast.makeText(ForgotPasswordActivity.this, "Failed to Reset Password", Toast.LENGTH_LONG).show();
                            pbReset.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}