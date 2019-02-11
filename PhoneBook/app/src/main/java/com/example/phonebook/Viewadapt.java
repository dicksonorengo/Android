package com.example.phonebook;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Viewadapt extends RecyclerView.Adapter<Viewadapt.MyviewHolder> {
    Context context;
    List<Cons_Contact> data;
    Dialog dialog;
    Call call;
    public Viewadapt(Context context, List<Cons_Contact> contact) {
        this.context = context;
        this.data = contact;
    }
    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,viewGroup,false);
        final MyviewHolder myviewHolder = new MyviewHolder(v);
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.fixed);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myviewHolder.contact_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView fixed_name = dialog.findViewById(R.id.fixed_name);
                final TextView fixed_phone = dialog.findViewById(R.id.fixed_number);
                ImageView fixed_image = dialog.findViewById(R.id.fixed_image);
                final Button fixed_call = dialog.findViewById(R.id.fixed_call);
                Button fixed_message = dialog.findViewById(R.id.fixed_message);
                fixed_name.setText(data.get(myviewHolder.getAdapterPosition()).getName());
                fixed_phone.setText(data.get(myviewHolder.getAdapterPosition()).getPhone());
                fixed_image.setImageResource(data.get(myviewHolder.getAdapterPosition()).getPhoto());

                final String phone  = fixed_phone.getText().toString();
                System.out.println(phone + "&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                fixed_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Log.d("SIDED","$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                        call.makePhoneCall(phone);
                    }
                });
                Toast.makeText(context,String.valueOf(myviewHolder.getAdapterPosition()),Toast.LENGTH_SHORT).show();
                dialog.show();
            }
        });
        myviewHolder.contact_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                System.out.println(myviewHolder.getAdapterPosition() + "&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                removeAt(myviewHolder.getAdapterPosition());
                return false;
            }
        });
        return myviewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull MyviewHolder viewHolder, int i) {
        viewHolder.t_phone.setText(data.get(i).getPhone());
        viewHolder.t_name.setText(data.get(i).getName());
        viewHolder.image.setImageResource(data.get(i).getPhoto());
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public static class MyviewHolder extends RecyclerView.ViewHolder{
        private TextView t_name;
        private TextView t_phone;
        private ImageView image;
        private LinearLayout contact_item;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            contact_item = (LinearLayout)itemView.findViewById(R.id.item_contact);
            t_name =(TextView)itemView.findViewById(R.id.name_contact);
            t_phone=itemView.findViewById(R.id.phone_contact);
            image = itemView.findViewById(R.id.image);
        }
    }
    public void removeAt(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
    }
}
