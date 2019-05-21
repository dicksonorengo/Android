package com.example.teacher_parent;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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


public class MyGridRecyclerAdapter extends RecyclerView.Adapter<MyGridRecyclerAdapter.GridViewHolder> {
    public interface GridItemClickListener{
        void onGridItemClick(View v, HashMap<String, String> child);
    }

    private static List<HashMap<String,String>> children = new ArrayList<>();
    private static List<HashMap<String,String>> selectedKids = new ArrayList<>();
    private GridItemClickListener mGridItemClickListner;

    public void setGridItemClickListner(GridItemClickListener listner){ mGridItemClickListner = listner;}

    public void setChildren(List<HashMap<String,String>> children){
        this.children = children;
        notifyDataSetChanged();
    }

    public void addChild(HashMap<String,String> child)
    {
        children.add(child);
        notifyItemInserted(children.size()-1);
    }

    public List<HashMap<String,String>> getChildren(){return children;}

    @Override
    public MyGridRecyclerAdapter.GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_grid_card, parent, false);
        return new MyGridRecyclerAdapter.GridViewHolder(v,mGridItemClickListner);
    }

    @Override
    public void onBindViewHolder(final GridViewHolder holder, int position) {
        HashMap<String,String> child = children.get(position);
        holder.selected = false;
        holder.grid_name.setText(child.get("name"));
        if(child.get("status").equalsIgnoreCase("none")){
            holder.status.setVisibility(View.INVISIBLE);
        } else {
            holder.status.setVisibility(View.VISIBLE);
            holder.status.setText(child.get("status"));
        }

        FirebaseDatabase.getInstance().getReference().child("children").child(child.get("id")).child("pic").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String pic_name = dataSnapshot.getValue().toString();
                if(!pic_name.isEmpty()||pic_name.equalsIgnoreCase("0")) {
                    Glide.with(holder.itemView.getContext()).load(pic_name).into(holder.grid_img);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
    @Override
    public int getItemCount() {
        return children.size();
    }

    public List<HashMap<String,String>> getSelectedKids(){return selectedKids;}
    public void resetSelection(){
        selectedKids = new ArrayList<>();
    }

    static class GridViewHolder extends RecyclerView.ViewHolder{
        private ImageView grid_img;
        private TextView grid_name;
        private TextView status;
        private boolean selected;
        public GridViewHolder(final View itemView, final GridItemClickListener mListner){
            super(itemView);
            grid_img = itemView.findViewById(R.id.child_grid_img);
            grid_name = itemView.findViewById(R.id.child_grid_name);
            status = itemView.findViewById(R.id.child_status_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    selected = !selected;
                    if(selected) {
                        grid_img.setBackgroundColor(v.getResources().getColor(R.color.colorPrimary, null));
                        selectedKids.add(children.get(getAdapterPosition()));
                    } else {
                        grid_img.setBackgroundColor(v.getResources().getColor(R.color.white, null));
                        selectedKids.remove(children.get(getAdapterPosition()));
                    }
                    mListner.onGridItemClick(v,children.get(getAdapterPosition()));
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){

                    return true;
                }
            });
        }
    }
}
