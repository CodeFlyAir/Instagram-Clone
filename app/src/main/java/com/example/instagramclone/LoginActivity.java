package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{
    private EditText email;
    private EditText password;
    private TextView registerUser;
    private TextView warningText;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        registerUser = findViewById(R.id.registerDirect);
        warningText = findViewById(R.id.warningText);
        progressDialog=new ProgressDialog(this);
        
        Button login = findViewById(R.id.loginButton);
        
        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();
                if ( !TextUtils.isEmpty(txtEmail) && !TextUtils.isEmpty(txtPassword) )
                    loginUser(txtEmail, txtPassword);
                else
                {
                    warningText.setText("Invalid Credentials");
                    warningText.setVisibility(View.VISIBLE);
                }
                
            }
        });
        
        registerUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
        
    }
    
    private void loginUser (String txtEmail, String txtPassword)
    {
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        
        mAuth.signInWithEmailAndPassword(txtEmail,txtPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>()
        {
            @Override
            public void onSuccess (AuthResult authResult)
            {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Log In Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure (@NonNull Exception e)
            {
                progressDialog.dismiss();
                Log.d("InstaClone","Login Failed : "+e.getMessage());
                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}