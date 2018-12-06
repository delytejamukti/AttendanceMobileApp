package teja.dely.cameraapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.zxing.Result;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class StepTwo extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView zXingScannerView;
    LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationClient;
    SweetAlertDialog sweets;
    String nrp,pass,idAgenda;


    double lat1, long1;
    double lat2, long2, jarak;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Intent in_absen = getIntent();
        nrp = in_absen.getStringExtra("nrp");
        pass = in_absen.getStringExtra("pass");
//        setContentView(R.layout.activity_absen);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
//                            Toast.makeText(StepTwo.this, "lat"+location.getLatitude(), Toast.LENGTH_SHORT).show();
                                lat2 = location.getLatitude();
                                long2 = location.getLongitude();
                        }
                    }
                });
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(StepTwo.this);
        zXingScannerView.startCamera();
    }

    @Override
    public void handleResult(Result result) {
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
        float jaraknya = getJarak();
        onPause(jaraknya);

//        zXingScannerView.removeAllViews();


    }
    @Override
    public void onResume() {
        super.onResume();
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }


    public void onPause(float jarak) {
        super.onPause();
        zXingScannerView.stopCamera();
        if(jarak <=100){
            Intent next_step = new Intent(StepTwo.this,StepThree.class);
            next_step.putExtra("idAgenda",idAgenda);
            next_step.putExtra("latitude",String.valueOf(lat2));
            next_step.putExtra("longitude",String.valueOf(long2));
            next_step.putExtra("nrp",nrp);
            next_step.putExtra("pass",pass);
            startActivity(next_step);
        }else{
//            sweets = new SweetAlertDialog(getApplicationContext(),SweetAlertDialog.ERROR_TYPE);
//            sweets.setTitleText("Jarak lebih dari 10 m");
//            sweets.show();
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

        float distanceInMeters = loc1.distanceTo(loc2);
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
