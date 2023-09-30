package com.example.myworkedtime;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
    private HoursClass calculateWorkedTime(String startTimeStr, String endTimeStr){
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

    public HoursClass getWorkedHours() {
        return workedHours;
    }
}
