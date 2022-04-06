package com.example.workhubemployee.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workhubemployee.R;
import com.example.workhubemployee.employee.EmployeeBottomNavigationActivity;
import com.example.workhubemployee.employee.employeeFragments.EmployeeHomeFragment;
import com.example.workhubemployee.models.AttendanceModel;
import com.example.workhubemployee.models.ChatModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class ChatDetailActivity extends AppCompatActivity {

    ImageView backBtn;
    TextView title;
    EditText chatMessage;
    ImageView btnSend;
    String employeeName;
    String employeeId;

    private RecyclerView chatList;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    CollectionReference attendanceRef;
    Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        Intent myIntent = getIntent(); // gets the previously created intent
        employeeName = myIntent.getStringExtra("employeeName");
        employeeId = myIntent.getStringExtra("employeeId");

        chatMessage = findViewById(R.id.chatMessage);
        btnSend = findViewById(R.id.chatSend);

        backBtn = findViewById(R.id.backBtn);
        title = findViewById(R.id.chatTitle);
        title.setText(employeeName);

        firebaseFirestore = FirebaseFirestore.getInstance();

        chatList = findViewById(R.id.rvChat);
        displyAllChat();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
//
        chatList.setLayoutManager(linearLayoutManager);
        chatList.setAdapter(adapter);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = chatMessage.getText().toString();

                if(TextUtils.isEmpty(msg)){
                    Toast.makeText(getApplicationContext(), "Please enter a message", Toast.LENGTH_SHORT).show();
                }else{
                    sendMessage(msg);
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void displyAllChat() {

        attendanceRef = firebaseFirestore.collection("business").document("Ygac4TwI6NvjIKYPZuxe").collection("chat");
        query = attendanceRef.orderBy("createdAt", Query.Direction.DESCENDING).whereEqualTo("employeeId",employeeId);
        FirestoreRecyclerOptions<ChatModel> options = new FirestoreRecyclerOptions.Builder<ChatModel>().setQuery(query,ChatModel.class).build();

        adapter = new FirestoreRecyclerAdapter<ChatModel, ChatDetailActivity.AttendacneViewHolder>(options) {
            @NonNull
            @Override
            public ChatDetailActivity.AttendacneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getApplication()).inflate(R.layout.chat_item_layout,parent,false);
                return new ChatDetailActivity.AttendacneViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull  ChatDetailActivity.AttendacneViewHolder holder, int position, @NonNull ChatModel model) {

                if(model.getFrom().equals("admin")){
                    holder.chatMessageFrom.setText(model.getMessage());
                    holder.llFrom.setVisibility(View.VISIBLE);
                }else{
                    holder.chatMessageTo.setText(model.getMessage());
                    holder.llTo.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onDataChanged() {
                Log.e("data","changed");
                adapter.startListening();
                adapter.notifyDataSetChanged();
            }
        };

    }

    public void sendMessage(String msg){
        chatMessage.setText("");

        HashMap<String, String> chat = new HashMap<>();
        chat.put("employeeId", employeeId);
        chat.put("from", "admin");
        chat.put("to", employeeName);
        chat.put("message", msg);
        chat.put("createdAt", String.valueOf(Timestamp.now().getSeconds()));



        firebaseFirestore.collection("business").document("Ygac4TwI6NvjIKYPZuxe").collection("chat")
                .add(chat)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

//
//                        Intent intent = getIntent();
//                        finish();
//                        startActivity(intent);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error in sending message"+e.getMessage(), Toast.LENGTH_SHORT).show();
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