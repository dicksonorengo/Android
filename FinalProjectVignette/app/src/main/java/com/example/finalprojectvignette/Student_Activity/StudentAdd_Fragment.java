package com.example.finalprojectvignette.Student_Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalprojectvignette.Fragment.Fragment_Student_Profile;
import com.example.finalprojectvignette.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class StudentAdd_Fragment extends Fragment {

    private Toolbar toolbar;

    public StudentAdd_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_student, container, false);
        setHasOptionsMenu(true);
        toolbar = getActivity().findViewById(R.id.parent_toolbar);
        toolbar.setTitle("Add Student");

        final EditText childID = v.findViewById(R.id.editText_addChild);
        Button addChildButton = v.findViewById(R.id.button_addChild);
        addChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(childID.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Enter Student ID",Toast.LENGTH_SHORT).show();
                } else {
                    addChildIfExixts(childID.getText().toString());
                }
            }
        });
        return v;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        toolbar.getMenu().clear();
        inflater.inflate(R.menu.empty_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void addChildIfExixts(final String childID){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference childrenTb = db.child("children");
        childrenTb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(childID)){
                    addChildToStudent(childID);
                } else {
                    Toast.makeText(getContext(),"Couldn't find Student with give ID",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void addChildToStudent(final String childId){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference student = db.child("student").child(user.getUid());
        student.child("people").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String currentChildren = dataSnapshot.getValue().toString();
                if(currentChildren.equals("")){
                    currentChildren += childId;
                    student.child("people").setValue(currentChildren);
                    Toast.makeText(getContext(),"Student added",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(),"current student is not empty",Toast.LENGTH_SHORT).show();
                }
                getFragmentManager().popBackStack(null, getFragmentManager().POP_BACK_STACK_INCLUSIVE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void onDestroy() {
        toolbar.setTitle("My Student");
        super.onDestroy();
    }
}
