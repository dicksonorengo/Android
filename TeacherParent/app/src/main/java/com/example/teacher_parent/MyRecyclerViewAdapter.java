package com.example.teacher_parent;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    public interface RecyclerItemClickListener{
        void onItemClick(View v, HashMap<String, String> child);
    }
    private static List<HashMap<String,String>> children = new ArrayList<>();
    private RecyclerItemClickListener mItemClickListner;
    public void setItemClickListener(final RecyclerItemClickListener listener){
        this.mItemClickListner = listener;
    }
    public void setChildren(List<HashMap<String,String>> children){
        this.children = children;
        notifyDataSetChanged();
    }
    public void addChild(HashMap<String,String> child) {
        children.add(child);
        notifyItemInserted(children.size()-1);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_list_card, parent, false);
        return new ViewHolder(v,mItemClickListner);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HashMap<String,String> child = children.get(position);
        holder.holder_child_name_textview.setText(child.get("name"));
        holder.image.setImageResource(R.drawable.blank_profile_pic);
        FirebaseDatabase.getInstance().getReference().child("children").child(child.get("id")).child("pic").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String pic_name = dataSnapshot.getValue().toString();
                if(!pic_name.isEmpty()||pic_name.equalsIgnoreCase("0")) {
                    Glide.with(holder.itemView.getContext()).load(pic_name).into(holder.image);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
    @Override
    public int getItemCount() {
        return children.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView holder_child_name_textview;
        private ImageView image;
        private RecyclerItemClickListener mItemClickListner;
        public ViewHolder(final View itemView, RecyclerItemClickListener listener) {
            super(itemView);
            holder_child_name_textview = itemView.findViewById(R.id.child_list_name);
            image = itemView.findViewById(R.id.child_list_pic);
            mItemClickListner = listener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mItemClickListner != null) {
                        mItemClickListner.onItemClick(v,children.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
}
