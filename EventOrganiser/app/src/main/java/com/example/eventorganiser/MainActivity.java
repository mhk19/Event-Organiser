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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    TextView Name,Email,Mobile,EnrollNo,Password,CPassword;
    Button SignUp;
    FirebaseAuth fAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Name = findViewById(R.id.Name1);
        Email = findViewById(R.id.Email1);
        Mobile = findViewById(R.id.Mobile1);
        EnrollNo = findViewById(R.id.Enroll_No1);
        Password = findViewById(R.id.Password1);
        CPassword = findViewById(R.id.Cpassword1);
        SignUp = findViewById(R.id.Signup1);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar_signup_as_student);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name =  Name.getText().toString().trim();
                final String email = Email.getText().toString().trim();
                final String mobile = Mobile.getText().toString().trim();
                final String enrollno = EnrollNo.getText().toString().trim();
                final String password = Password.getText().toString().trim();
                String cpassword = CPassword.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    Name.setError("Name is required");
                }

                else if(TextUtils.isEmpty(email)){
                    Email.setError("Email is Required");
                }

                else if(TextUtils.isEmpty(enrollno)){
                    EnrollNo.setError("Enrollment Number is required");
                }

                else if(TextUtils.isEmpty(mobile)){
                    Mobile.setError("Mobile number is required");
                }

                else if(mobile.length() != 10){
                    Mobile.setError("Invalid mobile number");
                }

                else if(TextUtils.isEmpty(password)){
                    Password.setError("Password is required");
                }

                else if(password.length()<6){
                    Password.setError("Password must be greater than or equal to 6 Characters");
                }
                else if(!cpassword.equals(password)){
                    CPassword.setError("Password and Confirm Password should be equal");
                }

                //register the user in Firebase.

                else {
                    progressBar.setVisibility(View.VISIBLE);
                    fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            UserDetails userDetails = new UserDetails(name, email, enrollno, mobile, "student");

                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Login.class));
                                finish();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
