package com.fit2081.invoicegeneratorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * MainActivity class that handles user registration and navigation to the login screen.
 * This class extends AppCompatActivity and sets up the user interface for user registration.
 */
public class MainActivity extends AppCompatActivity {

    // Declaring EditText fields
    EditText usernameEditText, passwordEditText, reenterPasswordEditText;

    // Declaring Buttons
    Button registerButton, loginButton;

    /**
     * onCreate method sets up the user interface, initialises UI components and sets onClickListeners
     * for the register and login buttons.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialising EditText fields
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        reenterPasswordEditText = findViewById(R.id.reenterPasswordEditText);

        // Initialising Buttons
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        // Button to save data to SharedPreferences
        registerButton.setOnClickListener(new View.OnClickListener() {

            /**
             * onClick method defines what happens when the user clicks the register button.
             * Validates the input fields and stores the credentials in SharedPreferences.
             * Navigates to LoginActivity upon successful registration.
             *
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {

                // Retrieves input texts and converts them into strings
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String rePassword = reenterPasswordEditText.getText().toString();

                // Checks if fields are empty and outputs an error message
                if (username.isEmpty() || password.isEmpty() || rePassword.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                }
                // Checks if do not match and outputs an error message
                else if (!password.equals(rePassword)) {
                    Toast.makeText(MainActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                }
                // Else, stores credentials in SharedPreferences
                else {
                    SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.apply();

                    // Notifies users of successful registration
                    Toast.makeText(MainActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                    // Redirect page to LoginActivity
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Close MainActivity
                }
            }
        });

        // Button that transitions to LoginActivity for existing users
        loginButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }));
    }
}
