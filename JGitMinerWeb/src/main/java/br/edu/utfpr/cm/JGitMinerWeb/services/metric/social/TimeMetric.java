/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.social;

import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxAllMetrics;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxCountTimeMetrics;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxDao;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxTimeFinal;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.count.DayOfMonthCount;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.count.DayOfWeekCount;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.count.MonthCount;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.count.PeriodOfDay;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.count.PeriodOfDayCount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 *
 * @author zanoni
 */
public class TimeMetric {

    private HashMap<Long, List<AuxCountTimeMetrics>> result;

    public TimeMetric() {
        result = new HashMap<>();
    }

    //public void calculateMetric(AuxCoChangeMetrics pairOfFiles, EntityPullRequest pullRequest) {
    public void calculateMetric(AuxAllMetrics pairOfFile, DateTime being, DateTime end) {

        List<AuxCountTimeMetrics> timeMetricsList = new ArrayList<>();
//        if (result.containsKey(pairOfFiles.getAuxPairOfFiles())) {
//            timeMetricsList = result.get(pairOfFiles.getAuxPairOfFiles());
//        } else {
//            timeMetricsList = new ArrayList<>();
//        }

        PeriodOfDayCount periodOfDayCount = new PeriodOfDayCount();
        DayOfWeekCount dayOfWeekCount = new DayOfWeekCount();
        DayOfMonthCount dayOfMonthCount = new DayOfMonthCount();
        MonthCount monthCount = new MonthCount();

        // if (isBetweenDate(pairOfFiles.getDateFile1(), being, end)) {
        if (!pairOfFile.getRepoCommits_idList().isEmpty()) {

            for (EntityRepositoryCommit repoCommit : pairOfFile.getRepoCommits()) {

                DateTime repoCommitDate = new DateTime(repoCommit.getCommit().getAuthor().getDateCommitUser(), DateTimeZone.UTC);
                
                if (isBetweenDate(repoCommitDate, being, end)) {
                    periodOfDayCount.countPeriodOfDay(getPeriodOfDay(repoCommitDate));
                    dayOfWeekCount.countDayOfWeek(repoCommitDate.getDayOfWeek());
                    dayOfMonthCount.countDayOfMonth(repoCommitDate.getDayOfMonth());
                    monthCount.countMonth(repoCommitDate.getMonthOfYear());
                }
            }
        }

//        periodOfDayCount.countPeriodOfDay(pairOfFiles.getPedriodOfDayFile1());
//        dayOfWeekCount.countDayOfWeek(pairOfFiles.getDayOfWeekFile1());
//        dayOfMonthCount.countDayOfMonth(pairOfFiles.getDayOfMonthFile1());
//        monthCount.countMonth(pairOfFiles.getMonthOfModFile1());
        AuxCountTimeMetrics timeMetrics = new AuxCountTimeMetrics(
                periodOfDayCount, dayOfWeekCount, dayOfMonthCount, monthCount);

        timeMetricsList.add(timeMetrics);
        //  }

        result.put(pairOfFile.getId(), timeMetricsList);

    }

    public AuxTimeFinal getResult(Long indexPairOfFiles) {

        AuxTimeFinal timeFinal = new AuxTimeFinal();

        for (AuxCountTimeMetrics auxCountTimeMetrics : result.get(indexPairOfFiles)) {

            timeFinal.countPeriodOfDay(auxCountTimeMetrics.getPeriodOfDayCount());

            timeFinal.countDayOfWeek(auxCountTimeMetrics.getDayOfWeekCount());

            timeFinal.countDayOfMonth(auxCountTimeMetrics.getDayOfMonthCount());

            timeFinal.countMonthCount(auxCountTimeMetrics.getMonthCount());

        }
        return timeFinal;
    }

    public String getHeadCSV() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean isBetweenDate(DateTime dataVerificar, DateTime dataInicial, DateTime dataFinal) {

        if (dataVerificar.isAfter(dataInicial) && dataVerificar.isBefore(dataFinal)) {
            return true;
        } else {
            return false;
        }

    }

    private PeriodOfDay getPeriodOfDay(DateTime dateFile) {

        if (dateFile.getHourOfDay() >= 6 && dateFile.getHourOfDay() < 12) {
            return PeriodOfDay.MANHA;
        } else if (dateFile.getHourOfDay() >= 12 && dateFile.getHourOfDay() < 18) {
            return PeriodOfDay.TARDE;
        } else if (dateFile.getHourOfDay() >= 18 && dateFile.getHourOfDay() < 24) {
            return PeriodOfDay.NOITE;
        } else if (dateFile.getHourOfDay() >= 0 && dateFile.getHourOfDay() < 6) {
            return PeriodOfDay.MADRUGADA;
        } else {
            return null;
        }
    }

}
