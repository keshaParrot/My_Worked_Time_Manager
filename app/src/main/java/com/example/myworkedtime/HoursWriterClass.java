package com.example.myworkedtime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class HoursWriterClass {
    private Gson gsonManager;
    private ArrayList<MonthProfileClass> listOfMonthProfiles;
    //private MonthProfileClass currentMonth;
    private File externalProfileFile;
    public HoursWriterClass(File externalFile) {
        this.gsonManager = new GsonBuilder().setPrettyPrinting().create();
        this.listOfMonthProfiles= new ArrayList<>();
        this.externalProfileFile = externalFile;
    }

    public void addWorkedDay(int month, int day, String startTime, String endTime){
        if (monthIsExist(month)!=null){
            monthIsExist(month).createWorkedDay(day, startTime, endTime);
            Collections.sort(listOfMonthProfiles);
            saveDataOnFile();
        }
    }
    private MonthProfileClass monthIsExist(int month){
        if(month<1 || month>12) {
            //log.i(month is not exist)
            return null;
        }
        MonthProfileClass tmpMonth = null;
        for (MonthProfileClass h : listOfMonthProfiles) {
            if (month == h.getMonth()) {
                tmpMonth = h;
                break;
            }
        }
        if (tmpMonth == null) {
            tmpMonth = new MonthProfileClass(month);
            listOfMonthProfiles.add(tmpMonth);
        }
        return tmpMonth;
    }
    //save data
    private void saveDataOnFile(){
        try (/*FileInputStream FIS = new FileInputStream(getFile())*/
                FileWriter FW = new FileWriter(getFile())){
            gsonManager.toJson(listOfMonthProfiles,FW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadDataFromFile(){
        ArrayList<MonthProfileClass> listOfMonthTMP = new ArrayList<>();
        try (/*FileOutputStream FOS = new FileOutputStream(getFile())*/
                FileReader FR = new FileReader(getFile())){
            MonthProfileClass[] tabOfMonthTMP = gsonManager.fromJson(FR,MonthProfileClass[].class);
            listOfMonthTMP.addAll(Arrays.asList(tabOfMonthTMP));
            this.listOfMonthProfiles = listOfMonthTMP;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void clearDayOnFile(int day, int month){

    }
    public void clearMonthOnFile(int month){

    }
    //getIndexOfMonth(){}
    public HoursClass getAllworkedTime(int month) {
        if (monthIsExist(month)==null) {
            //System.out.println("logi (HoursWriterClass): month is null");
            return new HoursClass();
        }
        else {
            //System.out.println("гийби шось мало би виводити");
            return monthIsExist(month).getAllworkedTime();
        }
    }

    public File getFile(){
        return externalProfileFile;
    }
}

