package ru.stairenx.nvsutimetable;

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

import java.util.ArrayList;
import java.util.List;

import ru.stairenx.nvsutimetable.adapter.PairAdapter;
import ru.stairenx.nvsutimetable.item.PairItem;
import ru.stairenx.nvsutimetable.server.WebAction;


public class MainActivity extends AppCompatActivity {

    public static List<PairItem> data = new ArrayList<>();
    private static RecyclerView RecyclerView;
    private RecyclerView.LayoutManager LayoutManager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbarAndSnackBar();
        RecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.setHasFixedSize(true);
        LayoutManager = new LinearLayoutManager(this);
        RecyclerView.setLayoutManager(LayoutManager);
        PairAdapter adapter = new PairAdapter(data);
        RecyclerView.setAdapter(adapter);
    }

    public static void update(){
        PairAdapter adapter = new PairAdapter(data);
        RecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
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
         FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
         fab.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Snackbar.make(view, "Расписание на сегодня", Snackbar.LENGTH_LONG)
                         .setAction("Action", null).show();
                 new WebAction.getBook().execute("3702");
                setToolbarTitle("Сегодня + Дата");//потом сделать чтобы сюда дата сама ставилась
             }
         });
     }

     private void setToolbarTitle(String title){
        if(toolbar != null)
            toolbar.setTitle(title);
     }
}
