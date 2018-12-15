package teja.dely.cameraapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.zxing.Result;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class StepTwo extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView zXingScannerView;
    LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationClient;
    SweetAlertDialog sweets;
    String nrp,pass,idAgenda;
    private BroadcastReceiver broadcastReceiver;
    Context context = this;


    double lat1, long1;
    double lat2, long2, jarak;
    double lat3,long3;



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String lokasi = (String) intent.getExtras().get("coordinates");
                    String lokasi2[] = lokasi.split("/");
                    lat3 = Double.parseDouble(lokasi2[0]);
                    long3 = Double.parseDouble(lokasi2[1]);

                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

        if (ActivityCompat.checkSelfPermission(StepTwo.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(StepTwo.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            lat2 = location.getLatitude();
                            long2 = location.getLongitude();
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled){
            Toast.makeText(context, "GPS Tidak Aktif, Aktifkan GPS", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
        Intent i =new Intent(getApplicationContext(),GPS_Service.class);
        startService(i);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestPermission();
        if (ActivityCompat.checkSelfPermission(StepTwo.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(StepTwo.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            lat2 = location.getLatitude();
                            long2 = location.getLongitude();
                        }
                    }
                });






        Intent in_step_two = getIntent();
        nrp = in_step_two.getStringExtra("nrp");
        pass = in_step_two.getStringExtra("pass");
//        setContentView(R.layout.activity_absen);
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(StepTwo.this);
        zXingScannerView.startCamera();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    public void handleResult(Result result) {
//        Toast.makeText(context,"2->"+String.valueOf(lat2), Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "3->"+String.valueOf(lat2), Toast.LENGTH_SHORT).show();

        String hasil = result.getText();
        String[] hasil2 = hasil.split(",");
        lat1 = Double.parseDouble(hasil2[0]);
        long1 = Double.parseDouble(hasil2[1]);
        idAgenda = hasil2[2];
//        Toast.makeText(this, "lat="+String.valueOf(lat2), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "long="+String.valueOf(long2), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, hasil2[0], Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, hasil2[1], Toast.LENGTH_SHORT).show();
//        getJarak();
        Toast.makeText(this, "Menghitung Jarak", Toast.LENGTH_SHORT).show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                float jaraknya = getJarak();
                onPause(jaraknya);
            }
        }, 3000);

//        zXingScannerView.removeAllViews();


    }



    public void onPause(float jarak) {
        super.onPause();
        zXingScannerView.stopCamera();

        if(jarak <=1000){
            Intent i = new Intent(getApplicationContext(),GPS_Service.class);
            stopService(i);

            Intent next_step = new Intent(StepTwo.this,StepThree.class);
            next_step.putExtra("idAgenda",idAgenda);
            next_step.putExtra("latitude",String.valueOf(lat2));
            next_step.putExtra("longitude",String.valueOf(long2));
            next_step.putExtra("nrp",nrp);
            next_step.putExtra("pass",pass);
            startActivity(next_step);
            finish();
        }else{
            Toast.makeText(this, "Jarak lebih dari 100 m", Toast.LENGTH_SHORT).show();
        }


    }

    private float getJarak() {
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(long1);

        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(long2);

        Location loc3 = new Location("");
        loc3.setLatitude(lat3);
        loc3.setLongitude(long3);

        float distanceInMeters = loc1.distanceTo(loc3);
        if(distanceInMeters >1000){
            distanceInMeters = loc1.distanceTo(loc2);
            if(distanceInMeters>1000){
                distanceInMeters = loc1.distanceTo(loc3);
            }
        }
        return distanceInMeters;

//        Toast.makeText(this, "Jarak = "+String.valueOf(distanceInMeters), Toast.LENGTH_SHORT).show();
//        if(distanceInMeters<10){
//            zXingScannerView.stopCamera();
//            sweets = new SweetAlertDialog(getApplicationContext(),SweetAlertDialog.SUCCESS_TYPE);
//            sweets.setTitleText("Jarak = "+String.valueOf(distanceInMeters));
//            sweets.show();
//        }else{
//            zXingScannerView.stopCamera();
//            sweets = new SweetAlertDialog(getApplicationContext(),SweetAlertDialog.ERROR_TYPE);
//            sweets.setTitleText("Jarak = "+String.valueOf(distanceInMeters));
//            sweets.show();
//        }
    }



}
