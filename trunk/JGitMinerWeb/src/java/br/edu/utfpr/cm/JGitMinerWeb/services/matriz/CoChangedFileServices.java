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
import br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary.AuxPairOfFiles;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author geazzy
 */
public class CoChangedFileServices extends AbstractMatrizServices {

    private Map<Long, List<EntityCommitFile>> changedList;
    private List<AuxPairOfFiles> coChangedList;

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

            for (Object idPullRequest : changedList.keySet()) {

                setPairOfCoChangedFilesPerPullRequest(changedList.get((Long)idPullRequest), (Long)idPullRequest);
            }

            addToEntityMatrizNodeList(coChangedList);

//            Set<AuxPairOfFiles> auxset = new HashSet<>(coChangedList);
//
//            System.out.println("cochange size list: " + coChangedList.size());
//            System.out.println("auxset size set: " + auxset.size());

        }

    }

    private void setPairOfCoChangedFilesPerPullRequest(List<EntityCommitFile> files, Long idPullRequest) {

        for (int i = 0; i < files.size(); i++) {

            for (int next = i + 1; next < files.size(); next++) {

                //forma par de arquivo SOMENTE quando eles pertencem ao mesmo commit
                if (files.get(i).getRepositoryCommit().getSha().equals(files.get(next).getRepositoryCommit().getSha())) {
                    coChangedList.add(new AuxPairOfFiles(idPullRequest,
                            files.get(i).getFilename(), files.get(next).getFilename(),
                            files.get(i).getRepositoryCommit().getSha(),
                            files.get(next).getRepositoryCommit().getSha()));
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
                    addFilesToMap(pullrequest.getIdPullRequest(), repositoryCommit.getFiles());
                }
            }
        }
    }

    private void addFilesToMap(Long idPullRequest, Set<EntityCommitFile> repositoryCommitFiles) {

        if (changedList.containsKey(idPullRequest)) {

            List<EntityCommitFile> files = changedList.get(idPullRequest);
            files.addAll(repositoryCommitFiles);
            changedList.put(idPullRequest, files);

        } else {
            changedList.put(idPullRequest, Lists.newArrayList(repositoryCommitFiles));
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
        return "idPullRequest;arquivo1;arquivo2;shaRepoCommit1;shaRepoCommit2;";
    }

}
