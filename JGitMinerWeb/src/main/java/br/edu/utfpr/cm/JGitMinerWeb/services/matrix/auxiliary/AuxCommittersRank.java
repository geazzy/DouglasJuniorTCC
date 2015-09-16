/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityUser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author geazzy
 */
public class AuxCommittersRank {

    private List<EntityPullRequest> pullRequestList;
    private Map<EntityUser, Integer> rank;

    public AuxCommittersRank(List<EntityPullRequest> pullRequestList) {
        this.pullRequestList = pullRequestList;
        rank = new HashMap<>();
    }

    public void setRankValues() {

        for (EntityPullRequest entityPullRequest : pullRequestList) {

            for (EntityRepositoryCommit repositoryCommit : entityPullRequest.getRepositoryCommits()) {
                
                if (repositoryCommit.getAuthor() != null) {
                    addUserToRank(repositoryCommit.getAuthor());
                }

            }

        }
    }

    private void addUserToRank(EntityUser user) {
        if (rank.containsKey(user)) {

            rank.put(user,
                    rank.get(user) + 1);
        } else {
            rank.put(user, 1);
        }
    }

    public Map<EntityUser, Integer> getRank() {
        return rank;
    }

}
