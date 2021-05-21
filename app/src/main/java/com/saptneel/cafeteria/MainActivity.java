package com.saptneel.cafeteria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    EditText edtUsername, edtEmail, edtPassword;
    TextView txtSignedUp;
    Button btnSignUp;
    private boolean isSigningUp = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtSignedUp = findViewById(R.id.txtSignedUp);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            startActivity(new Intent(MainActivity.this, Chats.class));
            finish();
        }

        txtSignedUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSigningUp) {
                    edtUsername.setVisibility(View.GONE);
                    txtSignedUp.setText("Don't have an account? Sign Up");
                    btnSignUp.setText("Log In");
                    isSigningUp = false;
                } else {
                    edtUsername.setVisibility(View.VISIBLE);
                    txtSignedUp.setText("Already have an account? Log In");
                    btnSignUp.setText("Sign Up");
                    isSigningUp = true;
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtEmail.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty())
                    if(isSigningUp && edtUsername.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                        return;
                    }
                if(isSigningUp)
                    handleSignUp();
                else
                    handleLogin();
            }
        });

    }

    private void handleSignUp() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseDatabase
                            .getInstance()
                            .getReference("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(new User(edtUsername.getText().toString(), "", edtEmail.getText().toString()));
                    startActivity(new Intent(MainActivity.this, Chats.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    Toast.makeText(MainActivity.this, "Successfully Signed Up", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLogin() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(MainActivity.this, Chats.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    Toast.makeText(MainActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}