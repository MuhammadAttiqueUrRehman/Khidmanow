package com.aic.khidmanow;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class TemporaryPlacesActivity extends AppCompatActivity implements AsyncResponse, OnMapReadyCallback, LocationListener {
    FusedLocationProviderClient fusedLocationProviderClient;
    EditText txtaddress;
    Location l = null;
    PlacesClient placesClient;
    HMCoreData myDB;
    IntroManager intomanager;
    String cityg, addressg, latlngu;
    ProgressBar progressBar20;
    GoogleMap mMap;
    Button btnset;
    LocationManager mlocationManager;
    String newaddress = "";
    Boolean maponce=false;
    private HMAppVariables hmAppVariables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDB = new HMCoreData(this);
        intomanager = new IntroManager(this);
        try {
            hmAppVariables = myDB.getUserData();
        } catch (HMOwnException e) {
            throw new RuntimeException(e);
        }
        setContentView(R.layout.activity_temporary_places);
        txtaddress = findViewById(R.id.txtaddress);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.maps_key));
        }

        placesClient = Places.createClient(getApplicationContext());
        final AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        List<Place.Field> fieldList = Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME);
        autocompleteSupportFragment.setPlaceFields(fieldList);
        progressBar20 = findViewById(R.id.progressBar20);
        btnset = findViewById(R.id.btnnext);


        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.i("Debugging", "Searching Location Place-------------------------------->");
                final LatLng latlng = place.getLatLng();
                maponce=false;
                if (!maponce) {
                    performAddressChange(latlng.latitude, latlng.longitude);
                    latlngu = latlng.latitude + "," + latlng.longitude;
                }
                Log.i("MENTOR", "Latitude:" + latlng.latitude + " Longitude:" + latlng.longitude);
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });


        btnset.setOnClickListener(v -> {


            JSONObject jsonParam = new JSONObject();
            JSONObject jsonMain = new JSONObject();
            try {
                jsonMain.put("merchantcode", HMConstants.appmerchantid);
                jsonMain.put("mdevice", intomanager.getDevice());
                jsonMain.put("geolocation", latlngu);
                jsonParam.put("indata", jsonMain);
            } catch (JSONException e) {
                e.printStackTrace();
                myDB.showToast(this, e.getMessage());
                return;
            }
            String[] myTaskParams = {"/SSMFetchCity", jsonParam.toString()};
            Log.i("Debugging ", "Fetch City  " + jsonParam);
            //  new HMDataAccess().execute(myTaskParams);
            try {
                HMDataAccess aasyncTask = new HMDataAccess(this, progressBar20, "SSMFetchCity");
                aasyncTask.delegate = (AsyncResponse) TemporaryPlacesActivity.this;
                aasyncTask.execute(myTaskParams);
            } catch (Exception e) {
                e.printStackTrace();
                myDB.showToast(this, e.getMessage());
                return;
            }
        });


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fm.getMapAsync(this);

    }

    private void fetchLastLocation() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    l = location;
                    Log.d("GEOLOCATION","Fetch Location onSuccess: "+ l.getLatitude()+" "+l.getLongitude());
                    performAddressChange(l.getLatitude(), l.getLongitude());
                }
            }
        });
    }


    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        Log.i("Debugging", "Fetchcity while firsttime  " + output);

        JSONObject myjson = new JSONObject(output);

        if (!myjson.getString("statuscode").equals("000")) {
            myDB.showToast(getApplicationContext(), myjson.getString("statusdesc"));
            return;
        } else {
            try {
                mlocationManager.removeUpdates(this);
                mlocationManager = null;
            } catch (Exception e) { }

            intomanager.setGeoLocationT(latlngu);
            intomanager.setAddressLandMarkT(txtaddress.getText().toString());



            Log.d("GEOLOCATION","Places geolocation: "+latlngu);


            finish();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            mlocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            mMap.setOnCameraMoveStartedListener(reason -> {
                Log.d("GEOLOCATION","onMapReady CameraMove: "+ mMap.getCameraPosition().target.latitude+" "+mMap.getCameraPosition().target.longitude);
                if (mMap.getCameraPosition().target.latitude != 0.0 && mMap.getCameraPosition().target.longitude!=0.0){
                    performAddressChange(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude);
                }
            });


            mMap.setOnCameraIdleListener(() -> {

            });

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mlocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


            mMap.setOnCameraIdleListener(() -> Log.i("GEOLOCATION", "Running Second Camera"));


            mMap.setOnCameraMoveStartedListener(reason -> {
                Log.d("GEOLOCATION","onCameraMoveStart: "+ mMap.getCameraPosition().target.latitude+" "+mMap.getCameraPosition().target.longitude);
                performAddressChange(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude);
                newaddress = "";
                mlocationManager.removeUpdates(TemporaryPlacesActivity.this);
            });

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fetchLastLocation();
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            fm.getMapAsync(this);

              /*  LatLng sydney = new LatLng(mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.0f));
                performAddressChange(mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude);*/
        }

        else {

        }
    }


    private LatLng getLatLngFromAddress(String address){

        Geocoder geocoder=new Geocoder(TemporaryPlacesActivity.this);
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(address, 1);
            if(addressList!=null){
                Address singleaddress=addressList.get(0);
                LatLng latLng=new LatLng(singleaddress.getLatitude(),singleaddress.getLongitude());
                return latLng;
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    private Address getAddressFromLatLng(LatLng latLng){
        Geocoder geocoder=new Geocoder(TemporaryPlacesActivity.this);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
            if(addresses!=null){
                Address address=addresses.get(0);
                return address;
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }


    public void performAddressChange(Double lat, Double lon) {
        try {
            maponce=true;
            latlngu = lat + "," + lon;
            Log.i("GEOLOCATION", "New Location----------->" + latlngu);
        } catch (Exception e) {
            myDB.showToast(getApplicationContext(), "Invalid location select. Please try again" + latlngu);
            return;
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15.0f));
        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses.size() > 0) {
                addressg = addresses.get(0).getAddressLine(0);
                for (Address adr : addresses) {
                    if (adr.getLocality() != null && adr.getLocality().length() > 0) {
                        addressg = adr.getAddressLine(0);
                        cityg = adr.getLocality();

                        break;
                    }
                }
                Log.i("Debugging", "Address  while Geocode " + addressg);
                Log.i("Debugging", "Geolocation while Geocoder  " + cityg);
            }
            //cityg = addresses.get(0).getAddressLine(1);

            txtaddress.setText(addressg);
            newaddress = addressg;
        } catch (Exception e) {
            myDB.showToast(getApplicationContext(), "Invalid location select. Please try again");
            return;
        }

        //    }

    }

    @Override
    public void onLocationChanged(Location location) {
        //      performAddressChange(location.getLatitude(),location.getLongitude());
        LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.0f));
        if (!maponce) {
            Log.d("GEOLOCATION","Location Changed: "+ location.getLatitude()+" "+location.getLongitude());
            performAddressChange(location.getLatitude(), location.getLongitude());
        }


        Log.i("Debugging", "Running Onloaction changed");
        mlocationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("Debugging", "Running onStatusChanged changed");
        try {
            mlocationManager.removeUpdates(this);
            mlocationManager = null;
        } catch (Exception e) {
//             //
        }

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("Debugging", "Running onProviderEnabled changed");
        try {
            mlocationManager.removeUpdates(this);
            mlocationManager = null;
        } catch (Exception e) {
//             //
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("Debugging", "Running onProviderDisabled changed");
        try {
            mlocationManager.removeUpdates(this);
            mlocationManager = null;
        } catch (Exception e) {
//             //
        }

    }

}