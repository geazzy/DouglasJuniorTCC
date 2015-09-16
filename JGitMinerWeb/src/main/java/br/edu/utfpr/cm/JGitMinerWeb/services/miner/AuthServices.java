/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.miner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

/**
 *
 * @author douglas
 */
public class AuthServices implements Serializable {

    private static final List<GitHub> clients;
    private static int i;
    private static int rate;
    private static final String APP_NAME;
    private static int clientCount;

    static {
        APP_NAME = "JGitMinerWeb";
        i = 0;
        rate = 0;
        clientCount = 0;
        clients = new ArrayList<GitHub>();
        try {
            prepareAccounts();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static GitHub getGitHubClient() {
        int maior = 0;
        int aux = 0;

        try {
            while (aux < clients.size()) {
                if (clients.get(aux).getRateLimit().remaining
                        > clients.get(maior).getRateLimit().remaining) {

                    maior = aux;
                }
                aux++;
            }

        } catch (IOException ex) {
            Logger.getLogger(AuthServices.class.getName()).log(Level.SEVERE, null, ex);
        }

        return clients.get(maior);
    }

    private static void prepareAccounts() throws FileNotFoundException, IOException, Exception {
        String path = AuthServices.class.getResource("/accounts").getPath();
        File fileAccounts = new File(URLDecoder.decode(path, "ASCII"));
        BufferedReader bf = new BufferedReader(new FileReader(fileAccounts));
        while (bf.ready()) {
            String linha = bf.readLine();
            try {
                String[] login = linha.split("[,]");
                GitHub cl = createClient(login[0], login[1]);
                if (cl != null) {
                    clients.add(cl);
                    clientCount++;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static GitHub createClient(String user, String token) throws Exception {
        GitHubBuilder cliente = new GitHubBuilder();
        cliente.withOAuthToken(token, user);
        GitHub gh = cliente.build();

//        OAuthService oauth = new OAuthService(cliente);
//        Authorization auth = new Authorization();
        try {
//            String token;
//            if (gh.getAuthorizations() == null || oauth.getAuthorizations().isEmpty()) {
//                auth = oauth.createAuthorization(auth);
//                token = auth.getToken();
//                System.out.println("autorizooou: " + token);
//            } else {
//                List<Authorization> auths = oauth.getAuthorizations();
//                System.out.println("autorizaçoes: " + auths.size());
//                System.out.println("autorização: " + auths.get(0));
//                token = auths.get(0).getToken();
//                System.out.println("token: " + token);
//            }
//            return new GitHubClient().setOAuth2Token(token);
            if (gh.isCredentialValid()) {
                return gh;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static int getClientCount() {
        return clientCount;
    }
}
