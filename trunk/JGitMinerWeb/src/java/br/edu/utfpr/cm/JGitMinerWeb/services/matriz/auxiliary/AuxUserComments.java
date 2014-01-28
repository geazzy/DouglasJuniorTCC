/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.pojo.miner.EntityUser;
import java.util.Objects;
import javax.persistence.Entity;

/**
 *
 * @author geazzy
 */
public class AuxUserComments {

    private EntityUser user;
    private Integer numeroCommits;
    private Boolean isDev;

//    public AuxUserComments(String login, String email, Long id) {
//        this.login = login;
//        this.email = email;
//        this.id = id;
//        this.isDev = false;
//        this.numeroCommits = 0;
//    }
    public AuxUserComments(EntityUser user) {
        this.user = user;
        this.isDev = false;
        this.numeroCommits = 0;
    }

    public EntityUser getUser() {
        return user;
    }

    public void setUser(EntityUser user) {
        this.user = user;
    }

    public Integer getNumeroCommits() {
        return numeroCommits;
    }

    public void setNumeroCommits(Integer numeroCommits) {
        this.numeroCommits = numeroCommits;
    }

    public Boolean isDev() {
        return isDev;
    }

    public void setIsDev() {
        if (numeroCommits > 0) {
            this.isDev = Boolean.TRUE;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.user);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AuxUserComments other = (AuxUserComments) obj;
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        return true;
    }
    
    

}
