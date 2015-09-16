/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.social;

/**
 *
 * @author geazzy
 */
public class MetricResult {

    private Integer sumValue;
    private Double meanValue;
    private Double entropyValue;

    public MetricResult(Integer sumValue, Double meanValue, Double entropyValue) {
        this.sumValue = sumValue;
        this.meanValue = meanValue;
        this.entropyValue = entropyValue;
    }

//    public MetricResult(Integer sumValue) {
//        this.sumValue = sumValue;
//        meanValue = null;
//        entropyValue = null;
//    }
//
//    public MetricResult(Integer sumValue, Double meanValue) {
//        this.sumValue = sumValue;
//        this.meanValue = meanValue;
//        entropyValue = null;
//    }
    public Integer getSumValue() {
        return sumValue;
    }

    public Double getMeanValue() {
        return meanValue;
    }

    public Double getEntropyValue() {
        return entropyValue;
    }

//    
//    public void setResult(Integer sum, Integer mean, Double entropy){
//         this.sumValue = sum;
//         this.meanValue = mean;
//         this.entropyValue = entropy;
//    }
//    
    @Override
    public String toString() {

//        if (sumValue != null && meanValue != null && entropyValue != null) {
//            return "sumValue=" + sumValue + ";meanValue=" + meanValue + 
//                    ";entropyValue=" + entropyValue;
//        }
//        if (sumValue != null && meanValue != null) {
//            return "sumValue=" + sumValue + ";meanValue=" + meanValue;
//        }
//
//        return "sumValue=" + sumValue;
//        return "sumValue=" + sumValue + ";meanValue=" + meanValue
//                + ";entropyValue=" + entropyValue;
        
        return sumValue + ";" + meanValue + ";" + entropyValue;
    }

}
