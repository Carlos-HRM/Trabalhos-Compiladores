/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.dc.compiladores.la_t3_compiladores;

public class AlgumaSemantico extends AlgumaBaseVisitor<Void> {
    
    // Escopos para armazenar as tabelas de símbolos
    Escopos escopos = new Escopos();

    @Override
    public Void visitPrograma(AlgumaParser.ProgramaContext ctx) {
        // Visita o nó 'programa' na árvore sintática
        return super.visitPrograma(ctx);
    }

    @Override
    public Void visitDeclaracao_tipo(AlgumaParser.Declaracao_tipoContext ctx) {
        // Obtém o escopo atual
        TabelaDeSimbolos escopoAtual = escopos.obterEscopoAtual();
        // Verifica se o tipo já foi declarado no mesmo escopo
        if (escopoAtual.existe(ctx.IDENT().getText())) {
            AlgumaSemanticoUtils.registrarErroSemantico(ctx.start, "tipo " + ctx.IDENT().getText()
                    + " declarado duas vezes no mesmo escopo");
        } else {
            // Adiciona o tipo à tabela de símbolos
            escopoAtual.adicionar(ctx.IDENT().getText(), TabelaDeSimbolos.TipoAlguma.TIPO);
        }
        return super.visitDeclaracao_tipo(ctx);
    }

    @Override
    public Void visitDeclaracao_var(AlgumaParser.Declaracao_varContext ctx) {
        // Obtém o escopo atual
        TabelaDeSimbolos escopoAtual = escopos.obterEscopoAtual();
        // Itera sobre cada identificador na declaração de variáveis
        for (AlgumaParser.IdentificadorContext id : ctx.variavel().identificador()) {
            // Verifica se o identificador já foi declarado no mesmo escopo
            if (escopoAtual.existe(id.getText())) {
                AlgumaSemanticoUtils.registrarErroSemantico(id.start, "identificador " + id.getText()
                        + " ja declarado anteriormente");
            } else {
                // Determina o tipo da variável e a adiciona à tabela de símbolos
                TabelaDeSimbolos.TipoAlguma tipo = determinarTipo(ctx.variavel().tipo().getText());
                escopoAtual.adicionar(id.getText(), tipo);
            }
        }
        return super.visitDeclaracao_var(ctx);
    }

    @Override
    public Void visitDeclaracao_const(AlgumaParser.Declaracao_constContext ctx) {
        // Obtém o escopo atual
        TabelaDeSimbolos escopoAtual = escopos.obterEscopoAtual();
        // Verifica se a constante já foi declarada no mesmo escopo
        if (escopoAtual.existe(ctx.IDENT().getText())) {
            AlgumaSemanticoUtils.registrarErroSemantico(ctx.start, "constante " + ctx.IDENT().getText()
                    + " ja declarada anteriormente");
        } else {
            // Determina o tipo da constante e a adiciona à tabela de símbolos
            TabelaDeSimbolos.TipoAlguma tipo = determinarTipo(ctx.tipo_basico().getText());
            escopoAtual.adicionar(ctx.IDENT().getText(), tipo);
        }
        return super.visitDeclaracao_const(ctx);
    }

    @Override
    public Void visitDeclaracao_global(AlgumaParser.Declaracao_globalContext ctx) {
        // Obtém o escopo atual
        TabelaDeSimbolos escopoAtual = escopos.obterEscopoAtual();
        // Verifica se o identificador global já foi declarado no mesmo escopo
        if (escopoAtual.existe(ctx.IDENT().getText())) {
            AlgumaSemanticoUtils.registrarErroSemantico(ctx.start, ctx.IDENT().getText()
                    + " ja declarado anteriormente");
        } else {
            // Adiciona o identificador global à tabela de símbolos
            escopoAtual.adicionar(ctx.IDENT().getText(), TabelaDeSimbolos.TipoAlguma.TIPO);
        }
        return super.visitDeclaracao_global(ctx);
    }

    @Override
    public Void visitTipo_basico_ident(AlgumaParser.Tipo_basico_identContext ctx) {
        // Verifica se o tipo básico identificado existe nos escopos
        if (ctx.IDENT() != null) {
            boolean tipoDeclarado = escopos.percorrerEscoposAninhados().stream()
                .anyMatch(escopo -> escopo.existe(ctx.IDENT().getText()));
            if (!tipoDeclarado) {
                AlgumaSemanticoUtils.registrarErroSemantico(ctx.start, "tipo " + ctx.IDENT().getText()
                        + " nao declarado");
            }
        }
        return super.visitTipo_basico_ident(ctx);
    }

    @Override
    public Void visitIdentificador(AlgumaParser.IdentificadorContext ctx) {
        // Verifica se o identificador existe nos escopos
        boolean identificadorDeclarado = escopos.percorrerEscoposAninhados().stream()
            .anyMatch(escopo -> escopo.existe(ctx.IDENT(0).getText()));
        if (!identificadorDeclarado) {
            AlgumaSemanticoUtils.registrarErroSemantico(ctx.start, "identificador " + ctx.IDENT(0).getText()
                    + " nao declarado");
        }
        return super.visitIdentificador(ctx);
    }

    // Verifica se a atribuição é válida
    @Override
    public Void visitCmdAtribuicao(AlgumaParser.CmdAtribuicaoContext ctx) {
        // Analisa o tipo da expressão
        TabelaDeSimbolos.TipoAlguma tipoExpressao = AlgumaSemanticoUtils.analisarExpressao(escopos, ctx.expressao());
        boolean error = false;

        // Constrói o nome da variável a partir do IdentificadorContext
        StringBuilder nomeVar = new StringBuilder();
        AlgumaParser.IdentificadorContext identificadorCtx = ctx.identificador();
        for (int i = 0; i < identificadorCtx.IDENT().size(); i++) {
            nomeVar.append(identificadorCtx.IDENT(i).getText());
            if (i != identificadorCtx.IDENT().size() - 1) {
                nomeVar.append(".");
            }
        }

        // Verifica a compatibilidade dos tipos
        if (tipoExpressao != TabelaDeSimbolos.TipoAlguma.INVALIDO) {
            boolean existeVariavel = false;
            for (TabelaDeSimbolos escopo : escopos.percorrerEscoposAninhados()) {
                if (escopo.existe(nomeVar.toString())) {
                    existeVariavel = true;
                    TabelaDeSimbolos.TipoAlguma tipoVariavel = AlgumaSemanticoUtils.analisarIdentificador(escopos, identificadorCtx);
                    if (!saoTiposCompativeis(tipoVariavel, tipoExpressao)) {
                        error = true;
                    }
                }
            }
            if (!existeVariavel) {
                error = true;
            }
        } else {
            error = true;
        }

        // Registra erro de atribuição, se houver
        if (error) {
            AlgumaSemanticoUtils.registrarErroSemantico(ctx.identificador().start, "atribuicao nao compativel para " + nomeVar.toString());
        }

        return super.visitCmdAtribuicao(ctx);
    }

    // Verifica se os tipos são compatíveis para atribuição
    private boolean saoTiposCompativeis(TabelaDeSimbolos.TipoAlguma tipoVariavel, TabelaDeSimbolos.TipoAlguma tipoExpressao) {
        boolean expNumeric = tipoExpressao == TabelaDeSimbolos.TipoAlguma.INTEIRO || tipoExpressao == TabelaDeSimbolos.TipoAlguma.REAL;
        boolean varNumeric = tipoVariavel == TabelaDeSimbolos.TipoAlguma.INTEIRO || tipoVariavel == TabelaDeSimbolos.TipoAlguma.REAL;
        return (varNumeric && expNumeric) || tipoVariavel == tipoExpressao;
    }

    // Determina o tipo de uma variável ou constante
    private TabelaDeSimbolos.TipoAlguma determinarTipo(String tipo) {
        switch (tipo) {
            case "literal":
                return TabelaDeSimbolos.TipoAlguma.CADEIA;
            case "inteiro":
                return TabelaDeSimbolos.TipoAlguma.INTEIRO;
            case "real":
                return TabelaDeSimbolos.TipoAlguma.REAL;
            case "logico":
                return TabelaDeSimbolos.TipoAlguma.LOGICO;
            default:
                return TabelaDeSimbolos.TipoAlguma.INVALIDO;
        }
    }
}
