package com.example.chscommunityprototype1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;

public class PostUpdateActivity extends AppCompatActivity {

    ImageView updateImage;
    Button updateButton;
    EditText updateQuestion;
    String topic, question;
    String imageUrl;
    String key, oldImageUrl;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    String[] topicList = {"Homework", "College", "Schedule", "Club", "Other"};

    boolean imageChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_update);

        updateButton = findViewById(R.id.postUpdateButton);
        //updateTopic = findViewById(R.id.postUpdateTopic);
        updateQuestion = findViewById(R.id.postUpdateQuestion);
        updateImage = findViewById(R.id.postUpdateImage);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        autoCompleteTextView = findViewById(R.id.postUpdateTopic);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, topicList);

        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                topic = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(PostUpdateActivity.this, "Topic: ", Toast.LENGTH_SHORT).show();
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            updateImage.setImageURI(uri);
                            imageChange = true;
                        } else{
                            Toast.makeText(PostUpdateActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Picasso.with(PostUpdateActivity.this).load(bundle.getString("Image")).into(updateImage);
            //autoCompleteTextView.setText(bundle.getString("Topic"));
            updateQuestion.setText(bundle.getString("Question"));
            key = bundle.getString("Key");
            oldImageUrl = bundle.getString("Image");
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("qna Data").child(key);

        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePost();
                Intent intent = new Intent(PostUpdateActivity.this, qnaActivity.class);
                startActivity(intent);
            }
        });

    }

    public void savePost(){
        if (imageChange){
            storageReference = FirebaseStorage.getInstance().getReference().child("qna Data").child(uri.getLastPathSegment());

            AlertDialog.Builder builder = new AlertDialog.Builder(PostUpdateActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uriTask.isComplete());
                    Uri uriImage = uriTask.getResult();
                    imageUrl = uriImage.toString();
                    updatePost();
                    dialog.dismiss();


                }
            });
        }
        else{
            imageUrl = oldImageUrl;
            updatePost();
        }

    }

    public void updatePost(){
        question = updateQuestion.getText().toString().trim();

        PostDataClass dataClass = new PostDataClass(topic, currentUser.getEmail(), question, imageUrl);

        databaseReference.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl);
                    reference.delete();
                    Toast.makeText(PostUpdateActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostUpdateActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}