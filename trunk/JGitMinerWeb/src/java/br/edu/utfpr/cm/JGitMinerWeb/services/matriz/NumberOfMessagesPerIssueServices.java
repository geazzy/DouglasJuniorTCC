/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.utfpr.cm.JGitMinerWeb.services.matriz;

import br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary.AuxNumberOfMessages;
import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.pojo.miner.EntityMilestone;
import br.edu.utfpr.cm.JGitMinerWeb.pojo.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author geazzy
 * Retonar o número de comentários de cada issue associada a um pullrequest
 */
public class NumberOfMessagesPerIssueServices extends AbstractMatrizServices{

    public NumberOfMessagesPerIssueServices(GenericDao dao) {
        super(dao);
    }

    public NumberOfMessagesPerIssueServices(GenericDao dao, EntityRepository repository, Map params) {
        super(dao, repository, params);
    }

     private Integer getMilestoneNumber() {
        String mileNumber = params.get("milestoneNumber") + "";
        return Util.tratarStringParaInt(mileNumber);
    }

    
    @Override
    public void run() {
        System.out.println(params);

        if (getRepository() == null) {
            throw new IllegalArgumentException("Parâmetro Repository não pode ser nulo.");
        }

        int mileNumber = new Integer(getMilestoneNumber());

        if (mileNumber <= 0) {
            throw new IllegalArgumentException("Numero do Milestone inválido.");
        }
        
        String jpql = "SELECT NEW " + AuxNumberOfMessages.class.getName() + "(p.number, p.issue.commentsCount, p.issue.url) "
                + "FROM "
                + "EntityPullRequest p "
                + "WHERE "
                + "p.repository = :repository  AND "
                + (mileNumber > 0 ? "p.issue.milestone.number = :milestoneNumber " : "")
                + " GROUP BY p.number ";


         
        System.out.println(jpql);
                  
        List<AuxNumberOfMessages> query = dao.selectWithParams(jpql,
                new String[]{
                    "repository",
                    mileNumber > 0 ? "milestoneNumber" : "#none#",
                },
                new Object[]{
                    getRepository(),
                    mileNumber,
                });

        System.out.println("query: " + query.size());
        
        List<AuxNumberOfMessages> numberOfMessagesList = new ArrayList<>();
        for (AuxNumberOfMessages aux : query) {
            if (aux.getNumberOfMessages() > 0)
                numberOfMessagesList.add(new AuxNumberOfMessages(
                    aux.getIssueNumber(),aux.getNumberOfMessages(), aux.getUrl() ));     
        }

        System.out.println("Result: " + numberOfMessagesList.size());

        addToEntityMatrizNodeList(numberOfMessagesList);
        
    }

    @Override
    public String getHeadCSV() {
        return "Issue;numberOfMessages;URL";
    }
    
}
