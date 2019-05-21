package com.example.teacher_parent;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity_TeacherMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    private static String email_curr, uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__teacher_main);
        Toolbar toolbar =  findViewById(R.id.teacher_toolbar);
        toolbar.setTitle("Билим Арт");
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final View hView =  navigationView.getHeaderView(0);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        email_curr = firebaseAuth.getCurrentUser().getEmail();
        uid = firebaseAuth.getCurrentUser().getUid();
        databaseReference.child("staff").child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView nav_username = hView.findViewById(R.id.Nav_teacher_name);
                String name_small = dataSnapshot.getValue().toString();
                String name_big = name_small.substring(0, 1).toUpperCase() + name_small.substring(1);
                nav_username.setText(name_big);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        TextView nav_email =  hView.findViewById(R.id.Nav_teacher_email);
        nav_email.setText(email_curr);
        getSupportFragmentManager().beginTransaction().replace(R.id.teacher_content_holder,new Fragment_TeacherClassList()).commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
       if (id == R.id.teacher_logout) {
           PrefManager pref = new PrefManager(this);
           pref.resetData();
           FirebaseAuth.getInstance().signOut();
           Intent i = new Intent(this, MainActivity.class);
           startActivity(i);
           finish();
        }
        return true;
    }
}
