/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.dc.compiladores.la_t5_compiladores;

import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.Token;

public class AlgumaSemanticoUtils {
    // Lista para armazenar os erros semânticos encontrados
    public static List<String> errosSemanticos = new ArrayList<>();

    // Adiciona um erro semântico com a linha e a mensagem fornecidas
    public static void registrarErroSemantico(Token t, String mensagem) {
        errosSemanticos.add(String.format("Linha %d: %s", t.getLine(), mensagem));
    }

    // Analisa a expressão e determina seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarExpressao(Escopos escopos, AlgumaParser.ExpressaoContext ctx) {
        TabelaDeSimbolos.TipoAlguma tipoResultado = null;
        // Percorre todos os termos lógicos presentes na expressão
        for (AlgumaParser.Termo_logicoContext termoLogico : ctx.termo_logico()) {
            // Realiza a verificação semântica do termo lógico
            TabelaDeSimbolos.TipoAlguma tipoTermo = analisarTermoLogico(escopos, termoLogico); // Alterado para analisarTermoLogico
            // Atualiza o tipo de retorno da expressão com base no tipo do termo lógico
            if (tipoResultado == null) {
                tipoResultado = tipoTermo;
            } else if (tipoResultado != tipoTermo && tipoTermo != TabelaDeSimbolos.TipoAlguma.INVALIDO) {
                tipoResultado = TabelaDeSimbolos.TipoAlguma.INVALIDO;
            }
        }
        return tipoResultado;
    }

    // Analisa o termo lógico e determina seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarTermoLogico(Escopos escopos, AlgumaParser.Termo_logicoContext ctx) {
        TabelaDeSimbolos.TipoAlguma tipoResultado = null;
        // Percorre todos os fatores lógicos presentes no termo lógico
        for (AlgumaParser.Fator_logicoContext fatorLogico : ctx.fator_logico()) {
            // Realiza a verificação semântica do fator lógico
            TabelaDeSimbolos.TipoAlguma tipoFator = analisarFatorLogico(escopos, fatorLogico);
            // Atualiza o tipo de retorno do termo lógico com base no tipo do fator lógico
            if (tipoResultado == null) {
                tipoResultado = tipoFator;
            } else if (tipoResultado != tipoFator && tipoFator != TabelaDeSimbolos.TipoAlguma.INVALIDO) {
                tipoResultado = TabelaDeSimbolos.TipoAlguma.INVALIDO;
            }
        }
        return tipoResultado;
    }

    // Analisa o fator lógico e determina seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarFatorLogico(Escopos escopos, AlgumaParser.Fator_logicoContext ctx) {
        return analisarParcelaLogica(escopos, ctx.parcela_logica());
    }

    // Analisa a parcela lógica e determina seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarParcelaLogica(Escopos escopos, AlgumaParser.Parcela_logicaContext ctx) {
        TabelaDeSimbolos.TipoAlguma tipoResultado = null;
        // Verifica se a parcela lógica contém uma expressão relacional
        if (ctx.exp_relacional() != null) {
            // Verifica a expressão relacional
            tipoResultado = analisarExpressaoRelacional(escopos, ctx.exp_relacional());
        } else {
            // Caso contrário, o tipo de retorno é lógico
            tipoResultado = TabelaDeSimbolos.TipoAlguma.LOGICO;
        }
        return tipoResultado;
    }

    // Analisa a expressão relacional e determina seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarExpressaoRelacional(Escopos escopos, AlgumaParser.Exp_relacionalContext ctx) {
        TabelaDeSimbolos.TipoAlguma tipoResultado = null;
        // Verifica se a expressão relacional possui um operador relacional
        if (ctx.op_relacional() != null) {
            // Percorre as expressões aritméticas presentes na expressão relacional
            for (AlgumaParser.Exp_aritmeticaContext expAritmetica : ctx.exp_aritmetica()) {
                // Verifica a expressão aritmética
                TabelaDeSimbolos.TipoAlguma tipoExp = analisarExpressaoAritmetica(escopos, expAritmetica);
                Boolean expNumeric = tipoExp == TabelaDeSimbolos.TipoAlguma.REAL || tipoExp == TabelaDeSimbolos.TipoAlguma.INTEIRO;
                Boolean resultadoNumeric = tipoResultado == TabelaDeSimbolos.TipoAlguma.REAL || tipoResultado == TabelaDeSimbolos.TipoAlguma.INTEIRO;
                // Atualiza o tipo de retorno da expressão relacional com base no tipo da expressão aritmética
                if (tipoResultado == null) {
                    tipoResultado = tipoExp;
                } else if (!(expNumeric && resultadoNumeric) && tipoExp != tipoResultado) {
                    tipoResultado = TabelaDeSimbolos.TipoAlguma.INVALIDO;
                }
            }
            // Se o tipo de retorno da expressão relacional não for inválido, ele é do tipo lógico
            if (tipoResultado != TabelaDeSimbolos.TipoAlguma.INVALIDO) {
                tipoResultado = TabelaDeSimbolos.TipoAlguma.LOGICO;
            }
        } else {
            // Caso contrário, a expressão relacional é reduzida para uma expressão aritmética
            tipoResultado = analisarExpressaoAritmetica(escopos, ctx.exp_aritmetica(0));
        }
        return tipoResultado;
    }

    // Analisa a expressão aritmética e determina seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarExpressaoAritmetica(Escopos escopos, AlgumaParser.Exp_aritmeticaContext ctx) {
        TabelaDeSimbolos.TipoAlguma tipoResultado = null;
        // Percorre os termos presentes na expressão aritmética
        for (AlgumaParser.TermoContext termo : ctx.termo()) {
            // Verifica o termo
            TabelaDeSimbolos.TipoAlguma tipoTermo = analisarTermo(escopos, termo);
            // Atualiza o tipo de retorno da expressão aritmética com base no tipo do termo
            if (tipoResultado == null) {
                tipoResultado = tipoTermo;
            } else if (tipoResultado != tipoTermo && tipoTermo != TabelaDeSimbolos.TipoAlguma.INVALIDO) {
                tipoResultado = TabelaDeSimbolos.TipoAlguma.INVALIDO;
            }
        }
        return tipoResultado;
    }

    // Analisa o termo e determina seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarTermo(Escopos escopos, AlgumaParser.TermoContext ctx) {
        TabelaDeSimbolos.TipoAlguma tipoResultado = null;
        // Percorre os fatores presentes no termo
        for (AlgumaParser.FatorContext fator : ctx.fator()) {
            // Verifica o fator
            TabelaDeSimbolos.TipoAlguma tipoFator = analisarFator(escopos, fator);
            Boolean fatorNumeric = tipoFator == TabelaDeSimbolos.TipoAlguma.REAL || tipoFator == TabelaDeSimbolos.TipoAlguma.INTEIRO;
            Boolean resultadoNumeric = tipoResultado == TabelaDeSimbolos.TipoAlguma.REAL || tipoResultado == TabelaDeSimbolos.TipoAlguma.INTEIRO;
            // Atualiza o tipo de retorno do termo com base no tipo do fator
            if (tipoResultado == null) {
                tipoResultado = tipoFator;
            } else if (!(fatorNumeric && resultadoNumeric) && tipoFator != tipoResultado) {
                tipoResultado = TabelaDeSimbolos.TipoAlguma.INVALIDO;
            }
        }
        return tipoResultado;
    }

    // Analisa o fator e determina seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarFator(Escopos escopos, AlgumaParser.FatorContext ctx) {
        TabelaDeSimbolos.TipoAlguma tipoResultado = null;

        for (AlgumaParser.ParcelaContext parcela : ctx.parcela()) {
            TabelaDeSimbolos.TipoAlguma tipoParcela = analisarParcela(escopos, parcela);
            if (tipoResultado == null) {
                tipoResultado = tipoParcela;
            } else if (tipoResultado != tipoParcela && tipoParcela != TabelaDeSimbolos.TipoAlguma.INVALIDO) {
                tipoResultado = TabelaDeSimbolos.TipoAlguma.INVALIDO;
            }
        }
        return tipoResultado;
    }

    // Analisa a parcela e determina seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarParcela(Escopos escopos, AlgumaParser.ParcelaContext ctx) {
        TabelaDeSimbolos.TipoAlguma tipoResultado = TabelaDeSimbolos.TipoAlguma.INVALIDO;

        if(ctx.parcela_nao_unario() != null){
            tipoResultado = analisarParcelaNaoUnaria(escopos, ctx.parcela_nao_unario());
        }
        else {
            tipoResultado = analisarParcelaUnaria(escopos, ctx.parcela_unario());
        }
        return tipoResultado;
    }

    // Analisa a parcela não unária e determina seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarParcelaNaoUnaria(Escopos escopos, AlgumaParser.Parcela_nao_unarioContext ctx) {
        if (ctx.identificador() != null) {
            return analisarIdentificador(escopos, ctx.identificador());
        }
        return TabelaDeSimbolos.TipoAlguma.CADEIA;
    }

    // Analisa o identificador e determina seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarIdentificador(Escopos escopos, AlgumaParser.IdentificadorContext ctx) {
        String nomeVariavel = "";
        TabelaDeSimbolos.TipoAlguma tipoResultado = TabelaDeSimbolos.TipoAlguma.INVALIDO;
        for(int i = 0; i < ctx.IDENT().size(); i++){
            nomeVariavel += ctx.IDENT(i).getText();
            if(i != ctx.IDENT().size() - 1){
                nomeVariavel += ".";
            }
        }
        for(TabelaDeSimbolos tabela : escopos.percorrerEscoposAninhados()){
            if (tabela.existe(nomeVariavel)) {
                tipoResultado = verificarTipoVariavel(escopos, nomeVariavel);
            }
        }
        return tipoResultado;
    }

    // Analisa a parcela unária e determina seu tipo
    public static TabelaDeSimbolos.TipoAlguma analisarParcelaUnaria(Escopos escopos, AlgumaParser.Parcela_unarioContext ctx) {
        if (ctx.NUM_INT() != null) {
            return TabelaDeSimbolos.TipoAlguma.INTEIRO;
        }
        if (ctx.NUM_REAL() != null) {
            return TabelaDeSimbolos.TipoAlguma.REAL;
        }
        if(ctx.identificador() != null){
            return analisarIdentificador(escopos, ctx.identificador());
        }
        if (ctx.IDENT() != null) {
            return verificarTipoVariavel(escopos, ctx.IDENT().getText());
        } else {
            TabelaDeSimbolos.TipoAlguma tipoResultado = null;
            for (AlgumaParser.ExpressaoContext exp : ctx.expressao()) {
                TabelaDeSimbolos.TipoAlguma tipoExp = analisarExpressao(escopos, exp);
                if (tipoResultado == null) {
                    tipoResultado = tipoExp;
                } else if (tipoResultado != tipoExp && tipoExp != TabelaDeSimbolos.TipoAlguma.INVALIDO) {
                    tipoResultado = TabelaDeSimbolos.TipoAlguma.INVALIDO;
                }
            }
            return tipoResultado;
        }
    }

    // Verificar o tipo de uma variável pelo nome
    public static TabelaDeSimbolos.TipoAlguma verificarTipoVariavel(Escopos escopos, String nomeVariavel) {
        TabelaDeSimbolos.TipoAlguma tipo = TabelaDeSimbolos.TipoAlguma.INVALIDO;
        for(TabelaDeSimbolos tabela : escopos.percorrerEscoposAninhados()){
            if(tabela.existe(nomeVariavel)){
                return tabela.verificar(nomeVariavel);
            }
        }
        return tipo;
    }

    // Combina tipos para expressões lógicas
    public static TabelaDeSimbolos.TipoAlguma combinarTipos(TabelaDeSimbolos.TipoAlguma tipo1, TabelaDeSimbolos.TipoAlguma tipo2) {
        if (tipo1 == null) return tipo2;
        if (tipo2 == null) return tipo1;
        if (tipo1 == tipo2) return tipo1;
        if ((tipo1 == TabelaDeSimbolos.TipoAlguma.INTEIRO && tipo2 == TabelaDeSimbolos.TipoAlguma.REAL) ||
            (tipo1 == TabelaDeSimbolos.TipoAlguma.REAL && tipo2 == TabelaDeSimbolos.TipoAlguma.INTEIRO)) {
            return TabelaDeSimbolos.TipoAlguma.REAL;
        }
        return TabelaDeSimbolos.TipoAlguma.INVALIDO;
    }

    // Combina tipos para registros e ponteiros
    public static TabelaDeSimbolos.TipoAlguma combinarTiposNumericos(TabelaDeSimbolos.TipoAlguma tipo1, TabelaDeSimbolos.TipoAlguma tipo2) {
        if (tipo1 == TabelaDeSimbolos.TipoAlguma.INTEIRO && tipo2 == TabelaDeSimbolos.TipoAlguma.REAL) {
            return TabelaDeSimbolos.TipoAlguma.REAL;
        }
        if (tipo1 == TabelaDeSimbolos.TipoAlguma.REAL && tipo2 == TabelaDeSimbolos.TipoAlguma.INTEIRO) {
            return TabelaDeSimbolos.TipoAlguma.REAL;
        }
        return combinarTipos(tipo1, tipo2);
    }
    
    public static TabelaDeSimbolos.TipoAlguma getTipoAlguma(String val) {
        TabelaDeSimbolos.TipoAlguma tipo = null;
        if(val.equals("literal")){
            tipo = TabelaDeSimbolos.TipoAlguma.CADEIA;
        } else if(val.equals("inteiro")){
            tipo = TabelaDeSimbolos.TipoAlguma.INTEIRO;
        } else if(val.equals("real")){
            tipo = TabelaDeSimbolos.TipoAlguma.REAL;
        } else {
            tipo = TabelaDeSimbolos.TipoAlguma.LOGICO;
        }
        
        return tipo;
    }

    
    public static String obterSimboloC(TabelaDeSimbolos.TipoAlguma tipoAlguma) {
        // Mapeia os tipos Alguma para seus símbolos C correspondentes
        switch (tipoAlguma) {
            case CADEIA:
                return "s";
            case INTEIRO:
                return "d";
            case REAL:
                return "f";
            default:
                return null; // Retorna null se o tipo não estiver mapeado
        }
    }
    
    public static String obterTipoC(String tipoAlguma) {
        // Mapeia os tipos Alguma para seus tipos C correspondentes
        switch (tipoAlguma) {
            case "literal":
                return "char";
            case "inteiro":
                return "int";
            case "real":
                return "float";
            default:
                return null; // Retorna null se o tipo não estiver mapeado
        }
    }


}
