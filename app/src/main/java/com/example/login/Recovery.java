package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Recovery extends AppCompatActivity {
    TextInputEditText textMail;
    TextView textView;
    Button btnRecovery;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);

        textMail = findViewById(R.id.textMailRecovery);
        btnRecovery = findViewById(R.id.recoveryButton);
        textView = findViewById(R.id.loginActivity);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Recovery.this, MainActivity.class);
                finish();
            }
        });
        btnRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = textMail.getText().toString();
                if (mail.isEmpty()) {
                    textMail.setError("Email is required");
                } else {
                    auth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Recovery.this, "Email sent", Toast.LENGTH_SHORT).show();
                                textMail.setText("");
                            } else {
                                Toast.makeText(Recovery.this, "Email not sent", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}