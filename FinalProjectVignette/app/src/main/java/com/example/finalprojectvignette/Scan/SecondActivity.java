package com.example.finalprojectvignette.Scan;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.finalprojectvignette.Activity.PostDetailActivity;
import com.example.finalprojectvignette.Adapter.CommentAdapter;
import com.example.finalprojectvignette.Model.Comment;
import com.example.finalprojectvignette.Model.Post;
import com.example.finalprojectvignette.Model.Vinetka;
import com.example.finalprojectvignette.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SecondActivity extends AppCompatActivity {
    ImageView imgPost,imgUserPost,imgCurrentUser;
    TextView txtPostDesc,txtPostDateName,txtPostTitle;
    VideoView videoView;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private MediaController mc;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_student_image);

        imgPost =findViewById(R.id.post_detail_img);
        imgUserPost = findViewById(R.id.post_detail_user_img);
        videoView = findViewById(R.id.videoview);
        txtPostTitle = findViewById(R.id.post_detail_title);
        txtPostDesc = findViewById(R.id.post_detail_desc);
        txtPostDateName = findViewById(R.id.post_detail_date_name);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        String barcode = getIntent().getStringExtra("code");
        if (TextUtils.isEmpty(barcode)) {
            Toast.makeText(getApplicationContext(), "Barcode is empty!", Toast.LENGTH_LONG).show();
            finish();
        }
        searchBarcode(barcode);
    }
    private void searchBarcode(final String barcode) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postsnap: dataSnapshot.child("Posts").getChildren()) {
                        String postt = postsnap.getKey();
                        if(barcode.equals(postt)){
                            Toast.makeText(getApplicationContext(),"Yeees equal qr code ",Toast.LENGTH_SHORT).show();

                            String postImage = dataSnapshot.child("Posts").child(barcode).child("picture").getValue().toString();
                            Glide.with(getApplicationContext()).load(postImage).into(imgPost);

                            String postTitle = dataSnapshot.child("Posts").child(barcode).child("title").getValue().toString();
                            txtPostTitle.setText(postTitle);

                            /*String userpostImage = getIntent().getExtras().getString("userPhoto");
                            Glide.with(getApplicationContext()).load(userpostImage).into(imgUserPost);*/

                            String postDescription = dataSnapshot.child("Posts").child(barcode).child("description").getValue().toString();
                            txtPostDesc.setText(postDescription);

                            //Glide.with(this).load(firebaseUser.getPhotoUrl()).into(imgCurrentUser);

                            String date = dataSnapshot.child("Posts").child(barcode).child("timeStamp").getValue().toString();
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(Long.parseLong(date));
                            SimpleDateFormat fmt = new SimpleDateFormat("yy:dd:hh:mm", Locale.US);
                            String time = fmt.format(cal.getTime());
                            txtPostDateName.setText(time);


                            String postVideo = dataSnapshot.child("Posts").child(barcode).child("video").getValue().toString();
                            videoView.setVideoURI(Uri.parse(postVideo));
                            videoView.requestFocus();
                            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                                        @Override
                                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                                            mc = new MediaController(  getApplicationContext());
                                            videoView.setMediaController(mc);
                                            mc.setAnchorView(videoView);
                                        }
                                    });
                                }
                            });
                            videoView.start();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Not equal qr code",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
