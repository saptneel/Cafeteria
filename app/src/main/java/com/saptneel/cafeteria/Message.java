package com.saptneel.cafeteria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Message extends AppCompatActivity {

    ImageView imgFriendPic, imgSend;
    TextView txtFriendUsername;
    EditText edtMsg;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    MsgAdapter msgAdapter;
    ArrayList<Msg> messages;
    String friendUsername, friendEmail, myUsername, myEmail, chatRoomID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        friendEmail = getIntent().getStringExtra("friendEmail");
        myEmail = getIntent().getStringExtra("myEmail");
        friendUsername = getIntent().getStringExtra("friendUsername");
        myUsername = getIntent().getStringExtra("myUsername");

        recyclerView = findViewById(R.id.recycler);
        progressBar = findViewById(R.id.progressBar);
        edtMsg = findViewById(R.id.edtMsg);
        txtFriendUsername = findViewById(R.id.txtFriend);
        imgFriendPic = findViewById(R.id.imgFriend);
        imgSend = findViewById(R.id.sendMsg);

        Glide.with(Message.this).load(getIntent().getStringExtra("friendPic"))
                .placeholder(R.drawable.ic_acc).error(R.drawable.ic_acc).into(imgFriendPic);
        txtFriendUsername.setText(getIntent().getStringExtra("friendUsername"));

        messages = new ArrayList<>();
        msgAdapter = new MsgAdapter(Message.this, messages,
                getIntent().getStringExtra("myPic"), getIntent().getStringExtra("friendPic"));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(msgAdapter);

        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtMsg.getText().toString().equals("")) {
                    FirebaseDatabase.getInstance().getReference("messages/" + chatRoomID).push()
                            .setValue(new Msg(myEmail, friendEmail, edtMsg.getText().toString()));
                    edtMsg.setText("");
                }
            }
        });

        setupRoom();
    }

    private void setupRoom() {
        FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(friendUsername.compareTo(myUsername) > 0) {
                            chatRoomID = myUsername + friendUsername;
                        } else {
                            chatRoomID = friendUsername + myUsername;
                        }
                        attachListener(chatRoomID);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void attachListener(String chatRoomID) {
        FirebaseDatabase.getInstance().getReference("messages/" + chatRoomID)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    messages.add(dataSnapshot.getValue(Msg.class));
                }
                msgAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size()-1);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}