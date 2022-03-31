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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.rpc.context.AttributeContext;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity
{
    private EditText username;
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText rePassword;
    private TextView warning;
    private FirebaseAuth mAuth;
    
    private DatabaseReference mRootRef;
    private ProgressDialog progressDialog;
    
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        
        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        rePassword = findViewById(R.id.rePassword);
        TextView directToLogin = findViewById(R.id.loginDirect);
        warning = findViewById(R.id.warningText);
        
        progressDialog=new ProgressDialog(this);
        mRootRef = FirebaseDatabase.getInstance("https://instagramclone-2828b-default-rtdb.firebaseio.com/").getReference();
        mAuth = FirebaseAuth.getInstance();
        
        directToLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
        Button register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                String txtUsername = username.getText().toString();
                String txtName = name.getText().toString();
                String txtEmail = email.getText().toString().trim();
                String txtPassword = password.getText().toString();
                String txtRePassword = rePassword.getText().toString();
                warning.setVisibility(View.INVISIBLE);
                
                if ( TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtUsername) || TextUtils.isEmpty(txtName)
                        || TextUtils.isEmpty(txtPassword) || TextUtils.isEmpty(txtRePassword) )
                {
                    warning.setText("Required Field Empty");
                    warning.setVisibility(View.VISIBLE);
                }
                
                else if ( !TextUtils.equals(txtPassword, txtRePassword) )
                {
                    warning.setText("Password and Re-Enter Password must match");
                    warning.setVisibility(View.VISIBLE);
                }
                else if ( password.length() < 6 )
                {
                    warning.setText("Password length must be equal or greater than 6");
                    warning.setVisibility(View.VISIBLE);
                }
                else
                    registerUser(txtUsername, txtName, txtEmail, txtPassword);
            }
        });
        
    }
    
    private void registerUser (String username, String name, String email, String password)
    {
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>()
        {
            @Override
            public void onSuccess (AuthResult authResult)
            {
                HashMap<String, Object> map = new HashMap<>();
                map.put("Name", name);
                map.put("Username", username);
                map.put("Email", email);
                map.put("id",mAuth.getUid());
        
                mRootRef.child("Users").setValue(map).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete (@NonNull Task<Void> task)
                    {
                        if ( task.isSuccessful() )
                        {
                            progressDialog.dismiss();
                            Log.d("InstaClone", "Registration Successful");
                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure (@NonNull Exception e)
                    {
                        progressDialog.dismiss();
                        Log.d("InstaClone", e.getMessage().toString());
                        Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure (@NonNull Exception e)
            {
                progressDialog.dismiss();
                Log.d("InstaClone", "Auth Fail : "+e.getMessage().toString());
            }
        });
        
    }
}