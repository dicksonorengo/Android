package com.example.finalprojectvignette.Student_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.finalprojectvignette.R;
import com.example.finalprojectvignette.Scan.ScanActivity;


public class Search_Qr_Fragment extends Fragment implements OnClickListener{
    Button qr_code;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.actiivity_new, container, false);
        qr_code = view.findViewById(R.id.btn_scan);
        qr_code.setOnClickListener(this);

        Toolbar toolbar;
        toolbar =  getActivity().findViewById(R.id.parent_toolbar);
        toolbar.setTitle("Scan Qr Code");
        return  view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan:
                startActivity(new Intent(getActivity(), ScanActivity.class));
                break;
        }
    }
}