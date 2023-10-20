package com.example.myworkedtime;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class HoursWriterClass {
    private static HoursWriterClass instance;
    private Gson gsonManager;
    private ArrayList<MonthProfileClass> listOfMonthProfiles;
    private static File externalProfileFile;
    private HoursWriterClass(File externalFile) {
        this.gsonManager = new GsonBuilder().setPrettyPrinting().create();
        this.listOfMonthProfiles = new ArrayList<>();
        this.externalProfileFile = externalFile;
        loadDataFromFile(null);
    }
    public synchronized static HoursWriterClass getInstance(File externalFile) {
        if (instance == null) {
            instance = new HoursWriterClass(externalFile);
        }
        return instance;
    }
    public synchronized static HoursWriterClass getInstance() {
        if (instance == null) {
            instance = new HoursWriterClass(getFile());
        }
        return instance;
    }

    public void addWorkedDay(int year, int month, int day, String startTime, String endTime){
        monthIsExist(year, month).createWorkedDay(day, startTime, endTime);
        Collections.sort(listOfMonthProfiles);
        saveDataOnFile(null);
    }
    public void saveBackupOfProfile() throws IOException {
        if(!SettingsWriterClass.getInstance().getBackupOfProfile().exists()) SettingsWriterClass.getInstance().createBackupFile();
        saveDataOnFile(SettingsWriterClass.getInstance().getBackupOfProfile());
    }
    public void loadBackBackupOfProfile() throws IOException {
        if(SettingsWriterClass.getInstance().getBackupOfProfile().exists()) {
            loadDataFromFile(SettingsWriterClass.getInstance().getBackupOfProfile());
        }else Log.e(TAG, "loadBackBackupOfProfile: backup file is not exist");
    }
    public void deleteBackupOfProfile() throws IOException {
        if(SettingsWriterClass.getInstance().getBackupOfProfile().exists()) SettingsWriterClass.getInstance().getBackupOfProfile().delete();
    }
    public long calculateWeightOfBackBackup() throws IOException {
        long weight = 0;
        File TMPFile = SettingsWriterClass.getInstance().getBackupOfProfile();
        if(!TMPFile.exists() || !TMPFile.isFile() || !TMPFile.canRead()){
            weight = TMPFile.length();
        }return weight;
    }
    public String getCalculatedWeightOfBackBackup() throws IOException {
        long sizeInBytes = calculateWeightOfBackBackup();
        if (sizeInBytes <= 0) {
            return "0b";
        }
        final String[] units = new String[]{"b", "kb", "mb"};
        int digitGroups = (int) (Math.log10(sizeInBytes) / Math.log10(1024));
        return String.format("%.1f%s", sizeInBytes / Math.pow(1024, digitGroups), units[digitGroups]);
    }
    private MonthProfileClass monthIsExist(int year, int month){
        if(month<1 || month>12) {
            Log.i(TAG, "monthIsExist: month is not exist");
            return null;
        }
        MonthProfileClass tmpMonth = null;
        for (MonthProfileClass h : listOfMonthProfiles) {
            if ((month == h.getMonth()) && (year == h.getYear())) {
                tmpMonth = h;
                break;
            }
        }
        if (tmpMonth == null) {
            tmpMonth = new MonthProfileClass(year, month);
            listOfMonthProfiles.add(tmpMonth);
        }
        return tmpMonth;
    }
    public DayProfileClass dayIsExist(int year, int month, int day){
        return monthIsExist(year,month).dayIsExist(day);
    }
    private void saveDataOnFile(File file){
        File externalFile = null;
        if(file == null) externalFile = getFile();
        else externalFile = file;
        try (/*FileInputStream FIS = new FileInputStream(getFile())*/
                FileWriter FW = new FileWriter(externalFile)){
            gsonManager.toJson(listOfMonthProfiles,FW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadDataFromFile(File file){
        File externalFile = null;
        if(file == null) externalFile = getFile();
        else externalFile = file;

        ArrayList<MonthProfileClass> listOfMonthTMP = new ArrayList<>();

        if (isFileEmpty(externalFile)) {
            Log.i(TAG, "loadDataFromFile: file is null");
            return;
        }
        try (/*FileOutputStream FOS = new FileOutputStream(getFile())*/
                FileReader FR = new FileReader(externalFile)){
            MonthProfileClass[] tabOfMonthTMP = gsonManager.fromJson(FR,MonthProfileClass[].class);
            listOfMonthTMP.addAll(Arrays.asList(tabOfMonthTMP));
            this.listOfMonthProfiles = listOfMonthTMP;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void clearDayOnFile(int year, int month, int day){
        if (dayIsExist(year,month,day)==null) {
            Log.i(TAG, "getAllworkedTime: month is null");
        } else{
            DayProfileClass dayTMP = dayIsExist(year, month, day);
            monthIsExist(year, month).deleteWorkedDay(dayTMP);
            saveDataOnFile(null);
        }
    }
    public void clearMonthOnFile(int year, int month){
        MonthProfileClass monthProfileTMP = monthIsExist(year, month);
        if (monthProfileTMP != null) {
            listOfMonthProfiles.remove(monthProfileTMP);
            saveDataOnFile(null);
        }
    }
    private boolean isFileEmpty(File file) {
        return file.exists() && file.length() == 0;
    }
    public HoursClass getAllworkedTime(int year, int month) {
        if (monthIsExist(year, month)==null) {
            Log.i(TAG, "getAllworkedTime: month is null");
            return new HoursClass();
        }
        else {
            Log.i(TAG, "getAllworkedTime: all is ok");
            return monthIsExist(year, month).getAllworkedTime();
        }
    }
    /*public HoursClass getWorkedTimeOnDay(int month, int day) {
        if ((monthIsExist(month) == null) && (monthIsExist(month).dayIsExist(day))==null) {
            Log.i(TAG, "getWorkedTimeOnDay: day is not exist");
            return new HoursClass();
        }
        else {
            return monthIsExist(month).dayIsExist(day).getWorkedHours();
        }
    }*/
    public HoursClass getWorkedTimeOnDay(int year, int month, int day) {
        MonthProfileClass monthProfile = monthIsExist(year, month);
        if (monthProfile == null) {
            Log.i(TAG, "getWorkedTimeOnDay: month is not exist");
            return new HoursClass();
        }

        DayProfileClass dayProfile = monthProfile.dayIsExist(day);
        if (dayProfile == null) {
            Log.i(TAG, "getWorkedTimeOnDay: day is not exist");
            return new HoursClass();
        }

        return dayProfile.getWorkedHours();
    }
    public ArrayList<CalendarDay> getAllWorkedDayOnMonth(int year, int month){
        ArrayList<CalendarDay> workedDaysList = new ArrayList<>();
        ArrayList<DayProfileClass> monthProfile = new ArrayList<>();
        if(monthIsExist(year, month)==null) return workedDaysList;
        else {
            monthProfile = monthIsExist(year, month).getWorkedTimeList();
            Log.e(TAG, "getAllWorkedDayOnMonth: " + monthIsExist(year, month).getWorkedTimeList());//error воно місяць найти не може пхд
            for (DayProfileClass h : monthProfile) {
                workedDaysList.add(CalendarDay.from(year,month,h.getDay()));
            }
        }
        return workedDaysList;
    }
    public String[] getShiftStartAndEndTime(int month, int day){
        MonthProfileClass MonthTMP = null;
        String[] StartAndEndTime = new String[2];
        for (MonthProfileClass h:listOfMonthProfiles) {
            if(h.getMonth()==month) MonthTMP = h;
        }
        if(MonthTMP==null) {
            //mb makeToast
            Log.e(TAG, "getStartAndEndWorkedTime: day is not exist");
            StartAndEndTime[0] = "00:00";
            StartAndEndTime[1] = "00:00";
        }
        else{
            StartAndEndTime[0] = MonthTMP.getStartAndEndWorkedTime(day)[0];
            StartAndEndTime[1] = MonthTMP.getStartAndEndWorkedTime(day)[1];
        }
        return StartAndEndTime;
    }
    //getters
    public static File getFile(){
        return externalProfileFile;
    }
    //setters
}