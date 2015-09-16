/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrix;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrixNode;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.CoChangedFileServices;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxPairOfFiles;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxCountTimeMetrics;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxCoChangeMetrics;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxTimeFinal;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.count.DayOfMonthCount;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.count.DayOfWeekCount;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.count.MonthCount;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.count.PeriodOfDayCount;
import br.edu.utfpr.cm.JGitMinerWeb.util.JsfUtil;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author geazzy
 */
public class TimeMetricServices extends AbstractMetricServices {

    List<AuxCoChangeMetrics> pairOfFileList;
    Map<AuxPairOfFiles, AuxCountTimeMetrics> coChangeCountMap;

    public TimeMetricServices(GenericDao dao, OutLog out) {
        super(dao, out);
        pairOfFileList = new ArrayList<>();
        coChangeCountMap = new HashMap<>();
    }

    public TimeMetricServices(GenericDao dao, EntityMatrix matrix, Map params, OutLog out) {
        super(dao, matrix, params, out);
        pairOfFileList = new ArrayList<>();
        coChangeCountMap = new HashMap<>();
    }

    @Override
    public void run() {

        if (getMatrix() == null && !getAvailableMatricesPermitted()
                .contains(getMatrix().getClassServicesName())) {
            throw new IllegalArgumentException("Selecione uma matriz gerada pelo Service: "
                    + getAvailableMatricesPermitted());
        }

        System.out.println("Selecionado matriz com " + getMatrix().getNodes().size() + " nodes.");

        setAuxCoChangedList();
//        
//        for (AuxCoChangeMetrics auxcochange : pairOfFileList) {
//            //System.out.println(auxcochange.getAuxPairOfFiles().getArq1()+"---"+auxcochange.getAuxPairOfFiles().getArq2()+"---"+
//             //       auxcochange.getAuxPairOfFiles().getshaCommitFile2()+"---"+auxcochange.getAuxPairOfFiles().getshaCommitFile2());
//            
//            for (int j = 0; j < pairOfFileList.size(); j++) {
//                    if (pairOfFileList.get(j).getAuxPairOfFiles().equals(auxcochange.getAuxPairOfFiles())) {
//                        
//                         System.out.println(pairOfFileList.get(j).getAuxPairOfFiles().getArq1()+"---"+pairOfFileList.get(j).getAuxPairOfFiles().getArq2()+"---"+
//                    pairOfFileList.get(j).getAuxPairOfFiles().getshaCommitFile2()+"---"+pairOfFileList.get(j).getAuxPairOfFiles().getshaCommitFile2());
//                    }
//            }
//        }

        countCoChanges();

        List<AuxTimeFinal> result = setResult();

        addToEntityMetricNodeList(result);

    }

    private void setAuxCoChangedList() throws NumberFormatException {

        for (EntityMatrixNode node : getMatrix().getNodes()) {
            String[] coluns = node.getLine().split(JsfUtil.TOKEN_SEPARATOR);
            //  System.out.println("linha: "+node.getLine());
            AuxPairOfFiles acc = new AuxPairOfFiles(Long.parseLong(coluns[0]), coluns[1], coluns[2], coluns[3], coluns[4]);
            //System.out.println(acc.getArq1()+";"+acc.getArq2()+";"+acc.getshaCommitFile1()+";"+acc.getshaCommitFile2());
            AuxCoChangeMetrics coChangeTemp = new AuxCoChangeMetrics(acc, dao);
            //    System.out.println(coChangeTemp);
            pairOfFileList.add(coChangeTemp);

        }
    }

    private void countCoChanges() {

        for (AuxCoChangeMetrics auxcochange : pairOfFileList) {
            int count = 0;
            PeriodOfDayCount periodOfDayCount = new PeriodOfDayCount();
            DayOfWeekCount dayOfWeekCount = new DayOfWeekCount();
            DayOfMonthCount dayOfMonthCount = new DayOfMonthCount();
            MonthCount monthCount = new MonthCount();

            if (!coChangeCountMap.containsKey(auxcochange.getAuxPairOfFiles())) {

//                System.out.println("COUNT: " + count);
//                System.out.println("File 1 atual: " + auxcochange.getAuxPairOfFiles().getArq1()
//                        + " - " + auxcochange.getAuxPairOfFiles().getshaCommitFile1());
//                System.out.println("File 2 atual: " + auxcochange.getAuxPairOfFiles().getArq2()
//                        + " - " + auxcochange.getAuxPairOfFiles().getshaCommitFile2());
                for (int j = 0; j < pairOfFileList.size(); j++) {
                    if (pairOfFileList.get(j).getAuxPairOfFiles().equals(auxcochange.getAuxPairOfFiles())) {
                        count++;

//                        System.out.println("COUNT: " + count);
//                        System.out.println("File 1: " + pairOfFileList.get(j).getAuxPairOfFiles().getArq1()
//                                + " - " + auxcochange.getAuxPairOfFiles().getshaCommitFile1());
//                        System.out.println("File 2: " + pairOfFileList.get(j).getAuxPairOfFiles().getArq2()
//                                + " - " + auxcochange.getAuxPairOfFiles().getshaCommitFile2());
                        periodOfDayCount.countPeriodOfDay(pairOfFileList.get(j).getPedriodOfDayFile1());
                        // periodOfDayCount.countPeriodOfDay(auxcochange.getPedriodOfDayFile2());

                        dayOfWeekCount.countDayOfWeek(pairOfFileList.get(j).getDayOfWeekFile1());
                        //dayOfWeekCount.countDayOfWeek(auxcochange.getDayOfWeekFile1());

                        dayOfMonthCount.countDayOfMonth(pairOfFileList.get(j).getDayOfMonthFile1());
                        //dayOfMonthCount.countDayOfMonth(auxcochange.getDayOfMonthFile2());

                        monthCount.countMonth(pairOfFileList.get(j).getMonthOfModFile1());
                        //monthCount.countMonth(auxcochange.getMonthOfModFile2());

//                        System.out.println(pairOfFileList.get(j).getPullRequest().getNumber());
                    }
                }
                coChangeCountMap.put(auxcochange.getAuxPairOfFiles(),
                        new AuxCountTimeMetrics(count, periodOfDayCount, dayOfWeekCount, dayOfMonthCount, monthCount));
            }

        }

    }

    private List<AuxTimeFinal> setResult() {

        List<AuxTimeFinal> countList = new ArrayList<>();
        for (AuxPairOfFiles auxCoChanged : coChangeCountMap.keySet()) {
//            System.out.println("File 1 " + auxCoChanged.getArq1() + " date: " + AuxRepositoryCommit.getRepositoryCommitBySha(dao, auxCoChanged.getshaCommitFile1())
//                    .getCommit().getCommitter().getDateCommitUser()
//                    + " sha1 " + auxCoChanged.getshaCommitFile1());
//            System.out.println("File 1 URL: " + AuxRepositoryCommit.getRepositoryCommitBySha(dao, auxCoChanged.getshaCommitFile1())
//                    .getUrl());
//            System.out.println("File 2 " + auxCoChanged.getArq1() + "  date: " + AuxRepositoryCommit.getRepositoryCommitBySha(dao, auxCoChanged.getshaCommitFile2())
//                    .getCommit().getCommitter().getDateCommitUser()
//                    + " sha1 " + auxCoChanged.getshaCommitFile2());
//            System.out.println("File 2 URL: " + AuxRepositoryCommit.getRepositoryCommitBySha(dao, auxCoChanged.getshaCommitFile2())
//                    .getUrl());
            countList.add(new AuxTimeFinal(auxCoChanged, coChangeCountMap.get(auxCoChanged)));
        }
        return countList;
    }

    @Override
    public String getHeadCSV() {
        return "issue;file1;file2;sha1;sha2;count;"
                + PeriodOfDayCount.getHeadCSV() + DayOfWeekCount.getHeadCSV() + DayOfMonthCount.getHeadCSV()
                + MonthCount.getHeadCSV();
    }

    @Override
    public List<String> getAvailableMatricesPermitted() {
        return Arrays.asList(CoChangedFileServices.class.getName());
    }

}
