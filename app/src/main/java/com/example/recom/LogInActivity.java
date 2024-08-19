package com.example.recom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LogInActivity extends AppCompatActivity {

    private static final String TAG = "LogInActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: Initializing Firebase Auth");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            Log.d(TAG, "onCreate: User not logged in, starting sign-in process");
            signIn(); // Start Firebase Auth sign-in process
        } else {
            Log.d(TAG, "onCreate: User is already logged in, checking profile");
            createUsername(user); // If user is already logged in, handle profile check
        }
    }

    private void createUsername(FirebaseUser user) {
        if (isProfileComplete(user)) {
            transactToMainActivity();
        } else {

            Intent intent = new Intent(LogInActivity.this, ProfileSetupActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    Log.d(TAG, "onActivityResult: Sign-in result received");
                    onSignInResult(result); // Handle sign-in result
                }
            }
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                    Log.d(TAG, "onSignInResult: Profile is incomplete, redirecting to ProfileSetupActivity");
                    Intent intent = new Intent(this, ProfileSetupActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
    }

    private boolean isProfileComplete(FirebaseUser user) {
        // Check if the user's profile is complete
        String displayName = user.getDisplayName();
        Log.d(TAG, "isProfileComplete: Checking if profile is complete");
        return displayName != null && !displayName.isEmpty();
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showSignInCancelledMessage() {
        Log.d(TAG, "showSignInCancelledMessage: Sign-in cancelled");
        Toast.makeText(this, "Sign-in cancelled", Toast.LENGTH_SHORT).show();
    }

    private void signIn() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        Log.d(TAG, "signIn: Creating sign-in intent");
        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.logo_icon) // Set a custom logo if available
                .build();

        Log.d(TAG, "signIn: Launching sign-in intent");
        signInLauncher.launch(signInIntent);
    }

    private void transactToMainActivity() {
        Log.d(TAG, "transactToMainActivity: Transitioning to MainActivity");
        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
