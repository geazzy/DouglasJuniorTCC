/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.miner;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityCommitComment;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
import br.edu.utfpr.cm.JGitMinerWeb.util.JsfUtil;
import br.edu.utfpr.cm.JGitMinerWeb.util.Util;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.github.GHCommitComment;
import org.kohsuke.github.GHRepository;

/**
 *
 * @author Douglas
 */
public class CommitCommentServices implements Serializable {

    public static EntityCommitComment createEntity(GHCommitComment gitComment, GenericDao dao) {
        if (gitComment == null) {
            return null;
        }

        EntityCommitComment comment = getCommitCommentByID(new Long(gitComment.getId()), dao);

        if (comment == null) {
            try {
                comment = new EntityCommitComment();

                comment.setMineredAt(new Date());
                comment.setCommitId(gitComment.getCommit().getSHA1());
                comment.setBody(JsfUtil.filterChar(gitComment.getBody()));
                comment.setBodyHtml(gitComment.getBody());
                comment.setBodyText(gitComment.getBody());
                comment.setCreatedAt(gitComment.getCreatedAt());
                comment.setIdComment(new Long(gitComment.getId()));
                comment.setLine(gitComment.getLine());
                comment.setPathCommitComment(gitComment.getPath());
                comment.setPosition(gitComment.getId());
                comment.setUpdatedAt(gitComment.getUpdatedAt());
                comment.setUrl(gitComment.getUrl().toString());
                comment.setUser(UserServices.createEntity(gitComment.getUser(), dao, false));

                dao.insert(comment);
            } catch (IOException ex) {
                Logger.getLogger(CommitCommentServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return comment;
    }

    private static EntityCommitComment getCommitCommentByID(Long idComment, GenericDao dao) {
        List<EntityCommitComment> comments = dao.executeNamedQueryWithParams("CommitComment.findByIdComment", new String[]{"idComment"}, new Object[]{idComment}, true);
        if (!comments.isEmpty()) {
            return comments.get(0);
        }
        return null;
    }

    public static List<GHCommitComment> getGitCommitComments(GHRepository gitRepo, EntityRepositoryCommit repoCommit) throws Exception {

        List<GHCommitComment> commitCommentList = new ArrayList<>();

        try {
            for (GHCommitComment comment : AuthServices.getGitHubClient().
                    getRepository(gitRepo.getFullName()).getCommit(repoCommit.getSha()).listComments()) {
                commitCommentList.add(comment);
            }
            return commitCommentList;
          
        } catch (Exception ex) {
            ex.printStackTrace();
            return getGitCommitComments(gitRepo, repoCommit);
        }
    }
}
