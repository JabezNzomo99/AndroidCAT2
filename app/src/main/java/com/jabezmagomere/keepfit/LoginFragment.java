package com.jabezmagomere.keepfit;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private TextInputEditText etUserName,etPassword;
    private TextInputLayout usernameLayout, passwordLayout;
    private String UserName, Password;
    private Button btnLogin;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private static final String URL="https://sleepy-beyond-36756.herokuapp.com/api/Login";
    private static final String URL1="https://sleepy-beyond-36756.herokuapp.com/api/UserDetails";
    private static final String LOG_TAG=LoginFragment.class.getSimpleName();
    private Intent intent;
    private SharedPreferences sharedPreferences;
    SessionManager sessionManager;
    private String token;
    private Context context;



    public LoginFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_login, container, false);
        etUserName=(TextInputEditText) view.findViewById(R.id.etUserName);
        etPassword=(TextInputEditText) view.findViewById(R.id.etPassword);
        btnLogin=(Button) view.findViewById(R.id.btnLogin);
        sessionManager=new SessionManager(getContext());
        usernameLayout=(TextInputLayout)view.findViewById(R.id.usernameInputLayout);
        passwordLayout=(TextInputLayout)view.findViewById(R.id.passwordInputLayout);
        requestQueue= Volley.newRequestQueue(getContext());
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.setVisibility(View.INVISIBLE);
                progressDialog=DialogUtils.showProgressDialog(v.getContext(),getResources().getString(R.string.logging_in_status));
                if(validateUserName(etUserName) && validatePassword(etPassword)){
                    UserName=etUserName.getText().toString();
                    Password=etPassword.getText().toString();
                    login();
                }

            }

        });

        return view;
    }
    private boolean validateUserName(TextInputEditText textInputEditText){
        if(TextUtils.isEmpty(textInputEditText.getText())){
            usernameLayout.setError(getResources().getString(R.string.username_required_error));
        }else {
            usernameLayout.setErrorEnabled(false);

        }
        return true;
    }
    private boolean validatePassword(TextInputEditText textInputEditText){
        if(TextUtils.isEmpty(textInputEditText.getText())){
            passwordLayout.setError(getResources().getString(R.string.password_required_error));
        }else{
            passwordLayout.setErrorEnabled(false);
        }
        if(textInputEditText.getText().toString().length() <6){
            passwordLayout.setError(getResources().getString(R.string.password_short_error));
        }
        return true;
    }
    public void login(){
        HashMap<String,String> params=new HashMap<>();
        params.put("UserName",UserName);
        params.put("password",Password);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject res=response.getJSONObject("response");
                    token=res.getString("token");
                    Log.d("Token",token);
                    if(token!=null){
                        sessionManager.setToken(token);
                        Toasty.success(getContext(),getResources().getString(R.string.login_msg_success),Toast.LENGTH_SHORT,true).show();
                        JsonObjectRequest jsonObjectRequest1=new JsonObjectRequest(Request.Method.POST, URL1, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                try {
                                    JSONObject response=jsonObject.getJSONObject("response");

                                        String UserId = response.getString("id");
                                        String FirstName = response.getString("FirstName");
                                        String LastName = response.getString("LastName");
                                        String UserName = response.getString("UserName");
                                        String Email = response.getString("email");
                                        sessionManager.setUser_Id(UserId);
                                        sessionManager.setFirstName(FirstName);
                                        sessionManager.setLastName(LastName);
                                        sessionManager.setUserName(UserName);
                                        sessionManager.setEmail(Email);
                                        sessionManager.loginUser();
                                        if(sessionManager.isLoggedIn()){
                                            Intent intent=new Intent(getContext(),MainActivity.class);
                                            startActivity(intent);
                                            Bungee.swipeLeft(getContext());
                                        }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }


                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toasty.error(getContext(),getResources().getString(R.string.failed_to_load_details),Toast.LENGTH_SHORT,true).show();

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
                        requestQueue.add(jsonObjectRequest1);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getContext(),getResources().getString(R.string.login_failed),Toast.LENGTH_SHORT,true).show();
                btnLogin.setClickable(true);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("UserName",UserName);
                params.put("Password",Password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers=new HashMap<>();
                headers.put("Content-Type","application/json");
                headers.put("Accept","application/json");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
       if(progressDialog!=null && progressDialog.isShowing()){
           progressDialog.dismiss();
       }
       btnLogin.setVisibility(View.VISIBLE);
    }




}
