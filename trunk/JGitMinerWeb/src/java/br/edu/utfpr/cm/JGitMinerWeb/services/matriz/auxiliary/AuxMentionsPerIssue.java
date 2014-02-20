/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityComment;
import com.google.common.base.Strings;
import java.util.Set;

/**
 *
 * @author geazzy
 */
public class AuxMentionsPerIssue {
    
    private Integer numberIssue;
    private Integer numberOfMentions;
    private String body;
    private Set<EntityComment> commentsList;
    private String url;

    public AuxMentionsPerIssue(Integer numberIssue, Set<EntityComment> commentsList, String url, String body) {
        this.numberIssue = numberIssue;
        this.commentsList = commentsList;
        this.url = url;
        this.body = body;
        this.numberOfMentions = 0;
    }

    public Integer getNumberIssue() {
        return numberIssue;
    }

   

    public Integer getNumberOfMentions() {
        return numberOfMentions;
    }

    public void setNumberOfMentions() {
       
        this.numberIssue += MentionsValidator.mentionInString(
                    Strings.nullToEmpty(this.body));
        
        for (EntityComment entityComment : commentsList) {
            this.numberOfMentions += MentionsValidator.mentionInString(
                    Strings.nullToEmpty(entityComment.getBody()));
                      
        }
    }

    public Set<EntityComment> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(Set<EntityComment> commentsList) {
        this.commentsList = commentsList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return numberIssue + ";" + numberOfMentions + ";" + url;
    }

}
