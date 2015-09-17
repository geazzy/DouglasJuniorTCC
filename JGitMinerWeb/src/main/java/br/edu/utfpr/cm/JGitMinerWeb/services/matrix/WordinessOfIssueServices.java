/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matrix;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrix;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxWordiness;
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
public class WordinessOfIssueServices extends AbstractMatrixServices {

    private List<AuxWordiness> issueList;

    public WordinessOfIssueServices(GenericDao dao, OutLog out) {
        super(dao, out);
        issueList = new ArrayList<>();
    }

    public WordinessOfIssueServices(GenericDao dao, EntityRepository repository, List<EntityMatrix> matricesToSave, Map params, OutLog out) {
        super(dao, repository, matricesToSave, params, out);
        issueList = new ArrayList<>();
    }

    protected Integer getMilestoneNumber() {
        String mileNumber = params.get("milestoneNumber") + "";
        return Util.tratarStringParaInt(mileNumber);
    }


    @Override
    public void run() {
        if (getRepository() == null) {
            throw new IllegalArgumentException("Parâmetro Repository não pode ser nulo.");
        }

        List<AuxWordiness> resultado;

        if (getMilestoneNumber() > 0) {
            resultado = getIssuesByMilestone();
        } else {
            resultado = getIssuesByDate();
        }

        setIssueComments(resultado);

        System.out.println("Result: . " + issueList.size());

        // addToEntityMatrizNodeList(issueList);
        EntityMatrix matrix = new EntityMatrix();
        matrix.setNodes(objectsToNodes(issueList));
        matricesToSave.add(matrix);
    }

    @Override
    public String getHeadCSV() {
        return "Issue;Wordiness;URL";
    }

    private List<AuxWordiness> getIssuesByMilestone() {

        int mileNumber = new Integer(getMilestoneNumber());

        if (mileNumber <= 0) {
            throw new IllegalArgumentException("Numero do Milestone inválido.");
        }

        String jpql = "SELECT NEW " + AuxWordiness.class.getName() + "(p.number, p.issue.url, p.issue.body) "
                + "FROM "
                + "EntityPullRequest p "
                + "WHERE "
                + "p.repository = :repository  AND "
                + "p.issue.commentsCount > 0  AND "
                + (mileNumber > 0 ? "p.issue.milestone.number = :milestoneNumber " : "")
                + "";
                //+ " GROUP BY p.number ";

        System.out.println(jpql);

        List<AuxWordiness> query = dao.selectWithParams(jpql,
                new String[]{
                    "repository",
                    mileNumber > 0 ? "milestoneNumber" : "#none#",},
                new Object[]{
                    getRepository(),
                    mileNumber,});

        System.out.println("query: " + query.size());

        return query;
    }

    private void setIssueComments(List<AuxWordiness> resultado) {
        for (AuxWordiness issue : resultado) {

            String jpql2 = "SELECT c "
                    + "FROM "
                    + "EntityComment c "
                    + "WHERE "
                    + "c.issue.number  = :number  ";

            System.out.println("jpql2: " + jpql2);

            issue.setComments(dao.selectWithParams(jpql2,
                    new String[]{
                        "number",}, new Object[]{
                        issue.getIssueNumber(),}));

            issue.setWordiness();

            issueList.add(new AuxWordiness(
                    issue.getIssueNumber(), issue.getUrl(), issue.getWordiness()));

//            System.out.println("NUMERO DE LINKS " + issue.getWordiness().toString());
//
            System.out.println("Comentarios: " + issue.getComments().size());

        }
    }

    private List<AuxWordiness> getIssuesByDate() {

        String jpql = "SELECT NEW " + AuxWordiness.class.getName() + "(p.number, p.issue.url, p.issue.body) "
                + "FROM "
                + "EntityPullRequest p "
                + "WHERE "
                + "p.repository = :repository  AND "
                + "p.issue.commentsCount > 0  AND "
                + "p.issue.createdAt >= :dataInicial AND "
                + "p.issue.createdAt <= :dataFinal "
                + "";
                //+ " GROUP BY p.number ";

        System.out.println(jpql);

        List<AuxWordiness> query = dao.selectWithParams(jpql,
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
