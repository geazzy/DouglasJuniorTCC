/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.miner;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityIssue;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityIssueEvent;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.github.GHEvent;
import org.kohsuke.github.GHEventInfo;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.PagedIterable;

/**
 *
 * @author douglas
 */
public class IssueEventServices implements Serializable {

//    public static List<GHEventInfo> getEventsByIssue(EntityIssue issue, String ownerRepositoryLogin, String repositoryName) {
//        // repos/:owner/:repo/issues/:issue_number/events
//        
//        try {
//           
//            GHIssue gitIssue = AuthServices.getGitHubClient().
//                    getRepository(ownerRepositoryLogin + "/" + repositoryName).getIssue(issue.getNumber());
//            
//            if (gitIssue.isPullRequest()){
//                
//                 List<GHEventInfo> issueEvents = new ArrayList<>();
//                 
//                 GHPullRequest gitPull = AuthServices.getGitHubClient().
//                    getRepository(ownerRepositoryLogin + "/" + repositoryName).getPullRequest(issue.getNumber());
//                 
//                 gitPull.
//            }
//            
//            
//        } catch (IOException ex) {
//            Logger.getLogger(IssueEventServices.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        
//        try {
//           
//            PagedIterable<GHEventInfo> repositoryEventInfo;
//            
//            repositoryEventInfo = AuthServices.getGitHubClient().
//                    getRepository(ownerRepositoryLogin + "/" + repositoryName).listEvents();
//            
//            for (GHEventInfo eventInfo : repositoryEventInfo) {
//                if (eventInfo.getType().equals(GHEvent.PULL_REQUEST)) {
//                    //issueEvents.add(eventInfo);
//                    eventInfo.getPayload(GHEventPayload.PullRequest);
//                }
//            }
//            return issueEvents;
//
//        } catch (RuntimeException ex) {
//            ex.printStackTrace();
//            return getEventsByIssue(issue, ownerRepositoryLogin, repositoryName);
//        } catch (IOException ex) {
//            Logger.getLogger(IssueEventServices.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    public static EntityIssueEvent createEntity(GHEventInfo gitIssueEvent, GenericDao dao) {
//        if (gitIssueEvent == null) {
//            return null;
//        }
//        
//        EntityIssueEvent issueEvent;// = getEventByIssueEventID(gitIssueEvent.getPayload(g), dao);
//
//        if (issueEvent == null) {
//            issueEvent = new EntityIssueEvent();
//
//            issueEvent.setMineredAt(new Date());
//            issueEvent.setCreatedAt(gitIssueEvent.getCreatedAt());
//            issueEvent.setActor(UserServices.createEntity(gitIssueEvent.getActor(), dao, false));
//            issueEvent.setCommitId(gitIssueEvent.);
//            issueEvent.setEvent(gitIssueEvent.getType().toString());
//            issueEvent.setIdIssueEvent(gitIssueEvent.);
//            issueEvent.setUrl(null);
//
//            dao.insert(issueEvent);
//        }
//
//        return issueEvent;
//    }
//
//    private static EntityIssueEvent getEventByIssueEventID(long issueEventID, GenericDao dao) {
//        List<EntityIssueEvent> events = dao.executeNamedQueryWithParams("IssueEvent.findByEventIssueID", new String[]{"idIssueEvent"}, new Object[]{issueEventID}, true);
//        if (!events.isEmpty()) {
//            return events.get(0);
//        }
//        return null;
//    }
}
