/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.miner;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
//import org.eclipse.egit.github.core.PullRequest;
//import org.eclipse.egit.github.core.Repository;
//import org.eclipse.egit.github.core.service.PullRequestService;

/**
 *
 * @author Douglas
 */
public class PullRequestServices implements Serializable {

    public static EntityPullRequest createEntity(GHPullRequest gitPullRequest, GenericDao dao) {
        try {
            if (gitPullRequest == null) {
                return null;
            }

            EntityPullRequest pull = getPullRequestByIdPull(gitPullRequest.getId(), dao);

            if (pull == null) {
                pull = new EntityPullRequest();
            }

            pull.setMineredAt(new Date());
            System.out.println("git url" + gitPullRequest.getUrl().toString());
           // System.out.println("git pullrequest mergeable: " + gitPullRequest.getMergeable());

            if (gitPullRequest.getMergeable() == null) {
                pull.setMergeable(false);
            } else {
                pull.setMergeable(gitPullRequest.getMergeable());
            }

            pull.setMerged(gitPullRequest.isMerged());
            pull.setClosedAt(gitPullRequest.getClosedAt());
            pull.setMergedAt(gitPullRequest.getMergedAt());
            pull.setUpdatedAt(gitPullRequest.getUpdatedAt());
            pull.setCreatedAt(gitPullRequest.getCreatedAt());
            pull.setIdPullRequest(new Long(gitPullRequest.getId()));
            pull.setAdditions(gitPullRequest.getAdditions());
            pull.setChangedFiles(gitPullRequest.getChangedFiles());
            pull.setCommentsCount(gitPullRequest.listComments().asList().size());
            pull.setCommitsCount(gitPullRequest.listCommits().asList().size());
            pull.setDeletions(gitPullRequest.getDeletions());
            pull.setNumber(gitPullRequest.getNumber());
            if (pull.getBase() == null) {
                pull.setBase(PullRequestMakerServices.createEntity(gitPullRequest.getBase(), dao));
            }
            if (pull.getHead() == null) {
                pull.setHead(PullRequestMakerServices.createEntity(gitPullRequest.getHead(), dao));
            }
            pull.setBody(gitPullRequest.getBody());
            pull.setBodyHtml(gitPullRequest.getBody());
            pull.setBodyText(gitPullRequest.getBody());
            pull.setDiffUrl(gitPullRequest.getDiffUrl().toString());
            pull.setHtmlUrl(gitPullRequest.getHtmlUrl().toString());
            pull.setIssueUrl(gitPullRequest.getIssueUrl().toString());
            pull.setPatchUrl(gitPullRequest.getPatchUrl().toString());
            pull.setStatePullRequest(gitPullRequest.getState().toString());
            pull.setTitle(gitPullRequest.getTitle());
            pull.setUrl(gitPullRequest.getUrl().toString());
            pull.setMergedBy(UserServices.createEntity(gitPullRequest.getMergedBy(), dao, false));
            pull.setUser(UserServices.createEntity(gitPullRequest.getUser(), dao, false));

            if (pull.getId() == null || pull.getId().equals(new Long(0))) {
                dao.insert(pull);
            } else {
                dao.edit(pull);
            }

            return pull;
        } catch (IOException ex) {
            Logger.getLogger(PullRequestServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static EntityPullRequest getPullRequestByIdPull(long idPullRequest, GenericDao dao) {
        List<EntityPullRequest> pulls = dao.executeNamedQueryWithParams("PullRequest.findByIdPullRequest", new String[]{"idPullRequest"}, new Object[]{idPullRequest}, true);
        if (!pulls.isEmpty()) {
            return pulls.get(0);
        }
        return null;
    }

    public static List<GHPullRequest> getGitPullRequestsFromRepository(GHRepository gitRepo, boolean open, boolean closed, OutLog out) {
        List<GHPullRequest> pulls = new ArrayList<>();
        try {
            //PullRequestService pullServ = new PullRequestService(AuthServices.getGitHubClient());
            if (open) {
                List<GHPullRequest> opensPulls;
                out.printLog("Baixando PullRequests Abertos...\n");
                opensPulls = AuthServices.getGitHubClient().getRepository(gitRepo.getFullName()).getPullRequests(GHIssueState.OPEN);
                //pullServ.getPullRequests(gitRepo, "open");
                out.printLog(opensPulls.size() + " PullRequests abertos baixados!");
                pulls.addAll(opensPulls);
            }
            if (closed) {
                List<GHPullRequest> closedsPulls;
                out.printLog("Baixando PullRequests Fechados...\n");
                closedsPulls = AuthServices.getGitHubClient().getRepository(gitRepo.getFullName()).getPullRequests(GHIssueState.CLOSED);
                out.printLog(closedsPulls.size() + " PullRequests fechados baixados!");
                pulls.addAll(closedsPulls);
            }
            out.printLog(pulls.size() + " PullRequests baixados no total!");
        } catch (Exception ex) {
            ex.printStackTrace();
            out.printLog(pulls.size() + " PullRequests baixados no total! Erro: " + ex.toString());
        }
        return pulls;
    }

    public static EntityPullRequest getPullRequestByNumber(int number, EntityRepository repo, GenericDao dao) {
        List<EntityPullRequest> pulls = dao.executeNamedQueryWithParams("PullRequest.findByNumberAndRepository", new String[]{"number", "repository"}, new Object[]{number, repo});
        if (!pulls.isEmpty()) {
            return pulls.get(0);
        }
        return null;
    }
}
