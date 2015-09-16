/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.miner;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHPullRequestCommitDetail;
import org.kohsuke.github.GHRepository;

/**
 *
 * @author Douglas
 */
public class RepositoryCommitServices implements Serializable {

    public static List<GHCommit> getGitCommitsFromRepository(GHRepository gitRepo, OutLog out) {
        List<GHCommit> repoCommits = null;
        try {
            out.printLog("Baixando RepositoryCommits...\n");

            repoCommits = AuthServices.getGitHubClient().getRepository(gitRepo.getFullName()).listCommits().asList();
//new CommitService(AuthServices.getGitHubClient()).getCommits(gitRepo);
            out.printLog(repoCommits.size() + " RepositoryCommits baixados no total!");
        } catch (Exception ex) {
            ex.printStackTrace();
            out.printLog(repoCommits.size() + " RepositoryCommits baixadaos no total! Erro: " + ex.toString());
        }
        return repoCommits;
    }

    public static EntityRepositoryCommit createEntity(GHCommit gitRepoCommit, GenericDao dao) {
        if (gitRepoCommit == null) {
            return null;
        }

        EntityRepositoryCommit repoCommit = getRepoCommitBySHA(gitRepoCommit.getSHA1(), dao);

        if (repoCommit == null) {
            repoCommit = new EntityRepositoryCommit();
            repoCommit.setMineredAt(new Date());

//          createParents(repoCommit, gitRepoCommit.getParents(), gitRepo, dao);
            repoCommit.setSha(gitRepoCommit.getSHA1());
            repoCommit.setUrl(null);
            dao.insert(repoCommit);
        }
        if (repoCommit.getCommit() == null) {
            repoCommit.setCommit(CommitServices.createEntity(gitRepoCommit, dao));
        }
        if (repoCommit.getAuthor() == null) {
            try {
                repoCommit.setAuthor(UserServices.createEntity(gitRepoCommit.getAuthor(), dao, false));
            } catch (IOException ex) {
                Logger.getLogger(RepositoryCommitServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (repoCommit.getCommitter() == null) {
            try {
                repoCommit.setCommitter(UserServices.createEntity(gitRepoCommit.getCommitter(), dao, false));
            } catch (IOException ex) {
                Logger.getLogger(RepositoryCommitServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return repoCommit;
    }

    private static EntityRepositoryCommit getRepoCommitBySHA(String sha, GenericDao dao) {
        List<EntityRepositoryCommit> repoCommits = dao.executeNamedQueryWithParams("RepositoryCommit.findBySHA", new String[]{"sha"}, new Object[]{sha}, true);
        if (!repoCommits.isEmpty()) {
            return repoCommits.get(0);
        }
        return null;
    }
//    private static void createParents(EntityRepositoryCommit repoCommit, List<Commit> gitParents, Repository gitRepo, GenericDao dao) {
//        if (gitParents != null) {
//            for (Commit gitParent : gitParents) {
//                repoCommit.addParent(CommitServices.createEntity(gitParent, gitRepo, dao));
//            }
//        }
//    }

    public static List<GHCommit> getGitRepoCommitsFromPullRequest(EntityPullRequest pullRequest,
            EntityRepository repositoryToMiner) {
        try {
            // https://api.github.com/repos/jashkenas/coffee-script/pulls/2682/commits

//        String id = getId(repositoryToMiner);
//        StringBuilder uri = new StringBuilder("/repos");
//        uri.append(id);
//        uri.append("/pulls");
//        uri.append("/").append(pullRequest.getNumber());
//        uri.append("/commits");
//        PagedRequest<RepositoryCommit> request = new PagedRequest<>(1, 100);
//        request.setUri(uri);
//        request.setType(new TypeToken<List<RepositoryCommit>>() {
//        }.getType());
//        PageIterator<RepositoryCommit> iterator = new PageIterator<>(request, AuthServices.getGitHubClient());
            List<GHCommit> gitCommitsList = new ArrayList<>();
            List<GHPullRequestCommitDetail> gitPRCommitsList = new ArrayList<>();
            
            gitPRCommitsList = AuthServices.getGitHubClient().getRepository(repositoryToMiner.getOwner().getLogin() + "/"
                    + repositoryToMiner.getName()).getPullRequest(pullRequest.getNumber()).
                    listCommits().asList();

            for (GHPullRequestCommitDetail gitPRCommit : gitPRCommitsList) {

                gitCommitsList.add(AuthServices.getGitHubClient().
                        getRepository(repositoryToMiner.getOwner().getLogin() + "/"
                                + repositoryToMiner.getName()).getCommit(gitPRCommit.getSha()));

            }

//        for (GHPullRequestCommitDetail prCommit : ghPRCommit) {
//            prCommit.getCommit()
//        }
//        try {
//            while (iterator.hasNext()) {
//                elements.addAll(iterator.next());
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return getGitRepoCommitsFromPullRequest(pullRequest, repositoryToMiner);
//        }
            return gitCommitsList;
        } catch (IOException ex) {
            Logger.getLogger(RepositoryCommitServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static String getId(EntityRepository repository) {
        return "/" + repository.getOwner().getLogin() + "/" + repository.getName();
    }

    public static GHCommit getGitRepositoryCommit(GHRepository gitRepo, GHCommit gitRepoCommit, OutLog out, int nRetries) throws Exception {
        if (nRetries <= 0) {
            return null;
        }
        try {

            return AuthServices.getGitHubClient().getRepository(gitRepo.getFullName()).getCommit(gitRepoCommit.getSHA1());
        } catch (Exception ex) {
            ex.printStackTrace();
            out.printLog("Erro de conexão: " + ex);
            out.printLog("Tentando novamente (" + nRetries + ") ...");
            return getGitRepositoryCommit(gitRepo, gitRepoCommit, out, nRetries);
        }
    }
}
