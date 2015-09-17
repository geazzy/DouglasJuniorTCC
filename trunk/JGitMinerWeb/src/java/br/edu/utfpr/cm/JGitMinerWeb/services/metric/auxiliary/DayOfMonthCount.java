/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary;

/**
 *
 * @author geazzy
 */
public class DayOfMonthCount {

    private Integer week1;
    private Integer week2;
    private Integer week3;
    private Integer week4;

    public DayOfMonthCount() {
        this.week1 = 0;
        this.week2 = 0;
        this.week3 = 0;
        this.week4 = 0;
    }

    public void countDayOfMonth(Integer dayOfMonthFile) {
        if (isBetween(dayOfMonthFile, 1, 8)) {
            week1++;
        } else if (isBetween(dayOfMonthFile, 9, 16)) {
            week2++;
        } else if (isBetween(dayOfMonthFile, 17, 24)) {
            week3++;
        } else if (isBetween(dayOfMonthFile, 24, 31)) {
            week4++;
        }
    }

    private boolean isBetween(Integer dayOfMonthFile, int inicio, int fim) {
        return (dayOfMonthFile >= inicio && dayOfMonthFile <= fim);
    }

    public Integer getWeek1() {
        return week1;
    }

    public Integer getWeek2() {
        return week2;
    }

    public Integer getWeek3() {
        return week3;
    }

    public Integer getWeek4() {
        return week4;
    }

    @Override
    public String toString() {
        return week1 + ";" + week2 + ";" + week3 + ";" + week4;
    }

    public static String getHeadCSV() {
        return "w1;w2;w3;w4;";
    }

}
