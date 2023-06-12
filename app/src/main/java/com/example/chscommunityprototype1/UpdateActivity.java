package com.example.chscommunityprototype1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UpdateActivity extends AppCompatActivity {
//update announcement
    EditText updateDesc, updateTitle;
    String title, desc;
    String key;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        updateButton = findViewById(R.id.updateButton);
        updateDesc = findViewById(R.id.updateDescription);
        updateTitle = findViewById(R.id.updateDate);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            updateTitle.setText(bundle.getString("Date"));
            updateDesc.setText(bundle.getString("Description"));
            key = bundle.getString("Key");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Android Tutorials").child(key);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
                redirectActivity(UpdateActivity.this, announcementActivity.class);
            }
        });

    }

    public static void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    public void updateData(){
        title = updateTitle.getText().toString().trim();
        desc = updateDesc.getText().toString().trim();

        DataClass dataClass = new DataClass(title, desc);

        databaseReference.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(UpdateActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    redirectActivity(UpdateActivity.this, announcementActivity.class);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}