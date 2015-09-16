/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.social;

import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxAllMetrics;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author zanoni
 */
public class StatusMetric implements MetricInterface {

    HashMap<Long, Integer> result;

    public StatusMetric() {
        result = new HashMap<>();
    }

    @Override
    public void calculateMetric(AuxAllMetrics pair) {

        Set<String> quantidadeStatus = new HashSet<>();

        for (EntityPullRequest entityPullRequest : pair.getPullRequests()) {
            
            quantidadeStatus.add(entityPullRequest.getStatePullRequest());
        }
        
        result.put(pair.getId(), quantidadeStatus.size());
    }

    @Override
    public MetricResult getResult(Long indexPairOfFiles) {
        return new MetricResult(result.get(indexPairOfFiles), null, null);
    }

    @Override
    public String getHeadCSV() {
        return StatusMetric.class.getSimpleName() + "SUM;"
                + StatusMetric.class.getSimpleName() + "MEAN;"
                + StatusMetric.class.getSimpleName() + "ENTROPY";
    }

}
