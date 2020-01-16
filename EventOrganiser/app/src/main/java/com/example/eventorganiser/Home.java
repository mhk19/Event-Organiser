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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Home extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    DatabaseReference pDatabase;
    FloatingActionButton calendarBtn;
    String date , userType;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mRecyclerView =  findViewById(R.id.HomeRecyclerView);
        calendarBtn = findViewById(R.id.calendar_btn);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        mDatabase.keepSynced(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pDatabase = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        progressBar = findViewById(R.id.progressBar_home);

        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Calender_View.class));
            }
        });


        pDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userType = String.valueOf(dataSnapshot.child("userType").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       date = getIntent().getStringExtra("date");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(date == null){
            java.util.Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("d/M/yyyy");
            date = df.format(c);
        }
        progressBar.setVisibility(View.VISIBLE);
        FirebaseRecyclerAdapter<Event,EventViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Event,EventViewHolder>
                (Event.class,R.layout.material_card_view,EventViewHolder.class,mDatabase.orderByChild("date").equalTo(date)){
            protected void populateViewHolder(EventViewHolder viewHolder, final Event model, int position){
                viewHolder.setName_of_Grp(model.getName_of_grp());
                viewHolder.setName_of_Event(model.getName_of_event());
                viewHolder.setDescription("Specifications: "+model.getSpecifications());
                viewHolder.setPrerequisite("Prerequisite: "+model.getPrerequisite());
                viewHolder.setDate("Date: "+model.getDate());
                viewHolder.setTime("Time: "+model.getTime());
                viewHolder.setVenue(model.getVenue());
                progressBar.setVisibility(View.GONE);


                    viewHolder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            Intent in = new Intent(getApplicationContext(), FeedbackStudent.class);
                            in.putExtra("groupName", model.getName_of_grp());
                            in.putExtra("eventName", model.getName_of_event());
                            in.putExtra("eventSpecs", model.getSpecifications());
                            in.putExtra("eventDate", model.getDate());
                            in.putExtra("eventTime", model.getTime());
                            in.putExtra("eventPrerequisite", model.getPrerequisite());
                            in.putExtra("eventVenue", model.getVenue());
                            in.putExtra("eventKey", model.getKey());
                            if(userType.equals("student")) {
                                startActivity(in);
                                finish();
                            }
                        }
                    });
                }

        };

        mDatabase.orderByChild("date").equalTo(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"No events on this date",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        View mView;
        ItemClickListener itemClickListener;

        public EventViewHolder(View itemView){
            super(itemView);
            mView=itemView;
            mView.setClickable(true);
            itemView.setOnClickListener(this);
        }
        public void setName_of_Grp(String name_of_grp){
            TextView GrpName = mView.findViewById(R.id.GrpName);
            GrpName.setText(name_of_grp);
        }
        public void setName_of_Event(String name_of_event){
            TextView EventName = mView.findViewById(R.id.EventName);
            EventName.setText(name_of_event);
        }
        public void setDescription(String description){
            TextView Description = mView.findViewById(R.id.Description);
            Description.setText(description);
        }
        public void setPrerequisite(String prerequisite){
            TextView Prerequisite = mView.findViewById(R.id.CardPrerequisite);
            Prerequisite.setText(prerequisite);
        }
        public void setDate(String date){
            TextView Date = mView.findViewById(R.id.CardDate);
            Date.setText(date);
        }
        public void setTime(String time){
            TextView Time = mView.findViewById(R.id.CardTime);
            Time.setText(time);
        }
        public void setVenue(String venue){
            TextView Venue = mView.findViewById(R.id.Venue);
            Venue.setText(venue);
        }

        public void onClick(View view){
            if (itemClickListener!=null){
                this.itemClickListener.onClick(view,getAdapterPosition());
            }
        }
        public void setItemClickListener(ItemClickListener ic){
            this.itemClickListener = ic;
        }

    }




    @Override
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
            if(userType.equals("student")){
                startActivity(new Intent(getApplicationContext(),AccountDetails_Student.class));
                finish();
            }
            else if(userType.equals("group")){
                startActivity(new Intent(getApplicationContext(),AccountDetails.class));
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
