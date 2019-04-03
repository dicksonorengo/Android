package com.example.googlemap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap map;
    ListView lv;
    ArrayList<Place> contents;
    PlaceAdapter adapter;;
    DataBaseHelper databaseHelper;
    RelativeLayout fragment;
    Animation fade_in,fade_out;
    private  static int k=10000;

    private static int WELCOME_TIMEOUT = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        contents = new ArrayList<>();
        databaseHelper = new DataBaseHelper(this);
        contents = databaseHelper.getNotes();
        adapter = new PlaceAdapter(MainActivity.this,contents);
        fragment = findViewById(R.id.relative);
        lv = findViewById(R.id.listView1);
        lv.setAdapter(adapter);

        fade_in = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this,R.anim.fade_out);



        longclick();

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        for(int i=0;i<contents.size();i++){
            LatLng mah = new LatLng(contents.get(i).getLatitude(),contents.get(i).getLongitude());
            map.addMarker(new MarkerOptions().position(mah).title(contents.get(i).getTitle()).snippet(contents.get(i).getDescription()));
            map.moveCamera(CameraUpdateFactory.newLatLng(mah));
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LatLng pos = new LatLng(contents.get(position).getLatitude(),contents.get(position).getLongitude());
                Log.d("???????!!!??!!?!",pos.latitude + "+" + pos.longitude);
                fragment.setVisibility(View.VISIBLE);
                lv.setVisibility(View.GONE);
                k--;
                //map.moveCamera(CameraUpdateFactory.newLatLng(pos));
                map.animateCamera(CameraUpdateFactory.newLatLng(pos));
            }
        });
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                final Dialog nagDialog = new Dialog(MainActivity.this);
                nagDialog.setCancelable(true);
                nagDialog.setContentView(R.layout.create_place);

                final EditText title = nagDialog.findViewById(R.id.title);
                final EditText decs = nagDialog.findViewById(R.id.description);

                nagDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        nagDialog.dismiss();
                    }
                });
                Button no = nagDialog.findViewById(R.id.no_button);
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nagDialog.dismiss();
                    }
                });
                Button yes = nagDialog.findViewById(R.id.yes_button);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = title.getText().toString().trim();
                        String ddd = decs.getText().toString().trim();
                        if (!text.equals("")) {
                            double latitude = latLng.latitude;
                            double longitude = latLng.longitude;
                            map.addMarker(new MarkerOptions().position(latLng).title(text).snippet(ddd)).toString();
                            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            Place new_place = new Place(text,ddd,latitude,longitude);
                            //Log.d("!!!!!",new_place.getTitle()+"+"+new_place.getDescription() + "+" + new_place.getLatitude() + "+" + new_place.getLongitude());
                            databaseHelper.insertNote(new_place);
                            contents = databaseHelper.getNotes();
                            adapter.setContents(contents);
                        }
                        nagDialog.dismiss();
                    }
                });
                nagDialog.show();
            }
        });

        /*map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!  " + addresses.toString());
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalcode = addresses.get(0).getPostalCode();
                    String knownname = addresses.get(0).getFeatureName();
                    Log.d("city",city);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/
    }
    public void radioclicked(View v){
        switch (v.getId()){
            case R.id.standard:
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.satellite:
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.hybrid:
                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_item,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                    if(k%2==0){
                        //fragment.startAnimation(fade_in);
                        lv.startAnimation(fade_in);
                        fragment.setVisibility(View.GONE);
                        lv.setVisibility(View.VISIBLE);
                        k--;
                    }
                    else {
                        fragment.startAnimation(fade_out);
                        fragment.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                        k--;
                    }
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fragment.setVisibility(View.GONE);
                        listview.setVisibility(View.VISIBLE);
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    }
                },WELCOME_TIMEOUT);*/
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        contents = databaseHelper.getNotes();
        adapter = new PlaceAdapter(this,contents);
        lv.setAdapter(adapter);
    }
    public  void longclick(){
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                fragment.setVisibility(View.GONE);
                lv.setVisibility(View.VISIBLE);
                android.app.AlertDialog.Builder builder =new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                boolean success = databaseHelper.deleteNote(contents.get(position).getId());
                                if (success) Toast.makeText(getApplicationContext(), "Delete!!", Toast.LENGTH_SHORT).show();
                                adapter.setContents(databaseHelper.getNotes());
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.setCancelable(true);
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                }).create();
                builder.show();
                return false;
            }
        });
    }
}
