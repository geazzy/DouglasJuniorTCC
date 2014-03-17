/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;

/**
 *
 * @author geazzy
 */
public class AuxRepositoryCommit {
    
//    private final GenericDao dao;
//    private EntityRepositoryCommit repositoryCommit;
//    
//    public AuxRepositoryCommit(GenericDao dao, String sha) {
//        this.dao = dao;
//        setRepositoryCommit(sha);
//    }
    
//    private void setRepositoryCommit(String sha){
//             
//        repositoryCommit = (EntityRepositoryCommit)dao.getEntityManager()
//                .createNamedQuery("RepositoryCommit.findBySHA")
//                .setParameter("sha", sha)
//                .getSingleResult();
//        
//        
//    }
      public static EntityRepositoryCommit getRepositoryCommitBySha(GenericDao dao,String sha){
             
        return (EntityRepositoryCommit)dao.getEntityManager()
                .createNamedQuery("RepositoryCommit.findBySHA")
                .setParameter("sha", sha)
                .getSingleResult();
        
        
    }
    
    
}
