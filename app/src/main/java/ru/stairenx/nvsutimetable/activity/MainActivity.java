package ru.stairenx.nvsutimetable.activity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
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
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.rhexgomez.typer.roboto.TyperRoboto;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import ru.stairenx.nvsutimetable.R;
import ru.stairenx.nvsutimetable.adapter.PairAdapter;
import ru.stairenx.nvsutimetable.database.DatabaseAction;
import ru.stairenx.nvsutimetable.database.DatabaseActionTask;
import ru.stairenx.nvsutimetable.item.PairItem;
import ru.stairenx.nvsutimetable.item.Teacheritem;
import ru.stairenx.nvsutimetable.server.ParsTeachersServer;
import ru.stairenx.nvsutimetable.server.WebAction;
import ru.stairenx.nvsutimetable.widget.TimeTableWidget;

public class MainActivity extends AppCompatActivity {

    public static List<PairItem> data = new ArrayList<>();
    public static boolean searchModeGroup = true;
    public static RecyclerView RecyclerView;
    public static ImageView errorCat;
    public static List<Teacheritem> teachers = new ArrayList<Teacheritem>();
    private RecyclerView.LayoutManager LayoutManager;
    private Toolbar toolbar;
    private Button buttonSettingsUser;
    private MaterialCalendarView calendarView;
    private CalendarDay currentDay;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private CheckBox checkBoxSubGroup;
    private static AppCompatAutoCompleteTextView editTextGroup;
    private EditText editTextSubGroup;
    private AppBarLayout appBarLayout;
    private LinearLayout linearLayout;
    public static String group;
    public static String subGroup;
    public static ProgressBar progressBar;
    public static String userKey;
    private List<WebAction.getTimeTableTask> WebActionTask = new ArrayList<WebAction.getTimeTableTask>();
    private ImageView arrowStateCalendar;
    private boolean arrowCalendarAnimationIsRunning = false;
    private TextView textViewMonthCalendar;
    private TextView actionStateTextCalendar;
    private TextView textYearCalendar;

    private final String CALENDAR_IS_COLLAPSING = "(Раскрыть)";
    private final String CALENDAR_IS_EXPANDED = "(Скрыть)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //userKey = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
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
        DatabaseAction.setContext(getApplicationContext());
        new ParsTeachersServer().getTeachers();
        initToolbarAndSnackBar();
        initEditTexts();
        initButtons();
        initMaterialCalendarView();
        linearLayout = (LinearLayout) findViewById(R.id.linear_fast_settings);
        statusBarChangeOnSDK();
        TimeTableWidget.onUpdateWidgetTimeTable(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (group == null | subGroup == null) {
            DatabaseAction.setContext(getApplicationContext());
            group = DatabaseAction.getUserGroup();
            subGroup = DatabaseAction.getUserSubgroup();
        }
        updateDataFromCalendarDay(currentDay);
        TimeTableWidget.onUpdateWidgetTimeTable(getApplicationContext());
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
        } else {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM");
            collapsingToolbarLayout.setTitle("На " + date.getDate().format(dateTimeFormatter));
        }
    }

    private void saveInformation(String newGroup, String newSubGroup) {
        MainActivity.group = newGroup;
        MainActivity.subGroup = newSubGroup;
        DatabaseAction.setContext(getApplicationContext());
        new DatabaseActionTask.changeUserGroupAndSubGroupTask().execute(newGroup, newSubGroup);
        updateDataFromCalendarDay(currentDay);
        TimeTableWidget.onUpdateWidgetTimeTable(getApplicationContext());
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
        editTextGroup = (AppCompatAutoCompleteTextView) findViewById(R.id.edit_search);
        editTextSubGroup = (EditText) findViewById(R.id.edit_text_subgroup);
        editTextGroup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    editTextGroup.setText(null);
                else{

                }

            }
        });
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
                        collapsingToolbarLayout.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    }
                    calendarView.setVisibility(View.INVISIBLE);
                    arrowStateCalendar.setVisibility(View.INVISIBLE);
                    arrowStateCalendar.setRotation(0f);
                    actionStateTextCalendar.setText("Развернуть");
                } else {
                    calendarView.setVisibility(View.VISIBLE);
                    arrowStateCalendar.setVisibility(View.VISIBLE);
                }
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_text_date_calendar);
        actionStateTextCalendar = (TextView) findViewById(R.id.action_state_text_calendar);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!arrowCalendarAnimationIsRunning) {
                    ViewGroup coordinatorLayout = (ViewGroup) findViewById(R.id.main_container);
                    TransitionManager.beginDelayedTransition(coordinatorLayout);
                    if (calendarView.getCalendarMode() == CalendarMode.WEEKS) {
                        calendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
                        actionStateTextCalendar.setText("Свернуть");
                    } else {
                        calendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
                        actionStateTextCalendar.setText("Развернуть");
                    }
                    arrowStateCalendar.animate().rotationBy(540f).setDuration(700).start();
                    collapsingToolbarLayout.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                }
            }
        });
    }

    private void initButtons() {
        buttonSettingsUser = (Button) findViewById(R.id.button_settigns_user);
        buttonSettingsUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                ViewGroup coordinatorLayout = (ViewGroup) findViewById(R.id.linear_fast_settings);
                TransitionManager.beginDelayedTransition(coordinatorLayout);
                editTextGroup.clearFocus();
                if (linearLayout.getLayoutParams().width != LinearLayout.LayoutParams.WRAP_CONTENT) {
                    DatabaseAction.setContext(getApplicationContext());
                    updateDateNamesTeachers();
                    buttonSettingsUser.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_form_toolbar_white));
                    buttonSettingsUser.setText("изм.");
                    buttonSettingsUser.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    buttonSettingsUser.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_save_fast_settings, 0);
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
                    setSizeLinearLayout(LinearLayout.LayoutParams.WRAP_CONTENT);
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editTextGroup.getWindowToken(),0);
                    imm.hideSoftInputFromWindow(editTextSubGroup.getWindowToken(),0);
                    buttonSettingsUser.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_form_toolbar));
                    buttonSettingsUser.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                    buttonSettingsUser.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_user, 0);
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
        arrowStateCalendar = findViewById(R.id.arrow_state_action_calendar);
        arrowStateCalendar.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                arrowCalendarAnimationIsRunning = true;
                collapsingToolbarLayout.setTitleEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                arrowCalendarAnimationIsRunning = false;
                collapsingToolbarLayout.setTitleEnabled(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void initMaterialCalendarView() {
        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        textViewMonthCalendar = (TextView) findViewById(R.id.text_month_calendar);
        textYearCalendar = (TextView) findViewById(R.id.text_year_calendar);
        textViewMonthCalendar.setText(getMonthFromNumber(currentDay.getMonth()));
        textYearCalendar.setText(String.valueOf(currentDay.getYear()));
        calendarView.setDynamicHeightEnabled(true);
        calendarView.setTopbarVisible(false);
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
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                textViewMonthCalendar.setText(getMonthFromNumber(date.getMonth()));
                textYearCalendar.setText(String.valueOf(date.getYear()));
            }
        });
    }

    private String getMonthFromNumber(int numberMonth) {
        switch (numberMonth) {
            case 1:
                return "Январь";
            case 2:
                return "Февраль";
            case 3:
                return "Март";
            case 4:
                return "Апрель";
            case 5:
                return "Май";
            case 6:
                return "Июнь";
            case 7:
                return "Июль";
            case 8:
                return "Август";
            case 9:
                return "Сентябрь";
            case 10:
                return "Октябрь";
            case 11:
                return "Ноябрь";
            case 12:
                return "Декабрь";
            default:
                return "";
        }
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

    public static void updateDateNamesTeachers() {
        if (teachers.isEmpty())
            teachers = DatabaseAction.getTeacherCollection();
        String[] namesTeachers = new String[teachers.size()];
        for (int i = 0; i != teachers.size(); i++) {
            namesTeachers[i] = (teachers.get(i).getName());
        }
        editTextGroup.setAdapter(new ArrayAdapter<>(DatabaseAction.getContext(), android.R.layout.simple_dropdown_item_1line, namesTeachers));
    }

    private void updateWidget(){
        try {
            Intent updateWidget = new Intent(this, TimeTableWidget.class);
            updateWidget.setAction("update_widget");
            PendingIntent pending = PendingIntent.getBroadcast(this, 0, updateWidget, PendingIntent.FLAG_CANCEL_CURRENT);
            pending.send();
        } catch (PendingIntent.CanceledException e) {
            Log.e("-----","Error widgetTrial()="+e.toString());
        }
    }

}
