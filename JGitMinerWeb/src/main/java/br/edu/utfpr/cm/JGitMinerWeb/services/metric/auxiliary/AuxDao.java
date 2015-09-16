/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;

/**
 *
 * @author zanoni
 */
public class AuxDao {
    
    
     private static final GenericDao dao = new GenericDao();
     
     public static EntityPullRequest getPullRequest(Long id) {

        return (EntityPullRequest) dao.getEntityManager()
                .createNamedQuery("PullRequest.findById")
                .setParameter("idPullRequest", id)
                .getSingleResult();
    }
    
     
      public static EntityRepositoryCommit getRepositoryCommit(Long id) {

        return (EntityRepositoryCommit) dao.getEntityManager()
                .createNamedQuery("RepositoryCommit.findById")
                .setParameter("id", id)
                .getSingleResult();
    }
}
