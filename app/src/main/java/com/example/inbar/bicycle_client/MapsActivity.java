package com.example.inbar.bicycle_client;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity  implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener{

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private StationsActivity stationsActivity;
    private  PlacesManager placesManager;
    private DrawerLayout mDrawerLayout;
    private NavigationView nv;
    private List<SpecificStation> stationsList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        stationsActivity = new StationsActivity();
        initNavigation();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(currentPosition).title("Your location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
            return;
        } else {
            configure();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

        }
    }

    @SuppressLint("MissingPermission")
    private void configure() {
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
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
    public void onMapReady(GoogleMap googleMap) {/// show on map
        mMap = googleMap;
        LatLng stationPosition1=null;
        String detailsStation="";

        //get user location from gps
        Location userLocation = getCurrentLocationOfUser();
         placesManager = new PlacesManager();

        try { //   retern the Tel Ofan stastion
            stationsList = stationsActivity.getStationsList(Double.toString(userLocation.getLongitude()),Double.toString(userLocation.getLatitude()));

            if(stationsList!= null && stationsList.size() != 0) {

                 stationPosition1 = new LatLng(stationsList.get(0).getLatitude(), stationsList.get(0).getLongitude());
                 detailsStation = String.format("Address: %s, Bicycles Available: %s", stationsList.get(0).getAddress(),stationsList.get(0).getNumOfBicyclesAvailable());

            }

            ArrayList<Place> placesArrayList = placesManager.LoadLocations(getIntent().getStringExtra("userOptions"),userLocation.getLongitude(),userLocation.getLatitude(),mMap,stationPosition1);


        // show the stations on the map
        mMap.addMarker(new MarkerOptions().position(stationPosition1).title(stationsList.get(0).getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.bi)).snippet(detailsStation));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(stationPosition1));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        LatLng yourLocation = new LatLng( userLocation.getLatitude(),userLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(yourLocation).title("Your Location"));


        for (Place place: placesArrayList) {//present places  icons on the map
            LatLng position = new LatLng(place.lat,place.lng );
            mMap.addMarker(new MarkerOptions().position(position).title(place.name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }

        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                    return false;
            }
        });



        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.showInfoWindow();
    }

    public void buttonOpenMenu_onClick(View view) {

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    //function that get user location from gps
    private Location getCurrentLocationOfUser(){
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        Location userLocation = locationManager.getLastKnownLocation(locationProvider);
        return userLocation;
    }

    private void initNavigation() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);

        nv = (NavigationView)findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(navSelectListener);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                myToolbar,
                R.string.drawer_open,
                R.string.drawer_close){
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
    }

    private void markTelOfunStationByCurrentLocationOnMap(boolean isToReturn) throws ExecutionException, InterruptedException{

        Location userLocation = getCurrentLocationOfUser();
        stationsList = stationsActivity.getStationsList(Double.toString(userLocation.getLongitude()),Double.toString(userLocation.getLatitude()));
        LatLng stationPosition1 = new LatLng(stationsList.get(0).getLatitude(), stationsList.get(0).getLongitude());
        String detailsStation;
        if(isToReturn)
            detailsStation = String.format("Address: %s, Available Docks: %s", stationsList.get(0).getAddress(), stationsList.get(0).getNumOfPolesAvailable());
       else
            detailsStation = String.format("Address: %s, Bicycles Available: %s", stationsList.get(0).getAddress(), stationsList.get(0).getNumOfBicyclesAvailable());

        mMap.addMarker(new MarkerOptions().position(stationPosition1).title(stationsList.get(0).getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.bi)).snippet(detailsStation));
    }

    private final NavigationView.OnNavigationItemSelectedListener navSelectListener = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            String selectedItem = (String) item.getTitle();
            item.setChecked(true);
            mDrawerLayout.closeDrawer(nv);
            try {
                if (selectedItem.equals("Find a bike"))
                    markTelOfunStationByCurrentLocationOnMap(false);
                else if (selectedItem.equals("Return the bike")) {
                    markTelOfunStationByCurrentLocationOnMap(true);
                } else if (selectedItem.equals("Find a new route")) {
                    startActivity(new Intent(MapsActivity.this, OptionMenu.class));
                    mDrawerLayout.closeDrawers();
                    finish();
                    return true;
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            item.setChecked(true);
            mDrawerLayout.closeDrawer(nv);
            return true;
        }
    };
}
