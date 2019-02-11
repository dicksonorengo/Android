package com.example.phonebook;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Call extends Fragment {
    private static final int REQUEST_CALL = 1;
    private TextView mEditTextNumber;
    private String phoneNo;
    View v;
    public Call() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.call, container, false);
        mEditTextNumber = v.findViewById(R.id.edit_text_number);
        phoneNo = mEditTextNumber.getText().toString();

        Button btnAterisk = v.findViewById(R.id.btnAterisk);
        Button btnHash = v.findViewById(R.id.btnHash);
        Button btnZero = v.findViewById(R.id.btnZero);
        Button btnOne = v.findViewById(R.id.btnOne);
        Button btnTwo = v.findViewById(R.id.btnTwo);
        Button btnThree = v.findViewById(R.id.btnThree);
        Button btnFour = v.findViewById(R.id.btnFour);
        Button btnFive = v.findViewById(R.id.btnFive);
        Button btnSix = v.findViewById(R.id.btnSix);
        Button btnSeven = v.findViewById(R.id.btnSeven);
        Button btnEight = v.findViewById(R.id.btnEight);
        Button btnNine = v.findViewById(R.id.btnNine);
        Button btndel = v.findViewById(R.id.btndel);
        Button btnClearAll = v.findViewById(R.id.btnClearAll);
        Button dial = v.findViewById(R.id.ic_phone);

        btnAterisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNo += "*";
                mEditTextNumber.setText(phoneNo);

            }
        });
        btnHash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNo += "#";
                mEditTextNumber.setText(phoneNo);
            }
        });
        btnZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNo += "0";
                mEditTextNumber.setText(phoneNo);
            }
        });
        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNo += "1";
                mEditTextNumber.setText(phoneNo);
            }
        });
        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNo += "2";
                mEditTextNumber.setText(phoneNo);
            }
        });
        btnThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNo += "3";
                mEditTextNumber.setText(phoneNo);
            }
        });
        btnFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNo += "4";
                mEditTextNumber.setText(phoneNo);
            }
        });
        btnFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNo += "4";
                mEditTextNumber.setText(phoneNo);
            }
        });
        btnFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNo += "5";
                mEditTextNumber.setText(phoneNo);
            }
        });
        btnSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNo += "6";
                mEditTextNumber.setText(phoneNo);
            }
        });
        btnSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNo += "7";
                mEditTextNumber.setText(phoneNo);
            }
        });
        btnEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNo += "8";
                mEditTextNumber.setText(phoneNo);
            }
        });
        btnNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNo += "9";
                mEditTextNumber.setText(phoneNo);
            }
        });
        btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNo != null && phoneNo.length() > 0) {
                    phoneNo = phoneNo.substring(0, phoneNo.length() - 1); }
                mEditTextNumber.setText(phoneNo);
            }
        });
        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditTextNumber.setText("");
            }
        });
        dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall(mEditTextNumber.getText().toString());
            }
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    public void makePhoneCall(String mEditTextNumber) {
        String number = mEditTextNumber;
        if (number.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            }
            else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(mEditTextNumber.getText().toString());
            } else {
                Toast.makeText(getContext(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }





}
