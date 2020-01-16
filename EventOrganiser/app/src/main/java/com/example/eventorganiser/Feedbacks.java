package com.example.eventorganiser;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Feedbacks extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase, pDatabase;
    String key, userType;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedbacks);

        key = getIntent().getStringExtra("eventKey");

        recyclerView =  findViewById(R.id.FeedbackRecyclerView);
        mDatabase = db.getReference("Events").child(key).child("Feedback");
        mDatabase.keepSynced(true);
        pDatabase = db.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        progressBar = findViewById(R.id.progressBar_feedbacks);

        pDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userType = String.valueOf(dataSnapshot.child("userType").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView username, feedback;

        public FeedbackViewHolder(View view) {
            super(view);
            mView = view;
            username =  mView.findViewById(R.id.feedback_username);
            feedback =  mView.findViewById(R.id.feedback_feedback);
        }

        public void setUsername(String userName) {
            username.setText(userName);
        }

        public void setFeedback(String feedBack) {
            feedback.setText(feedBack);
        }
    }

    protected void onStart() {
        super.onStart();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar.setVisibility(View.VISIBLE);

        FirebaseRecyclerAdapter<Feedback, FeedbackViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Feedback, FeedbackViewHolder>
                (Feedback.class, R.layout.feedbackcards, FeedbackViewHolder.class, mDatabase) {
            @Override
            protected void populateViewHolder(FeedbackViewHolder feedbackViewHolder, Feedback feedback, int i) {

                feedbackViewHolder.setUsername(feedback.getUsername());
                feedbackViewHolder.setFeedback(feedback.getFeedback());
                progressBar.setVisibility(View.GONE);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if(!dataSnapshot.exists()){
                    Toast.makeText(getApplicationContext(),"No feedback or message for this event",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_at_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Home) {
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        }
        else if (id == R.id.AccountDetails) {
            if (userType.equals("group")) {
                startActivity(new Intent(getApplicationContext(), AccountDetails.class));
                finish();
            } else if (userType.equals("student")) {
                startActivity(new Intent(getApplicationContext(), AccountDetails_Student.class));
                finish();
            }
        }
        else if (id == R.id.MyEvents) {
            if(userType.equals("group")) {
                startActivity(new Intent(getApplicationContext(), MyEvents.class));
                finish();
            }
            else if(userType.equals("student")) {
                startActivity(new Intent(getApplicationContext(),MyEventsStudent.class));
                finish();
            }
        }
        else if (id == R.id.Logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}



