package teja.dely.cameraapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.io.ByteArrayOutputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StepThree extends AppCompatActivity {

    String nrp,pass,idAgenda,lat,lon;
    private CameraView camera_absen;
    private CameraKitEventListener cameradListener;
    private Button btnAbsen;
    SweetAlertDialog sweetAbsen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen_camera);
        Intent in_next_step = getIntent();
        nrp = in_next_step.getStringExtra("nrp");
        pass = in_next_step.getStringExtra("pass");
        idAgenda = in_next_step.getStringExtra("idAgenda");
        lat = in_next_step.getStringExtra("latitude");
        lon = in_next_step.getStringExtra("longitude");

//        Toast.makeText(this, nrp, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, pass, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, idAgenda, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, lat, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, lon, Toast.LENGTH_SHORT).show();

        cameradListener = new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {

                byte[] picture = cameraKitImage.getJpeg();
                Bitmap result = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                result = Bitmap.createScaledBitmap(result, 96,96, true);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                String myBase64Image = encodeToBase64(result, Bitmap.CompressFormat.JPEG, 100);
                final ApiInterface api = Server.getclient().create(ApiInterface.class);
                Call<ResponseApi> doabsen =api.doAbsen(nrp,pass,"data:image/jpeg;base64,"+myBase64Image,lat,lon,idAgenda);
                doabsen.enqueue(new Callback<ResponseApi>() {
                    @Override
                    public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {

                        sweetAbsen = new SweetAlertDialog(StepThree.this,SweetAlertDialog.SUCCESS_TYPE);
                        sweetAbsen.setTitleText(response.body().getMsg());
                        sweetAbsen.show();
                        sweetAbsen.findViewById(R.id.confirm_button).setVisibility(View.GONE);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sweetAbsen.cancel();
                                finish();
                                Intent go_home = new Intent(StepThree.this,Home.class);
                                go_home.putExtra("nrp",nrp);
                                go_home.putExtra("pass",pass);
                                startActivity(go_home);

                            }
                        }, 2300);

                    }

                    @Override
                    public void onFailure(Call<ResponseApi> call, Throwable t) {
                        sweetAbsen = new SweetAlertDialog(StepThree.this,SweetAlertDialog.ERROR_TYPE);
                        sweetAbsen.setTitleText(t.getMessage());
                        sweetAbsen.show();
                        sweetAbsen.findViewById(R.id.confirm_button).setVisibility(View.GONE);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sweetAbsen.cancel();
                                finish();
                            }
                        }, 2300);

                    }
                });


            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        };

        camera_absen = (CameraView) findViewById(R.id.camera_absen);
        camera_absen.addCameraKitListener(cameradListener);

        btnAbsen = (Button) findViewById(R.id.btn_absen);
        btnAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera_absen.captureImage();
            }
        });



    }
    @Override
    protected void onResume() {
        super.onResume();
        camera_absen.start();
    }

    @Override
    protected void onPause() {
        camera_absen.stop();
        super.onPause();
    }
    private String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(),Base64.DEFAULT);
    }


}
