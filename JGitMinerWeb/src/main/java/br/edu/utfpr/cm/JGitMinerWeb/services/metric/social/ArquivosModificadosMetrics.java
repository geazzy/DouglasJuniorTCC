/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.utfpr.cm.JGitMinerWeb.services.metric.social;

import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxPairOfFiles;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxAllMetrics;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxCoChangeMetrics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author zanoni
 */
public class ArquivosModificadosMetrics implements MetricInterface{

    private HashMap<Long, List<Integer>> result;

    public ArquivosModificadosMetrics() {
        result = new HashMap<>();
    }
    
    
    
    @Override
    public void calculateMetric(AuxAllMetrics pairOfFile) {
    
         List<Integer> numArqModPorCommit = new ArrayList<>();
//        if (result.containsKey(pairOfFile.getAuxPairOfFiles())) {
//            numArqModPorCommit = result.get(pairOfFile.getAuxPairOfFiles());
//        } else {
//            numArqModPorCommit = new ArrayList<>();
//        }
//        
         if(!pairOfFile.getRepoCommits().isEmpty()){
             for (EntityRepositoryCommit repositoryCommit : pairOfFile.getRepoCommits()) {
                  numArqModPorCommit.add(repositoryCommit.getFiles().size());
             }
         }
        
//        numArqModPorCommit.add(pairOfFile.getRepositoryCommitFile1().getFiles().size());
//        System.out.println("arq mod: "+ pairOfFile.getRepositoryCommitFile1().getFiles().size());
        result.put(pairOfFile.getId(), numArqModPorCommit);
    }

    
    
    @Override
    public MetricResult getResult(Long index) {
        return new MetricResult(sumValue(index), null, null);
    }

    @Override
    public String getHeadCSV() {
        return ArquivosModificadosMetrics.class.getSimpleName() + "SUM;"
                + ArquivosModificadosMetrics.class.getSimpleName() + "MEAN;"
                + ArquivosModificadosMetrics.class.getSimpleName() + "ENTROPY";
    }

    private Integer sumValue(Long index) {
        
        Integer soma = 0;
        
        for (Integer valueInt : result.get(index)) {
            soma += valueInt;
        }

        return soma;
    }
    
}
