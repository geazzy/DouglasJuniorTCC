/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.count;

/**
 *
 * @author geazzy
 */
public class DayOfWeekCount {

    private Integer sundayCount;
    private Integer mondayCount;
    private Integer tuesdayCount;
    private Integer wednesdayCount;
    private Integer thursdayCount;
    private Integer fridayCount;
    private Integer saturdayCount;

    public DayOfWeekCount() {
        sundayCount = 0;
        mondayCount = 0;
        tuesdayCount = 0;
        wednesdayCount = 0;
        thursdayCount = 0;
        fridayCount = 0;
        saturdayCount = 0;

    }

    public void countDayOfWeek(Integer dayOfWeekFile) {
        switch (dayOfWeekFile) {
            case 1:
                sundayCount++;
                break;
            case 2:
                mondayCount++;
                break;
            case 3:
                tuesdayCount++;
                break;
            case 4:
                wednesdayCount++;
                break;
            case 5:
                thursdayCount++;
                break;
            case 6:
                fridayCount++;
                break;
            case 7:
                saturdayCount++;
                break;

        }
    }

    public Integer getSundayCount() {
        return sundayCount;
    }

    public Integer getMondayCount() {
        return mondayCount;
    }

    public Integer getTuesdayCount() {
        return tuesdayCount;
    }

    public Integer getWednesdayCount() {
        return wednesdayCount;
    }

    public Integer getThursdayCount() {
        return thursdayCount;
    }

    public Integer getFridayCount() {
        return fridayCount;
    }

    public Integer getSaturdayCount() {
        return saturdayCount;
    }

    @Override
    public String toString() {
        return  sundayCount + ";" + mondayCount + ";" + tuesdayCount + ";" + wednesdayCount + ";" 
                + thursdayCount + ";" + fridayCount + ";" + saturdayCount;
    }
    
        public static String getHeadCSV() {
        return "su;mo;tu;we;th;fr;sa;";
    }
    

}
