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

public class StepFour extends AppCompatActivity {

    SignaturePad SignSignaturePad;
    Button sign_send,sign_clear;
    String nrp,pass,idAgenda,lat,lon;
    SweetAlertDialog sweetSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_four);

        Intent in_step_four = getIntent();
        nrp = in_step_four.getStringExtra("nrp");
        pass = in_step_four.getStringExtra("pass");
        idAgenda = in_step_four.getStringExtra("idAgenda");
        lat = in_step_four.getStringExtra("lat");
        lon = in_step_four.getStringExtra("lon");


        sign_send = (Button)findViewById(R.id.id_signin_ttd);
        sign_clear = (Button)findViewById(R.id.id_clear_signin_ttd);

        SignSignaturePad = (SignaturePad) findViewById(R.id.signature_pad_signin);
        SignSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                sign_send.setEnabled(true);
                sign_clear.setEnabled(true);

            }

            @Override
            public void onClear() {
                sign_send.setEnabled(false);
                sign_clear.setEnabled(false);
            }
        });

        sign_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap signatureBitmap =   SignSignaturePad.getSignatureBitmap();
                String myBase64Image = encodeToBase64(signatureBitmap, Bitmap.CompressFormat.JPEG, 100);
                final ApiInterface api = Server.getclient().create(ApiInterface.class);
                Call<ResponseApi> signin_ttd = api.doAbsen_ttd(nrp,pass,"data:image/jpeg;base64,"+myBase64Image,lat,lon,idAgenda);
                signin_ttd.enqueue(new Callback<ResponseApi>() {
                    @Override
                    public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {

                        sweetSignin = new SweetAlertDialog(StepFour.this,SweetAlertDialog.SUCCESS_TYPE);
                        sweetSignin.setTitleText(response.body().getMsg());
                        sweetSignin.show();
                        sweetSignin.findViewById(R.id.confirm_button).setVisibility(View.GONE);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sweetSignin.cancel();
                                finish();

                            }
                        }, 2300);

                    }

                    @Override
                    public void onFailure(Call<ResponseApi> call, Throwable t) {

                        sweetSignin = new SweetAlertDialog(StepFour.this,SweetAlertDialog.ERROR_TYPE);
                        sweetSignin.setTitleText("Gagal Sign In TTD");
                        sweetSignin.show();
                        sweetSignin.findViewById(R.id.confirm_button).setVisibility(View.GONE);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sweetSignin.cancel();
                                finish();

                            }
                        }, 2300);



                    }
                });





            }
        });

        sign_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignSignaturePad.clear();
            }
        });

    }

    private String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {

        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
}
