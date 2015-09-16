/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.miner;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;

/**
 *
 * @author zanoni
 */
public class UserServicesTest {

    public UserServicesTest() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void hello() {
        try {
            GHRepository repo = AuthServices.getGitHubClient().getRepository("10logic/rails");
            GHRepository repo2 = AuthServices.getGitHubClient().getRepository("geazzy/Neo4jTest");
           // assertEquals("rails/rails", repo.getFullName());

            assertEquals("masterhou", AuthServices.getGitHubClient().getUser("masterhou").getLogin());

//            System.out.println("s-" + UserServices.getGitCollaboratorsFromRepository(repo));
          //  assertEquals(2, UserServices.getGitCollaboratorsFromRepository(repo2));
            System.out.println("users: "+AuthServices.getGitHubClient().getRepository(repo.getFullName()).listContributors().asList().size());
            for (GHUser col : AuthServices.getGitHubClient().getRepository(repo.getFullName()).listContributors().asList()) {
                System.out.println("u "+col.getLogin());
            }
            assertEquals(1, AuthServices.getGitHubClient().getRepository(repo2.getFullName()).listContributors().asList().size());
        } catch (IOException ex) {
            Logger.getLogger(UserServicesTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UserServicesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
