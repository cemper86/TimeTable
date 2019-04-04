package ru.stairenx.nvsutimetable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private static RecyclerView rv;
    private RecyclerView.LayoutManager lm;
    private Button btn;
    private EditText group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        group = findViewById(R.id.numGroup);
        btn = findViewById(R.id.btn);
        initBtn();
        rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        PairAdapter adapter = new PairAdapter(data);
        rv.setAdapter(adapter);
    }

    public static void update(){
        PairAdapter adapter = new PairAdapter(data);
        rv.setAdapter(adapter);
    }

    private void initBtn(){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gr = group.getText().toString();
                new WebAction.getBook().execute(gr);
            }
        });
    }
}
