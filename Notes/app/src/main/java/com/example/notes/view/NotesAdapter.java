package com.example.notes.view;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.notes.R;
import com.example.notes.database.model.Note;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {
    private Context context;
    private List<Note> notesList;
    private int btn=0;

    private MainActivity activity;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView note;
        public TextView timestamp;
        private LinearLayout relativeLayout;
        private LinearLayout bottom;

        public MyViewHolder(View view) {
            super(view);
            note = view.findViewById(R.id.note);
            timestamp = view.findViewById(R.id.timestamp);
            relativeLayout = view.findViewById(R.id.relative);
            bottom = view.findViewById(R.id.linear);

        }
        public TextView getTimestamp() {
            return timestamp;
        }
    }
    public NotesAdapter(Context context, List<Note> notesList) {
        this.context = context;
        this.notesList = notesList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Note note = notesList.get(position);
        holder.note.setText(note.getNote());
        holder.timestamp.setText(formatDate(note.getTimestamp()));
        /*holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while (holder.bottom.getBackground().toString()!=null){
                    if(holder.bottom.getBackground().toString()=="#fffffacb"){
                        holder.bottom.setBackgroundColor(R.string.red);
                    }
                    if(holder.bottom.getBackground().toString()=="#FF0000"){
                        holder.bottom.setBackgroundColor(R.string.blue);
                    }
                    if(holder.bottom.getBackground().toString()=="#0000FF"){
                        holder.bottom.setBackgroundColor(R.string.green);
                    }
                    if(holder.bottom.getBackground().toString()=="#00E100"){
                        holder.bottom.setBackgroundColor(R.string.mmmm);
                    }
                }
            }
        });*/

    }


    @Override
    public int getItemCount() {
        return notesList.size();
    }
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            return fmtOut.format(date);
        } catch (ParseException e) {
            e.getErrorOffset();
        }
        return "";
    }

}
