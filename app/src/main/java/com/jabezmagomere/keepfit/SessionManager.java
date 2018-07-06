package com.jabezmagomere.keepfit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.jabezmagomere.keepfit.LoginRegHost;

import java.util.HashMap;

public class SessionManager {
    String User_Id,  FirstName,  LastName,  UserName,  Email,  Token;

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE=0;
    private static final String PREFERENCE_NAME="KeepFitPreferences";
    private static final String IS_LOGIN="IsLoggedIn";
    public static final String USER_ID="user_id";
    public static final String FIRST_NAME="first_name";
    public static final String LAST_NAME="last_name";
    public static final String USER_NAME="user_name";
    public static final String EMAIL="email";
    public static final String TOKEN="token";
    public SessionManager(Context mcontext){
        this.context=mcontext;
        sharedPreferences=context.getSharedPreferences(PREFERENCE_NAME,PRIVATE_MODE);
        editor=sharedPreferences.edit();
    }
    public void loginUser(){
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(USER_ID,User_Id);
        editor.putString(FIRST_NAME,FirstName);
        editor.putString(LAST_NAME,LastName);
        editor.putString(USER_NAME,UserName);
        editor.putString(EMAIL,Email);
        editor.putString(TOKEN,Token);
        editor.commit();
    }
    public HashMap<String,String> getUserDetails(){
        HashMap<String,String> user =new HashMap<String, String>();
        user.put(USER_ID,sharedPreferences.getString(USER_ID,null));
        user.put(FIRST_NAME,sharedPreferences.getString(FIRST_NAME,null));
        user.put(LAST_NAME,sharedPreferences.getString(LAST_NAME,null));
        user.put(USER_NAME,sharedPreferences.getString(USER_NAME,null));
        user.put(EMAIL,sharedPreferences.getString(EMAIL,null));
        user.put(TOKEN,sharedPreferences.getString(TOKEN,null));
        return user;
            }
    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGIN,false);
        }
    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent intent=new Intent(context, LoginRegHost.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        }
    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent intent=new Intent(context, LoginRegHost.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
