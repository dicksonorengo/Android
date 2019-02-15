package com.example.mysdu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Staff extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);

        final ArrayList<Cons_Contact> contact = new ArrayList<>();

        contact.add(new Cons_Contact("Kanat Kozhakhmet","Rector",R.drawable.staff1));
        contact.add(new Cons_Contact("Daniyar Baimakhan","Senior Lecturer",R.drawable.staff2));
        contact.add(new Cons_Contact("Meirambek Zhaparov","Dean",R.drawable.staff3));
        contact.add(new Cons_Contact("Darkhan Kuanyshbay","Senior Lecturer",R.drawable.staff4));
        contact.add(new Cons_Contact("Cemil Turan","Assistant Professor",R.drawable.staff5));
        contact.add(new Cons_Contact("Alexandr Ivanov","Senior Lecturer",R.drawable.staff7));
        contact.add(new Cons_Contact("Rashid Baimukashev","Vice chairman",R.drawable.staff8));
        contact.add(new Cons_Contact("Abay Nussipbekov","Assistant Professor",R.drawable.staff9));
        contact.add(new Cons_Contact("Danday Yskakuly","Professor",R.drawable.staff10));
        contact.add(new Cons_Contact("Rashid Baimukashev","Senior Lecturer",R.drawable.staff8));
        ListView listView = findViewById(R.id.list);
        CustomAdapter1 customAdapter = new CustomAdapter1(contact);
        listView.setAdapter(customAdapter);
    }
    class CustomAdapter1 extends ArrayAdapter<Cons_Contact> {
        ArrayList<Cons_Contact> nws;
        public CustomAdapter1(ArrayList<Cons_Contact> nws) {
            super(Staff.this, R.layout.item);
            this.nws = nws;
        }
        @Override
        public int getCount() {
            return nws.size();
        }
        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            final View cv = getLayoutInflater().inflate(R.layout.item,parent, false);
            ImageView imageView = cv.findViewById(R.id.image);
            TextView textView = cv.findViewById(R.id.textView2);
            TextView text = cv.findViewById(R.id.textView7);

            textView.setText(nws.get(position).getName());
            text.setText(nws.get(position).getPhone());
            imageView.setImageResource(nws.get(position).getPhoto());
            return cv;
        }
    }
}
