package iitbbs.iitbhubaneswar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class NavFragment extends Fragment {

    private int navLayout;
    private MapView mapView = null;
    private GoogleMap googleMap = null;

    LocationRequest locationRequest;
    Location lastLocation;

    private FusedLocationProviderClient fusedLocationProviderClient;

    public void setNewLayout(int navLayout) {
        this.navLayout = navLayout;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = layoutInflater.inflate(navLayout, container, false);

        if (navLayout == iitbbs.iitbhubaneswar.R.layout.gymkhana) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("gymkhana_office_bearers/");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<String> officeBearersStringList = new ArrayList<>();
//                    Iterable<DataSnapshot> officeBearersObjectList = dataSnapshot.getChildren();
//                    for (DataSnapshot child : officeBearersObjectList) {
//                        officeBearersStringList.add(child.getKey());
//                    }

                    officeBearersStringList.addAll(Arrays.asList(new String[]{
                            "president", "treasurer", "vpresident","fasng", "fasnt",
                            "fasnc", "gsecsports", "gsecsnt", "gseccul",
                            "secysnt", "secyweb", "secyrobotics", "photosoc", "secyprogsoc",
                            "secysfs", "secylitsoc", "secycinesoc", "secymnd", "secyfinearts",
                            "secydrams", "secyfootball", "secyathletics", "secyindoor",
                            "secysasports", "secycricket"
                    }));

                    RecyclerView recyclerView = (RecyclerView) rootView.findViewById(iitbbs.iitbhubaneswar.R.id.office_bearers_recycler_view);
                    GymkhanaAdapter gymkhanaAdapter = new GymkhanaAdapter(officeBearersStringList, rootView.getContext());
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(gymkhanaAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        if (navLayout == iitbbs.iitbhubaneswar.R.layout.map) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

            mapView = (MapView) rootView.findViewById(iitbbs.iitbhubaneswar.R.id.map_layout);
            mapView.onCreate(savedInstanceState);

            mapView.onResume();

            mapView.getMapAsync(new OnMapReadyCallback() {
                @SuppressLint({"MissingPermission", "RestrictedApi"})
                @Override
                public void onMapReady(GoogleMap mMap) {
                    googleMap = mMap;

                    LatLng iitbbs = new LatLng(20.148352, 85.671158);
//                    googleMap.addMarker(new MarkerOptions().position(iitbbs).title("IIT Bhubaneswar - Main Building").snippet("Academic Block"));
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(iitbbs).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    LatLngBounds iitbbs_campus = new LatLngBounds(new LatLng(20.138375, 85.656087),
                            new LatLng(20.156808, 85.684665));
                    mMap.setLatLngBoundsForCameraTarget(iitbbs_campus);

                    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    googleMap.setBuildingsEnabled(true);
                    googleMap.setMinZoomPreference(14);
                    googleMap.setMaxZoomPreference(17);
                    googleMap.setTrafficEnabled(true);

                    UiSettings uiSettings = googleMap.getUiSettings();
                    uiSettings.setZoomControlsEnabled(true);
                    uiSettings.setAllGesturesEnabled(true);
                    uiSettings.setMyLocationButtonEnabled(true);

                    locationRequest = new LocationRequest();
                    locationRequest.setInterval(120000); // two minute interval
                    locationRequest.setFastestInterval(120000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED &&
                                checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                        == PackageManager.PERMISSION_GRANTED) {
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                            googleMap.setMyLocationEnabled(true);
                        } else {
                            checkLocationPermission();
                        }
                    } else {
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        googleMap.setMyLocationEnabled(true);
                    }

                    googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                        @Override
                        public boolean onMyLocationButtonClick() {
                            LocationManager locationManager = null;
                            boolean gps_enabled = false, network_enabled = false;

                            if (locationManager == null)
                                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                            try {
                                gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                            } catch (Exception ex) {
                            }

                            try {
                                network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                            } catch (Exception ex) {
                            }

                            if (!gps_enabled)
                                Snackbar.make(getView(), "Enable Location Services", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            else if (!network_enabled)
                                Snackbar.make(getView(), "Enable Network Services", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                            return false;
                        }
                    });
                }
            });
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
        if (mapView != null) {
            mapView.onPause();
        }

        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
        if (mapView != null) {
            mapView.onDestroy();
        }

        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                lastLocation = location;

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("getContext() app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions((Activity) getActivity(),
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions((Activity) getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        googleMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
