package com.example.finalprojectvignette.Adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.finalprojectvignette.Fragment.ClassFragment;
import com.example.finalprojectvignette.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    String currentClass = "";
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
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list, parent, false);
        return new ViewHolder(v,mItemClickListner);
    }

    private Context context;
    public MyRecyclerViewAdapter(Context context) {
        this.context = context;

    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final HashMap<String,String> child = children.get(position);
        holder.holder_child_name_textview.setText(child.get("name"));
        holder.image.setImageResource(R.drawable.blank_profile_pic);


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("children").child(child.get("id")).exists()) {
                    if(dataSnapshot.child("children").child(child.get("id")).hasChild("parent_num")){
                        String tel = dataSnapshot.child("children").child(child.get("id")).child("parent_num").getValue().toString();
                        holder.tvMobileNumber.setText(tel);
                    }
                    if(dataSnapshot.child("children").child(child.get("id")).hasChild("dob")){
                        String birth = dataSnapshot.child("children").child(child.get("id")).child("dob").getValue().toString();
                        holder.date_birth.setText(birth);
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


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


        holder.deletestudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("??????????<><><><>><>>>><<><><>><><><<>><><<>><><?");
                currentClass = ClassFragment.classid;
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("children").child(child.get("id")).exists()) {
                            String currChildren = dataSnapshot.child("classes").child(currentClass).child("children").getValue().toString();
                            String newchildren = "";
                            if (currChildren.equals("0"))
                                System.out.println("Empty");
                            else {
                                String[] class_children_arr = currChildren.split(",");
                                List<String> new_child_list = new ArrayList<>();
                                for(final String childInClass : class_children_arr){
                                    if(!childInClass.equals(child.get("id"))){
                                        new_child_list.add(childInClass + ",");
                                    }
                                }

                                for(String newchild : new_child_list){
                                    newchildren += newchild;
                                    databaseReference.child("classes").child(currentClass).child("children").setValue(newchildren);
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
            }
        });
        holder.ivcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("children").child(child.get("id")).exists()) {
                            if(dataSnapshot.child("children").child(child.get("id")).hasChild("parent_num")){
                                String tel = dataSnapshot.child("children").child(child.get("id")).child("parent_num").getValue().toString();
                                Intent sendIntent = new Intent(Intent.ACTION_DIAL);
                                sendIntent.setData(Uri.parse("tel:" + tel));
                                context.startActivity(sendIntent);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });



            }
        });



        holder.ivmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myMessage = "Hello";
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(sendIntent, "Share via.."));

                /*Intent intentsms = new Intent( Intent.ACTION_SEND, Uri.parse( "sms:" + "" ) );
                intentsms.putExtra( "sms_body", myMessage );
                context.startActivity(intentsms);*/
            }
        });

        /*holder.send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag = ShareChildCode.newInstance(child.get("id"),child.get("name"));
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.teacher_content_holder, frag)
                        .commit();
            }
        });*/


        holder.send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView Heading, ChildCode;
                ImageButton ShareCodeButton;
                ProgressDialog myProgress;

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.share_student_code);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                Heading =  dialog.findViewById(R.id.ShareCode_intro);
                String title = "The 'Child Code' for '" + child.get("name") + "' is given below.";
                Heading.setText(title);

                myProgress = new ProgressDialog(context);
                myProgress.setTitle("Sending");

                ChildCode = dialog.findViewById(R.id.ShareCode_childCode);
                ChildCode.setText(child.get("id"));

                ShareCodeButton =  dialog.findViewById(R.id.ShareCode_sendButton);
                ShareCodeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String myMessage = "Hello there, \nHere is the student code for the student '" + child.get("name") + "'. \n" +
                                "StudentCode = '" + child.get("id") + " ' ";
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, myMessage);
                        sendIntent.setType("text/plain");
                        context.startActivity(Intent.createChooser(sendIntent, "Share via.."));

                /*Intent intentsms = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" + "" ) );
                intentsms.putExtra( "sms_body", myMessage );
                startActivity( intentsms );*/
                /*myProgress.setMessage("Sending code!");
                myProgress.show();*/
                        //getActivity().getSupportFragmentManager().popBackStack();
                    }
                });
                dialog.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return children.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView holder_child_name_textview,tvMobileNumber,date_birth;
        private ImageView image;
        private RecyclerItemClickListener mItemClickListner;
        private ImageView deletestudent;
        private ImageView send_code,ivmail,ivcall;


        public ViewHolder(final View itemView, RecyclerItemClickListener listener) {
            super(itemView);
            holder_child_name_textview = itemView.findViewById(R.id.child_list_name);
            image = itemView.findViewById(R.id.child_list_pic);
            deletestudent = itemView.findViewById(R.id.ivDelete);
            send_code = itemView.findViewById(R.id.ivsend);
            ivmail = itemView.findViewById(R.id.ivMail);
            ivcall = itemView.findViewById(R.id.ivCall);

            tvMobileNumber = itemView.findViewById(R.id.tvMobileNumber);
            date_birth = itemView.findViewById(R.id.date_birth);

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
