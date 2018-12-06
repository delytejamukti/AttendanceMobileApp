package teja.dely.cameraapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static String[] header = {"No","Nama","Start","End","Time"};
    TimeDB mDB;
    float ratarata=0;
    String nrp,pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView judul = (TextView)findViewById(R.id.judul);

        Intent in_home = getIntent();


        nrp = in_home.getStringExtra("nrp");
        pass = in_home.getStringExtra("pass");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TableView<String[]> tableView = (TableView<String[]>)findViewById(R.id.tableView);
        tableView.setHeaderBackgroundColor(Color.parseColor("#0579b7"));
        tableView.setHeaderAdapter( new SimpleTableHeaderAdapter(this,header));
        tableView.setColumnCount(5);
        mDB = new TimeDB(this);
        Cursor data = mDB.getData();
        ArrayList<String> ListData = new ArrayList<String>();
        String datanya [] [] = new String [data.getCount()][5];
        int i =0;
        int jumlah=0;
        while (data.moveToNext())
        {

            String _nama_ = data.getString(0);
            String _start_ = data.getString(1);
            String _end_ = data.getString(2);
            String _delta_ = data.getString(3);
            String _time_ = data.getString(4);
            jumlah+= Integer.parseInt(_time_);

            datanya[i][0] = _nama_;
            datanya[i][1] = _start_;
            datanya[i][2] = _end_;
            datanya[i][3] = _delta_;
            datanya[i][4] = _time_;
            i++;

        }
        ratarata = jumlah/(i+1);

        tableView.setDataAdapter(new SimpleTableDataAdapter(this,datanya));
        String isi_judul = "Tabel Waktu Upload      Avg = "+String.valueOf(ratarata/1000)+"s";
        judul.setText(isi_judul);
        tableView.addDataClickListener(new TableDataClickListener<String[]>() {
            @Override
            public void onDataClicked(int rowIndex, String[] clickedData) {
                Toast.makeText(Home.this,((String[])clickedData)[1], Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_photo) {
            Intent photo = new Intent(Home.this,Camera.class);
            photo.putExtra("nrp",nrp);
            photo.putExtra("pass",pass);
            startActivity(photo);
        } else if (id == R.id.nav_time) {
            Intent time = new Intent(Home.this,Home.class);
            startActivity(time);
        } else if (id == R.id.nav_training) {
            Intent training = new Intent(Home.this,Training.class);
            training.putExtra("nrp",nrp);
            training.putExtra("pass",pass);
            startActivity(training);
        } else if (id == R.id.nav_predict) {
            Intent predict = new Intent(Home.this,Predict.class);
            predict.putExtra("nrp",nrp);
            predict.putExtra("pass",pass);
            startActivity(predict);
        } else if (id == R.id.nav_absen){
            Intent go_step_two = new Intent(Home.this,StepTwo.class);
            go_step_two.putExtra("nrp",nrp);
            go_step_two.putExtra("pass",pass);
            startActivity(go_step_two);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
