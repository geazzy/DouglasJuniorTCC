/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityComment;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityIssue;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author geazzy
 */
public class AuxMeanReplyTime {

    private EntityIssue issue;
    private List<EntityComment> commentsList;
    private Integer mean;

    public AuxMeanReplyTime(EntityIssue issuePullRequest) {
        this.issue = issuePullRequest;
        mean = 0;
    }

    public EntityIssue getIssue() {
        return issue;
    }

    public List<EntityComment> getCommentsList() {
        return commentsList;
    }

    @Override
    public String toString() {
        return issue.getNumber() + ";" + mean + ';'+issue.getUrl();
        //return issue.getNumber() + ";" + "" + ';' + issue.getUrl();
    }

    public void setCommentsList(List<EntityComment> commentsList) {
        this.commentsList = commentsList;
    }

    public void setMean() {

        DateTime dataAnterior = null;
    
        
        for (EntityComment comentario : getCommentsList()) {
            System.out.println(issue.getNumber()+" - hora: "+comentario.getCreatedAt());
            
            if ((dataAnterior == null) && (getCommentsList().size() > 0)) {
                dataAnterior = new DateTime(comentario.getCreatedAt());
            }else{
               mean += Hours.hoursBetween(new DateTime(comentario.getCreatedAt()),dataAnterior).getHours();
            
               System.out.println(issue.getNumber()+" - "+ mean);
            }

        }
        System.out.println("soma: "+mean);
        mean = mean/getCommentsList().size();
        System.out.println("media: "+mean);
    }

}
