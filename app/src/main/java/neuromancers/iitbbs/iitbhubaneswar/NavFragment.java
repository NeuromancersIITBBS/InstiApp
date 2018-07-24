package neuromancers.iitbbs.iitbhubaneswar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class NavFragment extends Fragment {

    private int navLayout;
    private MapView mapView = null;
    private GoogleMap googleMap = null;

    public void setNewLayout(int navLayout) {
        this.navLayout = navLayout;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = layoutInflater.inflate(navLayout, container, false);

        if (navLayout == R.layout.map) {
            mapView = (MapView) rootView.findViewById(R.id.map_layout);
            mapView.onCreate(savedInstanceState);

            mapView.onResume();

            mapView.getMapAsync(new OnMapReadyCallback() {
                @SuppressLint("MissingPermission")
                @Override
                public void onMapReady(GoogleMap mMap) {
                    googleMap = mMap;

                    // For showing a move to my location button

                    // For dropping a marker at a point on the Map
                    LatLng iitbbs = new LatLng(20.148352, 85.671158);
//                    googleMap.addMarker(new MarkerOptions().position(iitbbs).title("IIT Bhubaneswar - Main Building").snippet("Academic Block"));

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(iitbbs).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    // Create a LatLngBounds that includes the city of Adelaide in Australia.
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

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED ||
                                checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED) {
                            // Permission is not granted
                        } else {
                            googleMap.setMyLocationEnabled(true);
//                          googleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
//                        @Override
//                        public void onMyLocationClick(@NonNull Location location) {
//
//                        }
//                    });
                        }
                    } else {
                        googleMap.setMyLocationEnabled(true);
//                          googleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
//                        @Override
//                        public void onMyLocationClick(@NonNull Location location) {
//
//                        }
//                    });
                    }
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
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }
}
