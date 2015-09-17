/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityComment;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityUser;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author geazzy
 */
public class AuxMessagesRank {

    private List<EntityPullRequest> pullRequestList;
    private Map<EntityUser, Integer> rank;

    public AuxMessagesRank(List<EntityPullRequest> pullRequestList) {
        this.pullRequestList = pullRequestList;
        rank = new HashMap<>();
    }

    public void setRankValues() {
        for (EntityPullRequest entityPullRequest : pullRequestList) {

            addUserToRank(entityPullRequest.getUser());
          
            if (entityPullRequest.getIssue() == null) {
                System.out.println("null issue: " + entityPullRequest);
            } else {
                for (EntityComment comments : entityPullRequest.getIssue().getComments()) {
                    addUserToRank(comments.getUser());
                }
            }
        }
    }

    public Map<EntityUser, Integer> getRank() {
        return rank;
    }

    private void addUserToRank(EntityUser user) {

        if (rank.containsKey(user)) {

            rank.put(user, rank.get(user) + 1);
        } else {
            rank.put(user, 1);
        }
    }

}
