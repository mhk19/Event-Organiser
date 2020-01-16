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

public class AccountDetails_Student extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private TextView username,enrollNo,email,mobileNo;
    Button edit , changePassword;
    String userType;
    String Username,EnrollNo,Email,MobileNo;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details__student);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mDatabase.keepSynced(true);

        username = findViewById(R.id.accountDetails_Student_Username);
        enrollNo = findViewById(R.id.accountDetails_Student_EnrollNo);
        email = findViewById(R.id.accountDetails_Student_Email);
        edit = findViewById(R.id.AccountDetails_Student_Edit);
        changePassword = findViewById(R.id.AccountDetails_Student_PassChange);
        mobileNo = findViewById(R.id.accountDetails_Student_MobileNo);
        progressBar = findViewById(R.id.progressBar_ad2);



        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountDetails_Student.this,Forgot_Password.class));
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Username = String.valueOf(dataSnapshot.child("username").getValue());
                        EnrollNo = String.valueOf(dataSnapshot.child("enrollNo").getValue());
                        Email = String.valueOf(dataSnapshot.child("emailId").getValue());
                        MobileNo = String.valueOf(dataSnapshot.child("mobileNo").getValue());
                        userType = String.valueOf(dataSnapshot.child("userType").getValue());

                        Intent intent = new Intent(AccountDetails_Student.this,UpdateStudentDetails.class);
                        intent.putExtra("username",Username);
                        intent.putExtra("enrollNo",EnrollNo);
                        intent.putExtra("email",Email);
                        intent.putExtra("mobileNo",MobileNo);
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
                enrollNo.setText(String.valueOf(dataSnapshot.child("enrollNo").getValue()));
                email.setText(String.valueOf(dataSnapshot.child("emailId").getValue()));
                mobileNo.setText(String.valueOf(dataSnapshot.child("mobileNo").getValue()));
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
