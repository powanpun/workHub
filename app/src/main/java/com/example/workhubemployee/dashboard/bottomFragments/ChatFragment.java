package com.example.workhubemployee.dashboard.bottomFragments;

import android.content.Intent;
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
import com.example.workhubemployee.dashboard.ChatDetailActivity;
import com.example.workhubemployee.employee.EmployeeBottomNavigationActivity;
import com.example.workhubemployee.loginsignup.MainActivity;
import com.example.workhubemployee.models.EmployeeModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class ChatFragment extends Fragment {

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        employeeList = view.findViewById(R.id.rvEmployeeList);
        firebaseFirestore =  FirebaseFirestore.getInstance();
        displyAllEmployee();
        employeeList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        employeeList.setLayoutManager(linearLayoutManager);
        employeeList.setAdapter(adapter);

        return view;
    }
    private void displyAllEmployee() {

        Query query = firebaseFirestore.collection("employee");

        FirestoreRecyclerOptions<EmployeeModel> options = new FirestoreRecyclerOptions.Builder<EmployeeModel>().setQuery(query,EmployeeModel.class).build();

        adapter = new FirestoreRecyclerAdapter<EmployeeModel, ChatFragment.EmployeeViewHolder>(options) {
            @NonNull
            @Override
            public ChatFragment.EmployeeViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity()).inflate(  R.layout.item_employee,parent,false);
                return new ChatFragment.EmployeeViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull  ChatFragment.EmployeeViewHolder holder, int position, @NonNull  EmployeeModel model) {
                holder.employeeName.setText(model.getName());
                holder.employeeEmail.setText(model.getEmail());
                holder.employeePhn.setText(model.getMobile());
                holder.employeePost.setText(model.getDesignation());
                String userId = model.getUserId();
                holder.mcvSingleEmployee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ChatDetailActivity.class);
                        intent.putExtra("employeeId",userId);
                        intent.putExtra("employeeName",model.getName());
                        startActivity(intent);

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