package com.example.teacher_parent;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Fragment_CreateAccount extends Fragment implements View.OnClickListener{
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
        firebaseAuth.getInstance().signOut();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // EditText
        newname = (EditText) view.findViewById(R.id.New_UserName);
        newuserid = (EditText) view.findViewById(R.id.New_Email);
        newpassword = (EditText) view.findViewById(R.id.New_Password);

        // Buttons
        Registerme = (Button) view.findViewById(R.id.Button_Register);
        Registerme.setOnClickListener(this);

        Radio_Teacher = (RadioButton) view.findViewById(R.id.New_Teacher);
        Radio_Parent = (RadioButton) view.findViewById(R.id.New_Parent);

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


        firebaseAuth.createUserWithEmailAndPassword(new_email, new_password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()) {
                    String uid = firebaseAuth.getCurrentUser().getUid();
                    Toast.makeText(getActivity(), "Your new account has been created!", Toast.LENGTH_SHORT).show();
                    AddUserName(new_name, uid);
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

    private void AddUserName(String username, String uid)
    {
        if(Radio_Teacher.isChecked()) {
            databaseReference.child("staff").child(uid).child("name").setValue(username);
            databaseReference.child("staff").child(uid).child("class_ids").setValue("0");
        }
        else if (Radio_Parent.isChecked()) {
            databaseReference.child("parents").child(uid).child("name").setValue(username);
            databaseReference.child("parents").child(uid).child("children").setValue("");
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Button_Register:
                RegisterUser();
        }
    }
}
