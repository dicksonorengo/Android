package com.example.aws.blogapp.Activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.aws.blogapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;
public class SecondActivity extends AppCompatActivity {
    private static final String TAG = SecondActivity.class.getSimpleName();
    // url to search barcode
    private static final String URL = "https://api.jsonbin.io/b/";
    private TextView txtName, txtDuration, txtDirector, txtGenre, txtRating, txtPrice, txtError;
    private ImageView imgPoster;
    private Button btnBuy;
    private ProgressBar progressBar;
    private VideoView videoView;
    private MediaController mc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtName = findViewById(R.id.name);
        imgPoster = findViewById(R.id.poster);
        progressBar = findViewById(R.id.progressBar);
        videoView = findViewById(R.id.videoview);



        String barcode = getIntent().getStringExtra("code");
        // close the activity in case of empty barcode
        if (TextUtils.isEmpty(barcode)) {
            Toast.makeText(getApplicationContext(), "Barcode is empty!", Toast.LENGTH_LONG).show();
            finish();
        }
        searchBarcode(barcode);
    }
    private void searchBarcode(final String barcode) {
        // making volley's json request
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, URL + barcode, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "response: " + response.toString());
                // check for success status
                if (!response.has("error")) {
                    // received movie response
                    renderMovie(response);
                } else {
                    // no movie found
                    showNoTicket();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                System.out.println(URL+barcode);
                showNoTicket();
            }
        });
        MyApplication.getInstance().addToRequestQueue(jsonObjReq);
    }
    private void showNoTicket() {
        txtError.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
    private void renderMovie(JSONObject response) {
        try {
            // converting json to movie object
            Vinetka vinetka = new Gson().fromJson(response.toString(), Vinetka.class);
            if (vinetka != null) {
                txtName.setText(vinetka.getName());
                //txtDuration.setText(vinetka.getCity());
                progressBar.setVisibility(View.GONE);

                Glide.with(this).load(vinetka.getImage()).into(imgPoster);

                String postVideo = vinetka.getVideo();
                videoView.setVideoURI(Uri.parse(postVideo));
                videoView.requestFocus();

                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                            @Override
                            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                                mc = new MediaController(SecondActivity.this);
                                videoView.setMediaController(mc);
                                mc.setAnchorView(videoView);
                            }
                        });
                    }
                });

                videoView.start();

            } else {
                // movie not found
                showNoTicket();
            }
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "JSON Exception: " + e.getMessage());
            showNoTicket();
            Toast.makeText(getApplicationContext(), "Check your LogCat for full report", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // exception
            showNoTicket();
            Toast.makeText(getApplicationContext(), "Check your LogCat for full report", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private class Vinetka{
        String id;
        String title;
        String video;
        String picture;
        public String getVideo(){
            return  video;
        }
        public String getId() { return id; }
        public String getName() { return title; }
        public String getImage(){return picture;}
    }
}
