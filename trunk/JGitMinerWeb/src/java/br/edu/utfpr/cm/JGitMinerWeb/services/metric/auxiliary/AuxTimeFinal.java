/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary;

import br.edu.utfpr.cm.JGitMinerWeb.services.matriz.auxiliary.AuxPairOfFiles;

/**
 *
 * @author geazzy
 */
public class AuxTimeFinal {

    AuxPairOfFiles auxCoChanged;
    AuxCountCoChange auxCountCoChange;

    public AuxTimeFinal(AuxPairOfFiles auxCoChanged, AuxCountCoChange auxCountCoChange) {
        this.auxCoChanged = auxCoChanged;
        this.auxCountCoChange = auxCountCoChange;
    }

    @Override
    public String toString() {
        return  auxCoChanged + ";" + auxCountCoChange;
    }
    
    
    
}
