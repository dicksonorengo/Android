package com.spaceo.cardviewtutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.spaceo.cardviewtutorial.adapter.DetailAdapter;
import com.spaceo.cardviewtutorial.bean.Detail;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DetailAdapter detailAdapter;
    private ArrayList<Detail> arrayDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        /**
         * Add item in array
         */

        arrayDetails.add(new Detail("John", "john@gmail.com"));
        arrayDetails.add(new Detail("Andrew", "andrew@gmail.com"));
        arrayDetails.add(new Detail("Kunal", "kunal@gmail.com"));
        arrayDetails.add(new Detail("Sunder", "sunder@gmail.com"));
        arrayDetails.add(new Detail("Azhar", "azher@gmail.com"));
        arrayDetails.add(new Detail("Ronchi", "ronchi@gmail.com"));
        arrayDetails.add(new Detail("phunshukh", "phunshukh@gmail.com"));

        /**
         * set adapter of recyclview
         */
        detailAdapter = new DetailAdapter(this, arrayDetails);
        mRecyclerView.setAdapter(detailAdapter);
    }
}
