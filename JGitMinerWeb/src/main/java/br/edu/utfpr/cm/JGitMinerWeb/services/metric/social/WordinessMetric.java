/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.social;

import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityComment;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityCommitComment;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityUser;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.LuceneUtil;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxAllMetrics;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author zanoni
 */
public class WordinessMetric implements MetricInterface {

    HashMap<Long, List<Integer>> result;

    public WordinessMetric() {
        this.result = new HashMap<>();

    }

    //calculo por repocommit
    @Override
    public void calculateMetric(AuxAllMetrics pairOfFile) {

        List<EntityUser> userSetListTemp = new ArrayList<>();

        if (!pairOfFile.getRepoCommits().isEmpty()) {

            for (EntityRepositoryCommit repositoryCommit : pairOfFile.getRepoCommits()) {

                int wordiness = 0;

                for (EntityCommitComment comment : repositoryCommit.getComments()) {

                    wordiness += LuceneUtil.tokenizeString(Strings.nullToEmpty(comment.getBody())).size();

                }

                setResult(pairOfFile, wordiness);
            }
        }

        if (!pairOfFile.getPullRequests().isEmpty()) {

            for (EntityPullRequest pullRequest : pairOfFile.getPullRequests()) {

                int wordiness = 0;

                wordiness += LuceneUtil.tokenizeString(Strings.nullToEmpty(pullRequest.getBody())).size();
                 try{

                for (EntityComment comment : pullRequest.getIssue().getComments()) {

                    wordiness += LuceneUtil.tokenizeString(Strings.nullToEmpty(comment.getBody())).size();
                }
                 }catch(Exception e){
                     System.out.println("Erro");
                     System.out.println(e.toString());
                 }
                setResult(pairOfFile, wordiness);
            }
        }

    }

    private void setResult(AuxAllMetrics pairOfFile, int wordiness) {

        if (result.containsKey(pairOfFile.getId())) {
            List<Integer> temp = result.get(pairOfFile.getId());
            temp.add(wordiness);
            result.put(pairOfFile.getId(), temp);
        } else {
            result.put(pairOfFile.getId(), Lists.newArrayList(wordiness));
        }
    }

    @Override
    public MetricResult getResult(Long indexPairOfFiles) {

        return new MetricResult(sum(result.get(indexPairOfFiles)),
                mean(result.get(indexPairOfFiles)),
                entropyValue(result.get(indexPairOfFiles)));

    }

    @Override
    public String getHeadCSV() {
        return "WordinessMetricSUM;WordinessMetricMEAN;WordinessMetricENTROPY";
    }

    private Integer sum(List<Integer> list) {

        //System.out.println("list: " + list);
        if (list != null) {
            Integer temp = 0;
            for (Integer value : list) {

                temp += value;

            }
            return temp;
        } else {
            return null;
        }

    }

    private Double mean(List<Integer> list) {
        if (list != null) {
            Integer sum = 0;
            Integer count = 0;

            for (Integer value : list) {
                count++;
                sum += value;
            }

            if (count == 0) {
                return 0d;
            } else {
                return ((Integer) (sum / count)).doubleValue();
            }
        } else {
            return null;
        }
    }

    private Double entropyValue(List<Integer> list) {

        if (list != null) {

            List<Double> listDouble = new ArrayList<>();

            for (Integer value : list) {
                listDouble.add(value.doubleValue());

            }
            //    System.out.println("Doubles: " + listDouble.toString());
            Double resultEntropy = Entropy.calculateNormalizedShannonEntropy(listDouble);
            // System.out.println("Result: " + resultEntropy);
            return resultEntropy;
        }else{
            return null;
        }
        // return Entropy.calculateNormalizedShannonEntropyForInteger(result.get(pairOfFiles));
    }
}
