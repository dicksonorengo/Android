package com.example.finalprojectvignette.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.finalprojectvignette.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class Fragment_AdminClass extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private ListView ClassListView;
    private List<String> AllClassesList = new ArrayList<>();
    private boolean HasClasses = false;

    public Fragment_AdminClass() {
        // Required empty public constructor
    }
    public static Fragment_AdminClass newInstance(String param1, String param2) {
        Fragment_AdminClass fragment = new Fragment_AdminClass();
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
        View view = inflater.inflate(R.layout.fragment_adminclasslist, container, false);
        setHasOptionsMenu(true);
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        Toolbar toolbar = getActivity().findViewById(R.id.teacher_toolbar);
        toolbar.setTitle("SCHOOLS");
        ClassListView =  view.findViewById(R.id.teacher_class_list);
        GetClassList();
        ClassListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(HasClasses) {
                    String currClassId = AllClassesList.get(position);
                    Fragment classFrag = ClassFragment.newInstance(currClassId);
                    getFragmentManager().beginTransaction().replace(R.id.teacher_content_holder, classFrag).addToBackStack(null).commit();
                }
            }
        });
        return view;
    }
    private void GetClassList() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final String uid = firebaseAuth.getCurrentUser().getUid();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String AllClasses = dataSnapshot.child("admin").child(uid).child("class_ids").getValue().toString();
                if(!AllClasses.equals("0")) {
                    HasClasses = true;
                    String[] myList = AllClasses.split(",");
                    SetClassIdsList(myList);
                    List<String> ClassNameList = new ArrayList<String>();
                    for (String currClassID : myList) {
                        ClassNameList.add(dataSnapshot.child("classes").child(currClassID).child("class_name").getValue().toString());
                    }
                    String[] ClassName = new String[ClassNameList.size()];
                    ClassNameList.toArray(ClassName);
                    SetListVIew(ClassName);
                }
                else
                {
                    HasClasses = false;
                    String[] noclasses = {"You are not assigned to any class yet"};
                    ListAdapter myListAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, noclasses);
                    ClassListView.setAdapter(myListAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }
    private void SetClassIdsList(String[] ClassIds)
    {
        AllClassesList.clear();
        Collections.addAll(AllClassesList, ClassIds);
    }
    private void SetListVIew(String[] ClassNames) {
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for(int i = 0; i < ClassNames.length; i ++ ) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("one", ClassNames[i]);
            mylist.add(map);
        }
        String[] from1 = {"one"};
        int[] to1 = {R.id.ListItem_one};
        SimpleAdapter adapter1 = new SimpleAdapter(getContext(), mylist, R.layout.class_listview_item, from1, to1);
        ClassListView.setAdapter(adapter1);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.activity_admin_menu,menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_addClass) {
            AddNewClass();
        }
        if(id == R.id.action_newClass){
            AddFragment();
        }
        return super.onOptionsItemSelected(item);
    }
    private void AddFragment(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final String uidk = firebaseAuth.getCurrentUser().getUid();
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fixed);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final EditText classcode_edit = dialog.findViewById(R.id.classcode);
        final EditText name = dialog.findViewById(R.id.name);
        Button yes = dialog.findViewById(R.id.yes_button);
        Button no = dialog.findViewById(R.id.no_button);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String classcode = classcode_edit.getText().toString();
                        final String name_course = name.getText().toString();
                        if(!dataSnapshot.child("classes").child(classcode).exists()){
                            databaseReference.child("classes").child(classcode).child("class_name").setValue(name_course);
                            databaseReference.child("classes").child(classcode).child("children").setValue("0");
                            databaseReference.child("classes").child(classcode).child("posts").setValue("0");


                        }
                        String currClasses = dataSnapshot.child("admin").child(uidk).child("class_ids").getValue().toString();
                        if(currClasses.equals("0")) {
                            databaseReference.child("admin").child(uidk).child("class_ids").setValue(classcode + ",");
                            RefreshClassList();
                        }
                        else if(!isClassAssigned(currClasses, classcode)) {
                            currClasses += classcode + ",";
                            databaseReference.child("admin").child(uidk).child("class_ids").setValue(currClasses);
                            Toast.makeText(getContext(), "Your new class '" + classcode + "' has been added", Toast.LENGTH_SHORT).show();
                            RefreshClassList();
                        }
                        else {
                            Toast.makeText(getContext(), "You have been assigned to this class already!", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void AddNewClass() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final String uid = firebaseAuth.getCurrentUser().getUid();
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Add new class");
        alert.setMessage("Enter a valid class code of the class assigned to you");
        final EditText input = new EditText(getContext());
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                final String Classcode = input.getText().toString();
                if(Classcode.isEmpty())
                    Toast.makeText(getContext(), "Class Code cannot be empty!", Toast.LENGTH_SHORT).show();
                else {

                    final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
                    dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child("classes").child(Classcode).exists()) {
                                String currClasses = dataSnapshot.child("admin").child(uid).child("class_ids").getValue().toString();
                                if(currClasses.equals("0")) {
                                    dataRef.child("admin").child(uid).child("class_ids").setValue(Classcode + ",");
                                    RefreshClassList();
                                }
                                else if(!isClassAssigned(currClasses, Classcode)) {
                                    currClasses += Classcode + ",";
                                    dataRef.child("admin").child(uid).child("class_ids").setValue(currClasses);
                                    Toast.makeText(getContext(), "Your new class '" + Classcode + "' has been added", Toast.LENGTH_SHORT).show();
                                    RefreshClassList();
                                }
                                else {
                                    Toast.makeText(getContext(), "You have been assigned to this class already!", Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                Toast.makeText(getContext(), "Course code you have entered is wrong.", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) { }
                    });
                }

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }
    private boolean isClassAssigned(String classList, String NewClass) {
        return classList.contains(NewClass);
    }
    private void RefreshClassList() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.teacher_content_holder, new Fragment_AdminClass()).commit();
        getFragmentManager().popBackStack(null, getFragmentManager().POP_BACK_STACK_INCLUSIVE);
    }
}

