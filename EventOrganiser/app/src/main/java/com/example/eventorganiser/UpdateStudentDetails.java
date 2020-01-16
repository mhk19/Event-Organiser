package com.example.eventorganiser;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateStudentDetails extends AppCompatActivity {

    String username, enrollNo, email,mobileNo;
    TextView userName, EnrollNo, Email, MobileNo;
    Button update , back;
    private DatabaseReference mDatabase;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student_details);
        username = getIntent().getStringExtra("username");
        enrollNo = getIntent().getStringExtra("enrollNo");
        email = getIntent().getStringExtra("email");
        mobileNo = getIntent().getStringExtra("mobileNo");

        userName =  findViewById(R.id.Edit2_Username);
        EnrollNo =  findViewById(R.id.Edit2_EnrollNo);
        Email =  findViewById(R.id.Edit2_Email);
        update =  findViewById(R.id.Edit2_Update);
        back = findViewById(R.id.Edit2_Back);
        MobileNo =findViewById(R.id.Edit2_MobileNo);
        progressBar = findViewById(R.id.progressBar_upadteStudentDetails);

        userName.setText(username);
        EnrollNo.setText(enrollNo);
        Email.setText(email);
        MobileNo.setText(mobileNo);

        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Username = userName.getText().toString().trim();
                final String Enroll_no = EnrollNo.getText().toString().trim();
                final String emailId = Email.getText().toString().trim();
                final String Mobile_no = MobileNo.getText().toString().trim();

                if(TextUtils.isEmpty(username)){
                    userName.setError("Name is required");
                }

                else if(TextUtils.isEmpty(email)){
                    Email.setError("Email is Required");
                }

                else if(TextUtils.isEmpty(enrollNo)){
                    EnrollNo.setError("Enrollment Number is required");
                }

                else if(TextUtils.isEmpty(mobileNo)){
                    MobileNo.setError("Mobile number is required");
                }

                else if(mobileNo.length() != 10){
                    MobileNo.setError("Invalid mobile number");
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    UserDetails userDetails = new UserDetails(Username, emailId, Enroll_no, Mobile_no, "student");
                    mDatabase.setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), AccountDetails_Student.class));
                            finish();
                        }
                    });
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AccountDetails_Student.class));
                finish();
            }
        });
    }
}
