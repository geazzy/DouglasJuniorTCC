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
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxPairOfFiles;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxAllMetrics;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxCoChangeMetrics;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxReputationRank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author zanoni
 */
public class ReputationCommentsMetric implements MetricInterface {

    private HashMap<Long, List<EntityUser>> result;
    private AuxReputationRank reputationRank;

    public ReputationCommentsMetric() {
        reputationRank = AuxReputationRank.getInstance();
        result = new HashMap<>();
    }

    @Override
    public void calculateMetric(AuxAllMetrics pairOfFile) {

        List<EntityUser> userSetListTemp = new ArrayList<>();
//        if (result.containsKey(pairOfFile.getAuxPairOfFiles())) {
//            userSetListTemp = result.get(pairOfFile.getAuxPairOfFiles());
//        } else {
//            userSetListTemp = new ArrayList<>();
//        }

        //EntityUser commitAuthor = pairOfFiles.getRepositoryCommitFile1().getComments();
        if (!pairOfFile.getRepoCommits().isEmpty()) {

            for (EntityRepositoryCommit repositoryCommit : pairOfFile.getRepoCommits()) {

                for (EntityCommitComment comments : repositoryCommit.getComments()) {

                    if (comments.getUser() != null) {
                        if (reputationRank.containCommitterInRank(comments.getUser())) {
                          //  System.out.println("O autor do comentario "
                           //         + comments.getUser().getLogin() + " está presente no toprank");
                            if (!userSetListTemp.contains(comments.getUser())) {
                                userSetListTemp.add(comments.getUser());
                            }
                        }
                    }
                }

            }
        }

        if (!pairOfFile.getPullRequests().isEmpty()) {

            for (EntityPullRequest pullRequest : pairOfFile.getPullRequests()) {
                try {
                    for (EntityComment comments : pullRequest.getIssue().getComments()) {

                        if (comments.getUser() != null) {
                            if (reputationRank.containCommitterInRank(comments.getUser())) {
                              //  System.out.println("O autor do comentario "
                              //          + comments.getUser().getLogin() + " está presente no toprank");
                                if (!userSetListTemp.contains(comments.getUser())) {
                                    userSetListTemp.add(comments.getUser());
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Erro");
                    System.out.println(e.toString());
                }
            }
        }
//
//        for (EntityComment comments : pullRequest.getIssue().getComments()) {
//
//            if (reputationRank.containCommitterInRank(comments.getUser())) {
//                System.out.println("O autor do comentario "
//                        + comments.getUser().getLogin() + " está presente no toprank");
//                if (!userSetListTemp.contains(comments.getUser())) {
//                    userSetListTemp.add(comments.getUser());
//                }
//            }
//        }

        result.put(pairOfFile.getId(), userSetListTemp);
    }

    @Override
    public MetricResult getResult(Long indexPairOfFile
    ) {
        return new MetricResult(result.get(indexPairOfFile).size(), null,
                null);
    }

    @Override
    public String getHeadCSV() {
        return "ReputationCommentsMetricSUM;ReputationCommentsMetricMEAN;ReputationCommentsMetricENTROPY";
    }

}
