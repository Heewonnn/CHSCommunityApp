package com.example.chscommunityprototype1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

public class SettingActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu, profile;
    EditText editName, editEmail, editPassword;
    TextView profileName, profileEmail;
    LinearLayout home, setting, about, logOut;

    Button updateProfile;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    Uri uri;
    DatabaseReference databaseReference;
    String imageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        about = findViewById(R.id.info);
        logOut = findViewById(R.id.logOut);
        setting = findViewById(R.id.setting);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        profile = findViewById(R.id.settingProfile);
        profileName = findViewById(R.id.settingName);
        profileEmail = findViewById(R.id.settingEmail);

        editName = findViewById(R.id.editUsername);
        editEmail = findViewById(R.id.editUseremail);
        editPassword = findViewById(R.id.editUserpassword);

        updateProfile = findViewById(R.id.profileEditButton);

        Picasso.with(this).load(currentUser.getPhotoUrl()).into(profile);
        profileName.setText(currentUser.getDisplayName());
        profileEmail.setText(currentUser.getEmail());

        editName.setText(currentUser.getDisplayName());
        editEmail.setText(currentUser.getEmail());

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            profile.setImageURI(uri);

                        } else{
                            Toast.makeText(SettingActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = editEmail.getText().toString().trim();
                String pass = editPassword.getText().toString().trim();
                String name = editName.getText().toString().trim();

                if (user.isEmpty()) {
                    editEmail.setError("Email can not be empty");
                }
                if (pass.isEmpty()) {
                    editPassword.setError("Password can not be empty");
                }
                if (name.isEmpty()) {
                    editName.setError("Username can not be empty");
                } else{
                    currentUser.updateEmail(user);
                    currentUser.updatePassword(pass);
                    UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name).setPhotoUri(uri).build();

                    currentUser.updateProfile(request)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(SettingActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SettingActivity.this, SignUpActivity.class));
                                    }
                                }
                            });
                }

                startActivity(new Intent(SettingActivity.this, SignUpActivity.class));
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
                redirectActivity(SettingActivity.this, MainActivity.class);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(SettingActivity.this, aboutActivity.class);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(SettingActivity.this, SignUpActivity.class);
            }
        });

        updateNavHeader();

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

        navUserMail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());

        ImageView navUserPhoto = drawerLayout.findViewById(R.id.userProfile);
        Picasso.with(SettingActivity.this).load(currentUser.getPhotoUrl()).into(navUserPhoto);
    }

}