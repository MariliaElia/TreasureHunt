package com.example.marilia.treasurehunt;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.marilia.treasurehunt.database.User;

public class SharedPreferenceConfig {
    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedPreferenceConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.login_preference),Context.MODE_PRIVATE);
    }

    public void storeUserData(User user){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("first_name", user.first_name);
        editor.putString("last_name", user.last_name);
        editor.putString("username", user.username);
        editor.putString("password", user.password);
        editor.putString("email", user.email);
        editor.putString("dob", user.dob);
        editor.putInt("points", user.points);

        editor.commit();
    }

    public void storeUserID(int user_id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("user_id", user_id);
        editor.commit();
    }

    public void setLoginStatus(boolean status) {
        //creating an editor to write into shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(context.getResources().getString(R.string.login_status_preference), status);
        editor.commit();
    }

    public User getUserLoggedIn(){
        String first_name = sharedPreferences.getString("first_name","");
        String last_name = sharedPreferences.getString("last_name","");
        String username = sharedPreferences.getString("username","");
        String password = sharedPreferences.getString("password", "");
        String dob = sharedPreferences.getString("dob", "");
        String email = sharedPreferences.getString("email","");
        int points = sharedPreferences.getInt("points",0);

        User userLoggedIn = new User(first_name, last_name, username, password, dob, email, points);

        return userLoggedIn;
    }

    public int getUserID() {
        int user_id = sharedPreferences.getInt("user_id", -1);

        return user_id;
    }

    public boolean getLoginStatus() {
        boolean status = false;
        status = sharedPreferences.getBoolean(context.getResources().getString(R.string.login_status_preference), false);
        return status;
    }

    public void clearUserData(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

}
