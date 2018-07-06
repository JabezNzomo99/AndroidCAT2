package com.jabezmagomere.keepfit;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jabezmagomere.keepfit.Adapter.InstructorAdapter;
import com.jabezmagomere.keepfit.Models.Instructor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstructorsFragment extends Fragment {
    private List<Instructor> instructorList = new ArrayList<>();
    InstructorAdapter instructorAdapter;
    RecyclerView recyclerView;
    View view;
    private static final String URL = "https://sleepy-beyond-36756.herokuapp.com/api/Instructors";
    RequestQueue requestQueue;
    String token;
    private static final String TOKEN = "token";
    SessionManager sessionManager;
    private SwipeRefreshLayout swipeRefreshLayout;


    public InstructorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_instructors, container, false);
        requestQueue = Volley.newRequestQueue(getContext());
        sessionManager=new SessionManager(getContext());
        sessionManager.checkLogin();
        HashMap<String,String> user=sessionManager.getUserDetails();
        token=user.get(TOKEN);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerInstructor);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.card_backgroundColor,R.color.colorAccent,R.color.colorWhite);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                displayInstructors();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayInstructors();
            }
        });
        return view;

    }
    //Method is called upon when swipe refresh is pulled
//    @Override
//    public void onRefresh() {
//        displayInstructors();
//    }

    public void displayInstructors() {
        instructorList.clear();
        instructorAdapter = new InstructorAdapter(instructorList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(instructorAdapter);
        prepareInstructorsData();
    }

    private void prepareInstructorsData() {
//        Instructor instructor = new Instructor("Arnold", "Shwerznigger", "0711285975", "arnold@gmail.com", "Male", "https://i.ebayimg.com/images/g/Ug0AAOxyGwNTDKx1/s-l300.jpg", 1);
//        instructorList.add(instructor);
//        Instructor instructor1 = new Instructor("Usain", "Bolt", "0711285975", "usain@gmail.com", "Male", "http://wallvie.com/wp-content/uploads/2017/01/awesome-usain-bolt-sports-hd-wallpaper-high-quality-ain-cave-of-iphone.jpg", 1);
//        instructorList.add(instructor1);
//        Instructor instructor2 = new Instructor("Arnold", "Shwerznigger", "0711285975", "arnold@gmail.com", "Male", "https://i.ebayimg.com/images/g/Ug0AAOxyGwNTDKx1/s-l300.jpg", 1);
//        instructorList.add(instructor2);
//        Instructor instructor3 = new Instructor("Arnold", "Shwerznigger", "0711285975", "arnold@gmail.com", "Male", "https://i.ebayimg.com/images/g/Ug0AAOxyGwNTDKx1/s-l300.jpg", 1);
//        instructorList.add(instructor3);
        //Showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.i("RESPONCE","RESPONCE RECEIVED");
                for(int i=0;i<jsonArray.length();i++){
                    try {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String FirstName=jsonObject.getString("FirstName");
                        String LastName=jsonObject.getString("LastName");
                        String PhoneNumber=jsonObject.getString("PhoneNumber");
                        String Email=jsonObject.getString("Email");
                        String Gender=jsonObject.getString("Gender");
                        String PhotoURL=jsonObject.getString("PhotoURL");
                        Integer GymId=jsonObject.getInt("GymId");
                        Instructor instructor=new Instructor(FirstName,LastName,PhoneNumber,Email,Gender,PhotoURL,GymId);
                        instructorList.add(instructor);
                        instructorAdapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERROR","ERROR OCCURED");
                swipeRefreshLayout.setRefreshing(false);

            }


        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers=new HashMap<>();
                headers.put("Content-Type","application/json");
                headers.put("Authorization","Bearer"+" "+token);
                return headers;
            }
        };
        requestQueue.add(jsonArrayRequest);

    }



}
