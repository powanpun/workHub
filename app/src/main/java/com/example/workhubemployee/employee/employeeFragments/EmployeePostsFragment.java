package com.example.workhubemployee.employee.employeeFragments;

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
import com.example.workhubemployee.dashboard.bottomFragments.PostsFragment;
import com.example.workhubemployee.employee.EmployeeBottomNavigationActivity;
import com.example.workhubemployee.models.PostModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;


public class EmployeePostsFragment extends Fragment {


    private RecyclerView postsList;
    private MaterialCardView materialCardView;
    private ContentLoadingProgressBar contentLoadingProgressBar;
    private FirebaseFirestore db;
    private TextView empName;
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
       View view = inflater.inflate(R.layout.fragment_employee_posts, container, false);
        materialCardView = view.findViewById(R.id.addPost);
        contentLoadingProgressBar = view.findViewById(R.id.progressbar);
        postsList = view.findViewById(R.id.rvPostsList);
        empName= view.findViewById(R.id.employeeName);
        firebaseFirestore =  FirebaseFirestore.getInstance();
        String name = ((EmployeeBottomNavigationActivity) getActivity()).getName();
        empName.setText(name.toUpperCase());
        displyAllPosts();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postsList.setLayoutManager(linearLayoutManager);
        postsList.setAdapter(adapter);

        materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();
            }
        });


        return view;
    }


    private void displyAllPosts() {


        Query query = firebaseFirestore.collection("posts").orderBy("createdAt", Query.Direction.ASCENDING);;

        FirestoreRecyclerOptions<PostModel> options = new FirestoreRecyclerOptions.Builder<PostModel>().setQuery(query,PostModel.class).build();

        adapter = new FirestoreRecyclerAdapter<PostModel, EmployeePostsFragment.PostViewHolder>(options) {
            @NonNull
            @Override
            public EmployeePostsFragment.PostViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity()).inflate(  R.layout.item_post,parent,false);
                return new EmployeePostsFragment.PostViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull  EmployeePostsFragment.PostViewHolder holder, int position, @NonNull  PostModel model) {
                holder.postedBy.setText("posted by "+model.getUser());
                holder.postedMessage.setText(model.getData());
            }
        };

    }


    public  class PostViewHolder extends RecyclerView.ViewHolder {

        private TextView postedBy;
        private TextView postedMessage;

        public  PostViewHolder(@NonNull View itemView){
            super(itemView);
            postedBy = itemView.findViewById(R.id.postedBy);
            postedMessage = itemView.findViewById(R.id.postedMessage);
        }


    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.add_post_bottom_view);



        MaterialCardView register = bottomSheetDialog.findViewById(R.id.submitPost);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tvMessage = bottomSheetDialog.findViewById(R.id.postMessage);

                String message = tvMessage.getText().toString();

                materialCardView.setClickable(false);
                if (TextUtils.isEmpty(message) ) {
                    Toast.makeText(requireActivity(), "Please enter a message", Toast.LENGTH_SHORT).show();

                } else {
                    materialCardView.setClickable(false);
                    bottomSheetDialog.dismiss();
                    submitPost(message);
                }
            }
        });

        bottomSheetDialog.show();
    }


    private void submitPost(String message) {
        contentLoadingProgressBar.setVisibility(View.VISIBLE);
        db = FirebaseFirestore.getInstance();
        String name = ((EmployeeBottomNavigationActivity) getActivity()).getName();

        HashMap<String, String> post = new HashMap<>();
        post.put("data", message);
        post.put("user", name);
        post.put("createdAt", String.valueOf(Timestamp.now().getSeconds()));


        db.collection("posts")
                .add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        contentLoadingProgressBar.setVisibility(View.GONE);
                        materialCardView.setClickable(true);
                        Toast.makeText(requireActivity(), "Post successfully added", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        contentLoadingProgressBar.setVisibility(View.GONE);
                        materialCardView.setClickable(true);
                        Toast.makeText(requireActivity(), "Error in adding post"+e.getMessage(), Toast.LENGTH_SHORT).show();
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