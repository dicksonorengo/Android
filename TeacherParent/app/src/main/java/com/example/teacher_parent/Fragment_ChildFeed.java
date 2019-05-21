package com.example.teacher_parent;


import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;


public class Fragment_ChildFeed extends Fragment {
    RecyclerView rv;
    MyChildFeedAdapter adapter;
    String childid = "",childName ="", childPic = "";
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    DatabaseReference activitiesTb = db.child("activities");
    DatabaseReference childNode;
    Toolbar toolbar;
    ImageView propic;
    Boolean firstFetch = true;

    public static Fragment_ChildFeed newInstance(String id,String name, String picName) {
        Bundle args = new Bundle();
        args.putString("id",id);
        args.putString("name",name);
        args.putString("picName", picName);
        Fragment_ChildFeed fragment = new Fragment_ChildFeed();
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_ChildFeed() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        toolbar.getMenu().clear();
        if (getActivity().getClass() == ParentMain.class) {
            inflater.inflate(R.menu.child_feed_menu_parent, menu);
        } else {
            inflater.inflate(R.menu.child_feed_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int container = (getActivity().getClass()== Activity_TeacherMain.class) ? R.id.teacher_content_holder : R.id.parent_content_holder;
        switch (item.getItemId()) {
            case R.id.btn_child_profile_edit:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(container,Fragment_ChildProfile.newInstance(childid,childName))
                        .addToBackStack(null).commit();
                break;

            case R.id.btn_share_child_code:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(container, ShareChildCode.newInstance(childid,childName))
                        .addToBackStack(null).commit();
                break;
        }
        return true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_child_feed, container, false);
        firstFetch = true;
        childid = getArguments().getString("id");
        childName = getArguments().getString("name");
        childPic = getArguments().getString("picName");
        int toolbar_id = (getActivity().getClass()== Activity_TeacherMain.class) ? R.id.teacher_toolbar : R.id.parent_toolbar;
        toolbar = getActivity().findViewById(toolbar_id);
        toolbar.setTitle("Activity Feed");

        Toolbar coll_tool =  v.findViewById(R.id.toolbar_collapse);
        coll_tool.setTitleTextColor(getResources().getColor(R.color.white));
        coll_tool.setTitle(childName);
        CollapsingToolbarLayout myCollaps =  v.findViewById(R.id.collapsing_toolbar);
        myCollaps.setExpandedTitleTextAppearance(R.style.MyToolbarTheme);
        myCollaps.setCollapsedTitleTextAppearance(R.style.MyToolbarTheme);

        propic =  v.findViewById(R.id.expandedImage);
        propic.setImageResource(R.drawable.blank_profile_pic);



        childNode = db.child("children").child(childid);
        adapter = new MyChildFeedAdapter(childName);
        rv =  v.findViewById(R.id.child_feed_rv);
        //rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rv.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        rv.addItemDecoration(dividerItemDecoration);
        rv.setItemAnimator(new LandingAnimator());
        rv.getItemAnimator().setAddDuration(1000);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        scaleAdapter.setDuration(600);
        scaleAdapter.setFirstOnly(false);
        rv.setNestedScrollingEnabled(false);
        rv.setAdapter(scaleAdapter);
        childNode.child("pic").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String picname = dataSnapshot.getValue().toString();
                if(!picname.isEmpty()||picname.equalsIgnoreCase("0")) {
                    Glide.with(getContext()).load(picname).into(propic);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        childNode.child("act_ids").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String ids_str = dataSnapshot.getValue().toString();
                if(!ids_str.equalsIgnoreCase("0"))
                {
                    if(firstFetch){
                        rv.getRecycledViewPool().clear();
                        setActivitiesList(ids_str);
                        firstFetch = false;
                    } else {
                        List<String> ids_list = Arrays.asList(dataSnapshot.getValue().toString().split(","));
                        setActivitiesList(ids_list.get(ids_list.size()-1));
                        firstFetch = false;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        return v;
    }

    private void setActivitiesList(final String act_ids){
        final List<HashMap<String,String>> acts = new ArrayList<>();
        List<String> ids_list = Arrays.asList(act_ids.split(","));
        for(final String id : ids_list){
            activitiesTb.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.hasChild("type"))
                        return;
                    switch (dataSnapshot.child("type").getValue().toString()){
                        case "attendance":
                            adapter.addAct(getAttendanceAct(dataSnapshot));
                            break;
                        case "HomeWork":
                            adapter.addAct(getCustomAct(dataSnapshot));
                            break;
                        case "Academic_Performance":
                            adapter.addAct(getAcademicAct(dataSnapshot));
                            break;
                        case "Schedule":
                            adapter.addAct(getNapAct(dataSnapshot));
                            break;
                        default: break;
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }
    private HashMap<String,String> getAttendanceAct(DataSnapshot act_node){
        HashMap<String,String> act = new HashMap<>();
        act.put("type",act_node.child("type").getValue().toString());
        act.put("name",act_node.child("name").getValue().toString());
        act.put("class",act_node.child("class").getValue().toString());
        act.put("time",act_node.child("time").getValue().toString());
        return act;
    }
    private HashMap<String,String> getCustomAct(DataSnapshot act_node){
        HashMap<String,String> act = new HashMap<>();
        act.put("type",act_node.child("type").getValue().toString());
        act.put("name",act_node.child("name").getValue().toString());
        act.put("class",act_node.child("class").getValue().toString());
        act.put("time",act_node.child("time").getValue().toString());
        act.put("details",act_node.child("details").getValue().toString());
        act.put("childnames", act_node.child("childnames").getValue().toString());
        return act;
    }
    private HashMap<String,String> getNapAct(DataSnapshot act_node){
        HashMap<String,String> act = new HashMap<>();
        act.put("type",act_node.child("type").getValue().toString());
        act.put("class",act_node.child("class").getValue().toString());
        act.put("start_time",act_node.child("start_time").getValue().toString());
        act.put("end_time",act_node.child("end_time").getValue().toString());
        act.put("details",act_node.child("nap_details").getValue().toString());
        act.put("childnames", act_node.child("childnames").getValue().toString());
        return act;
    }
    private HashMap<String,String> getAcademicAct(DataSnapshot act_node){
        HashMap<String,String> act = new HashMap<>();
        act.put("type",act_node.child("type").getValue().toString());
        act.put("class",act_node.child("class").getValue().toString());
        act.put("time",act_node.child("time").getValue().toString());
        act.put("smilerating",act_node.child("smilerating").getValue().toString());
        act.put("childnames", act_node.child("childnames").getValue().toString());
        return act;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
