/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.count.DayOfMonthCount;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.count.DayOfWeekCount;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.count.MonthCount;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.count.PeriodOfDayCount;

/**
 *
 * @author geazzy
 */
public class AuxCountTimeMetrics {

    private Integer count;
    private PeriodOfDayCount periodOfDayCount;
    private DayOfWeekCount dayOfWeekCount;
    private DayOfMonthCount dayOfMonthCount;
    private MonthCount monthCount;

    public AuxCountTimeMetrics(Integer count, PeriodOfDayCount periodOfDayCount, DayOfWeekCount dayOfWeekCount, DayOfMonthCount dayOfMonthCount, MonthCount monthCount) {
        this.count = count;
        this.periodOfDayCount = periodOfDayCount;
        this.dayOfWeekCount = dayOfWeekCount;
        this.dayOfMonthCount = dayOfMonthCount;
        this.monthCount = monthCount;
    }

    public AuxCountTimeMetrics(PeriodOfDayCount periodOfDayCount, DayOfWeekCount dayOfWeekCount, DayOfMonthCount dayOfMonthCount, MonthCount monthCount) {
        count = null;
        this.periodOfDayCount = periodOfDayCount;
        this.dayOfWeekCount = dayOfWeekCount;
        this.dayOfMonthCount = dayOfMonthCount;
        this.monthCount = monthCount;
    }

    public Integer getCount() {
        return count;
    }

    public PeriodOfDayCount getPeriodOfDayCount() {
        return periodOfDayCount;
    }

    public DayOfWeekCount getDayOfWeekCount() {
        return dayOfWeekCount;
    }

    public DayOfMonthCount getDayOfMonthCount() {
        return dayOfMonthCount;
    }

    public MonthCount getMonthCount() {
        return monthCount;
    }

    @Override
    public String toString() {

        if (count == null) {
            return periodOfDayCount + ";" + dayOfWeekCount + ";"
                    + dayOfMonthCount + ";" + monthCount + ";";
        } else {
            return count + ";" + periodOfDayCount + ";" + dayOfWeekCount + ";"
                    + dayOfMonthCount + ";" + monthCount + ";";
        }

    }

}
