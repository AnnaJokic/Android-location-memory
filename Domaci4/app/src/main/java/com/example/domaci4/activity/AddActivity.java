package com.example.domaci4.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.domaci4.R;
import com.example.domaci4.model.LocationModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    private DatabaseReference reference;


    private static final int REQUEST_LOCATION_PERMISSION = 100;

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;


    EditText lokacija;
    TextView datum;
    EditText opis;
    Button addButton;
    Button cancelButton;
    private LocationModel mLocationModel;
    LatLng latLng;
    double latitude;
    double longitude;
    private ValueEventListener mValueEventListener;
    private static final String TAG = "AddActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add7);
        init();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        initUi();
        initFirebase();
        initLocationClient();
    }

    private void initLocationClient() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }


    private void initUi() {
        lokacija=findViewById(R.id.add_activity_lokacija);
        datum=findViewById(R.id.add_activity_datum);
        String date1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        datum.setText(date1);
        opis=findViewById(R.id.add_activity_opis);
        addButton=findViewById(R.id.add_activity_store);
        cancelButton=findViewById(R.id.add_activity_exit);

        MapFragment mapFragment = MapFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.map_add, mapFragment).commit();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.getUiSettings().setZoomControlsEnabled(true);
                getLocation();
            }
        });



        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String ime=lokacija.getText().toString();
               String opis1=opis.getText().toString();
               String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


                LocationModel lokacija=new LocationModel();
                lokacija.setName(ime);
                lokacija.setDate(date);
                lokacija.setOpis(opis1);
                lokacija.setLatitude(latitude);
                lokacija.setLongtitue(longitude);


              handleAddButtonClick(lokacija);
            }
        });
    }



    private void updateMap(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).title("Here we go"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        if (!hasAnyFeature(PackageManager.FEATURE_LOCATION, PackageManager.FEATURE_LOCATION_GPS, PackageManager.FEATURE_LOCATION_NETWORK)) {
            showToast("No feature");
            return;
        }

        if (hasPermissions(Manifest.permission.ACCESS_FINE_LOCATION)) {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(Task<Location> task) {
                            if (task.isSuccessful()) {
                               Location location = task.getResult();
                                 latitude = location.getLatitude();
                                 longitude = location.getLongitude();
                                latLng = new LatLng(latitude, longitude);
                                updateMap(latLng);
                            } else {
                                showToast("Something went wrong");
                            }
                        }
                    });
        } else {
            requestPermissions(REQUEST_LOCATION_PERMISSION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    showToast("Permission not granted");
                }
        }
    }

    protected boolean hasAnyFeature(String... features){
        for (String feature : features) {
            if (getPackageManager().hasSystemFeature(feature)){
                return true;
            }
        }
        return false;
    }

    protected boolean hasPermissions(String... permissions){
        for (String permission : permissions) {
            boolean hasPermission = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
            if(!hasPermission) {
                return false;
            }
        }
        return true;
    }

    protected void requestPermissions(int requestCode, String... permissions){
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void initFirebase() {
        reference = FirebaseDatabase.getInstance().getReference().child("locations");

    }

    private void handleAddButtonClick(LocationModel location) {
        reference.push().setValue(location);
        finish();
    }

}

