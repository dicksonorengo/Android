package com.example.finalprojectvignette.Student_Activity;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.finalprojectvignette.Activity.ActivityAdmin;
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

import static android.app.Activity.RESULT_OK;

public class Student_Profile  extends Fragment {
    public Student_Profile() {
        // Required empty public constructor
    }

    public static Student_Profile newInstance(String id, String name) {
        Bundle args = new Bundle();
        args.putString("id",id);
        args.putString("name",name);
        Student_Profile fragment = new Student_Profile();
        fragment.setArguments(args);
        return fragment;
    }

    private String childid,childName;
    private TextView tv_childname,tv_dob,tv_parent,tv_num,tv_meds,tv_allergy,tv_notes;
    private ImageView iv_child;
    private Button saveBtn;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    DatabaseReference childrenTb = db.child("children");
    DatabaseReference child;
    private static final int GALLERY_INT = 2;
    private static final int PReqCode = 2 ;
    static String imagedownload;
    private Uri CurrImageURI = null;
    private boolean isImageUploaded = false;
    private ProgressDialog myProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Some permissions for VM to allow local storage usage
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.student_profile, container, false);
        setHasOptionsMenu(true);
        tv_childname = v.findViewById(R.id.et_childName);
        tv_dob = v.findViewById(R.id.et_childDOB);
        tv_parent = v.findViewById(R.id.et_childParentName);
        tv_num =v.findViewById(R.id.et_childParentNum);
        tv_meds = v.findViewById(R.id.et_childMeds);
        tv_allergy = v.findViewById(R.id.et_childAllergy);
        tv_notes= v.findViewById(R.id.et_childNotes);
        iv_child = v.findViewById(R.id.child_profile_edit_pic) ;
        saveBtn = v.findViewById(R.id.btn_child_profile_save);
        childid = getArguments().getString("id");
        childName = getArguments().getString("name");
        child = childrenTb.child(childid);

        myProgress = new ProgressDialog(getContext());
        myProgress.setTitle("Updating Profile");

        Toolbar toolbar;
        toolbar =  getActivity().findViewById(R.id.parent_toolbar);
        toolbar.setTitle("Student Profile");
        setCurrentData();

        iv_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestForPermission();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tv_childname.getText().toString().isEmpty())
                    child.child("name").setValue(tv_childname.getText().toString());
                if(!tv_dob.getText().toString().isEmpty())
                    child.child("dob").setValue(tv_dob.getText().toString());
                if(!tv_parent.getText().toString().isEmpty())
                    child.child("parent").setValue(tv_parent.getText().toString());
                if(!tv_num.getText().toString().isEmpty())
                    child.child("parent_num").setValue(tv_num.getText().toString());
                if(!tv_meds.getText().toString().isEmpty())
                    child.child("meds").setValue(tv_meds.getText().toString());
                if(!tv_allergy.getText().toString().isEmpty())
                    child.child("allergy").setValue(tv_allergy.getText().toString());
                if(!tv_notes.getText().toString().isEmpty())
                    child.child("notes").setValue(tv_notes.getText().toString());
                CheckAndAddPic();

            }
        });
        return v;
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
        startActivityForResult(galleryIntent,GALLERY_INT);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INT && resultCode == RESULT_OK && data!=null) {
            CurrImageURI = data.getData();
            iv_child.setImageURI(CurrImageURI);
            isImageUploaded = true;
        }
    }
    private void CheckAndAddPic(){
        if(!isImageUploaded){
            getActivity().getSupportFragmentManager().popBackStack();
        } else {
            myProgress.setMessage("Uploading Photo");
            myProgress.show();
            StorageReference storagePref = FirebaseStorage.getInstance().getReference().child("blog_images");
            final StorageReference storage = storagePref.child(CurrImageURI.getLastPathSegment());
            storage.putFile(CurrImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(final Uri uri) {
                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    imagedownload = uri.toString();
                                    databaseReference.child("children").child(childid).child("pic").setValue("");
                                    databaseReference.child("children").child(childid).child("pic").setValue(imagedownload);
                                    myProgress.dismiss();
                                    getActivity().getSupportFragmentManager().popBackStack();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),"Fail",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
    private void setCurrentData(){
        child.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tv_childname.setText((dataSnapshot.child("name").getValue() != null)? dataSnapshot.child("name").getValue().toString() : "");
                tv_dob.setText((dataSnapshot.child("dob").getValue() != null)? dataSnapshot.child("dob").getValue().toString() : "");
                tv_parent.setText((dataSnapshot.child("parent").getValue() != null)? dataSnapshot.child("parent").getValue().toString() : "");
                tv_num.setText((dataSnapshot.child("parent_num").getValue() != null)? dataSnapshot.child("parent_num").getValue().toString() : "");
                tv_meds.setText((dataSnapshot.child("meds").getValue() != null)? dataSnapshot.child("meds").getValue().toString() : "");
                tv_allergy.setText((dataSnapshot.child("allergy").getValue() != null)? dataSnapshot.child("allergy").getValue().toString() : "");
                tv_notes.setText((dataSnapshot.child("notes").getValue() != null)? dataSnapshot.child("notes").getValue().toString() : "");
                final String pic_name = (dataSnapshot.child("pic").getValue() != null)? dataSnapshot.child("pic").getValue().toString() : "";
                FirebaseDatabase.getInstance().getReference().child("children").child(childid).child("pic").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!pic_name.isEmpty()||pic_name.equalsIgnoreCase("0")) {
                            Glide.with(getContext()).load(pic_name).into(iv_child);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.empty_menu,menu);
    }
}
