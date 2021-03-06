/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrix;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrixNode;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.UserModifySamePairOfFileInDateServices;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxFileCountSum;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxUserFile;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxFileMetrics;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxUserMetrics;
import br.edu.utfpr.cm.JGitMinerWeb.util.JsfUtil;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;
import edu.uci.ics.jung.algorithms.scoring.BarycenterScorer;
import edu.uci.ics.jung.algorithms.scoring.EigenvectorCentrality;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rodrigo T. Kuroda
 */
public class PairFileBaryCenterEigenvectorServices extends AbstractMetricServices {

    private EntityRepository repository;

    public PairFileBaryCenterEigenvectorServices(GenericDao dao, OutLog out) {
        super(dao, out);
    }

    public PairFileBaryCenterEigenvectorServices(GenericDao dao, EntityMatrix matrix, Map params, OutLog out) {
        super(dao, matrix, params, out);
    }

    public Date getFutureBeginDate() {
        return getDateParam("futureBeginDate");
    }

    public Date getFutureEndDate() {
        return getDateParam("futureEndDate");
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

        System.out.println(getMatrix().getClassServicesName());

        if (getMatrix() == null
                || !getAvailableMatricesPermitted().contains(getMatrix().getClassServicesName())) {
            throw new IllegalArgumentException("Selecione uma matrix gerada pelo Services: " + getAvailableMatricesPermitted());
        }

        repository = getRepository();

        if (repository == null) {
            throw new IllegalArgumentException("Não foi possível encontrar o repositório utilizado nesta matrix.");
        }

        System.out.println("Selecionado matrix com " + getMatrix().getNodes().size() + " nodes.");
        UndirectedSparseMultigraph<String, String> graphMulti = new UndirectedSparseMultigraph<>();
        UndirectedSparseGraph<String, String> graph = new UndirectedSparseGraph<>();
        List<String> files = new ArrayList<>();
        for (int i = 0; i < getMatrix().getNodes().size(); i++) {
            EntityMatrixNode node = getMatrix().getNodes().get(i);
            String[] coluns = node.getLine().split(JsfUtil.TOKEN_SEPARATOR);
            colectFile(files, coluns[2]);
            graph.addEdge(
                    coluns[2] + "(" + i + ")",
                    coluns[1],
                    coluns[0],
                    EdgeType.UNDIRECTED);
            graphMulti.addEdge(
                    coluns[2] + "(" + i + ")",
                    coluns[1],
                    coluns[0],
                    EdgeType.UNDIRECTED);
        }

        BarycenterScorer<String, String> barycenterGen = new BarycenterScorer<>(graph);
        EigenvectorCentrality<String, String> eigenvectorGen = new EigenvectorCentrality<>(graph);
        EdgeBetweennessClusterer<String, String> edgeBetweennessGen = new EdgeBetweennessClusterer<>(1);

        List<AuxUserMetrics> userMetrics = new ArrayList<>();

        for (String vertexUser : graphMulti.getVertices()) {
            userMetrics.add(new AuxUserMetrics(vertexUser,
                    barycenterGen.getVertexScore(vertexUser),
                    eigenvectorGen.getVertexScore(vertexUser)
            ));
        }

        List<AuxFileMetrics> fileMetrics = new ArrayList<>();
        List<AuxUserFile> occurrences = new ArrayList<>();

        for (String file : files) {
            Double baryCenterMax = 0d, baryCenterAve, baryCenterSum = 0d;
            Double eigenvectorMax = 0d, eigenvectorAve, eigenvectorSum = 0d;
            Double codeChurn, updates, dev = 0d;
            for (String edgeFile : graphMulti.getEdges()) {
                if (edgeFile.startsWith(file)) {
                    for (AuxUserMetrics auxUser : userMetrics) {
                        if (graphMulti.isIncident(auxUser.getUser(), edgeFile)) {
                            AuxUserFile reg = new AuxUserFile(auxUser.getUser(), file);
                            if (!occurrences.contains(reg)) {
                                occurrences.add(reg);
                                Double baryCenter = auxUser.getMetrics()[0];
                                baryCenterMax = calculeMax(baryCenter, baryCenterMax);
                                baryCenterSum += baryCenter;

                                Double eigenvector = auxUser.getMetrics()[1];
                                eigenvectorMax = calculeMax(eigenvector, eigenvectorMax);
                                eigenvectorSum += eigenvector;

                                dev++;
                            }
                        }
                    }
                }
            }
            baryCenterAve = baryCenterSum / dev;
            eigenvectorAve = eigenvectorSum / dev;

            AuxFileCountSum aux = calculeCodeChurnAndUpdates(file, getBeginDate(), getEndDate());
            codeChurn = (double) aux.getSum();
            updates = (double) aux.getCount();

//            aux = calculeCodeChurnAndUpdates(file, getFutureBeginDate(), getFutureEndDate());
//            futCodeChurn = (double) aux.getSum();
//            futUpdates = (double) aux.getCount();
            fileMetrics.add(new AuxFileMetrics(file,
                    baryCenterMax, baryCenterAve, baryCenterSum,
                    eigenvectorMax, eigenvectorAve, eigenvectorSum,
                    dev, codeChurn, updates));
        }

        addToEntityMetricNodeList(fileMetrics);
    }

    @Override
    public String getHeadCSV() {
        return "file;"
                + "baryCenterMax;baryCenterAve;baryCenterSum;"
                + "eigenvectorMax;eigenvectorAve;eigenvectorSum;"
                + "developers;codeChurn;updates;";
    }

    @Override
    public List<String> getAvailableMatricesPermitted() {
        return Arrays.asList(
                UserModifySamePairOfFileInDateServices.class.getName()
        );
    }

    private void colectFile(List<String> files, String file) {
        if (!files.contains(file)) {
            files.add(file);
        }
    }

    private Double calculeMax(Double v, Double vMax) {
        if (vMax < v) {
            return v;
        }
        return vMax;
    }

    private AuxFileCountSum calculeCodeChurnAndUpdates(String fileName, Date beginDate, Date endDate) {
        String jpql = "SELECT NEW " + AuxFileCountSum.class.getName() + "(f.filename, COUNT(f), SUM(f.changes)) "
                + "FROM "
                + "EntityRepositoryCommit rc JOIN rc.files f  "
                + "WHERE "
                + "rc.repository = :repo AND "
                + "rc.commit.committer.dateCommitUser BETWEEN :beginDate AND :endDate AND "
                + "f.filename = :fileName "
                + "GROUP BY f.filename";

        String[] bdParams = new String[]{
            "repo",
            "fileName",
            "beginDate",
            "endDate"
        };
        Object[] bdObjects = new Object[]{
            repository,
            fileName,
            beginDate,
            endDate
        };

        List<AuxFileCountSum> result = dao.selectWithParams(jpql, bdParams, bdObjects);

        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return new AuxFileCountSum(fileName, 0, 0);
        }
    }

    private EntityRepository getRepository() {
        String[] repoStr = getMatrix().getRepository().split("/");
        List<EntityRepository> repos = dao.executeNamedQueryWithParams(
                "Repository.findByNameAndOwner",
                new String[]{"login", "name"},
                new Object[]{repoStr[0], repoStr[1]});
        if (repos.size() == 1) {
            return repos.get(0);
        }
        return null;
    }
}
