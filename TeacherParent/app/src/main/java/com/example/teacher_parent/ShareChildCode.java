package com.example.teacher_parent;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class ShareChildCode extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String ChildName;
    private String ChildId;

    private TextView Heading, ChildCode;
    private ImageButton ShareCodeButton;
    private ProgressDialog myProgress;

    public ShareChildCode() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static ShareChildCode newInstance(String param1, String param2) {
        ShareChildCode fragment = new ShareChildCode();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ChildId = getArguments().getString(ARG_PARAM1);
            ChildName = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_share_child_code, container, false);
        
        Heading =  view.findViewById(R.id.ShareCode_intro);
        String title = "The 'Child Code' for '" + ChildName + "' is given below.";
        Heading.setText(title);

        myProgress = new ProgressDialog(getContext());
        myProgress.setTitle("Sending");

        ChildCode = view.findViewById(R.id.ShareCode_childCode);
        ChildCode.setText(ChildId);

        ShareCodeButton =  view.findViewById(R.id.ShareCode_sendButton);
        ShareCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myMessage = "Hello there, \nHere is the ChildCode for the student '" + ChildName + "'. \n" +
                        "ChildCode = '" + ChildId + " ' ";
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, myMessage);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share via.."));

                /*Intent intentsms = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" + "" ) );
                intentsms.putExtra( "sms_body", myMessage );
                startActivity( intentsms );*/
                /*myProgress.setMessage("Sending code!");
                myProgress.show();*/
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }

}
