package com.example.chscommunityprototype1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword, username;
    private Button signupButton;
    private TextView loginRedirectText;

    ImageView imgUserPhoto;
    static int PReqCode = 1;
    static int REQUECODE = 1;
    Uri pickedImgUrl;
    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE}; //idk


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        username = findViewById(R.id.username);

        imgUserPhoto = findViewById(R.id.userProfile);

        imgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 22){
                    checkAndRequestForPermission();
                }
                else{
                    openGallery();
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String user = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                String namee = username.getText().toString().trim();

                if (user.isEmpty()){
                    signupEmail.setError("Email can not be empty");
                }
                if (pass.isEmpty()){
                    signupPassword.setError("Password can not be empty");
                }
                if (namee.isEmpty()){
                    username.setError("Username can not be empty");
                }
                else{
                    auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Toast.makeText(SignUpActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();

                                //set user info
                                updateUserInfo(namee, pickedImgUrl, auth.getCurrentUser());

                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            } else {
                                Toast.makeText(SignUpActivity.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    //


                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });



    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(SignUpActivity.this, SignUpActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { // not working somehow
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 80){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Download code", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Download cancel", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void updateUserInfo(String name, Uri pickedImgUrl, FirebaseUser currentUser){
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        StorageReference imageFilePath = mStorage.child(pickedImgUrl.getLastPathSegment());
        imageFilePath.putFile(pickedImgUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name).setPhotoUri(uri).build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(SignUpActivity.this, "Sign up Successful", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                });
            }
        });
    }


    private void openGallery(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");

        //

        activityResultLauncher.launch(galleryIntent);


    }

    private void checkAndRequestForPermission(){ //not working for some reason
        if (ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignUpActivity.this, Manifest.permission.READ_MEDIA_IMAGES)){
                Toast.makeText(SignUpActivity.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            }

            else{
                ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PReqCode);
            }

        }
        else{
            openGallery();
        }

    }

    public String getPathFromURI(Uri contentUri){
        String res = null;

        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        pickedImgUrl = data.getData();

                        final String path = getPathFromURI(pickedImgUrl);

                        if(path!=null){
                            File f = new File(path);
                            pickedImgUrl = Uri.fromFile(f);
                        }

                        //imgUserPhoto.setVisibility(View.INVISIBLE); // notsure

//                        Picasso.with(SignUpActivity.this).load(pickedImgUrl).into(imgUserPhoto);

                        imgUserPhoto.setImageURI(pickedImgUrl);

                    }
                }
            }

    );




}