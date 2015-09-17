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

public class MentionsValidator {

        private static final String padrao = "(^@)(\\S+)";


    private static Boolean isMention(String value) {

            Pattern pattern = Pattern.compile(padrao);
            Matcher matcher = pattern.matcher(value);

            return matcher.matches();

    }

    public static Integer mentionInString(String linha){

        Integer quantidadeDeMentions = new Integer(0);

        Scanner tokens = new Scanner(linha);
        String token;

        while (tokens.hasNext()) {
            token = tokens.next();

            if (isMention(token)) {
//                try {
//                    escrever(token, "/home/geazzy/mentions.txt");
//                } catch (IOException ex) {
//                    Logger.getLogger(UrlValidator.class.getName()).log(Level.SEVERE, null, ex);
//                }
                quantidadeDeMentions++;
            }
        }

        tokens.close();
        return quantidadeDeMentions;
    }

//    private static void escrever(String valor, String file) throws IOException {
//
//        Path txt = Paths.get(file);
//
//        if (!Files.exists(txt)) {
//            Files.createFile(txt);
//        }
//
//        List<String> linhas = Files.readAllLines(txt, Charset.defaultCharset());
//
//        linhas.add(valor);
//        Files.write(txt, linhas, Charset.defaultCharset());
//
//    }
}
