package com.example.finalprojectvignette.Student_Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.finalprojectvignette.Activity.ActivityAdmin;
import com.example.finalprojectvignette.Adapter.PostAdapter;
import com.example.finalprojectvignette.Fragment.ClassFragment;
import com.example.finalprojectvignette.Model.Post;
import com.example.finalprojectvignette.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class Home_Student_Fragment_Post extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    String currentClass = "";


    private Home_Student_Fragment_Post mListener;

    RecyclerView postRecyclerView ;
    PostAdapter postAdapter ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;
    List<Post> postList;

    private String childid,childName;

    public Home_Student_Fragment_Post() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Home_Student_Fragment_Post newInstance(String param1, String param2) {
        Home_Student_Fragment_Post fragment = new Home_Student_Fragment_Post();
        Bundle args = new Bundle();
        args.putString("id", param1);
        args.putString("name", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.student_home_layout, container, false);
        postRecyclerView  = fragmentView.findViewById(R.id.postRV);

        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postRecyclerView.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        childid = getArguments().getString("id");
        childName = getArguments().getString("name");
        currentClass = ClassFragment.classid;


        Toolbar toolbar;
        toolbar =  getActivity().findViewById(R.id.parent_toolbar);
        toolbar.setTitle("Student Post");

        return fragmentView ;
    }
    @Override
    public void onStart() {
        super.onStart();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("children").child(childid).exists()) {
                    String[] name = new String[(int) dataSnapshot.child("classes").getChildrenCount()];
                    if (dataSnapshot.child("children").child(childid).exists()) {
                        int i = 0;
                        for(DataSnapshot d : dataSnapshot.child("classes").getChildren()) {
                            name[i] = d.getKey();
                            String keyvalue = name[i];
                            if(!dataSnapshot.child("classes").child(keyvalue).child("children").getValue().toString().equals("")){
                                String currentchild = dataSnapshot.child("classes").child(keyvalue).child("children").getValue().toString();
                                String[] class_children_arr = currentchild.split(",");
                                for(final String inside : class_children_arr){
                                    if(inside.equals(childid)){
                                        String allpost = dataSnapshot.child("classes").child(keyvalue).child("posts").getValue().toString();
                                        postList = new ArrayList<>();
                                        String[] myList = allpost.split(",");
                                        if(!allpost.equals("0")){
                                            for (DataSnapshot postsnap: dataSnapshot.child("Posts").getChildren()) {
                                                String postt = postsnap.getKey().toString();
                                                for(final String childInClass : myList)
                                                {
                                                    if(childInClass.equals(postt)){
                                                        Post post = postsnap.getValue(Post.class);
                                                        postList.add(post);
                                                    }
                                                    else {
                                                        //Toast.makeText(getContext(),"Not equal",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        }
                                        else {
                                            Toast.makeText(getContext(),"No Post",Toast.LENGTH_SHORT).show();
                                        }
                                        postAdapter = new PostAdapter(getActivity(),postList,true);
                                        postRecyclerView.setAdapter(postAdapter);

                                    }
                                    else {
                                        //Toast.makeText(getContext(),"Not equal",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            System.out.println("?????????????????????????? " + name[i]);
                            i++;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}

