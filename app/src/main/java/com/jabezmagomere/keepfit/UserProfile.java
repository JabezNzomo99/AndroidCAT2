package com.jabezmagomere.keepfit;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class UserProfile extends AppCompatActivity {
    EditText profile_fname, profile_lname, profile_uname, profile_mail, profile_home, profile_phone;
    TextView tvProfileName, tvEmail, tvCurrentWeight, tvTargetWeight;
    SeekBar seek_current_weight;
    SeekBar seek_target_weight;
    RadioButton radio_male, radio_female;
    NumberPicker numberPicker;
    private Toolbar toolbar;
    SessionManager sessionManager;
    RequestQueue requestQueue;
    private Button btnSave;
    private String token;
    private String user_id;
    private HashMap<String,String> user;
    private static final String TOKEN = "token";
    public static final String USER_ID="user_id";
    private static final String GET_URL="https://sleepy-beyond-36756.herokuapp.com/api/UserDetails";
    private static final String UPDATE_URL="https://sleepy-beyond-36756.herokuapp.com/api/UpdateUserProfile";
    private Context context;
    private static final String LOG_TAG=UserProfile.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    String Age, CurrentWeight,TargetWeight, Gender;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        context=UserProfile.this;
        profile_fname=(EditText)findViewById(R.id.profile_fname);
        profile_lname=(EditText)findViewById(R.id.profile_lname);
        profile_uname=(EditText)findViewById(R.id.profile_uname);
        profile_mail=(EditText)findViewById(R.id.profile_mail);
        profile_home=(EditText)findViewById(R.id.profile_home);
        profile_phone=(EditText)findViewById(R.id.profile_phone);
        numberPicker=(NumberPicker)findViewById(R.id.numberPicker);
        numberPicker.setMinValue(10);
        numberPicker.setMaxValue(100);
        toolbar=(Toolbar)findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.drawer_title_user_profile));
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        btnSave=(Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(onClickListener);
        numberPicker.setOnValueChangedListener(onValueChangeListener);
        radio_male=(RadioButton)findViewById(R.id.radio_male);
        radio_female=(RadioButton)findViewById(R.id.radio_female);
        seek_current_weight=(SeekBar) findViewById(R.id.seek_current_weight);
        seek_current_weight.setOnSeekBarChangeListener(onSeekBarChangeListener);
        seek_target_weight=(SeekBar) findViewById(R.id.seek_targetweight);
        seek_target_weight.setOnSeekBarChangeListener(onSeekBarChangeListener1);
        tvProfileName=(TextView)findViewById(R.id.profile_name);
        tvEmail=(TextView)findViewById(R.id.profile_email);
        tvCurrentWeight=(TextView)findViewById(R.id.tv_current_weight);
        tvTargetWeight=(TextView)findViewById(R.id.tvTargetWeight);
        sessionManager=new SessionManager(context);
        user=sessionManager.getUserDetails();
        token=user.get(TOKEN);
        user_id=user.get(USER_ID);
        requestQueue= Volley.newRequestQueue(UserProfile.this);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swiper);
        swipeRefreshLayout.setColorSchemeResources(R.color.card_backgroundColor,R.color.colorAccent,R.color.colorWhite);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getProfileData();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProfileData();
            }
        });

        }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bungee.swipeRight(UserProfile.this);
    }
    public void onRadioButtonClicked(View view){
        boolean checked=((RadioButton) view).isChecked();
        switch (view.getId()){
            case R.id.radio_male:
                if(checked)
                    Gender="Male";
                break;
            case R.id.radio_female:
                if(checked)
                    Gender="Female";
                break;
        }
    }



    NumberPicker.OnValueChangeListener onValueChangeListener =
            new 	NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    int number=numberPicker.getValue();
                    Age=String.valueOf(number);
                }
            };
    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener=new SeekBar.OnSeekBarChangeListener() {
        String progress;
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            progress=String.valueOf(i);
            tvCurrentWeight.setText(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
                CurrentWeight=progress;
        }
    };
    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener1=new SeekBar.OnSeekBarChangeListener() {
        String progress;
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            progress=String.valueOf(i);
            tvTargetWeight.setText(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            TargetWeight=progress;
        }
    };
    Button.OnClickListener onClickListener=new Button.OnClickListener(){

        @Override
        public void onClick(View view) {
            btnSave.setVisibility(View.INVISIBLE);
            updateUserProfile();
            btnSave.setVisibility(View.VISIBLE);
        }
    };
    public void getProfileData(){
        swipeRefreshLayout.setRefreshing(true);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, GET_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Toasty.success(UserProfile.this,getResources().getString(R.string.retrieval_success),Toast.LENGTH_LONG, true).show();
                Log.d("___________________",jsonObject.toString());
                try{
                    JSONObject response=jsonObject.getJSONObject("response");
                    String UserId = response.getString("id");
                    String FirstName = response.getString("FirstName");
                    String LastName = response.getString("LastName");
                    String UserName = response.getString("UserName");
                    String Email = response.getString("email");
                    String Home = response.getString("Home");
                    String Age = response.getString("Age");
                    String Gender = response.getString("Gender");
                    String Weight = response.getString("Weight");
                    String Targetweight = response.getString("TargetWeight");
                    String Phone=response.getString("PhoneNumber");
                    profile_fname.setText(FirstName);
                    if(!FirstName.equals("null")){
                        profile_fname.setText(FirstName);
                    }else{
                        profile_fname.setText("");
                    }
                    if(!LastName.equals("null")){
                        profile_lname.setText(LastName);
                        tvProfileName.setText(FirstName+" "+LastName);
                    }else{
                        profile_lname.setText("");
                    }
                    if(!UserName.equals("null")){
                        profile_uname.setText(UserName);
                    }else{
                        profile_uname.setText("");
                    }
                    if(!Email.equals("null")){
                        profile_mail.setText(Email);
                        tvEmail.setText(Email);
                    }else{
                        profile_mail.setText("");
                    }if(!Phone.equals("null")){
                        profile_phone.setText(Phone);
                    }else{
                        profile_phone.setText(" ");
                    }
                    if(!Home.equals("null")){
                        profile_home.setText(Home);
                    }else{
                        profile_fname.setText("");
                    }if(!Age.equals("null")){
                      numberPicker.setValue(Integer.parseInt(Age));
                    }else{
                        numberPicker.setValue(10);
                    }if(!Gender.equals("null")){
                        if(Gender.equals("Male")){
                           radio_male.setChecked(true);
                        }else{
                            radio_female.setChecked(true);
                        }
                    }else{

                    }if(!Weight.equals("null")){
                        seek_current_weight.setProgress(Integer.parseInt(Weight));
                        CurrentWeight=Weight;
                        tvCurrentWeight.setText(Weight);
                    }else{
                        seek_current_weight.setProgress(0);
                        tvCurrentWeight.setText("0");
                    }
                    if(!Targetweight.equals("null")){
                        seek_target_weight.setProgress(Integer.parseInt(Weight));
                        TargetWeight=Targetweight;
                        tvTargetWeight.setText(TargetWeight);
                    }else{
                        seek_target_weight.setProgress(0);
                        tvTargetWeight.setText("0");
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }catch (JSONException exception){
                    Log.d(LOG_TAG,exception.getMessage());

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(context,getResources().getString(R.string.error_occured), Toast.LENGTH_SHORT,true).show();
                swipeRefreshLayout.setRefreshing(false);
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
        requestQueue.add(jsonObjectRequest);
    }
    public void updateUserProfile(){
        final ProgressDialog progressDialog=new ProgressDialog(UserProfile.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.userprofile_update_status));
        progressDialog.show();
        final String fname=profile_fname.getText().toString();
        final String lname=profile_lname.getText().toString();
        final String uname=profile_uname.getText().toString();
        final String email=profile_mail.getText().toString();
        final String phone=profile_phone.getText().toString();
        final String home=profile_home.getText().toString();
        HashMap<String,String> params=new HashMap<String, String>();
        params.put("user_id",user_id);
        params.put("FirstName",fname);
        params.put("LastName",lname);
        params.put("UserName",uname);
        params.put("Home",home);
        params.put("email",email);
        params.put("PhoneNumber",phone);
        params.put("Age",Age);
        params.put("Gender",Gender);
        params.put("Weight",CurrentWeight);
        params.put("TargetWeight",TargetWeight);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, UPDATE_URL, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                Toasty.success(UserProfile.this,getResources().getString(R.string.userprofile_saved_success),Toast.LENGTH_SHORT,true).show();
                getProfileData();
                btnSave.setClickable(true);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                Toasty.error(UserProfile.this,getResources().getString(R.string.error_occured),Toast.LENGTH_SHORT,true).show();
                btnSave.setClickable(false);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<String, String>();
                params.put("user_id",user_id);
                params.put("FirstName",fname);
                params.put("LastName",lname);
                params.put("UserName",uname);
                params.put("Home",home);
                params.put("email",email);
                params.put("PhoneNumber",phone);
                params.put("Age",Age);
                params.put("Gender",Gender);
                params.put("Weight",CurrentWeight);
                params.put("TargetWeight",TargetWeight);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String > headers=new HashMap<>();
                headers.put("Content-Type","application/json");
                headers.put("Authorization","Bearer"+" "+token);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}
