package com.example.phonebook;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class Contact extends Fragment {
    View v;
    private TabLayout tab;
    private RecyclerView myview;
    private List<Cons_Contact> contact;

    public Contact() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.contact, parent, false);
        myview = (RecyclerView) v.findViewById(R.id.contact_recycler);
        Viewadapt viewAdapt = new Viewadapt(getContext(),contact);
        myview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myview.setAdapter(viewAdapt);
        return v;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contact = new ArrayList<>();
        contact.add(new Cons_Contact("Bill Gates","77087777777",R.drawable.bill));
        contact.add(new Cons_Contact("Teo Leo","77477777777",R.drawable.teo));
        contact.add(new Cons_Contact("Tommy Grand","77787777777",R.drawable.tommy));
        contact.add(new Cons_Contact("Sandy Edwards","77017777777",R.drawable.sandy));
        contact.add(new Cons_Contact("Mark Zuckerberg","77027777777",R.drawable.mark));
        contact.add(new Cons_Contact("Stive Jobs","77027777777",R.drawable.stivv));
        contact.add(new Cons_Contact("Elon Mask","77027777777",R.drawable.elon));
    }

}
