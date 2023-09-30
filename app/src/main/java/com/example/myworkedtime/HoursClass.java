package com.example.myworkedtime;

public class HoursClass {
    private int hours;
    private int minutes;

    public HoursClass(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }
    public HoursClass() {
        this.hours = 0;
        this.minutes = 0;
    }
    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
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
}

