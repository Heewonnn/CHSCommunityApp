package com.example.chscommunityprototype1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UploadActivity extends AppCompatActivity {

    ///upload announcement

//    DrawerLayout drawerLayout;
//    ImageView menu;
//    LinearLayout home, setting, about, logOut;
//
//    FirebaseAuth firebaseAuth;
//    FirebaseUser currentUser;

    Button saveButton;
    EditText uploadDate, uploadDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

//        drawerLayout = findViewById(R.id.drawerLayout);
//        menu = findViewById(R.id.menu);
//        home = findViewById(R.id.home);
//        about = findViewById(R.id.info);
//        logOut = findViewById(R.id.logOut);
//        setting = findViewById(R.id.setting);
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        currentUser = firebaseAuth.getCurrentUser();

        uploadDate = findViewById(R.id.uploadDate);
        uploadDesc = findViewById(R.id.uploadDescription);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData();
                //saveData();
            }
        });

//        menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openDrawer(drawerLayout);
//            }
//        });
//
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                redirectActivity(UploadActivity.this, MainActivity.class);
//            }
//        });
//
//        setting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                redirectActivity(UploadActivity.this, SettingActivity.class);
//            }
//        });
//
//        about.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                redirectActivity(UploadActivity.this, aboutActivity.class);
//            }
//        });
//
//        logOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(UploadActivity.this, "Log out", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        updateNavHeader();


    }

    public void uploadData(){
        String date = uploadDate.getText().toString();
        String desc = uploadDesc.getText().toString();

        DataClass dataClass = new DataClass(date, desc);

        FirebaseDatabase.getInstance().getReference("Android Tutorials").child(date)
                .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(UploadActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            redirectActivity(UploadActivity.this, announcementActivity.class);

                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

//    public void saveData(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
//        builder.setCancelable(false);
//        builder.setView(R.layout.progress_layout);
////        AlertDialog dialog = builder.create();
////        dialog.show();
//
//        uploadData();
//    }

//    public static void openDrawer(DrawerLayout drawerLayout){
//        drawerLayout.openDrawer(GravityCompat.START);
//    }
//    public static void closeDrawer(DrawerLayout drawerLayout){
//        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
//            drawerLayout.closeDrawer(GravityCompat.START);
//        }
//    }
    public static void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        closeDrawer(drawerLayout);
//    }
//
//    public void updateNavHeader(){
//        drawerLayout = findViewById(R.id.drawerLayout);
//        TextView navUsername = drawerLayout.findViewById(R.id.userName);
//        TextView navUserMail = drawerLayout.findViewById(R.id.userMail);
//
//        navUserMail.setText(currentUser.getEmail());
//        navUsername.setText(currentUser.getDisplayName());
//
//        ImageView navUserPhoto = drawerLayout.findViewById(R.id.userProfile);
//        Picasso.with(UploadActivity.this).load(currentUser.getPhotoUrl()).into(navUserPhoto);
//
//    }
}