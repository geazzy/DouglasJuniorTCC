/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.miner;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityComment;
import br.edu.utfpr.cm.JGitMinerWeb.util.JsfUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHRepository;


/**
 *
 * @author Douglas
 */
public class CommentServices implements Serializable {

    public static EntityComment getCommentByIdComment(long idComment, GenericDao dao) {
        List<EntityComment> comments = dao.executeNamedQueryWithParams("Comment.findByIdComment", new String[]{"idComment"}, new Object[]{idComment}, true);
        if (!comments.isEmpty()) {
            return comments.get(0);
        }
        return null;
    }

    public static EntityComment createEntity(GHIssueComment gitComment, GenericDao dao) {
        if (gitComment == null) {
            return null;
        }

        EntityComment comment = getCommentByIdComment(gitComment.getId(), dao);

        if (comment == null) {
            try {
                comment = new EntityComment();
                
                comment.setMineredAt(new Date());
                comment.setCreatedAt(gitComment.getCreatedAt());
                comment.setUpdatedAt(gitComment.getUpdatedAt());
                comment.setBody(JsfUtil.filterChar(gitComment.getBody()));
                comment.setBodyHtml(gitComment.getBody());
                comment.setBodyText(gitComment.getBody());
                comment.setIdComment(gitComment.getId());
                comment.setUrl(gitComment.getUrl().toString());
                comment.setUser(UserServices.createEntity(gitComment.getUser(), dao, false));
                
                dao.insert(comment);
            } catch (IOException ex) {
                Logger.getLogger(CommentServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return comment;
    }

    public static List<GHIssueComment> getGitCommentsByIssue(GHRepository gitRepo, Integer issueNumber) throws Exception {
        try {
            return AuthServices.getGitHubClient().getRepository(gitRepo.getFullName()).getIssue(issueNumber).getComments();
        } catch (Exception ex) {
            ex.printStackTrace();
            return getGitCommentsByIssue(gitRepo, issueNumber);
        }
    }
}
