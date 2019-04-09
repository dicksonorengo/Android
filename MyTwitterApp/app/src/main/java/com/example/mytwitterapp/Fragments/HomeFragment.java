package com.example.mytwitterapp.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mytwitterapp.Adapters.PostAdapter;
import com.example.mytwitterapp.Models.Post;
import com.example.mytwitterapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView postRecyclerView ;
    PostAdapter postAdapter ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;
    List<Post> postList;

    FloatingActionButton floatingActionButton;
    boolean isDark;
    ConstraintLayout rootlayout;

    public HomeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        postRecyclerView  = fragmentView.findViewById(R.id.postRV);
        floatingActionButton = fragmentView.findViewById(R.id.floating);
        rootlayout = fragmentView.findViewById(R.id.root_layout);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postRecyclerView.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Posts");

        isDark = getpref();
        if(isDark){
            rootlayout.setBackgroundColor(getResources().getColor(R.color.black));
        }
        else {
            rootlayout.setBackgroundColor(getResources().getColor(R.color.white));
        }

        return fragmentView ;
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList = new ArrayList<>();
                for (DataSnapshot postsnap: dataSnapshot.getChildren()) {
                    //System.out.println("()())()))())()" + postsnap);
                    Post post = postsnap.getValue(Post.class);
                    //System.out.println(postsnap.getValue(Post.class));
                    postList.add(post);
                }
                postAdapter = new PostAdapter(getActivity(),postList,true);
                postRecyclerView.setAdapter(postAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDark = !isDark;

                if(isDark){
                    rootlayout.setBackgroundColor(getResources().getColor(R.color.black));
                }
                else {
                    rootlayout.setBackgroundColor(getResources().getColor(R.color.white));
                }

                savestate(isDark);
            }
        });



    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private  void savestate(boolean isDark){
        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("mypref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isDark",isDark);
        editor.commit();
    }
    private boolean getpref(){
        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("mypref",Context.MODE_PRIVATE);
        boolean isDark = preferences.getBoolean("isDark",false);
        return isDark;
    }
}
