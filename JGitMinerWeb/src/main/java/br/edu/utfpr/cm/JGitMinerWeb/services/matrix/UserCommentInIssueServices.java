/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matrix;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrix;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.nodes.NodeGeneric;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import br.edu.utfpr.cm.JGitMinerWeb.util.Util;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author douglas
 */
public class UserCommentInIssueServices extends AbstractMatrixServices {

    public UserCommentInIssueServices(GenericDao dao, OutLog out) {
        super(dao, out);
    }

    public UserCommentInIssueServices(GenericDao dao, EntityRepository repository, List<EntityMatrix> matricesToSave, Map params, OutLog out) {
        super(dao, repository, matricesToSave, params, out);
    }


    public Date getBeginDate() {
        return getDateParam("beginDate");
    }

    public Date getEndDate() {
        return getDateParam("endDate");
    }

    @Override
    public void run() {
        if (getRepository() == null) {
            throw new IllegalArgumentException("Parâmetro Repository não pode ser nulo.");
        }

        List<NodeGeneric> nodes;

        if (getMilestoneNumber() != 0) {
            nodes = getFilesByMilestone();
        } else if (getBeginDate() != null
                && getEndDate() != null) {
            nodes = getFilesByDate();
        } else {
            throw new IllegalArgumentException("Informe o número do Milestone ou um Intervalo de datas.");
        }

        System.out.println("Nodes: " + nodes.size());

        EntityMatrix matrix = new EntityMatrix();
        matrix.setNodes(objectsToNodes(nodes));
        matricesToSave.add(matrix);
    }

    @Override
    public String getHeadCSV() {
        return "user;issue;countComments";
    }

    private List<NodeGeneric> getFilesByMilestone() {
        String jpql = "SELECT NEW " + NodeGeneric.class.getName() + "(u.login, i.number, count(u.id)) "
                + "FROM EntityIssue i JOIN i.comments c JOIN c.user u "
                + "WHERE i.repository = :repo "
                + "AND i.milestone.number >= :milestoneNumber "
                + "GROUP BY u.login, i.number";

        System.out.println(jpql);

        List<NodeGeneric> nodes = dao.selectWithParams(jpql,
                new String[]{
                    "repo",
                    "milestoneNumber"
                }, new Object[]{
                    getRepository(),
                    getMilestoneNumber()
                });
        return nodes;
    }

    private List<NodeGeneric> getFilesByDate() {
        String jpql = "SELECT NEW " + NodeGeneric.class.getName() + "(u.login, i.number, count(u.id)) "
                + "FROM EntityIssue i JOIN i.comments c JOIN c.user u "
                + "WHERE i.repository = :repo "
                + "AND i.createdAt >= :dataInicial "
                + "AND i.createdAt <= :dataFinal "
                + "GROUP BY u.login, i.number";

        System.out.println(jpql);

        List<NodeGeneric> nodes = dao.selectWithParams(jpql,
                new String[]{
                    "repo",
                    "dataInicial",
                    "dataFinal"
                }, new Object[]{
                    getRepository(),
                    getBeginDate(),
                    getEndDate()
                });
        return nodes;
    }
}
