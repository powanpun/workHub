package com.example.workhubemployee.employee.employeeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.workhubemployee.R;
import com.example.workhubemployee.dashboard.bottomFragments.ShiftFragment;
import com.example.workhubemployee.employee.EmployeeBottomNavigationActivity;
import com.example.workhubemployee.models.ShiftModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class EmployeeShiftFragment extends Fragment {
    private RecyclerView shiftList;
    private MaterialCardView materialCardView;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_employee_shift, container, false);
        materialCardView = view.findViewById(R.id.addShift);
        firebaseFirestore = FirebaseFirestore.getInstance();
        shiftList = view.findViewById(R.id.rvShiftList);

        displyAllShifts();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        shiftList.setLayoutManager(linearLayoutManager);
        shiftList.setAdapter(adapter);

        return view;
    }
    private void displyAllShifts() {

        String userId = ((EmployeeBottomNavigationActivity) getActivity()).getUserId();
        CollectionReference attendanceRef = firebaseFirestore.collection("business").document("Ygac4TwI6NvjIKYPZuxe").collection("shift");
        Query query = attendanceRef.whereEqualTo("employeeId",userId);
        FirestoreRecyclerOptions<ShiftModel> options = new FirestoreRecyclerOptions.Builder<ShiftModel>().setQuery(query,ShiftModel.class).build();

        adapter = new FirestoreRecyclerAdapter<ShiftModel, EmployeeShiftFragment.ShiftViewHolder>(options) {
            @NonNull
            @Override
            public EmployeeShiftFragment.ShiftViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity()).inflate(  R.layout.item_shift,parent,false);
                return new EmployeeShiftFragment.ShiftViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull  EmployeeShiftFragment.ShiftViewHolder holder, int position, @NonNull ShiftModel model) {
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