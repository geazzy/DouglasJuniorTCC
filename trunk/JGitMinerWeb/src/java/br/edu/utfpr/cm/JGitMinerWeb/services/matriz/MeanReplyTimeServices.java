/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matriz;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.pojo.miner.EntityComment;
import br.edu.utfpr.cm.JGitMinerWeb.pojo.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary.AuxMeanReplyTime;
import java.util.List;
import java.util.Map;

/**
 *
 * @author geazzy
 */
public class MeanReplyTimeServices extends AbstractMatrizServices {

    private List<AuxMeanReplyTime> issueList;

    public MeanReplyTimeServices(GenericDao dao) {
        super(dao);
    }

    public MeanReplyTimeServices(GenericDao dao, EntityRepository repository, Map params) {
        super(dao, repository, params);
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

        addToEntityMatrizNodeList(issueList);
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
                + " GROUP BY p.number ";

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
                + " GROUP BY p.number ";

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
