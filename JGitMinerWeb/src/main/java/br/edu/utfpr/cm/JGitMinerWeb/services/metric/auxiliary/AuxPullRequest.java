/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;

/**
 *
 * @author geazzy
 */
public class AuxPullRequest {
    
     public static EntityPullRequest getPullRequestByID(GenericDao dao, Long id){
             
        return (EntityPullRequest)dao.getEntityManager()
                .createNamedQuery("PullRequest.findByIdPullRequest")
                .setParameter("idPullRequest", id)
                .getSingleResult();
        
        
    }
    
}
