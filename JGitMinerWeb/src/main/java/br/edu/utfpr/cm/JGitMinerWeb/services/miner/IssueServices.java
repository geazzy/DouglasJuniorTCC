/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.miner;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityIssue;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.util.JsfUtil;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;

/**
 *
 * @author Douglas
 */
public class IssueServices implements Serializable {

    public static EntityIssue getIssueByIdIssue(long idIssue, GenericDao dao) {
        List<EntityIssue> issues = dao.executeNamedQueryWithParams("Issue.findByIdIssue", new String[]{"idIssue"}, new Object[]{idIssue}, true);
        if (!issues.isEmpty()) {
            return issues.get(0);
        }
        return null;
    }

    public static List<GHIssue> getGitIssuesFromRepository(GHRepository gitRepo, boolean open, boolean closed, OutLog out) {
        List<GHIssue> issues = new ArrayList<>();
        try {
         
            if (open) {
                List<GHIssue> opensIssues;
                out.printLog("Baixando Issues Abertas...\n");
               
                opensIssues = AuthServices.getGitHubClient().getRepository(gitRepo.getFullName()).getIssues(GHIssueState.OPEN);
               
                out.printLog(opensIssues.size() + " Issues abertas baixadas!");
                issues.addAll(opensIssues);
            }
            if (closed) {
                List<GHIssue> clodesIssues;
               
                out.printLog("Baixando Issues Fechadas...\n");
               
                clodesIssues = AuthServices.getGitHubClient().getRepository(gitRepo.getFullName()).getIssues(GHIssueState.CLOSED);
                out.printLog(clodesIssues.size() + " Issues fechadas baixadas!");
                issues.addAll(clodesIssues);
            }
            out.printLog(issues.size() + " Issues baixadas no total!");
        } catch (Exception ex) {
            ex.printStackTrace();
            out.printLog(issues.size() + " Issues baixadas no total! Erro: " + ex.toString());
        }
        return issues;
    }

    public static EntityIssue createEntity(GHIssue gitIssue, EntityRepository repository, GenericDao dao) {
        try {
            if (gitIssue == null) {
                return null;
            }
            
            EntityIssue issue = getIssueByIdIssue(gitIssue.getId(), dao);
            
            if (issue == null) {
                issue = new EntityIssue();
            }
            
            issue.setMineredAt(new Date());
            issue.setIdIssue(gitIssue.getId());
            issue.setClosedAt(gitIssue.getClosedAt());
            issue.setCreatedAt(gitIssue.getCreatedAt());
            issue.setUpdatedAt(gitIssue.getUpdatedAt());
            issue.setNumber(gitIssue.getNumber());
            issue.setCommentsCount(gitIssue.getComments().size());
            LabelServices.addLabels(issue, gitIssue.getLabels(), dao);
            issue.setMilestone(MilestoneServices.createEntity(gitIssue.getMilestone(), repository, dao));
            issue.setBody(JsfUtil.filterChar(gitIssue.getBody()));
            issue.setBodyHtml(gitIssue.getBody());
            issue.setBodyText(gitIssue.getBody());
            issue.setHtmlUrl(gitIssue.getHtmlUrl().toString());
            issue.setStateIssue(gitIssue.getState().toString());
            issue.setTitle(JsfUtil.filterChar(gitIssue.getTitle()));
            issue.setUrl(gitIssue.getUrl().toString());
            issue.setAssignee(UserServices.createEntity(gitIssue.getAssignee(), dao, false));
            issue.setUserIssue(UserServices.createEntity(gitIssue.getUser(), dao, false));
            
            
            
            if (issue.getId() == null || issue.getId().equals(new Long(0))) {
                dao.insert(issue);
            } else {
                dao.edit(issue);
            }
            
            return issue;
        } catch (IOException ex) {
            Logger.getLogger(IssueServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static EntityIssue getIssueByNumber(int number, EntityRepository repo, GenericDao dao) {
        List<EntityIssue> issues = dao.executeNamedQueryWithParams("Issue.findByNumberAndRepository", new String[]{"number", "repository"}, new Object[]{number, repo});
        if (!issues.isEmpty()) {
            return issues.get(0);
        }
        return null;
    }
}
