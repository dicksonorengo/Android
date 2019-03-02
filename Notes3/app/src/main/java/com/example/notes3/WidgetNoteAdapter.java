package com.example.notes3;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class WidgetNoteAdapter extends ArrayAdapter<Note> {
    ArrayList<Note> notes;
    public WidgetNoteAdapter(@NonNull Context context, ArrayList<Note> notes) {
        super(context, R.layout.note_item_layout);
        this.notes = notes;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View custom_view = inflater.inflate(R.layout.note_item_layout, parent, false);

        TextView date = custom_view.findViewById(R.id.note_date);
        TextView text = custom_view.findViewById(R.id.note_content);
        final CardView card = custom_view.findViewById(R.id.note__);

        date.setText(notes.get(position).getDate());
        text.setText(notes.get(position).getText());
        card.setCardBackgroundColor(getContext().getResources().getColor(MainActivity.colors[
                notes.get(position).getCurrent_color()]));

        return custom_view;
    }
}
