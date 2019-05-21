package com.example.finalprojectvignette.Fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.print.PrintHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectvignette.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class Fragment_ActivitiesGrid extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    ImageView image;
    Bitmap QRBit;
    Button print;

    String Qrcode;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Fragment_ActivitiesGrid() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Fragment_ActivitiesGrid newInstance(String param1) {
        Fragment_ActivitiesGrid fragment = new Fragment_ActivitiesGrid();
        Bundle args = new Bundle();
        args.putString("qr_code", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.qr_code_generator, container, false);
        image = view.findViewById(R.id.imageView);

        print = view.findViewById(R.id.print);
        print.setOnClickListener(this);


        if(getArguments()==null){
            //Toast.makeText(getContext(),"Vse ok",Toast.LENGTH_SHORT).show();
        }
        else {
            Qrcode = getArguments().getString("qr_code");
            QRBit = printQRCode(Qrcode);
            if (QRBit == null){
                Toast.makeText(getContext(), "Nursbrattt", Toast.LENGTH_SHORT).show();
            }else {
                image.setImageBitmap(QRBit);
            }
        }
        return view;
    }

    private void doPhotoPrint() {
        PrintHelper photoPrinter = new PrintHelper(getContext());
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.printBitmap("image.png_test_print", QRBit, new PrintHelper.OnPrintFinishCallback() {
            @Override
            public void onFinish() {
                Toast.makeText(getContext(), "Thank you!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Bitmap printQRCode(String textToQR){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(textToQR, BarcodeFormat.QR_CODE,300,300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.print:
                doPhotoPrint();
                break;
        }
    }
}
