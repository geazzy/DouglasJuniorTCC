/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.pojo.miner.EntityComment;
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
    private String issueBody;
    private List<EntityComment> comments;

    public AuxNumberOfLinks(Integer number, String url, Integer numberOflinks) {
        this.issueNumber = number;
        this.url = url;
        //   this.comments = comments;
        this.numberOflinks = numberOflinks;
    }

    public AuxNumberOfLinks(Integer issueNumber, String url, String body) {
        this.issueNumber = issueNumber;
        this.url = url;
        this.issueBody = body;
        this.numberOflinks = 0;
    }

    public String getIssueBody() {
        return issueBody;
    }

    public void setIssueBody(String issueBody) {
        this.issueBody = issueBody;
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

    public Integer getNumberOflinks() {
        return numberOflinks;
    }

    public void setNumberOflinks() {

        if (getIssueBody() != null) {
            System.out.println(getIssueBody().toString());
            this.numberOflinks += UrlValidator.urlInString(getIssueBody().toString());
        }

        for (EntityComment entityComment : comments) {

            if (entityComment.getBody() != null) {
                this.numberOflinks += UrlValidator.urlInString(entityComment.getBody().toString());
            }
        }

    }

    public List<EntityComment> getComments() {
        return comments;
    }

    public void setComments(List<EntityComment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return issueNumber + ";" + numberOflinks + ";" + url;
    }

}
