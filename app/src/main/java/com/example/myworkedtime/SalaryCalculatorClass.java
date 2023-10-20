package com.example.myworkedtime;

public class SalaryCalculatorClass {

    //technical
    private int currentMonth;
    private int currentYear;
    private double yearSalary;

    //
    private UserDataClass userData;
    public SalaryCalculatorClass(int currentMonth) {
        setCurrentMonth(currentMonth);
    }
    private double calculateSalary(){
        int percentOfTax = 0;
        //if(getIsUserStudent()) return просту типову зпху
        // тут також буде вилічатись перцент оф такс
        return 0.0;
    }
    //getters
    public Double getCalculatedSalary(){
        return calculateSalary();
    }
    private HoursClass getWorkedTime(){
        return HoursWriterClass.getInstance().getAllworkedTime(currentYear,currentMonth);
    }
    private double getRate(){
        return userData.getRate();
    }
    private boolean getIsUserStudent(){
        return userData.isUserStudent();
    }
    //setters
    public void setCurrentMonth(int currentMonth) {
        this.currentMonth = currentMonth;
    }
}
