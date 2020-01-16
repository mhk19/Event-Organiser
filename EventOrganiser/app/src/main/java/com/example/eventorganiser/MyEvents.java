package com.example.eventorganiser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MyEvents extends AppCompatActivity {

    FirebaseDatabase db= FirebaseDatabase.getInstance();
    FloatingActionButton addEvent;
    private RecyclerView mRecyclerView;
    DatabaseReference mDatabase,pDatabase;
    Query rDatabase;
    private String grpName,userType;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        addEvent =  findViewById(R.id.AddEvent);
        mRecyclerView = findViewById(R.id.MyEventsGLRecyclerView);
        mDatabase = db.getReference().child("Events");
        mDatabase.keepSynced(true);
        pDatabase = db.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        rDatabase=mDatabase.orderByChild("name_of_grp");
        rDatabase.keepSynced(true);
        progressBar = findViewById(R.id.progressBar_myEvents);

        pDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                grpName = String.valueOf(dataSnapshot.child("groupName").getValue());
                userType = String.valueOf(dataSnapshot.child("userType").getValue());
                onStart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyEvents.this, AddEvent.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerAdapter<Event,EventViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Event, EventViewHolder>
                (Event.class,R.layout.cards_my_events_gl, EventViewHolder.class,rDatabase.equalTo(grpName)){
            protected void populateViewHolder(EventViewHolder viewHolder, final Event model, int position) {
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
                        String NameofEvent = model.getName_of_event();
                        String description = model.getSpecifications();
                        String prerequisite = model.getPrerequisite();
                        String date = model.getDate();
                        String time = model.getTime();
                        String venue = model.getVenue();
                        String key = model.getKey();

                        Intent in = new Intent(MyEvents.this,Edit_Event_Details.class);
                        in.putExtra("EventName",NameofEvent);
                        in.putExtra("Description",description);
                        in.putExtra("Prerequisite",prerequisite);
                        in.putExtra("Date",date);
                        in.putExtra("Time",time);
                        in.putExtra("Venue",venue);
                        in.putExtra("key",key);
                        startActivity(in);
                        finish();
                    }
                });
            }
        };

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatabase.orderByChild("name_of_grp").equalTo(grpName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"You haven't added any event yet",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }, 100);


        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View mView;
        TextView EventName,Description,Prerequisite,Date,Time,Venue;
        ItemClickListener itemClickListener;

        public EventViewHolder(View itemView){
            super(itemView);
            mView=itemView;
            mView.setClickable(true);
            itemView.setOnClickListener(this);

            EventName = mView.findViewById(R.id.gl_EventName);
            Description = mView.findViewById(R.id.gl_Description);
            Prerequisite = mView.findViewById(R.id.gl_CardPrerequisite);
            Date = mView.findViewById(R.id.gl_CardDate);
            Time = mView.findViewById(R.id.gl_CardTime);
            Venue = mView.findViewById(R.id.gl_Venue);

        }



        public void setName_of_Grp(String name_of_grp){
            TextView GrpName = mView.findViewById(R.id.gl_GrpName);
            GrpName.setText(name_of_grp);
        }
        public void setName_of_Event(String name_of_event){
            EventName.setText(name_of_event);
        }
        public void setDescription(String description){
            Description.setText(description);
        }
        public void setPrerequisite(String prerequisite){
            Prerequisite.setText(prerequisite);
        }
        public void setDate(String date){
            Date.setText(date);
        }
        public void setTime(String time){
            Time.setText(time);
        }
        public void setVenue(String venue){
            Venue.setText(venue);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
            this.itemClickListener.onClick(view,getAdapterPosition());
        }

        public void setItemClickListener(ItemClickListener ic){
            this.itemClickListener = ic;
        }
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
            }         }
        else if(id == R.id.Logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
