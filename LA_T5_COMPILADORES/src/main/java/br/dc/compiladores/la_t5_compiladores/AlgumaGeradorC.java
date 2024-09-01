/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.dc.compiladores.la_t5_compiladores;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.dc.compiladores.la_t5_compiladores.AlgumaParser;
import br.dc.compiladores.la_t5_compiladores.TabelaDeSimbolos;
import br.dc.compiladores.la_t5_compiladores.AlgumaSemantico;
import static br.dc.compiladores.la_t5_compiladores.TabelaDeSimbolos.TipoAlguma.CADEIA;
import static br.dc.compiladores.la_t5_compiladores.TabelaDeSimbolos.TipoAlguma.INTEIRO;
import static br.dc.compiladores.la_t5_compiladores.TabelaDeSimbolos.TipoAlguma.REAL;
import org.antlr.v4.runtime.tree.TerminalNode;

public class AlgumaGeradorC extends AlgumaBaseVisitor<Void> {
    private final StringBuilder saida;
    private final TabelaDeSimbolos tabela;

    public AlgumaGeradorC() {
        this.saida = new StringBuilder();
        this.tabela = new TabelaDeSimbolos();
    }
    
    public String getSaida() {
        return saida.toString();
    }
    
    public static String getTipoC(String val) {
        switch(val) {
            case "literal": return "char";
            case "inteiro": return "int";
            case "real": return "float";
            default: return null;
        }
    }

    public static String getTipoCSimbolo(TabelaDeSimbolos.TipoAlguma tipo) {
        switch(tipo) {
            case CADEIA: return "s";
            case INTEIRO: return "d";
            case REAL: return "f";
            default: return null;
        }
    }
    
    @Override
    public Void visitPrograma(AlgumaParser.ProgramaContext ctx) {
        // Adicionando as bibliotecas C necessárias.
        saida.append("#include <stdio.h>\n")
             .append("#include <stdlib.h>\n\n");

        // Visita todas as declarações locais e globais do programa.
        ctx.declaracoes().decl_local_global().forEach(this::visitDecl_local_global);

        // Adiciona uma linha em branco para melhor legibilidade do código C.
        saida.append("\n");

        // Início da função principal 'int main()'.
        saida.append("int main() {\n");

        // Visita o corpo do programa.
        visitCorpo(ctx.corpo());

        // Adiciona a instrução de retorno 0 para finalizar o programa com sucesso.
        saida.append("return 0;\n");

        // Fim da função principal 'int main()'.
        saida.append("}\n");

        return null;
    }
    
    @Override
    public Void visitDecl_local_global(AlgumaParser.Decl_local_globalContext ctx) {
        // Verifica e visita a declaração local ou global, se existir.
        if (ctx.declaracao_local() != null) {
            visitDeclaracao_local(ctx.declaracao_local());
        } else {
            visitDeclaracao_global(ctx.declaracao_global());
        }
        return null;
    }
    
    @Override
    public Void visitCorpo(AlgumaParser.CorpoContext ctx) {
        // Visita todas as declarações locais.
        ctx.declaracao_local().forEach(this::visitDeclaracao_local);

        // Visita todos os comandos.
        ctx.cmd().forEach(this::visitCmd);

        return null;
    }

    
    @Override
    public Void visitDeclaracao_global(AlgumaParser.Declaracao_globalContext ctx) {
        // Verifica se a declaração global é um procedimento ou uma função.
        boolean isProcedimento = ctx.getText().contains("procedimento");
        String identificador = ctx.IDENT().getText();

        if (isProcedimento) {
            // Adiciona a palavra-chave 'void' seguida do identificador do procedimento e abre parênteses.
            saida.append("void ").append(identificador).append("(");
        } else {
            // Obtém o tipo C correspondente ao tipo estendido da função.
            String tipoExtendido = ctx.tipo_estendido().getText().replace("^", "");
            String cTipo = getTipoC(tipoExtendido);

            // Obtém o tipo Alguma da função.
            TabelaDeSimbolos.TipoAlguma tipoAlguma = AlgumaSemantico.determinarTipo(tipoExtendido);

            // Visita o tipo estendido para processá-lo.
            visitTipo_estendido(ctx.tipo_estendido());

            // Se o tipo for char, adiciona o tamanho [80] ao parâmetro da função.
            if ("char".equals(cTipo)) {
                saida.append("[80]");
            }

            // Adiciona o tipo e o identificador da função à saída.
            saida.append(" ").append(identificador).append("(");

            // Adiciona a função à tabela de símbolos.
            tabela.adicionar(identificador, tipoAlguma, TabelaDeSimbolos.Structure.FUNC);
        }

        // Visita todos os parâmetros da função.
        ctx.parametros().parametro().forEach(this::visitParametro);

        saida.append("){\n");

        // Visita todas as declarações locais da função.
        ctx.declaracao_local().forEach(this::visitDeclaracao_local);

        // Visita todos os comandos da função.
        ctx.cmd().forEach(this::visitCmd);

        saida.append("}\n");

        return null;
    }
    
    @Override
    public Void visitIdentificador(AlgumaParser.IdentificadorContext ctx) {
        // Adiciona um espaço à saída.
        saida.append(" ");

        // Obtém todos os identificadores presentes no contexto.
        List<TerminalNode> identificadores = ctx.IDENT();

        // Adiciona os identificadores à saída, separando-os com ponto.
        for (int i = 0; i < identificadores.size(); i++) {
            if (i > 0) {
                saida.append(".");
            }
            saida.append(identificadores.get(i).getText());
        }

        // Visita a dimensão dos identificadores, se existir.
        visitDimensao(ctx.dimensao());

        return null;
    }
    
    @Override
    public Void visitDimensao(AlgumaParser.DimensaoContext ctx) {
        // Percorre todas as expressões aritméticas presentes no contexto de dimensões
        for (AlgumaParser.Exp_aritmeticaContext exp : ctx.exp_aritmetica()) {
            saida.append("[");
            // Visita a expressão aritmética para processá-la e adicioná-la à saída.
            visitExp_aritmetica(exp);
            saida.append("]");
        }

        return null;
    }
    
    @Override
    public Void visitParametro(AlgumaParser.ParametroContext ctx) {
        // Obtém o tipo C e o tipo Alguma correspondente ao tipo estendido dos parâmetros.
        String tipoExtendido = ctx.tipo_estendido().getText().replace("^", "");
        String cTipo = getTipoC(tipoExtendido);
        TabelaDeSimbolos.TipoAlguma tipo = AlgumaSemantico.determinarTipo(tipoExtendido);
        // Visita o tipo estendido dos parâmetros para processá-lo e adicioná-lo à saída.
        visitTipo_estendido(ctx.tipo_estendido());
        // Variável para controlar o número de parâmetros no contexto.
        boolean primeiroParametro = true;
        // Percorre todos os identificadores presentes no contexto.
        for (AlgumaParser.IdentificadorContext id : ctx.identificador()) {
            // Adiciona uma vírgula antes do identificador, exceto para o primeiro parâmetro.
            if (!primeiroParametro) {
                saida.append(",");
            }
            primeiroParametro = false;
            // Visita o identificador dos parâmetros para processá-lo e adicioná-lo à saída.
            visitIdentificador(id);
            // Se o tipo for char, adiciona o tamanho [80] ao parâmetro.
            if ("char".equals(cTipo)) {
                saida.append("[80]");
            }
            // Adiciona o identificador e o tipo à tabela de símbolos como variável.
            tabela.adicionar(id.getText(), tipo, TabelaDeSimbolos.Structure.VAR);
        }

        return null;
    }
    
    @Override
    public Void visitDeclaracao_local(AlgumaParser.Declaracao_localContext ctx) {
        // Verifica e visita a declaração de variável, se presente.
        if (ctx.declaracao_var() != null) {
            visitDeclaracao_var(ctx.declaracao_var());
        }

        // Verifica e visita a declaração de constante, se presente.
        if (ctx.declaracao_const() != null) {
            visitDeclaracao_const(ctx.declaracao_const());
        }

        // Verifica e visita a declaração de tipo, se presente.
        if (ctx.declaracao_tipo() != null) {
            visitDeclaracao_tipo(ctx.declaracao_tipo());
        }

        return null;
    }
    
    @Override
    public Void visitDeclaracao_tipo(AlgumaParser.Declaracao_tipoContext ctx) {
        // Adiciona 'typedef' à saída.
        saida.append("typedef ");

        // Obtém o tipo C e o tipo Alguma da declaração.
        String tipoTexto = ctx.tipo().getText().replace("^", "");
        String cTipo = getTipoC(tipoTexto);
        TabelaDeSimbolos.TipoAlguma tipo = AlgumaSemantico.determinarTipo(tipoTexto);

        // Verifica se o tipo é um registro e processa suas subvariáveis.
        if (tipoTexto.contains("registro")) {
            for (AlgumaParser.VariavelContext sub : ctx.tipo().registro().variavel()) {
                for (AlgumaParser.IdentificadorContext id : sub.identificador()) {
                    TabelaDeSimbolos.TipoAlguma tipoIns = AlgumaSemantico.determinarTipo(sub.tipo().getText());
                    // Adiciona identificadores e tipos na tabela de símbolos.
                    tabela.adicionar(ctx.IDENT().getText() + "." + id.getText(), tipoIns, TabelaDeSimbolos.Structure.VAR);
                    tabela.adicionar(ctx.IDENT().getText(), tabela.new EntradaTabelaDeSimbolos(id.getText(), tipoIns, TabelaDeSimbolos.Structure.TIPO));
                }
            }
        }

        // Adiciona o tipo declarado à tabela de símbolos.
        tabela.adicionar(ctx.IDENT().getText(), tipo, TabelaDeSimbolos.Structure.VAR);

        // Adiciona o nome do tipo à saída.
        visitTipo(ctx.tipo());
        saida.append(ctx.IDENT().getText()).append(";\n");

        return null;
    }
    
    @Override
    public Void visitDeclaracao_var(AlgumaParser.Declaracao_varContext ctx) {
        // Processa a declaração da variável.
        visitVariavel(ctx.variavel());
        return null;
    }
    
    @Override
    public Void visitVariavel(AlgumaParser.VariavelContext ctx) {
        // Obtém o tipo C e o tipo Alguma da variável.
        String tipoTexto = ctx.tipo().getText().replace("^", "");
        String cTipo = getTipoC(tipoTexto);
        TabelaDeSimbolos.TipoAlguma tipo = AlgumaSemantico.determinarTipo(tipoTexto);

        // Processa cada identificador da variável.
        for (AlgumaParser.IdentificadorContext id : ctx.identificador()) {
            String idTexto = id.getText();
            // Se a variável é parte de um registro, adiciona seus membros.
            if (tipoTexto.contains("registro")) {
                for (AlgumaParser.VariavelContext sub : ctx.tipo().registro().variavel()) {
                    for (AlgumaParser.IdentificadorContext idIns : sub.identificador()) {
                        TabelaDeSimbolos.TipoAlguma tipoIns = AlgumaSemantico.determinarTipo(sub.tipo().getText());
                        tabela.adicionar(idTexto + "." + idIns.getText(), tipoIns, TabelaDeSimbolos.Structure.VAR);
                    }
                }
            } 
            // Se o tipo é indefinido, assume um tipo definido pelo usuário.
            else if (cTipo == null && tipo == null) {
                ArrayList<TabelaDeSimbolos.EntradaTabelaDeSimbolos> tipoDefinido = tabela.retornaTipo(tipoTexto);
                if (tipoDefinido != null) {
                    for (TabelaDeSimbolos.EntradaTabelaDeSimbolos entrada : tipoDefinido) {
                        tabela.adicionar(idTexto + "." + entrada.nome, entrada.tipo, TabelaDeSimbolos.Structure.VAR);
                    }
                }
            } 
            // Se a variável é um vetor, adiciona suas posições.
            else if (idTexto.contains("[")) {
                int ini = idTexto.indexOf("[");
                int fim = idTexto.indexOf("]");
                String tam = (fim - ini == 2) ? String.valueOf(idTexto.charAt(ini + 1)) : idTexto.substring(ini + 1, fim);
                String nome = id.IDENT().get(0).getText();
                for (int i = 0; i < Integer.parseInt(tam); i++) {
                    tabela.adicionar(nome + "[" + i + "]", tipo, TabelaDeSimbolos.Structure.VAR);
                }
            } 
            // Adiciona a variável normalmente à tabela de símbolos.
            else {
                tabela.adicionar(idTexto, tipo, TabelaDeSimbolos.Structure.VAR);
            }
            // Processa o tipo e o identificador da variável.
            visitTipo(ctx.tipo());
            visitIdentificador(id);

            // Adiciona o tamanho [80] se o tipo for char e finaliza a declaração com ponto-e-vírgula.
            if ("char".equals(cTipo)) {
                saida.append("[80]");
            }
            saida.append(";\n");
        }

        return null;
    }
    
    @Override
    public Void visitTipo(AlgumaParser.TipoContext ctx) {
        // Obtém o tipo C e o tipo Alguma a partir do contexto
        String cTipo = getTipoC(ctx.getText().replace("^", ""));
        TabelaDeSimbolos.TipoAlguma tipo = AlgumaSemantico.determinarTipo(ctx.getText());
        boolean isPointer = ctx.getText().contains("^");

        // Adiciona o tipo C à saída se for um tipo primitivo, ou visita o tipo correspondente
        if (cTipo != null) {
            saida.append(cTipo);
        } else if (ctx.registro() != null) {
            visitRegistro(ctx.registro());
        } else {
            visitTipo_estendido(ctx.tipo_estendido());
        }

        // Adiciona o asterisco para ponteiros, se aplicável, e um espaço
        if (isPointer) {
            saida.append("*");
        }
        saida.append(" ");

        return null;
    }
    
    @Override
    public Void visitTipo_estendido(AlgumaParser.Tipo_estendidoContext ctx) {
        // Processa o tipo básico ou identificador.
        visitTipo_basico_ident(ctx.tipo_basico_ident());

        // Adiciona o asterisco para ponteiros, se necessário.
        if (ctx.getText().contains("^")) {
            saida.append("*");
        }

        return null;
    }
    
    @Override
    public Void visitTipo_basico_ident(AlgumaParser.Tipo_basico_identContext ctx) {
        // Adiciona o nome do identificador ou o tipo C correspondente à saída.
        if (ctx.IDENT() != null) {
            saida.append(ctx.IDENT().getText());
        } else {
            saida.append(getTipoC(ctx.getText().replace("^", "")));
        }

        return null;
    }
    
    @Override
    public Void visitRegistro(AlgumaParser.RegistroContext ctx) {
        // Adiciona a declaração de struct.
        saida.append("struct {\n");

        // Processa cada variável do registro.
        ctx.variavel().forEach(this::visitVariavel);

        // Fecha o bloco do registro.
        saida.append("} ");

        return null;
    }

    
    @Override
    public Void visitDeclaracao_const(AlgumaParser.Declaracao_constContext ctx) {
        // Obtém o tipo C e o tipo Alguma da constante.
        String cTipo = getTipoC(ctx.tipo_basico().getText());
        TabelaDeSimbolos.TipoAlguma tipoAlguma = AlgumaSemantico.determinarTipo(ctx.tipo_basico().getText());

        // Adiciona a constante à tabela de símbolos.
        tabela.adicionar(ctx.IDENT().getText(), tipoAlguma, TabelaDeSimbolos.Structure.VAR);

        // Adiciona a declaração da constante à saída.
        saida.append(String.format("const %s %s = ", cTipo, ctx.IDENT().getText()));

        // Processa o valor constante e adiciona o ponto-e-vírgula.
        visitValor_constante(ctx.valor_constante());
        saida.append(";\n");

        return null;
    }

    
    @Override
    public Void visitValor_constante(AlgumaParser.Valor_constanteContext ctx) {
        // Adiciona o valor constante correspondente à saída.
        switch (ctx.getText()) {
            case "verdadeiro":
                saida.append("true");
                break;
            case "falso":
                saida.append("false");
                break;
            default:
                saida.append(ctx.getText());
                break;
        }

        return null;
    }
    
    @Override
    public Void visitCmd(AlgumaParser.CmdContext ctx) {
        // Processa o tipo de comando com base no contexto.
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

        return null;
    }
    
    @Override
    public Void visitCmdRetorne(AlgumaParser.CmdRetorneContext ctx) {
        // Adiciona 'return' seguido da expressão a ser retornada.
        saida.append("return ");
        visitExpressao(ctx.expressao());
        saida.append(";\n");

        return null;
    }
    
    @Override
    public Void visitCmdChamada(AlgumaParser.CmdChamadaContext ctx) {
        // Adiciona o nome da função e abre parênteses.
        saida.append(ctx.IDENT().getText()).append("(");

        // Adiciona as expressões de argumentos, separadas por vírgula.
        for (int i = 0; i < ctx.expressao().size(); i++) {
            if (i > 0) {
                saida.append(",");
            }
            visitExpressao(ctx.expressao(i));
        }

        // Fecha a chamada de função e adiciona ponto e vírgula.
        saida.append(");\n");

        return null;
    }

    @Override
    public Void visitCmdLeia(AlgumaParser.CmdLeiaContext ctx) {
        // Processa cada identificador da lista para leitura.
        for (AlgumaParser.IdentificadorContext id : ctx.identificador()) {
            // Obtém o tipo do identificador na tabela de símbolos.
            TabelaDeSimbolos.TipoAlguma tipo = tabela.verificar(id.getText());

            // Adiciona a string de formatação para 'scanf' ou usa 'gets' dependendo do tipo.
            if (tipo != TabelaDeSimbolos.TipoAlguma.CADEIA) {
                saida.append("scanf(\"%").append(getTipoCSimbolo(tipo)).append("\", &")
                     .append(id.getText()).append(");\n");
            } else {
                saida.append("gets(");
                visitIdentificador(id);
                saida.append(");\n");
            }
        }
        return null;
    }

    
    @Override
    public Void visitCmdEscreva(AlgumaParser.CmdEscrevaContext ctx) {
        // Processa cada expressão para escrita.
        for (AlgumaParser.ExpressaoContext exp : ctx.expressao()) {
            // Cria um novo escopo para determinar o tipo da expressão.
            Escopos escopo = new Escopos(tabela);
            // Obtém o tipo C da expressão.
            String cType = getTipoCSimbolo(AlgumaSemanticoUtils.analisarExpressao(escopo, exp));

            // Se a expressão já está na tabela de símbolos, usa o tipo da tabela.
            if (tabela.existe(exp.getText())) {
                TabelaDeSimbolos.TipoAlguma tipo = tabela.verificar(exp.getText());
                cType = getTipoCSimbolo(tipo);
            }

            // Adiciona o comando printf com a formatação correta.
            saida.append("printf(\"%").append(cType).append("\", ").append(exp.getText()).append(");\n");
        }
        return null;
    }
    
    @Override
    public Void visitCmdAtribuicao(AlgumaParser.CmdAtribuicaoContext ctx) {
        // Adiciona o símbolo '*' se a atribuição for para um ponteiro.
        if (ctx.getText().contains("^")) {
            saida.append("*");
        }

        // Obtém o tipo da variável identificada pela atribuição.
        TabelaDeSimbolos.TipoAlguma tipo = tabela.verificar(ctx.identificador().getText());

        // Se o tipo for CADEIA, usa 'strcpy' para a atribuição.
        if (tipo == TabelaDeSimbolos.TipoAlguma.CADEIA) {
            saida.append("strcpy(");
            visitIdentificador(ctx.identificador());
            saida.append(", ").append(ctx.expressao().getText()).append(");\n");
        } else {
            // Para outros tipos, realiza uma atribuição normal.
            visitIdentificador(ctx.identificador());
            saida.append(" = ").append(ctx.expressao().getText()).append(";\n");
        }

        return null;
    }

    @Override
    public Void visitCmdSe(AlgumaParser.CmdSeContext ctx) {
        // Adiciona a estrutura 'if' e a expressão condicional.
        saida.append("if (");
        visitExpressao(ctx.expressao());
        saida.append(") {\n");

        // Visita os comandos no bloco 'if'.
        ctx.cmdIf.forEach(this::visitCmd);

        saida.append("}\n");

        // Se existir um bloco 'else', adiciona a estrutura correspondente.
        if (ctx.cmdElse != null) {
            saida.append("else {\n");
            ctx.cmdElse.forEach(this::visitCmd);
            saida.append("}\n");
        }

        return null;
    }

    
    @Override
    public Void visitExpressao(AlgumaParser.ExpressaoContext ctx) {
        // Adiciona os termos lógicos à saída, separados por '||'.
        if (ctx.termo_logico() != null && !ctx.termo_logico().isEmpty()) {
            // Adiciona o primeiro termo lógico.
            visitTermo_logico(ctx.termo_logico(0));

            // Adiciona os demais termos lógicos com '||'.
            for (int i = 1; i < ctx.termo_logico().size(); i++) {
                saida.append(" || ");
                visitTermo_logico(ctx.termo_logico(i));
            }
        }

        return null;
    }

    @Override
    public Void visitTermo_logico(AlgumaParser.Termo_logicoContext ctx) {
        // Adiciona os fatores lógicos à saída, separados por '&&'.
        if (ctx.fator_logico() != null && !ctx.fator_logico().isEmpty()) {
            // Adiciona o primeiro fator lógico.
            visitFator_logico(ctx.fator_logico(0));

            // Adiciona os fatores lógicos restantes com '&&'.
            for (int i = 1; i < ctx.fator_logico().size(); i++) {
                saida.append(" && ");
                visitFator_logico(ctx.fator_logico(i));
            }
        }

        return null;
    }

    @Override
    public Void visitFator_logico(AlgumaParser.Fator_logicoContext ctx) {
        // Adiciona o operador '!' se o fator lógico for precedido por 'nao'.
        if (ctx.getText().startsWith("nao")) {
            saida.append("!");
        }

        // Processa a parcela lógica do fator lógico.
        visitParcela_logica(ctx.parcela_logica());

        return null;
    }

    @Override
    public Void visitParcela_logica(AlgumaParser.Parcela_logicaContext ctx) {
        // Se houver uma expressão relacional, processa-a.
        if (ctx.exp_relacional() != null) {
            visitExp_relacional(ctx.exp_relacional());
        } else {
            // Se a parcela lógica for 'verdadeiro' ou 'falso', adiciona o valor correspondente.
            saida.append(ctx.getText().equals("verdadeiro") ? "true" : "false");
        }

        return null;
    }
    
    @Override
    public Void visitExp_relacional(AlgumaParser.Exp_relacionalContext ctx) {
        // Adiciona a primeira expressão aritmética na saída.
        visitExp_aritmetica(ctx.exp_aritmetica(0));

        // Percorre as demais expressões aritméticas e operadores relacionais e os
        // adiciona à saída.
        for (int i = 1; i < ctx.exp_aritmetica().size(); i++) {
            AlgumaParser.Exp_aritmeticaContext termo = ctx.exp_aritmetica(i);
            if (ctx.op_relacional().getText().equals("=")) {
                saida.append(" == ");
            } else {
                saida.append(ctx.op_relacional().getText());
            }
            visitExp_aritmetica(termo);
        }

        return null;
    }

    
    @Override
    public Void visitExp_aritmetica(AlgumaParser.Exp_aritmeticaContext ctx) {
        // Adiciona o primeiro termo.
        visitTermo(ctx.termo(0));

        // Adiciona os demais termos com seus operadores aritméticos.
        for (int i = 1; i < ctx.termo().size(); i++) {
            saida.append(ctx.op1(i - 1).getText());
            visitTermo(ctx.termo(i));
        }

        return null;
    }

    
    @Override
    public Void visitTermo(AlgumaParser.TermoContext ctx) {
        // Adiciona o primeiro fator.
        visitFator(ctx.fator(0));

        // Adiciona os demais fatores com seus operadores '*' ou '/'.
        for (int i = 1; i < ctx.fator().size(); i++) {
            saida.append(ctx.op2(i - 1).getText());
            visitFator(ctx.fator(i));
        }

        return null;
    }

    
    @Override
    public Void visitFator(AlgumaParser.FatorContext ctx) {
        // Adiciona a primeira parcela à saída.
        visitParcela(ctx.parcela(0));

        // Adiciona as demais parcelas com operadores '+' ou '-'.
        for (int i = 1; i < ctx.parcela().size(); i++) {
            // Adiciona o operador aritmético correspondente.
            saida.append(ctx.op3(i - 1).getText());

            // Adiciona a parcela seguinte.
            visitParcela(ctx.parcela(i));
        }

        return null;
    }

    @Override
    public Void visitParcela(AlgumaParser.ParcelaContext ctx) {
        // Adiciona o operador unário, se presente.
        if (ctx.op_unario() != null) {
            saida.append(ctx.op_unario().getText());
        }

        // Visita a parcela unária ou não unária, conforme o caso.
        if (ctx.parcela_unario() != null) {
            visitParcela_unario(ctx.parcela_unario());
        } else {
            visitParcela_nao_unario(ctx.parcela_nao_unario());
        }

        return null;
    }

    
    @Override
    public Void visitParcela_unario(AlgumaParser.Parcela_unarioContext ctx) {
        if (ctx.IDENT() != null) {
            // Adiciona o identificador e a abertura dos parênteses para uma chamada de função.
            saida.append(ctx.IDENT().getText()).append("(");

            // Visita e adiciona cada expressão como argumento da função.
            for (int i = 0; i < ctx.expressao().size(); i++) {
                visitExpressao(ctx.expressao(i));
                if (i < ctx.expressao().size() - 1) {
                    saida.append(", ");
                }
            }
            saida.append(")");
        } else if (ctx.parentesis_expressao() != null) {
            // Adiciona parênteses ao redor da expressão.
            saida.append("(");
            visitExpressao(ctx.parentesis_expressao().expressao());
            saida.append(")");
        } else {
            // Adiciona o texto literal da parcela.
            saida.append(ctx.getText());
        }

        return null;
    }

    @Override
    public Void visitParcela_nao_unario(AlgumaParser.Parcela_nao_unarioContext ctx) {
        // Adiciona o texto literal da parcela não unária.
        saida.append(ctx.getText());
        return null;
    }
    
    @Override
    public Void visitCmdCaso(AlgumaParser.CmdCasoContext ctx) {
        // Inicia o switch com a expressão aritmética.
        saida.append("switch (").append(ctx.exp_aritmetica().getText()).append(") {\n");

        // Processa cada item de seleção.
        for (AlgumaParser.Item_selecaoContext itemCtx : ctx.selecao().item_selecao()) {
            for (AlgumaParser.Numero_intervaloContext intervaloCtx : itemCtx.constantes().numero_intervalo()) {
                // Adiciona casos para intervalos de números ou números individuais.
                if (intervaloCtx.NUM_INT().size() == 1) {
                    saida.append("case ").append(intervaloCtx.NUM_INT(0).getText()).append(":\n");
                } else {
                    int inicio = Integer.parseInt(intervaloCtx.NUM_INT(0).getText());
                    int fim = Integer.parseInt(intervaloCtx.NUM_INT(1).getText());
                    for (int i = inicio; i <= fim; i++) {
                        saida.append("case ").append(i).append(":\n");
                    }
                }
            }

            // Adiciona os comandos associados ao case.
            for (AlgumaParser.CmdContext cmdCtx : itemCtx.cmd()) {
                visitCmd(cmdCtx);
            }
            saida.append("break;\n");
        }

        // Adiciona o bloco default (senao) se presente.
        if (ctx.getChild(ctx.getChildCount() - 2).getText().equals("senao")) {
            saida.append("default:\n");
            for (int i = ctx.cmd().size() / 2; i < ctx.cmd().size(); i++) {
                visitCmd(ctx.cmd(i));
            }
            saida.append("break;\n");
        }

        saida.append("}\n");

        return null;
    }

    
    @Override
    public Void visitSelecao(AlgumaParser.SelecaoContext ctx) {
        // Visita cada item de seleção.
        ctx.item_selecao().forEach(this::visitItem_selecao);
        return null;
    }
    
    @Override
    public Void visitItem_selecao(AlgumaParser.Item_selecaoContext ctx) {
        // Divide a constante em intervalo se houver
        List<String> intervalo = Arrays.asList(ctx.constantes().getText().split("\\.\\."));

        // Define o primeiro e o último valor do intervalo
        String primeiro = intervalo.get(0);
        String ultimo = intervalo.size() > 1 ? intervalo.get(1) : primeiro;

        // Gera código para cada valor no intervalo
        for (int i = Integer.parseInt(primeiro); i <= Integer.parseInt(ultimo); i++) {
            saida.append("case ").append(i).append(":\n");
            // Visita os comandos dentro do case
            ctx.cmd().forEach(this::visitCmd);
            saida.append("break;\n");
        }

        return null;
    }

    
    @Override
    public Void visitCmdPara(AlgumaParser.CmdParaContext ctx) {
        String id = ctx.IDENT().getText();
        saida.append("for (")
             .append(id).append(" = ");
        visitExp_aritmetica(ctx.exp_aritmetica(0));
        saida.append("; ")
             .append(id).append(" <= ");
        visitExp_aritmetica(ctx.exp_aritmetica(1));
        saida.append("; ")
             .append(id).append("++) {\n");

        // Visita os comandos dentro do loop "para"
        ctx.cmd().forEach(this::visitCmd);
        saida.append("}\n");

        return null;
    }
    
    @Override
    public Void visitCmdEnquanto(AlgumaParser.CmdEnquantoContext ctx) {
        saida.append("while (");
        visitExpressao(ctx.expressao());
        saida.append(") {\n");

        // Visita os comandos dentro do loop "enquanto"
        ctx.cmd().forEach(this::visitCmd);
        saida.append("}\n");

        return null;
    }
    
    @Override
    public Void visitCmdFaca(AlgumaParser.CmdFacaContext ctx) {
        saida.append("do {\n");

        // Visita os comandos dentro do loop "faca"
        ctx.cmd().forEach(this::visitCmd);

        saida.append("} while (");
        visitExpressao(ctx.expressao());
        saida.append(");\n");

        return null;
    }

    
}
