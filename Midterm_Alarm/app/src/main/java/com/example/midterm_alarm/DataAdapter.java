package com.example.midterm_alarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DataAdapter extends ArrayAdapter<Data> {
    ArrayList<Data> contents;
    private DataBaseHelper helper;
    public DataAdapter(Context context, ArrayList<Data>contents) {
        super(context,R.layout.list_item);
        this.contents = contents;

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View custom_view = layoutInflater.inflate(R.layout.list_item,parent,false);
        TextView time = custom_view.findViewById(R.id.textView);
        TextView description = custom_view.findViewById(R.id.textView2);
        time.setText(contents.get(position).getTime());
        final ImageView del = custom_view.findViewById(R.id.delete_image);
        description.setText(contents.get(position).getDescription());

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myQuittingDialogBox =new AlertDialog.Builder(getContext());
                myQuittingDialogBox.setMessage("Are you sure?")
                        .setPositiveButton(/*"Delete"*/ "Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //boolean success = helper.deleteContent(contents.get(position).getID());
                                //setNotes(helper.getContents());
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
        return custom_view;
    }

    @Override
    public int getCount() {
        return contents.size();
    }


}