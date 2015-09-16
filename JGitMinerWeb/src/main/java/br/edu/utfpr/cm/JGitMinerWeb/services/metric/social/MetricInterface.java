/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.utfpr.cm.JGitMinerWeb.services.metric.social;

import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxAllMetrics;

/**
 *
 * @author geazzy
 */
public interface MetricInterface {
    
    public void calculateMetric(AuxAllMetrics pair);
    
    public MetricResult getResult(Long indexPairOfFiles);
  
    public String getHeadCSV();
    
}
