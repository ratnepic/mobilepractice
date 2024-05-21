package ru.mirea.porynovma.firebaseauth;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import ru.mirea.porynovma.firebaseauth.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding bind;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        mAuth = FirebaseAuth.getInstance();

        bind.signInButton.setOnClickListener(v ->
            signIn(
                    bind.emailEdit.getText().toString(),
                    bind.passwordEdit.getText().toString()
            )
        );

        bind.registerButton.setOnClickListener(v ->
            createAccount(
                    bind.emailEdit.getText().toString(),
                    bind.passwordEdit.getText().toString()
            )
        );

        bind.signOutButton.setOnClickListener(v -> signOut());

        bind.verifyEmailButton.setOnClickListener(v -> sendEmailVerification());
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            bind.statusTextView.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            bind.detailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            bind.emailPasswordButtons.setVisibility(View.GONE);
            bind.emailPasswordFields.setVisibility(View.GONE);
            bind.signedInButtons.setVisibility(View.VISIBLE);
            bind.verifyEmailButton.setEnabled(!user.isEmailVerified());
        } else {
            bind.statusTextView.setText(R.string.signed_out);
            bind.detailTextView.setText(null);
            bind.emailPasswordButtons.setVisibility(View.VISIBLE);
            bind.emailPasswordFields.setVisibility(View.VISIBLE);
            bind.signedInButtons.setVisibility(View.GONE);
        }
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                        bind.statusTextView.setText(R.string.auth_failed);
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {
        bind.verifyEmailButton.setEnabled(false);

        final FirebaseUser user = mAuth.getCurrentUser();
        Objects.requireNonNull(user).sendEmailVerification()
                .addOnCompleteListener(this, task -> {
                    bind.verifyEmailButton.setEnabled(true);
                    if (task.isSuccessful()) {
                        Toast.makeText(this,
                                "Verification email sent to " + user.getEmail(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}