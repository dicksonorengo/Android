package com.example.finalprojectvignette.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.finalprojectvignette.Model.Comment;
import com.example.finalprojectvignette.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    private List<Comment> mdata;

    public CommentAdapter(Context context, List<Comment> mdata) {
        this.context = context;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View commentview = LayoutInflater.from(context).inflate(R.layout.row_comment_item,parent,false);
        return new CommentViewHolder(commentview);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder commentViewHolder, int position) {
        //Glide.with(context).load(mData.get(position).getUserPhoto()).into(holder.imgPostProfile);
        Glide.with(context).load(mdata.get(position).getUimg()).into(commentViewHolder.img_user);
        commentViewHolder.tv_name.setText(mdata.get(position).getUname());
        commentViewHolder.tv_content.setText(mdata.get(position).getContent());
        commentViewHolder.tv_date.setText(timestampToString((Long)mdata.get(position).getTimestamp()));


    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        ImageView img_user;
        TextView tv_name,tv_content,tv_date;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            img_user = itemView.findViewById(R.id.comment_user_image);
            tv_name = itemView.findViewById(R.id.comment_username);
            tv_content = itemView.findViewById(R.id.comment_content);
            tv_date = itemView.findViewById(R.id.comment_date);
        }
    }

    private String timestampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("hh:mm",calendar).toString();
        return date;
    }
}
