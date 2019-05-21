package com.example.aws.blogapp.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.aws.blogapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class RegisterActivity extends Fragment implements View.OnClickListener {
    ImageView ImgUserPhoto;
    static int PReqCode = 1 ;
    static int REQUESCODE = 1 ;
    Uri pickedImgUri ;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private EditText newname;
    private EditText newuserid;
    private EditText newpassword;
    private Button Registerme;
    private RadioButton Radio_Teacher;
    private RadioButton Radio_Parent;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    public RegisterActivity() {
        // Required empty public constructor
    }
    public static RegisterActivity newInstance(String param1, String param2) {
        RegisterActivity fragment = new RegisterActivity();
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
        View view = inflater.inflate(R.layout.activity_register, container, false);
        progressDialog = new ProgressDialog(getActivity());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.getInstance().signOut();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // EditText
        newname = view.findViewById(R.id.New_UserName);
        newuserid = view.findViewById(R.id.New_Email);
        newpassword = view.findViewById(R.id.New_Password);

        // Buttons
        Registerme = view.findViewById(R.id.Button_Register);
        Registerme.setOnClickListener(this);

        Radio_Teacher =  view.findViewById(R.id.New_Teacher);
        Radio_Parent = view.findViewById(R.id.New_Parent);

        ImgUserPhoto = view.findViewById(R.id.register_user) ;

        return view;
    }

    private void RegisterUser()
    {
        final String new_name = newname.getText().toString();
        String new_email = newuserid.getText().toString();
        String new_password = newpassword.getText().toString();

        if (new_name.isEmpty()) {
            Toast.makeText(getActivity(), "User Name cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (new_email.isEmpty()) {
            Toast.makeText(getActivity(), "Email Id cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (new_password.isEmpty()) {
            Toast.makeText(getActivity(), "Password cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(new_password.length() <  8)
        {
            Toast.makeText(getActivity(), "Too short! Password must be at least 8 characters long.", Toast.LENGTH_LONG).show();
            newpassword.setText("");
            return;
        }

        progressDialog.setTitle("Registering");
        progressDialog.setMessage("Creating your account...");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(new_email, new_password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()) {
                    String uid = firebaseAuth.getCurrentUser().getUid();
                    Toast.makeText(getActivity(), "Your new account has been created!", Toast.LENGTH_SHORT).show();
                    AddUserName(new_name, uid);
                    FirebaseAuth.getInstance().signOut();
                    updateUserInfo(new_name,pickedImgUri,firebaseAuth.getCurrentUser());
                    sendemailverifiaction();
                    Intent i = new Intent(getActivity(), Home.class);
                    startActivity(i);
                    getActivity().finish();
                }
                else
                {
                    Toast.makeText(getActivity(), "Something went wrong! Your registration failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void AddUserName(String username, String uid)
    {
        if(Radio_Teacher.isChecked()) {
            databaseReference.child("admin").child(uid).child("name").setValue(username);
            databaseReference.child("admin").child(uid).child("posts").setValue("0");
        }
        else if (Radio_Parent.isChecked()) {
            databaseReference.child("staff").child(uid).child("name").setValue(username);
            databaseReference.child("staff").child(uid).child("room").setValue("");

        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Button_Register:
                RegisterUser();
                break;
            case R.id.register_user:
                Toast.makeText(getContext(),"Enter",Toast.LENGTH_SHORT).show();
                check();
                break;
        }
    }


    private void updateUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();
                        currentUser.updateProfile(profleUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            showMessage("Register Complete");
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }
    public void check(){
        if (Build.VERSION.SDK_INT >= 22) {
            checkAndRequestForPermission();
        }
        else { openGallery(); }
    }
    private void showMessage(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }
    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(getContext(),"Please accept for required permission",Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        }
        else
            openGallery();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {
            pickedImgUri = data.getData() ;
            ImgUserPhoto.setImageURI(pickedImgUri);
        }
    }
    private void sendemailverifiaction(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getContext(),"Check your Email for verification",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            });
        }
    }


}
