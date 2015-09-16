/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxPairOfFiles;

/**
 *
 * @author geazzy
 */
public class AuxTeste {

    private AuxPairOfFiles coChanged;
    private Integer count;

    public AuxTeste(AuxPairOfFiles coChanged, Integer count) {
        this.coChanged = coChanged;
        this.count = count;
    }

//    public void countOneMore() {
//        count++;
//    }

    @Override
    public String toString() {
        return coChanged.getFile1()+";"+coChanged.getFile2()+";" + count+";";
    }

}
