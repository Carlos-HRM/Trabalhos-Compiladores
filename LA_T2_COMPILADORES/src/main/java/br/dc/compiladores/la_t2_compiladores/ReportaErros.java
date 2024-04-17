/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.dc.compiladores.la_t2_compiladores;

import org.antlr.v4.runtime.ANTLRErrorListener; // cuidado para importar a versão 4
import org.antlr.v4.runtime.Token; // Vamos também precisar de Token
import java.io.PrintWriter;
import java.util.BitSet;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
// Outros imports vão ser necessários aqui. O NetBeans ou IntelliJ fazem isso automaticamente

public class ReportaErros implements ANTLRErrorListener {
    PrintWriter pw;
    int qtd_Erros;
    
    public ReportaErros(PrintWriter pw, int qtd_Erros){
        this.pw = pw;
        this.qtd_Erros = qtd_Erros;
    }
    
    @Override
    public void	reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
        // Não será necessário para o T2, pode deixar vazio
    }
    
    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
        // Não será necessário para o T2, pode deixar vazio
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
        // Não será necessário para o T2, pode deixar vazio
    }

    @Override
    public void	syntaxError(Recognizer<?,?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        // Aqui vamos colocar o tratamento de erro customizado

        Token t = (Token) offendingSymbol;
        
        if(qtd_Erros == 0){
            if("<EOF>".equals(t.getText())){
                pw.println("Linha "+line+": erro sintatico proximo a EOF");
                qtd_Erros = 1;
            }
            else if (t.getType() == LA_T2_COMPILADORESLexer.CADEIA_ABERTA){
                pw.println("Linha "+line+": cadeia literal nao fechada");
                qtd_Erros = 1;
            }
            else if (t.getType() == LA_T2_COMPILADORESLexer.COMENTARIO_ABERTO){
                pw.println("Linha "+line+": comentario nao fechado");
                qtd_Erros = 1;
            }
            else if (t.getType() == LA_T2_COMPILADORESLexer.ERRO){
                pw.println("Linha "+line+": " + t.getText()+ " - simbolo nao identificado");
                qtd_Erros = 1;
            }
            else {
                pw.println("Linha "+line+": erro sintatico proximo a "+t.getText());
                qtd_Erros = 1;
            }
            pw.println("Fim da compilacao");
        }
    }
}