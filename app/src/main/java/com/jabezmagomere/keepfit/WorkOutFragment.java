package com.jabezmagomere.keepfit;


import android.app.AlertDialog;
import android.app.DatePickerDialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jabezmagomere.keepfit.Adapter.InstructorAdapter;
import com.jabezmagomere.keepfit.Adapter.WorkOutAdapter;
import com.jabezmagomere.keepfit.Models.WorkOut;
import com.reginald.editspinner.EditSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class WorkOutFragment extends Fragment {
    RequestQueue requestQueue;
    RequestQueue getRequestQueue;
    SessionManager sessionManager;
    RecyclerView recyclerView;
    WorkOutAdapter workOutAdapter;
    TextView tvRepsCount, tvSetsCount,tvWorkOutDate;
    ImageButton btnDate;
    TextInputEditText etDate;
    TextInputLayout textInputLayout;
    private List<WorkOut> workOutList=new ArrayList<>();
    View view;
    public String ExcerciseName,Date,Location,Reps,Sets;
    private FloatingActionButton floatingActionButton;
    SwipeRefreshLayout swipeRefreshLayout;
    public static final String TOKEN="token";
    public static final String USER_ID="user_id";
    private String token;
    private String user_id;
    private static final String URL="https://sleepy-beyond-36756.herokuapp.com/api/ShowWorkouts";
    private static final String ADD_URL="https://sleepy-beyond-36756.herokuapp.com/api/AddWorkOut";
    private AlertDialog alert;
    private ProgressDialog progressDialog;
    private WorkOut work=new WorkOut();
    private HashMap<String,String> workout=new HashMap<String, String>();


    public WorkOutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_work_out, container, false);
        requestQueue= Volley.newRequestQueue(getContext());
        getRequestQueue=Volley.newRequestQueue(getContext());
        recyclerView=(RecyclerView)view.findViewById(R.id.workout_recycler);
        sessionManager=new SessionManager(getContext());
        sessionManager.checkLogin();
        HashMap<String,String> user=sessionManager.getUserDetails();
        token=user.get(TOKEN);
        user_id=user.get(USER_ID);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.card_backgroundColor,R.color.colorAccent,R.color.colorWhite);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                displayWorkOuts();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayWorkOuts();
            }
        });
        floatingActionButton=(FloatingActionButton)view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater=LayoutInflater.from(getContext());
                View workout_view=layoutInflater.inflate(R.layout.workout,null);
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(getContext());
                alertDialog.setView(workout_view);
                alertDialog.setCancelable(true);
                final EditSpinner etExcerciseName=(EditSpinner)workout_view.findViewById(R.id.etExerciseName);
                etExcerciseName.setEditable(false);
                etExcerciseName.setDropDownDrawableSpacing(50);
                ListAdapter adapter=new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,getResources().getStringArray(R.array.ExcerciseName));
                etExcerciseName.setItemConverter(new EditSpinner.ItemConverter() {
                    @Override
                    public String convertItemToString(Object selectedItem) {
                        Toasty.info(getContext(),selectedItem.toString(), Toast.LENGTH_SHORT,true).show();
                        ExcerciseName=selectedItem.toString();
                        workout.put("ExerciseName",selectedItem.toString());
                        return selectedItem.toString();

                    }
                });
                etExcerciseName.setAdapter(adapter);
                final EditSpinner etLocation=(EditSpinner)workout_view.findViewById(R.id.GymLocation);
                etLocation.setEditable(false);
                ListAdapter adapter1=new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,getResources().getStringArray(R.array.Gyms));
                etLocation.setItemConverter(new EditSpinner.ItemConverter() {
                    @Override
                    public String convertItemToString(Object selectedItem) {
                        Toasty.info(getContext(),selectedItem.toString(), Toast.LENGTH_SHORT,true).show();
                        Location=selectedItem.toString();
                        workout.put("Location",selectedItem.toString());
                        return selectedItem.toString();
                    }
                });
                etLocation.setAdapter(adapter1);
                btnDate=(ImageButton) workout_view.findViewById(R.id.btnDate);
                btnDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       showDatePicker();

                    }
                });
                textInputLayout=(TextInputLayout)workout_view.findViewById(R.id.etDateLayout);
                textInputLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDatePicker();
                    }
                });

                tvRepsCount=(TextView)workout_view.findViewById(R.id.tvRepsCount);
                tvSetsCount=(TextView)workout_view.findViewById(R.id.tvSetsCount);
                tvWorkOutDate=(TextView)workout_view.findViewById(R.id.tvWorkOutDate);
                final SeekBar reps_seekbar=(SeekBar)workout_view.findViewById(R.id.reps_seekbar);
                reps_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    String  progress;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                        progress=String.valueOf(i);
                        tvRepsCount.setText(String.valueOf(i));
                        Reps=progress;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        Reps=progress;
                        tvRepsCount.setText(progress);
                        workout.put("Reps",Reps);
                    }
                });
                final SeekBar sets_seekbar=(SeekBar)workout_view.findViewById(R.id.sets_seekbar);
                sets_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    String progress;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        progress=String.valueOf(i);
                        tvSetsCount.setText(String.valueOf(i));
                        Sets=progress;
                        work.setSets(progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        Sets=progress;
                        tvSetsCount.setText(progress);
                        workout.put("Sets",Sets);
                    }
                });
                final Button btnAddWorkOut=(Button)workout_view.findViewById(R.id.btnAddWorkOut);
                btnAddWorkOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnAddWorkOut.setVisibility(View.INVISIBLE);
                        progressDialog=new ProgressDialog(getContext());
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage(getResources().getString(R.string.workout_progress_saving));
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        AddWorkOut();
                        btnAddWorkOut.setVisibility(View.VISIBLE);

                    }
                });
                alert=alertDialog.create();
                alert.show();
            }
        });
        return view;
    }

    public void showDatePicker(){
        final DatePickerFragment datePickerFragment=new DatePickerFragment();
        Calendar calendar=Calendar.getInstance();
        Bundle args=new Bundle();
        args.putInt("year",calendar.get(Calendar.YEAR));
        args.putInt("month",calendar.get(Calendar.MONTH));
        args.putInt("day",calendar.get(Calendar.DAY_OF_MONTH));
        datePickerFragment.setArguments(args);
        datePickerFragment.setCallBack(onDateSetListener);
        datePickerFragment.show(getFragmentManager(),getResources().getString(R.string.workout_date_picker));
    }
    DatePickerDialog.OnDateSetListener onDateSetListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            int n_month=month+1;
            String date=String.valueOf(year)+"-"+String.valueOf(n_month)+"-"+String.valueOf(day);
            workout.put("Date",date);
            tvWorkOutDate.setText(date);

        }
    };
    public void displayWorkOuts() {
        workOutList.clear();
        workOutAdapter = new WorkOutAdapter(workOutList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(workOutAdapter);
        prepareWorkOutData();
    }
    public void prepareWorkOutData(){
        swipeRefreshLayout.setRefreshing(true);
        HashMap<String,String> params=new HashMap<String, String >();
        params.put("user_id",user_id);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response ==null){
                    Toasty.info(getContext(),"No Work Outs Saved",Toast.LENGTH_LONG,true).show();
                }

                try {
                    JSONArray jsonArray=response.getJSONArray("response");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String id=jsonObject.getString("id");
                        String date=jsonObject.getString("date");
                        String location=jsonObject.getString("location");
                        String exercise_name=jsonObject.getString("exercise_name");
                        String reps=jsonObject.getString("reps");
                        String sets=jsonObject.getString("sets");
                        WorkOut workOut=new WorkOut();
                        workOut.setId(id);
                        workOut.setDate(date);
                        workOut.setLocation(location);
                        workOut.setExcerciseName(exercise_name);
                        workOut.setReps(reps);
                        workOut.setSets(sets);
                        workOutList.add(workOut);
                        workOutAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d("ERROR",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<String, String >();
                params.put("user_id",user_id);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers=new HashMap<>();
                headers.put("Content-Type","application/json");
                headers.put("Authorization","Bearer"+" "+token);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    public void AddWorkOut(){
        Log.d("Work Out Data",workout.toString());
        Log.d("Work Out Data",user_id);
        HashMap<String,String> params=new HashMap<String, String>();
        params.put("user_id",user_id);
        params.put("date",workout.get("Date"));
        params.put("location",workout.get("Location"));
        params.put("exercise_name",workout.get("ExerciseName"));
        params.put("reps",workout.get("Reps"));
        params.put("sets",workout.get("Sets"));

       JsonObjectRequest jsonObjectRequest1=new JsonObjectRequest(Request.Method.POST, ADD_URL, new JSONObject(params), new Response.Listener<JSONObject>() {
           @Override
           public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                alert.dismiss();
                Toasty.success(getContext(),getResources().getString(R.string.workout_saved_message),Toast.LENGTH_SHORT,true).show();
                displayWorkOuts();

           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               progressDialog.dismiss();
               alert.dismiss();
               Toasty.error(getContext(),getResources().getString(R.string.error_occured),Toast.LENGTH_SHORT,true).show();
           }
       }){
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               HashMap<String,String> params=new HashMap<String, String>();
               params.put("user_id",user_id);
               params.put("date",workout.get("Date"));
               params.put("location",workout.get("Location"));
               params.put("exercise_name",workout.get("ExerciseName"));
               params.put("reps",workout.get("Reps"));
               params.put("sets",workout.get("Sets"));
               return params;
           }

           @Override
           public Map<String, String> getHeaders() throws AuthFailureError {
               HashMap<String,String> headers=new HashMap<>();
               headers.put("Content-Type","application/json");
               headers.put("Authorization","Bearer"+" "+token);
               return headers;
           }
       };

        getRequestQueue.add(jsonObjectRequest1);

    }

}
