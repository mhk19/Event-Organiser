package com.example.eventorganiser;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;

public class AddEvent extends AppCompatActivity {

    private EditText nameOfEvent,date,venueOfEvent,specifications,prerequisite,time;
    Button submitBtn;
    Button back_btn;
    private String nameOfGroup,userType;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        nameOfEvent =  findViewById(R.id.NameOfEvent);
        date =  findViewById(R.id.Date);
        venueOfEvent = findViewById(R.id.VenueOfEvent);
        specifications = findViewById(R.id.Specifications);
        prerequisite = findViewById(R.id.Prerequisite);
        submitBtn = findViewById(R.id.Submit_btn);
        back_btn = findViewById(R.id.Back_btn);
        time = findViewById(R.id.Time);
        progressBar = findViewById(R.id.progressBar_addEvent);

        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameOfGroup = String.valueOf(dataSnapshot.child("groupName").getValue());
                userType = String.valueOf(dataSnapshot.child("userType").getValue());
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
                datePickerDialog = new DatePickerDialog(AddEvent.this,
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
                mTimePicker = new TimePickerDialog(AddEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event event = new Event();
                event.setDate(date.getText().toString());
                event.setName_of_event(nameOfEvent.getText().toString());
                event.setName_of_grp(nameOfGroup);
                event.setPrerequisite(prerequisite.getText().toString());
                event.setSpecifications(specifications.getText().toString());
                event.setVenue(venueOfEvent.getText().toString());
                event.setTime(time.getText().toString());


                String DateAndTime = date.getText().toString().trim();
                String NameOfEvent = nameOfEvent.getText().toString().trim();
                String Prerequisite = prerequisite.getText().toString().trim();
                String Specifications = specifications.getText().toString().trim();
                String Venue = venueOfEvent.getText().toString().trim();
                String Time = time.getText().toString().trim();

                if(TextUtils.isEmpty(NameOfEvent)){
                    nameOfEvent.setError("This field is required");
                }

                else if(TextUtils.isEmpty(DateAndTime)){
                    date.setError("This field is required");
                }

                else if (TextUtils.isEmpty(Time)){
                    time.setError("This field is required");
                }

                else if(TextUtils.isEmpty(Venue)){
                    venueOfEvent.setError("This field is required");
                }

                else if(TextUtils.isEmpty(Specifications)){
                    specifications.setError("This field is required.If there are no specifications then enter none.");
                }

                else if(TextUtils.isEmpty(Prerequisite)){
                    prerequisite.setError("This field is required.If there are no prerequisites then enter none.");
                }

                else {
                    progressBar.setVisibility(View.VISIBLE);
                    new FirebaseDatabaseHelper().addEvent(event, new FirebaseDatabaseHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Event> events, List<String> keys) {

                        }

                        @Override
                        public void DataIsInserted() {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(AddEvent.this, "The event record has been inserted successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MyEvents.class));
                            finish();
                        }
                        @Override
                        public void DataIsDeleted(){

                        }

                    });
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddEvent.this,MyEvents.class));
                finish();
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_at_home,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.Home){
            startActivity(new Intent(getApplicationContext(),Home.class));
            finish();
        }
        else if(id == R.id.AccountDetails){
            if(userType.equals("group")) {
                startActivity(new Intent(getApplicationContext(), AccountDetails.class));
                finish();
            }
            else if(userType.equals("student")) {
                startActivity(new Intent(getApplicationContext(),AccountDetails_Student.class));
                finish();
            }
        }

        else if(id == R.id.MyEvents){
            if(userType.equals("group")) {
                startActivity(new Intent(getApplicationContext(), MyEvents.class));
                finish();
            }
            else if(userType.equals("student")) {
                startActivity(new Intent(getApplicationContext(),MyEventsStudent.class));
                finish();
            }
        }
        else if(id == R.id.Logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
