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
        // Obtém a lista de identificadores
        List<TerminalNode> identificadores = ctx.IDENT();
        // Percorre os identificadores e os adiciona à saída
        for (int i = 0; i < identificadores.size(); i++) {
            // Adiciona um ponto antes do identificador, exceto no primeiro
            if (i > 0) {
                resultado.append(".");
            }
            // Adiciona o identificador atual à saída
            resultado.append(identificadores.get(i).getText());
        }

        // Visita a dimensão dos identificadores, caso existam
        visitDimensao(ctx.dimensao());
        return null;
    }

    @Override
    public Void visitDimensao(DimensaoContext ctx) {
        // Itera sobre cada expressão aritmética no contexto
        ctx.exp_aritmetica().forEach(exp -> {
            // Adiciona um colchete de abertura à saída
            resultado.append("[");
            // Visita a expressão aritmética e a adiciona à saída
            visitExp_aritmetica(exp);
            // Adiciona um colchete de fechamento à saída
            resultado.append("]");
        });

        return null;
    }
    
    @Override
    public Void visitParametro(ParametroContext ctx) {
        // Obtém o tipo C e o tipo Alguma dos parâmetros
        String cTipo = AlgumaSemanticoUtils.obterTipoC(ctx.tipo_estendido().getText().replace("^", ""));
        TabelaDeSimbolos.TipoAlguma tipo = AlgumaSemanticoUtils.getTipoAlguma(ctx.tipo_estendido().getText());

        // Percorre os identificadores presentes no contexto
        for (int i = 0; i < ctx.identificador().size(); i++) {
            IdentificadorContext id = ctx.identificador().get(i);

            // Adiciona uma vírgula antes do identificador, exceto no primeiro parâmetro
            if (i > 0) {
                resultado.append(",");
            }

            // Processa o tipo estendido e o adiciona à saída
            visitTipo_estendido(ctx.tipo_estendido());
            
            resultado.append(" ");

            // Processa o identificador e o adiciona à saída
            visitIdentificador(id);

            // Se o tipo for char, adiciona o tamanho [80] ao parâmetro
            if ("char".equals(cTipo)) {
                resultado.append("[80]");
            }

            // Adiciona o identificador e o tipo à tabela de símbolos como uma variável
            tabela.adicionar(id.getText(), tipo, TabelaDeSimbolos.Structure.VAR);
        }

        return null;
    }

    @Override
    public Void visitDeclaracao_local(Declaracao_localContext ctx) {
        // Processa a declaração de variável, se presente
        if (ctx.declaracao_var() != null) {
            visitDeclaracao_var(ctx.declaracao_var());
        }
        if (ctx.declaracao_const() != null) {
            visitDeclaracao_const(ctx.declaracao_const());
        } else if (ctx.declaracao_tipo() != null) {
            visitDeclaracao_tipo(ctx.declaracao_tipo());
        }
        // Método sem retorno explícito, retorna null
        return null;
    }

    @Override
    public Void visitDeclaracao_tipo(Declaracao_tipoContext ctx) {
        // Adiciona 'typedef' à saída para definir um novo tipo
        resultado.append("typedef ");

        // Obtém o tipo C e o tipo Alguma da declaração de tipo
        String cTipo = AlgumaSemanticoUtils.obterTipoC(ctx.tipo().getText().replace("^", ""));
        TabelaDeSimbolos.TipoAlguma tipo = AlgumaSemanticoUtils.getTipoAlguma(ctx.tipo().getText());

        // Verifica se a declaração de tipo é um registro
        if (ctx.tipo().getText().contains("registro")) {
            processaRegistro(ctx);
        }

        // Adiciona o tipo declarado e o nome do tipo à tabela de símbolos
        tabela.adicionar(ctx.IDENT().getText(), tipo, TabelaDeSimbolos.Structure.VAR);

        // Processa o tipo da declaração e adiciona o nome do tipo à saída
        visitTipo(ctx.tipo());
        resultado.append(ctx.IDENT().getText()).append(";\n");

        return null;
    }

    // Método auxiliar para processar tipos de registro
    private void processaRegistro(Declaracao_tipoContext ctx) {
        // Itera sobre cada variável no registro
        ctx.tipo().registro().variavel().forEach(sub -> {
            // Itera sobre cada identificador da variável
            sub.identificador().forEach(idIns -> {
                TabelaDeSimbolos.TipoAlguma tipoIns = AlgumaSemanticoUtils.getTipoAlguma(sub.tipo().getText());
                // Adiciona a entrada para cada identificador no registro
                tabela.adicionar(ctx.IDENT().getText() + "." + idIns.getText(), tipoIns, TabelaDeSimbolos.Structure.VAR);
                // Adiciona a entrada para o tipo do registro
                tabela.adicionar(ctx.IDENT().getText(), tabela.new EntradaTabelaDeSimbolos(idIns.getText(), tipoIns, TabelaDeSimbolos.Structure.TIPO));
            });
        });
    }
    
    @Override
    public Void visitDeclaracao_var(Declaracao_varContext ctx) {
        visitVariavel(ctx.variavel());
        // Método não possui retorno explícito, retorna null
        return null;
    }
    
    @Override
    public Void visitVariavel(VariavelContext ctx) {
        // Obtém o tipo C correspondente ao tipo da variável e o tipo Alguma
        String cTipo = AlgumaSemanticoUtils.obterTipoC(ctx.tipo().getText().replace("^", ""));
        TabelaDeSimbolos.TipoAlguma tipo = AlgumaSemanticoUtils.getTipoAlguma(ctx.tipo().getText());

        // Processa cada identificador no contexto da variável
        for (AlgumaParser.IdentificadorContext id : ctx.identificador()) {
            String idText = id.getText();

            // Processa se a variável for um subcampo de um registro
            if (ctx.tipo().getText().contains("registro")) {
                for (VariavelContext sub : ctx.tipo().registro().variavel()) {
                    for (IdentificadorContext idIns : sub.identificador()) {
                        TabelaDeSimbolos.TipoAlguma tipoIns = AlgumaSemanticoUtils.getTipoAlguma(sub.tipo().getText());
                        tabela.adicionar(idText + "." + idIns.getText(), tipoIns, TabelaDeSimbolos.Structure.VAR);
                    }
                }
            }
            // Processa se a variável for de um tipo definido pelo usuário
            else if (cTipo == null && tipo == null) {
                ArrayList<TabelaDeSimbolos.EntradaTabelaDeSimbolos> tipoEntradas = tabela.retornaTipo(ctx.tipo().getText());
                if (tipoEntradas != null) {
                    for (TabelaDeSimbolos.EntradaTabelaDeSimbolos val : tipoEntradas) {
                        tabela.adicionar(idText + "." + val.nome, val.tipo, TabelaDeSimbolos.Structure.VAR);
                    }
                }
            }
            // Processa se a variável for um vetor
            else if (idText.contains("[")) {
                int ini = idText.indexOf("[");
                int end = idText.indexOf("]");
                String tamanho = idText.substring(ini + 1, end);
                String nome = id.IDENT().get(0).getText();
                int tam = Integer.parseInt(tamanho);
                for (int i = 0; i < tam; i++) {
                    tabela.adicionar(nome + "[" + i + "]", tipo, TabelaDeSimbolos.Structure.VAR);
                }
            } 
            // Adiciona a variável normalmente à tabela de símbolos
            else {
                tabela.adicionar(idText, tipo, TabelaDeSimbolos.Structure.VAR);
            }

            // Visita o tipo e o identificador da variável
            visitTipo(ctx.tipo());
            visitIdentificador(id);

            // Adiciona notação para tipo char se necessário
            if ("char".equals(cTipo)) {
                resultado.append("[80]");
            }

            // Adiciona um ponto e vírgula à declaração
            resultado.append(";\n");
        }

        // Retorna 'null' pois o método não possui um valor de retorno
        return null;
    }
    
       @Override
    public Void visitTipo(TipoContext ctx) {
        // Extrai o tipo C e o tipo Alguma a partir do texto do contexto.
        String cTipo = AlgumaSemanticoUtils.obterTipoC(ctx.getText().replace("^", ""));
        TabelaDeSimbolos.TipoAlguma tipo = AlgumaSemanticoUtils.getTipoAlguma(ctx.getText());

        // Determina se o tipo é um ponteiro.
        boolean isPointer = ctx.getText().contains("^");

        // Adiciona o tipo C correspondente à saída, se disponível.
        if (cTipo != null) {
            resultado.append(cTipo);
        } 
        // Se o tipo for um registro, processa-o.
        else if (ctx.registro() != null) {
            visitRegistro(ctx.registro());
        } 
        // Caso contrário, processa o tipo estendido.
        else {
            visitTipo_estendido(ctx.tipo_estendido());
        }

        // Se for um ponteiro, adiciona o asterisco (*) à saída.
        if (isPointer) {
            resultado.append("*");
        }

        // Adiciona um espaço em branco para a formatação.
        resultado.append(" ");

        // Retorna 'null' pois não há valor de retorno.
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
        // Adiciona 'struct {' seguido por uma quebra de linha.
        resultado.append("struct {\n");

        // Processa cada variável no registro.
        for (VariavelContext var : ctx.variavel()) {
            visitVariavel(var);
        }

        // Adiciona a chave de fechamento '}' e um espaço em branco.
        resultado.append("} ");

        // Retorno nulo para métodos que não precisam retornar valor.
        return null;
    }

    @Override
    public Void visitDeclaracao_const(Declaracao_constContext ctx) {
        // Obtém o tipo C e o tipo Alguma da constante a partir do tipo básico.
        String tipoC = AlgumaSemanticoUtils.obterTipoC(ctx.tipo_basico().getText());
        TabelaDeSimbolos.TipoAlguma tipoAlguma = AlgumaSemanticoUtils.getTipoAlguma(ctx.tipo_basico().getText());

        // Adiciona a constante à tabela de símbolos com o tipo Alguma e a estrutura de variável.
        tabela.adicionar(ctx.IDENT().getText(), tipoAlguma, TabelaDeSimbolos.Structure.VAR);

        // Adiciona a declaração constante à saída, começando com 'const', seguido do tipo C e do identificador.
        resultado.append("const ").append(tipoC).append(" ").append(ctx.IDENT().getText()).append(" = ");

        // Adiciona o valor da constante à saída.
        visitValor_constante(ctx.valor_constante());

        // Finaliza a declaração com um ponto-e-vírgula.
        resultado.append(";\n");

        // Retorno nulo para métodos que não precisam retornar valor.
        return null;
    }
    
    private void adicionarValorConstante(String valorTexto) {
        switch (valorTexto) {
            case "verdadeiro":
                resultado.append("true");
                break;
            case "falso":
                resultado.append("false");
                break;
            default:
                resultado.append(valorTexto);
                break;
        }
    }
    
    @Override
    public Void visitValor_constante(Valor_constanteContext ctx) {
        // Processa o valor constante e adiciona à saída.
        adicionarValorConstante(ctx.getText());
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
        // Adiciona a palavra-chave 'return' ao código de saída.
        resultado.append("return ");

        // Verifica se há uma expressão para retornar.
        if (ctx.expressao() != null) {
            // Visita a expressão para processá-la e adicioná-la à saída.
            visitExpressao(ctx.expressao());
        } else {
            // Se não houver expressão, adiciona um retorno vazio.
            resultado.append(";");
            return null; // Retorna após adicionar o ponto-e-vírgula.
        }

        // Adiciona o ponto-e-vírgula ao final do comando 'return'.
        resultado.append(";\n");

        // Retorna 'null' indicando que o método não tem valor de retorno.
        return null;
    }

    @Override
    public Void visitCmdChamada(CmdChamadaContext ctx) {
        // Adiciona o nome da função à saída e abre o parêntese.
        resultado.append(ctx.IDENT().getText()).append("(");

        // Obtém a lista de expressões passadas como argumentos para a função.
        List<ExpressaoContext> expressoes = ctx.expressao();

        // Itera sobre cada expressão para adicioná-la como argumento.
        for (int i = 0; i < expressoes.size(); i++) {
            if (i > 0) {
                // Adiciona uma vírgula entre os argumentos.
                resultado.append(",");
            }
            // Adiciona a expressão à saída.
            visitExpressao(expressoes.get(i));
        }

        // Fecha o parêntese da chamada de função e adiciona o ponto e vírgula.
        resultado.append(");\n");

        // Retorna null indicando que o método não possui valor de retorno.
        return null;
    }
    
    private void processaIdentificadorParaLeitura(AlgumaParser.IdentificadorContext id, TabelaDeSimbolos.TipoAlguma idType) {
        // Verifica o tipo do identificador e decide o método de leitura.
        if (idType != TabelaDeSimbolos.TipoAlguma.CADEIA) {
            // Usa 'scanf' para tipos diferentes de CADEIA.
            resultado.append("scanf(\"%").append(AlgumaSemanticoUtils.obterSimboloC(idType)).append("\", &")
                .append(id.getText()).append(");\n");
        } else {
            // Usa 'gets' para tipo CADEIA.
            resultado.append("gets(");
            visitIdentificador(id);
            resultado.append(");\n");
        }
    }
    
    @Override
    public Void visitCmdLeia(CmdLeiaContext ctx) {
        // Itera sobre cada identificador na lista de identificadores para leitura.
        for (AlgumaParser.IdentificadorContext id : ctx.identificador()) {
            // Obtém o tipo do identificador a partir da tabela de símbolos.
            TabelaDeSimbolos.TipoAlguma idType = tabela.verificar(id.getText());

            // Processa o identificador com base no seu tipo.
            processaIdentificadorParaLeitura(id, idType);
        }

        // Retorna null indicando que o método não possui valor de retorno.
        return null;
    }
    
    private void processaAtribuicao(String identificador, TabelaDeSimbolos.TipoAlguma tipoVariavel, String valorExpressao) {
        if (tipoVariavel == TabelaDeSimbolos.TipoAlguma.CADEIA) {
            // Processa a atribuição de cadeias de caracteres.
            resultado.append("strcpy(");
            resultado.append(identificador).append(", ").append(valorExpressao).append(");\n");
        } else {
            // Processa a atribuição normal.
            resultado.append(identificador).append(" = ").append(valorExpressao).append(";\n");
        }
    }
    
    @Override
    public Void visitCmdAtribuicao(CmdAtribuicaoContext ctx) {
        // Verifica se a atribuição é para um ponteiro e adiciona o símbolo '*' à saída.
        if (ctx.getText().contains("^")) {
            resultado.append("*");
        }

        // Obtém o tipo da variável identificada pela atribuição.
        TabelaDeSimbolos.TipoAlguma tipoVariavel = tabela.verificar(ctx.identificador().getText());

        // Processa a atribuição com base no tipo da variável.
        processaAtribuicao(ctx.identificador().getText(), tipoVariavel, ctx.expressao().getText());

        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitCmdSe(CmdSeContext ctx) {
        // Adiciona a estrutura do comando 'if' à saída.
        resultado.append("if (");
        visitExpressao(ctx.expressao());
        resultado.append(") {\n");
        
        for (CmdContext cmd : ctx.cmd()) {
            visitCmd(cmd);
        }
        
        if(ctx.getChild(ctx.getChildCount() - 2).getText().equals("senao")){
            resultado.append("} else {\n");
            
            for(int i = ctx.cmd().size() / 2; i < ctx.cmd().size(); i++){
                visitCmd(ctx.cmd(i));
            }
        }
        
        resultado.append("}\n");
        

        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitCmdEscreva(CmdEscrevaContext ctx) {
        // Percorre cada expressão a ser escrita.
        for (AlgumaParser.ExpressaoContext exp : ctx.expressao()) {
            Escopos escopo = new Escopos(tabela);

            // Obtém o símbolo do tipo da expressão em formato de caractere.
            String cType = AlgumaSemanticoUtils.obterSimboloC(AlgumaSemanticoUtils.analisarExpressao(escopo, exp));
            
            if (tabela.existe(exp.getText())) {
                TabelaDeSimbolos.TipoAlguma tip = tabela.verificar(exp.getText());
                cType = AlgumaSemanticoUtils.obterSimboloC(tip);
            }

            // Adiciona a string de formatação para 'printf' na saída.
            resultado.append("printf(\"%").append(cType).append("\", ");
            // Adiciona a expressão a ser escrita na saída.
            resultado.append(exp.getText()).append(");\n");
        }
        // Não possui retorno explícito, retorna 'null'.
        return null;
    }

    
    @Override
    public Void visitExpressao(ExpressaoContext ctx) {
        // Verifica se a expressão possui termos lógicos.
        if (ctx.termo_logico() != null && !ctx.termo_logico().isEmpty()) {
            // Visita e adiciona o primeiro termo lógico à saída.
            visitTermo_logico(ctx.termo_logico(0));

            // Percorre os termos lógicos restantes e os adiciona à saída com o operador '||' (ou).
            for (int i = 1; i < ctx.termo_logico().size(); i++) {
                resultado.append(" || ");
                visitTermo_logico(ctx.termo_logico(i));
            }
        }

        return null;
    }
    
    @Override
    public Void visitTermo_logico(Termo_logicoContext ctx) {
        // Verifica se há fatores lógicos na expressão.
        if (ctx.fator_logico() != null && !ctx.fator_logico().isEmpty()) {
            // Adiciona o primeiro fator lógico na saída.
            visitFator_logico(ctx.fator_logico(0));

            // Percorre os fatores lógicos restantes e os adiciona à saída com o operador '&&' (e).
            for (int i = 1; i < ctx.fator_logico().size(); i++) {
                resultado.append(" && ");
                visitFator_logico(ctx.fator_logico(i));
            }
        }

        return null;
    }
    
    @Override
    public Void visitParcela_logica(Parcela_logicaContext ctx) {
        // Verifica se a parcela lógica é uma expressão relacional.
        if (ctx.exp_relacional() != null) {
            // Visita e adiciona a expressão relacional à saída.
            visitExp_relacional(ctx.exp_relacional());
        } else {
            // Verifica se o texto da parcela lógica é 'verdadeiro' ou 'falso'.
            String text = ctx.getText();
            if ("verdadeiro".equals(text)) {
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
        // Adiciona a primeira expressão aritmética à saída.
        visitExp_aritmetica(ctx.exp_aritmetica(0));

        // Verifica se há operadores relacionais associados.
        if (ctx.op_relacional() != null) {
            // Percorre os operadores relacionais e as expressões aritméticas restantes.
            for (int i = 1; i < ctx.exp_aritmetica().size(); i++) {
                if(ctx.op_relacional().getText().equals("=")){
                    resultado.append(" == ");
                }else{
                    resultado.append(ctx.op_relacional().getText());
                }
                // Adiciona a expressão aritmética correspondente à saída.
                visitExp_aritmetica(ctx.exp_aritmetica(i));
            }
        }

        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitExp_aritmetica(Exp_aritmeticaContext ctx) {
        // Adiciona o primeiro termo à saída.
        visitTermo(ctx.termo(0));

        // Verifica se há operadores aritméticos e adiciona os demais termos com esses operadores à saída.
        if (ctx.op1() != null) {
            for (int i = 0; i < ctx.op1().size(); i++) {
                // Adiciona o operador aritmético à saída.
                resultado.append(ctx.op1(i).getText());
                // Adiciona o próximo termo à saída.
                visitTermo(ctx.termo(i + 1));
            }
        }

        // Não possui retorno explícito, retorna 'null'.
        return null;
    }

    @Override
    public Void visitTermo(TermoContext ctx) {
        // Adiciona o primeiro fator à saída.
        visitFator(ctx.fator(0));

        // Verifica se há operadores multiplicativos ('*' ou '/') e adiciona os demais fatores com esses operadores à saída.
        if (ctx.op2() != null) {
            for (int i = 0; i < ctx.op2().size(); i++) {
                // Adiciona o operador '*' ou '/' à saída.
                resultado.append(ctx.op2(i).getText());
                // Adiciona o próximo fator à saída.
                visitFator(ctx.fator(i + 1));
            }
        }

        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitFator(FatorContext ctx) {
        // Adiciona a primeira parcela à saída.
        visitParcela(ctx.parcela(0));

        // Verifica se há operadores aditivos ('+' ou '-') e processa as demais parcelas.
        if (ctx.op3() != null) {
            for (int i = 0; i < ctx.op3().size(); i++) {
                // Adiciona o operador '+' ou '-' à saída.
                resultado.append(ctx.op3(i).getText());
                // Adiciona a próxima parcela à saída.
                visitParcela(ctx.parcela(i + 1));
            }
        }

        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitParcela(ParcelaContext ctx) {
        // Verifica se a parcela é unária.
        if (ctx.parcela_unario() != null) {
            // Adiciona o operador unário, se presente.
            if (ctx.op_unario() != null) {
                resultado.append(ctx.op_unario().getText());
            }
            // Visita a parcela unária.
            visitParcela_unario(ctx.parcela_unario());
        } else {
            // Visita a parcela não unária.
            visitParcela_nao_unario(ctx.parcela_nao_unario());
        }

        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitParcela_unario(Parcela_unarioContext ctx) {
        // Verifica se a parcela unária é uma chamada de função.
        if (ctx.IDENT() != null) {
            // Adiciona o nome da função à saída.
            resultado.append(ctx.IDENT().getText()).append("(");
            // Processa as expressões de argumentos, adicionando-as à saída separadas por vírgula.
            for (int i = 0; i < ctx.expressao().size(); i++) {
                visitExpressao(ctx.expressao(i));
                if (i < ctx.expressao().size() - 1) {
                    resultado.append(", ");
                }
            }
            // Fecha a chamada de função.
            resultado.append(")");
        // Verifica se a parcela unária é uma expressão entre parênteses.
        } else {
            resultado.append(ctx.getText());
        // Caso contrário, trata a parcela como um valor literal ou identificador simples.
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
        // Itera sobre cada item de seleção e o processa.
        for (AlgumaParser.Item_selecaoContext item : ctx.item_selecao()) {
            visitItem_selecao(item);
        }
        // Não possui retorno explícito, retorna 'null'.
        return null;
    }
    
    @Override
    public Void visitItem_selecao(Item_selecaoContext ctx) {
        // Divide a constante em uma lista de intervalo (se houver)
        List<String> intervalo = Arrays.asList(ctx.constantes().getText().split("\\.\\."));

        // Obtém o primeiro valor do intervalo (ou único valor caso não haja intervalo)
        String first = intervalo.get(0);
        String last = intervalo.size() > 1 ? intervalo.get(1) : first;

        try {
            // Itera sobre os valores no intervalo e gera código para cada um deles
            int start = Integer.parseInt(first);
            int end = Integer.parseInt(last);

            for (int i = start; i <= end; i++) {
                resultado.append("case ").append(i).append(":\n");
                // Visita os comandos dentro do ramo "caso"
                for (CmdContext cmd : ctx.cmd()) {
                    visitCmd(cmd);
                }
                resultado.append("break;\n");
            }
        } catch (NumberFormatException e) {
            // Trata casos em que as constantes não são números inteiros válidos
            System.err.println("Erro: Intervalo inválido em 'case': " + ctx.constantes().getText());
        }

        // Não possui retorno explícito, retorna 'null'.
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

            // Visita os comandos dentro do loop "para"
            ctx.cmd().forEach(this::visitCmd);

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
