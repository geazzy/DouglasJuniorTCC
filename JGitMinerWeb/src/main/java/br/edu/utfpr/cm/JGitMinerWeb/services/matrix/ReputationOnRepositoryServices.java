/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matrix;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrix;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityCommitUser;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityUser;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxCommittersRank;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxMessagesRank;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxReputation;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxReputationRank;
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
public class ReputationOnRepositoryServices extends AbstractMatrixServices {

    private List<AuxReputation> result;

    public ReputationOnRepositoryServices(GenericDao dao, OutLog out) {
        super(dao, out);
        result = new ArrayList<>();
    }

    public ReputationOnRepositoryServices(GenericDao dao, EntityRepository repository, List<EntityMatrix> matricesToSave, Map params, OutLog out) {
        super(dao, repository, matricesToSave, params, out);
        result = new ArrayList<>();
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
        Map rankMessages;
        Map rankCommitters;

        if (getRepository() == null) {
            throw new IllegalArgumentException("Parâmetro Repository não pode ser nulo.");
        } else {
            if (getMilestoneNumber() > 0) {
                rankCommitters = setCommittersRank(getPullRequestByMilestone());
                rankMessages = setMessagesRank(getPullRequestByMilestone());
            } else {
                rankCommitters = setCommittersRank(getPullRequestByDate());
                rankMessages = setMessagesRank(getPullRequestByDate());
            }
        }

        for (Object key : rankMessages.keySet()) {

            result.add(new AuxReputation(((EntityUser) key).getLogin(), 0,
                    (Integer) rankMessages.get(key)));
        }


        for (Object key : rankCommitters.keySet()) {
            
//            AuxReputationRank.updateRank(((EntityUser) key), 
//                    (Integer)rankCommitters.get(key));
            
            result.add(new AuxReputation(((EntityUser) key).getLogin(),
                    (Integer) rankCommitters.get(key), 0));

        }
        
//        System.out.println("Rank size: "+AuxReputationRank.size());
        
        //addToEntityMatrizNodeList(result);
        EntityMatrix matrix = new EntityMatrix();
        matrix.setNodes(objectsToNodes(result));
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
        return "user;numberOfCommits;numberOfMensages";
    }

    private Map setCommittersRank(List<EntityPullRequest> pullRequest) {

        AuxCommittersRank commitersRank = new AuxCommittersRank(pullRequest);

        commitersRank.setRankValues();

        return commitersRank.getRank();
    }

    private Map setMessagesRank(List<EntityPullRequest> pullRequest) {

        AuxMessagesRank messagesRank = new AuxMessagesRank(pullRequest);

        messagesRank.setRankValues();

        return messagesRank.getRank();
    }

}
