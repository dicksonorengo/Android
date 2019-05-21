package com.example.finalprojectvignette.Student_Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.finalprojectvignette.Activity.MainActivity;
import com.example.finalprojectvignette.Authentication.PrefManager;
import com.example.finalprojectvignette.Fragment.Fragment_Student_Profile;
import com.example.finalprojectvignette.Fragment.HomeFragment;
import com.example.finalprojectvignette.R;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Student_Navigation_Bottom extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    static String currentChildren;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference db;
    DatabaseReference student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_navigation_bottom);

        Toolbar toolbar = findViewById(R.id.parent_toolbar);
        toolbar.setTitle("Student Activity");
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference();
        student = db.child("student").child(user.getUid());

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        if (savedInstanceState == null) {
            student.child("people").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    currentChildren = dataSnapshot.getValue().toString();
                    if(!currentChildren.equals("")){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home_Student_Fragment_Post().newInstance(currentChildren,currentChildren)).commit();
                    }
                    else {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Student_Home_Fragment()).commit();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            student.child("people").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    currentChildren = dataSnapshot.getValue().toString();
                                    if(!currentChildren.equals("")){
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home_Student_Fragment_Post().newInstance(currentChildren,currentChildren)).commit();
                                    }
                                    else {
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Student_Home_Fragment()).commit();
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });
                            break;
                        case R.id.nav_favorites:
                            student.child("people").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    currentChildren = dataSnapshot.getValue().toString();
                                    if(!currentChildren.equals("")){
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Student_Profile().newInstance(currentChildren,currentChildren)).commit();
                                    }
                                    else {
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Profile_Fragment()).commit();
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });
                            break;
                        case R.id.nav_search:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Search_Qr_Fragment()).commit();
                            break;
                    }
                    return true;
                }
            };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.studentaddmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_child) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new StudentAdd_Fragment()).addToBackStack(null).commit();
            return true;
        }
        if (id == R.id.logout) {
            PrefManager pref = new PrefManager(this);
            pref.resetData();
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}