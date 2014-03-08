/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary;

import java.util.Objects;

/**
 *
 * @author geazzy
 */
public class AuxCoChanged {

    private Integer issueNumber;
    private String arq1;
    private String shaCommitFile1;
    private String arq2;
    private String shaCommitFile2;


    public AuxCoChanged(Integer issueNumber, String arq1, String arq2, String shaCommitArq1, String shaCommitArq2) {
        this.issueNumber = issueNumber;
        this.arq1 = arq1;
        this.shaCommitFile1 = shaCommitArq1;
        this.arq2 = arq2;
        this.shaCommitFile2 = shaCommitArq2;
    }

    @Override
    public String toString() {
        return issueNumber + ";" + arq1 + ";" + arq2 + ";" + shaCommitFile1 + ";" + shaCommitFile2;
    }

    public Integer getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(Integer issueNumber) {
        this.issueNumber = issueNumber;
    }

    public String getArq1() {
        return arq1;
    }

    public void setArq1(String arq1) {
        this.arq1 = arq1;
    }

    public String getshaCommitFile1() {
        return shaCommitFile1;
    }

    public void setshaCommitFile1(String shaCommitFile1) {
        this.shaCommitFile1 = shaCommitFile1;
    }

    public String getArq2() {
        return arq2;
    }

    public void setArq2(String arq2) {
        this.arq2 = arq2;
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
        hash = 67 * hash + Objects.hashCode(this.arq1);
        hash = 67 * hash + Objects.hashCode(this.arq2);
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
        final AuxCoChanged other = (AuxCoChanged) obj;
        
        if (Objects.equals(this.arq1, other.arq1)) {

            if (Objects.equals(this.arq2, other.arq2)) {
                return true;
            }
        }
        if (Objects.equals(this.arq1, other.arq2)) {

            if (Objects.equals(this.arq2, other.arq1)) {
                return true;
            }
        }

        return false;
    }

}
