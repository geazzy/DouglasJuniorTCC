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
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityIssueEvent;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
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
public class CoChangedStatusFileServices extends AbstractMatrixServices {

    private Map<EntityPullRequest, List<EntityCommitFile>> changedList;
    private List<String> coChangedList;

    public CoChangedStatusFileServices(GenericDao dao, OutLog out) {

        super(dao, out);
        changedList = new HashMap<>();
        coChangedList = new ArrayList<>();
    }

    public CoChangedStatusFileServices(GenericDao dao, EntityRepository repository, List<EntityMatrix> matricesToSave, Map params, OutLog out) {

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

            for (Object pullRequest : changedList.keySet()) {

                setPairOfCoChangedFilesPerPullRequest(changedList.get((EntityPullRequest) pullRequest), (EntityPullRequest) pullRequest);
            }

            // addToEntityMatrizNodeList(coChangedList);
            EntityMatrix matrix = new EntityMatrix();
            matrix.setNodes(objectsToNodes(coChangedList));

            try {
                sleep(1l);
            } catch (InterruptedException ex) {
                Logger.getLogger(CoChangedStatusFileServices.class.getName()).log(Level.SEVERE, null, ex);
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
                    Integer closed = 0, reopened = 0, subscribed = 0, merged = 0,
                            referenced = 0, mentioned = 0, assigned = 0;
                    for (EntityIssueEvent evento : pullRequest.getIssue().getEvents()) {
                        switch (evento.getEvent()) {
                            case "closed":
                                closed++;
                                break;
                            case "reopened":
                                reopened++;
                                break;
                            case "subscribed":
                                subscribed++;
                                break;
                            case "merged":
                                merged++;
                                break;
                            case "referenced":
                                referenced++;
                                break;
                            case "mentioned":
                                mentioned++;
                                break;
                            case "assigned":
                                assigned++;
                                break;
                            default:
                                throw new IllegalArgumentException("Invalid day of the week: " + evento.getEvent());
                        }
                    }

                    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    List<String> filesNoDuplicated = new ArrayList<>();
                    Map<String, Integer> additionsMaps = new HashMap<>();
                    Map<String, Integer> deletionsMaps = new HashMap<>();
                    Map<String, Integer> changeMaps = new HashMap<>();

                    for (EntityCommitFile ecf : files) {

                        if (!filesNoDuplicated.contains(ecf.getFilename())) {
                            filesNoDuplicated.add(ecf.getFilename());
                            additionsMaps.put(ecf.getFilename(), ecf.getAdditions());
                            deletionsMaps.put(ecf.getFilename(), ecf.getDeletions());
                            changeMaps.put(ecf.getFilename(), ecf.getChanges());
                        } else {
                            additionsMaps.put(ecf.getFilename(),
                                    (additionsMaps.get(ecf.getFilename()) + ecf.getAdditions()));
                            deletionsMaps.put(ecf.getFilename(),
                                    deletionsMaps.get(ecf.getFilename()) + ecf.getDeletions());
                            changeMaps.put(ecf.getFilename(),
                                    changeMaps.get(ecf.getFilename()) + ecf.getChanges());
                        }
                    }

                    System.out.println("********************************************************");
                    System.out.println("additions: " + files.get(i).getAdditions());
                    System.out.println("changes: " + files.get(i).getChanges());
                    System.out.println("deletions: " + files.get(i).getDeletions());
                    System.out.println("additions map: " + additionsMaps.get(files.get(i).getFilename()));
                    System.out.println("changes map: " + changeMaps.get(files.get(i).getFilename()));
                    System.out.println("deletions map: " + deletionsMaps.get(files.get(i).getFilename()));
                    // System.out.println("patchs: "+files.get(i).getPatch());
                    System.out.println("url: " + pullRequest.getUrl());
                    System.out.println("arq modi: " + pullRequest.getChangedFiles());
                    System.out.println("files size: " + files.size());
                    System.out.println("files not duplicated size: " + filesNoDuplicated.size());
                    System.out.println("pull request size: " + pullRequest.getRepositoryCommits().size());
                    System.out.println("pull additions " + pullRequest.getAdditions());

                    Integer adicionadas = additionsMaps.get(files.get(i).getFilename()) + additionsMaps.get(files.get(next).getFilename());
                    Integer removidas = deletionsMaps.get(files.get(i).getFilename()) + deletionsMaps.get(files.get(next).getFilename());
                    Integer modificadas = changeMaps.get(files.get(i).getFilename()) + changeMaps.get(files.get(next).getFilename());
                    Integer arquivosModJuntos = filesNoDuplicated.size();

                    coChangedList.add(pullRequest.getId() + ";"
                            + files.get(i).getFilename() + ";" + files.get(next).getFilename() + ";"
                            + files.get(i).getRepositoryCommit().getSha() + ";"
                            + files.get(next).getRepositoryCommit().getSha() + ";"
                            + pullRequest.getIssue().getStateIssue() + ";"
                            + closed + ";" + reopened + ";" + subscribed + ";"
                            + merged + ";" + referenced + ";" + mentioned
                            + ";" + assigned + ";"
                            + pullRequest.getIssue().getEvents().size() + ";"
                            + arquivosModJuntos + ";"
                            + adicionadas + ";"
                            + removidas + ";" + modificadas + ";");

                }

            }
        }
    }

    private void getRepositoryCommitsFromPullRequest(List<EntityPullRequest> pullRequestListParam) {

        for (EntityPullRequest pullrequest : pullRequestListParam) {

//            System.out.println(pullrequest.getUrl() + " " + pullrequest.getStatePullRequest());
//
//            for (EntityIssueEvent status : pullrequest.getIssue().getEvents()) {
//                System.out.println(status.getEvent());
//            }
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
        return "idPullRequest;arquivo1;arquivo2;shaRepoCommit1;shaRepoCommit2;statusAtual;closed;reopened;subscribed;merged;referenced;mentioned;assigned;totalStatus;ArqModJunto;Add;Rem;Mod;";

    }

}
