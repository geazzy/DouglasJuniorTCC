/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.miner;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityCommitUser;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHPerson;


/**
 *
 * @author Douglas
 */
public class CommitUserServices implements Serializable {

    public static EntityCommitUser createEntity(GHPerson gitCommitUser, GenericDao dao) {
        try {
            if (gitCommitUser == null) {
                return null;
            }
            
            EntityCommitUser commitUser = getCommitUserByEmailAndDate(gitCommitUser.getEmail(), gitCommitUser.getUpdatedAt(), dao);
            
            if (commitUser == null) {
                commitUser = new EntityCommitUser();
                
                commitUser.setMineredAt(new Date());
                commitUser.setDateCommitUser(gitCommitUser.getUpdatedAt());
                commitUser.setEmail(gitCommitUser.getEmail());
                commitUser.setName(gitCommitUser.getName());
                
                dao.insert(commitUser);
            }
            
            return commitUser;
        } catch (IOException ex) {
            Logger.getLogger(CommitUserServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static EntityCommitUser getCommitUserByEmailAndDate(String email, Date date, GenericDao dao) {
        List<EntityCommitUser> users = dao.executeNamedQueryWithParams("CommitUser.findByEmailAndDate", new String[]{"email", "date"}, new Object[]{email, date}, true);
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }
}
