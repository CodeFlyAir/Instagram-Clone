package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity
{
    private ImageView icon;
    private LinearLayout linearLayout;
    
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    
        Button login = findViewById(R.id.login);
        Button register = findViewById(R.id.register);
        icon = findViewById(R.id.InstagramIcon);
        linearLayout = findViewById(R.id.linearlayout);
        
        linearLayout.animate().alpha(0f).setDuration(1);
        
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -1500);
        animation.setDuration(1000);
        animation.setFillAfter(false);
        animation.setAnimationListener(new AnimationListener());
        icon.setAnimation(animation);
        
        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                startActivity(new Intent(StartActivity.this,RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                startActivity(new Intent(StartActivity.this,LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }
    
    @Override
    protected void onStart ()
    {
        super.onStart();
        
        if( FirebaseAuth.getInstance().getCurrentUser() !=null )
        {
            startActivity(new Intent(StartActivity.this,MainActivity.class));
            finish();
        }
    }
    
    private class AnimationListener implements Animation.AnimationListener
    {
        
        @Override
        public void onAnimationStart (Animation animation)
        {
        
        }
        
        @Override
        public void onAnimationEnd (Animation animation)
        {
            icon.clearAnimation();
            icon.setVisibility(View.INVISIBLE);
            linearLayout.animate().alpha(1f).setDuration(1000);
        }
        
        @Override
        public void onAnimationRepeat (Animation animation)
        {
        
        }
    }
}