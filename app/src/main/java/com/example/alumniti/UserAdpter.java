package com.example.alumniti;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdpter extends RecyclerView.Adapter<UserAdpter.ViewHolder> {
    private Context context;
    private ArrayList<Users> usersArrayList;

    public UserAdpter(Context context, ArrayList<Users> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user = usersArrayList.get(position);
        holder.username.setText(user.getUserName());

        // Load the profile picture with error handling
        Picasso.get()
                .load(user.getProfilepic())
                .placeholder(R.drawable.bordermain) // Optional placeholder
                .error(R.drawable.bordermain) // Optional error image
                .into(holder.userimg);

        holder.itemView.setOnClickListener(view -> {
            Log.d("UserAdapter", "User clicked: " + user.getUserName());
            Intent intent = new Intent(context, UserProfileActivity.class);
            intent.putExtra("nameeee", user.getUserName());
            intent.putExtra("reciverImg", user.getProfilepic());
            intent.putExtra("uid", user.getUserId());
            context.startActivity(intent);
        });


    }
    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userimg;
        TextView username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userimg = itemView.findViewById(R.id.userimg);
            username = itemView.findViewById(R.id.username);
        }
    }
}
