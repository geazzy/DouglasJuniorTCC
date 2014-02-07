/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.pojo.miner.EntityCommitFile;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author geazzy
 */
public class AuxCoChangedFiles {

    private Integer issueNumber;
    private String urlCommit;
    private Set<EntityCommitFile> arquivos;

    public AuxCoChangedFiles() {
        this.issueNumber = 0;
        this.urlCommit = "";
        this.arquivos = new HashSet<EntityCommitFile>();
    }

    public AuxCoChangedFiles(Integer issueNumber, String url) {
        this.issueNumber = issueNumber;
        this.urlCommit = url;
        this.arquivos = new HashSet<EntityCommitFile>();
    }

    public AuxCoChangedFiles(Integer issueNumber, String url, Set<EntityCommitFile> arquivos) {
        this.issueNumber = issueNumber;
        this.urlCommit = url;
        this.arquivos = arquivos;
    }

    public Integer getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(Integer issueNumber) {
        this.issueNumber = issueNumber;
    }

    public String getUrlCommit() {
        return urlCommit;
    }

    public void setUrlCommit(String urlCommit) {
        this.urlCommit = urlCommit;
    }

    @Override
    public String toString() {
        String stringArquivos ="";

        for (EntityCommitFile arq : arquivos) {
            if (Strings.isNullOrEmpty(stringArquivos)) {
                stringArquivos = arq.getFilename();
            } else {
                stringArquivos = stringArquivos + ";" + arq.getFilename();
            }

        }

        return issueNumber + ";" + urlCommit + ";" + stringArquivos;
    }

    public Set<EntityCommitFile> getArquivos() {
        return arquivos;
    }

//    public void setArquivos(Set<EntityCommitFile> arquivos) {
//        this.arquivos = arquivos;
//    }

}
