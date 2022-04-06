package com.example.workhubemployee.employee.employeeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workhubemployee.R;
import com.example.workhubemployee.dashboard.bottomFragments.PostsFragment;
import com.example.workhubemployee.employee.EmployeeBottomNavigationActivity;
import com.example.workhubemployee.models.AttendanceModel;
import com.example.workhubemployee.models.PostModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class EmployeeHomeFragment extends Fragment {
    private RecyclerView attendaceList;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    private TextView empName;
    private TextView punchIn;
    private TextView punchOut;
    private ContentLoadingProgressBar contentLoadingProgressBar;
    private Boolean isPuchedIn = false;
    private Boolean isPunchedOut = false;
    private String collectionId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_employee_home, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        attendaceList = view.findViewById(R.id.rvAttendacneList);
        empName= view.findViewById(R.id.employeeName);
        punchIn= view.findViewById(R.id.punchIn);
        punchOut= view.findViewById(R.id.punchOut);
        contentLoadingProgressBar = view.findViewById(R.id.progressbar);

        String name = ((EmployeeBottomNavigationActivity) getActivity()).getName();
        empName.setText(name.toUpperCase());
        displyAllPosts();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        attendaceList.setLayoutManager(linearLayoutManager);
        attendaceList.setAdapter(adapter);

        checkPunchData();
        punchIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isPuchedIn){
                    Toast.makeText(requireActivity(),"You have already punched in",Toast.LENGTH_LONG).show();
                }else{
                    punchInData();
                }
            }
        });
        punchOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isPuchedIn){
                    Toast.makeText(requireActivity(),"You have to punch in first",Toast.LENGTH_SHORT).show();

                }else{

                    checkPunchData();
                    if(isPunchedOut){
                        Toast.makeText(requireActivity(),"You have already punch out",Toast.LENGTH_SHORT).show();
                    }else{
                        punchOutData();
                    }
                }


            }
        });
        return view;
    }

    private void checkPunchData(){

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String date = df.format(currentTime);
        String userId = ((EmployeeBottomNavigationActivity) getActivity()).getUserId();
        firebaseFirestore.collection("business").document("Ygac4TwI6NvjIKYPZuxe").collection("attendance")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> list = document.getData();
                                if(userId.equals(list.get("userId"))){
                                    if(date.equals(list.get("date"))){
                                        isPuchedIn = true;
                                        if(!list.get("outTime").toString().isEmpty()){
                                            isPunchedOut = true;
                                        }else{
                                            collectionId = document.getId();
                                            isPunchedOut = false;
                                        }

                                    }else{
                                        isPuchedIn = false;
                                    }
                                }


                            }

                        } else {
                            Log.w("data", "Error getting employees.", task.getException());
                        }
                    }

                });


    }

    private void punchOutData(){
        contentLoadingProgressBar.setVisibility(View.VISIBLE);
        punchOut.setClickable(false);
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String time = dfTime.format(currentTime);

        firebaseFirestore.collection("business").document("Ygac4TwI6NvjIKYPZuxe").collection("attendance").document(collectionId)
                .update("outTime",time);

        contentLoadingProgressBar.setVisibility(View.GONE);
        isPunchedOut = true;
        punchOut.setClickable(true);

    }

    private void punchInData(){
        contentLoadingProgressBar.setVisibility(View.VISIBLE);
        punchIn.setClickable(false);
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String date = df.format(currentTime);
        String time = dfTime.format(currentTime);
        String userId = ((EmployeeBottomNavigationActivity) getActivity()).getUserId();
        String name = ((EmployeeBottomNavigationActivity) getActivity()).getName();

        HashMap<String, String> attendance = new HashMap<>();
        attendance.put("date", date);
        attendance.put("inTime", time);
        attendance.put("outTime", "");
        attendance.put("userId", userId);
        attendance.put("employeeName", name);

        firebaseFirestore.collection("business").document("Ygac4TwI6NvjIKYPZuxe").collection("attendance")
                .add(attendance)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        contentLoadingProgressBar.setVisibility(View.GONE);
                        isPuchedIn= true;
                        collectionId = documentReference.getId();
                        Toast.makeText(requireActivity(), "Attendance completed", Toast.LENGTH_SHORT).show();
                        punchIn.setClickable(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        contentLoadingProgressBar.setVisibility(View.GONE);
                        punchIn.setClickable(false);
                        Toast.makeText(requireActivity(), "Error in attendance"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void displyAllPosts() {

        String userId = ((EmployeeBottomNavigationActivity) getActivity()).getUserId();
        CollectionReference attendanceRef = firebaseFirestore.collection("business").document("Ygac4TwI6NvjIKYPZuxe").collection("attendance");
        Query query = attendanceRef.whereEqualTo("userId",userId);
        FirestoreRecyclerOptions<AttendanceModel> options = new FirestoreRecyclerOptions.Builder<AttendanceModel>().setQuery(query,AttendanceModel.class).build();

        adapter = new FirestoreRecyclerAdapter<AttendanceModel, EmployeeHomeFragment.AttendacneViewHolder>(options) {
            @NonNull
            @Override
            public EmployeeHomeFragment.AttendacneViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity()).inflate(  R.layout.item_attendance,parent,false);
                return new EmployeeHomeFragment.AttendacneViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull  EmployeeHomeFragment.AttendacneViewHolder holder, int position, @NonNull AttendanceModel model) {
                holder.attendanceDate.setText(model.getDate());
                holder.attendancePunchIn.setText(model.getInTime());
                holder.attendancePunchOut.setText(model.getOutTime());
            }
        };

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