/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.social;

import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityUser;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxPairOfFiles;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxAllMetrics;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxCoChangeMetrics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author zanoni
 */
public class NumberOfDevelopers implements MetricInterface {

    private HashMap<Long, List<EntityUser>> result;

    public NumberOfDevelopers() {
        result = new HashMap<>();
    }

    @Override
    public void calculateMetric(AuxAllMetrics pairOfFile) {

        List<EntityUser> userSetListTemp = new ArrayList<>();
//        if (result.containsKey(pairOfFiles.getAuxPairOfFiles())) {
//            userSetListTemp = result.get(pairOfFiles.getAuxPairOfFiles());
//        } else {
//            userSetListTemp = new ArrayList<>();
//        }
        if (!pairOfFile.getRepoCommits().isEmpty()) {
            for (EntityRepositoryCommit repositoryCommit : pairOfFile.getRepoCommits()) {
                EntityUser commitAuthor = repositoryCommit.getAuthor();

                if (!userSetListTemp.contains(commitAuthor)) {
                    userSetListTemp.add(commitAuthor);
                }
            }
        }

        result.put(pairOfFile.getId(), userSetListTemp);
    }

    @Override
    public MetricResult getResult(Long index) {

//        System.out.println("pair: " + index.getRepositoryCommitFile1());
        return new MetricResult(result.get(index).size(), null,
                null);
    }

    @Override
    public String getHeadCSV() {
        return NumberOfDevelopers.class.getSimpleName() + "SUM;"
                + NumberOfDevelopers.class.getSimpleName() + "MEAN;"
                + NumberOfDevelopers.class.getSimpleName() + "ENTROPY";
    }

}
