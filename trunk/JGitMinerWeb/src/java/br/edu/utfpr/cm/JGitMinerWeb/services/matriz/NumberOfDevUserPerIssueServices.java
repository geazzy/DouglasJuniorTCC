/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matriz;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.pojo.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary.AuxNumberOfDevUser;
import br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary.AuxUserComments;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;

/**
 *
 * @author geazzy
 */
public class NumberOfDevUserPerIssueServices extends AbstractMatrizServices {

    private List<AuxNumberOfDevUser> issueList;

    public NumberOfDevUserPerIssueServices(GenericDao dao) {
        super(dao);
    }

    public NumberOfDevUserPerIssueServices(GenericDao dao, EntityRepository repository, Map params) {
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

        obterListaDeAutoresDosComentarios();

        obterNumeroDeCommittsDeCadaAutor();

        definirQuantidadeDeDevEUserPorIssue();

        addToEntityMatrizNodeList(issueList);

    }

    @Override
    public String getHeadCSV() {
        return "IssueNumber;dev;users;url";
    }

    private void getIssuesByMilestone() {

        int mileNumber = new Integer(getMilestoneNumber());

        if (mileNumber <= 0) {
            throw new IllegalArgumentException("Numero do Milestone inválido.");
        }

        String jpql = "SELECT NEW " + AuxNumberOfDevUser.class.getName() + "(p.number, p.issue.url, p.issue.commentsCount, p.user) "
                + "FROM "
                + "EntityPullRequest p "
                + "WHERE "
                + "p.repository = :repository  AND "
                + "p.issue.commentsCount > 0  AND "
                + (mileNumber > 0 ? "p.issue.milestone.number = :milestoneNumber " : "")
                + " GROUP BY p.number ";

        System.out.println(jpql);

        List<AuxNumberOfDevUser> query = dao.selectWithParams(jpql,
                new String[]{
                    "repository",
                    mileNumber > 0 ? "milestoneNumber" : "#none#",},
                new Object[]{
                    getRepository(),
                    mileNumber,});

        System.out.println("query: " + query.size());

        this.issueList = query;

    }

    private void getIssuesByDate() {

        String jpql = "SELECT NEW " + AuxNumberOfDevUser.class.getName() + "(p.number, p.issue.url, p.issue.commentsCount, p.user) "
                + "FROM "
                + "EntityPullRequest p "
                + "WHERE "
                + "p.repository = :repository  AND "
                + "p.issue.commentsCount > 0  AND "
                + "p.issue.createdAt >= :dataInicial AND "
                + "p.issue.createdAt <= :dataFinal "
                + " GROUP BY p.number ";

        System.out.println(jpql);

        List<AuxNumberOfDevUser> query = dao.selectWithParams(jpql,
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

        this.issueList = query;
    }

    private void obterListaDeAutoresDosComentarios() {

        for (AuxNumberOfDevUser issue : issueList) {

            String jpql2 = "SELECT NEW " + AuxUserComments.class.getName() + " (c.user) "
                    + "FROM "
                    + "EntityComment c "
                    + "WHERE "
                    + "c.issue.number  = :number  ";

            System.out.println("jpql2: " + jpql2);

            issue.setCommentsAuthorsList(Sets.newHashSet(dao.selectWithParams(jpql2,
                    new String[]{
                        "number",}, new Object[]{
                        issue.getIssueNumber(),})));

            System.out.println("Numero de autores: " + issue.getCommentsAuthorsList().size());

        }
    }

    private void obterNumeroDeCommittsDeCadaAutor() {

        for (AuxNumberOfDevUser issue : issueList) {

            issue.addAutorDaIssueAListaDeAutores();

            for (AuxUserComments autor : issue.getCommentsAuthorsList()) {
                String jpql3 = "SELECT count(rc) "
                        + "FROM "
                        + "EntityRepositoryCommit rc "
                        + "WHERE "
                        + "rc.author.id = :id ";

                System.out.println("jpql3: " + jpql3);

                autor.setNumeroCommits(((Long) dao.selectWithParams(jpql3,
                        new String[]{
                            "id",}, new Object[]{
                            autor.getUser().getId(),}).get(0)).intValue());

                autor.setIsDev();

                System.out.println("IssueNumber " + issue.getIssueNumber()
                        + " Autor: " + autor.getUser().getId()
                        + " numeroCommits: " + autor.getNumeroCommits()
                        + " isDev: " + autor.isDev());

            }

        }
    }

    private void definirQuantidadeDeDevEUserPorIssue() {
        for (AuxNumberOfDevUser issue : issueList) {

            for (AuxUserComments autor : issue.getCommentsAuthorsList()) {
                if (autor.isDev()) {
                    issue.incrementarNumeroDevs();
                } else {
                    issue.incrementarNumeroUsers();
                }
            }

        }
    }
}
