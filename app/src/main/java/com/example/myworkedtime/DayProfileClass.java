package com.example.myworkedtime;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DayProfileClass{
    private int day;
    private String startTime;
    private String endTime;
    private HoursClass workedHours;
    public DayProfileClass(int day, String startTime, String endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.workedHours = calculateWorkedTime(startTime, endTime);
    }
    public void changeDayWorkedTime(String startTime, String endTime){
        this.startTime = startTime;
        this.endTime = endTime;
        this.workedHours = calculateWorkedTime(startTime, endTime);
    }

    private HoursClass calculateWorkedTime(String startTimeStr, String endTimeStr){
        if (startTimeStr == null || endTimeStr == null || startTimeStr.isEmpty() || endTimeStr.isEmpty()) {
            return new HoursClass(0, 0);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = LocalTime.parse(startTimeStr, formatter);
        LocalTime endTime = LocalTime.parse(endTimeStr, formatter);
        Duration duration;

        if (endTime.isBefore(startTime)) {
            duration = Duration.between(startTime, LocalTime.MAX).plus(Duration.between(LocalTime.MIN, endTime));
        } else {
            duration = Duration.between(startTime, endTime);
        }

        long totalMinutes = duration.toMinutes();
        int hours = (int) (totalMinutes / 60);
        int minutes = (int) (totalMinutes % 60);

        return new HoursClass(hours, minutes);
    }

    //getters
    public String getStartTime() {
        return startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public int getDay() {
        return day;
    }
    public HoursClass getWorkedHours() {
        return workedHours;
    }
    //setters
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
