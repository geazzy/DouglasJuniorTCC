/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary;

/**
 *
 * @author geazzy
 */
public class AuxNumberOfMessages {
    private Integer issueNumber;
    private Integer numberOfMessages;
    private String url;

    public AuxNumberOfMessages(Integer issue, Integer numberOfMessages, String url) {
        this.issueNumber = issue;
        this.numberOfMessages = numberOfMessages;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    

      public Integer getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(Integer issueNumber) {
        this.issueNumber = issueNumber;
    }

    public Integer getNumberOfMessages() {
        return numberOfMessages;
    }

    public void setNumberOfMessages(Integer numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
    }

    @Override
    public String toString() {
        return issueNumber + ";" + numberOfMessages + ";" + url;
    }
    
    
    
}
