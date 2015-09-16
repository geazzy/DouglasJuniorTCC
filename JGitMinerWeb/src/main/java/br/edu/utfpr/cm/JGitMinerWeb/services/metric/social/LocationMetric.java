/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.social;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityUser;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxPairOfFiles;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxAllMetrics;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxCoChangeMetrics;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxDao;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxRepositoryCommit;
import br.edu.utfpr.cm.JGitMinerWeb.util.GeoCodeUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author zanoni
 */
public class LocationMetric implements MetricInterface {

    private HashMap<Long, Set<String>> result;
    /// private GenericDao dao = new GenericDao();
    //private Set<String> countryList = new HashSet<>();

    public LocationMetric() {
        this.result = new HashMap<>();
    }

    @Override
    public void calculateMetric(AuxAllMetrics pairOfFile) {

        Set<String> countrylist = new HashSet<>();
//        if (result.containsKey(pairOfFiles.getAuxPairOfFiles())) {
//            countryTemp = result.get(pairOfFiles.getAuxPairOfFiles());
//        } else {
//            countryTemp = new HashSet<>();
//        }
        if (!pairOfFile.getRepoCommits_idList().isEmpty()) {
            
            for (EntityRepositoryCommit repoCommit : pairOfFile.getRepoCommits()) {
               // System.out.println("id: "+repoCommit_id);
                
             //   EntityRepositoryCommit repoCommit  = AuxDao.getRepositoryCommit(repoCommit_id);
                 
                EntityUser autor = repoCommit.getAuthor();
                
                  if (autor != null && autor.getLogin() != null && autor.getLocation() != null) {
                       countrylist.add(GeoCodeUtil.getCountry(autor.getLogin(), autor.getLocation()));
                  }
            }
        }

       // EntityRepositoryCommit repoCommit = AuxRepositoryCommit.getRepositoryCommitBySha(pairOfFiles.getshaCommitFile1());
        //EntityUser autor = pairOfFiles.getRepositoryCommitFile1().getAuthor();

       // if (autor != null && autor.getLogin() != null && autor.getLocation() != null) {

            //EntityUser autor = pairOfFiles.getRepositoryCommitFile1().getAuthor();
       //     countryTemp.add(GeoCodeUtil.getCountry(autor.getLogin(), autor.getLocation()));
            //if (pairOfFiles.getAuxPairOfFiles().getshaCommitFile1() == "16ea791376818e58647bb6ea808edff93d1936b4"){
            // countryTemp.add(GeoCodeUtil.getCountry("teste", "maringá, pr"));
            //countryTemp.add(GeoCodeUtil.getCountry("teste2", "france"));
            //  countryTemp.add(GeoCodeUtil.getCountry("teste3", "buenos aires"));
    //   }

     //   System.out.println("country size: " + countrylist.size());

        result.put(pairOfFile.getId(), countrylist);
        //}
//         System.out.println("Autor i: "+files.get(i).getRepositoryCommit().getAuthor().getLogin()+
//                            " ; "+files.get(i).getRepositoryCommit().getAuthor().getLocation());
//                    
////                    System.out.println("Location: "+
////                            GeoCodeUtil.getCountry(files.get(i).getRepositoryCommit().getAuthor().getLocation()));
//                    
//                    coChangedList.add(new AuxNewPairOfFiles(pullRequest,
//                            files.get(i), files.get(next),
//                            files.get(i).getRepositoryCommit(),
//                            files.get(next).getRepositoryCommit()));
//                    GeoCodeUtil.getCountry(commitFile1.getAuthor().getLogin(),
//                        commitFile1.getAuthor().getLocation())
    }

    @Override
    public MetricResult getResult(Long indexPairOfFiles) {
        return new MetricResult(result.get(indexPairOfFiles).size(), null,
                null);
    }

    @Override
    public String getHeadCSV() {
        return "LocationMetricSUM;LocationMetricMEAN;LocationMetricENTROPY";
    }

}
