/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.social;

import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxPairOfFiles;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxCoChangeMetrics;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 *
 * @author geazzy
 */
public class DelegateMetricOLD {

    LinkedHashSet<MetricInterfaceOLD> metricList;

    public DelegateMetricOLD() {
        metricList = new LinkedHashSet<>();
    }
    
    public void addMetric(MetricInterfaceOLD metric){
        metricList.add(metric);
    }

    public void calculateAllMetrics(AuxCoChangeMetrics pairOfFiles, EntityPullRequest pullRequest){
        for (MetricInterfaceOLD metric : metricList) {
            metric.calculateMetric(pairOfFiles, pullRequest);
        }
    }
    
    public LinkedHashMap<String, Object> getResult(AuxCoChangeMetrics pairOfFiles){
        
        LinkedHashMap<String, Object> resultList = new LinkedHashMap<>();
        
        for (MetricInterfaceOLD metric : metricList) {
            resultList.put(metric.getClass().getName(), metric.getResult(pairOfFiles));
        }
        return resultList;
    }

    public String getAllHeadCSV() {
        StringBuilder head = new StringBuilder();
        
        for (MetricInterfaceOLD metric : metricList) {
            head.append(metric.getHeadCSV());
            head.append(";");
        }
        
        return head.toString();
    }
    
}
