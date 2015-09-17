package br.edu.utfpr.cm.JGitMinerWeb.services.matrix;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrix;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrixNode;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxFileFileMetrics;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Douglas
 */
public class CochangeSupportConfidenceLiftConvictionInPullRequestsServices extends AbstractMatrixServices {

    public CochangeSupportConfidenceLiftConvictionInPullRequestsServices(GenericDao dao, OutLog out) {
        super(dao, out);
    }

    public CochangeSupportConfidenceLiftConvictionInPullRequestsServices(GenericDao dao, EntityRepository repo, List<EntityMatrix> matrices, Map params, OutLog out) {
        super(dao, repo, matrices, params, out);
    }

    private Long getBeginDate() {
        return getLongParam("beginPullRequest");
    }

    private Long getEndDate() {
        return getLongParam("endPullRequest");
    }

    public Long getFutureBeginDate() {
        return getLongParam("futureBeginPullRequest");
    }

    public Long getFutureEndDate() {
        return getLongParam("futureEndPullRequest");
    }

    public List<String> getFilesToIgnore() {
        return getStringLinesParam("filesToIgnore", true, true);
    }

    public List<String> getFilesToConsiders() {
        return getStringLinesParam("filesToConsiders", true, true);
    }

    public Boolean isOnlyMergeds() {
        return getBooleanParam("onlyMergeds");
    }

    public Integer getMaxFilesPerCommit() {
        return getIntegerParam("maxFilesPerCommit");
    }

    public Integer getMinFilesPerCommit() {
        return getIntegerParam("minFilesPerCommit");
    }

    @Override
    public void run() {
        try {

            if (getRepository() == null) {
                throw new IllegalArgumentException("Parâmetro Repository não pode ser nulo.");
            }

            Long futureBeginPull = getFutureBeginDate();
            Long futureEndPull = getFutureEndDate();
            Long beginPull = getBeginDate();
            Long endPull = getEndDate();
            List<String> filesToIgnore = getFilesToIgnore();
            List<String> filesToConsiders = getFilesToConsiders();

            out.printLog("Iniciando preenchimento da lista de pares.");

            List<Object> selectParams = new ArrayList<>();

            StringBuilder select = new StringBuilder();
            select.append("select distinct fil.filename, fil2.filename ")
                    .append(" from gitcommitfile fil, ")
                    .append("      gitpullrequest_gitrepositorycommit prc, ")
                    .append("      gitcommitfile fil2, ")
                    .append("      gitpullrequest_gitrepositorycommit prc2, ")
                    .append("      gitpullrequest pul ")
                    .append("where pul.repository_id = ? ")
                    .append("  and pul.number between ? and ? ");

            if (isOnlyMergeds()) {
                select.append("  and pul.mergedat is not null ");
            }
            select.append("  and prc.entitypullrequest_id = pul.id ")
                    .append("  and (select count(*) from gitcommitfile f where f.repositorycommit_id = prc.repositorycommits_id) <= ")
                    .append(getMaxFilesPerCommit())
                    .append("  and (select count(*) from gitcommitfile f where f.repositorycommit_id = prc.repositorycommits_id) >= ")
                    .append(getMinFilesPerCommit())
                    .append("  and fil.repositorycommit_id = prc.repositorycommits_id ")
                    .append("  and prc2.entitypullrequest_id = pul.id ")
                    .append("  and (select count(*) from gitcommitfile f where f.repositorycommit_id = prc2.repositorycommits_id) <= ")
                    .append(getMaxFilesPerCommit())
                    .append("  and (select count(*) from gitcommitfile f where f.repositorycommit_id = prc2.repositorycommits_id) >= ")
                    .append(getMinFilesPerCommit())
                    .append("  and fil2.repositorycommit_id = prc2.repositorycommits_id ")
                    .append("  and md5(fil.filename) < md5(fil2.filename) ");

            if (!filesToIgnore.isEmpty()) {
                for (String fileName : filesToIgnore) {
                    select.append("  and fil.filename not like '").append(fileName).append("' ")
                            .append("  and fil2.filename not like '").append(fileName).append("' ");
                }
            }

            if (!filesToConsiders.isEmpty()) {
                select.append(" and (");

                int likeFilename = 0;
                for (String fileName : filesToConsiders) {
                    if (likeFilename++ > 0) {
                        select.append("or");
                    }
                    select.append(" fil.filename like '").append(fileName).append("' ");
                }
                select.append(")");

                select.append(" and (");

                likeFilename = 0;
                for (String fileName : filesToConsiders) {
                    if (likeFilename++ > 0) {
                        select.append("or");
                    }
                    select.append(" fil2.filename like '").append(fileName).append("' ");
                }
                select.append(")");
            }

            System.out.println(select);

            selectParams.add(getRepository().getId());
            selectParams.add(beginPull);
            selectParams.add(endPull);

            List<Object[]> cochangeResult = dao.selectNativeWithParams(select.toString(), selectParams.toArray());

            Set<AuxFileFileMetrics> pairFileMetrics = new HashSet<>();
            for (Object[] record : cochangeResult) {
                pairFileMetrics.add(new AuxFileFileMetrics(record[0] + "", record[1] + ""));
            }
            cochangeResult.clear();

            out.printLog(pairFileMetrics.size() + " pares encontrados.");

            out.printLog("Iniciando cálculo do support, confidence, lift e conviction.");
            int i = 1;
            for (AuxFileFileMetrics pairFile : pairFileMetrics) {
                if (i % 100 == 0 || i == pairFileMetrics.size()) {
                    System.out.println(i + "/" + pairFileMetrics.size());
                }

                Long pairFileNumberOfPullrequestOfPair = calculeUpdates(pairFile.getFile(), pairFile.getFile2(), beginPull, endPull);
                Long pairFileNumberOfPullrequestOfPairFuture = calculeUpdates(pairFile.getFile(), pairFile.getFile2(), futureBeginPull, futureEndPull);
                Long fileNumberOfPullrequestOfPairFuture = calculeUpdates(pairFile.getFile(), null, futureBeginPull, futureEndPull);
                Long file2NumberOfPullrequestOfPairFuture = calculeUpdates(pairFile.getFile2(), null, futureBeginPull, futureEndPull);
                Long numberOfAllPullrequestFuture = calculeUpdates(null, null, futureBeginPull, futureEndPull);

                pairFile.addMetrics(pairFileNumberOfPullrequestOfPair, pairFileNumberOfPullrequestOfPairFuture, fileNumberOfPullrequestOfPairFuture, file2NumberOfPullrequestOfPairFuture, numberOfAllPullrequestFuture);

                Double supportFile = numberOfAllPullrequestFuture == 0 ? 0d : fileNumberOfPullrequestOfPairFuture.doubleValue() / numberOfAllPullrequestFuture.doubleValue();
                Double supportFile2 = numberOfAllPullrequestFuture == 0 ? 0d : file2NumberOfPullrequestOfPairFuture.doubleValue() / numberOfAllPullrequestFuture.doubleValue();
                Double supportPairFile = numberOfAllPullrequestFuture == 0 ? 0d : pairFileNumberOfPullrequestOfPairFuture.doubleValue() / numberOfAllPullrequestFuture.doubleValue();
                Double confidence = supportFile == 0 ? 0d : supportPairFile / supportFile;
                Double confidence2 = supportFile2 == 0 ? 0d : supportPairFile / supportFile2;
                Double lift = supportFile * supportFile2 == 0 ? 0d : supportPairFile / (supportFile * supportFile2);
                Double conviction = 1 - confidence == 0 ? 0d : (1 - supportFile) / (1 - confidence);
                Double conviction2 = 1 - confidence2 == 0 ? 0d : (1 - supportFile2) / (1 - confidence2);

                pairFile.addMetrics(supportFile, supportFile2, supportPairFile, confidence, confidence2, lift, conviction, conviction2);

                i++;
            }

            EntityMatrix matrix = new EntityMatrix();
            matrix.setNodes(objectsToNodes(pairFileMetrics));

            pairFileMetrics.clear();

            saveMatrix(matrix);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void saveMatrix(EntityMatrix entityMatrix) {
        out.printLog("Iniciando salvamento da rede.");
        List<EntityMatrixNode> matrixNodes = entityMatrix.getNodes();
        entityMatrix.setNodes(new ArrayList<EntityMatrixNode>());
        entityMatrix.getParams().putAll(params);
        entityMatrix.setRepository(getRepository() + "");
        entityMatrix.setClassServicesName(this.getClass().getName());
        entityMatrix.setLog(out.getLog().toString());
        dao.insert(entityMatrix);
        for (Iterator<EntityMatrixNode> it = matrixNodes.iterator(); it.hasNext();) {
            EntityMatrixNode node = it.next();
            node.setMatrix(entityMatrix);
            entityMatrix.getNodes().add(node);
            dao.insert(node);
            it.remove();
        }
        entityMatrix.setStoped(new Date());
        entityMatrix.setComplete(true);
        out.printLog("Concluida geração da rede.");
        entityMatrix.setLog(out.getLog().toString());
        dao.edit(entityMatrix);
        out.printLog("");
    }

    @Override
    public String getHeadCSV() {
        return "file;file2;"
                + "pairFileCochange;pairFileCochangeFuture;fileChangeFuture;file2ChangeFuture;allPullrequestFuture;"
                + "supportFile;supportFile2;supportPairFile;confidence;confidence2;lift;conviction;conviction2";
    }

    private long calculeUpdates(String file, String file2, Long beginPull, Long endPull) {
        List<Object> selectParams = new ArrayList<>();

        String jpql = " SELECT count(pul.*) "
                + " FROM gitpullrequest pul "
                + " where pul.repository_id = ? "
                + "   and pul.number between ? and ? ";

        if (isOnlyMergeds()) {
            jpql += "  and pul.mergedat is not null ";
        }

        selectParams.add(getRepository().getId());
        selectParams.add(beginPull);
        selectParams.add(endPull);

        if (file != null) {
            jpql += "  and exists  "
                    + "  ( select f.* "
                    + "  from gitpullrequest_gitrepositorycommit r, "
                    + "       gitcommitfile f  "
                    + "  where r.entitypullrequest_id = pul.id "
                    + "    and f.repositorycommit_id = r.repositorycommits_id "
                    + "    and f.filename = ? ) ";
            selectParams.add(file);
        }

        if (file2 != null) {
            jpql += "  and exists  "
                    + "  ( select f2.*  "
                    + "  from gitpullrequest_gitrepositorycommit r2, "
                    + "       gitcommitfile f2  "
                    + "  where r2.entitypullrequest_id = pul.id "
                    + "    and f2.repositorycommit_id = r2.repositorycommits_id "
                    + "    and f2.filename = ? ) ";
            selectParams.add(file2);
        }

        Long count = dao.selectNativeOneWithParams(jpql, selectParams.toArray());

        return count != null ? count : 0l;
    }

}
