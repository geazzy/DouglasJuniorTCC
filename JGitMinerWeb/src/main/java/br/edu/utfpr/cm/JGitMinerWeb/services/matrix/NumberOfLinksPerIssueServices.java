/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matrix;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrix;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityComment;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityIssue;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxNumberOfLinks;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxPairOfFiles;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.UrlValidator;

import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import br.edu.utfpr.cm.JGitMinerWeb.util.Util;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author geazzy
 */
public class NumberOfLinksPerIssueServices extends AbstractMatrixServices{

    private List<AuxNumberOfLinks> resultList;
    private List<EntityIssue> issueList;
  


    public NumberOfLinksPerIssueServices(GenericDao dao, OutLog out) {
        super(dao, out);
         this.issueList = new ArrayList<>();
        this.resultList = new ArrayList<>();
    }

    public NumberOfLinksPerIssueServices(GenericDao dao, EntityRepository repository, List<EntityMatrix> matricesToSave, Map params, OutLog out) {
        super(dao, repository, matricesToSave, params, out);
         this.issueList = new ArrayList<>();
        this.resultList = new ArrayList<>();
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
            getRepositoryCommitsFromPullRequest(getPullRequestByMilestone());
        } else {
            getRepositoryCommitsFromPullRequest(getPullRequestByDate());
        }

        for (EntityIssue entityIssue : issueList) {
            resultList.add(new AuxNumberOfLinks(entityIssue.getNumber(),
                    entityIssue.getUrl(), calculateNumberOfLinks(entityIssue)));
        }

        System.out.println("Result: . " + resultList.size());

     //   addToEntityMatrizNodeList(resultList);

        EntityMatrix matrix = new EntityMatrix();
        matrix.setNodes(objectsToNodes(resultList));
        matricesToSave.add(matrix);
        
    }

    @Override
    public String getHeadCSV() {
        return "Issue;NumberofLinks;URL";
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

    private void getRepositoryCommitsFromPullRequest(List<EntityPullRequest> pullRequestListParam) {

        for (EntityPullRequest pullrequest : pullRequestListParam) {
            issueList.add(pullrequest.getIssue());
        }

    }

    private Integer calculateNumberOfLinks(EntityIssue issue) {
        Integer numberOfLinks = 0;

        numberOfLinks += UrlValidator.urlInString(Strings.nullToEmpty(issue.getBody()));

        for (EntityComment entityComment : issue.getComments()) {

            numberOfLinks += UrlValidator.urlInString(Strings.nullToEmpty(entityComment.getBody()));
            //System.out.println("");
        }

        return numberOfLinks;
    }

    

}
