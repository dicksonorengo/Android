package com.example.teacher_parent;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyChildFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<HashMap<String,String>> act_list = new ArrayList<>();
    private String childName="";
    MyChildFeedAdapter(String name){
        childName = name;
    }

    public void setActList(List<HashMap<String,String>> list){act_list = list;}

    public List<HashMap<String,String>> getActList(){return act_list;}
    public void addAct(HashMap<String,String> act){
        act_list.add(0,act);
        notifyDataSetChanged();
        notifyItemInserted(0);
        notifyItemRangeInserted(0,act_list.size());
    }

    @Override
    public int getItemViewType(int position) {
        switch (act_list.get(position).get("type"))
        {
            case "attendance": return 1;
            case "HomeWork": return 2;
            case "Academic_Performance":return 3;
            case "Schedule": return 4;
        }
        return 0;
    }
    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch(viewType) {
            case 0:
                break;
            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_card_attendance,parent,false);
                return new AttendanceHolder(v);
            case 2:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_card_custom_activity,parent,false);
                return new CustomActHolder(v);
            case 3:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_card_academic_performance,parent,false);
                return new AcademicHolder(v);
            case 4:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_card_nap,parent,false);
                return new NapActHolder(v);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HashMap<String,String> act = act_list.get(position);
        switch (holder.getItemViewType()){
            case 1: // Attendance item
                AttendanceHolder atd_holder = (AttendanceHolder) holder;
                if(act.get("name").equalsIgnoreCase("absent")){
                    atd_holder.atd_img.setImageResource(R.drawable.x);
                } else {
                    atd_holder.atd_img.setImageResource(R.drawable.check);
                }
                atd_holder.atd_type.setText(act.get("name"));
                atd_holder.atd_time.setText(act.get("time"));
                atd_holder.atd_class.setText("Class: "+act.get("class"));
                break;
            case 2: //Custom activity item
                CustomActHolder act_holder = (CustomActHolder) holder;
                act_holder.act_name.setText(act.get("type"));
                act_holder.name.setText(act.get("name"));
                act_holder.act_time_class.setText("Time: "+act.get("time")+"\nClass: "+act.get("class"));
                act_holder.act_kids.setText("Tagged kids: "+act.get("childnames"));
                act_holder.act_details.setText("Details: "+act.get("details"));
                break;
            case 3: //Academic_Performance
                AcademicHolder academicHolder = (AcademicHolder) holder;
                academicHolder.academic_act_name.setText(act.get("type"));
                academicHolder.academic_act_time_and_class.setText("Time: "+act.get("time")+"\nClass: "+act.get("class"));
                academicHolder.academic_smile.setText("Tagged kids: "+act.get("childnames") + "\nSmilerating: " + act.get("smilerating"));
                break;
            case 4: //Nap
                NapActHolder nap = (NapActHolder)holder;
                nap.className.setText("Class: "+act.get("class"));
                nap.times.setText("Time: "+act.get("start_time")+" - "+act.get("end_time"));
                nap.details.setText("Details: "+act.get("details"));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return act_list.size();
    }

    static class AttendanceHolder extends RecyclerView.ViewHolder{
        private ImageView atd_img;
        private TextView atd_type,atd_time,atd_class;
        public AttendanceHolder(View v){
            super(v);
            atd_img = v.findViewById(R.id.attendance_img);
            atd_type = v.findViewById(R.id.attendance_act_type);
            atd_time = v.findViewById(R.id.attendance_time);
            atd_class = v.findViewById(R.id.attendance_class);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Attendance item clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    static class CustomActHolder extends RecyclerView.ViewHolder{
        private TextView act_name,act_time_class,act_kids,act_details,name;
        boolean toggle = true;
        public CustomActHolder(View v){
            super(v);
            name = v.findViewById(R.id.name);
            act_name = v.findViewById(R.id.custom_act_name);
            act_time_class = v.findViewById(R.id.custom_act_time_and_class);
            act_details = v.findViewById(R.id.custom_act_details);
            act_kids = v.findViewById(R.id.custom_act_kids);
            act_kids.setSingleLine(toggle);
            act_details.setSingleLine(toggle);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggle = !toggle;
                    act_kids.setSingleLine(toggle);
                    act_details.setSingleLine(toggle);
                }
            });
        }
    }
    static class NapActHolder extends RecyclerView.ViewHolder{
        private TextView className,times,details;
        public NapActHolder(View v){
            super(v);
            className = v.findViewById(R.id.nap_class);
            times = v.findViewById(R.id.nap_times);
            details =v.findViewById(R.id.nap_details);
        }
    }
    static class AcademicHolder extends RecyclerView.ViewHolder{
        private TextView academic_act_name,academic_act_time_and_class,academic_smile;
        public AcademicHolder(@NonNull View itemView) {
            super(itemView);
            academic_act_name = itemView.findViewById(R.id.custom_act_name);
            academic_act_time_and_class = itemView.findViewById(R.id.custom_act_time_and_class);
            academic_smile = itemView.findViewById(R.id.smile);
        }
    }
}
