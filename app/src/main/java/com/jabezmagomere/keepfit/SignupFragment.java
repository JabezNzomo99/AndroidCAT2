package com.jabezmagomere.keepfit;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {
    private String FirstName, LastName, UserName, Email, Password;
    private TextInputEditText etFirstName,etUserName, etLastName,etEmail,etPassword,etConfirmPassword;
    private TextInputLayout layout_firstName, layout_lastName, layout_UserName,layout_Email, layout_Password, layoutConfirmPassword;
    public static final String URL="https://sleepy-beyond-36756.herokuapp.com/api/Register";
    RequestQueue requestQueue;
    Button btnSignUp;


    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_signup, container, false);
        etFirstName=(TextInputEditText) view.findViewById(R.id.etFirstName);
        etLastName=(TextInputEditText) view.findViewById(R.id.etLastName);
        etUserName=(TextInputEditText) view.findViewById(R.id.etUserName);
        etEmail=(TextInputEditText) view.findViewById(R.id.etEmail);
        etPassword=(TextInputEditText) view.findViewById(R.id.etPass);
        etConfirmPassword=(TextInputEditText) view.findViewById(R.id.etConfirmPass);
        layout_firstName=(TextInputLayout)view.findViewById(R.id.layout_firstName);
        layout_lastName=(TextInputLayout)view.findViewById(R.id.layout_lastName);
        layout_UserName=(TextInputLayout)view.findViewById(R.id.layout_UserName);
        layout_Email=(TextInputLayout)view.findViewById(R.id.layout_Email);
        layout_Password=(TextInputLayout)view.findViewById(R.id.layout_Password);
        layoutConfirmPassword=(TextInputLayout)view.findViewById(R.id.layout_confirmPassword);
        requestQueue= Volley.newRequestQueue(getContext());
        btnSignUp=(Button)view.findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateName(etFirstName,layout_firstName)&&validateName(etLastName,layout_lastName) &&
                        validateName(etUserName,layout_UserName)&&validateEmail(etEmail,layout_Email)&&validatePassword(etPassword,layout_Password)){
                    FirstName=etFirstName.getText().toString();
                    LastName=etLastName.getText().toString();
                    UserName=etUserName.getText().toString();
                    Email=etEmail.getText().toString();
                    Password=etPassword.getText().toString();
                        Register();
                }

            }
        });


        return view;
    }
    public boolean validateName(TextInputEditText textInputEditText, TextInputLayout textInputLayout){
        if(TextUtils.isEmpty(textInputEditText.getText().toString())){
            textInputLayout.setError(getResources().getString(R.string.signup_name_required_error));
        }else{
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validatePassword(TextInputEditText textInputEditText, TextInputLayout passwordLayout){
        if(TextUtils.isEmpty(textInputEditText.getText().toString()) || textInputEditText.getText().toString().length()<6 ){
            passwordLayout.setError(getResources().getString(R.string.signup_invalid_password_error));
        }
        if(!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())){
            passwordLayout.setError(getResources().getString(R.string.password_match_error));
        }
        return true;
    }
    public boolean validateEmail(TextInputEditText textInputEditText,TextInputLayout textInputLayout){
        if(!TextUtils.isEmpty(textInputEditText.getText())&& Patterns.EMAIL_ADDRESS.matcher(textInputEditText.getText().toString()).matches()){
            textInputLayout.setErrorEnabled(false);
            return true;
        }else{
            textInputLayout.setError(getResources().getString(R.string.signup_invalid_email_error));
        }
        return false;
    }
    public void Register(){
        HashMap<String,String> params=new HashMap<>();
        params.put("FirstName",FirstName);
        params.put("LastName",LastName);
        params.put("UserName",UserName);
        params.put("email",Email);
        params.put("password",Password);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toasty.success(getContext(),getResources().getString(R.string.account_created_message), Toast.LENGTH_LONG,true).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getContext(),getResources().getString(R.string.error_occured), Toast.LENGTH_LONG,true).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("FirstName",FirstName);
                params.put("LastName",LastName);
                params.put("UserName",UserName);
                params.put("email",Email);
                params.put("password",Password);
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

    }

}
