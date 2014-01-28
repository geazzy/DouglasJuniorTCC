/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.pojo.miner.EntityUser;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author geazzy
 */
public class AuxNumberOfDevUser {

    private Integer issueNumber;
    private String url;
    private Integer numberOfUsers;
    private Integer numberOfDevs;
    private Integer commentsCount;
    private Set<AuxUserComments> commentsAuthorsList;
    private EntityUser autor;

    public AuxNumberOfDevUser(Integer issueNumber, String url, Integer commentsCount, EntityUser autor) {
        this.issueNumber = issueNumber;
        this.url = url;
        this.commentsCount = commentsCount;
        this.numberOfDevs = 0;
        this.numberOfUsers = 0;
        this.commentsAuthorsList = Sets.newHashSet();
        this.autor = autor;
    }

    public EntityUser getAutorDaIssue() {
        return autor;
    }

    public void setAutor(EntityUser autor) {
        this.autor = autor;
    }

    public Integer getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(Integer issueNumber) {
        this.issueNumber = issueNumber;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setNumberOfUsers(Integer numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    public Integer getNumberOfDevs() {
        return numberOfDevs;
    }

    public void setNumberOfDevs(Integer numberOfDevs) {
        this.numberOfDevs = numberOfDevs;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Set<AuxUserComments> getCommentsAuthorsList() {
        return commentsAuthorsList;
    }

    
    public void setCommentsAuthorsList(Set<AuxUserComments> comments) {
        this.commentsAuthorsList = comments;
    }

//     public void setCommentsAuthorsList(EntityUser autor) {
//        commentsAuthorsList.add(autor);
//    }
    @Override
    public String toString() {
        return issueNumber + ";" + numberOfDevs  + ";" + numberOfUsers + ";" + url;
        //return issueNumber + ";" + commentsCount + ";" + numberOfUsers + ";" + url;
    }

    public void addAutorDaIssueAListaDeAutores() {
     this.commentsAuthorsList.add(new AuxUserComments(autor));
    }

    public void incrementarNumeroDevs() {
        this.numberOfDevs++;
    }

    public void incrementarNumeroUsers() {
        this.numberOfUsers++;
    }

}
