package com.example.m3_quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class LoginActivity
        extends AppCompatActivity
        implements View.OnClickListener {

    private EditText etUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Setup button click listeners
        Button buLogin = findViewById(R.id.login);
        Button buRegister = findViewById(R.id.register);
        buLogin.setOnClickListener(this);
        buRegister.setOnClickListener(this);
    }
    // OnClick method for login and register buttons
    public void onClick(View view){
        if (view.getId() == R.id.login) loginUser(view);
        else if (view.getId() == R.id.register) launchRegisterAct(view);
    }
    // Launch Register activity. Intent stores the username if it has been
    // provided on the login screen.
    public void launchRegisterAct(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        etUserName = findViewById(R.id.userName);
        String strLogin = etUserName.getText().toString();
        intent.putExtra("USERNAME", strLogin);
        startActivity(intent);
    }
    public void loginUser(View view){
        etUserName = findViewById(R.id.userName);
        EditText etPassword = findViewById(R.id.password);
        String UserName = etUserName.getText().toString();
        String Password = etPassword.getText().toString();
        // retrieve stored password and firstName from SharedPreferences. UserName part of the Key.
        final String sharedPrefKey = "com.example.m3_quizgame." +  UserName;
        // log full key name
        Log.d("SHAREDPREF", "sharedPrefKey: " + sharedPrefKey);
        SharedPreferences sharedPref = this.getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE);
        String SHA_storedPassword = sharedPref.getString("passWord", "NULL");
        String firstName = sharedPref.getString("firstName", "NULL");
        // Return to login if user is not found.
        if ( firstName.equals("NULL")) {
            Toast.makeText(this, "user not found", Toast.LENGTH_SHORT).show();
        } else {
            // Check to see if the password matches login submission
            String SHA_password = SHA256(Password);
            Log.d("SHAREDPREF", "SHA256 Password: " + SHA_password);
            Log.d("SHAREDPREF", "SHA_storedPasswd: " + SHA_storedPassword);
            if( SHA_password.equals(SHA_storedPassword)){
                Toast.makeText(this, R.string.toLoginSuccess , Toast.LENGTH_SHORT).show();
                TextView tvWelcome = findViewById(R.id.tvWelcome);
                tvWelcome.setText(getString(R.string.welcome) + firstName + "!");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LoginActivity.this, GameBegin.class);
                        intent.putExtra("USERNAME", UserName);
                        startActivity(intent);
                        finish();
                    }
                }, 1500);
            } else {
                Toast.makeText(this, R.string.toLoginFail, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // SHA256 helper function used by Login and Resister Activities
    public static String SHA256(String password) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}