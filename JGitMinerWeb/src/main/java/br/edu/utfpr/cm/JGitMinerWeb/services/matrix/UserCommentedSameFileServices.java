/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matrix;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrix;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxUserUserFileMilestoneDouglas;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import br.edu.utfpr.cm.JGitMinerWeb.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author douglas
 */
public class UserCommentedSameFileServices extends AbstractMatrixServices {

    public UserCommentedSameFileServices(GenericDao dao, OutLog out) {
        super(dao, out);
    }

    public UserCommentedSameFileServices(GenericDao dao, EntityRepository repository, List<EntityMatrix> matricesToSave, Map params, OutLog out) {
        super(dao, repository, matricesToSave, params, out);
    }

    public Long getBeginPullRequestNumber() {
        String idPull = params.get("beginPull") + "";
        return Util.tratarStringParaLong(idPull);
    }

    public Long getEndPullRequestNumber() {
        String idPull = params.get("endPull") + "";
        return Util.tratarStringParaLong(idPull);
    }

    private List<String> getFilesName() {
        List<String> filesName = new ArrayList<>();
        for (String fileName : (params.get("filesName") + "").split("\n")) {
            fileName = fileName.trim();
            if (!fileName.isEmpty()) {
                filesName.add(fileName);
            }
        }
        return filesName;
    }

    private Boolean isIncludeGenericComments() {
        return Boolean.parseBoolean(params.get("includeGenericsComments") + "");
    }

    public Date getBeginDate() {
        return getDateParam("beginDate");
    }

    public Date getEndDate() {
        return getDateParam("endDate");
    }

    @Override
    public void run() {
        System.out.println(params);

        if (getRepository() == null) {
            throw new IllegalArgumentException("Parâmetro Repository não pode ser nulo.");
        }

        List<AuxUserUserFileMilestoneDouglas> result;

        if (getMilestoneNumber() > 0) {
            result = getByMilestoneNumber();
        } else {
            result = getByDate();
        }

        System.out.println("Nodes: " + result.size());

        EntityMatrix matrix = new EntityMatrix();
        matrix.setNodes(objectsToNodes(result));
        matricesToSave.add(matrix);
    }

    private List<AuxUserUserFileMilestoneDouglas> getByMilestoneNumber() {
        String jpql = "SELECT DISTINCT NEW " + AuxUserUserFileMilestoneDouglas.class.getName() + "(cm.user.login, cm2.user.login, cm.pathCommitComment) "
                + "FROM "
                + "EntityPullRequest p JOIN p.repositoryCommits rc JOIN rc.comments cm, "
                + "EntityPullRequest p2 JOIN p2.repositoryCommits rc2 JOIN rc2.comments cm2 "
                + "WHERE "
                + "p.repository = :repo AND "
                + "p2.repository = :repo AND "
                + "p.issue.milestone.number = :milestoneNumber AND "
                + "p2.issue.milestone.number = :milestoneNumber AND "
                + (!getFilesName().isEmpty() ? "cm.pathCommitComment IN :filesName AND " : "")
                + (!getFilesName().isEmpty() ? "cm2.pathCommitComment IN :filesName AND " : "")
                + "cm.pathCommitComment = cm2.pathCommitComment AND "
                + "cm.user <> cm2.user ";

        String[] bdParams = new String[]{
            "repo",
            !getFilesName().isEmpty() ? "filesName" : "#none#",
            "milestoneNumber"
        };
        Object[] bdObjects = new Object[]{
            getRepository(),
            getFilesName(),
            getMilestoneNumber()
        };

        System.out.println(jpql);

        List<AuxUserUserFileMilestoneDouglas> result = new ArrayList<>();
        List<AuxUserUserFileMilestoneDouglas> temp;

        temp = dao.selectWithParams(jpql,
                bdParams,
                bdObjects);

        System.out.println("Result temp: " + temp.size());

        addTempInResultAndRemoveDuplicate(temp, result);

        if (isIncludeGenericComments()) {
            jpql = "SELECT DISTINCT NEW " + AuxUserUserFileMilestoneDouglas.class.getName() + "(cm.user.login, cm2.user.login, f.filename) "
                    + "FROM "
                    + "EntityPullRequest p JOIN p.repositoryCommits rc JOIN rc.comments cm JOIN rc.files f, "
                    + "EntityPullRequest p2 JOIN p2.repositoryCommits rc2 JOIN rc2.comments cm2 JOIN rc2.files f2 "
                    + "WHERE "
                    + "p.repository = :repo "
                    + "AND p2.repository = :repo "
                    + "AND p.issue.milestone.number = :milestoneNumber "
                    + "AND p.issue.milestone.number = p2.issue.milestone.number "
                    + "AND cm.pathCommitComment IS NULL "
                    + "AND cm2.pathCommitComment IS NULL "
                    + (!getFilesName().isEmpty() ? "AND f.filename IN :filesName " : "")
                    + (!getFilesName().isEmpty() ? "AND f2.filename IN :filesName " : "")
                    + "AND f.filename = f2.filename "
                    + "AND cm.user <> cm2.user ";

            System.out.println(jpql);

            temp = dao.selectWithParams(jpql,
                    bdParams,
                    bdObjects);

            System.out.println("Result temp2: " + temp.size());

            addTempInResultAndRemoveDuplicate(temp, result);
        }

        System.out.println("Result: " + result.size());
        return result;
    }

    private List<AuxUserUserFileMilestoneDouglas> getByDate() {
        String jpql = "SELECT DISTINCT NEW " + AuxUserUserFileMilestoneDouglas.class.getName() + "(cm.user.login, cm2.user.login, cm.pathCommitComment) "
                + "FROM "
                + "EntityCommitComment cm JOIN cm.repositoryCommit rc, "
                + "EntityCommitComment cm2 JOIN cm2.repositoryCommit rc2 "
                + "WHERE "
                + "rc.repository = :repo AND "
                + "rc2.repository = :repo AND "
                + "rc.commit.committer.dateCommitUser BETWEEN :beginDate AND :endDate AND "
                + "rc2.commit.committer.dateCommitUser BETWEEN :beginDate AND :endDate AND "
                + (!getFilesName().isEmpty() ? "cm.pathCommitComment IN :filesName AND " : "")
                + (!getFilesName().isEmpty() ? "cm2.pathCommitComment IN :filesName AND " : "")
                + "cm.pathCommitComment = cm2.pathCommitComment AND "
                + "cm.user <> cm2.user ";

        String[] bdParams = new String[]{
            "repo",
            "beginDate",
            "endDate",
            !getFilesName().isEmpty() ? "filesName" : "#none#"
        };
        Object[] bdObjects = new Object[]{
            getRepository(),
            getBeginDate(),
            getEndDate(),
            getFilesName()
        };

        System.out.println(jpql);

        List<AuxUserUserFileMilestoneDouglas> result = new ArrayList<>();
        List<AuxUserUserFileMilestoneDouglas> temp;

        temp = dao.selectWithParams(jpql,
                bdParams,
                bdObjects);

        System.out.println("Result temp: " + temp.size());

        addTempInResultAndRemoveDuplicate(temp, result);

        if (isIncludeGenericComments()) {
            jpql = "SELECT DISTINCT NEW " + AuxUserUserFileMilestoneDouglas.class.getName() + "(cm.user.login, cm2.user.login, f.filename) "
                    + "FROM "
                    + "EntityPullRequest p JOIN p.repositoryCommits rc JOIN rc.comments cm JOIN rc.files f, "
                    + "EntityPullRequest p2 JOIN p2.repositoryCommits rc2 JOIN rc2.comments cm2 JOIN rc2.files f2 "
                    + "WHERE "
                    + "p.repository = :repo "
                    + "AND p2.repository = :repo "
                    + "AND rc.commit.committer.dateCommitUser BETWEEN :beginDate AND :endDate "
                    + "AND rc2.commit.committer.dateCommitUser BETWEEN :beginDate AND :endDate "
                    + "AND cm.pathCommitComment IS NULL "
                    + "AND cm2.pathCommitComment IS NULL "
                    + (!getFilesName().isEmpty() ? "AND f.filename IN :filesName " : "")
                    + (!getFilesName().isEmpty() ? "AND f2.filename IN :filesName " : "")
                    + "AND f.filename = f2.filename "
                    + "AND cm.user <> cm2.user ";

            System.out.println(jpql);

            temp = dao.selectWithParams(jpql,
                    bdParams,
                    bdObjects);

            System.out.println("Result temp2: " + temp.size());

            addTempInResultAndRemoveDuplicate(temp, result);
        }

        System.out.println("Result: " + result.size());
        return result;
    }

    private void addTempInResultAndRemoveDuplicate(List<AuxUserUserFileMilestoneDouglas> temp, List<AuxUserUserFileMilestoneDouglas> result) {
        for (AuxUserUserFileMilestoneDouglas aux : temp) {
            if (!result.contains(aux)) {
                result.add(aux);
            }
        }
    }

    @Override
    public String getHeadCSV() {
        return "user;user2;file";
    }
}
