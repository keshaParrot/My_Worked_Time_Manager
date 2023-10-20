package com.example.myworkedtime;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CalendarMenuDialogFragmentClass extends DialogFragment{
    private ImageButton saveChangeOnDayButton,cancelChangeOnDayButton;
    private Button deleteRecordDayButton, deleteRecordMonthButton;
    private TextView infoAboutDayTextView, allWorkedTimeTextView;
    private String stringStartTime,stringStartTimeTMP,stringEndTime,stringEndTimeTMP;
    private EditText startTimeEditText ,endTimeEditText;
    private int year, day, month;
    private MainActivityCallback callback;
    private Context context;
    public CalendarMenuDialogFragmentClass(int year, int day, int month,Context context) {
        this.year = year;
        this.day = day;
        this.month = month;
        this.context = context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //хуйня, пів коду треба лапатити

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.calendar_menu_dialog, null);
        //view.setBackground(new ColorDrawable(Color.TRANSPARENT));
        //setStyle(DialogFragment.STYLE_NO_FRAME, R.style.MyDialogStyle);

        saveChangeOnDayButton = view.findViewById(R.id.saveButton);
        cancelChangeOnDayButton = view.findViewById(R.id.cancelButton);
        deleteRecordDayButton = view.findViewById(R.id.DeleteRecordDayButton);
        deleteRecordMonthButton = view.findViewById(R.id.DeleteRecordMonthButton);
        infoAboutDayTextView = view.findViewById(R.id.ActualDayTextView);
        allWorkedTimeTextView = view.findViewById(R.id.workedTimeThisDayOutPutTextView);
        startTimeEditText = view.findViewById(R.id.startTimeEditText);
        endTimeEditText = view.findViewById(R.id.endtimeEditText);
        //saveChangeOnDayButton.setEnabled(false);

        actualizeMenuInfo();

        //if(isDataChanged()){
        //    saveChangeOnDayButton.setActivated(true);
        //}
        saveChangeOnDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //тут перевірку на пусті поля пбо пизда
                String startTimeTMP = startTimeEditText.getText().toString();
                String endTimeTMP = endTimeEditText.getText().toString();
                if(!startTimeTMP.isEmpty() && !endTimeTMP.isEmpty()){
                    HoursWriterClass.getInstance().addWorkedDay(getYear(),getMonth(),getDay(),startTimeTMP,endTimeTMP);
                    Log.e(TAG, "onClick: edit texts is valid");
                }else Log.e(TAG, "onClick: edit texts is empty");
                actualizeMenuInfo();
            }
        });
        cancelChangeOnDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "close calendar add day dialog menu");
                getDialog().dismiss();
            }
        });
        deleteRecordDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HoursWriterClass.getInstance().clearDayOnFile(getYear(),getMonth(),getDay());
                actualizeMenuInfo();
            }
        });
        deleteRecordMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HoursWriterClass.getInstance().clearMonthOnFile(getYear(),getMonth());
                actualizeMenuInfo();
            }
        });

        return view;
    }
    public String DayBuilder(int day, int month){
        String tmpDay = "";
        String[] monthNames = getResources().getStringArray(R.array.month_names);
        String monthName = monthNames[month-1];
        tmpDay+=day + " " + monthName;
        return tmpDay;
    }
    private boolean isDataChanged(){
        return stringStartTime.equals(stringStartTimeTMP) && stringEndTime.equals(stringEndTimeTMP);
    }
    private void actualizeMenuInfo(){
        actualizeInfoCallBack();
        infoAboutDayTextView.setText(DayBuilder(getDay(), getMonth()));
        actualizeButtons();
        if(HoursWriterClass.getInstance().dayIsExist(getYear(),getMonth(),getDay())!=null) {
            stringStartTimeTMP = HoursWriterClass.getInstance().getShiftStartAndEndTime(month, day)[0];
            stringEndTimeTMP = HoursWriterClass.getInstance().getShiftStartAndEndTime(month, day)[1];
            allWorkedTimeTextView.setText(HoursWriterClass.getInstance().getWorkedTimeOnDay(year,month,day).getHoursOnString()); //тут може бути кончена помилка, під увагу
            startTimeEditText.setText(stringStartTimeTMP);
            endTimeEditText.setText(stringEndTimeTMP);
            Log.e(TAG, "actualizeMenuInfo: "+ getMonth() +" "+getDay()); //гийби норм виводить і актуалізує місяці

        }else {
            allWorkedTimeTextView.setText(new HoursClass().getHoursOnString());
        }
    }
    private void setDisableImageButton(boolean state,ImageButton button){

    }
    //мб хуйня
    private boolean isValidTimeFormat(String timeStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime.parse(timeStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public void setMainActivityCallback(MainActivityCallback callback) {
        this.callback = callback;
    }
    private void actualizeButtons(){
        if(HoursWriterClass.getInstance().dayIsExist(getYear(),getMonth(),getDay())==null){
            deleteRecordMonthButton.setEnabled(false);
            deleteRecordDayButton.setEnabled(false);
        }else{
            deleteRecordMonthButton.setEnabled(true);
            deleteRecordDayButton.setEnabled(true);
        }
    }
    private void actualizeInfoCallBack(){
        if (callback != null) {
            callback.actualizeInfo();
        }
    }
    //мб хуйня
    private void isInvalidTimeFormat(EditText Time){
        //setColorRed
        Time.setText("00:00");
        Toast.makeText(context, "The time is invalid", Toast.LENGTH_SHORT).show();
    }
}
