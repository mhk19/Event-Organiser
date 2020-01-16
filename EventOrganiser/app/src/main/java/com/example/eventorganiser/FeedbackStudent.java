package com.example.eventorganiser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FeedbackStudent extends AppCompatActivity {

    Button addToMyEvents, submitFeedback,back;
    TextView feedback;
    DatabaseReference mDatabase, pDatabase;
    String eventKey, username;
    String groupName,eventName,eventSpecs,eventPrerequisite,eventDate,eventTime,eventVenue;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_student);

        addToMyEvents =  findViewById(R.id.AddToMyEvents);
        submitFeedback =  findViewById(R.id.submit_feedback_btn);
        back = findViewById(R.id.feedback_back_btn);
        feedback =  findViewById(R.id.feedback);
        eventKey = getIntent().getStringExtra("eventKey");
        groupName = getIntent().getStringExtra("groupName");
        eventName = getIntent().getStringExtra("eventName");
        eventDate = getIntent().getStringExtra("eventDate");
        eventTime = getIntent().getStringExtra("eventTime");
        eventPrerequisite = getIntent().getStringExtra("eventPrerequisite");
        eventSpecs = getIntent().getStringExtra("eventSpecs");
        eventVenue = getIntent().getStringExtra("eventVenue");
        progressBar = findViewById(R.id.progressBar_feedbackStudent);

        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MyEvents");
        pDatabase = FirebaseDatabase.getInstance().getReference("Events").child(eventKey).child("Feedback");


        addToMyEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = mDatabase.push().getKey();
                Event event = new Event(groupName,eventName,eventDate,eventVenue,eventSpecs,eventPrerequisite,eventTime,eventKey);
                mDatabase.child(key).setValue(event);
                Toast.makeText(getApplicationContext(),"Event added successfully",Toast.LENGTH_SHORT).show();
            }
        });

        submitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String key = pDatabase.push().getKey();
                progressBar.setVisibility(View.VISIBLE);
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        username = String.valueOf(dataSnapshot.child("username").getValue());
                        pDatabase.child(key).child("username").setValue(username);
                        pDatabase.child(key).child("feedback").setValue(feedback.getText().toString().trim());
                        Toast.makeText(getApplicationContext(),"Feedback Submitted successfully",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Home.class));
                finish();
            }
        });
    }
}
