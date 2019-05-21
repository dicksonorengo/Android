package com.example.aws.blogapp.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.aws.blogapp.Fragments.HomeFragment;
import com.example.aws.blogapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends Fragment implements View.OnClickListener{
    private EditText userid;
    private EditText password;
    private Button Loginme;
    private ImageButton btregister;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private RadioButton Radio_Teacher;
    private RadioButton Radio_Parent;
    private CheckBox rememberBox;
    private PrefManager pref;
    private String email,pass,type;

    public LoginActivity() {
        // Required empty public constructor
    }
    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_login, container, false);
        pref = new PrefManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        //FloatingActionButton floatingActionButton = view.findViewById(R.id.fab);
        //floatingActionButton.setVisibility(view.INVISIBLE);

        userid =  view.findViewById(R.id.UsernameField);
        password = view.findViewById(R.id.PasswordField);

        btregister = view.findViewById(R.id.btRegister);

        btregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, new RegisterActivity()).commit();
            }
        });
        Loginme =  view.findViewById(R.id.Button_Submit);
        Loginme.setOnClickListener(this);

        Radio_Teacher = view.findViewById(R.id.Radio_Teacher);
        Radio_Parent = view.findViewById(R.id.Radio_Parent);
        rememberBox = view.findViewById(R.id.remember_ckbox);

        if(pref.isDataSet()){LoginPressed(view);}
        return view;
    }

    public void LoginPressed(View view) {
        if(pref.isDataSet()){
            String[] loginData = pref.getLoginData();
            email = loginData[0];
            pass = loginData[1];
        } else {
            email = userid.getText().toString();
            pass = password.getText().toString();
        }

        if (email.isEmpty()) {
            Toast.makeText(getActivity(), "User Id cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.isEmpty()) {
            Toast.makeText(getActivity(), "Password cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setTitle("Authenticating");
        progressDialog.setMessage("Loggin in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())
                {
                    String uid = firebaseAuth.getCurrentUser().getUid();
                    checkUserType(uid);
                }
                else
                {
                    Toast.makeText(getActivity(), "Loggin failed", Toast.LENGTH_SHORT).show();
                    pref.resetData();
                }
            }
        });
    }
    private void checkUserType(String uid) {
        final boolean result = false;
        if(pref.isDataSet()){
            String[] loginData = pref.getLoginData();
            type = loginData[2];
            if(type.equalsIgnoreCase("admin")) {Radio_Teacher.setChecked(true);}
            else if(type.equalsIgnoreCase("staff")) {Radio_Parent.setChecked(true);}
        }
        if(Radio_Teacher.isChecked())
        {
            type = "admin";
            Intent i = new Intent(getActivity(), Home.class);
            LoginAs("admin", uid, i);
        }

        else if (Radio_Parent.isChecked())
        {
            type = "staff";
            Intent i = new Intent(getActivity(), Staff.class);
            LoginAs("staff", uid, i);
        }
    }
    private void LoginAs(final String usertype, final String uid, final Intent i) {
        databaseReference.child(usertype).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uid)) {
                    if(!pref.isDataSet() && rememberBox.isChecked()){
                        pref.setLoginData(email,pass,type);
                    }
                    startActivity(i);
                    getActivity().finish();
                }
                else
                {
                    Toast.makeText(getActivity(), "You do not have access to " + usertype + " account", Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().signOut();
                    pref.resetData();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Button_Submit:
                LoginPressed(v);
        }
    }
}