package com.example.m3_quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity
        extends AppCompatActivity {

    private EditText etUserName; // used in multi methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Pull User ID from main activity if one was entered
        Intent intent = getIntent();
        String UserName = intent.getStringExtra("USERNAME");
        etUserName = findViewById(R.id.userName);
        etUserName.setText(UserName);
    }

    public void registerNewUser(View view){
        // Setup EditText instance
        etUserName = findViewById(R.id.userName);
        EditText etFirstName = findViewById(R.id.firstName);
        EditText etFamName = findViewById(R.id.familyName);
        EditText etDOB = findViewById(R.id.DOB);
        EditText etEmail= findViewById(R.id.email);
        EditText etPassword = findViewById(R.id.password);

        boolean inputValid = true;  // Will be set to false if any input criteria not met

        // Check for empty fields
        if (etUserName.getText().toString().isEmpty()) inputValid = false;
        if (etFirstName.getText().toString().isEmpty()) inputValid = false;
        if (etFamName.getText().toString().isEmpty()) inputValid = false;
        if (etDOB.getText().toString().isEmpty()) inputValid = false;
        if (etEmail.getText().toString().isEmpty()) inputValid = false;
        if (etPassword.getText().toString().isEmpty()) inputValid = false;
        if (!inputValid){
            Toast.makeText(this, R.string.toMissingField, Toast.LENGTH_LONG).show();
        }
        // Check first name length
        if (etFirstName.getText().toString().length() < 3 ||
                etFirstName.getText().toString().length() > 30  ) {
            inputValid = false;
            Toast.makeText(this, R.string.toFirstNameLenBad, Toast.LENGTH_LONG).show();
        }
        // Validate email format
        if ( !Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches() ) {
            inputValid = false;
            Toast.makeText(this, R.string.toBadEmail, Toast.LENGTH_LONG).show();
        }
        // Validate Date of Birth format MM/DD/YYYY
        if ( !etDOB.getText().toString().matches(getString(R.string.format_MMDDYYYY))) {
            inputValid = false;
            Toast.makeText(this, R.string.toBadDOB, Toast.LENGTH_LONG).show();
        }
        if (!inputValid) return; // Allow user to correct input fields

        // Set a SharedPreference per user with the below key
        final String sharedPrefKey = "com.example.m3_quizgame." +  etUserName.getText().toString();
        Log.d("SHAREDPREF_REGISTER", "sharedPrefKey: " + sharedPrefKey); // log full key name
        SharedPreferences sharedPref = super.getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        // Store
        String SHA_password = LoginActivity.SHA256(etPassword.getText().toString());
        Log.d("SHAREDPREF_REGISTER", "SHA256 Password: " + SHA_password);
        editor.putString("passWord", SHA_password);
        editor.putString("firstName", etFirstName.getText().toString());
        editor.putString("famName", etFamName.getText().toString());
        editor.putString("DOB", etDOB.getText().toString());
        editor.putString("Email", etEmail.getText().toString());
        editor.apply();
        finish(); // return to Login Activity
    }
}