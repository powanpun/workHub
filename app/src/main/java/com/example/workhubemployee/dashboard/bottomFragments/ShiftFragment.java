package com.example.workhubemployee.dashboard.bottomFragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workhubemployee.R;
import com.example.workhubemployee.dashboard.AddShiftActivity;
import com.example.workhubemployee.dashboard.ChatDetailActivity;
import com.example.workhubemployee.employee.EmployeeBottomNavigationActivity;
import com.example.workhubemployee.employee.employeeFragments.EmployeeHomeFragment;
import com.example.workhubemployee.models.AttendanceModel;
import com.example.workhubemployee.models.EmployeeModel;
import com.example.workhubemployee.models.ShiftModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShiftFragment extends Fragment {

    private RecyclerView shiftList;
    private MaterialCardView materialCardView;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    private ContentLoadingProgressBar contentLoadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shift, container, false);
        materialCardView = view.findViewById(R.id.addShift);
        firebaseFirestore = FirebaseFirestore.getInstance();
        shiftList = view.findViewById(R.id.rvShiftList);
        contentLoadingProgressBar = view.findViewById(R.id.progressbar);

        displyAllShifts();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        shiftList.setLayoutManager(linearLayoutManager);
        shiftList.setAdapter(adapter);

        materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddShiftActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    private void displyAllShifts() {
//
//        String userId = ((EmployeeBottomNavigationActivity) getActivity()).getUserId();
//        CollectionReference attendanceRef = firebaseFirestore.collection("business").document("Ygac4TwI6NvjIKYPZuxe").collection("shift");
//        Query query = attendanceRef.whereEqualTo("userId",userId);
        Query query = firebaseFirestore.collection("business").document("Ygac4TwI6NvjIKYPZuxe").collection("shift").orderBy("employeeName", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ShiftModel> options = new FirestoreRecyclerOptions.Builder<ShiftModel>().setQuery(query,ShiftModel.class).build();

        adapter = new FirestoreRecyclerAdapter<ShiftModel, ShiftFragment.ShiftViewHolder>(options) {
            @NonNull
            @Override
            public ShiftFragment.ShiftViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity()).inflate(  R.layout.item_shift,parent,false);
                return new ShiftFragment.ShiftViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull  ShiftFragment.ShiftViewHolder holder, int position, @NonNull ShiftModel model) {
                holder.attendanceDate.setText(model.getDate());
                holder.attendancePunchIn.setText(model.getShiftStart());
                holder.attendancePunchOut.setText(model.getShiftEnd());
                holder.employeeName.setText(model.getEmployeeName());

            }
        };

    }


    public  class ShiftViewHolder extends RecyclerView.ViewHolder {

        private TextView attendanceDate;
        private TextView attendancePunchIn;
        private TextView attendancePunchOut;
        private TextView employeeName;

        public  ShiftViewHolder(@NonNull View itemView){
            super(itemView);
            attendanceDate = itemView.findViewById(R.id.attendanceDate);
            attendancePunchIn = itemView.findViewById(R.id.attendancePunchIn);
            attendancePunchOut = itemView.findViewById(R.id.attendancePunchOut);
            employeeName = itemView.findViewById(R.id.employeeName);

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