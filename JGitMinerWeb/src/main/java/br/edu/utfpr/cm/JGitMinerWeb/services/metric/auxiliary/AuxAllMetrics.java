/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrixNode;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxPairOfFiles;
import br.edu.utfpr.cm.JGitMinerWeb.util.JsfUtil;
import com.google.common.base.Strings;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author zanoni
 */
public class AuxAllMetrics {

    private final Long id;
    private final String file1;
    private final String file2;

    private final Set<Long> repoCommits_idList;
    private final Set<Long> pullRequests_idList;

    private final Set<EntityRepositoryCommit> repoCommits;
    private final Set<EntityPullRequest> pullRequests;

//    public AuxAllMetrics(String fileName1, String fileName2) {
//        this.file1 = fileName1;
//        this.file2 = fileName2;
//        this.repoCommits_idList = new HashSet<>();
//        this.pullRequests_idList = new HashSet<>();
//    }
    public AuxAllMetrics(EntityMatrixNode node, Long id) {

        this.id = id;

        String[] coluns = node.getLine().split("--");
        this.file1 = coluns[0].split(JsfUtil.TOKEN_SEPARATOR)[0];
        this.file2 = coluns[0].split(JsfUtil.TOKEN_SEPARATOR)[1];

        String[] ids = coluns[1].split("%");

        this.repoCommits_idList = new HashSet<>();
        this.pullRequests_idList = new HashSet<>();

        this.repoCommits = new HashSet<>();
        this.pullRequests = new HashSet<>();

        if (ids.length > 1) {
            String repoCommits_ids = ids[0];
            String pullRequests_ids = ids[1];

            this.setRepoCommits(repoCommits_ids);
            this.setPullRequests(pullRequests_ids);
        }

    }

    private void setRepoCommits(String repoCommits) {

        if (repoCommits != null && !"".equals(repoCommits)) {
            String[] ids = repoCommits.split(JsfUtil.TOKEN_SEPARATOR);

            for (String id : ids) {
                if (!Strings.isNullOrEmpty(id)) {
                    this.repoCommits_idList.add(Long.parseLong(id));
                }

            }
        }

    }

    private void setPullRequests(String pullRequests) {

        if (pullRequests != null && !"".equals(pullRequests)) {
            String[] ids = pullRequests.split(JsfUtil.TOKEN_SEPARATOR);

            for (String id : ids) {
                if (!Strings.isNullOrEmpty(id)) {
                    this.pullRequests_idList.add(Long.parseLong(id));
                }
            }
        }
    }

    @Override
    public String toString() {

        if (repoCommits_idList.isEmpty() && pullRequests_idList.isEmpty()) {
            return file1 + ";" + file2;
        }
        if (repoCommits_idList.isEmpty()) {
            return file1 + ";" + file2 + ";" + pullRequests_idList;
        }
        if (pullRequests_idList.isEmpty()) {
            return file1 + ";" + file2 + ";" + repoCommits_idList;
        }

        return file1 + ";" + file2 + ";" + repoCommits_idList + ";" + pullRequests_idList;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.file1) + Objects.hashCode(this.file2);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AuxAllMetrics other = (AuxAllMetrics) obj;

        if (Objects.equals(this.file1, other.file1)) {

            if (Objects.equals(this.file2, other.file2)) {
                return true;
            }
        }
        if (Objects.equals(this.file1, other.file2)) {

            if (Objects.equals(this.file2, other.file1)) {
                return true;
            }
        }

        return false;
    }

    public String getFile1() {
        return file1;
    }

    public String getFile2() {
        return file2;
    }

    public Set<Long> getRepoCommits_idList() {
        return repoCommits_idList;
    }

    public Set<Long> getPullRequests_idList() {
        return pullRequests_idList;
    }

    public Long getId() {
        return id;
    }

    public void addRepoCommit(EntityRepositoryCommit repositoryCommit) {
        this.repoCommits.add(repositoryCommit);
    }

    public void addPullRequest(EntityPullRequest pullRequest) {
        this.pullRequests.add(pullRequest);
    }

    public Set<EntityRepositoryCommit> getRepoCommits() {
        return repoCommits;
    }

    public Set<EntityPullRequest> getPullRequests() {
        return pullRequests;
    }
    
    

}
