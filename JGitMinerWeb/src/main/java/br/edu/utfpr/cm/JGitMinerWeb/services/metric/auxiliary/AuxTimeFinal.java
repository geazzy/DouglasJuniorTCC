/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxPairOfFiles;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.count.DayOfMonthCount;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.count.DayOfWeekCount;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.count.MonthCount;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.count.PeriodOfDayCount;

/**
 *
 * @author geazzy
 */
public class AuxTimeFinal {

    private Integer manha;
    private Integer tarde;
    private Integer noite;
    private Integer madrugada;

    private Integer sunday;
    private Integer monday;
    private Integer tuesday;
    private Integer thursday;
    private Integer wednesday;
    private Integer friday;
    private Integer saturday;

    private Integer week1;
    private Integer week2;
    private Integer week3;
    private Integer week4;

    private Integer jan;
    private Integer fev;
    private Integer mar;
    private Integer apr;
    private Integer may;
    private Integer jun;
    private Integer jul;
    private Integer ago;
    private Integer sep;
    private Integer oct;
    private Integer nov;
    private Integer dec;
    private AuxPairOfFiles auxCoChanged;
    private AuxCountTimeMetrics auxCountTimeMetrics;

    public AuxTimeFinal() {

        manha = 0;
        tarde = 0;
        noite = 0;
        madrugada = 0;

        sunday = 0;
        monday = 0;
        tuesday = 0;
        thursday = 0;
        wednesday = 0;
        friday = 0;
        saturday = 0;

        week1 = 0;
        week2 = 0;
        week3 = 0;
        week4 = 0;

        jan = 0;
        fev = 0;
        mar = 0;
        apr = 0;
        may = 0;
        jun = 0;
        jul = 0;
        ago = 0;
        sep = 0;
        oct = 0;
        nov = 0;
        dec = 0;

    }

    public AuxTimeFinal(AuxPairOfFiles auxCoChanged, AuxCountTimeMetrics auxCountTimeMetrics) {
        this.auxCoChanged = auxCoChanged;
        this.auxCountTimeMetrics = auxCountTimeMetrics;

    }

    public void countPeriodOfDay(PeriodOfDayCount periodOfDayCount) {
        manha += periodOfDayCount.getManhaCount();
        tarde += periodOfDayCount.getTardeCount();
        noite += periodOfDayCount.getNoiteCount();
        madrugada += periodOfDayCount.getMadrugadaCount();
    }

    public void countDayOfWeek(DayOfWeekCount dayOfWeekCount) {

        sunday += dayOfWeekCount.getSundayCount();
        monday += dayOfWeekCount.getMondayCount();
        tuesday += dayOfWeekCount.getTuesdayCount();
        thursday += dayOfWeekCount.getThursdayCount();
        wednesday += dayOfWeekCount.getWednesdayCount();
        friday += dayOfWeekCount.getFridayCount();
        saturday += dayOfWeekCount.getSaturdayCount();

    }

    public void countDayOfMonth(DayOfMonthCount dayOfMonthCount) {
        week1 += dayOfMonthCount.getWeek1();
        week2 += dayOfMonthCount.getWeek2();
        week3 += dayOfMonthCount.getWeek3();
        week4 += dayOfMonthCount.getWeek4();
    }

    public void countMonthCount(MonthCount monthCount) {
        jan += monthCount.getJan();
        fev += monthCount.getFev();
        mar += monthCount.getMar();
        apr += monthCount.getApr();
        may += monthCount.getMay();
        jun += monthCount.getJun();
        jul += monthCount.getJul();
        ago += monthCount.getAgo();
        sep += monthCount.getSep();
        oct += monthCount.getOct();
        nov += monthCount.getNov();
        dec += monthCount.getDec();

    }

    @Override
    public String toString() {
        return manha + ";" + tarde + ";" + noite + ";" + madrugada + ";"
                + sunday + ";" + monday + ";" + tuesday + ";" + thursday + ";"
                + wednesday + ";" + friday + ";" + saturday + ";"
                + week1 + ";" + week2 + ";" + week3 + ";" + week4 + ";"
                + jan + ";" + fev + ";" + mar + ";" + apr + ";" + may + ";"
                + jun + ";" + jul + ";" + ago + ";" + sep + ";" + oct + ";"
                + nov + ";" + dec + ';';
    }

    public static String headCSV() {
        return "manha;tarde;noite;madrugada;"
                + "sunday;monday;tuesday;thursday;"
                + "wednesday;friday;saturday;"
                + "week1;week2;week3;week4;"
                + "jan;fev;mar;apr;may;"
                + "jun;jul;ago;sep;oct;"
                + "nov;dec";
    }

}
