/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.social;

import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxAllMetrics;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 *
 * @author geazzy
 */
public class DelegateMetric {

    LinkedHashSet<MetricInterface> metricList;

    public DelegateMetric() {
        metricList = new LinkedHashSet<>();
    }
    
    public void addMetric(MetricInterface metric){
        metricList.add(metric);
    }

    public void calculateAllMetrics(AuxAllMetrics pair){
        for (MetricInterface metric : metricList) {
            metric.calculateMetric(pair);
        }
    }
    
    public LinkedHashMap<String, Object> getResult(Long indexPairOfFiles){
        
        LinkedHashMap<String, Object> resultList = new LinkedHashMap<>();
        
        for (MetricInterface metric : metricList) {
            resultList.put(metric.getClass().getName(), metric.getResult(indexPairOfFiles));
        }
        return resultList;
    }

    public String getAllHeadCSV() {
        StringBuilder head = new StringBuilder();
        
        for (MetricInterface metric : metricList) {
            head.append(metric.getHeadCSV());
            head.append(";");
        }
        
        return head.toString();
    }
    
}
