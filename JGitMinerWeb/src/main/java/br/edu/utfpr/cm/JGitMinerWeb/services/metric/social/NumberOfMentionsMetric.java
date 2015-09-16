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
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.MentionsValidator;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxAllMetrics;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 *
 * @author geazzy
 */
public class NumberOfMentionsMetric implements MetricInterface {

    private HashMap<Long, List<Integer>> resultCoChangeList;

    public NumberOfMentionsMetric() {
        this.resultCoChangeList = new HashMap<>();
    }

    @Override
    public String getHeadCSV() {
        return NumberOfMentionsMetric.class.getSimpleName() + "SUM;"
                + NumberOfMentionsMetric.class.getSimpleName() + "MEAN;"
                + NumberOfMentionsMetric.class.getSimpleName() + "ENTROPY";
    }

    @Override
    public void calculateMetric(AuxAllMetrics pairOfFile) {

        List<Integer> tempList = new ArrayList<>();

        if (!pairOfFile.getPullRequests().isEmpty()) {
            for (EntityPullRequest pullRequest : pairOfFile.getPullRequests()) {
                try {
                    tempList.add(calculateNumberOfMentions(pullRequest.getIssue()));
                } catch (Exception e) {
                    System.out.println("Erro");
                    System.out.println(e.toString());
                }
            }
        }

        if (!pairOfFile.getRepoCommits().isEmpty()) {
            for (EntityRepositoryCommit repositoryCommit : pairOfFile.getRepoCommits()) {
                tempList.add(calculateNumberOfMentions(repositoryCommit.getComments()));
            }
        }

        resultCoChangeList.put(pairOfFile.getId(), tempList);

    }

    private Integer calculateNumberOfMentions(EntityIssue issue) throws Exception{
        Integer numberOfMentions = 0;

        numberOfMentions += MentionsValidator.mentionInString(Strings.nullToEmpty(issue.getBody()));

        for (EntityComment entityComment : issue.getComments()) {

            numberOfMentions += MentionsValidator.mentionInString(Strings.nullToEmpty(entityComment.getBody()));

        }

        return numberOfMentions;
    }

    private Integer calculateNumberOfMentions(Set<EntityCommitComment> comments) {
        Integer numberOfMentions = 0;

        for (EntityCommitComment comment : comments) {
            numberOfMentions += MentionsValidator.mentionInString(comment.getBody());
        }

        return numberOfMentions;
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
