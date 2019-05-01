package ru.stairenx.nvsutimetable.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;
import com.rhexgomez.typer.roboto.TyperRoboto;

import org.threeten.bp.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ru.stairenx.nvsutimetable.ConstantsNVSU;
import ru.stairenx.nvsutimetable.R;
import ru.stairenx.nvsutimetable.adapter.PairAdapter;
import ru.stairenx.nvsutimetable.database.DatabaseAction;
import ru.stairenx.nvsutimetable.database.DatabaseActionTask;
import ru.stairenx.nvsutimetable.item.PairItem;
import ru.stairenx.nvsutimetable.server.WebAction;

public class MainActivity extends AppCompatActivity {

    public static List<PairItem> data = new ArrayList<>();
    public static RecyclerView RecyclerView;
    public static ImageView errorCat;
    private RecyclerView.LayoutManager LayoutManager;
    private Toolbar toolbar;
    private Button buttonSettingsUser;
    private MaterialCalendarView calendarView;
    private CalendarDay currentDay;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private CheckBox checkBoxSubGroup;
    private EditText editTextGroup;
    private EditText editTextSubGroup;
    private TextView updateDateLast;
    private AppBarLayout appBarLayout;
    private LinearLayout linearLayout;
    public static String group;
    public static String subGroup;
    public static ProgressBar progressBar;
    public static String userKey;
    private List<WebAction.getTimeTableTask> WebActionTask = new ArrayList<WebAction.getTimeTableTask>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userKey = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        if (savedInstanceState != null)
            currentDay = CalendarDay.from(savedInstanceState.getInt("Year"), savedInstanceState.getInt("Month"), savedInstanceState.getInt("Day"));
        else
            currentDay = CalendarDay.today();
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        RecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.setHasFixedSize(true);
        LayoutManager = new LinearLayoutManager(this);
        RecyclerView.setLayoutManager(LayoutManager);
        errorCat = findViewById(R.id.error_cat);
        initToolbarAndSnackBar();
        initEditTexts();
        initButtons();
        initMaterialCalendarView();
        linearLayout = (LinearLayout) findViewById(R.id.linear_fast_settings);
        statusBarChangeOnSDK();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(group == null | subGroup == null){
            DatabaseAction.setContext(getApplicationContext());
            group = DatabaseAction.getUserGroup();
            subGroup = DatabaseAction.getUserSubgroup();
        }
        updateDataFromCalendarDay(currentDay);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("Day", currentDay.getDay());
        outState.putInt("Month", currentDay.getMonth());
        outState.putInt("Year", currentDay.getYear());
        super.onSaveInstanceState(outState);
    }

    public static void update() {
        PairAdapter adapter = new PairAdapter(data);
        RecyclerView.setAdapter(adapter);
    }

    private void updateDataFromCalendarDay(CalendarDay day) {
        buttonSettingsUser.setText(group);
        calendarView.setSelectedDate(CalendarDay.from(day.getDate()));
        calendarView.setCurrentDate(day);
        updateTableFromDate(group, day);
        //updateDateLast.setText(calendarView.getSelectedDate().getDate().getMonth().toString());
    }

    public void updateTableFromDate(String group, CalendarDay date) {
        if (!WebActionTask.isEmpty()) {
            WebActionTask.get(0).cancel(true);
            WebActionTask.clear();
        }
        WebActionTask.add(new WebAction().getTimeTableCreateTask(group, date.getDate().format(dateTimeFormatter), userKey));
        if (CalendarDay.today().getMonth() == date.getMonth() & CalendarDay.today().getYear() == date.getYear()) {
            switch (CalendarDay.today().getDay() - date.getDay()) {
                case 1:
                    collapsingToolbarLayout.setTitle("Вчера");
                    break;
                case 0:
                    collapsingToolbarLayout.setTitle("На Сегодня");
                    break;
                case -1:
                    collapsingToolbarLayout.setTitle("На Завтра");
                    break;
                case -2:
                    collapsingToolbarLayout.setTitle("На Послезавтра");
                    break;
                default:
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM");
                    collapsingToolbarLayout.setTitle("На " + date.getDate().format(dateTimeFormatter));
                    break;
            }
        }
        else {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM");
            collapsingToolbarLayout.setTitle("На " + date.getDate().format(dateTimeFormatter));
        }

    }

    private void updateTitleCollapsingToolbarFromDate(CalendarDay day) {

    }

    private void saveInformation(String newGroup, String newSubGroup) {
        MainActivity.group = newGroup;
        MainActivity.subGroup = newSubGroup;
        DatabaseAction.setContext(getApplicationContext());
        new DatabaseActionTask.changeUserGroupAndSubGroupTask().execute(newGroup, newSubGroup);
        updateDataFromCalendarDay(currentDay);
        Toast.makeText(getApplicationContext(), "Изменения сохранены", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (Math.abs(i) - appBarLayout.getTotalScrollRange() == 0) { //  Collapsed
                    if (calendarView.getCalendarMode() != CalendarMode.WEEKS) {
                        calendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
                        collapsingToolbarLayout.getLayoutParams().height = (int) getResources().getDimension(R.dimen.app_bar_height_collapse);
                    }
                    calendarView.setVisibility(View.INVISIBLE);
                } else
                    calendarView.setVisibility(View.VISIBLE);
            }
        });
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
                    editTextGroup.setText(group);
                    if (!DatabaseAction.getUserSubgroup().equals("0")) {
                        editTextSubGroup.setVisibility(View.VISIBLE);
                        checkBoxSubGroup.setChecked(true);
                        editTextSubGroup.setText(DatabaseAction.getUserSubgroup());
                        checkBoxSubGroup.setText("");
                    } else {
                        editTextSubGroup.setText("");
                        editTextSubGroup.setVisibility(View.GONE);
                        checkBoxSubGroup.setChecked(false);
                        checkBoxSubGroup.setText("Указать \n подгруппу");
                    }
                    setSizeLinearLayout(LinearLayout.LayoutParams.MATCH_PARENT);
                } else {
                    buttonSettingsUser.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_form_toolbar));
                    buttonSettingsUser.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                    buttonSettingsUser.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_user, 0);
                    collapsingToolbarLayout.setTitleEnabled(true);
                    setSizeLinearLayout(0);
                    if (editTextGroup.length() > 3 && checkBoxSubGroup.isChecked())
                        if (editTextSubGroup.getText().toString().equals(""))
                            saveInformation(editTextGroup.getText().toString(), "0");
                        else
                            saveInformation(editTextGroup.getText().toString(), editTextSubGroup.getText().toString());
                    else if (editTextGroup.length() > 3 && !checkBoxSubGroup.isChecked())
                        saveInformation(editTextGroup.getText().toString(), "0");
                    else
                        Toast.makeText(getApplicationContext(), "Параметры заданы не верно, попробуйте еще раз", Toast.LENGTH_SHORT).show();
                }
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Расписание на сегодня", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                updateDataFromCalendarDay(CalendarDay.today());
                currentDay = CalendarDay.today();
            }
        });
        checkBoxSubGroup = (CheckBox) findViewById(R.id.check_subgroup_toolbar);
        checkBoxSubGroup.setChecked(false);
        checkBoxSubGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxSubGroup.isChecked()) {
                    editTextSubGroup.setVisibility(View.VISIBLE);
                    checkBoxSubGroup.setText("");
                } else {
                    editTextSubGroup.setVisibility(View.GONE);
                    checkBoxSubGroup.setText("Указать \n подгруппу");
                }
            }
        });
    }

    private void initMaterialCalendarView() {
        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        calendarView.setDynamicHeightEnabled(true);
        TitleFormatter titleFormatter = new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                DateTimeFormatter TimeFormatter = DateTimeFormatter.ofPattern("LLL yyyy");
                return day.getDate().format(TimeFormatter) + " года";
            }
        };
        calendarView.setTitleFormatter(titleFormatter);
        calendarView.setHeaderTextAppearance(R.style.MaterialCalendarViewHeaderText);
        calendarView.setWeekDayTextAppearance(R.style.MaterialCalendarViewWeekDayText);
        calendarView.setDateTextAppearance(R.style.MaterialCalendarViewDateText);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                currentDay = date;
                updateDataFromCalendarDay(date);
            }
        });
        calendarView.setOnTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup coordinatorLayout = (ViewGroup) findViewById(R.id.main_container);
                TransitionManager.beginDelayedTransition(coordinatorLayout);
                if (calendarView.getCalendarMode() == CalendarMode.WEEKS)
                    calendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
                else
                    calendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
                collapsingToolbarLayout.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        });
    }

    private void setSizeLinearLayout(int size) {
        linearLayout.getLayoutParams().width = size;
        linearLayout.requestLayout();
    }

    @SuppressLint("NewApi")
    private void statusBarChangeOnSDK() { //Определение SDK платформы, чтобы сделать статус бар прозрачным для этой активности + белый шрифт статус бара.
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor("#00000000"));
        }
    }
}
