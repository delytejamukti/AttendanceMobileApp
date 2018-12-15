package teja.dely.cameraapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.ByteArrayOutputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PredictTtd extends AppCompatActivity {
    String nrp,pass;
    Button ttd_send_predict,ttd_clear_prdict;
    SignaturePad PredictSignaturePad;
    SweetAlertDialog sweetPredict;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict_ttd);

        Intent in_predict = getIntent();
        nrp = in_predict.getStringExtra("nrp");
        pass = in_predict.getStringExtra("pass");

        ttd_send_predict = (Button)findViewById(R.id.id_predict_ttd);
        ttd_clear_prdict = (Button)findViewById(R.id.id_clear_predict_ttd);

        PredictSignaturePad = (SignaturePad) findViewById(R.id.signature_pad_predict);
        PredictSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                ttd_send_predict.setEnabled(true);
                ttd_clear_prdict.setEnabled(true);
            }

            @Override
            public void onClear() {
                ttd_send_predict.setEnabled(false);
                ttd_clear_prdict.setEnabled(false);

            }
        });
        ttd_clear_prdict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PredictSignaturePad.clear();
            }
        });

        ttd_send_predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap signatureBitmap =   PredictSignaturePad.getSignatureBitmap();
                String myBase64Image = encodeToBase64(signatureBitmap, Bitmap.CompressFormat.JPEG, 100);
                final ApiInterface api = Server.getclient().create(ApiInterface.class);
                Call<ResponseApi> predict_ttd =api.predict_ttd(nrp,pass,"data:image/jpeg;base64,"+myBase64Image);
                predict_ttd.enqueue(new Callback<ResponseApi>() {
                    @Override
                    public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {

                        sweetPredict = new SweetAlertDialog(PredictTtd.this,SweetAlertDialog.SUCCESS_TYPE);
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
                        sweetPredict = new SweetAlertDialog(PredictTtd.this,SweetAlertDialog.ERROR_TYPE);
                        sweetPredict.setTitleText("Gagal Predict TTD");
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
        });

    }

    private String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {

        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
}
