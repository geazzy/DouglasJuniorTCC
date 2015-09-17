/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matrix;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrix;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrixNode;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityCommitFile;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxNewPairOfFiles;
import br.edu.utfpr.cm.JGitMinerWeb.util.GeoCodeUtil;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import br.edu.utfpr.cm.JGitMinerWeb.util.Util;
import com.google.common.collect.Lists;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author geazzy
 */
public class CoChangedLocationServices extends AbstractMatrixServices {

    private Map<EntityPullRequest, List<EntityCommitFile>> changedList;
    private List<AuxNewPairOfFiles> coChangedList;

    public CoChangedLocationServices(GenericDao dao, OutLog out) {

        super(dao, out);
        changedList = new HashMap<>();
        coChangedList = new ArrayList<>();
    }

    public CoChangedLocationServices(GenericDao dao, EntityRepository repository, List<EntityMatrix> matricesToSave, Map params, OutLog out) {

        super(dao, repository, matricesToSave, params, out);
        changedList = new HashMap<>();
        coChangedList = new ArrayList<>();
    }

    protected Integer getMilestoneNumber() {
        String mileNumber = params.get("milestoneNumber") + "";
        return Util.tratarStringParaInt(mileNumber);
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

            for (EntityPullRequest idPullRequest : changedList.keySet()) {

                setPairOfCoChangedFilesPerPullRequest(changedList.get((EntityPullRequest) idPullRequest), (EntityPullRequest) idPullRequest);
            }

           // addToEntityMatrizNodeList(coChangedList);
        
             EntityMatrix matrix = new EntityMatrix();
            matrix.setNodes(objectsToNodes(coChangedList));

           
            try {
                sleep(1l);
            } catch (InterruptedException ex) {
                Logger.getLogger(CoChangedLocationServices.class.getName()).log(Level.SEVERE, null, ex);
            }

            saveMatrix(matrix);

//            Set<AuxPairOfFiles> auxset = new HashSet<>(coChangedList);
//
//            System.out.println("cochange size list: " + coChangedList.size());
//            System.out.println("auxset size set: " + auxset.size());
        }

    }
    
     private void saveMatrix(EntityMatrix entityMatrix) {
        out.printLog("Iniciando salvamento da rede.");
        List<EntityMatrixNode> matrixNodes = entityMatrix.getNodes();
        entityMatrix.setNodes(new ArrayList<EntityMatrixNode>());
        entityMatrix.getParams().putAll(params);
        entityMatrix.setRepository(getRepository() + "");
        entityMatrix.setClassServicesName(this.getClass().getName());
        entityMatrix.setLog(out.getLog().toString());
        dao.insert(entityMatrix);
        for (Iterator<EntityMatrixNode> it = matrixNodes.iterator(); it.hasNext();) {
            EntityMatrixNode node = it.next();
            node.setMatrix(entityMatrix);
            entityMatrix.getNodes().add(node);
            dao.insert(node);
            it.remove();
        }
        entityMatrix.setStoped(new Date());
        entityMatrix.setComplete(true);
        out.printLog("Concluida geração da rede.");
        entityMatrix.setLog(out.getLog().toString());
        dao.edit(entityMatrix);
        out.printLog("");
    }

    private void setPairOfCoChangedFilesPerPullRequest(List<EntityCommitFile> files, EntityPullRequest pullRequest) {

        for (int i = 0; i < files.size(); i++) {

            for (int next = i + 1; next < files.size(); next++) {

                //forma par de arquivo SOMENTE quando eles pertencem ao mesmo commit
                if (files.get(i).getRepositoryCommit().getSha().equals(files.get(next).getRepositoryCommit().getSha())) {
                    
                    System.out.println("Autor i: "+files.get(i).getRepositoryCommit().getAuthor().getLogin()+
                            " ; "+files.get(i).getRepositoryCommit().getAuthor().getLocation());
                    
//                    System.out.println("Location: "+
//                            GeoCodeUtil.getCountry(files.get(i).getRepositoryCommit().getAuthor().getLocation()));
                    
                    coChangedList.add(new AuxNewPairOfFiles(pullRequest,
                            files.get(i), files.get(next),
                            files.get(i).getRepositoryCommit(),
                            files.get(next).getRepositoryCommit()));
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
                    addFilesToMap(pullrequest, repositoryCommit.getFiles());
                }
            }
        }
    }

    private void addFilesToMap(EntityPullRequest pullRequest, Set<EntityCommitFile> repositoryCommitFiles) {

        if (changedList.containsKey(pullRequest)) {

            List<EntityCommitFile> files = changedList.get(pullRequest);
            files.addAll(repositoryCommitFiles);
            changedList.put(pullRequest, files);

        } else {
            changedList.put(pullRequest, Lists.newArrayList(repositoryCommitFiles));
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
        return "idPullRequest;arquivo1;arquivo2;shaRepoCommit1;shaRepoCommit2;location";
    }

}
