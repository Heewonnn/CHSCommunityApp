package com.example.chscommunityprototype1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;
import com.squareup.picasso.Picasso;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    CardView announcementCard, qnaCard;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, setting, about, logOut;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    Uri photoUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        announcementCard = findViewById(R.id.announcementCard);
        qnaCard = findViewById(R.id.qnaCard);
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        about = findViewById(R.id.info);
        logOut = findViewById(R.id.logOut);
        setting = findViewById(R.id.setting);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        announcementCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, announcementActivity.class);
                startActivity(intent);
            }
        });

        qnaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, qnaActivity.class);
                startActivity(intent);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, SettingActivity.class);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, aboutActivity.class);
            }
        });
        
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, SignUpActivity.class);
            }
        });

        updateNavHeader();

    }

    @Override
    // stop user from going back and going to wrong page -- like log in or signup page
    public void onBackPressed(){
        startActivity(new Intent(MainActivity.this, MainActivity.class));
    }

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

        ImageView navUserPhoto = drawerLayout.findViewById(R.id.userProfile);

        navUserMail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());

        //image

        // notsure

        //navUserPhoto.setImageURI(currentUser.getPhotoUrl());

        Picasso.with(MainActivity.this).load(currentUser.getPhotoUrl()).into(navUserPhoto);

//        photoUser = currentUser.getPhotoUrl();
//        String path = getPathFromURI(photoUser);
//        File f = new File(path);
//        photoUser = Uri.fromFile(f);
//        navUserPhoto.setImageURI(photoUser);


    }

//    public String getPathFromURI(Uri contentUri){ // not used for now
//        String res = null;
//
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
//        if(cursor.moveToFirst()){
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            res = cursor.getString(column_index);
//        }
//        cursor.close();
//        return res;
//    }

}