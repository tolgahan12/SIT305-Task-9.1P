package com.example.passtask9_1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passtask9_1.data.DatabaseHelper;
import com.example.passtask9_1.model.Restaurants;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class AddANewPlaceActivity extends AppCompatActivity {

    private static final String TAG = "Running ";

    DatabaseHelper db;

    String placeLat = "-34";
    String placeLng = "211";

    String currentLat;
    String currentLng;


    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_new_place);

        TextView placeName = findViewById(R.id.placeNameTextView);
        Button getCurrentLocationButton = findViewById(R.id.getCurrentLocationButton);
        Button showOnMapButton = findViewById(R.id.showOnMapButton);
        Button saveButton = findViewById(R.id.saveButton);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        db = new DatabaseHelper(this);
        //---------------------------------------------------------------------

        //Auto complete
        // Initialize the SDK
        Places.initialize(getApplicationContext(), getString(R.string.Places_API));

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() +  ", " + place.getLatLng());

                //Get latlong together
                LatLng destinationLatLng = place.getLatLng();
                //Now extra them separately into double then string to send via intent
                Double placeLatt = destinationLatLng.latitude;
                Double placeLngg = destinationLatLng.longitude;

                placeLat = placeLatt.toString();
                placeLng = placeLngg.toString();

                //Toast.makeText(AddANewPlaceActivity.this, placeLat, Toast.LENGTH_SHORT).show();
                //Toast.makeText(AddANewPlaceActivity.this, placeLng, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        //----------------------------------------------------------------------------------------------------------------------


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Double cLat = location.getLatitude();
                Double cLng = location.getLongitude();

                currentLat = cLat.toString();
                currentLng = cLng.toString();
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }

        //---------------------------------------------------------------------------------------------------------------------
        //Buttons
        getCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If user wants current location then only we will store the lat and long
                placeLat = currentLat;
                placeLng = currentLng;
                // Set search bar to current location coordinates
                autocompleteFragment.setText(placeLat + ", " + placeLng);

            }
        });

        showOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Send to map activity
                Intent showOnMapIntent = new Intent(AddANewPlaceActivity.this, ShowOneRestarurantMapsActivity.class);
                //Send Lat and Long to next activity
                showOnMapIntent.putExtra("lat", placeLat);
                showOnMapIntent.putExtra("lng", placeLng);

                startActivity(showOnMapIntent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String givenName = placeName.getText().toString();

                Restaurants restaurant = new Restaurants(givenName, placeLat.toString(), placeLng.toString());

                //long result = db.insertUser(new Notes(note));
                long result = db.insertRestaurant(restaurant);

                if (result > 0)
                {
                    Toast.makeText(AddANewPlaceActivity.this, "Restaurant stored successfully!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(AddANewPlaceActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}