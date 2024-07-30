/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.dc.compiladores.la_t3_compiladores;

import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.Token;

public class AlgumaSemanticoUtils {
    // Lista de erros semânticos detectados
    public static List<String> listaErrosSemanticos = new ArrayList<>();

    // Registra um erro semântico com a linha e a mensagem fornecidas
    public static void registrarErroSemantico(Token t, String mensagem) {
        listaErrosSemanticos.add(String.format("Linha %d: %s", t.getLine(), mensagem));
    }

    // Analisar a expressão e determinar seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarExpressao(Escopos escopos, AlgumaParser.ExpressaoContext ctx) {
        TabelaDeSimbolos.TipoAlguma tipoFinal = null;
        // Itera sobre cada termo lógico na expressão
        for (AlgumaParser.Termo_logicoContext termo : ctx.termo_logico()) {
            TabelaDeSimbolos.TipoAlguma tipoAtual = analisarTermoLogico(escopos, termo);
            tipoFinal = combinarTipos(tipoFinal, tipoAtual);
        }
        return tipoFinal;
    }

    // Analisar o termo lógico e determinar seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarTermoLogico(Escopos escopos, AlgumaParser.Termo_logicoContext ctx) {
        TabelaDeSimbolos.TipoAlguma tipoFinal = null;
        // Itera sobre cada fator lógico no termo lógico
        for (AlgumaParser.Fator_logicoContext fator : ctx.fator_logico()) {
            TabelaDeSimbolos.TipoAlguma tipoAtual = analisarFatorLogico(escopos, fator);
            tipoFinal = combinarTipos(tipoFinal, tipoAtual);
        }
        return tipoFinal;
    }

    // Analisar o fator lógico e determinar seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarFatorLogico(Escopos escopos, AlgumaParser.Fator_logicoContext ctx) {
        return analisarParcelaLogica(escopos, ctx.parcela_logica());
    }

    // Analisar a parcela lógica e determinar seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarParcelaLogica(Escopos escopos, AlgumaParser.Parcela_logicaContext ctx) {
        if (ctx.exp_relacional() != null) {
            return analisarExpressaoRelacional(escopos, ctx.exp_relacional());
        } else {
            return TabelaDeSimbolos.TipoAlguma.LOGICO;
        }
    }

    // Analisar a expressão relacional e determinar seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarExpressaoRelacional(Escopos escopos, AlgumaParser.Exp_relacionalContext ctx) {
        TabelaDeSimbolos.TipoAlguma tipoFinal = null;
        if (ctx.op_relacional() != null) {
            // Itera sobre cada expressão aritmética na expressão relacional
            for (AlgumaParser.Exp_aritmeticaContext exp : ctx.exp_aritmetica()) {
                TabelaDeSimbolos.TipoAlguma tipoAtual = analisarExpressaoAritmetica(escopos, exp);
                tipoFinal = combinarTiposNumericos(tipoFinal, tipoAtual);
            }
            if (tipoFinal != TabelaDeSimbolos.TipoAlguma.INVALIDO) {
                tipoFinal = TabelaDeSimbolos.TipoAlguma.LOGICO;
            }
        } else {
            tipoFinal = analisarExpressaoAritmetica(escopos, ctx.exp_aritmetica(0));
        }
        return tipoFinal;
    }

    // Analisar a expressão aritmética e determinar seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarExpressaoAritmetica(Escopos escopos, AlgumaParser.Exp_aritmeticaContext ctx) {
        TabelaDeSimbolos.TipoAlguma tipoFinal = null;
        // Itera sobre cada termo na expressão aritmética
        for (AlgumaParser.TermoContext termo : ctx.termo()) {
            TabelaDeSimbolos.TipoAlguma tipoAtual = analisarTermo(escopos, termo);
            tipoFinal = combinarTipos(tipoFinal, tipoAtual);
        }
        return tipoFinal;
    }

    // Analisar o termo e determinar seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarTermo(Escopos escopos, AlgumaParser.TermoContext ctx) {
        TabelaDeSimbolos.TipoAlguma tipoFinal = null;
        // Itera sobre cada fator no termo
        for (AlgumaParser.FatorContext fator : ctx.fator()) {
            TabelaDeSimbolos.TipoAlguma tipoAtual = analisarFator(escopos, fator);
            tipoFinal = combinarTiposNumericos(tipoFinal, tipoAtual);
        }
        return tipoFinal;
    }

    // Analisar o fator e determinar seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarFator(Escopos escopos, AlgumaParser.FatorContext ctx) {
        TabelaDeSimbolos.TipoAlguma tipoFinal = null;
        // Itera sobre cada parcela no fator
        for (AlgumaParser.ParcelaContext parcela : ctx.parcela()) {
            TabelaDeSimbolos.TipoAlguma tipoAtual = analisarParcela(escopos, parcela);
            tipoFinal = combinarTipos(tipoFinal, tipoAtual);
        }
        return tipoFinal;
    }

    // Analisar a parcela e determinar seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarParcela(Escopos escopos, AlgumaParser.ParcelaContext ctx) {
        if (ctx.parcela_nao_unario() != null) {
            return analisarParcelaNaoUnaria(escopos, ctx.parcela_nao_unario());
        } else {
            return analisarParcelaUnaria(escopos, ctx.parcela_unario());
        }
    }

    // Analisar a parcela não unária e determinar seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarParcelaNaoUnaria(Escopos escopos, AlgumaParser.Parcela_nao_unarioContext ctx) {
        if (ctx.identificador() != null) {
            return analisarIdentificador(escopos, ctx.identificador());
        }
        return TabelaDeSimbolos.TipoAlguma.CADEIA;
    }

    // Analisar o identificador e determinar seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarIdentificador(Escopos escopos, AlgumaParser.IdentificadorContext ctx) {
        StringBuilder nomeVar = new StringBuilder();
        for (int i = 0; i < ctx.IDENT().size(); i++) {
            nomeVar.append(ctx.IDENT(i).getText());
            if (i != ctx.IDENT().size() - 1) {
                nomeVar.append(".");
            }
        }

        // Verifica se o identificador existe em algum escopo aninhado
        for (TabelaDeSimbolos tabela : escopos.percorrerEscoposAninhados()) {
            if (tabela.existe(nomeVar.toString())) {
                return verificarNomeVar(escopos, nomeVar.toString());
            }
        }
        return TabelaDeSimbolos.TipoAlguma.INVALIDO;
    }

    // Analisar a parcela unária e determinar seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarParcelaUnaria(Escopos escopos, AlgumaParser.Parcela_unarioContext ctx) {
        if (ctx.NUM_INT() != null) {
            return TabelaDeSimbolos.TipoAlguma.INTEIRO;
        }
        if (ctx.NUM_REAL() != null) {
            return TabelaDeSimbolos.TipoAlguma.REAL;
        }
        if (ctx.identificador() != null) {
            return analisarIdentificador(escopos, ctx.identificador());
        }
        if (ctx.IDENT() != null) {
            TabelaDeSimbolos.TipoAlguma tipoFinal = verificarNomeVar(escopos, ctx.IDENT().getText());
            // Itera sobre cada expressão na parcela unária
            for (AlgumaParser.ExpressaoContext exp : ctx.expressao()) {
                TabelaDeSimbolos.TipoAlguma tipoAtual = analisarExpressao(escopos, exp);
                tipoFinal = combinarTipos(tipoFinal, tipoAtual);
            }
            return tipoFinal;
        } else {
            TabelaDeSimbolos.TipoAlguma tipoFinal = null;
            // Itera sobre cada expressão na parcela unária
            for (AlgumaParser.ExpressaoContext exp : ctx.expressao()) {
                TabelaDeSimbolos.TipoAlguma tipoAtual = analisarExpressao(escopos, exp);
                tipoFinal = combinarTipos(tipoFinal, tipoAtual);
            }
            return tipoFinal;
        }
    }

    // Verificar o tipo de uma variável pelo nome
    public static TabelaDeSimbolos.TipoAlguma verificarNomeVar(Escopos escopos, String nomeVar) {
        for (TabelaDeSimbolos tabela : escopos.percorrerEscoposAninhados()) {
            TabelaDeSimbolos.TipoAlguma tipo = tabela.verificar(nomeVar);
            if (tipo != null) {
                return tipo;
            }
        }
        return TabelaDeSimbolos.TipoAlguma.INVALIDO;
    }

    // Combina dois tipos e retorna o tipo resultante
    private static TabelaDeSimbolos.TipoAlguma combinarTipos(TabelaDeSimbolos.TipoAlguma tipo1, TabelaDeSimbolos.TipoAlguma tipo2) {
        if (tipo1 == null) {
            return tipo2;
        }
        if (tipo2 == null || tipo1 == tipo2) {
            return tipo1;
        }
        return TabelaDeSimbolos.TipoAlguma.INVALIDO;
    }

    // Combina dois tipos numéricos e retorna o tipo resultante
    private static TabelaDeSimbolos.TipoAlguma combinarTiposNumericos(TabelaDeSimbolos.TipoAlguma tipo1, TabelaDeSimbolos.TipoAlguma tipo2) {
        if (tipo1 == null) {
            return tipo2;
        }
        boolean tipo1Numerico = tipo1 == TabelaDeSimbolos.TipoAlguma.REAL || tipo1 == TabelaDeSimbolos.TipoAlguma.INTEIRO;
        boolean tipo2Numerico = tipo2 == TabelaDeSimbolos.TipoAlguma.REAL || tipo2 == TabelaDeSimbolos.TipoAlguma.INTEIRO;
        if (tipo1Numerico && tipo2Numerico) {
            return tipo1 == TabelaDeSimbolos.TipoAlguma.REAL || tipo2 == TabelaDeSimbolos.TipoAlguma.REAL ? TabelaDeSimbolos.TipoAlguma.REAL : TabelaDeSimbolos.TipoAlguma.INTEIRO;
        }
        return TabelaDeSimbolos.TipoAlguma.INVALIDO;
    }
}
