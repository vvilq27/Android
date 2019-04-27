package com.example.gps;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    Button button;
    TextView textView;
    LocationManager locationManager;
    String lattitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ActivityCompat.requestPermissions(this, new String[](and));

        textView = (TextView) findViewById(R.id.textview);
        textView.setText("finito");
//        button = (Button)findViewById(R.id.button);

    }

    public void send(View v){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> list = null;

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            textView.setText("enable error");
        else if(locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
            list = getLocation();

            StringBuilder sb = new StringBuilder();
            sb.append(list.get(0));
            sb.append(",");
            sb.append(list.get(1));
            MessageSender ms = new MessageSender();
            ms.execute(sb.toString());
        }
    }

    private List<String> getLocation(){
        List list = new ArrayList<String>();
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            textView.setText("requesting permissions");
        }else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(location != null){
                double lat = location.getLatitude();
                double lon = location.getLongitude();

                lattitude = String.valueOf(lat);
                longitude = String.valueOf(lon);

                list.add(lattitude);
                list.add(longitude);

                textView.setText(String.format("Lokalizacja arasa: \n %s - %s", lattitude, longitude));
            }else
                Toast.makeText(this, "unable to reace your location", Toast.LENGTH_SHORT).show();
        }
        return list;
    }

    protected void buildAllertMessageNoGps(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("plese turn on your gps connection")
                .setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
