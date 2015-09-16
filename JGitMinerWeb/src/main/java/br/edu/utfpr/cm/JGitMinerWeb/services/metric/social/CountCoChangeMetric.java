/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.social;

import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxPairOfFiles;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxAllMetrics;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxCoChangeMetrics;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zanoni
 */
public class CountCoChangeMetric implements MetricInterface {

    private Map<Long, Integer> count;

    public CountCoChangeMetric() {
        this.count = new HashMap<>();
    }

    @Override
    public void calculateMetric(AuxAllMetrics pair) {
        Integer i = pair.getPullRequests_idList().size();

        if (i == 0) {
            i = 1;
        }
        count.put(pair.getId(), i);
    }

    @Override
    public MetricResult getResult(Long index) {
        return new MetricResult(count.get(index), null, null);
    }

    @Override
    public String getHeadCSV() {
        return "NumberOfCoChangeSUM;NumberOfCoChangeMEAN;NumberOfCoChangeEntropy";

    }

}
