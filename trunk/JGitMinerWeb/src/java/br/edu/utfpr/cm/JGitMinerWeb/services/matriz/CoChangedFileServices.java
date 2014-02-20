/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matriz;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityCommitFile;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityCommitUser;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
import br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary.AuxCoChanged;
import br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary.AuxCoChangedFiles;
import br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary.AuxReputation;
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

//    private List<AuxCoChangedFiles> changedList;
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

            System.out.println(changedList);
            
            for (Object key : changedList.keySet()) {
                System.out.println("*******************************************************************************");
                System.out.println("issue number: "+ (Integer)key + 
                        " arquivos associados: "+ changedList.get((Integer)key).size());  
                
                for (EntityCommitFile file : changedList.get((Integer)key)) {
                  
                    System.out.println("===============================================================================");
                    System.out.println("file name: "+file.getFilename());
                    System.out.println("url comit: "+ file.getRepositoryCommit().getUrl());
                    
                    System.out.println(" commit autor: "+ file.getRepositoryCommit().getCommit().getAuthor().getName()
                            + " - data "+file.getRepositoryCommit().getCommit().getAuthor().getDateCommitUser());
                    System.out.println("commit commiter: " + file.getRepositoryCommit().getCommit().getCommitter()
                            +" - data -"+file.getRepositoryCommit().getCommit().getCommitter().getDateCommitUser());
                    System.out.println("data da MINERACAO: "+ file.getRepositoryCommit().getCommit().getMineredAt());
                  
                    System.out.println("-----------------------------------------");
                }
            }
   
//            for (AuxCoChangedFiles commit : changedList) {
//                setPairOfCoChangedFilesPerPullRequest(commit);
//            }

            // addToEntityMatrizNodeList(changedList);
            addToEntityMatrizNodeList(coChangedList);
        }

    }

    private void setPairOfCoChangedFilesPerPullRequest(AuxCoChangedFiles commit) {

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

            for (EntityRepositoryCommit repositoryCommit : pullrequest.getRepositoryCommits()) {
                
                if (!repositoryCommit.getFiles().isEmpty()) {
                    addFilesToMap(pullrequest.getNumber(), repositoryCommit.getFiles());
                }else{
                    System.out.println("issue sem arquivo: " +pullrequest.getNumber()+ 
                            " repositorycommit url: "+repositoryCommit.getUrl());
                    
                    System.out.println("comitado em: "+repositoryCommit.getCommit().getAuthor().getDateCommitUser()+" minerado em "+repositoryCommit.getMineredAt());
                    System.out.println("url "+repositoryCommit.getCommit().getUrl());
                           
                }

            }
        }
    }

    private void addFilesToMap(Integer pullNumber, Set<EntityCommitFile> repositoryCommitFiles) {
       
        if(changedList.containsKey(pullNumber)){
            
            List<EntityCommitFile> files = changedList.get(pullNumber);
            System.out.println("Arqs ANTES de concatenar:" +files.size());
            files.addAll(repositoryCommitFiles);
            System.out.println("Arqs DEPOIS de concatenar:" +files.size());
            
            changedList.put(pullNumber, files);
            
        }else{
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
        return "issueNumber;url;arquivo1;arquivo2;";
    }

}
