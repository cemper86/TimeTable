package ru.stairenx.nvsutimetable.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.stairenx.nvsutimetable.ConstantsNVSU;
import ru.stairenx.nvsutimetable.R;
import ru.stairenx.nvsutimetable.adapter.PairAdapter;
import ru.stairenx.nvsutimetable.item.PairItem;
import ru.stairenx.nvsutimetable.server.WebAction;


public class MainActivity extends AppCompatActivity {

    public static List<PairItem> data = new ArrayList<>();
    private static RecyclerView RecyclerView;
    private RecyclerView.LayoutManager LayoutManager;
    private Toolbar toolbar;
    private Button buttonSettingsUser;
    public static String group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        group = getIntent().getExtras().getString("group");
        setContentView(R.layout.activity_main);
        initToolbarAndSnackBar();
        RecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.setHasFixedSize(true);
        LayoutManager = new LinearLayoutManager(this);
        RecyclerView.setLayoutManager(LayoutManager);
        initButtons();
        getTimeTable(group);
        update();
    }

    @Override
    protected void onResume() {
        super.onResume();
        buttonSettingsUser.setText(group);
        getTimeTable(group);

    }

    public static void update(){
        if(data.size()!=0) {
            PairAdapter adapter = new PairAdapter(data);
            RecyclerView.setAdapter(adapter);
        }else{
            data.add(ConstantsNVSU.ITEM_PLACEHOLDER);
            PairAdapter adapter = new PairAdapter(data);
            RecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_user) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
     private void initToolbarAndSnackBar(){
         toolbar = (Toolbar) findViewById(R.id.toolbar);
         toolbar.setTitle("");
         setSupportActionBar(toolbar);
         // Ставиться в Toolbar сегодняшняя дата
         setToolbarTitle("Cегодня "+ getNowDate());
         FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
         fab.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Snackbar.make(view, "Расписание на сегодня", Snackbar.LENGTH_LONG)
                         .setAction("Action", null).show();
                 getTimeTable(group);
             }
         });
     }

     private void setToolbarTitle(String title){
        if(toolbar != null)
            toolbar.setTitle(title);
     }

     private void initButtons(){
         buttonSettingsUser = (Button) findViewById(R.id.button_settigns_user);
         buttonSettingsUser.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
             }
         });

     }

     private String getNowDate(){
        String now;
        Date date = new Date();
        SimpleDateFormat dataFormat = new SimpleDateFormat("E, d.M");
        now = dataFormat.format(date);
        return now;
     }

     public static void getTimeTable(String group){
         new WebAction.getBook().execute(group);
     }

}
