/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.social;

import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityCommitFile;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxAllMetrics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author zanoni
 */
public class NumberOfChangeLinesMetric implements MetricInterface {

    HashMap<Long, List<Integer>> result;

    public NumberOfChangeLinesMetric() {
        result = new HashMap<>();
    }

    @Override
    public void calculateMetric(AuxAllMetrics pair) {

//        int additions = 0;
//        int deletions = 0;
//        int changes = 0;
        List<Integer> changeList = result.get(pair.getId());
        if (changeList == null) {
            changeList = new ArrayList<>();
        }

        for (EntityRepositoryCommit repositoryCommit : pair.getRepoCommits()) {
            for (EntityCommitFile commitFile : repositoryCommit.getFiles()) {

                if (commitFile.getFilename().equals(pair.getFile1())
                        || commitFile.getFilename().equals(pair.getFile2())) {

                    changeList.add(commitFile.getChanges());
//                    additions += commitFile.getAdditions();
//                    deletions += commitFile.getDeletions();
//                    changes  += commitFile.getChanges();
                }
            }
        }

        //System.out.println("changesLines: " + changeList);
//        System.out.println("deletionsFile: "+deletions);
//        System.out.println("changesFile: "+changes);

        result.put(pair.getId(), changeList);
    }

    @Override
    public MetricResult getResult(Long indexPairOfFiles) {
        return new MetricResult(sum(indexPairOfFiles),
                mean(indexPairOfFiles), 
                getEntropyValue(indexPairOfFiles));
    }

    @Override
    public String getHeadCSV() {
        return NumberOfChangeLinesMetric.class.getSimpleName() + "SUM;"
                + NumberOfChangeLinesMetric.class.getSimpleName() + "MEAN;"
                + NumberOfChangeLinesMetric.class.getSimpleName() + "ENTROPY";
    }

    private Integer sum(Long indexPairOfFiles) {

        List<Integer> additionsList = result.get(indexPairOfFiles);
        Integer sum = 0;

        if (additionsList == null) {
            return sum;
        } else {
            for (Integer value : additionsList) {
                sum += value;
            }
            return sum;
        }
    }

    private Double mean(Long indexPairOfFiles) {

        List<Integer> additionsList = result.get(indexPairOfFiles);
        Double sum = 0.0;

        if (additionsList == null) {
            return sum;
        } else {
            for (Integer value : additionsList) {
                sum += value;
            }
            return sum / additionsList.size();
        }
    }

    private Double getEntropyValue(Long indexPairOfFiles) {

        List<Double> listDouble = new ArrayList<>();

        for (Integer value : result.get(indexPairOfFiles)) {
            listDouble.add(value.doubleValue());

        }
       // System.out.println("Doubles: " + listDouble.toString());
        Double resultEntropy = Entropy.calculateNormalizedShannonEntropy(listDouble);
        //System.out.println("Result: " + resultEntropy);
        return resultEntropy;

        // return Entropy.calculateNormalizedShannonEntropyForInteger(result.get(pairOfFiles));
    }

}
