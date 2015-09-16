/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.util.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author douglas
 */
public class AuxFileFileMetrics2 {

    private String file;
    private String file2;
    private Long repositorycommits_id;
    private Long repositorycommits_id2;
    private Long pullRequest_id;
    private List<Double> metrics = new ArrayList<>();
    private Set<Long> repositorycommits_idList = new HashSet<>();
    private Set<Long> pullRequest_idList = new HashSet<>();

    public AuxFileFileMetrics2(String file, String file2,
            String repositorycommits_id,
            String repositorycommits_id2,
            String pullRequest_id,
            double... metrics) {
        this.file = file;
        this.file2 = file2;
        this.repositorycommits_id = Long.parseLong(repositorycommits_id);
        this.repositorycommits_id2 = Long.parseLong(repositorycommits_id2);
        this.pullRequest_id = Long.parseLong(pullRequest_id);
        this.metrics = new ArrayList<>();
        this.repositorycommits_idList = new HashSet<>();
        addMetrics(metrics);
    }

    public AuxFileFileMetrics2(String file, String file2,
            String repositorycommits_id,
            String repositorycommits_id2,
            String pullRequest_id,
            List<Double> metrics) {

        this.file = file;
        this.file2 = file2;
        this.repositorycommits_id = Long.parseLong(repositorycommits_id);
        this.repositorycommits_id2 = Long.parseLong(repositorycommits_id2);
        this.pullRequest_id = Long.parseLong(pullRequest_id);
        this.metrics = metrics;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFile2() {
        return file2;
    }

    public void setFile2(String file2) {
        this.file2 = file2;
    }

    public List<Double> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Double> metrics) {
        this.metrics = metrics;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(file + ";" + file2);
        for (double m : metrics) {
            sb.append(";");
            sb.append(Util.tratarDoubleParaString(m));
        }
        sb.append(";--");
        //sb.append(repositorycommits_id).append(";").append(repositorycommits_id2);
        for (Long repo_commit_id : repositorycommits_idList) {
            sb.append(";");
            sb.append(repo_commit_id.toString());
        }

        sb.append(";%");
        for (Long pull_id : pullRequest_idList) {
            sb.append(";");
            sb.append(pull_id.toString());
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (Objects.hashCode(this.file) + Objects.hashCode(this.file2));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AuxFileFileMetrics2)) {
            return false;
        }
        final AuxFileFileMetrics2 other = (AuxFileFileMetrics2) obj;
        if (Util.stringEquals(this.file, other.file) && Util.stringEquals(this.file2, other.file2)) {
//            System.out.println("id1: " + other.getRepositorycommits_id());
//            this.repositorycommits_idList.add(other.getRepositorycommits_id());
//            System.out.println("id2: " + other.getRepositorycommits_id2());
//            this.repositorycommits_idList.add(other.getRepositorycommits_id2());
            return true;
        }
        if (Util.stringEquals(this.file, other.file2) && Util.stringEquals(this.file2, other.file)) {
//            System.out.println("id1: " + other.getRepositorycommits_id());
//            this.repositorycommits_idList.add(other.getRepositorycommits_id());
//            System.out.println("id2: " + other.getRepositorycommits_id2());
//            this.repositorycommits_idList.add(other.getRepositorycommits_id2());
            return true;
        }
        return false;
    }

    public void addMetrics(double... metrics) {
        for (double value : metrics) {
            this.metrics.add(value);
        }
    }

    public Long getRepositorycommits_id() {
        return repositorycommits_id;
    }

    public void setRepositorycommits_id(Long repositorycommits_id) {
        this.repositorycommits_id = repositorycommits_id;
    }

    public Long getRepositorycommits_id2() {
        return repositorycommits_id2;
    }

    public void setRepositorycommits_id2(Long repositorycommits_id2) {
        this.repositorycommits_id2 = repositorycommits_id2;
    }

    public void addRepoCommitIdAndPullRequestId(AuxFileFileMetrics2 pairFileFile) {

        this.repositorycommits_idList.add(pairFileFile.getRepositorycommits_id());
        this.repositorycommits_idList.add(pairFileFile.getRepositorycommits_id2());

        this.pullRequest_idList.add(pairFileFile.getPullRequest_id());
    }

    public Set<Long> getRepositorycommits_idList() {
        return repositorycommits_idList;
    }

    public void setRepositorycommits_idList(Set<Long> repositorycommits_idList) {
        this.repositorycommits_idList = repositorycommits_idList;
    }

    public void addRepositoyCommits_ids(List<Long> repoCommits_idList) {
        this.repositorycommits_idList.addAll(repoCommits_idList);
    }

    public Long getPullRequest_id() {
        return pullRequest_id;
    }

    public void setPullRequest_id(Long pullRequest_id) {
        this.pullRequest_id = pullRequest_id;
    }

}
