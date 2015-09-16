/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrix;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrixNode;
import br.edu.utfpr.cm.JGitMinerWeb.model.metric.EntityMetric;
import br.edu.utfpr.cm.JGitMinerWeb.model.metric.EntityMetricNode;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityComment;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityUser;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.CochangeSupportConfidenceLiftConvictionInDateServices;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxAllMetrics;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxReputationRank;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxTimeFinal;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.ArquivosModificadosMetrics;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.CollaboratorsMetric;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.CountCoChangeMetric;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.DelegateMetric;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.LocationMetric;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.MetricInterface;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.NumberOfCommentsMetric;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.NumberOfDevelopers;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.NumberOfLinksMetric;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.NumberOfMentionsMetric;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.ReputationCommentsMetric;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.ReputationCommiterMetric;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.StatusMetric;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.NumberOfAddLinesMetric;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.NumberOfChangeLinesMetric;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.NumberOfDeletionsLinesMetric;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.TempoDeResposta;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.TimeMetric;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.WordinessMetric;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import com.google.common.collect.Lists;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author geazzy
 */
public class AllMetricServices extends AbstractMetricServices {

    //lista do pares de arquivos
    private List<AuxAllMetrics> pairOfFileList;
    //Guarda o par de arquivos com um map<metrica, resultado>;
    private Map<AuxAllMetrics, HashMap<String, Object>> countMetricMap;
    private List<Class<?>> metricList = new LinkedList<>();
    //private static String teste;
    private Map<EntityUser, Integer> committerRank = new HashMap<>();
    private Map<EntityUser, Integer> commentsRank = new HashMap<>();
    private Map<Long, AuxTimeFinal> timeMetricMap;
    private EntityMetric entityMetric;
           
    private Integer sizeTopRank;

    public AllMetricServices(GenericDao dao, OutLog out) {
        super(dao, out);
        pairOfFileList = new LinkedList<>();
        countMetricMap = new HashMap<>();
        // metricList = new LinkedList<>();
        metricList.add(CountCoChangeMetric.class);
        metricList.add(LocationMetric.class);
        metricList.add(NumberOfCommentsMetric.class);
        metricList.add(NumberOfLinksMetric.class);
        metricList.add(ReputationCommiterMetric.class);
        metricList.add(ReputationCommentsMetric.class);
        metricList.add(NumberOfDevelopers.class);
        metricList.add(ArquivosModificadosMetrics.class);
        metricList.add(WordinessMetric.class);
        metricList.add(CollaboratorsMetric.class);
        metricList.add(NumberOfMentionsMetric.class);
        metricList.add(TempoDeResposta.class);
        metricList.add(StatusMetric.class);
        metricList.add(NumberOfAddLinesMetric.class);
        metricList.add(NumberOfDeletionsLinesMetric.class);
        metricList.add(NumberOfChangeLinesMetric.class);

        timeMetricMap = new HashMap<>();

        sizeTopRank = 0;

        entityMetric = new EntityMetric();
    }

    public AllMetricServices(GenericDao dao, EntityMatrix matrix, Map params, OutLog out) {
        super(dao, matrix, params, out);
        pairOfFileList = new LinkedList<>();
        countMetricMap = new HashMap<>();
        // metricList = new LinkedList<>();
        metricList.add(CountCoChangeMetric.class);
        metricList.add(LocationMetric.class);
        metricList.add(NumberOfCommentsMetric.class);
        metricList.add(NumberOfLinksMetric.class);
        metricList.add(ReputationCommiterMetric.class);
        metricList.add(ReputationCommentsMetric.class);
        metricList.add(NumberOfDevelopers.class);
        metricList.add(ArquivosModificadosMetrics.class);
        metricList.add(WordinessMetric.class);
        metricList.add(CollaboratorsMetric.class);
        metricList.add(NumberOfMentionsMetric.class);
        metricList.add(TempoDeResposta.class);
        metricList.add(StatusMetric.class);
        metricList.add(NumberOfAddLinesMetric.class);
        metricList.add(NumberOfDeletionsLinesMetric.class);
        metricList.add(NumberOfChangeLinesMetric.class);

        timeMetricMap = new HashMap<>();

        sizeTopRank = 0;
        entityMetric = new EntityMetric();
    }

    private void validacao() throws IllegalArgumentException {

        if (getMatrix() == null && !getAvailableMatricesPermitted()
                .contains(getMatrix().getClassServicesName())) {
            throw new IllegalArgumentException("Selecione uma matriz gerada pelo Service: "
                    + getAvailableMatricesPermitted());
        }
    }

    @Override
    public void run() {
        System.out.println("parametros: " + params);

        validacao();

        out.printLog("Selected matrix with " + getMatrix().getNodes().size() + " nodes.");

        out.printLog("Number of pages: " + getPages());
        
        setPairOfFilesList();

        System.out.println("TESTE");

        countMetrics();

        //System.out.println("set result");

       // addToEntityMetricNodeList(setResult());
      //  saveEntityMetric(entityMetric);
    }

    private void saveEntityMetric(EntityMetric entityMetric) {
        out.printLog("Iniciando salvamento da rede.");
        List<EntityMetricNode> metricNodes = entityMetric.getNodes();
        entityMetric.setNodes(new ArrayList<EntityMetricNode>());
        entityMetric.getParams().putAll(params);
        //entityMetric.setClassServicesName(get + "");
        entityMetric.setClassServicesName(this.getClass().getName());
        entityMetric.setLog(out.getLog().toString());
        dao.insert(entityMetric);
        for (Iterator<EntityMetricNode> it = metricNodes.iterator(); it.hasNext();) {
            EntityMetricNode node = it.next();
            node.setMetric(entityMetric);
            entityMetric.getNodes().add(node);
            dao.insert(node);
            it.remove();
        }
        entityMetric.setStoped(new Date());
        entityMetric.setComplete(true);
        out.printLog("Concluida geração da rede.");
        entityMetric.setLog(out.getLog().toString());
        dao.edit(entityMetric);
        out.printLog("");
    }
    private void setPairOfFilesList() {

        Long id = 0L;

        for (EntityMatrixNode node : getMatrix().getNodes()) {

            AuxAllMetrics auxPair = new AuxAllMetrics(node, id);

            for (Long idRepoCommit : auxPair.getRepoCommits_idList()) {
                auxPair.addRepoCommit(getRepositoryCommit(idRepoCommit));
            }
            for (Long idPullRequest : auxPair.getPullRequests_idList()) {
                auxPair.addPullRequest(getPullRequest(idPullRequest));
            }
            pairOfFileList.add(auxPair);
           // System.out.println(id);
            
             if (id % 100 == 0 || id == getMatrix().getNodes().size()) {
                    System.out.println(id + "/" + getMatrix().getNodes().size());
                }
            id++;
        }
    }

    private void countMetrics() {

        setRank();
        int pages = getPages();
        
        int quantidade = pairOfFileList.size() / pages;

        for (int i = 0; i <= pages; i++) {
            
            out.printLog("Página: " + i);

            count(this.getSubList(i, quantidade));
        }

    }

    private void count(List<AuxAllMetrics> pairOfFileListParam) {

        for (AuxAllMetrics pairOfFile : pairOfFileListParam) {

            DelegateMetric delegateMetric = new DelegateMetric();

            for (Class<?> metrica : metricList) {
                try {
                    delegateMetric.addMetric((MetricInterface) metrica.newInstance());
                } catch (InstantiationException | IllegalAccessException ex) {
                    out.printLog("Erro ao instanciar as metricas: " + ex);
                    Logger.getLogger(AllMetricServices.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            

            TimeMetric timeMetric = new TimeMetric();

            
            delegateMetric.calculateAllMetrics(pairOfFile);
            

            timeMetric.calculateMetric(pairOfFile,
                    getDateTimeFromString(params.get("beginDate").toString()),
                    getDateTimeFromString(params.get("endDate").toString()));

            countMetricMap.put(pairOfFile,
                    delegateMetric.getResult(pairOfFile.getId()));

            timeMetricMap.put(pairOfFile.getId(),
                    timeMetric.getResult(pairOfFile.getId()));
        }
        setResult();
        countMetricMap.clear();
        timeMetricMap.clear();
         
    }

    @Override
    public String getHeadCSV() {
        StringBuilder head = new StringBuilder();
        head.append("File1;File2;");

        for (Class<?> metrica : metricList) {
            try {
                Method method = metrica.getMethod("getHeadCSV");
                head.append(method.invoke(metrica.newInstance()).toString()).append(";");

            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalArgumentException | InvocationTargetException | IllegalAccessException ex) {
                out.printLog("Erro ao instanciar as metricas para o headCSV: " + ex);
                Logger.getLogger(AllMetricServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        head.append(AuxTimeFinal.headCSV()).append(";");

        return head.toString();
    }

    @Override
    public List<String> getAvailableMatricesPermitted() {
        return Arrays.asList(CochangeSupportConfidenceLiftConvictionInDateServices.class.getName());
    }

    private void setResult() {

        List<String> result = new ArrayList<>();

        for (AuxAllMetrics auxPairOfFiles : countMetricMap.keySet()) {

            StringBuilder stringResult = new StringBuilder();

            stringResult.append(auxPairOfFiles.getFile1());
            stringResult.append(";");
            stringResult.append(auxPairOfFiles.getFile2());
            stringResult.append(";");

            for (Class<?> metrica : metricList) {
                stringResult.append(countMetricMap.get(auxPairOfFiles).get(metrica.getName())).append(";");

            }

            stringResult.append(timeMetricMap.get(auxPairOfFiles.getId()).toString());
            result.add(stringResult.toString());


        }

        addToEntityMetricNodeList(result);
       // result.clear();
       // return result;
        
    }

    private void setRank() {

  
        Set<Long> pullRequesUniqueList = new HashSet<>();

        for (AuxAllMetrics auxcochange : pairOfFileList) {

            for (Long pullRequest_id : auxcochange.getPullRequests_idList()) {

                if (!pullRequesUniqueList.contains(pullRequest_id)) {

                    setCommentRank(pullRequest_id);
                    setCommitterRank(pullRequest_id);
                    pullRequesUniqueList.add(pullRequest_id);
                }
            }

        }
        AuxReputationRank reputationRank = AuxReputationRank.getInstance();
        reputationRank.init(setTopRank(commentsRank, getSizeTopRank()), setTopRank(committerRank, getSizeTopRank()));


    }

    private List<EntityUser> setTopRank(Map<EntityUser, Integer> rank, int size) {

        if (rank.size() < size) {
            size = rank.size() - 1;
        }

        EntityUser[] topRank = new EntityUser[size];

        List<EntityUser> rankList = Lists.newArrayList(rank.keySet());

        for (int i = 0; i < topRank.length; i++) {
            topRank[i] = rankList.get(i + 1);
        }

        for (EntityUser user : rank.keySet()) {
            if (user != null) {
             //   System.out.println(user);

                for (int j = 0; j < size; j++) {

                    if (rank.get(user) >= rank.get(topRank[j])) {

                        for (int k = size - 1; k > j; k--) {
                            if (k < size) {
                                topRank[k] = topRank[k - 1];
                            }

                        }
                        topRank[j] = ((EntityUser) user);
                        break;
                    }
                }
            }
        }

        return Arrays.asList(topRank);
    }

    private void setCommentRank(Long pullRequest_id) {

        EntityPullRequest pullRequest = getPullRequest(pullRequest_id);

        try {
            for (EntityComment comments : pullRequest.getIssue().getComments()) {
                if (commentsRank.containsKey(comments.getUser())) {
                    commentsRank.put(comments.getUser(), commentsRank.get(comments.getUser()) + 1);

                } else {
                    commentsRank.put(comments.getUser(), 1);
                }
            }
        } catch (Exception e) {
            out.printLog("Erro ao processar setCommentRank!");
            out.printLog("pullRequest_id:" + pullRequest_id);
            out.printLog("pullRequest getissue: " + pullRequest.getIssue());
            out.printLog(e.toString());
        }

        //}
    }

    private void setCommitterRank(Long pullRequest_id) {

        EntityPullRequest pullRequest = getPullRequest(pullRequest_id);


        try {
            for (EntityRepositoryCommit repoCommit : pullRequest.getRepositoryCommits()) {
                if (committerRank.containsKey(repoCommit.getAuthor())) {
                    committerRank.put(repoCommit.getAuthor(), committerRank.get(repoCommit.getAuthor()) + 1);
                } else {
                    committerRank.put(repoCommit.getAuthor(), 1);
                }
            }
        } catch (Exception e) {
            out.printLog("Erro ao processar setCommentRank!");
            out.printLog("pullRequest_id:" + pullRequest_id);
            out.printLog("pullRequest getissue: " + pullRequest.getIssue());
//             out.printLog("pullRequest getissue comments size: " + pullRequest.getIssue().getComments().size());
            out.printLog(e.toString());
        }

    }


    private DateTime getDateTimeFromString(String strParam) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        //Sun Jun 01 00:00:00 BRT 2014
        String[] str = strParam.split(" ");
 
        DateTime date = new DateTime(formatter.parseDateTime(str[2] + "/" + getMes(str[1]) + "/" + str[5] + " " + str[3]));

        return date;
    }

    private Integer getMes(String string) {

        switch (string) {

            case "Jan":
                return 1;

            case "Feb":
                return 2;
            case "Mar":
                return 3;
            case "Apr":
                return 4;
            case "May":
                return 5;
            case "Jun":
                return 6;
            case "Jul":
                return 7;
            case "Aug":
                return 8;
            case "Sep":
                return 9;
            case "Oct":
                return 10;
            case "Nov":
                return 11;
            case "Dec":
                return 12;

            default:
                return null;
        }
    }

    private EntityPullRequest getPullRequest(Long id) {

        return (EntityPullRequest) dao.getEntityManager()
                .createNamedQuery("PullRequest.findById")
                .setParameter("idPullRequest", id)
                .getSingleResult();
    }

    private EntityRepositoryCommit getRepositoryCommit(Long id) {

        return (EntityRepositoryCommit) dao.getEntityManager()
                .createNamedQuery("RepositoryCommit.findById")
                .setParameter("id", id)
                .getSingleResult();
    }

    private int getSizeTopRank() {
        return getIntegerParam("sizeTopRank");
    }
    
     private int getPages() {
        return getIntegerParam("pages");
    }

    private List<AuxAllMetrics> getSubList(int page, int quantity) {
        int fromIndex = page * quantity;
        int toIndex = fromIndex + quantity;

        if (toIndex > pairOfFileList.size()) {
            toIndex = pairOfFileList.size();
        }
        
        out.printLog("From: "+fromIndex+" to: "+toIndex);
        return pairOfFileList.subList(fromIndex, toIndex);
    }

    private List<EntityMetricNode> objectsToNodes(Collection list) {
        
        List<EntityMetricNode> nodes = new ArrayList<>();
        for (Object obj : list) {
            nodes.add(new EntityMetricNode(obj));
        }
        return nodes;
    
    }

}
