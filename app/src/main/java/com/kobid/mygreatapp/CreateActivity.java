package com.kobid.mygreatapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateActivity extends AppCompatActivity {

    private static final String TAG = "CreateActivity";
    private EditText needTitle;
    private EditText needDescription;
    private AutoCompleteTextView needCity;
    private Button needCreateBtn;
    private Button needCancleBtn;
    List<String> centerArr = new ArrayList<String>();
    List<String> northrArr = new ArrayList<String>();
    List<String> southrArr = new ArrayList<String>();
    private static int reqIdCounter = 0;
    private static final String[] CITIES = new String[]{
            "Holon", "Rishon", "BatYam", "Yavne", "Rehovot", "Binyamina", "Yahud"
    };
    FirebaseAuth firebaseAuth;
    FirebaseAuth mAuth;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    TextView latTextView, lonTextView, myUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        needCity = (AutoCompleteTextView) findViewById(R.id.editArea);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, CITIES);
        needCity.setAdapter(adapter);

        needTitle = (EditText) findViewById(R.id.needTitleField);
        needDescription = (EditText) findViewById(R.id.NeedDescriptionField);

        needCreateBtn = (Button) findViewById(R.id.buttonCreateNeed);
        needCancleBtn = (Button) findViewById(R.id.buttonCancel);

        needCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCreateNeed();
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();
    }

    private void startCreateNeed() {
        // String userId=firebaseAuth.getUid().toString();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // userId, email
        final String userId = firebaseUser.getUid();
        String email = firebaseUser.getEmail();
        reqIdCounter = reqIdCounter++;
        final Map<String, Object> needsList = new HashMap<>();
        needsList.put("userId", userId.toString());
        needsList.put("needTitle", needTitle.getText().toString());
        needsList.put("needDescription", needDescription.getText().toString());
        needsList.put("needCity", needCity.getText().toString());
        needsList.put("status", "opened");
        needsList.put("requestid", reqIdCounter);
        needsList.put("loc","22");

        for (int i = 0; i < CITIES.length; i++) {

        }

        db.collection("Requests")
                .add(needsList)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.w(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        Requests requests = new Requests(documentReference.getId(), userId, needTitle.getText().toString(), needDescription.getText().toString(), needCity.getText().toString());
                        Log.w(TAG, "DocumentSnapshot written with ID: " + documentReference.getId() + "," + userId + "," + needTitle.getText().toString() + "," + needDescription.getText().toString() + "," + needCity.getText().toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        finish();
    }






    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    //latTextView.setText(location.getLatitude()+"");
                                    //lonTextView.setText(location.getLongitude()+"");
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latTextView.setText(mLastLocation.getLatitude()+"");
            lonTextView.setText(mLastLocation.getLongitude()+"");
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }

}




