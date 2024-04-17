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
            CharStream cs = CharStreams.fromFileName(args[0]);
            LA_T2_COMPILADORESLexer lexer = new LA_T2_COMPILADORESLexer(cs);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            LA_T2_COMPILADORESParser parser = new LA_T2_COMPILADORESParser(tokens);
            parser.removeErrorListeners();
            
            ReportaErros re = new ReportaErros(pw,x);
            parser.addErrorListener(re);
            
            parser.programa(); 
        } catch(IOException ex){
            ex.printStackTrace();
        }
    }
}