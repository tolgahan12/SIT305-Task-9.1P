package com.example.passtask9_1;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.passtask9_1.data.DatabaseHelper;
import com.example.passtask9_1.model.Restaurants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class ShowAllRestaurantsMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    DatabaseHelper db;

    ArrayList<String> restaurantArrayList;

    List<Marker> markerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_restaurants_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        restaurantArrayList = new ArrayList<>();

        DatabaseHelper db = new DatabaseHelper(ShowAllRestaurantsMapsActivity.this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        db = new DatabaseHelper(this);

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        markerList = new ArrayList<>();
        List<Restaurants> restaurantList2 = db.fetchAllRestaurants();

        for(Restaurants restaurant :restaurantList2){
            String myInfo = " Title: " + restaurant.getName() + " Latitude: "+ restaurant.getLatitude() + " Longitude: "
                    + restaurant.getLongitude();
            Log.d("myInfo", myInfo);

            markerList.add(mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(restaurant.getLatitude()),Double.parseDouble(restaurant.getLongitude()))).title(restaurant.getName())));
        }


        for(Marker m : markerList){
            // Add a marker in Sydney and move the camera
            LatLng latLng = new LatLng(m.getPosition().latitude, m.getPosition().longitude);
            mMap.addMarker(new MarkerOptions().position(latLng) );
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,8));
        }
    }
}