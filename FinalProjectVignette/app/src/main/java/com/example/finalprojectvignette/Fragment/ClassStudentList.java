package com.example.finalprojectvignette.Fragment;


import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.finalprojectvignette.Adapter.MyRecyclerViewAdapter;
import com.example.finalprojectvignette.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ClassStudentList extends Fragment {
    private static final int PReqCode = 2 ;
    private static final int REQUESCODE = 2 ;
    private static Dialog dialog;
    private static ImageView picture;
    private Uri pickedImgUri = null;
    static String imageDownlaodLink;
    final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    DatabaseReference classesTb = db.child("classes");
    DatabaseReference childrenTb = db.child("children");
    String currentClass = "";
    MyRecyclerViewAdapter adapter ;
    RecyclerView recyclerView;
    boolean k=false;
    public ClassStudentList() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v= inflater.inflate(R.layout.fragment_student_list, container, false);
        currentClass = ClassFragment.classid;
        FloatingActionButton fab = v.findViewById(R.id.Button_AddChildToClass);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addchild();
            }
        });

        adapter = new MyRecyclerViewAdapter(getContext());

        FloatingActionButton exist_child = v.findViewById(R.id.Button_existchild);
        exist_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewChildToThisClass();
            }
        });
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
                            String[] class_children_arr = class_children_string.split(",");
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
                            }


                            adapter.setItemClickListener(new MyRecyclerViewAdapter.RecyclerItemClickListener() {
                                @Override
                                public void onItemClick(View v,HashMap<String, String> child) {
                                    ImageView myPic = v.findViewById(R.id.child_list_pic);
                                    myPic.setTransitionName("ProPicShared");
                                    Fragment Myfrag = Fragment_Student_Profile.newInstance(child.get("id"),child.get("name"));

                                    Fade exitFade = new Fade();
                                    exitFade.setDuration(500);
                                    setExitTransition(exitFade);

                                    TransitionSet MoveImage = new TransitionSet();
                                    MoveImage.addTransition(new ChangeTransform()).addTransition(new ChangeImageTransform());
                                    MoveImage.addTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
                                    MoveImage.setDuration(800);
                                    MoveImage.setStartDelay(100);
                                    Myfrag.setSharedElementEnterTransition(MoveImage);
                                    Fragment previous = getActivity().getSupportFragmentManager().findFragmentById(R.id.teacher_content_holder);
                                    previous.setExitTransition(new Explode().setDuration(700));
                                    Myfrag.setEnterTransition(new Slide().setDuration(500).setStartDelay(700));
                                    Myfrag.setExitTransition(new Explode().setDuration(700).setStartDelay(300));


                                    Fade enterFade = new Fade();
                                    enterFade.setStartDelay(300);
                                    enterFade.setDuration(300);
                                    Myfrag.setEnterTransition(enterFade);

                                    getActivity().getSupportFragmentManager().beginTransaction().addSharedElement(myPic, myPic.getTransitionName())
                                            .replace(R.id.teacher_content_holder, Myfrag)
                                            .addToBackStack(null)
                                            .commit();

                                }
                            });
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
    private void AddNewChildToThisClass() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Add new child to this class");
        alert.setMessage("Enter a valid child code of the child to be added to this class");

        final EditText input = new EditText(getContext());
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                final String ChildCode = input.getText().toString();
                if(ChildCode.isEmpty())
                    Toast.makeText(getContext(), "Child Code cannot be empty!", Toast.LENGTH_SHORT).show();
                else
                {
                    final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
                    dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String[] name = new String[(int) dataSnapshot.child("classes").getChildrenCount()];
                            if(dataSnapshot.child("children").child(ChildCode).exists()) {
                                    int i = 0;
                                    for(DataSnapshot d : dataSnapshot.child("classes").getChildren()) {
                                        name[i] = d.getKey();
                                        String keyvalue = name[i];
                                        if(!dataSnapshot.child("classes").child(keyvalue).child("children").getValue().toString().equals("")){
                                            String currentchild = dataSnapshot.child("classes").child(keyvalue).child("children").getValue().toString();
                                            String[] class_children_arr = currentchild.split(",");
                                            for(final String inside : class_children_arr){
                                                System.out.println(">>>><<>><>><><><><><><><><>><><><><><><>< " + inside);
                                                System.out.println("!!!!!!!!!!! " + ChildCode);
                                                if(inside.equals(ChildCode)){
                                                    k=true;
                                                    Toast.makeText(getContext(),"Student maybe have another class",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                        i++;
                                    }
                                    if(!k){
                                        String currChildren = dataSnapshot.child("classes").child(currentClass).child("children").getValue().toString();
                                        if(currChildren.equals("0")) {
                                            dataRef.child("classes").child(currentClass).child("children").setValue(ChildCode + ",");
                                        }
                                        else if(!isChildAssigned(currChildren, ChildCode)) {
                                            currChildren += ChildCode + ",";
                                            dataRef.child("classes").child(currentClass).child("children").setValue(currChildren);
                                            Toast.makeText(getContext(), "New child with id '" + ChildCode + "' has been added", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(getContext(), "This child has been Enrolled to this class already!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    k=false;
                            }
                            else {
                                Toast.makeText(getContext(), "Child code you have entered is wrong.", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data!=null){
            if(requestCode == REQUESCODE){
                pickedImgUri = data.getData();
                picture.setImageURI(pickedImgUri);
            }
        }
    }
    private void addchild(){
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.add_new_student_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final EditText class_id = dialog.findViewById(R.id.class_id);
        final EditText child_name = dialog.findViewById(R.id.name);
        picture = dialog.findViewById(R.id.picture);
        Button yes = dialog.findViewById(R.id.yes_button);
        Button no = dialog.findViewById(R.id.no_button);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRequestForPermission();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String child_id = class_id.getText().toString();
                final String name_course = child_name.getText().toString();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {
                                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        imageDownlaodLink = uri.toString();
                                        databaseReference.child("children").child(child_id).child("name").setValue(name_course);
                                        databaseReference.child("children").child(child_id).child("pic").setValue(imageDownlaodLink);
                                        databaseReference.child("children").child(child_id).child("act_ids").setValue("0");
                                        if(!dataSnapshot.child("children").child(child_id).exists()){
                                            String currChildren = dataSnapshot.child("classes").child(currentClass).child("children").getValue().toString();
                                            if(currChildren.equals("0"))
                                            {
                                                databaseReference.child("classes").child(currentClass).child("children").setValue(child_id + ",");
                                            }
                                            else if(!isChildAssigned(currChildren, child_id)) {
                                                currChildren += child_id + ",";
                                                databaseReference.child("classes").child(currentClass).child("children").setValue(currChildren);
                                                Toast.makeText(getContext(), "New child with id '" + child_id + "' has been added", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(getContext(), "This child has been Enrolled to this class already!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        else {
                                            Toast.makeText(getContext(), "Этот ученик уже есть в классе", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
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
    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(getContext(),"Please accept for required permission",Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        }
        else openGallery();
    }
    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }
    private boolean isChildAssigned(String childidList, String NewChildid) { return childidList.contains(NewChildid); }
}

