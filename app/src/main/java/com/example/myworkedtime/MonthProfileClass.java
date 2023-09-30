package com.example.myworkedtime;

import java.util.ArrayList;

public class MonthProfileClass implements Comparable<MonthProfileClass>{
    private int month;
    private HoursClass allWorkedTime;
    private ArrayList<DayProfileClass> WorkedTimeList;

    @Override
    public int compareTo(MonthProfileClass other) {
        return Integer.compare(this.getMonth(), other.getMonth());
    }

    public MonthProfileClass(int month) {
        this.month = month;
        this.WorkedTimeList = new ArrayList<>();
        this.allWorkedTime = new HoursClass();
    }

    public void createWorkedDay(int day, String startTime, String endTime){
        WorkedTimeList.add(new DayProfileClass(day, startTime, endTime));
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
    public int getMonth() {
        return month;
    }

    public HoursClass getAllworkedTime() {
        return allWorkedTime;
    }
}

