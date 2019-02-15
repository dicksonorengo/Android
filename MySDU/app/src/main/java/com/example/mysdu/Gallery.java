package com.example.mysdu;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;


public class Gallery extends AppCompatActivity {
    private Integer[] images={R.drawable.nature1,R.drawable.nature2,R.drawable.nature3, R.drawable.nature4,R.drawable.nature5,R.drawable.nature6,R.drawable.nature8};
    private ImageView imageview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);

        android.widget.Gallery imgGallery = findViewById(R.id.gallery);

        imgGallery.setAdapter(new ImageAdapter(this));
        imageview = (ImageView) findViewById(R.id.imageView);
        imgGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                Toast.makeText(getApplicationContext(), "Image " + arg2,Toast.LENGTH_SHORT).show();
                imageview.setImageResource(images[arg2]);
            }
        });

    }
    public class ImageAdapter extends BaseAdapter {
        private Context context;
        int imageBackground;
        public ImageAdapter(Context context) {
            this.context = context;
        }
        @Override
        public int getCount() {
            return images.length;
        }
        @Override
        public Object getItem(int arg0) {
            return arg0;
        }
        @Override
        public long getItemId(int arg0) {

            return arg0;
        }
        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(images[arg0]);
            return imageView;
        }
    }
}
