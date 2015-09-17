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
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxMentionsPerIssue;
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
public class NumberOfMentionsPerIssueServices extends AbstractMatrixServices {

    private List<AuxMentionsPerIssue> mentionsList;

    public NumberOfMentionsPerIssueServices(GenericDao dao, OutLog out) {
        super(dao, out);
        mentionsList = new ArrayList<AuxMentionsPerIssue>();
    }

    public NumberOfMentionsPerIssueServices(GenericDao dao, EntityRepository repository, List<EntityMatrix> matricesToSave, Map params, OutLog out) {
        super(dao, repository, matricesToSave, params, out);
        mentionsList = new ArrayList<AuxMentionsPerIssue>();
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
                setMentionsList(getPullRequestByMilestone());
            } else {
                setMentionsList(getPullRequestByDate());
            }
        }

        setNumberOfMentions();

        //addToEntityMatrizNodeList(mentionsList);
        EntityMatrix matrix = new EntityMatrix();
        matrix.setNodes(objectsToNodes(mentionsList));
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
        return "numberIssue;numberOfMentions;url;";
    }

    private void setMentionsList(List<EntityPullRequest> pullRequestList) {

        for (EntityPullRequest entityPullRequest : pullRequestList) {
            mentionsList.add(
                    new AuxMentionsPerIssue(entityPullRequest.getIssue().getNumber(),
                            entityPullRequest.getIssue().getComments(),
                            entityPullRequest.getUrl(), entityPullRequest.getBody()));
        }
    }

    private void setNumberOfMentions() {

        for (AuxMentionsPerIssue issue : mentionsList) {
            issue.setNumberOfMentions();
        }
    }

}
