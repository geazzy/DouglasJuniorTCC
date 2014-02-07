/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary;

/**
 *
 * @author geazzy
 */
public class AuxCoChanged {

    private Integer issueNumber;
    private String urlCommit;
    private String arq1;
    private String arq2;


    public AuxCoChanged(Integer issueNumber, String urlCommit, String arq1, String arq2) {
        this.issueNumber = issueNumber;
        this.urlCommit = urlCommit;
        this.arq1 = arq1;
        this.arq2 = arq2;
    }

    @Override
    public String toString() {
        return issueNumber + ";" + urlCommit + ";" + arq1 + ";" + arq2;
    }
    
    
    
}
