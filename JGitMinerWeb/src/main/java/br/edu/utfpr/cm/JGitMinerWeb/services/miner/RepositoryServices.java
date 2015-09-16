/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.miner;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.github.GHRepository;

/**
 *
 * @author Douglas
 */
public class RepositoryServices implements Serializable {

    private static EntityRepository getRepositoryByIdRepository(Long idRepo, GenericDao dao) {
        List<EntityRepository> users = dao.executeNamedQueryWithParams("Repository.findByIdRepository", new String[]{"idRepository"}, new Object[]{idRepo});
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

    public static EntityRepository createEntity(GHRepository gitRepository, GenericDao dao, boolean primary) {
        try {
            if (gitRepository == null) {
                return null;
            }
            
            EntityRepository repo = getRepositoryByIdRepository(new Long(gitRepository.getId()), dao);
            
            if (primary || repo == null) {
                try {
                    if (repo == null) {
                        repo = new EntityRepository();
                    }
                    repo.setParent(RepositoryServices.createEntity(gitRepository.getParent(), dao, false));
                    repo.setSource(RepositoryServices.createEntity(gitRepository.getSource(), dao, false));
                    repo.setMasterBranch(gitRepository.getMasterBranch());
                } catch (IOException ex) {
                    Logger.getLogger(RepositoryServices.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            repo.setMineredAt(new Date());
            repo.setPrimaryMiner(primary);
            repo.setFork(gitRepository.isFork());
            repo.setHasDownloads(gitRepository.hasDownloads());
            repo.setHasIssues(gitRepository.hasIssues());
            repo.setHasWiki(gitRepository.hasWiki());
            repo.setIsPrivate(gitRepository.isPrivate());
            repo.setCreatedAt(gitRepository.getCreatedAt());
            repo.setPushedAt(gitRepository.getPushedAt());
            repo.setUpdatedAt(gitRepository.getUpdatedAt());
            repo.setIdRepository(new Long(gitRepository.getId()));
            repo.setSizeRepository(gitRepository.getSize());
            repo.setCloneUrl(gitRepository.getGitTransportUrl());
            repo.setDescription(gitRepository.getDescription());
            repo.setHomepage(gitRepository.getHomepage());
            repo.setGitUrl(gitRepository.getSshUrl());
            repo.setHtmlUrl(gitRepository.getUrl().toString());
            repo.setLanguageRepository(gitRepository.getLanguage());
            repo.setMirrorUrl(gitRepository.getSvnUrl());
            repo.setName(gitRepository.getName());
            repo.setSshUrl(gitRepository.getSshUrl());
            repo.setSvnUrl(gitRepository.getSvnUrl());
            repo.setUrl(gitRepository.getUrl().toString());
            repo.setOwner(UserServices.createEntity(gitRepository.getOwner(), dao, false));
            
            if (repo.getId() == null || repo.getId().equals(new Long(0))) {
                dao.insert(repo);
            } else {
                dao.edit(repo);
            }
            
            return repo;
        } catch (IOException ex) {
            Logger.getLogger(RepositoryServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static GHRepository getGitRepository(String ownerLogin, String repoName) throws Exception {
        
        return AuthServices.getGitHubClient().getRepository(ownerLogin+"/"+repoName);
    }

//    public static List<GHRepository> getGitForksFromRepository(GHRepository gitRepo, OutLog out) throws Exception {
//        out.printLog("Baixando Forks...\n");
//        List<GHRepository> forks = AuthServices.getGitHubClient().getRepository(gitRepo.getFullName());
//        out.printLog(forks.size() + " Forks baixados!");
//        return forks;
//    }
}
