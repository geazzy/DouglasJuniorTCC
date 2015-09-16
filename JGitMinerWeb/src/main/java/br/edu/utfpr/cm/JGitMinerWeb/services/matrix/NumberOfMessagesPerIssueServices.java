/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matrix;

import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxNumberOfMessages;
import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrix;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import br.edu.utfpr.cm.JGitMinerWeb.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author geazzy Retonar o número de comentários de cada issue associada a um
 * pullrequest
 */
public class NumberOfMessagesPerIssueServices extends AbstractMatrixServices {

    public NumberOfMessagesPerIssueServices(GenericDao dao, OutLog out) {
        super(dao, out);
    }

    public NumberOfMessagesPerIssueServices(GenericDao dao, EntityRepository repository, List<EntityMatrix> matricesToSave, Map params, OutLog out) {
        super(dao, repository, matricesToSave, params, out);
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
        System.out.println(params);

        if (getRepository() == null) {
            throw new IllegalArgumentException("Parâmetro Repository não pode ser nulo.");
        }

        int mileNumber = new Integer(getMilestoneNumber());
        List<AuxNumberOfMessages> query = new ArrayList<>();

        if (mileNumber > 0) {
            query = getIssuesByMilestone(mileNumber);
        } else {
            query = getIssuesByDate();
        }

        List<AuxNumberOfMessages> numberOfMessagesList = new ArrayList<>();
        for (AuxNumberOfMessages aux : query) {
            if (aux.getNumberOfMessages() > 0) {
                numberOfMessagesList.add(new AuxNumberOfMessages(
                        aux.getIssueNumber(), aux.getNumberOfMessages(), aux.getUrl()));
            }
        }

        System.out.println("Result: " + numberOfMessagesList.size());

       // addToEntityMatrizNodeList(numberOfMessagesList);
         EntityMatrix matrix = new EntityMatrix();
        matrix.setNodes(objectsToNodes(numberOfMessagesList));
        matricesToSave.add(matrix);

    }

    @Override
    public String getHeadCSV() {
        return "Issue;numberOfMessages;URL";
    }

    private List<AuxNumberOfMessages> getIssuesByMilestone(int mileNumber) {

        String jpql = "SELECT NEW " + AuxNumberOfMessages.class.getName() + "(p.number, p.issue.commentsCount, p.issue.url) "
                + "FROM "
                + "EntityPullRequest p "
                + "WHERE "
                + "p.repository = :repository  AND "
                + (mileNumber > 0 ? "p.issue.milestone.number = :milestoneNumber " : "")
               + "";
                // + " GROUP BY p.number ";

        System.out.println(jpql);

        List<AuxNumberOfMessages> query = dao.selectWithParams(jpql,
                new String[]{
                    "repository",
                    mileNumber > 0 ? "milestoneNumber" : "#none#",},
                new Object[]{
                    getRepository(),
                    mileNumber,});

        System.out.println("query: " + query.size());
        return query;
    }

    private List<AuxNumberOfMessages> getIssuesByDate() {

        String jpql = "SELECT NEW " + AuxNumberOfMessages.class.getName() + "(p.number, p.issue.commentsCount, p.issue.url) "
                + "FROM "
                + "EntityPullRequest p "
                + "WHERE "
                + "p.repository = :repository  AND "
                + "p.issue.createdAt >= :dataInicial AND "
                + "p.issue.createdAt <= :dataFinal "
                + "";
                // + " GROUP BY p.number ";

        System.out.println(jpql);

        List<AuxNumberOfMessages> query = dao.selectWithParams(jpql,
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
        return query;
    }

}
