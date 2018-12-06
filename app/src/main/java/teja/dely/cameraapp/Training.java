package teja.dely.cameraapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Training extends AppCompatActivity {
    String nrp,pass;
    SweetAlertDialog sweets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        Intent in_training = getIntent();
        nrp = in_training.getStringExtra("nrp");
        pass = in_training.getStringExtra("pass");

        final ApiInterface api = Server.getclient().create(ApiInterface.class);
        Call<ResponseApi> doTrain = api.train(nrp,pass);
        doTrain.enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                if(response.code() == 200){
                    sweets = new SweetAlertDialog(Training.this,SweetAlertDialog.SUCCESS_TYPE);
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
//                Toast.makeText(Training.this,response.body().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
//                Toast.makeText(Training.this, "Gagal train", Toast.LENGTH_SHORT).show();
                sweets = new SweetAlertDialog(Training.this,SweetAlertDialog.ERROR_TYPE);
                sweets.setTitleText("Training Data Gagal");
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
