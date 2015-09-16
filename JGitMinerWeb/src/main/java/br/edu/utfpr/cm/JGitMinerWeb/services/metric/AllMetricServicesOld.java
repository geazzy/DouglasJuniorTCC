///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package br.edu.utfpr.cm.JGitMinerWeb.services.metric;
//
//import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
//import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrix;
//import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrixNode;
//import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityComment;
//import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
//import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
//import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityUser;
//import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.CoChangedFileServices;
//import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxPairOfFiles;
//import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxCoChangeMetrics;
//import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxCountTimeMetrics;
//import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxReputationRank;
//import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxTimeFinal;
//import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.ArquivosModificadosMetrics;
//import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.CountCoChangeMetric;
//import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.DelegateMetric;
//import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.LocationMetric;
//import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.MetricInterface;
//import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.NumberOfCommentsMetric;
//import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.NumberOfDevelopers;
//import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.NumberOfLinksMetric;
//import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.ReputationCommentsMetric;
//import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.ReputationCommiterMetric;
//import br.edu.utfpr.cm.JGitMinerWeb.services.metric.social.TimeMetric;
//import br.edu.utfpr.cm.JGitMinerWeb.util.JsfUtil;
//import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
//import com.google.common.collect.Lists;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.joda.time.DateTime;
//import org.joda.time.JodaTimePermission;
//import org.joda.time.Months;
//import org.joda.time.Period;
//import org.joda.time.format.DateTimeFormat;
//import org.joda.time.format.DateTimeFormatter;
//
///**
// *
// * @author geazzy
// */
//public class AllMetricServicesOld extends AbstractMetricServices {
//
//    //lista do pares de arquivos
//    private List<AuxCoChangeMetrics> pairOfFileList;
//    //Guarda o par de arquivos com um map<metrica, resultado>;
//    private Map<AuxPairOfFiles, HashMap<String, Object>> countMetricMap;
//    private List<Class<?>> metricList = new LinkedList<>();
//    //private static String teste;
//    private Map<EntityUser, Integer> committerRank = new HashMap<>();
//    private Map<EntityUser, Integer> commentsRank = new HashMap<>();
//    private Map<AuxPairOfFiles, AuxTimeFinal> timeMetricMap;
//
//    public AllMetricServicesOld(GenericDao dao, OutLog out) {
//        super(dao, out);
//        pairOfFileList = new ArrayList<>();
//        countMetricMap = new HashMap<>();
//        // metricList = new LinkedList<>();
//        metricList.add(CountCoChangeMetric.class);
//        metricList.add(LocationMetric.class);
//        metricList.add(NumberOfCommentsMetric.class);
//        metricList.add(NumberOfLinksMetric.class);
//        metricList.add(ReputationCommiterMetric.class);
//        metricList.add(ReputationCommentsMetric.class);
//        metricList.add(NumberOfDevelopers.class);
//        metricList.add(ArquivosModificadosMetrics.class);
//        
//
//        timeMetricMap = new HashMap<>();
//
//    }
//
//    public AllMetricServicesOld(GenericDao dao, EntityMatrix matrix, Map params, OutLog out) {
//        super(dao, matrix, params, out);
//        pairOfFileList = new ArrayList<>();
//        countMetricMap = new HashMap<>();
//        // metricList = new LinkedList<>();
//        metricList.add(CountCoChangeMetric.class);
//        metricList.add(LocationMetric.class);
//        metricList.add(NumberOfCommentsMetric.class);
//        metricList.add(NumberOfLinksMetric.class);
//        metricList.add(ReputationCommiterMetric.class);
//        metricList.add(ReputationCommentsMetric.class);
//        metricList.add(NumberOfDevelopers.class);
//        metricList.add(ArquivosModificadosMetrics.class);
//
//        timeMetricMap = new HashMap<>();
//    }
//
//    private void validacao() throws IllegalArgumentException {
//
//        if (getMatrix() == null && !getAvailableMatricesPermitted()
//                .contains(getMatrix().getClassServicesName())) {
//            throw new IllegalArgumentException("Selecione uma matriz gerada pelo Service: "
//                    + getAvailableMatricesPermitted());
//        }
//    }
//
//    @Override
//    public void run() {
//        System.out.println("parametros: " + params);
//
//        validacao();
//
//        out.printLog("Selected matrix with " + getMatrix().getNodes().size() + " nodes.");
//
//        setPairOfFilesList();
//
//        countMetrics();
//
//        System.out.println("set result");
//
//        addToEntityMetricNodeList(setResult());
//    }
//
//    private void setPairOfFilesList() {
//
//        for (EntityMatrixNode node : getMatrix().getNodes()) {
//
//            String[] coluns = node.getLine().split(JsfUtil.TOKEN_SEPARATOR);
//            AuxPairOfFiles acc = new AuxPairOfFiles(Long.parseLong(coluns[0]), coluns[1], coluns[2], coluns[3], coluns[4]);
//            AuxCoChangeMetrics auxTime = new AuxCoChangeMetrics(acc, dao);
//            pairOfFileList.add(auxTime);
//
//        }
//    }
//
//    private void countMetrics() {
//
//        setRank();
//
//        for (AuxCoChangeMetrics auxcochange : pairOfFileList) {
//
//            int count = 0;
//            DelegateMetric delegateMetric = new DelegateMetric();
//
//            for (Class<?> metrica : metricList) {
//                try {
//                    delegateMetric.addMetric((MetricInterface) metrica.newInstance());
//                } catch (InstantiationException | IllegalAccessException ex) {
//                    out.printLog("Erro ao instanciar as metricas: " + ex);
//                    Logger.getLogger(AllMetricServicesOld.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            //delegateMetric.addMetric(new NumberOfLinksMetric());
//
//            TimeMetric timeMetric = new TimeMetric();
//
//            if (!countMetricMap.containsKey(auxcochange.getAuxPairOfFiles())) {
//
//                for (AuxCoChangeMetrics pairOfCoChange : pairOfFileList) {
//                    if (pairOfCoChange.getAuxPairOfFiles().equals(auxcochange.getAuxPairOfFiles())) {
////                            && isBetweenDate(pairOfCoChange.getPullRequest().getCreatedAt())) {
//                        count++;
//
//                        // metodo polimorfico
//                        delegateMetric.calculateAllMetrics(pairOfCoChange, pairOfCoChange.getPullRequest());
//                        //System.out.println("Resultado: " + delegateMetric.getResult(pairOfFileList1));
//
//                        timeMetric.calculateMetric(pairOfCoChange, pairOfCoChange.getPullRequest(), 
//                                getDateTimeFromString(params.get("beginDate").toString()),
//                                getDateTimeFromString(params.get("endDate").toString()));
//                    }
//                }
//
//                countMetricMap.put(auxcochange.getAuxPairOfFiles(),
//                        delegateMetric.getResult(auxcochange));
//
//                timeMetricMap.put(auxcochange.getAuxPairOfFiles(),
//                        timeMetric.getResult(auxcochange));
//            }
//
//        }
//
//    }
//
//    @Override
//    public String getHeadCSV() {
//        StringBuilder head = new StringBuilder();
//        head.append("File1;File2;");
//
//        for (Class<?> metrica : metricList) {
//            try {
//                Method method = metrica.getMethod("getHeadCSV");
//                head.append(method.invoke(metrica.newInstance()).toString()).append(";");
//
//            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalArgumentException | InvocationTargetException | IllegalAccessException ex) {
//                out.printLog("Erro ao instanciar as metricas para o headCSV: " + ex);
//                Logger.getLogger(AllMetricServicesOld.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        head.append(AuxTimeFinal.headCSV()).append(";");
//
//        return head.toString();
//    }
//
//    @Override
//    public List<String> getAvailableMatricesPermitted() {
//        return Arrays.asList(CoChangedFileServices.class.getName());
//    }
//
//    private List<String> setResult() {
//
//        List<String> result = new ArrayList<>();
//
//        for (AuxPairOfFiles auxPairOfFiles : countMetricMap.keySet()) {
//
//            StringBuilder stringResult = new StringBuilder();
//
//            stringResult.append(auxPairOfFiles.getFile1());
//            stringResult.append(";");
//            stringResult.append(auxPairOfFiles.getFile2());
//            stringResult.append(";");
//
//            for (Class<?> metrica : metricList) {
//                stringResult.append(countMetricMap.get(auxPairOfFiles).get(metrica.getName())).append(";");
//
//            }
//
//            stringResult.append(timeMetricMap.get(auxPairOfFiles).toString());
//            result.add(stringResult.toString());
//
//            //System.out.println("rank size: " + AuxReputationRank.size());
//            // result.add(auxPairOfFiles.getArq1() + ";" + auxPairOfFiles.getArq2() + ";"
//            //      + countMetricMap.get(auxPairOfFiles).get(new CountCoChangeMetric().getClass().getName()) + ";"
//            //      + countMetricMap.get(auxPairOfFiles).get(new CommentsMetric().getClass().getName()) + ";"
//            //    + countMetricMap.get(auxPairOfFiles).get(new NumberOfLinksMetric().getClass().getName()));
//        }
////        AuxReputationRank reputation = AuxReputationRank.getInstance();
////        reputation.showCommentsRank();
////        reputation.showCommittersRank();
//        return result;
//    }
//
//    private void setRank() {
//
//        // Set<AuxCoChangeMetrics> cochangeVisit = new HashSet<>();
//        Set<EntityPullRequest> pullRequesUniqueList = new HashSet<>();
//
//        for (AuxCoChangeMetrics auxcochange : pairOfFileList) {
//
//            if (!pullRequesUniqueList.contains(auxcochange.getPullRequest())) {
//
//                setCommentRank(auxcochange.getPullRequest());
//                setCommitterRank(auxcochange.getPullRequest());
//                pullRequesUniqueList.add(auxcochange.getPullRequest());
//            }
//
//        }
//        AuxReputationRank reputationRank = AuxReputationRank.getInstance();
//        reputationRank.init(setTopRank(commentsRank, 15), setTopRank(committerRank, 15));
//
////        System.out.println("comments rank: " + setTopRank(commentsRank, 5));
////        System.out.println("comments rank size: " + commentsRank.size());
////        System.out.println("committers rank: " + setTopRank(committerRank, 5));
////        System.out.println("committers rank size: " + committerRank.size());
//    }
//
//    private List<EntityUser> setTopRank(Map<EntityUser, Integer> rank, int size) {
//
//        if (rank.size() < size) {
//            size = rank.size() - 1;
//        }
//
//        EntityUser[] topRank = new EntityUser[size];
//
//        List<EntityUser> rankList = Lists.newArrayList(rank.keySet());
//
//        for (int i = 0; i < topRank.length; i++) {
//            topRank[i] = rankList.get(i + 1);
//        }
//
////        System.out.println("TOPRANK " + topRank);
////
////        System.out.println("rankmap: " + rank);
//        for (EntityUser user : rank.keySet()) {
//            if (user != null) {
//                System.out.println(user);
//
//                for (int j = 0; j < size; j++) {
//                    // System.out.println("rank at j= " + j + " user: " + rank.get((EntityUser) user)
//                    //        + "toprank: " + topRank[j]);
//                    if (rank.get(user) >= rank.get(topRank[j])) {
//
//                        for (int k = size - 1; k > j; k--) {
//                            if (k < size) {
//                                topRank[k] = topRank[k - 1];
//                            }
//
//                        }
//                        topRank[j] = ((EntityUser) user);
//                        break;
//                    }
//                }
//            }
//        }
//
////        System.out.println("topRankFinal: " + topRank);
////        for (int i = 0; i < topRank.length; i++) {
////            System.out.println(i + " " + topRank[i]);
////        }
//        return Arrays.asList(topRank);
//    }
//
//    private void setCommentRank(EntityPullRequest pullRequest) {
//
//        for (EntityComment comments : pullRequest.getIssue().getComments()) {
//            if (commentsRank.containsKey(comments.getUser())) {
//                commentsRank.put(comments.getUser(), commentsRank.get(comments.getUser()) + 1);
//
//            } else {
//                commentsRank.put(comments.getUser(), 1);
//            }
//        }
//    }
//
//    private void setCommitterRank(EntityPullRequest pullRequest) {
//
//        for (EntityRepositoryCommit repoCommit : pullRequest.getRepositoryCommits()) {
//            if (committerRank.containsKey(repoCommit.getAuthor())) {
//                committerRank.put(repoCommit.getAuthor(), committerRank.get(repoCommit.getAuthor()) + 1);
//            } else {
//                committerRank.put(repoCommit.getAuthor(), 1);
//            }
//        }
//    }
//
////    private boolean isBetweenDate(Date createdAt) {
////
////        //DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
////        //Sun Jun 01 00:00:00 BRT 2014
////        //String[] str = params.get("beginDate").toString().split(" ");
////
////        //DateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int secondOfMinute) 
////        //DateTime begin = new DateTime(formatter.parseDateTime(str[2] + "/" + getMes(str[1]) + "/" + str[5] + " " + str[3]));
////
////        DateTime begin = getDateTimeFromString(params.get("beginDate").toString());
////        
////       // str = params.get("endDate").toString().split(" ");
////
////       // DateTime end = new DateTime(formatter.parseDateTime(str[2] + "/" + getMes(str[1]) + "/" + str[5] + " " + str[3]));
////        DateTime end = getDateTimeFromString(params.get("endDate").toString());
////        
////        DateTime created = new DateTime(createdAt);
////
////        if (created.isAfter(begin) && created.isBefore(end)) {
////            return true;
////        } else {
////            return false;
////        }
////
////    }
//
//    private DateTime getDateTimeFromString(String strParam){
//         DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
//        //Sun Jun 01 00:00:00 BRT 2014
//        String[] str = strParam.split(" ");
//
//        //DateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int secondOfMinute) 
//        DateTime date = new DateTime(formatter.parseDateTime(str[2] + "/" + getMes(str[1]) + "/" + str[5] + " " + str[3]));
//        
//        return date;
//    }
//    
//    private Integer getMes(String string) {
//
//        //int mes = 00;
//        switch (string) {
//
//            case "Jan":
//                return 1;
//
//            case "Feb":
//                return 2;
//            case "Mar":
//                return 3;
//            case "Apr":
//                return 4;
//            case "May":
//                return 5;
//            case "Jun":
//                return 6;
//            case "Jul":
//                return 7;
//            case "Aug":
//                return 8;
//            case "Sep":
//                return 9;
//            case "Oct":
//                return 10;
//            case "Nov":
//                return 11;
//            case "Dec":
//                return 12;
//
//            default:
//                return null;
//        }
//    }
//
//}
