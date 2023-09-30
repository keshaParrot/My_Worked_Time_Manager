package com.example.myworkedtime;

import java.io.File;

public class SettingsWriterClass {
    private SettingsWriterClass instance;
    private double Rate = 1;
    private final String MonthProfileFileName = "WorkedDataFile.json";
    private final String SettingsFileName = "SettingsFile.json";
    private final String FileDir = "MyWorkedTimeLocalData";
    private File MonthProfileFile;
    private File SettingsFile;
    //private curLanguage;
    //private curMonth;
    //private

    private SettingsWriterClass() {
        //this.MonthProfileFile = new File(getExternalFilesDir(FileDir),MonthProfileFileName);
        //this.SettingsFile = new File(getExternalFilesDir(FileDir),SettingsFileName);
        //>>loadData
    }

    public SettingsWriterClass getInstance(){
        if (instance == null) {
            instance = new SettingsWriterClass();
        }
        return instance;
    }

    //saveData
    //loadData

    public double getRate() {
        return Rate;
    }

    public void setRate(double rate) {
        Rate = rate;
    }

    public File getMonthProfileFile() {
        return MonthProfileFile;
    }

    public File getSettingsFile() {
        return SettingsFile;
    }
}

