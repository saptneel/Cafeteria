package com.saptneel.cafeteria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chats extends AppCompatActivity {

    ProgressBar progressBar;
    RecyclerView recyclerView;
    ArrayList<User> users;
    UsersAdapter usersAdapter;
    UsersAdapter.OnClickListener onClickListener;
    SwipeRefreshLayout swipeRefreshLayout;
    private String myPic, myUsername, myEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recycler);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        users = new ArrayList<>();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsers();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        onClickListener = new UsersAdapter.OnClickListener() {
            @Override
            public void onClicked(int position) {
                startActivity(new Intent(Chats.this, Message.class)
                        .putExtra("friendUsername", users.get(position).getUsername())
                        .putExtra("friendEmail", users.get(position).getEmail())
                        .putExtra("friendPic", users.get(position).getProfilePic())
                        .putExtra("myPic", myPic)
                        .putExtra("myEmail", myEmail)
                        .putExtra("myUsername", myUsername));
            }
        };

        getUsers();
    }

    private void getUsers() {
        users.clear();
        FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    users.add(dataSnapshot.getValue(User.class));

                usersAdapter = new UsersAdapter(users, Chats.this, onClickListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(Chats.this));
                recyclerView.setAdapter(usersAdapter);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                for(int i = 0; i < users.size(); i++)
                    if(users.get(i).getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        myPic = users.get(i).getProfilePic();
                        myUsername = users.get(i).getUsername();
                        myEmail = users.get(i).getEmail();
                        users.remove(i);
                        return;
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pro_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.profile)
            startActivity(new Intent(Chats.this, Profile.class)
                    .putExtra("myPic", myPic));
        return super.onOptionsItemSelected(item);
    }
}