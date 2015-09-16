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
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxPairOfFiles;
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
import org.joda.time.DateTime;

/**
 *
 * @author geazzy
 */
public class CoChangedFileServices extends AbstractMatrixServices {

    private Map<Long, List<EntityCommitFile>> changedList;
    private List<AuxPairOfFiles> coChangedList;

    public CoChangedFileServices(GenericDao dao, OutLog out) {

        super(dao, out);
        changedList = new HashMap<>();
        coChangedList = new ArrayList<>();
    }

    public CoChangedFileServices(GenericDao dao, EntityRepository repository, List<EntityMatrix> matricesToSave, Map params, OutLog out) {

        super(dao, repository, matricesToSave, params, out);
        changedList = new HashMap<>();
        coChangedList = new ArrayList<>();
    }

    protected Integer getMilestoneNumber() {
        String mileNumber = params.get("milestoneNumber") + "";
        return Util.tratarStringParaInt(mileNumber);
    }

    private Date getBeginDate() {
        return getDateParam("beginDate");
    }

    private Date getEndDate() {
        return getDateParam("endDate");
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

                setPairOfCoChangedFilesPerPullRequest(changedList.get((Long) idPullRequest), (Long) idPullRequest);
            }

            // addToEntityMatrizNodeList(coChangedList);
            EntityMatrix matrix = new EntityMatrix();
            matrix.setNodes(objectsToNodes(coChangedList));

            try {
                sleep(1l);
            } catch (InterruptedException ex) {
                Logger.getLogger(CoChangedFileServices.class.getName()).log(Level.SEVERE, null, ex);
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
                    
                    //add -verificando data
                   if ( isBetween(repositoryCommit.getCommit().getCommitter().getDateCommitUser(), 
                          getBeginDate(), getEndDate())){
                       addFilesToMap(pullrequest.getIdPullRequest(), repositoryCommit.getFiles());
                   }

                    
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

    private boolean isBetween(Date dateCommitUser, Date beginDate, Date endDate) {

        DateTime dateCommit = new DateTime(dateCommitUser);
        DateTime begin = new DateTime(beginDate);
        DateTime end = new DateTime(endDate);

        if (dateCommit.isAfter(begin) && dateCommit.isBefore(end)) {
            return true;
        } else {
            return false;
        }
    }

}
