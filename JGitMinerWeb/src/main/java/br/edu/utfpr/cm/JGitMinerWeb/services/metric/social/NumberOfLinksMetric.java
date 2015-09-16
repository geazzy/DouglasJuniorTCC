/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.social;

import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityComment;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityCommitComment;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityIssue;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxPairOfFiles;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.UrlValidator;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxAllMetrics;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxCoChangeMetrics;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 *
 * @author geazzy
 */
public class NumberOfLinksMetric implements MetricInterface {

    private HashMap<Long, List<Integer>> resultCoChangeList;

    public NumberOfLinksMetric() {
        this.resultCoChangeList = new HashMap<>();
    }

    @Override
    public String getHeadCSV() {
        return "NumberOfLinksMetricSUM;NumberOfLinksMetricMEAN;NumberOfLinksMetricEntropy";
    }

    @Override
    public void calculateMetric(AuxAllMetrics pairOfFile) {

        List<Integer> tempList = new ArrayList<>();
//        if (resultCoChangeList.containsKey(pairOfFile.getAuxPairOfFiles())) {
//            tempList = resultCoChangeList.get(pairOfFile.getAuxPairOfFiles());
//        } else {
//            tempList = new ArrayList<>();
//        }

        if (!pairOfFile.getPullRequests().isEmpty()) {
            for (EntityPullRequest pullRequest : pairOfFile.getPullRequests()) {
                try{
                tempList.add(calculateNumberOfLinks(pullRequest.getIssue()));
                }catch(Exception e){
                    System.out.println("Erro");
                    System.out.println(e.toString());
                }
            }
        }

        if (!pairOfFile.getRepoCommits().isEmpty()) {
            for (EntityRepositoryCommit repositoryCommit : pairOfFile.getRepoCommits()) {
                tempList.add(calculateNumberOfLinks(repositoryCommit.getComments()));
            }
        }

//        tempList.add(calculateNumberOfLinks(pullRequest.getIssue()));
//        tempList.add(calculateNumberOfLinks(pairOfFile.getRepositoryCommitFile1().getComments()));
        resultCoChangeList.put(pairOfFile.getId(), tempList);

//        System.out.println("issue: " + pullRequest.getIssue().getUrl() + " comentarios: "
//                + pullRequest.getIssue().getComments().size()
//                + "sha: " + pairOfFile.getAuxPairOfFiles().getshaCommitFile1());
    }

    private Integer calculateNumberOfLinks(EntityIssue issue) throws Exception{
        Integer numberOfLinks = 0;
 
        numberOfLinks += UrlValidator.urlInString(Strings.nullToEmpty(issue.getBody()));

        for (EntityComment entityComment : issue.getComments()) {

            numberOfLinks += UrlValidator.urlInString(Strings.nullToEmpty(entityComment.getBody()));

        }

        return numberOfLinks;
    }

    private Integer calculateNumberOfLinks(Set<EntityCommitComment> comments) {
        Integer numberOfLinks = 0;

        for (EntityCommitComment comment : comments) {
            numberOfLinks += UrlValidator.urlInString(comment.getBody());
        }

        return numberOfLinks;
    }

    @Override
    public MetricResult getResult(Long indexPairOfFiles) {

        return new MetricResult(getSumValue(indexPairOfFiles), getMeanValue(indexPairOfFiles),
                null);

    }

    private Integer getSumValue(Long indexPairOfFiles) {

        Integer sum = 0;
        for (Integer value : resultCoChangeList.get(indexPairOfFiles)) {
            sum += value;
        }
        return sum;
    }

    private Double getMeanValue(Long indexPairOfFiles) {

        Integer sum = 0;
        Integer count = 0;

        for (Integer value : resultCoChangeList.get(indexPairOfFiles)) {
            count++;
            sum += value;
        }

        if (count == 0) {
            return 0d;
        } else {
            return ((Integer) (sum / count)).doubleValue();
        }

    }

}
