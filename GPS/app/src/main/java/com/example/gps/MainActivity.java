package com.example.gps;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener{
    private static final int REQUEST_LOCATION = 1;
    Button button;
    TextView textView;
    private LocationListener locationListener;
    private LocationManager locationManager;
//    BroadcastReceiver broadcastReceiver;
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if(broadcastReceiver == null){
//            broadcastReceiver = new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context context, Intent intent) {
////                    textView.append((String) intent.getExtras().get("data"));
//                    textView.append("HEHEHEH");
//                }
//            };
//        }
//        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
//    }
//
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(broadcastReceiver != null)
//            unregisterReceiver(broadcastReceiver);
//
//        super.onStop();
//        Intent i = new Intent(getApplicationContext(), GPS_Service.class);
//        stopService(i);

        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(locationListener);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent i = new Intent(getApplicationContext(), GPS_Service.class);
//        startService(i);

        runtime_permissions();

        textView = (TextView) findViewById(R.id.textview);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                textView.setText("permissions granted!");
            }else {
                runtime_permissions();
            }
        }
    }


    public void send(View v){
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        List<String> list = null;
//
//        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
//            textView.setText("enable error");
//        else if(locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
//            list = getLocation();
//
//            StringBuilder sb = new StringBuilder();
//            sb.append(list.get(0));
//            sb.append(",");
//            sb.append(list.get(1));
//            MessageSender ms = new MessageSender();
//            ms.execute(sb.toString());
//        }
    }

//    private List<String> getLocation(){
//        List list = new ArrayList<String>();
//        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
//        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
//        != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//            textView.setText("requesting permissions");
//        }else {
//            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//            if(location != null){
//                double lat = location.getLatitude();
//                double lon = location.getLongitude();
//
//                lattitude = String.valueOf(lat);
//                longitude = String.valueOf(lon);
//
//                list.add(lattitude);
//                list.add(longitude);
//
//                textView.setText(String.format("Lokalizacja arasa: \n %s - %s", lattitude, longitude));
//            }else
//                Toast.makeText(this, "unable to reace your location", Toast.LENGTH_SHORT).show();
//        }
//        return list;
//    }

//    protected void buildAllertMessageNoGps(){
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("plese turn on your gps connection")
//                .setCancelable(false)
//                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//        final AlertDialog alert = builder.create();
//        alert.show();
//    }

    @Override
    public void onLocationChanged(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Date d = new Date(location.getTime());

        textView.setText( "time: " + d.toString() + "\nLatitude: " +
                latitude + "\n Longitude: " + longitude + "\n speed: " + location.getSpeed() +
                "\n height: ");

        MessageSender ms = new MessageSender();
        ms.execute(latitude + "," + longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }
}
