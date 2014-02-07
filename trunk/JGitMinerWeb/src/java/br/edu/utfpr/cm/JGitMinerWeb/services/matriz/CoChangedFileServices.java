/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matriz;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.pojo.miner.EntityCommitFile;
import br.edu.utfpr.cm.JGitMinerWeb.pojo.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.pojo.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.pojo.miner.EntityRepositoryCommit;
import br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary.AuxCoChanged;
import br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary.AuxCoChangedFiles;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author geazzy
 */
public class CoChangedFileServices extends AbstractMatrizServices {

    List<AuxCoChangedFiles> changedList;
    private List<AuxCoChanged> coChangedList;

    public CoChangedFileServices(GenericDao dao) {
        super(dao);
        changedList = new ArrayList<>();
        coChangedList = new ArrayList<>();

    }

    public CoChangedFileServices(GenericDao dao, EntityRepository repository, Map params) {
        super(dao, repository, params);
        changedList = new ArrayList<>();
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

            for (AuxCoChangedFiles commit : changedList) {
                setPairOfCoChangedFiles(commit);
            }

           // addToEntityMatrizNodeList(changedList);
            addToEntityMatrizNodeList(coChangedList);
        }

    }

    private void setPairOfCoChangedFiles(AuxCoChangedFiles commit) {
        
        List<EntityCommitFile> arquivos = Lists.newArrayList(commit.getArquivos());
        
        for (int i = 0; i < arquivos.size(); i++) {
            Integer next = i + 1;
            if (next < arquivos.size()) {
                coChangedList.add(new AuxCoChanged(commit.getIssueNumber(),
                        commit.getUrlCommit(), arquivos.get(i).getFilename(),
                        arquivos.get(next).getFilename()));
            }
        }
    }

    private void getRepositoryCommitsFromPullRequest(List<EntityPullRequest> pullRequestListParam) {

        for (EntityPullRequest pullrequest : pullRequestListParam) {
            getFilesFromRepositoryCommits(pullrequest);
        }
    }

    private void getFilesFromRepositoryCommits(EntityPullRequest pullrequest) {

        for (EntityRepositoryCommit repositoryCommit : pullrequest.getRepositoryCommits()) {

            if (repositoryCommit.getFiles().size() > 0) {
                changedList.add(new AuxCoChangedFiles(pullrequest.getNumber(),
                        repositoryCommit.getUrl(), repositoryCommit.getFiles()));
            }

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
        return "issueNumber;url;arquivo1;arquivo2;";
    }

}
