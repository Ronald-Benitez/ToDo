package com.example.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class sign_up extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    EditText textMail, textPassword;
    TextView login;
    Button logButton;
    ImageButton googleButton;

    FirebaseAuth firebaseAuth;
    AwesomeValidation awesomeValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        //Initialize Awesome Validation
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.textMail, Patterns.EMAIL_ADDRESS, R.string.invalid_email);
        awesomeValidation.addValidation(this, R.id.textPassword, ".{6,}", R.string.invalid_password);

        //Initialize Views
        login = findViewById(R.id.textLogIn);
        textMail = findViewById(R.id.textMail);
        textPassword = findViewById(R.id.textPassword);

        //Initialize Buttons
        logButton = findViewById(R.id.signButton);
        googleButton = findViewById(R.id.googleButton2);

        //Set OnClickListener for Login Button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sign_up.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //Set OnClickListener for Sign Up Button
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (awesomeValidation.validate()) {
                    //Create User
                    firebaseAuth.createUserWithEmailAndPassword(textMail.getText().toString(), textPassword.getText().toString())
                            .addOnCompleteListener(sign_up.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(sign_up.this, "User Created", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(sign_up.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(sign_up.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}