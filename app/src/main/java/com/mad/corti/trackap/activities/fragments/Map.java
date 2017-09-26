package com.mad.corti.trackap.activities.fragments;

/*
 * Trackap
 * Created by Mathieu Corti on 8/2/17.
 * mathieucorti@gmail.com
 */

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mad.corti.trackap.R;
import com.mad.corti.trackap.activities.Trackap;
import com.mad.corti.trackap.helpers.DummyLocationService;
import com.mad.corti.trackap.models.Meeting;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Map extends Fragment {

    private MapView             _mapView;
    private GoogleMap           _googleMap;
    private ArrayList<Meeting>  _meetings;


    // ---------------------------------------------------------------------------------------------
    // PUBLIC PART - --------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        Trackap trackap = (Trackap) getActivity();
        _meetings = trackap.getMeetingList();

        setHasOptionsMenu(true);

        initializeMap(rootView, savedInstanceState);

        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.action_sort);
        item.setVisible(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        _mapView.onResume();

        updateMarkers();
    }

    @Override
    public void onPause() {
        super.onPause();
        _mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        _mapView.onLowMemory();
    }

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------
    // PRIVATE PART --------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    private void initializeMap(View rootView, Bundle savedInstanceState) {

        _mapView = (MapView) rootView.findViewById(R.id.mapView);
        _mapView.onCreate(savedInstanceState);
        _mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        _mapView.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap mMap) {
                _googleMap = mMap;

                // TODO: Move to my location button
                if (getActivity().getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions( new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                } else {
                    _googleMap.setMyLocationEnabled(false);
                }

                updateMarkers();
            }
        });
    }

    private void updateMarkers() {

        if (_googleMap != null) {

            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            for (Meeting meeting : _meetings) {
                _googleMap.addMarker(new MarkerOptions().position(meeting.get_locationLatLng()).title(meeting.get_title()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                builder.include(meeting.get_locationLatLng());
            }


            try {

                Calendar cal = Calendar.getInstance();
                DummyLocationService dlc = DummyLocationService.getSingletonInstance(getContext());
                Date date = DateFormat.getTimeInstance(DateFormat.MEDIUM).parse(
                        cal.get(Calendar.HOUR) + ":" +
                                cal.get(Calendar.MINUTE) + ":" +
                                cal.get(Calendar.SECOND) + " " +
                                (cal.get(Calendar.HOUR_OF_DAY) > 11 ? "PM" : "AM")
                );

                List<DummyLocationService.FriendLocation> friendLocations =  dlc.getFriendLocationsForTime(date, 10, 10);

                for (DummyLocationService.FriendLocation friendLocation : friendLocations)  {
                    LatLng latLng = new LatLng(friendLocation.latitude, friendLocation.longitude);
                    _googleMap.addMarker(new MarkerOptions().position(latLng) .title(friendLocation.name));
                    builder.include(latLng);
                }


            } catch (ParseException ex) {
                Log.e("ERR", ex.toString());
            }

            // Update camera from builder
            LatLngBounds bounds = builder.build();
            int padding = getResources().getInteger(R.integer.map_padding);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            _googleMap.animateCamera(cameraUpdate);
        }
    }


    // ---------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
}
