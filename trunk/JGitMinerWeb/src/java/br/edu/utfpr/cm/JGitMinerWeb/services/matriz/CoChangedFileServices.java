/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matriz;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityCommitFile;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
import br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary.AuxCoChanged;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author geazzy
 */
public class CoChangedFileServices extends AbstractMatrizServices {

    private Map<Integer, List<EntityCommitFile>> changedList;
    private List<AuxCoChanged> coChangedList;

    public CoChangedFileServices(GenericDao dao, OutLog out) {
        super(dao, out);
        changedList = new HashMap<>();
        coChangedList = new ArrayList<>();
    }

    public CoChangedFileServices(GenericDao dao, EntityRepository repository, Map params, OutLog out) {
        super(dao, repository, params, out);
        changedList = new HashMap<>();
        coChangedList = new ArrayList<>();
    }

    @Override
    public void run() {
        if (getRepository() == null) {
            throw new IllegalArgumentException("Parâmetro Repository não pode ser nulo.");
        } else {

            if (getMilestoneNumber() > 0) {
                getRepositoryCommitsFromPullRequest(getPullRequestByMilestone());
            } else {
                getRepositoryCommitsFromPullRequest(getPullRequestByDate());
            }

            for (Object issueNumber : changedList.keySet()) {

                setPairOfCoChangedFilesPerPullRequest(changedList.get((Integer) issueNumber), (Integer) issueNumber);
            }

            addToEntityMatrizNodeList(coChangedList);
           
            Set<AuxCoChanged> auxset = new HashSet<>(coChangedList);
            
            System.out.println("cochange size list: "+ coChangedList.size());
            System.out.println("auxset size set: "+ auxset.size());
                        
        }

    }

    private void setPairOfCoChangedFilesPerPullRequest(List<EntityCommitFile> arquivos, Integer issueNumber) {

        for (int i = 0; i < arquivos.size(); i++) {

            for (int next = i + 1; next < arquivos.size(); next++) {

                if (!arquivos.get(i).getFilename().equals(arquivos.get(next).getFilename())) {
                    coChangedList.add(new AuxCoChanged(issueNumber,
                            arquivos.get(i).getFilename(), arquivos.get(next).getFilename(),
                            arquivos.get(i).getRepositoryCommit().getSha(),
                            arquivos.get(next).getRepositoryCommit().getSha()));
                }

            }
        }
    }

    private void getRepositoryCommitsFromPullRequest(List<EntityPullRequest> pullRequestListParam) {

        for (EntityPullRequest pullrequest : pullRequestListParam) {

            for (EntityRepositoryCommit repositoryCommit : pullrequest.getRepositoryCommits()) {

                    if (repositoryCommit.getFiles().size() > 100) {
                        out.printLog("Commit IGNORADO - muitos arquivos: " + repositoryCommit.getUrl() + " tamanho: " + repositoryCommit.getFiles().size());
                    } else {
                        addFilesToMap(pullrequest.getNumber(), repositoryCommit.getFiles());
                    }
            }
        }
    }

    private void addFilesToMap(Integer pullNumber, Set<EntityCommitFile> repositoryCommitFiles) {

        if (changedList.containsKey(pullNumber)) {

            List<EntityCommitFile> files = changedList.get(pullNumber);
            files.addAll(repositoryCommitFiles);
            changedList.put(pullNumber, files);

        } else {
            changedList.put(pullNumber, Lists.newArrayList(repositoryCommitFiles));
        }

    }

    private List<EntityPullRequest> getPullRequestByMilestone() {

        return dao.getEntityManager()
                .createNamedQuery("PullRequest.findByMilestoneAndRepository")
                .setParameter("repository", getRepository())
                .setParameter("milestone", getMilestoneNumber())
                .getResultList();
    }

    private List<EntityPullRequest> getPullRequestByDate() {

        return dao.getEntityManager()
                .createNamedQuery("PullRequest.findByDateAndRepository")
                .setParameter("repository", getRepository())
                .setParameter("dataInicial", getBeginDate())
                .setParameter("dataFinal", getEndDate())
                .getResultList();
    }

    @Override
    public String getHeadCSV() {
        return "issueNumber;arquivo1;arquivo2;idCommitArq1;idCommitArq2;";
    }

}
