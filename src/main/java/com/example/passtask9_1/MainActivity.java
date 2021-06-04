package com.example.passtask9_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addNewPlace = findViewById(R.id.addNewPlaceButton);
        Button showAllOnMap = findViewById(R.id.showAllOnMapButton);

        addNewPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Create new", Toast.LENGTH_SHORT).show();

                Intent addNewPlaceIntent = new Intent(MainActivity.this, AddANewPlaceActivity.class);
                startActivity(addNewPlaceIntent);
            }
        });

        showAllOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Show all", Toast.LENGTH_SHORT).show();

                Intent showAllIntent = new Intent(MainActivity.this, ShowAllRestaurantsMapsActivity.class);
                startActivity(showAllIntent);
            }
        });


    }

}