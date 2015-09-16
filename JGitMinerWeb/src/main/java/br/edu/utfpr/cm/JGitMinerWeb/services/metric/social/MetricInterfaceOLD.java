/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.utfpr.cm.JGitMinerWeb.services.metric.social;

import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxPairOfFiles;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxCoChangeMetrics;

/**
 *
 * @author geazzy
 */
public interface MetricInterfaceOLD {
    
    public void calculateMetric(AuxCoChangeMetrics pairOfFiles, EntityPullRequest pullRequest);
    
    public MetricResult getResult(AuxCoChangeMetrics pairOfFiles);
  
    public String getHeadCSV();
    
}
