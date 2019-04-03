package com.example.googlemap;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PlaceAdapter extends ArrayAdapter<Place>{
    ArrayList<Place> contents;
    private DataBaseHelper helper;
    public PlaceAdapter(Context context, ArrayList<Place> contents) {
        super(context,R.layout.list_item);
        this.contents = contents;
        helper = new DataBaseHelper(getContext());
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View custom_view = layoutInflater.inflate(R.layout.list_item,parent,false);
        TextView title = custom_view.findViewById(R.id.textView2);
        title.setText(contents.get(position).getTitle());
        return custom_view;
    }
    @Override
    public int getCount() {
        return contents.size();
    }
    public void setContents(ArrayList<Place> contents){
        this.contents = contents;
        notifyDataSetChanged();
    }
}