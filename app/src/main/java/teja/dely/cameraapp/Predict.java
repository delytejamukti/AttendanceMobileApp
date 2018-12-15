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
import android.widget.Toast;

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

public class Predict extends AppCompatActivity {

    private CameraView camera_predict;
    private CameraKitEventListener PredictcameradListener;
    private Button btnPredict;
    SweetAlertDialog sweetPredict;
    String nrp,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict);
        Intent in_predict = getIntent();
        nrp = in_predict.getStringExtra("nrp");
        password = in_predict.getStringExtra("pass");
        PredictcameradListener = new CameraKitEventListener() {
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
                Log.d("test", "data:image/jpeg;base64,"+myBase64Image);

                final ApiInterface api = Server.getclient().create(ApiInterface.class);
                Call<ResponseApi> doPredict =api.predict(nrp,password,"data:image/jpeg;base64,"+myBase64Image);

                doPredict.enqueue(new Callback<ResponseApi>() {
                    @Override
                    public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {

                        sweetPredict = new SweetAlertDialog(Predict.this,SweetAlertDialog.SUCCESS_TYPE);
                        sweetPredict.setTitleText(response.body().getMsg());
                        sweetPredict.show();
                        sweetPredict.findViewById(R.id.confirm_button).setVisibility(View.GONE);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sweetPredict.cancel();
                                finish();

                            }
                        }, 2300);

                    }

                    @Override
                    public void onFailure(Call<ResponseApi> call, Throwable t) {
                        sweetPredict = new SweetAlertDialog(Predict.this,SweetAlertDialog.ERROR_TYPE);
                        sweetPredict.setTitleText(t.getMessage());
                        sweetPredict.show();
                        sweetPredict.findViewById(R.id.confirm_button).setVisibility(View.GONE);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sweetPredict.cancel();
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

        camera_predict = (CameraView) findViewById(R.id.camera_predict);
        camera_predict.addCameraKitListener(PredictcameradListener);

        btnPredict = (Button) findViewById(R.id.btn_predict);
        btnPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera_predict.captureImage();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera_predict.start();
    }

    @Override
    protected void onPause() {
        camera_predict.stop();
        super.onPause();
    }
    private String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(),Base64.DEFAULT);
    }
}
