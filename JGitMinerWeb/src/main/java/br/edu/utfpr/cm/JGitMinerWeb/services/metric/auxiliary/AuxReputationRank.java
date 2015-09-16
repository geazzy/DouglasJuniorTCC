/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author zanoni
 */
public final class AuxReputationRank {

    private static  List<EntityUser> commentsRank = new ArrayList<>();
    private static  List<EntityUser> committersRank = new ArrayList<>();
    private static  AuxReputationRank instance;

    private AuxReputationRank() {

    }

    public static AuxReputationRank getInstance() {
        
        if (instance == null) {
            instance = new AuxReputationRank();
        }
        return instance;
    }
    
     public void init(List<EntityUser> commentsRankParam, List<EntityUser> committersRankParam) {
        this.commentsRank = commentsRankParam;
        this.committersRank = committersRankParam;
    }
     
     public void showCommittersRank(){
         System.out.println("CommittersRank:");
         System.out.println(committersRank);
     }
     
     public void showCommentsRank(){
         System.out.println("CommentsRank:");
         System.out.println(commentsRank);
     }
     
     public List<EntityUser> getCommittersRank(){
         return committersRank;
     }
     
      public List<EntityUser> getCommentsRank(){
         return commentsRank;
     }

    //public static void init()
//    private static Boolean containsUser(EntityUser user) {
//
//        return userRank.containsKey(user);
//    }
//
//    private static void updateUserRank(EntityUser user, Integer value) {
//
//        userRank.put(user, userRank.get(user) + 1);
//    }
//
//    private static void addUserToRank(EntityUser user, Integer value) {
//
//        userRank.put(user, value);
//    }
//
//    public static void updateRank(EntityUser user, Integer value) {
//
//        if (containsUser(user)) {
//            updateUserRank(user, value);
//        } else {
//            addUserToRank(user, value);
//        }
//    }
//
//    public static Integer getValue(EntityUser user) {
//        return userRank.get(user);
//    }
//
//    public static Integer size() {
//        return userRank.size();
//    }

    public boolean containCommitterInRank(EntityUser commitAuthor) {
        return committersRank.contains(commitAuthor);
    }

   
}
