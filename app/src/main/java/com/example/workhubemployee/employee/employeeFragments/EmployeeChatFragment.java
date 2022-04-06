package com.example.workhubemployee.employee.employeeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workhubemployee.R;
import com.example.workhubemployee.dashboard.ChatDetailActivity;
import com.example.workhubemployee.employee.EmployeeBottomNavigationActivity;
import com.example.workhubemployee.models.ChatModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;


public class EmployeeChatFragment extends Fragment {

    EditText chatMessage;
    ImageView btnSend;
    String employeeName;
    String employeeId;

    private RecyclerView chatList;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        employeeName =((EmployeeBottomNavigationActivity) getActivity()).getName();
        employeeId = ((EmployeeBottomNavigationActivity) getActivity()).getUserId();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employee_chat, container, false);
        chatMessage = view.findViewById(R.id.chatMessage);
        btnSend = view.findViewById(R.id.chatSend);
        firebaseFirestore = FirebaseFirestore.getInstance();
        chatList = view.findViewById(R.id.rvChat);
        displyAllChat();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        linearLayoutManager.setReverseLayout(true);
        chatList.setLayoutManager(linearLayoutManager);
        chatList.setAdapter(adapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = chatMessage.getText().toString();

                if(TextUtils.isEmpty(msg)){
                    Toast.makeText(requireActivity(), "Please enter a message", Toast.LENGTH_SHORT).show();
                }else{
                    sendMessage(msg);
                }
            }
        });
        return view;
    }
    private void displyAllChat() {

        CollectionReference attendanceRef = firebaseFirestore.collection("business").document("Ygac4TwI6NvjIKYPZuxe").collection("chat");
        Query query = attendanceRef.orderBy("createdAt", Query.Direction.DESCENDING).whereEqualTo("employeeId",employeeId);
        FirestoreRecyclerOptions<ChatModel> options = new FirestoreRecyclerOptions.Builder<ChatModel>().setQuery(query,ChatModel.class).build();

        adapter = new FirestoreRecyclerAdapter<ChatModel, EmployeeChatFragment.AttendacneViewHolder>(options) {
            @NonNull
            @Override
            public EmployeeChatFragment.AttendacneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(requireActivity()).inflate(R.layout.chat_item_layout,parent,false);
                return new EmployeeChatFragment.AttendacneViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull  EmployeeChatFragment.AttendacneViewHolder holder, int position, @NonNull ChatModel model) {

                    if(model.getFrom().equals(employeeName)){
                        holder.chatMessageFrom.setText(model.getMessage());
                        holder.llFrom.setVisibility(View.VISIBLE);
                    }else{
                        holder.chatMessageTo.setText(model.getMessage());
                        holder.llTo.setVisibility(View.VISIBLE);
                    }

            }
        };

    }

    public void sendMessage(String msg){


        HashMap<String, String> chat = new HashMap<>();
        chat.put("employeeId", employeeId);
        chat.put("from", employeeName);
        chat.put("to", "admin");
        chat.put("message", msg);
        chat.put("createdAt", String.valueOf(Timestamp.now().getSeconds()));


        firebaseFirestore.collection("business").document("Ygac4TwI6NvjIKYPZuxe").collection("chat")
                .add(chat)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        chatMessage.setText("");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireActivity(), "Error in sending message"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public  class AttendacneViewHolder extends RecyclerView.ViewHolder {

        private TextView chatMessageTo;
        private TextView chatMessageFrom;
        private LinearLayout llTo;
        private LinearLayout llFrom;

        public  AttendacneViewHolder(@NonNull View itemView){
            super(itemView);
            chatMessageTo = itemView.findViewById(R.id.chatMessageTo);
            chatMessageFrom = itemView.findViewById(R.id.chatMessageFrom);
            llTo = itemView.findViewById(R.id.llTo);
            llFrom = itemView.findViewById(R.id.llFrom);
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