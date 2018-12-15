package teja.dely.cameraapp;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;



public class Dashboard extends AppCompatActivity {
    LinearLayout photo_,training_,predict_,signin_,layout_upload_ttd,layout_train_ttd,predict_ttd,logout;
    String nrp,pass;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

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
        Intent in_dashboard = getIntent();
        nrp = in_dashboard.getStringExtra("nrp");
        pass = in_dashboard.getStringExtra("pass");

//        Toast.makeText(this, nrp+" "+pass, Toast.LENGTH_SHORT).show();



        photo_ = (LinearLayout)findViewById(R.id.take_photo);
        photo_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ambil_foto = new Intent(Dashboard.this,Camera.class);
                ambil_foto.putExtra("nrp",nrp);
                ambil_foto.putExtra("pass",pass);
                startActivity(ambil_foto);

            }
        });
        training_ = (LinearLayout)findViewById(R.id.training);
        training_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_training = new Intent(Dashboard.this,Training.class);
                go_training.putExtra("nrp",nrp);
                go_training.putExtra("pass",pass);
                startActivity(go_training);
            }
        });
        predict_ = (LinearLayout)findViewById(R.id.predict);
        predict_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_predict = new Intent(Dashboard.this,Predict.class);
                go_predict.putExtra("nrp",nrp);
                go_predict.putExtra("pass",pass);
                startActivity(go_predict);

            }
        });
        signin_ = (LinearLayout)findViewById(R.id.signin);
        signin_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_absen = new Intent(Dashboard.this,StepTwo.class);
                go_absen.putExtra("nrp",nrp);
                go_absen.putExtra("pass",pass);
                startActivity(go_absen);

            }
        });
        layout_upload_ttd = (LinearLayout)findViewById(R.id.layout_upload_ttd);
        layout_upload_ttd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_upload_ttd = new Intent(Dashboard.this,UploadTtd.class);
                go_upload_ttd.putExtra("nrp",nrp);
                go_upload_ttd.putExtra("pass",pass);
                startActivity(go_upload_ttd);
            }
        });
        layout_train_ttd = (LinearLayout)findViewById(R.id.layout_train_ttd);
        layout_train_ttd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_train_ttd = new Intent(Dashboard.this,TrainTtd.class);
                go_train_ttd.putExtra("nrp",nrp);
                go_train_ttd.putExtra("pass",pass);
                startActivity(go_train_ttd);
            }
        });
        predict_ttd = (LinearLayout)findViewById(R.id.predict_ttd);
        predict_ttd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_predict_ttd = new Intent(Dashboard.this,PredictTtd.class);
                go_predict_ttd.putExtra("nrp",nrp);
                go_predict_ttd.putExtra("pass",pass);
                startActivity(go_predict_ttd);
            }
        });
        logout = (LinearLayout)findViewById(R.id.Logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
            }
        });
    }
}
