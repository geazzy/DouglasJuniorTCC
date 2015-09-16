/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.social;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author geazzy
 */
public class Entropy {

    /**
     *
     * @param valueList list
     * @return Double value for Shannon entroy
     */
    public static Double calculateNormalizedShannonEntropy(List<Double> valueList) {

        List<Double> normalizedValues = normalizedValues(valueList);
        Integer logBase = normalizedValues.size();

        Double result = 0.0;

        for (Double value : normalizedValues) {

            result += -(value * logBaseFunction(value, logBase));
        }

        return result;
    }

    private static List<Double> normalizedValues(List<Double> values) {

        Double sum = 0.0;
        List<Double> tempList = new ArrayList<>();

        for (Double value : values) {
            sum += value;
        }

        for (Double value : values) {
            tempList.add(value / sum);
        }

        return tempList;
    }

    public static Integer calculateNormalizedShannonEntropyForInteger(List<Integer> valueList) {

        List<Integer> normalizedValues = normalizedIntegerValues(valueList);
        Integer logBase = normalizedValues.size();

        Double result = 0.0;

        for (Integer value : normalizedValues) {

            result += -(value * logBaseFunction(value, logBase));
        }

        return result.intValue();
    }

    private static List<Integer> normalizedIntegerValues(List<Integer> values) {

        Integer sum = 0;
        List<Integer> tempList = new ArrayList<>();

        for (Integer value : values) {
            sum += value;
        }

        for (Integer value : values) {
            tempList.add(value / sum);
        }

        return tempList;
    }

    public static double logBaseFunction(double num, double base) {
        return (Math.log(num) / Math.log(base));
    }

}
