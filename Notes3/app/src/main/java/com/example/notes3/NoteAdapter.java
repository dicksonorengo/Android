package com.example.notes3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NoteAdapter extends ArrayAdapter<Note> {
    private ArrayList<Note> notes;
    private DatabaseHelper helper;

    public NoteAdapter(@NonNull Context context, final ArrayList<Note> notes) {
        super(context, R.layout.note_item_layout);
        this.notes = notes;
        helper = new DatabaseHelper(getContext());
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View custom_view = inflater.inflate(R.layout.note_item_layout, parent, false);
        TextView date = custom_view.findViewById(R.id.note_date);
        TextView text = custom_view.findViewById(R.id.note_content);
        final ImageView del = custom_view.findViewById(R.id.delete_image);
        final CardView card = custom_view.findViewById(R.id.note__);
        RelativeLayout inter = custom_view.findViewById(R.id.note_interface);

        date.setText(notes.get(position).getDate());
        text.setText(notes.get(position).getText());
        card.setCardBackgroundColor(getContext().getResources().getColor(MainActivity.colors[
                notes.get(position).getCurrent_color()]));

        custom_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog nagDialog = new Dialog(getContext());
                nagDialog.setCancelable(true);
                nagDialog.setContentView(R.layout.note_edit);

                final String old_text = notes.get(position).getText();

                final EditText note_text = nagDialog.findViewById(R.id.edit_note_content);
                note_text.setText(old_text);

                nagDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        nagDialog.dismiss();
                    }
                });
                Button no = nagDialog.findViewById(R.id.no_button);
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nagDialog.dismiss();
                    }
                });
                Button yes = nagDialog.findViewById(R.id.yes_button);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String new_text = note_text.getText().toString();
                        int new_color = notes.get(position).getCurrent_color();
                        if (new_text.trim().equals("")) {
                            boolean delete = helper.deleteNote(notes.get(position).getID());
                            if (delete){
                                setNotes(helper.getNotes());
                                /*Toast.makeText(getContext(), "Delete!!", Toast.LENGTH_SHORT).show();*/}
                        }
                        else if (!old_text.equals(new_text.trim())) {
                            boolean success = helper.updateNote(notes.get(position).getID(), new_text, new_color);
                            if (success) {
                                setNotes(helper.getNotes());
                                /*Toast.makeText(getContext(), "Update!!", Toast.LENGTH_SHORT).show();*/
                            } else
                                Toast.makeText(getContext(), "ERror!!", Toast.LENGTH_SHORT).show();
                        }
                        nagDialog.dismiss();
                    }
                });
                nagDialog.show();
                return true;
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myQuittingDialogBox =new AlertDialog.Builder(getContext());
                //set message, title, and icon
                myQuittingDialogBox.setMessage("Are you sure?")
                        .setPositiveButton(/*"Delete"*/ "Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                boolean success = helper.deleteNote(notes.get(position).getID());
                                /*if (success) Toast.makeText(getContext(), "Delete!!", Toast.LENGTH_SHORT).show();*/
                                setNotes(helper.getNotes());
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(/*"Cancel"*/ "No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                myQuittingDialogBox.setCancelable(true);
                myQuittingDialogBox.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                }).create();
                myQuittingDialogBox.show();
            }
        });
        inter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = notes.get(position).getCurrent_color();
                int[] colors = MainActivity.colors;
                if (current + 1 >= colors.length){
                    current = 0;
                }else {
                    current++;
                }
                notes.get(position).setCurrent_color(current);
                card.setCardBackgroundColor(getContext().getResources().getColor(MainActivity.colors[
                notes.get(position).getCurrent_color()]));
                helper.updateNoteColor(notes.get(position).getID(), notes.get(position).getCurrent_color());
                notifyDataSetChanged();
            }
        });
        return custom_view;
    }

}
