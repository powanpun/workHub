package com.example.workhubemployee.dashboard.bottomFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workhubemployee.R;
import com.example.workhubemployee.employee.EmployeeBottomNavigationActivity;
import com.example.workhubemployee.employee.employeeFragments.EmployeeHomeFragment;
import com.example.workhubemployee.models.AttendanceModel;
import com.example.workhubemployee.models.EmployeeModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Objects;


public class HomeFragment extends Fragment {

    private MaterialCardView materialCardView;
    private ContentLoadingProgressBar contentLoadingProgressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private RecyclerView employeeList;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    FirestoreRecyclerAdapter empDetailAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        materialCardView = view.findViewById(R.id.addEmployee);
        contentLoadingProgressBar = view.findViewById(R.id.progressbar);
        employeeList = view.findViewById(R.id.rvEmployeeList);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore =  FirebaseFirestore.getInstance();


        displyAllEmployee();
        employeeList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        employeeList.setLayoutManager(linearLayoutManager);
        employeeList.setAdapter(adapter);


        materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    private void displyAllEmployee() {

        Query query = firebaseFirestore.collection("employee");

        FirestoreRecyclerOptions<EmployeeModel> options = new FirestoreRecyclerOptions.Builder<EmployeeModel>().setQuery(query,EmployeeModel.class).build();

         adapter = new FirestoreRecyclerAdapter<EmployeeModel, EmployeeViewHolder>(options) {
            @NonNull
            @Override
            public EmployeeViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity()).inflate(  R.layout.item_employee,parent,false);
                return new EmployeeViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull  HomeFragment.EmployeeViewHolder holder, int position, @NonNull  EmployeeModel model) {
                holder.employeeName.setText(model.getName());
                holder.employeeEmail.setText(model.getEmail());
                holder.employeePhn.setText(model.getMobile());
                holder.employeePost.setText(model.getDesignation());
                holder.mcvSingleEmployee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showEmployeeDetail(model.getUserId());
                    }
                });
            }
        };

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


    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.bottom_view_add_employee);



        MaterialCardView register = bottomSheetDialog.findViewById(R.id.registerEmployee);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tvEmail = bottomSheetDialog.findViewById(R.id.employeeEmail);
                TextView tvPassword = bottomSheetDialog.findViewById(R.id.employeePassword);
                TextView tvName = bottomSheetDialog.findViewById(R.id.employeeName);
                TextView tvMobile = bottomSheetDialog.findViewById(R.id.employeeMobile);
                TextView tvPosition = bottomSheetDialog.findViewById(R.id.employeePosition);
                String name = tvName.getText().toString();
                String passwrod = tvPassword.getText().toString();
                String email = tvEmail.getText().toString();
                String mobile = tvMobile.getText().toString();
                String position = tvPosition.getText().toString();

                materialCardView.setClickable(false);
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(passwrod)||TextUtils.isEmpty(email)) {
                    Toast.makeText(requireActivity(), "All Fields required", Toast.LENGTH_SHORT).show();

                } else {
                    materialCardView.setClickable(false);
                    bottomSheetDialog.dismiss();
                    register(name, email,passwrod,mobile,position);
                }
            }
        });

        bottomSheetDialog.show();
    }

    private void showEmployeeDetail(String userId){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.employee_detail_attendance);

        CollectionReference attendanceRef = firebaseFirestore.collection("business").document("Ygac4TwI6NvjIKYPZuxe").collection("attendance");
        Query query = attendanceRef.whereEqualTo("userId",userId);

        FirestoreRecyclerOptions<AttendanceModel> options = new FirestoreRecyclerOptions.Builder<AttendanceModel>().setQuery(query,AttendanceModel.class).build();

        empDetailAdapter = new FirestoreRecyclerAdapter<AttendanceModel, HomeFragment.AttendacneViewHolder>(options) {
            @NonNull
            @Override
            public HomeFragment.AttendacneViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity()).inflate(  R.layout.item_attendance,parent,false);
                return new HomeFragment.AttendacneViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull  HomeFragment.AttendacneViewHolder holder, int position, @NonNull AttendanceModel model) {
                holder.attendanceDate.setText(model.getDate());
                holder.attendancePunchIn.setText(model.getInTime());
                holder.attendancePunchOut.setText(model.getOutTime());
            }
        };
        RecyclerView rvEmployeeAttendance = bottomSheetDialog.findViewById(R.id.rvEmployeeAttendance);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvEmployeeAttendance.setLayoutManager(linearLayoutManager);
        rvEmployeeAttendance.setAdapter(empDetailAdapter);

        empDetailAdapter.startListening();
        bottomSheetDialog.show();


    }
    public  class AttendacneViewHolder extends RecyclerView.ViewHolder {

        private TextView attendanceDate;
        private TextView attendancePunchIn;
        private TextView attendancePunchOut;

        public  AttendacneViewHolder(@NonNull View itemView){
            super(itemView);
            attendanceDate = itemView.findViewById(R.id.attendanceDate);
            attendancePunchIn = itemView.findViewById(R.id.attendancePunchIn);
            attendancePunchOut = itemView.findViewById(R.id.attendancePunchOut);
        }


    }


    private void register(String user_name, String email, String txt_password, String txt_mobile, String position) {
        contentLoadingProgressBar.setVisibility(View.VISIBLE);

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
                    user.put("name", user_name);
                    user.put("email", email);
                    user.put("password", txt_password);
                    user.put("mobile", txt_mobile);
                    user.put("designation", position);
                    user.put("userType", "employee");
                    user.put("businessName", "Avengers");

                    db.collection("employee")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    contentLoadingProgressBar.setVisibility(View.GONE);
                                    materialCardView.setClickable(true);
                                    Toast.makeText(requireActivity(), "Employee account successfully created.", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    contentLoadingProgressBar.setVisibility(View.GONE);
                                    materialCardView.setClickable(true);
                                    Toast.makeText(requireActivity(), "Account creation unsucessful "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });



                } else {
                    contentLoadingProgressBar.setVisibility(View.GONE);
                    materialCardView.setClickable(true);
                    Toast.makeText(requireActivity(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
