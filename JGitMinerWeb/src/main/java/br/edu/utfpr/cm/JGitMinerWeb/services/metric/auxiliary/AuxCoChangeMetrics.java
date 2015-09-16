/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.count.PeriodOfDay;
import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxPairOfFiles;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 *
 * @author geazzy
 */
public class AuxCoChangeMetrics {

    private AuxPairOfFiles auxPairOfFiles;
    private EntityRepositoryCommit repositoryCommitFile1;
    private EntityRepositoryCommit repositoryCommitFile2;
    private DateTime dateFile1;
    private DateTime dateFile2;
    private EntityPullRequest pullRequest;

    public AuxCoChangeMetrics(AuxPairOfFiles auxCoChanged, GenericDao dao) {
        
        this.pullRequest = AuxPullRequest.getPullRequestByID(dao, auxCoChanged.getIdPullRequest());
        this.auxPairOfFiles = auxCoChanged;
        this.repositoryCommitFile1 = AuxRepositoryCommit.getRepositoryCommitBySha(dao,
                this.auxPairOfFiles.getshaCommitFile1());
        this.repositoryCommitFile2 = AuxRepositoryCommit.getRepositoryCommitBySha(dao,
                this.auxPairOfFiles.getshaCommitFile2());
        
        //List<EntityRepositoryCommit> list = Lists.newArrayList(pullRequest.getRepositoryCommits());
       
        this.dateFile1 = new DateTime(repositoryCommitFile1.getCommit().getAuthor().getDateCommitUser(), DateTimeZone.UTC);
        this.dateFile2 = new DateTime(repositoryCommitFile2.getCommit().getAuthor().getDateCommitUser(), DateTimeZone.UTC);

    }

    public EntityRepositoryCommit getRepositoryCommitFile1() {
        return repositoryCommitFile1;
    }

    public EntityRepositoryCommit getRepositoryCommitFile2() {
        return repositoryCommitFile2;
    }

    
    
//    public void setPeriodOfDay() {
//    
// 
//
//        System.out.println("url arq1: " + repositoryCommitFile1.getCommit().getUrl());
//        System.out.println("hora do dia arq 1: " + dateFile1.getHourOfDay());
//        System.out.println("periodo do dia: " + getPedriodOfDayFile1());
//
//        System.out.println("url arq2: " + repositoryCommitFile2.getCommit().getUrl());
//        System.out.println("hora do dia arq 2: " + dateFile2.getHourOfDay());
//        System.out.println("periodo do dia: " + getPedriodOfDayFile2());
//
//    }

    
    public AuxPairOfFiles getAuxPairOfFiles() {
        return auxPairOfFiles;
    }
    

    public PeriodOfDay getPedriodOfDayFile1() {
        return getPeriodOfDay(dateFile1);
    }

    public PeriodOfDay getPedriodOfDayFile2() {
        return getPeriodOfDay(dateFile2);
    }

    public Integer getDayOfWeekFile1() {
        return getDayOfWeek(dateFile1);
    }

    public Integer getDayOfWeekFile2() {
        return getDayOfWeek(dateFile2);
    }
  
    public Integer getDayOfMonthFile1() {
        return getDayOfMonth(dateFile1);
    }

    public Integer getDayOfMonthFile2() {
        return getDayOfMonth(dateFile2);
    }

    public Integer getMonthOfModFile1() {
        return getMonth(dateFile1);
    }

    public Integer getMonthOfModFile2() {
        return getMonth(dateFile1);
    }

    private Integer getDayOfWeek(DateTime dateFile) {
        return dateFile.getDayOfWeek();
    }

    private Integer getDayOfMonth(DateTime dateFile) {
        return dateFile.getDayOfMonth();
    }

    private Integer getMonth(DateTime dateFile) {
        return dateFile.getMonthOfYear();
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

    
    public Integer getNumArqMod(){
        
        return this.repositoryCommitFile1.getFiles().size();
    }
    
    public void getNumDevModSameFile(){
        //quantidade de dev diferentes que modificaram o msmo arquivo.
    }

    public EntityPullRequest getPullRequest() {
        return pullRequest;
    }

    public void setPullRequest(EntityPullRequest pullRequest) {
        this.pullRequest = pullRequest;
    }

    @Override
    public String toString() {
        return auxPairOfFiles.getFile1()+";"+auxPairOfFiles.getFile2();
    }

    public DateTime getDateFile1() {
        return dateFile1;
    }

    public DateTime getDateFile2() {
        return dateFile2;
    }
 
    
    
}
