package br.edu.utfpr.cm.minerador.services.metric;

import br.edu.utfpr.cm.JGitMinerWeb.dao.BichoDAO;
import br.edu.utfpr.cm.JGitMinerWeb.dao.BichoFileDAO;
import br.edu.utfpr.cm.JGitMinerWeb.dao.BichoPairFileDAO;
import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericBichoDAO;
import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrix;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrixNode;
import br.edu.utfpr.cm.JGitMinerWeb.model.metric.EntityMetric;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import br.edu.utfpr.cm.minerador.services.matrix.BichoPairOfFileGroupingByNumberOfIssuesServices;
import static br.edu.utfpr.cm.minerador.services.metric.AbstractBichoMetricServices.objectsToNodes;
import br.edu.utfpr.cm.minerador.services.metric.committer.Committer;
import br.edu.utfpr.cm.minerador.services.metric.committer.CommitterFileMetrics;
import br.edu.utfpr.cm.minerador.services.metric.committer.CommitterFileMetricsCalculator;
import br.edu.utfpr.cm.minerador.services.metric.committer.EmptyCommitterFileMetrics;
import br.edu.utfpr.cm.minerador.services.metric.model.CodeChurn;
import br.edu.utfpr.cm.minerador.services.metric.model.Commit;
import br.edu.utfpr.cm.minerador.services.metric.model.CommitMetrics;
import br.edu.utfpr.cm.minerador.services.metric.model.File;
import br.edu.utfpr.cm.minerador.services.metric.model.FileIssueMetrics;
import br.edu.utfpr.cm.minerador.services.metric.model.FilePair;
import br.edu.utfpr.cm.minerador.services.metric.model.IssueMetrics;
import br.edu.utfpr.cm.minerador.services.metric.socialnetwork.NetworkMetrics;
import br.edu.utfpr.cm.minerador.services.metric.socialnetwork.NetworkMetricsCalculator;
import br.edu.utfpr.cm.minerador.services.util.MatrixUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.BooleanUtils;

/**
 *
 * @author Rodrigo T. Kuroda
 */
public class BichoPairFileMetricsInNumberOfIssuesServices extends AbstractBichoMetricServices {

    private String repository;

    public BichoPairFileMetricsInNumberOfIssuesServices() {
        super();
    }

    public BichoPairFileMetricsInNumberOfIssuesServices(GenericBichoDAO dao, GenericDao genericDao, EntityMatrix matrix, Map<Object, Object> params, OutLog out) {
        super(dao, genericDao, matrix, params, out);
    }

    public String getAdditionalFilename() {
        return getStringParam("filename");
    }

    public Integer getIndex() {
        return getIntegerParam("index");
    }

    public Integer getQuantity() {
        return getIntegerParam("quantity");
    }

    public Integer getGroupsQuantity() {
        return getIntegerParam("groupsQuantity");
    }

    @Override
    public void run() {
        repository = getRepository();

        out.printLog("Iniciado cálculo da métrica de matriz com " + getMatrix().getNodes().size() + " nodes. Parametros: " + params);

        final int maxFilePerCommit = 20;
        final BichoDAO bichoDAO = new BichoDAO(dao, repository, maxFilePerCommit);
        final BichoFileDAO bichoFileDAO = new BichoFileDAO(dao, repository, maxFilePerCommit);
        final BichoPairFileDAO bichoPairFileDAO = new BichoPairFileDAO(dao, repository, maxFilePerCommit);

        final Map<String, Integer> headerIndexesMap = MatrixUtils.extractHeaderIndexes(matrix);
        final List<EntityMatrixNode> matrixNodes = MatrixUtils.extractValues(matrix);
        final int quantity;
        if (getGroupsQuantity() != null && getGroupsQuantity() > 0) {
            final long totalIssues = bichoDAO.calculeNumberOfAllFixedIssues();
            quantity = Double.valueOf(Math.ceil(totalIssues / getGroupsQuantity().doubleValue())).intValue();
            out.printLog("Total issues: " + totalIssues);
            out.printLog("Quantity per group (" + getGroupsQuantity() + "): " + quantity);
        } else if (getQuantity() != null && getQuantity() > 0) {
            quantity = getQuantity();

        } else {
            throw new IllegalArgumentException("Parameter quantity or group quantity is required.");
        }
        final int analysisIndex = getIndex();
        final int pastIndex = analysisIndex - 1;
        final int futureIndex = analysisIndex + 1;

        // cache for optimization number of pull requests where file is in,
        // reducing access to database
        Cacher cacher = new Cacher(bichoFileDAO, bichoPairFileDAO);

        // pares I+J em ordem de support e confidence
        Set<FilePair> issuesGroup = getFilePairsFromMatrix(matrix.getNodes().get(0), matrixNodes, headerIndexesMap);

        Set<Integer> issues = bichoDAO.selectIssuesAndType(quantity, analysisIndex);//getIntegerSetFromMatrix(matrixNodes, headerIndexesMap.get("issuesId"));
        Set<Integer> futureIssues = bichoDAO.selectIssuesAndType(quantity, futureIndex);//getIntegerSetFromMatrix(matrixNodes, headerIndexesMap.get("futureIssuesId"));

        final Integer issuesSize = issues.size();

        System.out.println("Number of all pull requests: " + issuesSize);

        CommitterFileMetricsCalculator committerFileMetricsCalculator = new CommitterFileMetricsCalculator(bichoFileDAO);

        final Set<Committer> majorContributorsInPreviousVersion = new HashSet<>();
        final Map<String, Double> ownerExperienceInPreviousVersionMap = new HashMap<>();
        final Map<CommitterFileMetrics, CommitterFileMetrics> committerFileMetricsList = new HashMap<>();
        // calcule committer experience for each file pairs in previous version
        for (FilePair filePair : issuesGroup) {
            final String filename = filePair.getFileName();

            // committers do arquivo I
            final Set<Committer> fileCommittersInPreviousVersion
                    = bichoFileDAO.selectCommitters(filename, issues);

            for (Committer committer : fileCommittersInPreviousVersion) {
                CommitterFileMetrics committerFileMetrics;
                if (pastIndex >= 0) {
                    committerFileMetrics
                            = committerFileMetricsCalculator.calculeForIndex(
                                    filename, committer, pastIndex, quantity);
                } else {
                    committerFileMetrics = new EmptyCommitterFileMetrics();
                }
                committerFileMetricsList.put(committerFileMetrics, committerFileMetrics);
                if (committerFileMetrics.getOwnership() > 0.05) { // maior que 5% = major
                    majorContributorsInPreviousVersion.add(committer);
                }

                if (ownerExperienceInPreviousVersionMap.containsKey(filename)) {
                    ownerExperienceInPreviousVersionMap.put(filename, Math.max(committerFileMetrics.getExperience(), ownerExperienceInPreviousVersionMap.get(filename)));
                } else {
                    ownerExperienceInPreviousVersionMap.put(filename, committerFileMetrics.getExperience());
                }
            }
        }

        int rank = 1;
        for (FilePair filePair : issuesGroup) {
            final Set<FileIssueMetrics> allFileChanges = new LinkedHashSet<>();

            // par analisado
            final String filename = filePair.getFileName(); // arquivo principal
            final String filename2 = filePair.getFileName2();
            final File file = new File(filename);
            final File file2 = new File(filename2);

            final List<Integer> issueWhereFileChanged = bichoFileDAO.selectIssues(filename, issues);
            int progress = 0, totalProgress = issueWhereFileChanged.size();

            for (Integer issue : issueWhereFileChanged) {

                if (progress++ % 100 == 0 || progress == totalProgress) {
                    System.out.println("Rank " + rank + " - " + progress + "/" + totalProgress);
                }

                final IssueMetrics issueMetrics = cacher.calculeIssueMetrics(issue);

                final Set<Commit> issueCommits = bichoDAO.selectFilesAndCommitByIssue(issue);

                for (Commit commitInIssue : issueCommits) {
                    if (!commitInIssue.getFiles().contains(file)) {
                        continue;
                    }
                    Set<File> filesInCommit = commitInIssue.getFiles();

                    // metricas do arquivo com maior confiança, somente
                    final FileIssueMetrics fileIssueMetrics = new FileIssueMetrics(filename, filename2, commitInIssue, issueMetrics);

                    // TODO metrics do commit
                    final CommitMetrics commitMetrics = new CommitMetrics(commitInIssue);

                    final CommitterFileMetrics committerFileMetrics;
                    final CommitterFileMetrics committerFileInAnalysis = new CommitterFileMetrics(commitInIssue.getCommiter(), file, 0, 0);
                    if (committerFileMetricsList.containsKey(committerFileInAnalysis)) {
                        // committer already commits the file
                        committerFileMetrics = committerFileMetricsList.get(committerFileInAnalysis);
                    } else {
                        // committer does not commit the file yet (i.e. first commit of committer)
                        committerFileMetrics = new EmptyCommitterFileMetrics();
                    }

                    fileIssueMetrics.setCommitMetrics(commitMetrics);
                    fileIssueMetrics.setCommitterFileMetrics(committerFileMetrics);

                    if (filesInCommit.contains(file2)) { // se houve commit do arquivo 2, então o par mudou
                        // muda a coluna changed para 1, indicando que o par analisado mudou nessa issue
                        fileIssueMetrics.changed();
                    }

                    // calculo das metricas de commit apenas para o primeiro arquivo
                    if (!allFileChanges.contains(fileIssueMetrics)) {
                        // pair file network
                        final NetworkMetrics networkMetrics
                                = new NetworkMetricsCalculator(issue, bichoDAO).getNetworkMetrics();

                        fileIssueMetrics.setNetworkMetrics(networkMetrics);

                        final long totalCommitters = bichoFileDAO.calculeCummulativeCommitters(filename, issue, issues);
                        final long totalCommits = bichoFileDAO.calculeCummulativeCommits(filename, issues);

                        final Map<String, Long> futureIssuesTypes
                                = bichoFileDAO.calculeNumberOfIssuesGroupedByType(
                                        filename, futureIssues);
                        final long numberOfFutureDefects;
                        if (!futureIssuesTypes.containsKey("Bug")) {
                            numberOfFutureDefects = 0;
                        } else {
                            numberOfFutureDefects = futureIssuesTypes.get("Bug");
                        }

                        long numberOfFutureIssues = 0;
                        for (Map.Entry<String, Long> entrySet : futureIssuesTypes.entrySet()) {
                            numberOfFutureIssues += entrySet.getValue();
                        }

                        final Committer lastCommitter = bichoFileDAO.selectLastCommitter(file.getFileName(), commitInIssue, issues);
                        final boolean sameOwnership = commitInIssue.getCommiter().equals(lastCommitter);

                        final CodeChurn fileCodeChurn = bichoFileDAO.calculeAddDelChanges(filename, issue, commitInIssue.getId(), issues);

                        // file age in release interval (days)
                        final int ageRelease = bichoFileDAO.calculeFileAgeInDays(filename, issue, issues);

                        // file cummulative age: from first commit until previous (past) release
                        final int ageTotal = bichoFileDAO.calculeTotalFileAgeInDays(filename, issue);
                        final double ownerExperienceInPreviousVersion = ownerExperienceInPreviousVersionMap.get(filename);

                        fileIssueMetrics.addMetrics(// majorContributors
                                BooleanUtils.toInteger(majorContributorsInPreviousVersion.contains(commitInIssue.getCommiter())),
                                // ownerExperience,
                                ownerExperienceInPreviousVersion == Double.NaN ? 0.0d : ownerExperienceInPreviousVersion,
                                // sameOwnership
                                BooleanUtils.toInteger(sameOwnership),
                                // committers, totalCommitters, commits, totalCommits,
                                totalCommitters, totalCommits,
                                // pairFileCodeChurn.getAdditionsNormalized(), pairFileCodeChurn.getDeletionsNormalized(), pairFileCodeChurn.getChanges()
                                fileCodeChurn.getAdditionsNormalized(), fileCodeChurn.getDeletionsNormalized(), fileCodeChurn.getChanges(),
                                // ageRelease, ageTotal
                                ageRelease, ageTotal,
                                // futureDefects, futureIssues
                                numberOfFutureDefects, numberOfFutureIssues
                        );

                        allFileChanges.add(fileIssueMetrics);
                    }
                }
            }

            EntityMetric metrics = new EntityMetric();
            metrics.setNodes(objectsToNodes(allFileChanges, FileIssueMetrics.HEADER));
            metrics.getParams().put("rank", rank++);
            metrics.setAdditionalFilename(getAdditionalFilename());
            saveMetrics(metrics, getClass());
        }
    }

    public String getHeadCSV() {
        return "file;file2;issue;"
                + "issueType;issuePriority;issueAssignedTo;issueSubmittedBy;"
                + "issueWatchers;issueReopened;"
                + "samePackage;sameOwnership;" // arquivos do par são do mesmo pacote = 1, caso contrário 0
                // + "brcAvg;brcSum;brcMax;"
                + "btwSum;btwAvg;btwMdn;btwMax;"
                + "clsSum;clsAvg;clsMdn;clsMax;"
                + "dgrSum;dgrAvg;dgrMdn;dgrMax;"
                //+ "egvSum;egvAvg;egvMax;"
                + "egoBtwSum;egoBtwAvg;egoBtwMdn;egoBtwMax;"
                + "egoSizeSum;egoSizeAvg;egoSizeMdn;egoSizeMax;"
                + "egoTiesSum;egoTiesAvg;egoTiesMdn;egoTiesMax;"
                // + "egoPairsSum;egoPairsAvg;egoPairsMax;"
                + "egoDensitySum;egoDensityAvg;egoDensityMdn;egoDensityMax;"
                + "efficiencySum;efficiencyAvg;efficiencyMdn;efficiencyMax;"
                + "efvSizeSum;efvSizeAvg;efvSizeMdn;efvSizeMax;"
                + "constraintSum;constraintAvg;constraintMdn;constraintMax;"
                + "hierarchySum;hierarchyAvg;hierarchyMdn;hierarchyMax;"
                + "size;ties;density;diameter;"
                + "devCommitsSum;devCommitsAvg;devCommitsMdn;devCommitsMax;"
                + "ownershipSum;ownershipAvg;ownershipMdn;ownershipMax;"
                + "majorContributors;minorContributors;"
                + "oexp;oexp2;"
                + "own;own2;"
                + "numFiles;"
                + "committers;" // committers na release
                + "totalCommitters;" // ddev, committers desde o começo até a data final da relese
                + "commits;" // commits do par de arquivos na release
                + "totalCommits;" // todos commits do par de arquivos
                + "devCommenters;" // número de autores de comentários que são desenvolvedores
                + "commenters;comments;wordiness;"
                + "codeChurn;codeChurn2;codeChurnAvg;"
                + "add;del;changes;"
                // + "rigidityFile1;rigidityFile2;rigidityPairFile;"
                // + "taskImprovement;taskDefect;"
                + "ageRelease;ageTotal;"
                + "updates;futureUpdates;futureDefects;"
                + "fileIssues;file2Issues;allIssues;"
                + "supportFile;supportFile2;supportPairFile;confidence;confidence2;lift;conviction;conviction2;changed";
    }

    @Override
    public List<String> getAvailableMatricesPermitted() {
        return Arrays.asList(BichoPairOfFileGroupingByNumberOfIssuesServices.class.getName());
    }

    private String getRepository() {
        return getMatrix().getRepository();
    }    
}
