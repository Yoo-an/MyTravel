package com.example.mytravel;

import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.mytravel.db.AppDatabase;
import com.example.mytravel.db.TravelDAO;
import com.example.mytravel.models.Travel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geoCoder;
    public static String EXTRA_TRAVEL_ID = "travel_id";
    Travel travel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        TravelDAO mTravelDAO = Room.databaseBuilder(this, AppDatabase.class, "db-contacts").allowMainThreadQueries().build().getTravelDAO();
        travel = mTravelDAO.getTravelWithId(getIntent().getIntExtra(EXTRA_TRAVEL_ID, -1));


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        geoCoder = new Geocoder(this);
        String location;
        String city;
        String country;

        country = travel.getCountry();
        city = travel.getCity();
        location = city+","+country;
        Log.i("abc",location);

        List<Address> addressList = null;
        try {
            // convert addreess through geocoder
            addressList = geoCoder.getFromLocationName(location, 10); // 최대 검색 결과 개수
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.e("abc","geocoder error");
        }

        //create latitude, longitude
        double latitude = addressList.get(0).getLatitude();
        double longitude = addressList.get(0).getLongitude();
        System.out.println(latitude);
        System.out.println(longitude);

        LatLng point = new LatLng(latitude,longitude);
        // create marker
        MarkerOptions mOptions2 = new MarkerOptions();
        mOptions2.title(location);
        mOptions2.position(point);
        //add Marker
        mMap.addMarker(mOptions2);
        // move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,10));

    }
}
