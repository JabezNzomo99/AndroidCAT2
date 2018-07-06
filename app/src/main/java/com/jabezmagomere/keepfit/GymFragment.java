package com.jabezmagomere.keepfit;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jabezmagomere.keepfit.Adapter.CustomInfoWindowAdapter;
import com.jabezmagomere.keepfit.Adapter.GymAdapter;
import com.jabezmagomere.keepfit.Models.Gym;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class GymFragment extends Fragment implements OnMapReadyCallback {

    MapView mapView;
    private GoogleMap map;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private RequestQueue requestQueue;
    private static final String URL="https://sleepy-beyond-36756.herokuapp.com/api/ShowGyms";
    private String token;
    private static final String TOKEN = "token";
    private GymAdapter gymAdapter;
    private List<Gym> gyms=new ArrayList<>();;
    private RecyclerView recyclerView;
    SessionManager sessionManager;



    public GymFragment() {
        // Required empty public constructor
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,30,10,locationListener);
            }


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_gym, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        gymAdapter=new GymAdapter(gyms);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.HORIZONTAL));
        recyclerView.setAdapter(gymAdapter);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        requestQueue= Volley.newRequestQueue(getContext());
        sessionManager=new SessionManager(getContext());
        sessionManager.checkLogin();
        HashMap<String,String> user=sessionManager.getUserDetails();
        token=user.get(TOKEN);
        mapView.onResume();//display map
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map=googleMap;
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //ask for permssion
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            googleMap.setMyLocationEnabled(true);
        }
            try {
                boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
                if (!success) {
                    Log.e("ERROR", "Style parsing failed");
                }
            } catch (Resources.NotFoundException e) {
                Log.e("ERROR", e.getMessage());

            }
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        final GoogleMap finalGoogleMap = googleMap;
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                map.clear();
                gyms.clear();
                LatLng userlocation=new LatLng(location.getLatitude(),location.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(userlocation).title("Your position").snippet("Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.manmarker)));
                CameraPosition cameraPosition=new CameraPosition.Builder().target(userlocation).zoom(11).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.POST, URL, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        for(int i=0;i<jsonArray.length();i++){
                            try {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                Double latitude=jsonObject.getDouble("Latitude");
                                Double longitude=jsonObject.getDouble("Longitude");
                                String gymName=jsonObject.getString("GymName");
                                String rating=jsonObject.getString("Rating");
                                String phone=jsonObject.getString("PhoneNumber");
                                String open=jsonObject.getString("Open");
                                String close =jsonObject.getString("Close");
                                LatLng userlocation=new LatLng(latitude,longitude);
                                 MarkerOptions markerOptions=new MarkerOptions().position(userlocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.gym_marker));
                                Marker marker=googleMap.addMarker(markerOptions);
                                Gym gym=new Gym(gymName,rating,phone,open,close);
                                gyms.add(gym);
                                gymAdapter.notifyDataSetChanged();
                                marker.setTag(gym);
                                googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getContext()));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String > headers=new HashMap<>();
                        headers.put("Content-Type","application/json");
                        headers.put("Authorization","Bearer"+" "+token);
                        return headers;
                    }
                };
                requestQueue.add(jsonArrayRequest);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //ask for permssion
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,30,10,locationListener);
            map.clear();
            Location lastknownlocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastknownlocation!=null) {
                LatLng location = new LatLng(lastknownlocation.getLatitude(), lastknownlocation.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(location).title("Your position").snippet("Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.manmarker)));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(11).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }else{
                Toasty.info(getContext(),"Loading Location",Toast.LENGTH_SHORT, true).show();
            }

        }

        }


}
