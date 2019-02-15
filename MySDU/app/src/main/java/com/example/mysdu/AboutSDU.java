package com.example.mysdu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class AboutSDU extends AppCompatActivity {
    TextView pagetitle, pagesubtitle;
    ImageView packagePlace;
    SeekBar packageRange;
    Animation atg, packageimg,atgtwo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        atg = AnimationUtils.loadAnimation(this, R.anim.atg);
        atgtwo = AnimationUtils.loadAnimation(this, R.anim.atgtwo);
        packageimg = AnimationUtils.loadAnimation(this, R.anim.packageimg);
        pagetitle = findViewById(R.id.pagetitle);
        pagesubtitle = findViewById(R.id.pagesubtitle);
        packagePlace = findViewById(R.id.packagePlace);
        packageRange = findViewById(R.id.packageRange);
        packageRange.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 35){
                    pagetitle.setText("BAD");
                    pagesubtitle.setText("The simply text be dummies too good and easier");
                    packagePlace.setImageResource(R.drawable.icstarter);

                    // pass animation
                    packagePlace.startAnimation(packageimg);
                    pagetitle.startAnimation(atg);
                    pagesubtitle.startAnimation(atg);
                }
                else if(progress == 75){
                    pagetitle.setText("GOOD");
                    pagesubtitle.setText("The simply text be dummies too good and easier");
                    packagePlace.setImageResource(R.drawable.icbusinessplayer);

                    // pass animation
                    packagePlace.startAnimation(packageimg);
                    pagetitle.startAnimation(atg);
                    pagesubtitle.startAnimation(atg);
                }
                else if(progress == 100){
                    pagetitle.setText("Excellent");
                    pagesubtitle.setText("The simply text be dummies too good and easier");
                    packagePlace.setImageResource(R.drawable.icvip);

                    packagePlace.startAnimation(packageimg);
                    pagetitle.startAnimation(atg);
                    pagesubtitle.startAnimation(atg);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        packageRange.startAnimation(atg);
    }
}
