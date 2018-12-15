package teja.dely.cameraapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainTtd extends AppCompatActivity {
    String nrp,pass;
    SweetAlertDialog sweets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_ttd);
        Intent in_training = getIntent();
        nrp = in_training.getStringExtra("nrp");
        pass = in_training.getStringExtra("pass");
        final ApiInterface api = Server.getclient().create(ApiInterface.class);
        Call<ResponseApi> doTrain_ttd = api.train_ttd(nrp,pass);
        doTrain_ttd.enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {

                sweets = new SweetAlertDialog(TrainTtd.this,SweetAlertDialog.SUCCESS_TYPE);
                sweets.setTitleText(response.body().getMsg());
                sweets.show();
                sweets.findViewById(R.id.confirm_button).setVisibility(View.GONE);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sweets.cancel();
                        finish();

                    }
                }, 2300);
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {

                sweets = new SweetAlertDialog(TrainTtd.this,SweetAlertDialog.ERROR_TYPE);
                sweets.setTitleText("Training TTD Gagal");
                sweets.show();
                sweets.findViewById(R.id.confirm_button).setVisibility(View.GONE);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sweets.cancel();
                        finish();
                    }
                }, 2300);

            }
        });


    }
}
