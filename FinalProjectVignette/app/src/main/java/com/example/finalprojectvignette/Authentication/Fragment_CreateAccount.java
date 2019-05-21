package com.example.finalprojectvignette.Authentication;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.finalprojectvignette.Activity.MainActivity;
import com.example.finalprojectvignette.Fragment.Fragment_Student_Profile;
import com.example.finalprojectvignette.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


public class Fragment_CreateAccount extends Fragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private EditText newname;
    private EditText newuserid;
    private EditText newpassword;
    private Button Registerme,Login;
    private RadioButton Radio_Admin;
    private RadioButton Radio_Student;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ImageView registerimage;
    public Fragment_CreateAccount() {
        // Required empty public constructor
    }
    public static Fragment_CreateAccount newInstance(String param1, String param2) {
        Fragment_CreateAccount fragment = new Fragment_CreateAccount();
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
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);
        progressDialog = new ProgressDialog(getActivity());
        firebaseAuth = FirebaseAuth.getInstance();
        //firebaseAuth.getInstance().signOut();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        newname = view.findViewById(R.id.New_UserName);
        newuserid = view.findViewById(R.id.New_Email);
        newpassword = view.findViewById(R.id.New_Password);

        Registerme = view.findViewById(R.id.Button_Register);
        Registerme.setOnClickListener(this);


        Login = view.findViewById(R.id.log_in);
        Login.setOnClickListener(this);


        Radio_Admin = view.findViewById(R.id.New_Admin);
        Radio_Student = view.findViewById(R.id.New_Student);

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
            Toast.makeText(getActivity(), "Too short! Password must be atleast 8 characters long.", Toast.LENGTH_LONG).show();
            newpassword.setText("");
            return;
        }
        progressDialog.setTitle("Registering");
        progressDialog.setMessage("Creating your account...");
        progressDialog.show();
        CreateUserAccount(new_email,new_name,new_password);
    }


    private void AddUserName(String username, String uid)
    {
        if(Radio_Admin.isChecked()) {
            databaseReference.child("admin").child(uid).child("name").setValue(username);
            databaseReference.child("admin").child(uid).child("class_ids").setValue("0");
        }
        else if (Radio_Student.isChecked()) {
            databaseReference.child("student").child(uid).child("name").setValue(username);
            databaseReference.child("student").child(uid).child("people").setValue("");
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Button_Register:
                RegisterUser();
                break;
            case R.id.log_in:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MyFrame, new Fragment_Login()).commit();
                break;
        }
    }
    private void CreateUserAccount(String new_email, final String new_name, String new_password) {
        firebaseAuth.createUserWithEmailAndPassword(new_email, new_password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()) {
                    String uid = firebaseAuth.getCurrentUser().getUid();
                    AddUserName(new_name,uid);
                    sendemailverifiaction();
                    FirebaseAuth.getInstance().signOut();
                    Intent i = new Intent(getActivity(), MainActivity.class);
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
