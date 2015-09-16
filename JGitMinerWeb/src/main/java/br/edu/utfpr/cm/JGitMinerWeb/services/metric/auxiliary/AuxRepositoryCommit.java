/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
import java.util.List;

/**
 *
 * @author geazzy
 */
public class AuxRepositoryCommit {
    private static final GenericDao dao = new GenericDao();

//    private final GenericDao dao;
//    private EntityRepositoryCommit repositoryCommit;
//    
//    public AuxRepositoryCommit(GenericDao dao, String sha) {
//        this.dao = dao;
//        setRepositoryCommit(sha);
//    }
//    private void setRepositoryCommit(String sha){
//             
//        repositoryCommit = (EntityRepositoryCommit)dao.getEntityManager()
//                .createNamedQuery("RepositoryCommit.findBySHA")
//                .setParameter("sha", sha)
//                .getSingleResult();
//        
//        
//    }
    public static EntityRepositoryCommit getRepositoryCommitBySha(GenericDao daoTemp,
            String sha) {

      
        //SELECT c FROM EntityRepositoryCommit c WHERE c.sha = :sha
        System.out.println("sha: " + sha);

        EntityRepositoryCommit result = (EntityRepositoryCommit) daoTemp.getEntityManager()
                .createNamedQuery("RepositoryCommit.findBySHA")
                .setParameter("sha", sha)
                .getSingleResult();

        System.out.println("RESULTADO: "+result);
        
        return result;

//        String jpql = "SELECT  c "
//                + "FROM EntityRepositoryCommit c "
//                + "WHERE c.sha = :sha ";
//
//        System.out.println(jpql);
//        System.out.println("sha: " + sha);
//        
//        if(sha == null){
//            System.out.println("NULL");
//        }
//        List<EntityRepositoryCommit> result = dao.selectWithParams(jpql,
//                new String[]{"sha"}, new Object[]{sha});
//        
//        System.out.println("Resultado: "+result);
//        return result.get(0);
    }

    public static EntityRepositoryCommit getRepositoryCommitBySha(String sha) {
        //SELECT c FROM EntityRepositoryCommit c WHERE c.sha = :sha
        System.out.println("sha: " + sha);

        if (sha.equals("16ea791376818e58647bb6ea808edff93d1936b4")){
            return null;
        }
        
        EntityRepositoryCommit result = (EntityRepositoryCommit) dao.getEntityManager()
                .createNamedQuery("RepositoryCommit.findBySHA")
                .setParameter("sha", sha)
                .getSingleResult();

        System.out.println("RESULTADO: "+result);
        
        return result;

//        String jpql = "SELECT  c "
//                + "FROM EntityRepositoryCommit c "
//                + "WHERE c.sha = :sha ";
//
//        System.out.println(jpql);
//        System.out.println("sha: " + sha);
//        
//        if(sha == null){
//            System.out.println("NULL");
//        }
//        List<EntityRepositoryCommit> result = dao.selectWithParams(jpql,
//                new String[]{"sha"}, new Object[]{sha});
//        
//        System.out.println("Resultado: "+result);
//        return result.get(0)
    }

}
