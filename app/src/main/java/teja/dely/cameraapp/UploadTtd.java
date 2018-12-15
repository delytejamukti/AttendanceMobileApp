package teja.dely.cameraapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.ByteArrayOutputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadTtd extends AppCompatActivity {

    SignaturePad mSignaturePad;
    Button ttd_send,ttd_clear;
    String nrp,pass;
    SweetAlertDialog sweetUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_ttd);

        Intent in_upload_ttd = getIntent();
        nrp = in_upload_ttd.getStringExtra("nrp");
        pass = in_upload_ttd.getStringExtra("pass");


        ttd_send = (Button)findViewById(R.id.id_send_ttd);
        ttd_clear = (Button)findViewById(R.id.id_clear_ttd);

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                ttd_send.setEnabled(true);
                ttd_clear.setEnabled(true);
//                Bitmap hasil= Bitmap.createScaledBitmap(mSignaturePad.getSignatureBitmap(),96,96,true);

            }

            @Override
            public void onClear() {
                ttd_send.setEnabled(false);
                ttd_clear.setEnabled(false);

            }
        });
        ttd_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignaturePad.clear();
            }
        });

        ttd_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap signatureBitmap =   mSignaturePad.getSignatureBitmap();
                String myBase64Image = encodeToBase64(signatureBitmap, Bitmap.CompressFormat.JPEG, 100);
                final ApiInterface api = Server.getclient().create(ApiInterface.class);
                Call<ResponseApi> kirim_ttd =api.kirim_ttd(nrp,pass,"data:image/jpeg;base64,"+myBase64Image);
                kirim_ttd.enqueue(new Callback<ResponseApi>() {
                    @Override
                    public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                        sweetUpload = new SweetAlertDialog(UploadTtd.this,SweetAlertDialog.SUCCESS_TYPE);
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
                        sweetUpload = new SweetAlertDialog(UploadTtd.this,SweetAlertDialog.ERROR_TYPE);
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
        });


    }

    private String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {

        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
}
