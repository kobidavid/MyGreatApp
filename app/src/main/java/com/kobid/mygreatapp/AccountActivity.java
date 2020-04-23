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
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class AccountActivity extends AppCompatActivity{

    private static final String TAG = "AccountActivity";
    private Button mLogOutBtn;
    private Button mCreateNeedBtn;
    private Button mMainActivity;
    private ImageView imageView;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    //private TextView mtextView;
    private TextView[] swSingapore;
    private LinearLayout linearLayout;

    //RecyclerView
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<ExampleItem> requestsArrayList;

    List<String> userIdList = new ArrayList<String>();



   /* List<String> centerArr = new ArrayList<String>();
    List<String> northArr = new ArrayList<String>();
    List<String> southArr = new ArrayList<String>();*/

// ...


    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    TextView latTextView, lonTextView, myUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mLogOutBtn = (Button) findViewById(R.id.buttonLogout);
        mCreateNeedBtn = (Button) findViewById(R.id.buttonCreateNeedYouRequest);

        myUserId= (TextView) findViewById(R.id.myUserId);
        myUserId.setText(mAuth.getInstance().getUid());

        mCreateNeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mAuthListener = new FirebaseAuth.AuthStateListener() {
                //@Override
                //public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //if (firebaseAuth.getCurrentUser() == null) {
                startActivity(new Intent(AccountActivity.this, CreateActivity.class));
                //}
            }
            //};
            //}
        });

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true); //too heavy
        mLayoutManager = new LinearLayoutManager(this);
        //mAdapter = new ExampleAdapter(exampleItem);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView3);

       /* Glide.with(this).load("https://i.picsum.photos/id/572/300/300.jpg")
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);*/

        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);//zero can be replaced with any action code (called requestCode)


        mLogOutBtn.setOnClickListener(new View.OnClickListener() {
            private static final String TAG = "logout";

            @Override
            public void onClick(View view) {
                startLogOut();
            }

            private void startLogOut() {
                FirebaseAuth.getInstance().signOut();
                finish();
                Log.w(TAG, "signInWithEmail:failed");

            }
        });

        userIdList.add("EXVY4ED5LiUid7ofDtdqTpDOOsl1");
        userIdList.add("G4qFCGxPePTxw2JNKIEd0w8UutZ2");
        userIdList.add("4FEdlFHZ8xat4hBsmS6j7LP1ilu2");
        userIdList.add("DxbZ5sTXaAfAwGU0bNzZQ69Fog72");





        mAuth = FirebaseAuth.getInstance();
        db.collection("Requests")
                //.whereEqualTo("userId","4FEdlFHZ8xat4hBsmS6j7LP1ilu2")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        requestsArrayList = new ArrayList<>();
                        mAuth = FirebaseAuth.getInstance();

                        for (QueryDocumentSnapshot doc : value) {
                            Log.w(TAG, "kkk, "+ mAuth.getUid() +", "+firebaseUser.getUid()+", "+ userIdList.get(3));

                            if (doc.get("userId") != null) {
                                Log.w(TAG, "Doc_Id: " + doc.getId());
                                ExampleItem exampleItem = new ExampleItem(doc.getString("needTitle"), doc.getString("needDescription"), doc.getId());
                                requestsArrayList.add(exampleItem);

                            }
                            Log.d(TAG, "Current cites in CA: " + doc.get("needCity"));
                        }

                        mAdapter = new ExampleAdapter(requestsArrayList, new ExampleAdapter.OnItemClickListener() {
                            @Override
                            public void onDeleteClick(ExampleItem item) {

                                db.collection("Requests").document(item.getmDocId())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully deleted! ");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error deleting document", e);
                                            }
                                        });
                            }
                        });
                        mRecyclerView.setAdapter(mAdapter);
                        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
                        itemTouchHelper.attachToRecyclerView(mRecyclerView);
                    }
                    ItemTouchHelper.SimpleCallback simpleCallback= new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                        @Override
                        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                            return false;
                        }


                        @Override
                        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                            int position=viewHolder.getAdapterPosition();

                            switch (direction){
                                case ItemTouchHelper.LEFT:
                                    Log.w("mytag","getItemId"+ viewHolder.getAdapterPosition());

                                    break;
                                case ItemTouchHelper.RIGHT:
                                    break;
                            }
                        }
                    };
                });

        DocumentReference userProfile = db.collection("Users").document(mAuth.getUid());

        userProfile.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    String img_url = (String) doc.get("image");
                    Glide.with(getApplicationContext())
                            .load(img_url)
                            .apply(RequestOptions.circleCropTransform())
                            .into(imageView);
                }
            }
        });



        latTextView = findViewById(R.id.latTextView);
        lonTextView = findViewById(R.id.lonTextView);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();

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
                                    latTextView.setText(location.getLatitude()+"");
                                    lonTextView.setText(location.getLongitude()+"");
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