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
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxReputationRank;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author zanoni
 */
public class ReputationCommiterMetric implements MetricInterface {

    private HashMap<Long, List<EntityUser>> result;
    private AuxReputationRank reputationRank;

    public ReputationCommiterMetric() {
        result = new HashMap<>();
        reputationRank = AuxReputationRank.getInstance();
    }

    @Override
    public void calculateMetric(AuxAllMetrics pairOfFiles) {

        List<EntityUser> userSetListTemp = new ArrayList<>();
//        if (result.containsKey(pairOfFiles.getAuxPairOfFiles())) {
//            userSetListTemp = result.get(pairOfFiles.getAuxPairOfFiles());
//        } else {
//            userSetListTemp = new ArrayList<>();
//        }

        if (!pairOfFiles.getRepoCommits().isEmpty()) {
            for (EntityRepositoryCommit repositoryCommit : pairOfFiles.getRepoCommits()) {

                EntityUser commitAuthor = repositoryCommit.getAuthor();

                if (commitAuthor != null) {
                    if (reputationRank.containCommitterInRank(commitAuthor)) {
                        //System.out.println("O autor " + commitAuthor.getLogin() + " está presente no toprank");

                        if (!userSetListTemp.contains(commitAuthor)) {
                            userSetListTemp.add(commitAuthor);
                        }
                    }

                }
            }
        }

//        EntityUser commitAuthor = pairOfFiles.getRepositoryCommitFile1().getAuthor();
//
//        if (reputationRank.containCommitterInRank(commitAuthor)) {
//            System.out.println("O autor " + commitAuthor.getLogin() + " está presente no toprank");
//            if (!userSetListTemp.contains(commitAuthor)) {
//                userSetListTemp.add(commitAuthor);
//            }
//
//        }
        result.put(pairOfFiles.getId(), userSetListTemp);
    }

    @Override
    public MetricResult getResult(Long indexPairOfFiles) {
        return new MetricResult(result.get(indexPairOfFiles).size(), null,
                null);
    }

    @Override
    public String getHeadCSV() {
        return "ReputationMetricSUM;ReputationMetricMEAN;ReputationMetricENTROPY";
    }

}
