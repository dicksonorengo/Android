package com.example.mysdu;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class Newss extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        final ArrayList<New> ns = new ArrayList<>();
        ns.add(new New("SDU","SDU met an honourable guest from The Waterloo University, Canada ... SDU awarded 100% grant to the world-famous kazakh huntress Aisholpan Nurgaip.",R.drawable.sduu));
        ns.add(new New("SDU","SDU met an honourable guest from The Waterloo University, Canada ... SDU awarded 100% grant to the world-famous kazakh huntress Aisholpan Nurgaip.",R.drawable.sduu));
        ns.add(new New("SDU","SDU met an honourable guest from The Waterloo University, Canada ... SDU awarded 100% grant to the world-famous kazakh huntress Aisholpan Nurgaip.",R.drawable.sduu));
        ns.add(new New("SDU","SDU met an honourable guest from The Waterloo University, Canada ... SDU awarded 100% grant to the world-famous kazakh huntress Aisholpan Nurgaip.",R.drawable.sduu));
        ns.add(new New("SDU","SDU met an honourable guest from The Waterloo University, Canada ... SDU awarded 100% grant to the world-famous kazakh huntress Aisholpan Nurgaip.",R.drawable.sduu));
        ns.add(new New("SDU","SDU met an honourable guest from The Waterloo University, Canada ... SDU awarded 100% grant to the world-famous kazakh huntress Aisholpan Nurgaip.",R.drawable.sduu));
        ListView listView = findViewById(R.id.list);
        CustomAdapter customAdapter = new CustomAdapter(ns);
        listView.setAdapter(customAdapter);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Newss.this,FullDescriptions.class);
                //i.putExtra("array",ns.get(position));
                String transitionName = getString(R.string.transition);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(Newss.this, view, transitionName);
                ActivityCompat.startActivity(Newss.this, i, options.toBundle());
            }
        });*/
    }
    class CustomAdapter extends ArrayAdapter<New> {
       ArrayList<New> nws;
        public CustomAdapter(ArrayList<New> nws) {
            super(Newss.this, R.layout.customlayout);
            this.nws = nws;
        }
        @Override
        public int getCount() {
            return nws.size();
        }
        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            final View c_v = getLayoutInflater().inflate(R.layout.customlayout,parent, false);
            final ImageView imageView = c_v.findViewById(R.id.image);
            TextView textView = c_v.findViewById(R.id.textView2);
            TextView text = c_v.findViewById(R.id.textView7);
            Button readmore = c_v.findViewById(R.id.read_more);
            imageView.setImageResource(nws.get(position).getImage());
            textView.setText(nws.get(position).getTitle());
            text.setText(nws.get(position).getDescription());

            readmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), FullDescriptions.class);
                    //String transitionName = getString(R.string.transition);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(Newss.this, imageView, ViewCompat.getTransitionName(imageView));
                    v.getContext().startActivity(intent,options.toBundle());
                }
            });
            return c_v;
        }
    }
}
