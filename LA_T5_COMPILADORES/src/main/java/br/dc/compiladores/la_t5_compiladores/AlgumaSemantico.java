/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.dc.compiladores.la_t5_compiladores;

import br.dc.compiladores.la_t5_compiladores.TabelaDeSimbolos.EntradaTabelaDeSimbolos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.antlr.v4.runtime.tree.TerminalNode;

public class AlgumaSemantico extends AlgumaBaseVisitor {
    
    //Criando o objeto do escopo
    Escopos escopos = new Escopos(TabelaDeSimbolos.TipoAlguma.VOID);

    @Override
    public Object visitPrograma(AlgumaParser.ProgramaContext ctx) {
        // Chama o método visitPrograma da superclasse para manter o comportamento padrão
        return super.visitPrograma(ctx);
    }

    // Verifica se a constante foi declarada anteriormente, evitando alterações posteriores
    @Override
    public Object visitDeclaracao_const(AlgumaParser.Declaracao_constContext ctx) {
        // Obtém o escopo atual da tabela de símbolos
        TabelaDeSimbolos escopoAtual = escopos.obterEscopoAtual();

        // Verifica se a constante já foi declarada no escopo atual
        if (escopoAtual.existe(ctx.IDENT().getText())) {
            // Registra um erro semântico se a constante já foi declarada
            AlgumaSemanticoUtils.registrarErroSemantico(ctx.start, "constante " + ctx.IDENT().getText()
                    + " ja foi declarada anteriormente");
        } else {
            // Define o tipo padrão como INTEIRO
            TabelaDeSimbolos.TipoAlguma tipo = TabelaDeSimbolos.TipoAlguma.INTEIRO;

            // Determina o tipo a partir do tipo básico declarado
            TabelaDeSimbolos.TipoAlguma tipoDeterminado = determinarTipo(ctx.tipo_basico().getText());
            if (tipoDeterminado != null) {
                tipo = tipoDeterminado;
            }

            // Adiciona a constante ao escopo atual com o tipo determinado
            escopoAtual.adicionar(ctx.IDENT().getText(), tipo, TabelaDeSimbolos.Structure.CONST);
        }

        // Chama o método visitDeclaracao_const da superclasse para manter o comportamento padrão
        return super.visitDeclaracao_const(ctx);
    }


    // Verifica se o tipo foi declarado mais de uma vez no mesmo escopo
    @Override
    public Object visitDeclaracao_tipo(AlgumaParser.Declaracao_tipoContext ctx) {
        // Obtém o escopo atual da tabela de símbolos
        TabelaDeSimbolos escopoAtual = escopos.obterEscopoAtual();

        // Checa se o tipo já existe no escopo atual
        if (escopoAtual.existe(ctx.IDENT().getText())) {
            // Registra um erro semântico se o tipo já foi declarado
            AlgumaSemanticoUtils.registrarErroSemantico(ctx.start, "tipo " + ctx.IDENT().getText()
                    + " foi declarado mais de uma vez no mesmo escopo");
        } else {
            // Determina o tipo a partir da declaração
            TabelaDeSimbolos.TipoAlguma tipo = determinarTipo(ctx.tipo().getText());

            // Adiciona o tipo ao escopo se for válido
            if (tipo != null) {
                escopoAtual.adicionar(ctx.IDENT().getText(), tipo, TabelaDeSimbolos.Structure.TIPO);
            } else if (ctx.tipo().registro() != null) {
                // Cria uma lista para armazenar as variáveis do registro
                ArrayList<TabelaDeSimbolos.EntradaTabelaDeSimbolos> variaveisRegistro = new ArrayList<>();

                // Processa cada variável do registro
                for (AlgumaParser.VariavelContext variavelContext : ctx.tipo().registro().variavel()) {
                    TabelaDeSimbolos.TipoAlguma tipoVariavel = determinarTipo(variavelContext.tipo().getText());

                    for (AlgumaParser.IdentificadorContext identificadorContext : variavelContext.identificador()) {
                        // Adiciona cada variável do registro à lista
                        variaveisRegistro.add(escopoAtual.new EntradaTabelaDeSimbolos(identificadorContext.getText(), tipoVariavel, TabelaDeSimbolos.Structure.TIPO));
                    }
                }

                // Verifica se o identificador do tipo já foi declarado
                if (escopoAtual.existe(ctx.IDENT().getText())) {
                    AlgumaSemanticoUtils.registrarErroSemantico(ctx.start, "identificador " + ctx.IDENT().getText()
                            + " ja foi declarado anteriormente");
                } else {
                    // Adiciona o tipo de registro ao escopo
                    escopoAtual.adicionar(ctx.IDENT().getText(), TabelaDeSimbolos.TipoAlguma.REGISTRO, TabelaDeSimbolos.Structure.TIPO);
                }

                // Adiciona as variáveis do registro ao escopo
                for (TabelaDeSimbolos.EntradaTabelaDeSimbolos entrada : variaveisRegistro) {
                    String nomeVariavel = ctx.IDENT().getText() + '.' + entrada.nome;
                    if (escopoAtual.existe(nomeVariavel)) {
                        AlgumaSemanticoUtils.registrarErroSemantico(ctx.start, "identificador " + nomeVariavel
                                + " ja foi declarado anteriormente");
                    } else {
                        // Adiciona a variável ao escopo
                        escopoAtual.adicionar(entrada);
                        escopoAtual.adicionar(ctx.IDENT().getText(), entrada);
                    }
                }
            }

            // Adiciona o tipo ao escopo, considerando o tipo determinado anteriormente
            TabelaDeSimbolos.TipoAlguma tipoDeterminado = determinarTipo(ctx.tipo().getText());
            escopoAtual.adicionar(ctx.IDENT().getText(), tipoDeterminado, TabelaDeSimbolos.Structure.TIPO);
        }

        // Chama o método visitDeclaracao_tipo da superclasse para manter o comportamento padrão
        return super.visitDeclaracao_tipo(ctx);
    }

    // Verifica se a variável declarada já foi registrada anteriormente no escopo atual
   @Override
   public Object visitDeclaracao_var(AlgumaParser.Declaracao_varContext ctx) {
       // Obtém o escopo atual da tabela de símbolos
       TabelaDeSimbolos escopoAtual = escopos.obterEscopoAtual();

       // Itera sobre cada identificador da variável declarada
       for (AlgumaParser.IdentificadorContext id : ctx.variavel().identificador()) {
           String nomeId = "";
           int i = 0;

           // Concatena os identificadores com pontos, caso haja mais de um identificador
           for (TerminalNode ident : id.IDENT()) {
               if (i++ > 0) {
                   nomeId += ".";
               }
               nomeId += ident.getText(); // Adiciona o nome do identificador ao nomeId
           }

           // Verifica se o identificador já foi declarado no escopo atual
           if (escopoAtual.existe(nomeId)) {
               // Registra um erro semântico se o identificador já foi declarado
               AlgumaSemanticoUtils.registrarErroSemantico(id.start, "identificador " + nomeId
                       + " ja declarado anteriormente");
           } else {
               // Determina o tipo da variável com base no texto do tipo
               TabelaDeSimbolos.TipoAlguma tipo = determinarTipo(ctx.variavel().tipo().getText());

               if (tipo != null) {
                   // Adiciona a variável ao escopo com o tipo determinado
                   escopoAtual.adicionar(nomeId, tipo, TabelaDeSimbolos.Structure.VAR);
               } else {
                   // Verifica se há um tipo extendido e obtém seu identificador
                   TerminalNode identTipo = ctx.variavel().tipo() != null
                           && ctx.variavel().tipo().tipo_estendido() != null
                           && ctx.variavel().tipo().tipo_estendido().tipo_basico_ident() != null
                           && ctx.variavel().tipo().tipo_estendido().tipo_basico_ident().IDENT() != null
                           ? ctx.variavel().tipo().tipo_estendido().tipo_basico_ident().IDENT() : null;

                   if (identTipo != null) {
                       // Cria uma lista para armazenar as variáveis do registro, se for o caso
                       ArrayList<TabelaDeSimbolos.EntradaTabelaDeSimbolos> regVars = null;
                       boolean found = false;

                       // Procura o tipo do registro nos escopos aninhados
                       for (TabelaDeSimbolos t : escopos.percorrerEscoposAninhados()) {
                           if (!found && t.existe(identTipo.getText())) {
                               regVars = t.retornaTipo(identTipo.getText());
                               found = true;
                           }
                       }

                       // Verifica novamente se o identificador já foi declarado no escopo atual
                       if (escopoAtual.existe(nomeId)) {
                           AlgumaSemanticoUtils.registrarErroSemantico(id.start, "identificador " + nomeId
                                   + " ja declarado anteriormente");
                       } else {
                           // Adiciona o identificador como um tipo de registro ao escopo atual
                           escopoAtual.adicionar(nomeId, TabelaDeSimbolos.TipoAlguma.REGISTRO, TabelaDeSimbolos.Structure.VAR);

                           // Adiciona cada variável do registro ao escopo com o nome completo (incluindo prefixo)
                           for (TabelaDeSimbolos.EntradaTabelaDeSimbolos s : regVars) {
                               escopoAtual.adicionar(nomeId + "." + s.nome, s.tipo, TabelaDeSimbolos.Structure.VAR);
                           }
                       }
                   } else if (ctx.variavel().tipo().registro() != null) {
                       // Cria uma lista para variáveis do registro se o tipo for um registro
                       ArrayList<TabelaDeSimbolos.EntradaTabelaDeSimbolos> varReg = new ArrayList<>();

                       // Processa cada variável dentro do registro
                       for (AlgumaParser.VariavelContext va : ctx.variavel().tipo().registro().variavel()) {
                           TabelaDeSimbolos.TipoAlguma tipoReg = determinarTipo(va.tipo().getText());
                           for (AlgumaParser.IdentificadorContext id2 : va.identificador()) {
                               varReg.add(escopoAtual.new EntradaTabelaDeSimbolos(id2.getText(), tipoReg, TabelaDeSimbolos.Structure.VAR));
                           }
                       }

                       // Adiciona o identificador como um registro no escopo atual
                       escopoAtual.adicionar(nomeId, TabelaDeSimbolos.TipoAlguma.REGISTRO, TabelaDeSimbolos.Structure.VAR);

                       // Adiciona as variáveis do registro ao escopo com seus respectivos nomes completos
                       for (TabelaDeSimbolos.EntradaTabelaDeSimbolos re : varReg) {
                           String nameVar = nomeId + '.' + re.nome;
                           if (escopoAtual.existe(nameVar)) {
                               AlgumaSemanticoUtils.registrarErroSemantico(id.start, "identificador " + nameVar
                                       + " ja declarado anteriormente");
                           } else {
                               escopoAtual.adicionar(re);
                               escopoAtual.adicionar(nameVar, re.tipo, TabelaDeSimbolos.Structure.VAR);
                           }
                       }
                   } else {
                       // Caso não seja um tipo extendido ou registro, assume-se que é um inteiro
                       escopoAtual.adicionar(id.getText(), TabelaDeSimbolos.TipoAlguma.INTEIRO, TabelaDeSimbolos.Structure.VAR);
                   }
               }
           }
       }

       // Chama o método visitDeclaracao_var da superclasse para manter o comportamento padrão
       return super.visitDeclaracao_var(ctx);
   }



    // Verifica se a variável global já foi declarada
    @Override
    public Object visitDeclaracao_global(AlgumaParser.Declaracao_globalContext ctx) {
        TabelaDeSimbolos escopoAtual = escopos.obterEscopoAtual();
        Object retorno;

        // Verifica se o identificador já existe no escopo atual
        if (escopoAtual.existe(ctx.IDENT().getText())) {
            AlgumaSemanticoUtils.registrarErroSemantico(ctx.start, ctx.IDENT().getText() + " ja declarado anteriormente");
            retorno = super.visitDeclaracao_global(ctx);
        } else {
            TabelaDeSimbolos.TipoAlguma tipoRetornoFunc = TabelaDeSimbolos.TipoAlguma.VOID;

            // Verifica se é uma função e define o tipo de retorno
            if (ctx.getText().startsWith("funcao")) {
                tipoRetornoFunc = determinarTipo(ctx.tipo_estendido().getText());
                escopoAtual.adicionar(ctx.IDENT().getText(), tipoRetornoFunc, TabelaDeSimbolos.Structure.FUNC);
            } else {
                escopoAtual.adicionar(ctx.IDENT().getText(), tipoRetornoFunc, TabelaDeSimbolos.Structure.PROC);
            }

            // Cria um novo escopo para a função/procedimento
            escopos.criarNovoEscopo(tipoRetornoFunc);
            TabelaDeSimbolos escopoAnterior = escopoAtual;
            escopoAtual = escopos.obterEscopoAtual();

            // Processa os parâmetros, se houver
            if (ctx.parametros() != null) {
                for (AlgumaParser.ParametroContext parametro : ctx.parametros().parametro()) {
                    for (AlgumaParser.IdentificadorContext identificador : parametro.identificador()) {
                        String nomeId = "";
                        int i = 0;

                        // Concatena identificadores com pontos, se necessário
                        for (TerminalNode ident : identificador.IDENT()) {
                            if (i++ > 0) {
                                nomeId += ".";
                            }
                            nomeId += ident.getText();
                        }

                        // Verifica se o identificador já existe no escopo atual
                        if (escopoAtual.existe(nomeId)) {
                            AlgumaSemanticoUtils.registrarErroSemantico(identificador.start, "identificador " + nomeId + " ja declarado anteriormente");
                        } else {
                            TabelaDeSimbolos.TipoAlguma tipo = determinarTipo(parametro.tipo_estendido().getText());

                            if (tipo != null) {
                                EntradaTabelaDeSimbolos entrada = escopoAtual.new EntradaTabelaDeSimbolos(nomeId, tipo, TabelaDeSimbolos.Structure.VAR);
                                escopoAtual.adicionar(entrada);
                                escopoAnterior.adicionar(ctx.IDENT().getText(), entrada);
                            } else {
                                TerminalNode identTipo = parametro.tipo_estendido().tipo_basico_ident() != null && parametro.tipo_estendido().tipo_basico_ident().IDENT() != null
                                        ? parametro.tipo_estendido().tipo_basico_ident().IDENT()
                                        : null;

                                if (identTipo != null) {
                                    ArrayList<TabelaDeSimbolos.EntradaTabelaDeSimbolos> varsRegistro = null;
                                    boolean encontrado = false;

                                    // Busca em escopos aninhados
                                    for (TabelaDeSimbolos t : escopos.percorrerEscoposAninhados()) {
                                        if (!encontrado && t.existe(identTipo.getText())) {
                                            varsRegistro = t.retornaTipo(identTipo.getText());
                                            encontrado = true;
                                        }
                                    }

                                    if (escopoAtual.existe(nomeId)) {
                                        AlgumaSemanticoUtils.registrarErroSemantico(identificador.start, "identificador " + nomeId + " ja declarado anteriormente");
                                    } else {
                                        EntradaTabelaDeSimbolos entrada = escopoAtual.new EntradaTabelaDeSimbolos(nomeId, TabelaDeSimbolos.TipoAlguma.REGISTRO, TabelaDeSimbolos.Structure.VAR);
                                        escopoAtual.adicionar(entrada);
                                        escopoAnterior.adicionar(ctx.IDENT().getText(), entrada);

                                        for (TabelaDeSimbolos.EntradaTabelaDeSimbolos s : varsRegistro) {
                                            escopoAtual.adicionar(nomeId + "." + s.nome, s.tipo, TabelaDeSimbolos.Structure.VAR);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            retorno = super.visitDeclaracao_global(ctx);
            escopos.abandonarEscopo();
        }
        return retorno;
    }


    @Override
    public Object visitTipo_basico_ident(AlgumaParser.Tipo_basico_identContext ctx) {
        if (ctx.IDENT() != null) {
            boolean tipoExiste = false;

            // Percorre os escopos aninhados para verificar se o tipo foi declarado
            for (TabelaDeSimbolos escopo : escopos.percorrerEscoposAninhados()) {
                if (escopo.existe(ctx.IDENT().getText())) {
                    tipoExiste = true;
                    break;  // Interrompe a busca se o tipo for encontrado
                }
            }

            // Se o tipo não foi encontrado em nenhum escopo, registra um erro semântico
            if (!tipoExiste) {
                AlgumaSemanticoUtils.registrarErroSemantico(ctx.start, "tipo " + ctx.IDENT().getText()
                            + " nao declarado");
            }
        }

        return super.visitTipo_basico_ident(ctx);
    }



    //verifica se o identificador foi declarado
    @Override
    public Object visitIdentificador(AlgumaParser.IdentificadorContext ctx) {
        String identificadorCompleto = "";
        int contador = 0;

        // Concatena os identificadores, considerando identificadores compostos (com '.')
        for (TerminalNode id : ctx.IDENT()) {
            if (contador++ > 0)
                identificadorCompleto += ".";
            identificadorCompleto += id.getText();
        }

        boolean identificadorInvalido = true;

        // Percorre os escopos aninhados para verificar se o identificador existe
        for (TabelaDeSimbolos escopo : escopos.percorrerEscoposAninhados()) {
            if (escopo.existe(identificadorCompleto)) {
                identificadorInvalido = false;
                break; // Interrompe a busca após encontrar o identificador
            }
        }

        // Registra um erro semântico se o identificador não foi declarado
        if (identificadorInvalido) {
            AlgumaSemanticoUtils.registrarErroSemantico(ctx.start, "identificador " + identificadorCompleto + " nao declarado");
        }

        // Continua a visita padrão do parse tree
        return super.visitIdentificador(ctx);
    }

    //verifica se a atribuição é válida
    @Override
    public Object visitCmdAtribuicao(AlgumaParser.CmdAtribuicaoContext ctx) {
        // Analisa o tipo da expressão na atribuição
        TabelaDeSimbolos.TipoAlguma tipoExpr = AlgumaSemanticoUtils.analisarExpressao(escopos, ctx.expressao());
        boolean erroNaAtribuicao = false;

        // Verifica se a atribuição utiliza o caractere '^' (para ponteiros)
        String caracterPointer = ctx.getText().charAt(0) == '^' ? "^" : "";
        String nomeVariavel = "";
        int contador = 0;

        // Concatena os identificadores para formar o nome completo da variável
        for (TerminalNode id : ctx.identificador().IDENT()) {
            if (contador++ > 0)
                nomeVariavel += ".";
            nomeVariavel += id.getText();
        }

        // Verifica se a expressão não é inválida e se o tipo é compatível
        if (tipoExpr != TabelaDeSimbolos.TipoAlguma.INVALIDO) {
            boolean encontrado = false;

            // Percorre os escopos para verificar a existência e o tipo da variável
            for (TabelaDeSimbolos escopo : escopos.percorrerEscoposAninhados()) {
                if (escopo.existe(nomeVariavel) && !encontrado) {
                    encontrado = true;
                    TabelaDeSimbolos.TipoAlguma tipoVar = AlgumaSemanticoUtils.verificarTipoVariavel(escopos, nomeVariavel);

                    // Verifica se a variável e a expressão são numéricas (inteiro ou real)
                    boolean variavelNumerica = tipoVar == TabelaDeSimbolos.TipoAlguma.REAL || tipoVar == TabelaDeSimbolos.TipoAlguma.INTEIRO;
                    boolean expressaoNumerica = tipoExpr == TabelaDeSimbolos.TipoAlguma.REAL || tipoExpr == TabelaDeSimbolos.TipoAlguma.INTEIRO;

                    // Se os tipos não forem compatíveis, marca erro
                    if (!(variavelNumerica && expressaoNumerica) && tipoVar != tipoExpr) {
                        erroNaAtribuicao = true;
                    }
                }
            }
        } else {
            // Marca erro se a expressão for inválida
            erroNaAtribuicao = true;
        }

        // Registra um erro semântico se houver problema na atribuição
        if (erroNaAtribuicao) {
            nomeVariavel = ctx.identificador().getText();
            AlgumaSemanticoUtils.registrarErroSemantico(ctx.identificador().start, "atribuicao nao compativel para " + caracterPointer + nomeVariavel);
        }

        // Continua a visita padrão do parse tree
        return super.visitCmdAtribuicao(ctx);
    }
    
    // Verifica se o comando 'retorne' é permitido no escopo atual
    @Override
    public Object visitCmdRetorne(AlgumaParser.CmdRetorneContext ctx) {
        // Se o tipo do escopo atual for VOID, o uso de 'retorne' não é permitido
        if (escopos.obterEscopoAtual().tipo == TabelaDeSimbolos.TipoAlguma.VOID) {
            AlgumaSemanticoUtils.registrarErroSemantico(ctx.start, "comando retorne nao permitido nesse escopo");
        }
        return super.visitCmdRetorne(ctx); // Continua com a visita padrão
    }

    // Verifica se uma parcela unária está correta em relação aos parâmetros
    @Override
    public Object visitParcela_unario(AlgumaParser.Parcela_unarioContext ctx) {
        TabelaDeSimbolos escopoAtual = escopos.obterEscopoAtual();

        if (ctx.IDENT() != null) {
            String nomeId = ctx.IDENT().getText();

            // Confere se o identificador existe no escopo atual
            if (escopoAtual.existe(nomeId)) {
                List<EntradaTabelaDeSimbolos> parametros = escopoAtual.retornaTipo(nomeId);
                boolean erro = false;

                // Verifica a quantidade de parâmetros
                if (parametros.size() != ctx.expressao().size()) {
                    erro = true;
                } else {
                    // Checa se os tipos dos parâmetros estão corretos
                    for (int i = 0; i < parametros.size(); i++) {
                        if (parametros.get(i).tipo != AlgumaSemanticoUtils.analisarExpressao(escopos, ctx.expressao().get(i))) {
                            erro = true;
                        }
                    }
                }

                if (erro) {
                    AlgumaSemanticoUtils.registrarErroSemantico(ctx.start, "incompatibilidade de parametros na chamada de " + nomeId);
                }
            }
        }

        return super.visitParcela_unario(ctx); // Continua com a visita padrão
    }

    // Determina o tipo com base em uma string
    private TabelaDeSimbolos.TipoAlguma determinarTipo(String valor) {
        TabelaDeSimbolos.TipoAlguma tipo = null;
        switch(valor) {
            case "literal":
                tipo = TabelaDeSimbolos.TipoAlguma.CADEIA;
                break;
            case "inteiro":
                tipo = TabelaDeSimbolos.TipoAlguma.INTEIRO;
                break;
            case "real":
                tipo = TabelaDeSimbolos.TipoAlguma.REAL;
                break;
            case "logico":
                tipo = TabelaDeSimbolos.TipoAlguma.LOGICO;
                break;
            default:
                break;
        }
        return tipo;
    }
}

//semantico