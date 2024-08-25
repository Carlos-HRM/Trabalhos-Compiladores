/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.dc.compiladores.la_t5_compiladores;

import br.dc.compiladores.la_t5_compiladores.AlgumaParser.ProgramaContext;
import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class Principal {
    public static void main(String[] args) throws IOException {
        // Verifica se os argumentos foram passados corretamente
        if (args.length < 2) {
            System.err.println("Usage: java Principal <input file> <output file>");
            return;
        }

        // Lê o arquivo de entrada e cria um CharStream
        CharStream inputStream = CharStreams.fromFileName(args[0]);
        
        // Cria o lexer a partir do CharStream
        AlgumaLexer lexer = new AlgumaLexer(inputStream);
        
        // Cria o token stream a partir do lexer
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        
        // Cria o parser a partir do token stream
        AlgumaParser parser = new AlgumaParser(tokenStream);
        
        // Gera a árvore de análise sintática
        ProgramaContext programTree = parser.programa();

        // Cria o analisador semântico e visita a árvore sintática
        AlgumaSemantico semanticAnalyzer = new AlgumaSemantico();
        semanticAnalyzer.visitPrograma(programTree);

        // Tenta escrever os erros semânticos no arquivo de saída
        try (PrintWriter writer = new PrintWriter(new File(args[1]))) {
            AlgumaSemanticoUtils.errosSemanticos.forEach(writer::println);
            writer.println("Fim da compilacao");
        } catch (IOException e) {
            System.err.printf("Error writing to file: %s%n", e.getMessage());
        } catch (Exception e) {
            System.err.printf("An unexpected error occurred: %s%n", e.getMessage());
        }
        
        if(AlgumaSemanticoUtils.errosSemanticos.isEmpty()) {
                AlgumaGeradorC agc = new AlgumaGeradorC();
                agc.visitPrograma(programTree);
                try(PrintWriter pw = new PrintWriter(args[1])) {
                    pw.print(agc.resultado.toString());
            }
        }
    }
}
