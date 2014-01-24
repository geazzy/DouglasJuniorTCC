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
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class UrlValidate {

        private static final String padrao = "(\\[\\w*\\])?\\(?\\b(http://|https://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";


    private static Boolean isURL(String value) {

            Pattern pattern = Pattern.compile(padrao);
            Matcher matcher = pattern.matcher(value);

            return matcher.matches();

    }

    public static Integer urlInString(String linha) throws NullPointerException{

        Integer quantidadeDeUrl = new Integer(0);

        Scanner tokens = new Scanner(linha);
        String token;

        while (tokens.hasNext()) {
            token = tokens.next();

            if (isURL(token)) {
                try {
                    escrever(token, "/home/geazzy/url.txt");
                } catch (IOException ex) {
                    Logger.getLogger(UrlValidate.class.getName()).log(Level.SEVERE, null, ex);
                }
                quantidadeDeUrl++;
            }
        }

        tokens.close();
        return quantidadeDeUrl;
    }

    private static void escrever(String valor, String file) throws IOException {
        
        Path txt = Paths.get(file);
        
        if(!Files.exists(txt)){
            Files.createFile(txt);
        }
        
        List<String> linhas = Files.readAllLines(txt, Charset.defaultCharset());
        
        linhas.add(valor);
        Files.write(txt, linhas, Charset.defaultCharset());
       

    }
}
