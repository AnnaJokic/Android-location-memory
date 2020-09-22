package com.example.domaci4.activity;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.domaci4.R;
import com.example.domaci4.adapter.LocationAdapter;
import com.example.domaci4.model.LocationModel;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private LocationAdapter mAdapter;
    private DatabaseReference mLocationReference;

    private LinearLayoutManager mLinearLayoutManager;

    private ValueEventListener mValueEventListener;
    private ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        initFirebase();
        initUi();
    }

    private void initUi() {
        RecyclerView lista = findViewById(R.id.rv_main_lista);
        ImageView add=findViewById(R.id.add);
        mLinearLayoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(mLinearLayoutManager);
        mAdapter = new LocationAdapter();
        mAdapter.setOnLocationClickedListener(new LocationAdapter.OnLocationClickedListener() {
            @Override
            public void onLocationMore(LocationModel locationModel) {
                Log.e(TAG, "onLocationEdit: " + locationModel.toString());
                Intent intent = new Intent(MainActivity.this, MoreActivity.class);
                String locationReferenceString = "locations/" + locationModel.getId();
                intent.putExtra(MoreActivity.LOCATION_REFERENCE_KEY, locationReferenceString);
                startActivity(intent);
            }
        });


        lista.setAdapter(mAdapter);
        add.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);


        });
    }

    private void initFirebase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mLocationReference = firebaseDatabase.getReference().child("locations");
        createAndAddListeners();
    }

    private void createAndAddListeners() {
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, "onDataChange: " + dataSnapshot.toString());
                List<LocationModel> locationModels = new ArrayList<>();

                if (dataSnapshot.getValue() == null) {
                    return;
                }

                for (DataSnapshot childDataSnapshot:dataSnapshot.getChildren()) {
                    LocationModel location = childDataSnapshot.getValue(LocationModel.class);
                    // Since each employee is identified by key in the realtime db,
                    // we get it as a part od data snapshot object, and not inside of object itself.
                    // We want to add it to the object when we get employee objects from the server.
                    // Why? Because we have to uniqly identify objects in DiffUtil, and we want to
                    // use key to get mEmployeeReference to the object when we want to delete it from the db.
                    String key = childDataSnapshot.getKey();
                    location.setId(key);
                    locationModels.add(location);
                }
                mAdapter.setData(locationModels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

       mLocationReference.addValueEventListener(mValueEventListener);

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.e(TAG, "onChildAdded: " + dataSnapshot.toString());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, "onChildRemoved: " + dataSnapshot.toString());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mLocationReference.addChildEventListener(mChildEventListener);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationReference.removeEventListener(mValueEventListener); }
}


