package com.example.notes3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static int[] colors = {R.color.notesBack1,
            R.color.notesBack2,
            R.color.notesBack3,
            R.color.notesBack4};
    private NoteAdapter adapter;
    private ArrayList<Note> notes;
    ListView list_notes;
    private DatabaseHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list_notes = findViewById(R.id.notes_list);
        notes = new ArrayList<>();
        helper = new DatabaseHelper(this);
        notes = helper.getNotes();
        adapter = new NoteAdapter(this, notes);
        list_notes.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_button:
                /*Toast.makeText(getApplication(), "asd", Toast.LENGTH_SHORT).show();*/
                final Dialog nagDialog = new Dialog(this);
                nagDialog.setCancelable(true);
                nagDialog.setContentView(R.layout.note_edit);
                final EditText note_text = nagDialog.findViewById(R.id.edit_note_content);
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
                        String _text = note_text.getText().toString().trim();
                        if (!_text.equals("")) {
                            Note new_note = new Note(_text);
                            helper.insertNote(new_note);
                            notes = helper.getNotes();
                            adapter.setNotes(notes);
                        }
                        nagDialog.dismiss();
                    }
                });
                nagDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        list_notes.setAdapter(adapter);
    }
}
