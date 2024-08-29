/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.dc.compiladores.la_t5_compiladores;

import br.dc.compiladores.la_t5_compiladores.TabelaDeSimbolos.EntradaTabelaDeSimbolos;
import br.dc.compiladores.la_t5_compiladores.AlgumaParser.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.antlr.v4.runtime.tree.TerminalNode;
import java.util.Arrays;


public class AlgumaGeradorC extends AlgumaBaseVisitor {
    StringBuilder resultado;
    TabelaDeSimbolos tabela;
    
    public AlgumaGeradorC(){
        resultado = new StringBuilder();
        this.tabela = new TabelaDeSimbolos();
    }
    
    @Override
    public Void visitPrograma(AlgumaParser.ProgramaContext ctx){
        resultado.append("#include <stdio.h>\n");
        resultado.append("#include <stdlib.h>\n\n");
        
        ctx.declaracoes().decl_local_global().forEach(dec -> visitDecl_local_global(dec));
        
        resultado.append("int main() {\n");
        
        visitCorpo(ctx.corpo());
        
        resultado.append("return 0;\n}\n");
        
        return null;
    }
    
    @Override
    public Void visitDecl_local_global(Decl_local_globalContext ctx) {
        // Verifica se há uma declaração local e a visita, caso contrário, verifica e visita a global.
        if (ctx.declaracao_local() != null) {
            visitDeclaracao_local(ctx.declaracao_local());
        } else if (ctx.declaracao_global() != null) {
            visitDeclaracao_global(ctx.declaracao_global());
        }
        // Retorna null, pois o método não tem um retorno específico.
        return null;
    }
    
    @Override
    public Void visitCorpo(CorpoContext ctx) {
        for (AlgumaParser.Declaracao_localContext dec : ctx.declaracao_local()) {
            visitDeclaracao_local(dec);
        }

        for (AlgumaParser.CmdContext com : ctx.cmd()) {
            visitCmd(com);
        }

        return null;
    }
    
    @Override
    public Void visitDeclaracao_global(Declaracao_globalContext ctx) {
        String identificador = ctx.IDENT().getText();

        if (ctx.getText().contains("procedimento")) {
            // Caso seja um procedimento, insere a assinatura com 'void'.
            resultado.append("void ").append(identificador).append("(");
        } else {
            // Caso seja uma função, determina o tipo e insere a assinatura correspondente.
            String cTipo = AlgumaSemanticoUtils.obterTipoC(ctx.tipo_estendido().getText().replace("^", ""));
            TabelaDeSimbolos.TipoAlguma tipo = AlgumaSemanticoUtils.getTipoAlguma(ctx.tipo_estendido().getText());
            // Processa o tipo estendido e adiciona a assinatura.
            visitTipo_estendido(ctx.tipo_estendido());
            resultado.append(" ").append(identificador).append("(");
            // Se o tipo for 'char', ajusta o tamanho do parâmetro.
            if ("char".equals(cTipo)) {
                resultado.append("[80]");
            }
            // Adiciona a função à tabela de símbolos.
            tabela.adicionar(identificador, tipo, TabelaDeSimbolos.Structure.FUNC);
        }
        // Visita todos os parâmetros da função.
        ctx.parametros().parametro().forEach(var -> visitParametro(var));
        // Abre o bloco de código da função.
        resultado.append("){\n");
        // Visita todas as declarações locais da função.
        ctx.declaracao_local().forEach(var -> visitDeclaracao_local(var));
        // Visita todos os comandos da função.
        ctx.cmd().forEach(var -> visitCmd(var));
        // Fecha o bloco de código da função.
        resultado.append("}\n");
        return null;
    }
    
    @Override
    public Void visitIdentificador(IdentificadorContext ctx) {
        // Adiciona um espaço à saída.
        resultado.append(" ");
        // Variável para controlar o número de identificadores no nó.
        int i = 0;
        // Percorre todos os identificadores presentes no contexto.
        for (TerminalNode id : ctx.IDENT()) {
            // Se não for o primeiro identificador, adiciona um ponto antes do
            // identificador.
            if (i++ > 0)
                resultado.append(".");
            // Adiciona o identificador à saída.
            resultado.append(id.getText());
        }
        // Visita a dimensão dos identificadores, caso existam.
        visitDimensao(ctx.dimensao());

        return null;
    }

    @Override
    public Void visitDimensao(DimensaoContext ctx) {
        // Percorre todas as expressões aritméticas presentes no contexto.
        for (Exp_aritmeticaContext exp : ctx.exp_aritmetica()) {
            // Adiciona um colchete de abertura à saída.
            resultado.append("[");
            // Visita a expressão aritmética para processá-la e adicioná-la à saída.
            visitExp_aritmetica(exp);
            // Adiciona um colchete de fechamento à saída.
            resultado.append("]");
        }

        return null;
    }
    
    @Override
    public Void visitParametro(ParametroContext ctx) {
        // Variável para controlar o número de parâmetros no contexto.
        int i = 0;
        // Obtém o tipo C correspondente ao tipo estendido dos parâmetros.
        String cTipo = AlgumaSemanticoUtils.obterTipoC(ctx.tipo_estendido().getText().replace("^", ""));
        // Obtém o tipo Alguma dos parâmetros.
        TabelaDeSimbolos.TipoAlguma tipo = AlgumaSemanticoUtils.getTipoAlguma(ctx.tipo_estendido().getText());
        // Percorre todos os identificadores presentes no contexto.
        for (IdentificadorContext id : ctx.identificador()) {
            // Se não for o primeiro parâmetro, adiciona uma vírgula antes do identificador.
            if (i++ > 0)
                resultado.append(",");
            // Visita o tipo estendido dos parâmetros para processá-lo e adicioná-lo à
            // saída.
            visitTipo_estendido(ctx.tipo_estendido());
            // Visita o identificador dos parâmetros para processá-lo e adicioná-lo à saída.
            visitIdentificador(id);
            // Se o tipo for char, adiciona o tamanho [80] ao parâmetro.
            if (cTipo.equals("char")) {
                resultado.append("[80]");
            }
            // Adiciona o identificador e o tipo à tabela de símbolos como variável.
            tabela.adicionar(id.getText(), tipo, TabelaDeSimbolos.Structure.VAR);
        }

        return null;
    }

    @Override
    public Void visitDeclaracao_local(Declaracao_localContext ctx) {
        // Verifica se a declaração local é uma declaração de variável.
        if (ctx.declaracao_var() != null) {
            // Visita a declaração de variável para processá-la.
            visitDeclaracao_var(ctx.declaracao_var());
        }
        // Verifica se a declaração local é uma declaração de constante.
        if (ctx.declaracao_const() != null) {
            // Visita a declaração de constante para processá-la.
            visitDeclaracao_const(ctx.declaracao_const());
        } else if (ctx.declaracao_tipo() != null) {
            // Caso contrário, verifica se é uma declaração de tipo e visita-a para
            // processá-la.
            visitDeclaracao_tipo(ctx.declaracao_tipo());
        }
        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitDeclaracao_tipo(Declaracao_tipoContext ctx) {
        // Adiciona a palavra-chave 'typedef' à saída.
        resultado.append("typedef ");
        // Obtém o tipo C correspondente ao tipo da declaração de tipo.
        String cTipo = AlgumaSemanticoUtils.obterTipoC(ctx.tipo().getText().replace("^", ""));
        // Obtém o tipo Alguma da declaração de tipo.
        TabelaDeSimbolos.TipoAlguma tipo = AlgumaSemanticoUtils.getTipoAlguma(ctx.tipo().getText());

        // Verifica se a declaração de tipo é uma declaração de registro.
        if (ctx.tipo().getText().contains("registro")) {
            // Para cada subvariável do registro, adiciona os identificadores e seus tipos
            // na tabela de símbolos.
            for (VariavelContext sub : ctx.tipo().registro().variavel()) {
                for (IdentificadorContext idIns : sub.identificador()) {
                    TabelaDeSimbolos.TipoAlguma tipoIns = AlgumaSemanticoUtils.getTipoAlguma(sub.tipo().getText());
                    tabela.adicionar(ctx.IDENT().getText() + "." + idIns.getText(), tipoIns,
                            TabelaDeSimbolos.Structure.VAR);
                    tabela.adicionar(ctx.IDENT().getText(), tabela.new EntradaTabelaDeSimbolos(idIns.getText(), tipoIns,
                            TabelaDeSimbolos.Structure.TIPO));
                }
            }
        }
        // Adiciona o tipo declarado e o nome do tipo à tabela de símbolos como
        // variável.
        tabela.adicionar(ctx.IDENT().getText(), tipo, TabelaDeSimbolos.Structure.VAR);
        // Visita o tipo da declaração para processá-lo.
        visitTipo(ctx.tipo());
        // Adiciona o nome do tipo e um ponto-e-vírgula à saída.
        resultado.append(ctx.IDENT() + ";\n");
        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitDeclaracao_const(Declaracao_constContext ctx) {
        // Obtém o tipo C correspondente ao tipo básico da constante e o tipo Alguma do
        // contexto.
        System.out.println(ctx.tipo_basico().getText());
        String type = AlgumaSemanticoUtils.obterTipoC(ctx.tipo_basico().getText());
        TabelaDeSimbolos.TipoAlguma typeVar = AlgumaSemanticoUtils.getTipoAlguma(ctx.tipo_basico().getText());
        // Adiciona a constante à tabela de símbolos com o tipo Alguma e a estrutura de
        // variável.
        tabela.adicionar(ctx.IDENT().getText(), typeVar, TabelaDeSimbolos.Structure.VAR);
        // Adiciona a declaração constante com a palavra-chave 'const' seguida do tipo C
        // e do identificador à saída.
        resultado.append("const " + type + " " + ctx.IDENT().getText() + " = ");
        // Visita o valor constante para processá-lo.
        visitValor_constante(ctx.valor_constante());
        // Adiciona o ponto-e-vírgula ao final da declaração de constante.
        resultado.append(";\n");
        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
   
    
    @Override
    public Void visitDeclaracao_var(Declaracao_varContext ctx) {
        visitVariavel(ctx.variavel());
        // Método não possui retorno explícito, retorna null
        return null;
    }
    
    @Override
    public Void visitVariavel(VariavelContext ctx) {
        // Obtém o tipo C correspondente ao tipo da variável.
        String cTipo = AlgumaSemanticoUtils.obterTipoC(ctx.tipo().getText().replace("^", ""));
        // Obtém o tipo Alguma da variável.
        TabelaDeSimbolos.TipoAlguma tipo = AlgumaSemanticoUtils.getTipoAlguma(ctx.tipo().getText());
        // Percorre todos os identificadores presentes no contexto.
        for (AlgumaParser.IdentificadorContext id : ctx.identificador()) {
            // Verifica se a variável é uma subvariável de um registro.
            if (ctx.tipo().getText().contains("registro")) {
                // Para cada subvariável do registro, adiciona os identificadores e seus tipos
                // na tabela de símbolos.
                for (VariavelContext sub : ctx.tipo().registro().variavel()) {
                    for (IdentificadorContext idIns : sub.identificador()) {
                        TabelaDeSimbolos.TipoAlguma tipoIns = AlgumaSemanticoUtils.getTipoAlguma(sub.tipo().getText());
                        tabela.adicionar(id.getText() + "." + idIns.getText(), tipoIns, TabelaDeSimbolos.Structure.VAR);
                    }
                }
            }
            // Verifica se a variável é um tipo definido pelo usuário e se sim, adiciona
            // seus membros à tabela de símbolos.
            else if (cTipo == null && tipo == null) {
                ArrayList<EntradaTabelaDeSimbolos> arg = tabela.retornaTipo(ctx.tipo().getText());
                if (arg != null) {
                    for (TabelaDeSimbolos.EntradaTabelaDeSimbolos val : arg) {
                        tabela.adicionar(id.getText() + "." + val.nome, val.tipo, TabelaDeSimbolos.Structure.VAR);
                    }
                }
            }
            // Verifica se a variável é um vetor e, se sim, adiciona suas posições à tabela
            // de símbolos.
            if (id.getText().contains("[")) {
                int ini = id.getText().indexOf("[", 0);
                int end = id.getText().indexOf("]", 0);
                String tam;
                if (end - ini == 2)
                    tam = String.valueOf(id.getText().charAt(ini + 1));
                else
                    tam = id.getText().substring(ini + 1, end - 1);
                String nome = id.IDENT().get(0).getText();
                for (int i = 0; i < Integer.parseInt(tam); i++) {
                    tabela.adicionar(nome + "[" + i + "]", tipo, TabelaDeSimbolos.Structure.VAR);
                }

            }
            // Caso contrário, adiciona a variável normalmente à tabela de símbolos.
            else {
                tabela.adicionar(id.getText(), tipo, TabelaDeSimbolos.Structure.VAR);
            }
            // Visita o tipo da variável para processá-lo.
            visitTipo(ctx.tipo());
            // Visita o identificador da variável para processá-lo.
            visitIdentificador(id);
            // Se o tipo for char, adiciona o tamanho [80] à variável na saída.
            if (cTipo == "char") {
                resultado.append("[80]");
            }
            // Adiciona um ponto-e-vírgula ao final da declaração da variável na saída.
            resultado.append(";\n");
        }
        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitTipo(TipoContext ctx) {
        // Obtém o tipo C correspondente ao tipo da declaração e o tipo Alguma do
        // contexto.
        String cTipo = AlgumaSemanticoUtils.obterTipoC(ctx.getText().replace("^", ""));
        TabelaDeSimbolos.TipoAlguma tipo = AlgumaSemanticoUtils.getTipoAlguma(ctx.getText());
        // Verifica se o tipo é um ponteiro.
        boolean pointer = ctx.getText().contains("^");
        // Se o tipo for um tipo primitivo, adiciona o tipo C correspondente à saída.
        if (cTipo != null) {
            resultado.append(cTipo);
        }
        // Se o tipo for um registro, visita o nó de registro para processá-lo.
        else if (ctx.registro() != null) {
            visitRegistro(ctx.registro());
        }
        // Caso contrário, visita o tipo estendido para processá-lo.
        else {
            visitTipo_estendido(ctx.tipo_estendido());
        }
        // Se o tipo for um ponteiro, adiciona o asterisco (*) à saída.
        if (pointer)
            resultado.append("*");
        // Adiciona um espaço em branco à saída.
        resultado.append(" ");
        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitTipo_estendido(Tipo_estendidoContext ctx) {
        // Processa o tipo básico ou identificador.
        visitTipo_basico_ident(ctx.tipo_basico_ident());
        // Adiciona o asterisco (*) se o tipo for um ponteiro.
        if (ctx.getText().contains("^")) {
            resultado.append("*");
        }
        // Não retorna valor, portanto retorna 'null'.
        return null;
    }

    @Override
    public Void visitTipo_basico_ident(Tipo_basico_identContext ctx) {
        // Adiciona o texto do identificador à saída, se presente.
        if (ctx.IDENT() != null) {
            resultado.append(ctx.IDENT().getText());
        } else {
            // Caso contrário, adiciona o tipo C correspondente ao tipo básico.
            String tipoC = AlgumaSemanticoUtils.obterTipoC(ctx.getText().replace("^", ""));
            resultado.append(tipoC);
        }

        // Não há valor de retorno, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitRegistro(RegistroContext ctx) {
        // Adiciona a palavra-chave 'struct' seguida de uma quebra de linha à saída.
        resultado.append("struct {\n");
        // Visita cada variável do registro para processá-las.
        ctx.variavel().forEach(var -> visitVariavel(var));
        // Adiciona a chave de fechamento do registro seguida de um espaço em branco à
        // saída.
        resultado.append("} ");
        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitValor_constante(Valor_constanteContext ctx) {
        // Verifica o valor constante do contexto e adiciona o valor correspondente à
        // saída.
        if (ctx.getText().equals("verdadeiro")) {
            resultado.append("true");
        } else if (ctx.getText().equals("falso")) {
            resultado.append("false");
        } else {
            resultado.append(ctx.getText());
        }
        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    private void processarComando(CmdContext ctx) {
        if (ctx.cmdLeia() != null) {
            visitCmdLeia(ctx.cmdLeia());
        } else if (ctx.cmdEscreva() != null) {
            visitCmdEscreva(ctx.cmdEscreva());
        } else if (ctx.cmdAtribuicao() != null) {
            visitCmdAtribuicao(ctx.cmdAtribuicao());
        } else if (ctx.cmdSe() != null) {
            visitCmdSe(ctx.cmdSe());
        } else if (ctx.cmdCaso() != null) {
            visitCmdCaso(ctx.cmdCaso());
        } else if (ctx.cmdPara() != null) {
            visitCmdPara(ctx.cmdPara());
        } else if (ctx.cmdEnquanto() != null) {
            visitCmdEnquanto(ctx.cmdEnquanto());
        } else if (ctx.cmdFaca() != null) {
            visitCmdFaca(ctx.cmdFaca());
        } else if (ctx.cmdChamada() != null) {
            visitCmdChamada(ctx.cmdChamada());
        } else if (ctx.cmdRetorne() != null) {
            visitCmdRetorne(ctx.cmdRetorne());
        }
    }
    
    @Override
    public Void visitCmd(CmdContext ctx) {
        // Determina o tipo de comando e processa o comando correspondente.
        processarComando(ctx);
        return null;
    }

    @Override
    public Void visitCmdRetorne(CmdRetorneContext ctx) {
        // Adiciona a palavra-chave 'return' à saída seguida da expressão a ser
        // retornada.
        resultado.append("return ");
        // Visita a expressão para processá-la.
        visitExpressao(ctx.expressao());
        // Adiciona o ponto-e-vírgula ao final do comando 'retorne'.
        resultado.append(";\n");
        // Não possui retorno explícito, retorna 'null'.
        return null;
    }

    
    @Override
    public Void visitCmdChamada(CmdChamadaContext ctx) {
        // Adiciona o nome da chamada de função à saída.
        resultado.append(ctx.IDENT().getText()).append("(");
        int i = 0;
        // Percorre as expressões de argumentos da chamada de função.
        for (ExpressaoContext exp : ctx.expressao()) {
            if (i++ > 0)
                resultado.append(",");
            // Visita a expressão e adiciona à saída.
            visitExpressao(exp);
        }
        // Adiciona o fechamento da chamada de função e ponto e vírgula à saída.
        resultado.append(");\n");
        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitCmdLeia(CmdLeiaContext ctx) {
        // Percorre cada identificador da lista de identificadores para leitura.
        for (AlgumaParser.IdentificadorContext id : ctx.identificador()) {
            // Verifica o tipo do identificador na tabela de símbolos.
            TabelaDeSimbolos.TipoAlguma idType = tabela.verificar(id.getText());
            // Verifica se o tipo é diferente de CADEIA, para usar 'scanf'.
            if (idType != TabelaDeSimbolos.TipoAlguma.CADEIA) {
                // Adiciona a string de formatação para 'scanf' na saída.
                resultado.append("scanf(\"%").append(AlgumaSemanticoUtils.obterSimboloC(idType)).append("\", &");
                // Adiciona o nome do identificador e ponto e vírgula à saída.
                resultado.append(id.getText()).append(");\n");
            } else {
                // Se o tipo for CADEIA, usa 'gets' para leitura.
                resultado.append("gets(");
                // Visita o identificador para adicionar à saída.
                visitIdentificador(id);
                resultado.append(");\n");
            }
        }
        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitCmdAtribuicao(CmdAtribuicaoContext ctx) {
        // Verifica se a atribuição é para um ponteiro e adiciona o símbolo '*' à saída.
        if (ctx.getText().contains("^"))
            resultado.append("*");
        try {
            // Obtém o tipo da variável identificada pela atribuição.
            TabelaDeSimbolos.TipoAlguma tip = tabela.verificar(ctx.identificador().getText());
            // Se o tipo for CADEIA, trata como uma atribuição de cadeia de caracteres
            // (strcpy).
            if (tip != null && tip == TabelaDeSimbolos.TipoAlguma.CADEIA) {
                // Adiciona a chamada a 'strcpy' na saída.
                resultado.append("strcpy(");
                // Adiciona o identificador da variável e a expressão a ser atribuída na saída.
                visitIdentificador(ctx.identificador());
                resultado.append(",").append(ctx.expressao().getText()).append(");\n");
            } else {
                // Caso contrário, é uma atribuição normal.
                // Adiciona o identificador da variável na saída.
                visitIdentificador(ctx.identificador());
                // Adiciona o sinal de igual e a expressão a ser atribuída na saída.
                resultado.append(" = ").append(ctx.expressao().getText()).append(";\n");
            }
        } catch (Exception e) {
            // Em caso de exceção, imprime a mensagem de erro.
            System.out.println(e.getMessage());
        }
        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitCmdSe(CmdSeContext ctx) {
       
        
        resultado.append("if ("); 
        visitExpressao(ctx.expressao()); 
        resultado.append(") {\n");

        for (CmdContext cmdCtx: ctx.cmdIf)
        {
            visitCmd(cmdCtx);
        }

        resultado.append("}\n");

        if (ctx.cmdElse != null){
            resultado.append("else {\n");

            for (CmdContext cmdCtx : ctx.cmdElse)
            {
                visitCmd(cmdCtx);
            }

            resultado.append("}\n");
        }

        return null;
    }


    @Override
    public Void visitCmdEscreva(CmdEscrevaContext ctx) {
        // Percorre cada expressão a ser escrita.
        for (AlgumaParser.ExpressaoContext exp : ctx.expressao()) {
            // Cria um novo escopo para verificar o tipo da expressão.
            Escopos escopo = new Escopos(tabela);
            // Obtém o símbolo do tipo da expressão em formato de caractere.
            String cType = AlgumaSemanticoUtils.obterSimboloC(AlgumaSemanticoUtils.analisarExpressao(escopo, exp));
            // Se a expressão já existe na tabela de símbolos, verifica seu tipo.
            if (tabela.existe(exp.getText())) {
                TabelaDeSimbolos.TipoAlguma tip = tabela.verificar(exp.getText());
                cType = AlgumaSemanticoUtils.obterSimboloC(tip);
            }
            // Adiciona a string de formatação para 'printf' na saída.
            resultado.append("printf(\"%").append(cType).append("\", ");
            // Adiciona a expressão a ser escrita na saída.
            resultado.append(exp.getText());
            resultado.append(");\n");
        }
        // Não possui retorno explícito, retorna 'null'.
        return null;
    }


    
    @Override
    public Void visitExpressao(ExpressaoContext ctx) {
        // Verifica se a expressão possui termos lógicos.
        if (ctx.termo_logico() != null) {
            // Adiciona o primeiro termo lógico na saída.
            visitTermo_logico(ctx.termo_logico(0));

            // Percorre os termos lógicos restantes e os adiciona à saída com o operador
            // '||' (ou).
            for (int i = 1; i < ctx.termo_logico().size(); i++) {
                AlgumaParser.Termo_logicoContext termo = ctx.termo_logico(i);
                resultado.append(" || ");
                visitTermo_logico(termo);
            }
        }
        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitTermo_logico(Termo_logicoContext ctx) {
        // Adiciona o primeiro fator lógico na saída.
        visitFator_logico(ctx.fator_logico(0));

        // Percorre os fatores lógicos restantes e os adiciona à saída com o operador
        // '&&' (e).
        for (int i = 1; i < ctx.fator_logico().size(); i++) {
            AlgumaParser.Fator_logicoContext fator = ctx.fator_logico(i);
            resultado.append(" && ");
            visitFator_logico(fator);
        }

        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitFator_logico(Fator_logicoContext ctx) {
        // Verifica se o fator lógico possui o operador 'nao' (não) e adiciona '!' na
        // saída.
        if (ctx.getText().startsWith("nao")) {
            resultado.append("!");
        }
        // Visita a parcela lógica do fator lógico.
        visitParcela_logica(ctx.parcela_logica());

        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    
    @Override
    public Void visitParcela_logica(Parcela_logicaContext ctx) {
        // Verifica se a parcela lógica possui uma expressão relacional.
        if (ctx.exp_relacional() != null) {
            // Visita a expressão relacional e a adiciona à saída.
            visitExp_relacional(ctx.exp_relacional());
        } else {
            // Caso não haja expressão relacional, verifica se a parcela é 'verdadeiro' ou
            // 'falso'.
            if (ctx.getText().equals("verdadeiro")) {
                resultado.append("true");
            } else {
                resultado.append("false");
            }
        }

        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitExp_relacional(Exp_relacionalContext ctx) {
        // Adiciona a primeira expressão aritmética na saída.
        visitExp_aritmetica(ctx.exp_aritmetica(0));

        // Percorre as demais expressões aritméticas e operadores relacionais e os
        // adiciona à saída.
        for (int i = 1; i < ctx.exp_aritmetica().size(); i++) {
            AlgumaParser.Exp_aritmeticaContext termo = ctx.exp_aritmetica(i);
            if (ctx.op_relacional().getText().equals("=")) {
                resultado.append(" == ");
            } else {
                resultado.append(ctx.op_relacional().getText());
            }
            visitExp_aritmetica(termo);
        }

        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
//    @Override
//    public Void visitExp_aritmetica(Exp_aritmeticaContext ctx) {
//        // Adiciona o primeiro termo à saída.
//        visitTermo(ctx.termo(0));
//
//        // Verifica se há operadores aritméticos e adiciona os demais termos com esses operadores à saída.
//        if (ctx.op1() != null) {
//            for (int i = 0; i < ctx.op1().size(); i++) {
//                // Adiciona o operador aritmético à saída.
//                resultado.append(ctx.op1(i).getText());
//                // Adiciona o próximo termo à saída.
//                visitTermo(ctx.termo(i + 1));
//            }
//        }
//
//        // Não possui retorno explícito, retorna 'null'.
//        return null;
//    }
    
    @Override
    public Void visitExp_aritmetica(Exp_aritmeticaContext ctx) {
        // Adiciona o primeiro termo na saída.
        visitTermo(ctx.termo(0));

        // Percorre os demais termos e operadores aritméticos e os adiciona à saída.
        for (int i = 1; i < ctx.termo().size(); i++) {
            AlgumaParser.TermoContext termo = ctx.termo(i);
            resultado.append(ctx.op1(i - 1).getText());
            visitTermo(termo);
        }

        // Não possui retorno explícito, retorna 'null'.
        return null;
    }

    @Override
    public Void visitTermo(TermoContext ctx) {
        // Adiciona o primeiro fator na saída.
        visitFator(ctx.fator(0));

        // Percorre os demais fatores e operadores '*' ou '/' e os adiciona à saída.
        for (int i = 1; i < ctx.fator().size(); i++) {
            AlgumaParser.FatorContext fator = ctx.fator(i);
            resultado.append(ctx.op2(i - 1).getText());
            visitFator(fator);
        }

        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitFator(FatorContext ctx) {
        // Adiciona a primeira parcela na saída.
        visitParcela(ctx.parcela(0));

        // Percorre as demais parcelas e operadores '+' ou '-' e os adiciona à saída.
        for (int i = 1; i < ctx.parcela().size(); i++) {
            AlgumaParser.ParcelaContext parcela = ctx.parcela(i);
            resultado.append(ctx.op3(i - 1).getText());
            visitParcela(parcela);
        }

        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitParcela(ParcelaContext ctx) {
        if (ctx.parcela_unario() != null) {
            if (ctx.op_unario() != null) {
                resultado.append(ctx.op_unario().getText());
            }
            visitParcela_unario(ctx.parcela_unario());
        } else {
            visitParcela_nao_unario(ctx.parcela_nao_unario());
        }

        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitParcela_unario(Parcela_unarioContext ctx) {

        if (ctx.IDENT() != null) {
            resultado.append(ctx.IDENT().getText());
            resultado.append("(");
            for (int i = 0; i < ctx.expressao().size(); i++) {
                visitExpressao(ctx.expressao(i));
                if (i < ctx.expressao().size() - 1) {
                    resultado.append(", ");
                }
            }
            resultado.append(")");
        } else if (ctx.parentesis_expressao() != null) {
            resultado.append("(");
            visitExpressao(ctx.parentesis_expressao().expressao());
            resultado.append(")");
        } else {
            resultado.append(ctx.getText());
        }

        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitParcela_nao_unario(Parcela_nao_unarioContext ctx) {
        resultado.append(ctx.getText());
        return null;
    }
    
    @Override
    public Void visitCmdCaso(CmdCasoContext ctx) {
        // Iniciar o switch com a expressão aritmética
        resultado.append("switch (");
        resultado.append(ctx.exp_aritmetica().getText());
        resultado.append(") {\n");

        // Visitar cada item de seleção
        for (Item_selecaoContext itemCtx : ctx.selecao().item_selecao()) {
            for (Numero_intervaloContext intervaloCtx : itemCtx.constantes().numero_intervalo()) {
                // Se o intervalo for um único número
                if (intervaloCtx.NUM_INT().size() == 1) {
                    resultado.append("case ");
                    resultado.append(intervaloCtx.NUM_INT(0).getText());
                    resultado.append(":\n");
                }
                // Se o intervalo for um intervalo de números
                else {
                    int inicio = Integer.parseInt(intervaloCtx.NUM_INT(0).getText());
                    int fim = Integer.parseInt(intervaloCtx.NUM_INT(1).getText());
                    for (int i = inicio; i <= fim; i++) {
                        resultado.append("case ");
                        resultado.append(i);
                        resultado.append(":\n");
                    }
                }
            }

            // Adicionar os comandos dentro do case
            for (CmdContext cmdCtx : itemCtx.cmd()) {
                visitCmd(cmdCtx);
            }

            resultado.append("break;\n");
        }

        // Verificar se há o bloco "senao" (default)
        if (ctx.getChild(ctx.getChildCount() - 2).getText().equals("senao")) {
            resultado.append("default:\n");
            for (int i = ctx.cmd().size() / 2; i < ctx.cmd().size(); i++) {
                visitCmd(ctx.cmd(i));
            }
            resultado.append("break;\n");
        }

        resultado.append("}\n");

        return null;
    }
    
    @Override
    public Void visitSelecao(SelecaoContext ctx) {
        ctx.item_selecao().forEach(var -> visitItem_selecao(var));
        return null;
    }
    
    @Override
    public Void visitItem_selecao(Item_selecaoContext ctx) {
        // Divide a constante em uma lista de intervalo (se houver)
        ArrayList<String> intervalo = new ArrayList<>(Arrays.asList(ctx.constantes().getText().split("\\.\\.")));
        // Obtém o primeiro e o último valor do intervalo, ou o único valor caso não
        // haja intervalo
        String first = intervalo.size() > 0 ? intervalo.get(0) : ctx.constantes().getText();
        String last = intervalo.size() > 1 ? intervalo.get(1) : intervalo.get(0);
        // Itera sobre os valores no intervalo e gera código para cada um deles
        for (int i = Integer.parseInt(first); i <= Integer.parseInt(last); i++) {
            resultado.append("case " + i + ":\n");
            // Visita os comandos dentro do ramo "caso"
            ctx.cmd().forEach(var -> visitCmd(var));
            resultado.append("break;\n");
        }
        return null;
    }
    
    
    @Override
    public Void visitCmdPara(CmdParaContext ctx) {
        String id = ctx.IDENT().getText();
        

        if (id != null) {
            resultado.append("for(")
                 .append(id)
                 .append(" = ");
            visitExp_aritmetica(ctx.exp_aritmetica(0));

            resultado.append("; ")
                 .append(id)
                 .append(" <= ");
            
            visitExp_aritmetica(ctx.exp_aritmetica(1));

            resultado.append("; ")
                 .append(id)
                 .append("++){\n");
            
            for(CmdContext cmdCtx: ctx.cmd()){
                visitCmd(cmdCtx);
            }
            
            resultado.append("}\n");
        }

        return null;
    }
    
    @Override
    public Void visitCmdEnquanto(CmdEnquantoContext ctx) {
        // Adiciona a palavra-chave 'while' e abre o parêntese para a condição
        resultado.append("while(");

        // Verifica se há uma expressão para processar
        if (ctx.expressao() != null) {
            // Visita a expressão lógica do comando "enquanto"
            visitExpressao(ctx.expressao());
        }

        // Fecha o parêntese da condição e abre o bloco de comandos
        resultado.append(") {\n");

        // Verifica se há comandos para processar dentro do loop
        if (ctx.cmd() != null) {
            // Visita os comandos dentro do loop "enquanto"
            ctx.cmd().forEach(this::visitCmd);
        }

        // Adiciona o fechamento do bloco de comandos do loop
        resultado.append("}\n");

        return null;
    }
    
    @Override
    public Void visitCmdFaca(CmdFacaContext ctx) {
        // Adiciona a palavra-chave 'do' e abre o bloco de comandos
        resultado.append("do {\n");

        // Verifica se há comandos para processar
        if (ctx.cmd() != null) {
            // Visita os comandos dentro do loop "faça"
            ctx.cmd().forEach(this::visitCmd);
        }

        // Fecha o bloco de comandos e adiciona a condição 'while'
        resultado.append("} while(");

        // Verifica se há uma expressão lógica para processar
        if (ctx.expressao() != null) {
            // Visita a expressão lógica do comando "faça"
            visitExpressao(ctx.expressao());
        }

        // Fecha o parêntese e adiciona o ponto e vírgula
        resultado.append(");\n");

        return null;
    }
}
