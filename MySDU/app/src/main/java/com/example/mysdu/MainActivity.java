package com.example.mysdu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public classMainActivity extends AppCompatActivity {
    Animation atg, atgtwo, atgthree,atgfour;
    ImageView imageView3;
    Button btnguide;
    LinearLayout linearLayout2;
    ImageView staff,faculty,news,gallery;
    TextView pagetitle, pagesubtitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        atg = AnimationUtils.loadAnimation(this, R.anim.);
        atgtwo = AnimationUtils.loadAnimation(this, R.anim.atgtwo);
        atgthree = AnimationUtils.loadAnimation(this, R.anim.atgthree);
        atgfour = AnimationUtils.loadAnimation(this, R.anim.atgtwo);
        pagetitle = findViewById(R.id.pagetitle);
        pagesubtitle = findViewById(R.id.pagesubtitle);
        imageView3 = findViewById(R.id.imageView3);
        btnguide = findViewById(R.id.btnguide);
        linearLayout2 = findViewById(R.id.linearLayout2);

        gallery = findViewById(R.id.gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(getApplicationContext(),Gallery.class);
                startActivity(a);
            }
        });
        faculty = findViewById(R.id.faculty);
        faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(getApplicationContext(),Faculty.class);
                startActivity(j);
            }
        });
        btnguide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AboutSDU.class);
                startActivity(i);
            }
        });

        news = findViewById(R.id.news);
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(getApplicationContext(),Newss.class);
                startActivity(k);
            }
        });

        staff = findViewById(R.id.staff);
        staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Staff.class);
                startActivity(i);
            }
        });

        imageView3.startAnimation(atg);
        pagetitle.startAnimation(atgtwo);
        pagesubtitle.startAnimation(atgtwo);
        linearLayout2.startAnimation(atgfour);
        btnguide.startAnimation(atgthree);
    }
}
