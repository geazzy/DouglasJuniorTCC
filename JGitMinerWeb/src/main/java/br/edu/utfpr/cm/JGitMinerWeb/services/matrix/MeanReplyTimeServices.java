/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matrix;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrix;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityComment;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxMeanReplyTime;
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
public class MeanReplyTimeServices extends AbstractMatrixServices {

    private List<AuxMeanReplyTime> issueList;

    public MeanReplyTimeServices(GenericDao dao, OutLog out) {
        super(dao, out);
        issueList = new ArrayList<>();
    }

    public MeanReplyTimeServices(GenericDao dao, EntityRepository repository, List<EntityMatrix> matricesToSave, Map params, OutLog out) {
        super(dao, repository, matricesToSave, params, out);
        issueList = new ArrayList<>();
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
        }

        if (getMilestoneNumber() > 0) {
            getIssuesByMilestone();
        } else {
            getIssuesByDate();
        }

        setIssueComments();

        setMeanReplyTime();

        System.out.println("Result: . " + issueList.size());

        //addToEntityMatrizNodeList(issueList);
        EntityMatrix matrix = new EntityMatrix();
        matrix.setNodes(objectsToNodes(issueList));
        matricesToSave.add(matrix);
    }

    @Override
    public String getHeadCSV() {
        return "IssueNumber;Mean;url";
    }

    private void getIssuesByMilestone() {

        int mileNumber = new Integer(getMilestoneNumber());

        String jpql = "SELECT NEW " + AuxMeanReplyTime.class.getName() + "(p.issue) "
                + "FROM "
                + "EntityPullRequest p "
                + "WHERE "
                + "p.repository = :repository  AND "
                + "p.issue.commentsCount > 0  AND "
                + (mileNumber > 0 ? "p.issue.milestone.number = :milestoneNumber " : "")
                + "";
        //+ " GROUP BY p.number ";

        System.out.println(jpql);

        List<AuxMeanReplyTime> query = dao.selectWithParams(jpql,
                new String[]{
                    "repository",
                    mileNumber > 0 ? "milestoneNumber" : "#none#",},
                new Object[]{
                    getRepository(),
                    mileNumber,});

        System.out.println("query: " + query.size());

        issueList = query;
    }

    private void getIssuesByDate() {
        String jpql = "SELECT NEW " + AuxMeanReplyTime.class.getName() + "(p.issue) "
                + "FROM "
                + "EntityPullRequest p "
                + "WHERE "
                + "p.repository = :repository  AND "
                + "p.issue.commentsCount > 0  AND "
                + "p.issue.createdAt >= :dataInicial AND "
                + "p.issue.createdAt <= :dataFinal "
                + "";
        // + " GROUP BY p.number ";

        System.out.println(jpql);

        List<AuxMeanReplyTime> query = dao.selectWithParams(jpql,
                new String[]{
                    "repository",
                    "dataInicial",
                    "dataFinal"
                }, new Object[]{
                    getRepository(),
                    getBeginDate(),
                    getEndDate()
                });

        System.out.println("query: " + query.size());

        issueList = query;
    }

    private void setIssueComments() {

        for (AuxMeanReplyTime issue : issueList) {
            String jpql2 = "SELECT c "
                    + "FROM "
                    + "EntityComment c "
                    + "WHERE "
                    + "c.issue.number  = :number  ";

            System.out.println("jpql2: " + jpql2);

            issue.setCommentsList(dao.selectWithParams(jpql2,
                    new String[]{
                        "number",}, new Object[]{
                        issue.getIssue().getNumber(),}));

            System.out.println("numero de comentario: " + issue.getCommentsList().size());
        }

    }

    private void setMeanReplyTime() {

        for (AuxMeanReplyTime issue : issueList) {
            issue.setMean();
        }
    }

}
