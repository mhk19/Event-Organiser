package com.example.eventorganiser;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Edit_Event_Details extends AppCompatActivity {

    private TextView eventName,description,prerequisite,date,time,venue;
    private String EventName,Description,Prerequisite,Date,Time,Venue,key;
    Button Save_changes,delete,back,SeeFeedback;
    DatabaseReference mDatabase;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    String nameOfGroup;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__event__details);
        eventName = findViewById(R.id.EditNameOfEvent);
        description = findViewById(R.id.EditSpecifications);
        prerequisite = findViewById(R.id.EditPrerequisite);
        date = findViewById(R.id.EditDate);
        time = findViewById(R.id.EditTime);
        venue = findViewById(R.id.EditVenueOfEvent);
        Save_changes = findViewById(R.id.EditSaveChanges_btn);
        delete = findViewById(R.id.EditDelete_btn);
        back = findViewById(R.id.EditBack_btn);
        SeeFeedback = findViewById(R.id.see_feedbacks_btn);
        progressBar = findViewById(R.id.progressBar_editEventDetails);

        EventName = getIntent().getStringExtra("EventName");
        Description = getIntent().getStringExtra("Description");
        Prerequisite = getIntent().getStringExtra("Prerequisite");
        Date = getIntent().getStringExtra("Date");
        Time = getIntent().getStringExtra("Time");
        Venue = getIntent().getStringExtra("Venue");
        key = getIntent().getStringExtra("key");

        eventName.setText(EventName);
        description.setText(Description);
        prerequisite.setText(Prerequisite);
        date.setText(Date);
        time.setText(Time);
        venue.setText(Venue);

        mDatabase = FirebaseDatabase.getInstance().getReference("Events").child(key);
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameOfGroup = String.valueOf(dataSnapshot.child("groupName").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(Edit_Event_Details.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                date.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Edit_Event_Details.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        Save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event event = new Event();
                event.setName_of_grp(nameOfGroup);
                event.setName_of_event(eventName.getText().toString().trim());
                event.setSpecifications(description.getText().toString().trim());
                event.setPrerequisite(prerequisite.getText().toString().trim());
                event.setDate(date.getText().toString().trim());
                event.setTime(time.getText().toString().trim());
                event.setVenue(venue.getText().toString().trim());

                if(TextUtils.isEmpty(EventName)){
                    eventName.setError("This field is required");
                }

                else if(TextUtils.isEmpty(Date)){
                    date.setError("This field is required");
                }

                else if (TextUtils.isEmpty(Time)){
                    time.setError("This field is required");
                }

                else if(TextUtils.isEmpty(Venue)){
                    venue.setError("This field is required");
                }

                else if(TextUtils.isEmpty(Description)){
                    description.setError("This field is required.If there are no specifications then enter none.");
                }

                else if(TextUtils.isEmpty(Prerequisite)){
                    prerequisite.setError("This field is required.If there are no prerequisites then enter none.");
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    mDatabase.setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Event Details updated successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MyEvents.class));
                            finish();
                        }
                    });
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                mDatabase.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Event deleted successfully.",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MyEvents.class));
                        finish();
                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyEvents.class));
                finish();
            }
        });

        SeeFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(),Feedbacks.class);
                in.putExtra("eventKey",key);
                startActivity(in);
                finish();
            }
        });
    }
}
