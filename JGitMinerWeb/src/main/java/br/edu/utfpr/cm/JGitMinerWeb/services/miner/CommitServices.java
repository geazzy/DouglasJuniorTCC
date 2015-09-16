/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.miner;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityCommit;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.github.GHCommit;


/**
 *
 * @author Douglas
 */
public class CommitServices implements Serializable {

    public static EntityCommit createEntity(GHCommit gitCommit, GenericDao dao) {
        try {
            if (gitCommit == null) {
                return null;
            }
            
            EntityCommit commit = getCommitBySHA(gitCommit.getSHA1(), dao);
            
            if (commit == null) {
                commit = new EntityCommit();
            }
            
            commit.setMineredAt(new Date());
            commit.setAuthor(CommitUserServices.createEntity(gitCommit.getAuthor(), dao));
            commit.setCommitter(CommitUserServices.createEntity(gitCommit.getCommitter(), dao));
            commit.setCommentCount(gitCommit.listComments().asList().size());
            commit.setMessage(gitCommit.getCommitShortInfo().getMessage());
//        createParents(commit, gitCommit.getParents(), gitRepo, dao);
            commit.setSha(gitCommit.getSHA1());
            //  commit.setTree(TreeServices.createTreeEntity(gitCommit.getTree(), gitRepo, dao));
            commit.setUrl(gitCommit.getSHA1());
            
            if (commit.getId() == null || commit.getId().equals(new Long(0))) {
                dao.insert(commit);
            } else {
                dao.edit(commit);
            }
            
            return commit;
        } catch (IOException ex) {
            Logger.getLogger(CommitServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static EntityCommit getCommitBySHA(String sha, GenericDao dao) {
        List<EntityCommit> commits = dao.executeNamedQueryWithParams("Commit.findBySha", new String[]{"sha"}, new Object[]{sha}, true);
        if (!commits.isEmpty()) {
            return commits.get(0);
        }
        return null;
    }
//    private static void createParents(EntityCommit commit, List<Commit> gitParents, Repository gitRepo, GenericDao dao) {
//        if (gitParents != null) {
//            for (Commit gitParent : gitParents) {
//                commit.addParent(createEntity(gitParent, gitRepo, dao));
//            }
//        }
//    }
}
