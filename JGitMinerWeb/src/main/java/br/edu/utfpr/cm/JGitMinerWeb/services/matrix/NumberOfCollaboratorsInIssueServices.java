/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matrix;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrix;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxCollabPerIssue;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import br.edu.utfpr.cm.JGitMinerWeb.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author geazzy
 */
public class NumberOfCollaboratorsInIssueServices extends AbstractMatrixServices {

    private List<AuxCollabPerIssue> collabList;

    public NumberOfCollaboratorsInIssueServices(GenericDao dao, OutLog out) {
        super(dao, out);
        collabList = new ArrayList<>();
    }

    public NumberOfCollaboratorsInIssueServices(GenericDao dao, EntityRepository repository, List<EntityMatrix> matricesToSave, Map params, OutLog out) {
        super(dao, repository, matricesToSave, params, out);
        collabList = new ArrayList<>();
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

                setCollabList(getPullRequestByMilestone());
            } else {
                setCollabList(getPullRequestByDate());
            }
        }

        setNumberOfCollabsPerIssue();

        //  addToEntityMatrizNodeList(collabList);
        EntityMatrix matrix = new EntityMatrix();
        matrix.setNodes(objectsToNodes(collabList));
        matricesToSave.add(matrix);
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
        return "issueNumber;numberOfCollabs;url;";
    }

    private void setCollabList(List<EntityPullRequest> pullRequestList) {

        for (EntityPullRequest entityPullRequest : pullRequestList) {
            collabList.add(new AuxCollabPerIssue(entityPullRequest.getNumber(),
                    entityPullRequest.getIssue().getUserIssue(),
                    entityPullRequest.getIssue().getComments(),
                    entityPullRequest.getUrl(),
                    entityPullRequest.getRepository().getCollaborators()));
        }
    }

    private void setNumberOfCollabsPerIssue() {

        for (AuxCollabPerIssue issue : collabList) {
            issue.setNumberOfCollabs();
        }
    }

}
