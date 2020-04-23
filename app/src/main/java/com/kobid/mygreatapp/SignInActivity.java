package com.kobid.mygreatapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";


    private EditText mEmailField;
    private EditText mpasswordField;
    public Button mLoginBtn;
    public Button mRegistrationBtn;

    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mAuth = FirebaseAuth.getInstance();

        mEmailField=(EditText) findViewById(R.id.emailField);
        mpasswordField=(EditText) findViewById(R.id.passwordField);
        mLoginBtn=(Button) findViewById(R.id.buttonLogin);
        mRegistrationBtn=(Button) findViewById(R.id.buttonRegistration);

        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(SignInActivity.this,AccountActivity.class));
                }
            }
        };



        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignIn();
            }
        });

        mRegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void startSignIn(){
        String email=mEmailField.getText().toString().trim();
        String password=mpasswordField.getText().toString().trim();


        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(SignInActivity.this,"Fields are empty", Toast.LENGTH_LONG).show();
        }else{

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                    if (!task.isSuccessful()) {
                        Toast.makeText(SignInActivity.this, "Sign In failed", Toast.LENGTH_LONG).show();

                        Log.w(TAG, "signInWithEmail:failed", task.getException());
                    }
                    else {
                        Log.d(TAG, "bbb");
                        startActivity(new Intent(SignInActivity.this,AccountActivity.class));
                    }
                }
            });
        }
    }
}