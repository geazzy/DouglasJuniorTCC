/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.social;

import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityUser;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxAllMetrics;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author zanoni
 */
public class CollaboratorsMetric implements MetricInterface {

    HashMap<Long, Set<EntityUser>> result;

    public CollaboratorsMetric() {

        result = new HashMap<>();
        
    }

    @Override
    public void calculateMetric(AuxAllMetrics pair) {

        //     Integer quantidadeCollab = 0;
        for (EntityRepositoryCommit repoCommit : pair.getRepoCommits()) {
            Set<EntityUser> collaborators = repoCommit.getRepository().getCollaborators();

            if (repoCommit.getAuthor() != null && collaborators.contains(repoCommit.getAuthor())) {
                setResult(pair.getId(), repoCommit.getAuthor());

            }
        }

        // result.put(pair.getId(), quantidadeCollab);
    }

    @Override
    public MetricResult getResult(Long indexPairOfFiles) {

        if (result.get(indexPairOfFiles) != null) {
            return new MetricResult(result.get(indexPairOfFiles).size(), null, null);
        } else {
            return new MetricResult(0, null, null);
        }

    }

    @Override
    public String getHeadCSV() {
        return CollaboratorsMetric.class.getSimpleName() + "SUM;"
                + CollaboratorsMetric.class.getSimpleName() + "MEAN;"
                + CollaboratorsMetric.class.getSimpleName() + "ENTROPY";
    }

    private void setResult(Long id, EntityUser author) {
        
        //if (author != null) {

            Set<EntityUser> users = result.get(id);
            if (users == null) {
                users = new HashSet<>();
            }
            users.add(author);
            result.put(id, users);
       // }
    }

}
