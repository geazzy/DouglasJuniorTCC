/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.util.JsfUtil;
import com.google.common.base.Strings;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author geazzy
 */
public class AuxPairOfFiles {

    private Long idPullRequest;
    private String file1;
    private String shaCommitFile1;
    private String file2;
    private String shaCommitFile2;

    private Set<Long> repoCommits_idList;
    private Set<Long> pullRequests_idList;

    public AuxPairOfFiles(Long idPullRequest, String arq1, String arq2, String shaCommitArq1, String shaCommitArq2) {
        this.idPullRequest = idPullRequest;
        this.file1 = arq1;
        this.shaCommitFile1 = shaCommitArq1;
        this.file2 = arq2;
        this.shaCommitFile2 = shaCommitArq2;
    }

    public AuxPairOfFiles(String f1, String f2) {
        this.file1 = f1;
        this.file2 = f2;
        this.repoCommits_idList = new HashSet<>();
        this.pullRequests_idList = new HashSet<>();
    }

    @Override
    public String toString() {
//        return idPullRequest + ";" + file1 + ";" + file2 + ";" + shaCommitFile1 + ";" + shaCommitFile2;
        return file1 + ";" + file2 + ";" + repoCommits_idList + ";" + pullRequests_idList;
    }

    public Long getIdPullRequest() {
        return idPullRequest;
    }

    public void setIdPullRequest(Long idPullRequest) {
        this.idPullRequest = idPullRequest;
    }

    public String getFile1() {
        return file1;
    }

    public void setFile1(String file1) {
        this.file1 = file1;
    }

    public String getshaCommitFile1() {
        return shaCommitFile1;
    }

    public void setshaCommitFile1(String shaCommitFile1) {
        this.shaCommitFile1 = shaCommitFile1;
    }

    public String getFile2() {
        return file2;
    }

    public void setFile2(String file2) {
        this.file2 = file2;
    }

    public String getshaCommitFile2() {
        return shaCommitFile2;
    }

    public void setshaCommitFile2(String shaCommitFile2) {
        this.shaCommitFile2 = shaCommitFile2;
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
        final AuxPairOfFiles other = (AuxPairOfFiles) obj;

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

    public void setRepoCommits(String repoCommits) {

        if (repoCommits != null && !"".equals(repoCommits)) {
            String[] ids = repoCommits.split(JsfUtil.TOKEN_SEPARATOR);

            for (String id : ids) {
                if (!Strings.isNullOrEmpty(id)) {
                    this.repoCommits_idList.add(Long.parseLong(id));
                }

            }
        }

    }

    public void setPullRequests(String pullRequests) {

        if (pullRequests != null && !"".equals(pullRequests)) {
            String[] ids = pullRequests.split(JsfUtil.TOKEN_SEPARATOR);

            for (String id : ids) {
                if (!Strings.isNullOrEmpty(id)) {
                    this.pullRequests_idList.add(Long.parseLong(id));
                }
            }
        }
    }

}
