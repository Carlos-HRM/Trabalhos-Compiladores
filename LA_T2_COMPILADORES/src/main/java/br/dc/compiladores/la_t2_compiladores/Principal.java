/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.dc.compiladores.la_t2_compiladores;

import java.io.IOException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileOutputStream;

public class Principal {
    public static void main(String args[]) throws IOException {
        try(PrintWriter pw = new PrintWriter(new FileOutputStream(args[1]))){
            int x = 0;
            // Cria um CharStream a partir do arquivo de entrada usado como argumento
            CharStream cs = CharStreams.fromFileName(args[0]);
            
            // Cria um lexer para analisar o CharStream
            LA_T2_COMPILADORESLexer lexer = new LA_T2_COMPILADORESLexer(cs);
            
            // Cria um CommonTokenStream a partir dos tokens gerados pelo lexer
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            
            // Cria um parser que vai utilizar os tokens para fazer a análise sintática
            LA_T2_COMPILADORESParser parser = new LA_T2_COMPILADORESParser(tokens);
            
            // Remove os listeners de erros padrão do parser
            parser.removeErrorListeners();
            
            // Cria um objeto para relatar os erros encontrados durante a análise e adiciona como listener
            ReportaErros re = new ReportaErros(pw,x);
            
            // Inicia a análise do programa
            parser.addErrorListener(re);
            
            parser.programa(); 
        } catch(IOException ex){
            ex.printStackTrace();
        }
    }
}