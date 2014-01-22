/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matriz;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.pojo.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary.AuxWordiness;
import br.edu.utfpr.cm.JGitMinerWeb.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author geazzy
 */
public class WordinessOfIssueServices extends AbstractMatrizServices {

    private List<AuxWordiness> issueList;

    public WordinessOfIssueServices(GenericDao dao) {
        super(dao);
        this.issueList = new ArrayList<>();
    }

    public WordinessOfIssueServices(GenericDao dao, EntityRepository repository, Map params) {
        super(dao, repository, params);
        this.issueList = new ArrayList<>();
    }

    @Override
    public void run() {
        if (getRepository() == null) {
            throw new IllegalArgumentException("Parâmetro Repository não pode ser nulo.");
        }

        List<AuxWordiness> resultado;

        resultado = getIssues();
        setIssueComments(resultado);

        System.out.println("Result: . " + issueList.size());

        addToEntityMatrizNodeList(issueList);
    }

    @Override
    public String getHeadCSV() {
        return "Issue;Wordiness;URL";
    }

    private List<AuxWordiness> getIssues() {

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
                + " GROUP BY p.number ";

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

}
