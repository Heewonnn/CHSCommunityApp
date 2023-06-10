package com.example.chscommunityprototype1;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder>{
    private Context context;
    private List<PostDataClass> dataList;

    public PostAdapter(Context context, List<PostDataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }


    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.postTopic.setText(dataList.get(position).getDataTopic());
        holder.postQuestion.setText(dataList.get(position).getDataQuestion());

        holder.postCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostDetail.class);
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getDataImage());
                intent.putExtra("Topic", dataList.get(holder.getAdapterPosition()).getDataTopic());
                intent.putExtra("Question", dataList.get(holder.getAdapterPosition()).getDataQuestion());
                intent.putExtra("Key", dataList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("Useremail", dataList.get(holder.getAdapterPosition()).getUseremail());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

class PostViewHolder extends RecyclerView.ViewHolder{
    TextView postTopic, postQuestion;
    CardView postCard;

    public PostViewHolder(@NonNull View itemView){
        super(itemView);
        postTopic = itemView.findViewById(R.id.postTopic);
        postQuestion = itemView.findViewById(R.id.postQuestion);
        postCard = itemView.findViewById(R.id.postCard);


    }


}
