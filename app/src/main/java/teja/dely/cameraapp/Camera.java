package teja.dely.cameraapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Camera extends AppCompatActivity {

    private CameraView camera_upload;
    private CameraKitEventListener UploadcameradListener;
    private Button btnCapture_upload;
    SweetAlertDialog sweetUpload;
    String nrp,pass;

    TextView status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Intent in_camera = getIntent();
        nrp = in_camera.getStringExtra("nrp");
        pass = in_camera.getStringExtra("pass");


        UploadcameradListener = new CameraKitEventListener() {
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
                Log.d("test", "onImage: "+myBase64Image);
                JSONObject paramObject = new JSONObject();
                final long StartTime = new Date().getTime();
                Call<ResponseApi> kirim =api.kirim(nrp,pass,"data:image/jpeg;base64,"+myBase64Image);
                kirim.enqueue(new Callback<ResponseApi>() {
                    @Override
                    public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                        long EndTime = new Date().getTime();
                        long Delta = EndTime - StartTime;

                        saveDB(nrp,String.valueOf(StartTime),String.valueOf(EndTime),String.valueOf(Delta),String.valueOf(EndTime));
                        sweetUpload = new SweetAlertDialog(Camera.this,SweetAlertDialog.SUCCESS_TYPE);
                        sweetUpload.setTitleText(response.body().getMsg());
                        sweetUpload.show();
                        sweetUpload.findViewById(R.id.confirm_button).setVisibility(View.GONE);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sweetUpload.cancel();
                                finish();

                            }
                        }, 2300);
                    }

                    @Override
                    public void onFailure(Call<ResponseApi> call, Throwable t) {
                        t.printStackTrace();
                        sweetUpload = new SweetAlertDialog(Camera.this,SweetAlertDialog.ERROR_TYPE);
                        sweetUpload.setTitleText("Gagal Upload");
                        sweetUpload.show();
                        sweetUpload.findViewById(R.id.confirm_button).setVisibility(View.GONE);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sweetUpload.cancel();
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

        camera_upload = (CameraView) findViewById(R.id.camera_upload);
        camera_upload.addCameraKitListener(UploadcameradListener);

        btnCapture_upload = (Button) findViewById(R.id.btn_upload);
        btnCapture_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera_upload.captureImage();
            }
        });



    }

    private void saveDB(String name, String start, String end, String delta, String created_at) {
        String _nama=name;
        String _start=start;
        String _end = end;
        String _delta = delta;
        String _created_at = created_at;

        TimeDB mDB = new TimeDB(this);
        boolean status = mDB.insertData(_nama,_start,_end,_delta,_created_at);
        if(status){
            Toast.makeText(this, "Berhasil Simpan ke DB", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Gagal Simpan ke DB", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera_upload.start();
    }

    @Override
    protected void onPause() {
        camera_upload.stop();
        super.onPause();
    }

    private String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
}
