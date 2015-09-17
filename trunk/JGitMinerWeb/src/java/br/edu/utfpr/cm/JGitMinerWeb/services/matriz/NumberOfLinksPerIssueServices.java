/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matriz;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary.AuxNumberOfLinks;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author geazzy
 */
public class NumberOfLinksPerIssueServices extends AbstractMatrizServices {

    private List<AuxNumberOfLinks> issueList;
      

    public NumberOfLinksPerIssueServices(GenericDao dao, OutLog out) {
        super(dao, out);
        this.issueList = new ArrayList<>();
    }

    public NumberOfLinksPerIssueServices(GenericDao dao, EntityRepository repository, Map params, OutLog out) {
        super(dao, repository, params, out);
        this.issueList = new ArrayList<>();
    }

 

    @Override
    public void run() {
        if (getRepository() == null) {
            throw new IllegalArgumentException("Parâmetro Repository não pode ser nulo.");
        }

       
        if(getMilestoneNumber() > 0){
             getIssuesByMilestone();
        }else{
             getIssuesByDate();
        }
        
        setIssueComments();

        System.out.println("Result: . " + issueList.size());

        addToEntityMatrizNodeList(issueList);

    }

    @Override
    public String getHeadCSV() {
        return "Issue;NumberofLinks;URL";
    }

    private void getIssuesByMilestone() {

        int mileNumber = new Integer(getMilestoneNumber());

        if (mileNumber <= 0) {
            throw new IllegalArgumentException("Numero do Milestone inválido.");
        }

        String jpql = "SELECT NEW " + AuxNumberOfLinks.class.getName() + "(p.number, p.issue.url, p.issue.body) "
                + "FROM "
                + "EntityPullRequest p "
                + "WHERE "
                + "p.repository = :repository  AND "
                + "p.issue.commentsCount > 0  AND "
                + (mileNumber > 0 ? "p.issue.milestone.number = :milestoneNumber " : "")
                + " GROUP BY p.number ";

        System.out.println(jpql);

        List<AuxNumberOfLinks> query = dao.selectWithParams(jpql,
                new String[]{
                    "repository",
                    mileNumber > 0 ? "milestoneNumber" : "#none#",},
                new Object[]{
                    getRepository(),
                    mileNumber,});

        System.out.println("query: " + query.size());

        issueList = query;
    }

    private void setIssueComments() {
        
        for (AuxNumberOfLinks issue : issueList) {

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

            issue.setNumberOflinks();

            issueList.add(new AuxNumberOfLinks(
                    issue.getIssueNumber(), issue.getUrl(), issue.getNumberOflinks()));

            System.out.println("NUMERO DE LINKS " + issue.getNumberOflinks().toString());

            System.out.println("query 2: " + issue.getComments().size());

        }
    }

    private void getIssuesByDate() {
        
          String jpql = "SELECT NEW " + AuxNumberOfLinks.class.getName() + "(p.number, p.issue.url, p.issue.body) "
                + "FROM "
                + "EntityPullRequest p "
                + "WHERE "
                + "p.repository = :repository  AND "
                + "p.issue.commentsCount > 0  AND "
                + "p.issue.createdAt >= :dataInicial AND "
                + "p.issue.createdAt <= :dataFinal "
                + " GROUP BY p.number ";

        System.out.println(jpql);

        List<AuxNumberOfLinks> query = dao.selectWithParams(jpql,
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

}
