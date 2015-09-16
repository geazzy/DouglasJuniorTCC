/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.miner;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.github.GHRepository;

/**
 *
 * @author zanoni
 */
public class TesteUserServices {

    public static void main(String[] args) {
        try {
            GHRepository repo = AuthServices.getGitHubClient().getRepository("rails/rails");
            
            System.out.println("s-" + UserServices.createEntity(null, null, true));
        } catch (IOException ex) {
            Logger.getLogger(TesteUserServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
