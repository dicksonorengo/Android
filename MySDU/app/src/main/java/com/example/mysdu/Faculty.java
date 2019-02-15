package com.example.mysdu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Faculty extends AppCompatActivity {
    TextView t1,t2,t3,t4;
    Animation atg, atgtwo, atgthree,atgfour;
    ImageView engineriing,juruspendency,economics,philology;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faculty);

        engineriing = findViewById(R.id.imageView);
        philology = findViewById(R.id.imageView4);
        economics = findViewById(R.id.imageView5);
        juruspendency = findViewById(R.id.imageView6);
        engineriing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Enginering.class);
                startActivity(i);
            }
        });
        philology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Philology.class);
                startActivity(i);
            }
        });
        economics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Economics.class);
                startActivity(i);
            }
        });
        juruspendency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Juruspendency.class);
                startActivity(i);
            }
        });

        t1 = findViewById(R.id.textView3);
        t2 = findViewById(R.id.textView4);
        t3 = findViewById(R.id.textView5);
        t4 = findViewById(R.id.textView6);

        atg = AnimationUtils.loadAnimation(this, R.anim.atg);
        atgtwo = AnimationUtils.loadAnimation(this, R.anim.atgtwo);
        atgthree = AnimationUtils.loadAnimation(this, R.anim.atgthree);
        atgfour = AnimationUtils.loadAnimation(this, R.anim.atgtwo);

        engineriing.startAnimation(atg);
        philology.startAnimation(atg);
        juruspendency.startAnimation(atgtwo);
        economics.startAnimation(atgfour);

        t1.startAnimation(atg);
        t2.startAnimation(atg);
        t3.startAnimation(atgtwo);
        t4.startAnimation(atgfour);
    }
}
