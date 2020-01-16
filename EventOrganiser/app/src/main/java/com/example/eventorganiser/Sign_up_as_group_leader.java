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

public class Sign_up_as_group_leader extends AppCompatActivity {

    TextView GroupName,Email,Password,CPassword,Username;
    Button SignUp;
    FirebaseAuth fAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_as_group_leader);

        GroupName = findViewById(R.id.GroupName2);
        Email = findViewById(R.id.Email2);
        Password = findViewById(R.id.Password2);
        CPassword = findViewById(R.id.Cpassword2);
        SignUp = findViewById(R.id.Signup2);
        Username = findViewById(R.id.Username2);
        progressBar = findViewById(R.id.progressBar_signup_as_gl);

        fAuth = FirebaseAuth.getInstance();

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = Email.getText().toString().trim();
                final String password = Password.getText().toString().trim();
                final String groupname = GroupName.getText().toString().trim();
                String cpassword = CPassword.getText().toString().trim();
                final String username = Username.getText().toString().trim();

                if(TextUtils.isEmpty(username)){
                    Username.setError("Username is Required");
                }

                else if(TextUtils.isEmpty(groupname)){
                    GroupName.setError("Group Name is required");
                }

                else if(TextUtils.isEmpty(email)){
                    Email.setError("Email is Required");
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

                else {
                    progressBar.setVisibility(View.VISIBLE);
                    fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                UserDetails userDetails = new UserDetails(username, groupname, email, "group");
                                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(Sign_up_as_group_leader.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(getApplicationContext(), Login.class));
                                            finish();
                                        }
                                    }
                                });
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(Sign_up_as_group_leader.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
