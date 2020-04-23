package com.kobid.mygreatapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";


    private EditText mname;
    private EditText memailField;
    private EditText mpasswordField;
    private EditText mpasswordVerificationField;
    private AutoCompleteTextView mcity;
    private Button msignUpBtn;
    private Button mcancelBtn;

    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;


    private static final String [] CITIES=new String[]{
            "","Rishon Lezion","BatYam","Yavne","Rehovot","Binyamina","Yahud"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


    /*    String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "city_list.json");
    Log.i("data", jsonFileString);

    Gson gson = new Gson();
    Type listCitiesTypes = new TypeToken<List<City>>() { }.getType();
    List<City> cities = gson.fromJson(jsonFileString, listCitiesTypes);
    String[] itemsArray = new String[cities.size()];
    itemsArray = cities.toArray(itemsArray);*/



      //  String[] stringArray = cities.toArray(new String[0]);
      /* for (int i = 0; i < cities.size(); i++) {
        Log.i("data", "> Itemmmm " + i + "\n" + cities.get(i));
    }*/






        mAuth = FirebaseAuth.getInstance();

        mname=(EditText) findViewById(R.id.editName);
        memailField=(EditText) findViewById(R.id.emailField);

        mpasswordField=(EditText) findViewById(R.id.passwordField);
        mpasswordVerificationField=(EditText) findViewById(R.id.passwordVerificationField);

        mcity=(AutoCompleteTextView) findViewById(R.id.city);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,CITIES);
        mcity.setAdapter(adapter);

        msignUpBtn=(Button) findViewById(R.id.buttonSignUp);

        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(SignUpActivity.this,AccountActivity.class));
                }
            }
        };

        msignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignUp();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void startSignUp() {
        final String name = mname.getText().toString().trim();
        final String email = memailField.getText().toString().trim();
        final String city = mcity.getText().toString().trim();
        final String password = mpasswordField.getText().toString().trim();
        final String verPassword = mpasswordVerificationField.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)|| TextUtils.isEmpty(verPassword)) {
            Toast.makeText(SignUpActivity.this, "One or more fields are empty", Toast.LENGTH_LONG).show();
        } else if
            (!password.equals(verPassword)) {
            Toast.makeText(SignUpActivity.this, "Both password and verification password should be the same", Toast.LENGTH_LONG).show();
        }else{

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                  FirebaseUser user = mAuth.getCurrentUser();
                                Log.w(TAG,"UserId:" + mAuth.getCurrentUser());
                                    createUserInDb(name,email,city,mAuth.getInstance().getUid());
                            } else {
                                //If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }

                        private void createUserInDb(String name, String email, String city, String currentUser) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            // Add a new document with a generated id.
                            Map<String, Object> data = new HashMap<>();
                            data.put("name", name);
                            data.put("city", city);
                            data.put("email", email);
                            data.put("userid", currentUser);

                            db.collection("Users").document(currentUser)
                                    .set(data);
                                   /* .add(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });*/
                        }
                    });


        }

    }


}