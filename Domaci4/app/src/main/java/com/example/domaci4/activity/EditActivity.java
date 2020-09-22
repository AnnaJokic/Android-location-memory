package com.example.domaci4.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.domaci4.R;
import com.example.domaci4.model.LocationModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.*;

public class EditActivity extends AppCompatActivity {

    public static String LOCATION_REFERENCE_KEY = "locationIdKey";
    private static final int REQUEST_LOCATION_PERMISSION = 100;

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;

    private DatabaseReference reference;

    EditText lokacija;
    TextView datum;
    EditText opis;
    Button edit_button;
    Button cancelButton;
    private String mLocationReferenceString;
    private LocationModel mLocationModel;
    private ValueEventListener mValueEventListener;
    private static final String TAG = "AddActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_edit2);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reference.removeEventListener(mValueEventListener);
    }

    private void init() {
        parseIntent();
        initUi();
        initFirebase();


    }

    private void parseIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            mLocationReferenceString = intent.getStringExtra(LOCATION_REFERENCE_KEY);

            Log.e(TAG, "parseIntent: " + mLocationReferenceString);
        }
    }

    private void initUi() {
        lokacija=findViewById(R.id.name2);
        datum=findViewById(R.id.datum_edit2);
        opis=findViewById(R.id.lokacija_opis2);
        edit_button=findViewById(R.id.button_edit2);
        cancelButton=findViewById(R.id.button_cancel_edit2);


        MapFragment mapFragment = MapFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.map_main_edit2, mapFragment).commit();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.getUiSettings().setZoomControlsEnabled(true);
                double lat = mLocationModel.getLatitude();
                double lon = mLocationModel.getLongtitue();
                LatLng latlng = new LatLng(lat, lon);
                updateMap(latlng);
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = lokacija.getText().toString();
                String opis1 = opis.getText().toString();
                updateUser(name,opis1);
                Intent returnIntent= new Intent();
                returnIntent.putExtra("Name",1);
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

    }


    private void updateMap(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).title("Here we go"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void initFirebase() {
        reference = FirebaseDatabase.getInstance().getReference(mLocationReferenceString);
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    mLocationModel = dataSnapshot.getValue(LocationModel.class);
                    if (mLocationModel != null) {

                        lokacija.setText(mLocationModel.getName());
                        opis.setText(mLocationModel.getOpis());
                        datum.setText(mLocationModel.getDate());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToast("Something went terribly wrong while fetching user!");
            }
        };
        reference.addValueEventListener(mValueEventListener);
    }

    private void updateUser(String name,String opis) {

        mLocationModel.setName(name);
        mLocationModel.setOpis(opis);
        reference.setValue(mLocationModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast("Update successful");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast("Update failed");
            }
        });

    }

    private void showToast(String message) {
        Toast.makeText(EditActivity.this, message, Toast.LENGTH_LONG).show();
    }




}
