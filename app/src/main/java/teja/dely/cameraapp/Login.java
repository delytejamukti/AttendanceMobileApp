package teja.dely.cameraapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    EditText edit_nrp,edit_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
         edit_nrp = (EditText)findViewById(R.id.nrp);
         edit_pass = (EditText)findViewById(R.id.pass);

        CardView btn_login = (CardView)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_home = new Intent(Login.this,Home.class);
                go_home.putExtra("nrp",edit_nrp.getText().toString());
                go_home.putExtra("pass",edit_pass.getText().toString());
                startActivity(go_home);
            }
        });


    }

}
