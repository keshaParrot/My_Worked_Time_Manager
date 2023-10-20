package com.example.myworkedtime;

import static android.content.ContentValues.TAG;
import android.util.Log;
import java.util.ArrayList;

public class MonthProfileClass implements Comparable<MonthProfileClass>{
    private int year; //test
    private int month;
    private HoursClass allWorkedTime;
    private ArrayList<DayProfileClass> WorkedTimeList;
    public MonthProfileClass(int year, int month) {
        this.month = month;
        this.year = year;//test
        this.WorkedTimeList = new ArrayList<>();
        this.allWorkedTime = new HoursClass();
    }
    public void createWorkedDay(int day, String startTime, String endTime){
        if(dayIsExist(day)!=null){
            dayIsExist(day).changeDayWorkedTime(startTime,endTime);
            Log.i(TAG, "createWorkedDay: day is changed");
        }else {
            WorkedTimeList.add(new DayProfileClass(day, startTime, endTime));
            Log.i(TAG, "createWorkedDay: day is added");
        }
        calculateAllWorkedHours();
    }
    private void calculateAllWorkedHours() {
        HoursClass hoursTmp = new HoursClass();
        if (WorkedTimeList != null) {
            for (DayProfileClass listElement : WorkedTimeList) {
                hoursTmp = hoursTmp.add(listElement.getWorkedHours());
            }
        }
        this.allWorkedTime = hoursTmp;
    }
    public String[] getStartAndEndWorkedTime(int day){
        DayProfileClass DayTMP = null;
        String[] StartAndEndTime = new String[2];
        for (DayProfileClass h:WorkedTimeList) {
            if(h.getDay()==day) DayTMP = h;
        }
        if(DayTMP==null) {
            Log.e(TAG, "getStartAndEndWorkedTime: day is not exist");
            StartAndEndTime[0] = "00:00";
            StartAndEndTime[1] = "00:00";
        }
        else {
            StartAndEndTime[0] = DayTMP.getStartTime();
            StartAndEndTime[1] = DayTMP.getEndTime();
        }
        return StartAndEndTime;
    }
    protected DayProfileClass dayIsExist(int day){
        if(day<1 || day>31) {
            Log.i(TAG, "dayIsExist: day cannot be exist");
            return null;
        }
        DayProfileClass tmpDay = null;
        for (DayProfileClass h : WorkedTimeList) {
            if (day == h.getDay()) {
                tmpDay = h;
                Log.i(TAG, "dayIsExist: day "+ tmpDay.getDay() +" is exist");
                break;
            }
        }
        return tmpDay;
    }
    public void deleteWorkedDay(DayProfileClass dayTMP){
        WorkedTimeList.remove(dayTMP);
        calculateAllWorkedHours();
    }
    @Override
    public int compareTo(MonthProfileClass other) {
        return Integer.compare(this.getMonth(), other.getMonth());
    }
    //getters
    public HoursClass getAllworkedTime() {return allWorkedTime;}
    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<DayProfileClass> getWorkedTimeList() {
        return WorkedTimeList;
    }
    //setters
}

