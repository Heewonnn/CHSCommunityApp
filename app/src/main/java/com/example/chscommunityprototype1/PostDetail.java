package com.example.chscommunityprototype1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class PostDetail extends AppCompatActivity {
//code for post page
    TextView detailTopic, detailQuestion;
    EditText textComment;
    ImageView detailImage, detailCommentImage;
    Button add;
    FloatingActionButton deleteButton, editButton;
    String key = "";
    String imageUrl = "";

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    RecyclerView RVComment;
    CommentAdapter commentAdapter;
    List<Comment> listComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        detailImage = findViewById(R.id.postDetailImage);
        detailTopic = findViewById(R.id.postDetailTopic);
        detailQuestion = findViewById(R.id.postDetailQuestion);

        textComment = findViewById(R.id.detailComment);
        detailCommentImage = findViewById(R.id.detailCommentImage);

        add = findViewById(R.id.commentButton);
        deleteButton = findViewById(R.id.postDeleteButton);
        editButton = findViewById(R.id.postEditButton);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        RVComment = findViewById(R.id.commentRecyclerView);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                add.setVisibility(View.INVISIBLE);

                DatabaseReference commentReference = firebaseDatabase.getReference("Comment").child(key).push();
                String comment_content = textComment.getText().toString();
                String uid = currentUser.getUid();
                String uname = currentUser.getDisplayName();
                String uimg = currentUser.getPhotoUrl().toString();

                Comment comment = new Comment(comment_content, uid, uimg, uname);

                commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PostDetail.this, "Comment added", Toast.LENGTH_SHORT).show();
                        textComment.setText("");
                        add.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostDetail.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        
        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            detailQuestion.setText(bundle.getString("Question"));
            detailTopic.setText(bundle.getString("Topic"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            Picasso.with(PostDetail.this).load(bundle.getString("Image")).into(detailImage);
            Picasso.with(PostDetail.this).load(currentUser.getPhotoUrl()).into(detailCommentImage);
        }

        //only user who created qna post can delete or edit the button
        if(!currentUser.getEmail().equals(bundle.getString("Useremail"))){
            deleteButton.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("qna Data");
                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(PostDetail.this, "Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), qnaActivity.class));
                        finish();
                    }
                });
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostDetail.this, PostUpdateActivity.class)
                        .putExtra("Topic", detailTopic.getText().toString())
                        .putExtra("Question", detailQuestion.getText().toString())
                        .putExtra("Image", imageUrl)
                        .putExtra("Key", key);
                startActivity(intent);
            }
        });
        
        iniRVComment();


    }

    private void iniRVComment() {


        RVComment.setLayoutManager(new LinearLayoutManager(this));


        DatabaseReference commentRef = firebaseDatabase.getReference("Comment").child(key);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listComment = new ArrayList<>();
                for(DataSnapshot snap:snapshot.getChildren()){
                    Comment comment = snap.getValue(Comment.class);
                    listComment.add(comment);
                }

                commentAdapter = new CommentAdapter(getApplicationContext(), listComment);
                RVComment.setAdapter(commentAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}