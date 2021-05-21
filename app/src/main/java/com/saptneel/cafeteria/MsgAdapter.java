package com.saptneel.cafeteria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MsgHolder> {

    Context context;
    ArrayList<Msg> messages;
    String senderImg, receiverImg;

    public MsgAdapter(Context context, ArrayList<Msg> messages, String senderImg, String receiverImg) {
        this.context = context;
        this.messages = messages;
        this.senderImg = senderImg;
        this.receiverImg = receiverImg;
    }

    @NonNull
    @Override
    public MsgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.msg_holder, parent, false);
        return new MsgHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgHolder holder, int position) {
        holder.txtMsg.setText(messages.get(position).getContent());

        ConstraintSet cs = new ConstraintSet();
        cs.clone(holder.constraintLayout);
        if(messages.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
            Glide.with(context).load(senderImg).error(R.drawable.ic_acc).placeholder(R.drawable.ic_acc)
                    .into(holder.imgSenderPic);
            cs.clear(R.id.cardView, ConstraintSet.LEFT);
            cs.clear(R.id.txtMsg, ConstraintSet.LEFT);
            cs.connect(R.id.cardView, ConstraintSet.RIGHT, R.id.constraintLayout, ConstraintSet.RIGHT, 0);
            cs.connect(R.id.txtMsg, ConstraintSet.RIGHT, R.id.cardView, ConstraintSet.LEFT, 0);
        } else {
            Glide.with(context).load(receiverImg).error(R.drawable.ic_acc).placeholder(R.drawable.ic_acc)
                    .into(holder.imgSenderPic);
            cs.clear(R.id.cardView, ConstraintSet.RIGHT);
            cs.clear(R.id.txtMsg, ConstraintSet.RIGHT);
            cs.connect(R.id.cardView, ConstraintSet.LEFT, R.id.constraintLayout, ConstraintSet.LEFT, 0);
            cs.connect(R.id.txtMsg, ConstraintSet.LEFT, R.id.cardView, ConstraintSet.RIGHT, 0);
        }
        cs.applyTo(holder.constraintLayout);

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MsgHolder extends RecyclerView.ViewHolder {
        TextView txtMsg;
        ImageView imgSenderPic;
        ConstraintLayout constraintLayout;
        public MsgHolder(@NonNull View itemView) {
            super(itemView);
            txtMsg = itemView.findViewById(R.id.txtMsg);
            imgSenderPic = itemView.findViewById(R.id.imgProfilePic);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }
}
