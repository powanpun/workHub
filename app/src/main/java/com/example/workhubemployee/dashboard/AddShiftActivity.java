package com.example.workhubemployee.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workhubemployee.R;
import com.example.workhubemployee.dashboard.bottomFragments.HomeFragment;
import com.example.workhubemployee.models.EmployeeModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.HashMap;

public class AddShiftActivity extends AppCompatActivity {

    ImageView backBtn;
    private RecyclerView employeeList;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    public String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shift);

        backBtn = findViewById(R.id.backBtn);
        firebaseFirestore =  FirebaseFirestore.getInstance();
        employeeList = findViewById(R.id.rvAddShift);

        employeeList.setHasFixedSize(true);

        displyAllEmployee();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        employeeList.setLayoutManager(linearLayoutManager);
        employeeList.setAdapter(adapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void displyAllEmployee() {

        Query query = firebaseFirestore.collection("employee");

        FirestoreRecyclerOptions<EmployeeModel> options = new FirestoreRecyclerOptions.Builder<EmployeeModel>().setQuery(query,EmployeeModel.class).build();

        adapter = new FirestoreRecyclerAdapter<EmployeeModel, AddShiftActivity.EmployeeViewHolder>(options) {
            @NonNull
            @Override
            public AddShiftActivity.EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getApplication()).inflate(  R.layout.item_employee,parent,false);
                return new AddShiftActivity.EmployeeViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull  AddShiftActivity.EmployeeViewHolder holder, int position, @NonNull  EmployeeModel model) {
                holder.employeeName.setText(model.getName());
                holder.employeeEmail.setText(model.getEmail());
                holder.employeePhn.setText(model.getMobile());
                holder.employeePost.setText(model.getDesignation());
                holder.mcvSingleEmployee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDatePicker(model.getName(),model.getUserId());
                    }
                });
            }
        };

    }

    public void showDatePicker(String name, String id){
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(AddShiftActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        showBottomDialog(date, name,id);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }

    public  class EmployeeViewHolder extends RecyclerView.ViewHolder {

        private TextView employeeName;
        private TextView employeeEmail;
        private TextView employeePhn;
        private TextView employeePost;
        private MaterialCardView mcvSingleEmployee;

        public  EmployeeViewHolder(@NonNull View itemView){
            super(itemView);
            employeeName = itemView.findViewById(R.id.itemEmployeeName);
            employeeEmail = itemView.findViewById(R.id.itemEmployeeEmail);
            employeePhn = itemView.findViewById(R.id.itemEmployeePhn);
            employeePost = itemView.findViewById(R.id.itemEmployeePost);
            mcvSingleEmployee = itemView.findViewById(R.id.mcvSingleEmployee);
        }

    }

    private void showBottomDialog(String date, String name, String id){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AddShiftActivity.this);
        bottomSheetDialog.setContentView(R.layout.add_shift_bottom_dialog);

        EditText startTime = bottomSheetDialog.findViewById(R.id.shiftStartTime);
        EditText endTime = bottomSheetDialog.findViewById(R.id.shiftEndTime);

        MaterialCardView submit = bottomSheetDialog.findViewById(R.id.mcvSubmit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shiftStartTime = startTime.getText().toString();
                String shiftEndTime = endTime.getText().toString();

                if(TextUtils.isEmpty(shiftEndTime) || TextUtils.isEmpty(shiftEndTime) ){
                    Toast.makeText(getApplication(),"Please enter start and end time",Toast.LENGTH_SHORT).show();
                }else{
                    HashMap<String, String> chat = new HashMap<>();
                    chat.put("date", date);
                    chat.put("employeeId", id);
                    chat.put("employeeName", name);
                    chat.put("shiftEnd", shiftEndTime);
                    chat.put("shiftStart", shiftStartTime);

                    firebaseFirestore.collection("business").document("Ygac4TwI6NvjIKYPZuxe").collection("shift")
                            .add(chat)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                 finish();
                                    Toast.makeText(getApplicationContext(), "Shift successfully created", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error in creating shift"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });

        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}