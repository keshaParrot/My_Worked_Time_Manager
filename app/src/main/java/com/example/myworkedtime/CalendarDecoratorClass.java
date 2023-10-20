package com.example.myworkedtime;

import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;

public class CalendarDecoratorClass implements DayViewDecorator {
    private final int color;
    private ArrayList<CalendarDay> dates;

    public CalendarDecoratorClass(int color, ArrayList<CalendarDay> dates) {
        this.color = color;
        setDates(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        //view.addSpan(new ForegroundColorSpan(color));
        view.addSpan(new DotSpan(10, color));
    }
    public void clearDecorate() {
        setDates(new ArrayList<>());
    }
    public void setDates(ArrayList<CalendarDay> dates){
        this.dates = dates;
    }
}
