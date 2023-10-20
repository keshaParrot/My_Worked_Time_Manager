package com.example.myworkedtime;

import android.content.Context;
import android.util.Log;
import java.util.Date;
import java.io.File;
import java.io.IOException;

public class SettingsWriterClass {
    private static SettingsWriterClass instance;
    private File getExternalFileDir;
    private final String MonthProfileFileName = "WorkedDataFile.json";
    private final String SettingsFileName = "SettingsFile.json";
    private final String FileDir = "MyWorkedTimeLocalData";
    private File MonthProfileFile;
    private File SettingsFile;
    private int currentMonth = 1; // no usages
    private int currentYear;
    private Context context;
    //  this we need to write on file
    //private curLanguage;
    UserDataClass userData = null;

    private SettingsWriterClass() {
        userData = new UserDataClass();
    }

    public static SettingsWriterClass getInstance(){
        if (instance == null) {
            instance = new SettingsWriterClass();
        }
        return instance;
    }
    public void setFiles(File getExternalFileDir) throws IOException {
        this.getExternalFileDir = getExternalFileDir;
        this.MonthProfileFile = new File(getExternalFileDir,getMonthProfileFileName());
        this.SettingsFile = new File(getExternalFileDir,getSettingsFileName());
        if(!(this.MonthProfileFile.exists() && this.SettingsFile.exists())){
            this.MonthProfileFile.createNewFile();
            this.SettingsFile.createNewFile();
        }
    }
    public File getBackupOfProfile() throws IOException {
        return new File(getExternalFileDir,"BackupProfile.json");

    }
    public void createBackupFile() throws IOException {
        if(!getBackupOfProfile().exists()){
            getBackupOfProfile().createNewFile();
            setDateOfCreateBackup(dayBuilder());
        }
    }
    //saveData
    //loadData
    public String dayBuilder() throws IOException {
        Date currentDate = new Date();
        String[] monthNames = context.getResources().getStringArray(R.array.month_names);
        String monthName = monthNames[currentDate.getMonth()];

        return (currentDate.getDay()+1) +" "+ monthName +" - "+HoursWriterClass.getInstance().getCalculatedWeightOfBackBackup();
    }
    public void setRatePerHour(double rate) {
        if(rate>0) setRate(rate);
        else{
            //Toast.makeText(context, "ставка не може бути меншою за нуль", Toast.LENGTH_SHORT).show(); //error
            Log.e("CalculateSalary", "ставка не може бути меншою яко нуль");
        }
    }
    public void setUserDataClass(UserDataClass userDataClass){
        this.userData = userDataClass;
    }
    public void setContext(Context context) {
        this.context = context;
    }
    public File getMonthProfileFile() throws IOException {
        return MonthProfileFile;
    }

    public String getMonthProfileFileName() {
        return MonthProfileFileName;
    }

    public String getSettingsFileName() {
        return SettingsFileName;
    }

    public int getCurrentMonth() {
        return currentMonth;
    }
    public int getCurrentYear() {
        return currentYear;
    }
    public void setCurrentDate(int currentYear, int currentMonth) {
        this.currentMonth = currentMonth;
        this.currentYear = currentYear;
    }

    public String getFileDir() {
        return FileDir;
    }

    public File getSettingsFile() {
        return SettingsFile;
    }
    public void setDateOfCreateBackup(String date) {
        userData.setDateOfCreateBackup(date);
    }
    public String getDateOfCreateBackup() {
        return userData.getDateOfCreateBackup();
    }
    public void setRate(double rate) {
        userData.setRate(rate);
    }
    public double getRatePerHour() {
        if (userData != null) {
            return userData.getRate();
        } else {
            return 1.0;
        }
    }
}

