package com.example.myworkedtime;

import static android.content.ContentValues.TAG;

import android.util.Log;

public class HoursClass {
    private int hours;
    private int minutes;

    public HoursClass(int hours, int minutes) {
        if((hours>=0)&&(hours<=23)){
            if((minutes>=0)&&(minutes<=59)){
                this.hours = hours;
                this.minutes = minutes;
            }else Log.e(TAG, "HoursClass: minutes cannot be less as 0, and more than 59");
        }else Log.e(TAG, "HoursClass: hours cannot be less as 0, and more than 23");
    }
    public HoursClass() {
        this.hours = 0;
        this.minutes = 0;
    }
    public HoursClass add(HoursClass other) {
        int newHours = this.hours + other.hours;
        int newMinutes = this.minutes + other.minutes;

        if (newMinutes >= 60) {
            newHours += newMinutes / 60;
            newMinutes %= 60;
        }

        return new HoursClass(newHours, newMinutes);
    }
    public HoursClass subtract(HoursClass other) {
        int newHours = this.hours - other.hours;
        int newMinutes = this.minutes - other.minutes;

        if (newMinutes < 0) {
            newHours--;
            newMinutes += 60;
        }

        if (newHours < 0) {
            newHours = 0;
            newMinutes = 0; // Запобігаємо від'ємним значенням годин і хвилин
        }

        return new HoursClass(newHours, newMinutes);
    }

    //  getters
    public String getHoursOnString(){
        return String.format("%02dh:%02dm", getHours(), getMinutes());
    }
    public int getHours() {
        return hours;
    }
    public int getMinutes() {
        return minutes;
    }

    //setters
    public void setHoursAndMinutes(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
        //normalize(); // Викликаємо метод для нормалізації годин та хвилин
    }
    public void setHoursAndMinutes(HoursClass other) {
        this.hours = other.getHours();
        this.minutes = other.getMinutes();
        //normalize(); // Викликаємо метод для нормалізації годин та хвилин
    }
    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

}