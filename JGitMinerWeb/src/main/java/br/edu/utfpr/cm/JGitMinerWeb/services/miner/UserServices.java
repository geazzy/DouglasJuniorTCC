/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.miner;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityUser;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;

/**
 *
 * @author Douglas
 */
public class UserServices implements Serializable {

    private static EntityUser getUserByLogin(String login, GenericDao dao) {
        List<EntityUser> users = dao.executeNamedQueryWithParams("User.findByLogin", new String[]{"login"}, new Object[]{login}, true);
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

    public static EntityUser createEntity(GHUser gitUser, GenericDao dao, boolean firstMiner) {
       
        try {
            if (gitUser == null) {
                return null;
            }
            
            
            EntityUser user = getUserByLogin(gitUser.getLogin(), dao);
            GHUser ghUser = AuthServices.getGitHubClient().getUser(gitUser.getLogin());
            
            if (firstMiner || user == null) {
                try {
                    if (user == null) {
                        user = new EntityUser();
                    }
                    
                    
                    
                    user.setCreatedAt(ghUser.getCreatedAt());
                    user.setCollaborators(0);
                    user.setDiskUsage(0);
                    user.setFollowers(ghUser.getFollowersCount());
                    user.setFollowing(ghUser.getFollowingCount());
                    user.setOwnedPrivateRepos(0);
                    user.setPrivateGists(0);
                    user.setPublicGists(ghUser.getPublicGistCount());
                    user.setPublicRepos(ghUser.getPublicRepoCount());
                    user.setTotalPrivateRepos(0);
                    user.setAvatarUrl(ghUser.getBlog());
                    user.setCompany(ghUser.getCompany());
                    user.setEmail(ghUser.getEmail());
                    user.setHtmlUrl(ghUser.getHtmlUrl().toString());
                    user.setLocation(ghUser.getLocation());
                    user.setName(ghUser.getName());
                    user.setType(ghUser.getClass().getSimpleName());
                } catch (IOException ex) {
                    Logger.getLogger(UserServices.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }

            user.setMineredAt(
                    new Date());
            user.setGravatarId(ghUser.getAvatarUrl());
            user.setIdUser(ghUser.getId());
            user.setLogin(ghUser.getLogin());
            user.setUrl(ghUser.getUrl().toString());
            
            if (user.getId()
                    == null || user.getId().equals(new Long(0))) {
                dao.insert(user);
            } else {
                dao.edit(user);
            }
            
            return user;
        } catch (IOException ex) {
            Logger.getLogger(UserServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static List<GHUser> getGitCollaboratorsFromRepository(GHRepository gitRepo, OutLog out) throws Exception {

        List<GHUser> users = new ArrayList<GHUser>();

        try {

            out.printLog("Baixando Collaborators...\n");
            users.addAll(AuthServices.getGitHubClient().getRepository(gitRepo.getFullName()).getCollaborators());
            out.printLog(users.size() + " Collaborators baixados!");

        } catch (Exception ex) {

            ex.printStackTrace();
            out.printLog("Erro: " + ex.toString());

        }

        return users;
    }

    public static List<GHUser> getGitCollaboratorsFromRepository(GHRepository gitRepo) {

        List<GHUser> users = new ArrayList<>();
        try {
            //  out.printLog("Baixando Collaborators...\n");
            users = (List<GHUser>) AuthServices.getGitHubClient().getRepository(gitRepo.getFullName()).getCollaborators();
            // out.printLog(users.size() + " Collaborators baixados!");
        } catch (Exception ex) {
            ex.printStackTrace();
            // out.printLog("Erro: " + ex.toString());
        }
        return users;
    }

//    public static List<GHUser> getGitWatchersFromRepository(GHRepository gitRepo, OutLog out) throws Exception {
//        out.printLog("Baixando Watchers...\n");
//        List<GHUser> users = AuthServices.getGitHubClient().getRepository(gitRepo.getFullName()).getWatchers();
//        
//        out.printLog(users.size() + " Watchers baixados!");
//        return users;
//    }
}
