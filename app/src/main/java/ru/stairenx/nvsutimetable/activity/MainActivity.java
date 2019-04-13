package ru.stairenx.nvsutimetable.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;
import com.rhexgomez.typer.roboto.TyperRoboto;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private MaterialCalendarView calendarView;
    private CalendarDay currentDay = CalendarDay.today();
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Animation anim;
    public static String group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        group = getIntent().getExtras().getString("group");
        RecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.setHasFixedSize(true);
        LayoutManager = new LinearLayoutManager(this);
        RecyclerView.setLayoutManager(LayoutManager);
        anim = AnimationUtils.loadAnimation(this, R.anim.translate_list);
        RecyclerView.startAnimation(anim);
        initToolbarAndSnackBar();
        initButtons();
        initMaterialCalendarView();
        update();
    }

    @Override
    protected void onResume() {
        super.onResume();
        buttonSettingsUser.setText(group);
        calendarView.setSelectedDate(CalendarDay.from(currentDay.getDate()));
        updateTableFromDate(group,currentDay);
    }

    public static void update() {
        if (data.size() != 0) {
            PairAdapter adapter = new PairAdapter(data);
            RecyclerView.setAdapter(adapter);
        } else {
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

    private void initToolbarAndSnackBar() {
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitleEnabled(true);
        collapsingToolbarLayout.setCollapsedTitleTypeface(TyperRoboto.ROBOTO_REGULAR());
        collapsingToolbarLayout.setExpandedTitleTypeface(TyperRoboto.ROBOTO_ITALIC());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Расписание на сегодня", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                calendarView.setSelectedDate(CalendarDay.from(LocalDate.now()));
                calendarView.setCurrentDate(currentDay);
                updateTableFromDate(group,currentDay);
            }
        });
    }

    private void initButtons() {
        buttonSettingsUser = (Button) findViewById(R.id.button_settigns_user);
        buttonSettingsUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }
        });

    }

    private String getNowDate() {
        String now;
        Date date = new Date();
        SimpleDateFormat dataFormat = new SimpleDateFormat("E, d.M");
        now = dataFormat.format(date);
        return now;
    }


    public void updateTableFromDate(String group, CalendarDay date) {
        new WebAction.getBook().execute(group, date.getDate().format(dateTimeFormatter));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM");
        if (date.getDay() == currentDay.getDay()) collapsingToolbarLayout.setTitle("На Сегодня");
        else if(date.getDay()-1 == currentDay.getDay()) collapsingToolbarLayout.setTitle("На Завтра");
        else if(date.getDay()-2 == currentDay.getDay()) collapsingToolbarLayout.setTitle("На Послезавтра");
        else if(date.getDay()+1 == currentDay.getDay()) collapsingToolbarLayout.setTitle("Вчера");
        else
            collapsingToolbarLayout.setTitle("На "+date.getDate().format(dateTimeFormatter));
    }

    private void initMaterialCalendarView() {
        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        calendarView.setHeaderTextAppearance(R.style.MaterialCalendarViewHeaderText);
        calendarView.setWeekDayTextAppearance(R.style.MaterialCalendarViewWeekDayText);
        calendarView.setDateTextAppearance(R.style.MaterialCalendarViewDateText);
        calendarView.setTitleFormatter(DateFormatTitleFormatter.DEFAULT);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                updateTableFromDate(group, date);
                RecyclerView.startAnimation(anim);

                //toolbar.setTitle((date.getDate().format(dateTimeFormatter)));
                //getSupportActionBar().setTitle(date.getDate().format(dateTimeFormatter));
            }
        });
    }

}
