package com.saptneel.cafeteria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserHolder> {

    ArrayList<User> users;
    Context context;
    OnClickListener onClickListener;

    public UsersAdapter(ArrayList<User> users, Context context, OnClickListener onClickListener) {
        this.users = users;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_holder, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.txtUsername.setText(users.get(position).getUsername());
        Glide.with(context).load(users.get(position).getProfilePic()).error(R.drawable.ic_acc).placeholder(R.drawable.ic_acc).into(holder.imgProfilePic);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserHolder extends RecyclerView.ViewHolder {

        ImageView imgProfilePic;
        TextView txtUsername;
        public UserHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClicked(getAdapterPosition());
                }
            });

            txtUsername = itemView.findViewById(R.id.txtUsername);
            imgProfilePic = itemView.findViewById(R.id.imgProfilePic);
        }
    }

    interface OnClickListener {
        void onClicked(int position);
    }
}
