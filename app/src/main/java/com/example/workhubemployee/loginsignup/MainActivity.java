package com.example.workhubemployee.loginsignup;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workhubemployee.R;
import com.example.workhubemployee.dashboard.BottomNavigationContainerActivity;
import com.example.workhubemployee.employee.EmployeeBottomNavigationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private EditText email, password;
    private ProgressBar progressBar;
    private TextView forgotPassword, register;
    private FirebaseAuth firebaseAuth;
    private RadioGroup radioGroup;
    private FirebaseFirestore firebaseFirestore;
    Boolean checkEmail= false;
    CheckBox remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button login = findViewById(R.id.log);
        email = findViewById(R.id.emaill);
        password = findViewById(R.id.passwrd);
        progressBar = findViewById(R.id.progressbar);
        firebaseAuth = FirebaseAuth.getInstance();
        remember =findViewById(R.id.remem);
        radioGroup = findViewById(R.id.radioBu);
        forgotPassword = findViewById(R.id.forg);
        register = findViewById(R.id.ress);
        firebaseFirestore =FirebaseFirestore.getInstance();


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, forgotPasswordActivity.class));
            }

        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }

        });


        login.setOnClickListener((v -> {
            String tex_email = email.getText().toString();
            String tex_password = password.getText().toString();

            if (TextUtils.isEmpty(tex_email) || TextUtils.isEmpty(tex_password)) {
                Toast.makeText(MainActivity.this, "All Fields required", Toast.LENGTH_SHORT).show();
            } else {
                checkEmployee(tex_email,tex_password);
            }
        }));

    }



    private void login(String tex_email, String tex_password,String name,String userId) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(tex_email, tex_password).addOnCompleteListener((task) -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {

                if(checkEmail){

                    Intent intent = new Intent(MainActivity.this, EmployeeBottomNavigationActivity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("userId",userId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(MainActivity.this, BottomNavigationContainerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }

            } else {
                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkEmployee(String tex_email,String tex_password){


        firebaseFirestore.collection("employee")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String name = "";
                        String userId = "";
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> list = document.getData();

                                if(tex_email.equals(list.get("email"))){
                                    checkEmail = true;
                                    name = list.get("name").toString();
                                    userId = list.get("userId").toString();
                                }
                            }

                            login(tex_email, tex_password,name,userId);
                        } else {
                            Log.w("data", "Error getting employees.", task.getException());
                        }
                    }

                });


        return checkEmail;
    }



}

