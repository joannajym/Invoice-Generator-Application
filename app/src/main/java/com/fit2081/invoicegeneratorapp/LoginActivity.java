package com.fit2081.invoicegeneratorapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

/**
 * LoginActivity class handles user authentication and navigation to the Invoice Generator screen.
 * This class extends AppCompatActivity and manages the login process by verifying user credentials.
 */
public class LoginActivity extends AppCompatActivity {

    // Declaring EditText fields
    EditText usernameEditText, passwordEditText;

    // Declaring Buttons
    Button loginButton, registerButton;

    // Key for SharedPreferences file name
    public static final String PREFERENCES = "com.fit2081.assignment1.PREFERENCES";

    /**
     * onCreate methods sets up the user interface, initialises UI components,
     * and sets onClickListeners for the login and register buttons.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialise EditText fields
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        // Initialise Buttons
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        /**
         * Method that authenticates users credentials when logging in.
         * Compares the entered credentials with the ones stored in SharedPreferences.
         * If the credentials match, navigate to InvoiceGeneratorActivity and closes LoginActivity.
         * Otherwise, display error message.
         */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {

                // Retrieves input texts and converts them into strings
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Accesses and retrieves username and password from SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFERENCES, MODE_PRIVATE);
                String registeredUsername = sharedPreferences.getString("username", "");
                String registeredPassword = sharedPreferences.getString("password", "");

                // Checks if credentials from SharedPreferences matches input
                if(username.equals(registeredUsername) && password.equals(registeredPassword)) {
                    Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                    // Redirect to InvoiceGeneratorActivity after successful login
                    Intent intent = new Intent(LoginActivity.this, InvoiceGeneratorActivity.class);
                    startActivity(intent);
                    finish(); // Close the LoginActivity
                }
                // Else, displays error message
                else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Button that transitions to MainActivity for registering users
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
