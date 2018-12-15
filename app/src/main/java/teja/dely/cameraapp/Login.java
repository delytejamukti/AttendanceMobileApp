package teja.dely.cameraapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    EditText edit_nrp,edit_pass;
    public SharedPreferences.Editor loginPrefsEditor;
    public  SharedPreferences loginPreferences;
    private Boolean saveLogin;
    int tes =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_login);
            edit_nrp = (EditText)findViewById(R.id.nrp);
            edit_pass = (EditText)findViewById(R.id.pass);

//            final String nrp = edit_nrp.getText().toString();


            CardView btn_login = (CardView)findViewById(R.id.btn_login);
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(Login.this, nrp, Toast.LENGTH_SHORT).show();
                    Intent go_dashboard = new Intent(Login.this,Dashboard.class);
                    go_dashboard.putExtra("nrp",edit_nrp.getText().toString());
                    go_dashboard.putExtra("pass",edit_pass.getText().toString());
                    startActivity(go_dashboard);
                    edit_nrp.setText("");
                    edit_pass.setText("");
                }
            });




    }

}
