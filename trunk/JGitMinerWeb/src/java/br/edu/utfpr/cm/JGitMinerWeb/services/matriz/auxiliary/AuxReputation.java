/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary;


/**
 *
 * @author geazzy
 */
public class AuxReputation {
    
    private String user;
    private Integer countCommitters;
    private Integer countMessages;

    public AuxReputation(String user, Integer countCommitters, Integer countMessages) {
        this.user = user;
        this.countCommitters = countCommitters;
        this.countMessages = countMessages;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    @Override
    public String toString() {
        return  user + ";" + countCommitters + ";" +countMessages;
    }

    public Integer getCountCommitters() {
        return countCommitters;
    }

    public void setCountCommitters(Integer countCommitters) {
        this.countCommitters = countCommitters;
    }

    public Integer getCountMessages() {
        return countMessages;
    }

    public void setCountMessages(Integer countMessages) {
        this.countMessages = countMessages;
    }
    
    
    
}
