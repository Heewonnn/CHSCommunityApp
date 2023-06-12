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

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

//    DrawerLayout drawerLayout;
//    ImageView menu;
//    LinearLayout home, setting, about, logOut;
//
//    FirebaseAuth firebaseAuth;
//    FirebaseUser currentUser;

    //call variable
    TextView detailDesc, detailDate;

    FloatingActionButton deleteButton, editButton;
    String key = "";

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    //initialize variable
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

//        drawerLayout = findViewById(R.id.drawerLayout);
//        menu = findViewById(R.id.menu);
//        home = findViewById(R.id.home);
//        about = findViewById(R.id.info);
//        logOut = findViewById(R.id.logOut);
//        setting = findViewById(R.id.setting);
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        currentUser = firebaseAuth.getCurrentUser();

        detailDesc = findViewById(R.id.detailDesc);
        detailDate = findViewById(R.id.detailDate);

        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            detailDesc.setText(bundle.getString("Description"));
            detailDate.setText(bundle.getString("Date"));
            key = bundle.getString("Key");
        }

        //remove edit or delete button if the user is not me -- for announcement page
        if(!currentUser.getEmail().equals("hejung@cresskillnj.net")){
            deleteButton.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
        }

        //delete/edit announcement
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Android Tutorials");

                reference.child(key).removeValue();
                Toast.makeText(DetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), announcementActivity.class));
                finish();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class)
                        .putExtra("Date", detailDate.getText().toString())
                        .putExtra("Description", detailDesc.getText().toString())
                        .putExtra("Key", key);
                startActivity(intent);

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
//                redirectActivity(DetailActivity.this, MainActivity.class);
//            }
//        });
//
//        setting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                redirectActivity(DetailActivity.this, SettingActivity.class);
//            }
//        });
//
//        about.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                redirectActivity(DetailActivity.this, aboutActivity.class);
//            }
//        });
//
//        logOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(DetailActivity.this, "Log out", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        updateNavHeader();

    }

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
//        Picasso.with(DetailActivity.this).load(currentUser.getPhotoUrl()).into(navUserPhoto);
//
//    }

}