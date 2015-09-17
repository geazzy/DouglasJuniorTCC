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
public class AuxNumberInteressados {

    private Long id;
    private Integer issueNumber;
    private String url;
    private Integer quantidadeInteressados;

    public AuxNumberInteressados(Long id, Integer number, String url) {
        this.id = id;
        this.issueNumber = number;
        this.url = url;
        quantidadeInteressados = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(Integer issueNumber) {
        this.issueNumber = issueNumber;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getQuantidadeInteressados() {
        return quantidadeInteressados;
    }

  

    @Override
    public String toString() {
        return issueNumber + ";" + quantidadeInteressados + ";" + url;
    }

    public void setQuantidadeDeInteressados(Integer intValue) {
        this.quantidadeInteressados = intValue;
    }

}
