/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matrix;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrix;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxUserFile;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.nodes.NodeGeneric;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import br.edu.utfpr.cm.JGitMinerWeb.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author douglas
 */
public class UserModifyFileInMilestoneServices extends AbstractMatrixServices {

    public UserModifyFileInMilestoneServices(GenericDao dao, OutLog out) {
        super(dao, out);
    }

    public UserModifyFileInMilestoneServices(GenericDao dao, EntityRepository repository, List<EntityMatrix> matricesToSave, Map params, OutLog out) {
        super(dao, repository, matricesToSave, params, out);
    }


    private String getPrefixFile() {
        return params.get("prefixFile") + "%";
    }

    private String getSuffixFile() {
        return "%" + params.get("suffixFile");
    }

    @Override
    public void run() {
        System.out.println(params);

        if (getRepository() == null) {
            throw new IllegalArgumentException("Parâmetro Repository não pode ser nulo.");
        }

        int mileNumber = getMilestoneNumber();

        if (mileNumber <= 0) {
            throw new IllegalArgumentException("Numero do Milestone inválido.");
        }

        String jpql = "SELECT NEW " + AuxUserFile.class.getName() + "(rc.committer.login, c.committer.email, f.filename) "
                + "FROM "
                + "EntityPullRequest p JOIN p.issue i JOIN i.milestone m JOIN p.repositoryCommits rc JOIN rc.files f JOIN rc.commit c "
                + "WHERE "
                + "p.repository = :repository AND "
                + (mileNumber > 0 ? "m.number = :milestoneNumber AND " : "")
                + "f.filename LIKE :prefixFile AND "
                + "f.filename LIKE :suffixFile ";

        System.out.println(jpql);

        List<AuxUserFile> query = dao.selectWithParams(jpql,
                new String[]{
                    "repository",
                    mileNumber > 0 ? "milestoneNumber" : "#none#",
                    "prefixFile",
                    "suffixFile"
                },
                new Object[]{
                    getRepository(),
                    mileNumber,
                    getPrefixFile(),
                    getSuffixFile()
                });

        System.out.println("query: " + query.size());

        List<NodeGeneric> nodes = new ArrayList<>();
        for (AuxUserFile aux : query) {
            NodeGeneric rec = new NodeGeneric(
                    aux.getUser(),
                    aux.getFile());
            incrementNode(nodes, rec);
        }

        System.out.println("Result: " + query.size());

        EntityMatrix matrix = new EntityMatrix();
        matrix.setNodes(objectsToNodes(nodes));
        matricesToSave.add(matrix);
    }

    @Override
    public String getHeadCSV() {
        return "user;file;count";
    }
}
