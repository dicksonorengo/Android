package com.example.teacher_parent;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class Frag_ts_Academic_Performance extends Fragment{
    final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    DatabaseReference classesTb = db.child("classes");
    DatabaseReference childrenTb = db.child("children");
    String currentClass = "";
    MyRecyclerViewAdapter_Smile adapter = new MyRecyclerViewAdapter_Smile();
    RecyclerView recyclerView;
    private String ClassId;

    String[] class_children_arr;
    TextView child_name;
    String child_text;

    CheckBox checkBox;
    String smilerating;

    public Frag_ts_Academic_Performance() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ClassId = ClassFragment.className;

        final View v= inflater.inflate(R.layout.fragment_frag_ts__academic__performance, container, false);
        currentClass = ClassFragment.classid;

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String curchild = dataSnapshot.child("classes").child(currentClass).child("children").getValue().toString();
                if(curchild.equals("0")){
                    Toast.makeText(getContext(),"Пока что нету",Toast.LENGTH_SHORT);
                }
                else {
                    classesTb.child(currentClass).child("children").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String class_children_string =  dataSnapshot.getValue().toString();
                            class_children_arr = class_children_string.split(",");
                            final List<HashMap<String,String>> childrenList = new ArrayList<>();
                            for(final String childInClass : class_children_arr){
                                childrenTb.child(childInClass).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String name = dataSnapshot.child("name").getValue().toString();
                                        HashMap<String,String> childInfo = new HashMap<>();
                                        childInfo.put("name",name);
                                        childInfo.put("id",childInClass);
                                        childInfo.put("pic",dataSnapshot.child("pic").getValue().toString());
                                        childrenList.add(childInfo);
                                        adapter.setChildren(childrenList);

                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {}
                                });


                                adapter.setItemClickListener(new MyRecyclerViewAdapter_Smile.RecyclerItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, HashMap<String, String> child) {
                                        child_name = v.findViewById(R.id.child_list_name);
                                        child_text = child_name.getText().toString();
                                        smilerating = MyRecyclerViewAdapter_Smile.ViewHolder.smilerating;
                                        checkBox = v.findViewById(R.id.checkbox);

                                        if(checkBox.isChecked()){
                                            AddNewActivity();
                                        }
                                        else{
                                            Toast.makeText(getContext(),"Checkbox is not checked",Toast.LENGTH_SHORT).show();
                                        }

                                        System.out.println("??????????!!!!!!!!!!! " + checkBox);
                                    }
                                });
                            }





                            recyclerView = v.findViewById(R.id.class_child_list);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setAdapter(adapter);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;
    }

    private void AddNewActivity() {
        SimpleDateFormat MyFormat = new SimpleDateFormat("hh:mm a");
        String CurrentTimeis = MyFormat.format(new Date());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("activities");
        String MyActId = databaseReference.push().getKey();
        databaseReference.child(MyActId).child("type").setValue("Academic_Performance");
        databaseReference.child(MyActId).child("time").setValue(CurrentTimeis);
        databaseReference.child(MyActId).child("class").setValue(ClassId);
        databaseReference.child(MyActId).child("childnames").setValue(child_text);
        databaseReference.child(MyActId).child("smilerating").setValue(smilerating);

        AddActivityToTaggedKids(MyActId);
    }


    private void AddActivityToTaggedKids(final String key)
    {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("children");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(final String childInClass : class_children_arr)
                {
                    String curid = dataSnapshot.child(childInClass).child("name").getValue().toString();
                    if(curid.equals(child_text)){
                        String currActs = dataSnapshot.child(childInClass).child("act_ids").getValue().toString();
                        if(currActs.equals("0"))
                        {
                            databaseReference.child(childInClass).child("act_ids").setValue(key);
                        }
                        else
                        {
                            String acts = currActs + "," + key;
                            databaseReference.child(childInClass).child("act_ids").setValue(acts);
                        }
                    }
                    else {
                        Toast.makeText(getContext(),"Not equal",Toast.LENGTH_SHORT).show();
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //CompletedAllTasks();
    }

    private void CompletedAllTasks()
    {
        Toast.makeText(getContext(), "Added your new Activity", Toast.LENGTH_SHORT).show();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack ("ActFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
