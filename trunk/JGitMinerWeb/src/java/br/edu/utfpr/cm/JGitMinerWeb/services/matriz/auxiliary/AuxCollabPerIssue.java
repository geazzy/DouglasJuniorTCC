/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityComment;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityUser;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author geazzy
 */
public class AuxCollabPerIssue {

    private Integer number;
    private EntityUser author;
    private Set<EntityComment> comments;
    private Set<EntityUser> collaboratorsOfRepo;
    private Set<EntityUser> collabsInIssue;
    private String url;
    
    public AuxCollabPerIssue(Integer number, EntityUser userIssue,
            Set<EntityComment> comments, String url, Set<EntityUser> collaborators) {
        
        this.number = number;
        this.author = userIssue;
        this.comments = comments;
        this.url = url;
        this.collaboratorsOfRepo = collaborators;
        this.collabsInIssue = new HashSet<>();
    }

    @Override
    public String toString() {
        return number + ";" + collabsInIssue.size() + ";" + url;
    }

    public void setNumberOfCollabs() {
       if (collaboratorsOfRepo.contains(author)){
           collabsInIssue.add(author);
       }
       
        for (EntityComment entityComment : comments) {
            if (collaboratorsOfRepo.contains(entityComment.getUser())){
                collabsInIssue.add(entityComment.getUser());
            }
        }
    }

    
    
    
}
