/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityCommitFile;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
import br.edu.utfpr.cm.JGitMinerWeb.util.GeoCodeUtil;
import java.util.Objects;

/**
 *
 * @author geazzy
 */
public class AuxNewPairOfFiles {

    private EntityPullRequest pullRequest;
    private EntityCommitFile file1;
    private EntityRepositoryCommit commitFile1;
    private EntityCommitFile file2;
    private EntityRepositoryCommit commitFile2;


    public AuxNewPairOfFiles(EntityPullRequest pullRequest, EntityCommitFile file1,
            EntityCommitFile file2, EntityRepositoryCommit commitArq1, EntityRepositoryCommit commitArq2) {
        this.pullRequest = pullRequest;
        this.file1 = file1;
        this.commitFile1 = commitArq1;
        this.file2 = file2;
        this.commitFile2 = commitArq2;
    }

    @Override
    public String toString() {
        return pullRequest.getIdPullRequest() + ";" + file1.getFilename() + ";" + file2.getFilename()
                + ";" + commitFile1.getSha() + ";" + commitFile2.getSha()+";"+
                GeoCodeUtil.getCountry(commitFile1.getAuthor().getLogin(),
                        commitFile1.getAuthor().getLocation());
    }

    public EntityPullRequest getPullRequest() {
        return pullRequest;
    }

    public EntityCommitFile getFile1() {
        return file1;
    }

    public EntityRepositoryCommit getCommitFile1() {
        return commitFile1;
    }

    public EntityCommitFile getFile2() {
        return file2;
    }

    public EntityRepositoryCommit getCommitFile2() {
        return commitFile2;
    }


   

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.file1)+Objects.hashCode(this.file2);
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
        final AuxNewPairOfFiles other = (AuxNewPairOfFiles) obj;
        
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

}
