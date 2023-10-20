package com.example.myworkedtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements MainActivityCallback{

    private TextView workedTimeTextView, totalSalaryTextView,userRateTextView;
    private ImageButton openSettingsButton;
    private HoursWriterClass hoursWriter;
    private SettingsWriterClass settingsWriter;
    private SalaryCalculatorClass salaryCalculator;
    //private CalendarView calendarView;
    private int currentMonth,currentYear;
    private MaterialCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        salaryCalculator = new SalaryCalculatorClass(currentMonth);
        settingsWriter = SettingsWriterClass.getInstance();
        settingsWriter.setContext(this);

        try {
            settingsWriter.setFiles(getExternalFilesDir(settingsWriter.getFileDir()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        calendarView = findViewById(R.id.calendarView);
        openSettingsButton = findViewById(R.id.openSettingsButton);
        workedTimeTextView = findViewById(R.id.WorkedTimeOutput);
        totalSalaryTextView = findViewById(R.id.TotalSalaryOutput);
        userRateTextView = findViewById(R.id.RateOutput);

        try {
            hoursWriter = HoursWriterClass.getInstance(settingsWriter.getMonthProfileFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        actualizeInfo();
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int year = date.getYear();
                int dayOfMonth = date.getDay();
                int month = date.getMonth();

                CalendarMenuDialogFragmentClass dialogFragment = new CalendarMenuDialogFragmentClass(year,dayOfMonth, month,MainActivity.this);
                dialogFragment.setMainActivityCallback(MainActivity.this);
                dialogFragment.show(getSupportFragmentManager(), "show day dialog");
            }
        });
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                int month = date.getMonth();
                updateCurrentMonthAndYear();
                actualizeInfo();
            }
        });

        openSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsMenuDialogFragment settingsDialogFragment = new SettingsMenuDialogFragment();
                settingsDialogFragment.show(getSupportFragmentManager(), "show settings dialog");
            }
        });

    }

    private boolean isExternalStorageAvaibleRW() {
        String extStorageState = Environment.getExternalStorageState();
        return extStorageState.equals(Environment.MEDIA_MOUNTED);
    }
    public boolean isStoragePermissionGranted() {
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }
    }
    public void updateCurrentMonthAndYear(){
        CalendarDay calendar = calendarView.getCurrentDate();
        int monthTMP = calendar.getMonth();
        int yearTMP = calendar.getYear();
        SettingsWriterClass.getInstance().setCurrentDate(yearTMP, monthTMP);
        this.currentMonth = SettingsWriterClass.getInstance().getCurrentMonth();
        this.currentYear = SettingsWriterClass.getInstance().getCurrentYear();
    }
    public void actualizeEvents(){
        CalendarDecoratorClass CalendarDecorator = new CalendarDecoratorClass(Color.RED, HoursWriterClass.getInstance().getAllWorkedDayOnMonth(getCurrentYear(),getCurrentMonth()));
        calendarView.addDecorator(CalendarDecorator);
        Log.e("12edweg23fwdsv", "actualizeEvents: "+HoursWriterClass.getInstance().getAllWorkedDayOnMonth(getCurrentYear(),getCurrentMonth()));
    }
    @Override
    public void actualizeInfo(){
        updateCurrentMonthAndYear();
        actualizeEvents();
        salaryCalculator.setCurrentMonth(currentMonth);
        HoursClass allWorkedTime = hoursWriter.getAllworkedTime(getCurrentYear(),getCurrentMonth());
        if (allWorkedTime==null) allWorkedTime = new HoursClass();
        workedTimeTextView.setText(String.valueOf(allWorkedTime.getHoursOnString()));
        Locale polishLocale = new Locale("pl", "PL");
        NumberFormat polishCurrencyFormat = NumberFormat.getCurrencyInstance(polishLocale);
        totalSalaryTextView.setText(polishCurrencyFormat.format(salaryCalculator.getCalculatedSalary()));
        //SetRateInputField.setText(polishCurrencyFormat.format(CalculateSalaryClass.getInstance().getRatePerHour()));
        Log.i("actualizeInfo", "actualizeInfo: info is actualized");
    }

    public int getCurrentMonth() {
        return currentMonth;
    }

    public int getCurrentYear() {
        return currentYear;
    }
}