/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.social;

import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxPairOfFiles;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxAllMetrics;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxCoChangeMetrics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author geazzy
 */
public class NumberOfCommentsMetric implements MetricInterface {

    HashMap<Long, List<Integer>> result;

    public NumberOfCommentsMetric() {
        result = new HashMap<>();
    }

    @Override
    public void calculateMetric(AuxAllMetrics pair) {

        List<Integer> resultList = new ArrayList<>();
//        if (result.containsKey(pairOfFiles.getAuxPairOfFiles())) {
//            resultList = result.get(pairOfFiles.getAuxPairOfFiles());
//        } else {
//            resultList = new ArrayList<>();
//        }

        if (!pair.getPullRequests().isEmpty()) {
            for (EntityPullRequest pullRequest : pair.getPullRequests()) {
                try {
                    resultList.add(pullRequest.getIssue().getComments().size());
                } catch (Exception e) {
                    System.out.println("Erro ao processar: "+NumberOfCommentsMetric.class.getSimpleName());
                    System.out.println(e.toString());
                }

            }
        }

        if (!pair.getRepoCommits().isEmpty()) {
            for (EntityRepositoryCommit repositoryCommit : pair.getRepoCommits()) {
                
                try {
                    resultList.add(repositoryCommit.getComments().size());
                } catch (Exception e) {
                    System.out.println("Erro ao processar: "+NumberOfCommentsMetric.class.getSimpleName());
                    System.out.println(e.toString());
                }
            }
        }

        result.put(pair.getId(), resultList);

    }

    @Override
    public MetricResult getResult(Long indexPairOfFiles) {

        return new MetricResult(getSumValue(indexPairOfFiles), getMeanValue(indexPairOfFiles),
                getEntropyValue(indexPairOfFiles));
    }

    private Integer getSumValue(Long indexPairOfFiles) {

        Integer sum = 0;
        for (Integer value : result.get(indexPairOfFiles)) {
            sum += value;
        }
        return sum;
    }

    private Double getMeanValue(Long indexPairOfFiles) {

        Integer sum = 0;
        Integer count = 0;

        for (Integer value : result.get(indexPairOfFiles)) {
            count++;
            sum += value;
        }

        if (count == 0) {
            return 0d;
        } else {
            return ((Integer) (sum / count)).doubleValue();
        }
    }

    private Double getEntropyValue(Long indexPairOfFiles) {

        List<Double> listDouble = new ArrayList<>();

        for (Integer value : result.get(indexPairOfFiles)) {
            listDouble.add((Double) value.doubleValue());

        }
       // System.out.println("Doubles: " + listDouble.toString());
        Double resultEntropy = Entropy.calculateNormalizedShannonEntropy(listDouble);
        //System.out.println("Result: " + resultEntropy);
        return resultEntropy;

        // return Entropy.calculateNormalizedShannonEntropyForInteger(result.get(pairOfFiles));
    }

    @Override
    public String getHeadCSV() {
        return "NumberOfCommentsMetricSUM;NumberOfCommentsMetricMEAN;NumberOfCommentsMetricENTROPY";
    }

}
