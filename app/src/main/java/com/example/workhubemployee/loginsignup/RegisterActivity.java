package com.example.workhubemployee.loginsignup;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workhubemployee.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private EditText register_email, register_username, register_password, register_confirmpassword, register_contactnumber;
    private Button registerBtn;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        register_email = findViewById(R.id.email);
        register_username = findViewById(R.id.fullnam);
        register_password = findViewById(R.id.pass);
        register_contactnumber = findViewById(R.id.contactnum);
        register_confirmpassword = findViewById(R.id.confpass);
        registerBtn = findViewById(R.id.signin);
        progressBar = findViewById(R.id.progbar);

        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String user_name = register_username.getText().toString();
                final String email = register_email.getText().toString();
                final String txt_password = register_password.getText().toString();
                final String txt_conf = register_confirmpassword.getText().toString();
                final String txt_mobile = register_contactnumber.getText().toString();

                if (user_name == null) {
                    Toast.makeText(RegisterActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
                } else {

                    if (TextUtils.isEmpty(user_name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_conf) ||
                            TextUtils.isEmpty(txt_mobile)) {
                        Toast.makeText(RegisterActivity.this, "Every fields are required", Toast.LENGTH_SHORT).show();
                    } else if (txt_password.equals(txt_conf)) {
                        register(user_name, email, txt_password, txt_conf, txt_mobile);
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Password and confirm password field do not match.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }


    private void register(String user_name, String email, String txt_password, String txt_conf, String txt_mobile) {
        progressBar.setVisibility(View.VISIBLE);
        
        firebaseAuth.createUserWithEmailAndPassword(email, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser rUser = firebaseAuth.getCurrentUser();
                    assert rUser != null;
                    String userId = rUser.getUid();

                    db = FirebaseFirestore.getInstance();

                    HashMap<String, String> user = new HashMap<>();
                    user.put("userId", userId);
                    user.put("businessName", user_name);
                    user.put("email", email);
                    user.put("password", txt_password);
                    user.put("mobile", txt_mobile);
                    user.put("userType", "business");

                    db.collection("business")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    progressBar.setVisibility(View.GONE);
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                    Toast.makeText(RegisterActivity.this, "your account has been created.", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, "Account creation unsucessful "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });



                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}



