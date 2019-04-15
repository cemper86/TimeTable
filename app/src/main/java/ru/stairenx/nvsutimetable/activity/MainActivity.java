package ru.stairenx.nvsutimetable.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;
import com.rhexgomez.typer.roboto.TyperRoboto;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import ru.stairenx.nvsutimetable.ConstantsNVSU;
import ru.stairenx.nvsutimetable.R;
import ru.stairenx.nvsutimetable.adapter.PairAdapter;
import ru.stairenx.nvsutimetable.database.DatabaseAction;
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
    private ImageView imageCalendarArrow;
    private Animation animRecyclerView;
    private CheckBox checkBoxSubGroup;
    private EditText editTextGroup;
    private EditText editTextSubGroup;
    private TextView updateDateLast;
    LinearLayout linearLayout;
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
        animRecyclerView = AnimationUtils.loadAnimation(this, R.anim.translate_list);
        RecyclerView.startAnimation(animRecyclerView);
        initToolbarAndSnackBar();
        initEditTexts();
        initButtons();
        initMaterialCalendarView();
        update();
        linearLayout = (LinearLayout) findViewById(R.id.linear_fast_settings);

    }

    private void updateData(){
        buttonSettingsUser.setText(group);
        calendarView.setSelectedDate(CalendarDay.from(currentDay.getDate()));
        updateTableFromDate(group, currentDay);
        updateDateLast.setText("");// pqwoe!
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
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

    private void initEditTexts() {
        editTextGroup = (EditText) findViewById(R.id.edit_text_group);
        editTextSubGroup = (EditText) findViewById(R.id.edit_text_subgroup);
        updateDateLast = (TextView) findViewById(R.id.update_date_last);
    }

    private void initToolbarAndSnackBar() {
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitleEnabled(true);
        collapsingToolbarLayout.setCollapsedTitleTypeface(TyperRoboto.ROBOTO_REGULAR());
        collapsingToolbarLayout.setExpandedTitleTypeface(TyperRoboto.ROBOTO_ITALIC());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    private void initButtons() {
        buttonSettingsUser = (Button) findViewById(R.id.button_settigns_user);
        buttonSettingsUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                ViewGroup coordinatorLayout = (ViewGroup) findViewById(R.id.linear_fast_settings);
                TransitionManager.beginDelayedTransition(coordinatorLayout);
                if (linearLayout.getLayoutParams().width != LinearLayout.LayoutParams.MATCH_PARENT) {
                    buttonSettingsUser.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_form_toolbar_white));
                    buttonSettingsUser.setText("изм.");
                    buttonSettingsUser.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    buttonSettingsUser.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_save_fast_settings, 0);
                    collapsingToolbarLayout.setTitleEnabled(false);
                    setSizeLinearLayout(LinearLayout.LayoutParams.MATCH_PARENT);
                } else {
                    buttonSettingsUser.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_form_toolbar));
                    buttonSettingsUser.setText(group);
                    buttonSettingsUser.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                    buttonSettingsUser.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_user, 0);
                    collapsingToolbarLayout.setTitleEnabled(true);
                    setSizeLinearLayout(0);
                    if(editTextGroup.length()>0 && checkBoxSubGroup.isChecked()) saveInformation(editTextGroup.getText().toString(),editTextSubGroup.getText().toString());
                    else if (editTextGroup.length()>0 && !checkBoxSubGroup.isChecked()) saveInformation(editTextGroup.getText().toString(),"0");
                }
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Расписание на сегодня", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                calendarView.setSelectedDate(CalendarDay.from(LocalDate.now()));
                calendarView.setCurrentDate(currentDay);
                updateTableFromDate(group, currentDay);
            }
        });
        imageCalendarArrow = (ImageView) findViewById(R.id.ImageCalendarArrow);
        checkBoxSubGroup = (CheckBox) findViewById(R.id.check_subgroup_toolbar);
        checkBoxSubGroup.setChecked(false);
        checkBoxSubGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxSubGroup.isChecked()) {
                    editTextSubGroup.setVisibility(View.VISIBLE);
                    checkBoxSubGroup.setText("");
                } else {
                    editTextSubGroup.setVisibility(View.INVISIBLE);
                    checkBoxSubGroup.setText("Указать \n подгруппу");
                }
            }
        });
    }

    public void updateTableFromDate(String group, CalendarDay date) {
        new WebAction.getBook().execute(group, date.getDate().format(dateTimeFormatter));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM");
        if (date.getDay() == currentDay.getDay()) collapsingToolbarLayout.setTitle("На Сегодня");
        else if (date.getDay() - 1 == currentDay.getDay())
            collapsingToolbarLayout.setTitle("На Завтра");
        else if (date.getDay() - 2 == currentDay.getDay())
            collapsingToolbarLayout.setTitle("На Послезавтра");
        else if (date.getDay() + 1 == currentDay.getDay())
            collapsingToolbarLayout.setTitle("Вчера");
        else
            collapsingToolbarLayout.setTitle("На " + date.getDate().format(dateTimeFormatter));
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
                RecyclerView.startAnimation(animRecyclerView);
            }
        });
        calendarView.setOnDateLongClickListener(new OnDateLongClickListener() {
            @Override
            public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {

            }
        });
        calendarView.setOnTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup coordinatorLayout = (ViewGroup) findViewById(R.id.main_container);
                TransitionManager.beginDelayedTransition(coordinatorLayout);
                if (calendarView.getCalendarMode() == CalendarMode.WEEKS) {
                    calendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
                    imageCalendarArrow.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorFloatingActionButton));
                } else {
                    calendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
                    imageCalendarArrow.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary2));
                }

                imageCalendarArrow.animate().rotationBy(180).setDuration(600).start();

            }
        });
    }

    private void setSizeLinearLayout(int size) {
        linearLayout.getLayoutParams().width = size;
        linearLayout.requestLayout();
    }

    private void saveInformation(String newGroup, String newSubGroup) {
        MainActivity.group = newGroup;
        DatabaseAction.setContext(getApplicationContext());
        DatabaseAction.changeUserGroupAndSubGroup(newGroup, newSubGroup);
        updateData();
        //Toast.makeText(SettingsActivity.this, "Группа изменена на " + gr, Toast.LENGTH_SHORT).show();
    }
}
