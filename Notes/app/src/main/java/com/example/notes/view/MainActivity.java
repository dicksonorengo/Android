package com.example.notes.view;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.notes.R;
import com.example.notes.database.DatabaseHelper;
import com.example.notes.database.model.Note;

import com.example.notes.utils.RecyclerTouchListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private NotesAdapter mAdapter;
    private List<Note> notesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView noNotesView;
    private EditText inputNote;
    private NotesAdapter.MyViewHolder viewHolder;
    private int btn=0;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Paper.init(this);
        recyclerView = findViewById(R.id.recycler_view);
        noNotesView = findViewById(R.id.empty_notes_view);

        db = new DatabaseHelper(this);

        notesList.addAll(db.getAllNotes());

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteDialog(false, null, -1);
            }
        });

        mAdapter = new NotesAdapter(this, notesList);

        View view = LayoutInflater.from(this).inflate(R.layout.note_list, null,false);
        viewHolder = new NotesAdapter.MyViewHolder(view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        empty();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                showNoteDialog(true, notesList.get(position), position);
                Paper.book().write("title",inputNote.getText().toString());
            }
            @Override
            public void onLongClick(View view, int position) {
                deleteNote(position);
            }
        }));
    }
    private void createNote(String note) {
        long id = db.insertNote(note);
        Note n = db.getNote(id);
        if (n != null) {
            notesList.add(0, n);
            mAdapter.notifyDataSetChanged();
            empty();
        }
    }
    private void updateNote(String note, int position) {
        Note n = notesList.get(position);
        n.setNote(note);
        db.updateNote(n);
        notesList.set(position, n);
        mAdapter.notifyItemChanged(position);
        empty();
    }
    private void deleteNote(int position) {
        db.deleteNote(notesList.get(position));
        notesList.remove(position);
        mAdapter.notifyItemRemoved(position);
        empty();
    }
    /*private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, notesList.get(position), position);
                } else {
                    deleteNote(position);
                }
            }
        });
        builder.show();
    }*/
    private void showNoteDialog(final boolean shouldUpdate, final Note note, final int position) {
        //getposition(position);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        inputNote = view.findViewById(R.id.note);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));
        if (shouldUpdate && note != null) {
            inputNote.setText(note.getNote());
        }
        alertDialogBuilderUserInput.setCancelable(false).setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) { }}).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(inputNote.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter note!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }
                if (shouldUpdate && note != null) {
                    updateNote(inputNote.getText().toString(), position);
                    Paper.book().write("title",inputNote.getText().toString());
                    //Paper.book().write("time",viewHolder.getTimestamp().toString());
                    Toast.makeText(MainActivity.this,"Save!!!",Toast.LENGTH_SHORT).show();
                } else {
                    createNote(inputNote.getText().toString());
                    Paper.book().write("title",inputNote.getText().toString());
                    //Paper.book().write("time",viewHolder.getTimestamp().toString());
                    Toast.makeText(MainActivity.this,"Save!!!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void empty() {
        if (db.getNotesCount() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }
    }




}
