/*
Autores:
Carlos Henrique Rennó Matos Filho, RA: 793241
Pedro Cassiano Coleone, RA:793249
Pietro Minghini Moralles, RA:792238
 */
package br.ufscar.dc.compiladores.la_t1_compiladores;

import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

public class Principal {

    public static void main(String[] args) {
        try {
            // Verifica se os argumentos da linha de comando foram fornecidos corretamente
            if (args.length < 2) {
                System.err.println("Uso: java Principal <arquivo_entrada> <arquivo_saida>");
                System.exit(1);
            }

            // Extrai os nomes dos arquivos de entrada e saída dos argumentos
            String arquivoEntrada = args[0];
            String arquivoSaida = args[1];

            // Cria um fluxo de caracteres a partir do arquivo de entrada
            CharStream cs = CharStreams.fromFileName(arquivoEntrada);
            LA_T1_COMPILADORES lex = new LA_T1_COMPILADORES(cs);

            Token t = null;
            StringBuilder sb = new StringBuilder();

            // Itera sobre os tokens identificados pelo analisador léxico
            while ((t = lex.nextToken()).getType() != Token.EOF) {
                String identificador_token = LA_T1_COMPILADORES.VOCABULARY.getDisplayName(t.getType());

                // Verifica o tipo do token e executa ação correspondente
                switch (identificador_token) {
                    case "ERRO" : {
                        // Mensagem de erro para símbolo não identificado
                        String msgErro = "Linha " + t.getLine() + ": " + t.getText() + " - símbolo não identificado" + "\n";
                        sb.append(msgErro);
                        byte[] bytes_awnser = sb.toString().getBytes();
                        FileOutputStream fo = new FileOutputStream(arquivoSaida);
                        fo.write(bytes_awnser);
                        return;
                    }
                    case "COMENTARIO_ABERTO" : {
                        // Mensagem de erro para comentário não fechado
                        String msgErro = "Linha " + t.getLine() + ": comentário não fechado" + "\n";
                        sb.append(msgErro);
                        byte[] bytes_awnser = sb.toString().getBytes();
                        FileOutputStream fo = new FileOutputStream(arquivoSaida);
                        fo.write(bytes_awnser);
                        return;
                    }
                    case "CADEIA_ABERTA" : {
                        // Mensagem de erro para cadeia literal não fechada
                        String cadAberta = "Linha " + t.getLine() + ": cadeia literal não fechada" + "\n";
                        sb.append(cadAberta);
                        byte[] bytes_awnser = sb.toString().getBytes();
                        FileOutputStream fo = new FileOutputStream(arquivoSaida);
                        fo.write(bytes_awnser);
                        return;
                    }
                    // Outros tipos de tokens
                    case "NUM_INT" : {
                        // Tokens numéricos inteiros
                        String completeToken = "<" + "'" + t.getText() + "'" + "," + identificador_token + ">" + "\n";
                        sb.append(completeToken);
                    }
                    case "NUM_REAL" : {
                        // Tokens numéricos reais
                        String completeToken = "<" + "'" + t.getText() + "'" + "," + identificador_token + ">" + "\n";
                        sb.append(completeToken);
                    }
                    case "IDENT" : {
                        // Tokens de identificadores
                        String completeToken = "<" + "'" + t.getText() + "'" + "," + identificador_token + ">" + "\n";
                        sb.append(completeToken);
                    }
                    case "CADEIA" : {
                        // Tokens de cadeias literais
                        String completeToken = "<" + "'" + t.getText() + "'" + "," + identificador_token + ">" + "\n";
                        sb.append(completeToken);
                    }
                    default : {
                        // Tokens não especificados anteriormente
                        String completeToken = "<" + "'" + t.getText() + "'" + ",'" + t.getText() + "'>" + "\n";
                        sb.append(completeToken);
                    }
                }

            }
            // Escreve os resultados no arquivo de saída
            byte[] bytes_awnser = sb.toString().getBytes();
            FileOutputStream fo = new FileOutputStream(arquivoSaida);
            fo.write(bytes_awnser);

        } catch (IOException ex) {
            // Manipula exceção de E/S
            ex.printStackTrace();
        }
    }
}