package com.example.chscommunityprototype1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

//show view/info for comment

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context mContext;
    private List<Comment> mData;


    public CommentAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Picasso.with(mContext).load(mData.get(position).getUimg()).into(holder.img_user);
        holder.tv_name.setText(mData.get(position).getUname());
        holder.tv_content.setText(mData.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{

        ImageView img_user;
        TextView tv_name, tv_content;

        public CommentViewHolder(View itemView){
            super(itemView);

            img_user = itemView.findViewById(R.id.commentUserImage);
            tv_name = itemView.findViewById(R.id.commentUserName);
            tv_content = itemView.findViewById(R.id.commentUserComment);
        }


    }






}
