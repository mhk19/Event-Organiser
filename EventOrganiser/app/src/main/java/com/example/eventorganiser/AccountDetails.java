package com.example.eventorganiser;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountDetails extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private TextView username,groupName,email;
    Button edit , changePassword;
    String userType;
    String Username,GroupName,Email;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mDatabase.keepSynced(true);

        username = findViewById(R.id.accountDetails_Username);
        groupName = findViewById(R.id.accountDetails_GroupName);
        email = findViewById(R.id.accountDetails_Email);
        edit = findViewById(R.id.AccountDetails_Edit);
        changePassword = findViewById(R.id.AccountDetails_PassChange);
        progressBar = findViewById(R.id.progressBar_ad1);


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountDetails.this,Forgot_Password.class));
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Username = String.valueOf(dataSnapshot.child("username").getValue());
                        GroupName = String.valueOf(dataSnapshot.child("groupName").getValue());
                        Email = String.valueOf(dataSnapshot.child("emailId").getValue());

                        Intent intent = new Intent(AccountDetails.this,UpdateGroupLeaderDetails.class);
                        intent.putExtra("username",Username);
                        intent.putExtra("groupName",GroupName);
                        intent.putExtra("email",Email);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    protected void onStart(){
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username.setText(String.valueOf(dataSnapshot.child("username").getValue()));
                groupName.setText(String.valueOf(dataSnapshot.child("groupName").getValue()));
                email.setText(String.valueOf(dataSnapshot.child("emailId").getValue()));
                userType = String.valueOf(dataSnapshot.child("userType").getValue());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
            }         }
        else if(id == R.id.Logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
