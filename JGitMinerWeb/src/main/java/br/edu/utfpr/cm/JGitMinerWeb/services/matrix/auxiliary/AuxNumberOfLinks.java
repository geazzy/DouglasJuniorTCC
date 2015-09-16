/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityComment;
import com.google.common.base.Strings;
import java.util.List;
import java.util.Set;

/**
 *
 * @author geazzy
 */
public class AuxNumberOfLinks {

    private Integer issueNumber;
    private String url;
    private Integer numberOflinks;
    //private String issueBody;
    //private List<EntityComment> comments;

    public AuxNumberOfLinks(Integer number, String url, Integer numberOflinks) {
        this.issueNumber = number;
        this.url = url;
        //   this.comments = comments;
        this.numberOflinks = numberOflinks;
    }

//    public AuxNumberOfLinks(Integer issueNumber, String url, String body) {
//        this.issueNumber = issueNumber;
//        this.url = url;
//        this.issueBody = body;
//        this.numberOflinks = 0;
//    }

//    public String getIssueBody() {
//        return issueBody;
//    }
//
//    public void setIssueBody(String issueBody) {
//        this.issueBody = issueBody;
//    }

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

    public Integer getNumberOflinks() {
        return numberOflinks;
    }

    public void setNumberOflinks(Integer numberLinks) {

//        this.numberOflinks += UrlValidator.urlInString(Strings.nullToEmpty(getIssueBody()));
//
//        for (EntityComment entityComment : comments) {
//
//            this.numberOflinks += UrlValidator.urlInString(Strings.nullToEmpty(entityComment.getBody()));
//
//        }
        this.numberOflinks = numberLinks;

    }

//    public List<EntityComment> getComments() {
//        return comments;
//    }
//
//    public void setComments(List<EntityComment> comments) {
//        this.comments = comments;
//    }

    @Override
    public String toString() {
        return issueNumber + ";" + numberOflinks + ";" + url;
    }

}
