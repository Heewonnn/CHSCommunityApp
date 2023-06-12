package com.example.chscommunityprototype1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class aboutActivity extends AppCompatActivity {

    //calling variables
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, setting, about, logOut;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //initializing variables
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        about = findViewById(R.id.info);
        logOut = findViewById(R.id.logOut);
        setting = findViewById(R.id.setting);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        //action - when click menu button
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            } // open drawer
        });
        //action - when click home button
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(aboutActivity.this, MainActivity.class); // move to main page
            }
        });
        //action - when click setting button
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(aboutActivity.this, SettingActivity.class);
            }
        });
//action - when click about button
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
//action - when click log out button
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(aboutActivity.this, SignUpActivity.class);
            }
        });
//update user info
        updateNavHeader();

    }

    //method for drawer and other activities such as moving pages
    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public static void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    public void updateNavHeader(){
        drawerLayout = findViewById(R.id.drawerLayout);
        TextView navUsername = drawerLayout.findViewById(R.id.userName);
        TextView navUserMail = drawerLayout.findViewById(R.id.userMail);

        navUserMail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());

        ImageView navUserPhoto = drawerLayout.findViewById(R.id.userProfile);
        Picasso.with(aboutActivity.this).load(currentUser.getPhotoUrl()).into(navUserPhoto);
    }

}