package br.dc.compiladores.la_t5_compiladores;

import java.util.ArrayList;
import java.util.HashMap;

public class TabelaDeSimbolos {

    // Tipo do escopo atual, que pode ser uma função, variável, etc.
    public TipoAlguma tipo;

    // Enumeração para os tipos básicos e especiais de dados
    public enum TipoAlguma {
        INTEIRO,
        REAL,
        CADEIA,
        LOGICO,
        INVALIDO,
        REGISTRO,
        VOID
    }

    // Enumeração para os diferentes tipos de estrutura de dados
    public enum Structure {
        VAR, 
        CONST, 
        PROC, 
        FUNC, 
        TIPO
    }

    // Classe interna que representa uma entrada na tabela de símbolos
    class EntradaTabelaDeSimbolos {
        TipoAlguma tipo;        // Tipo da entrada (inteiro, real, etc.)
        String nome;            // Nome da entrada
        Structure structure;    // Estrutura da entrada (variável, constante, etc.)

        // Construtor da classe EntradaTabelaDeSimbolos
        public EntradaTabelaDeSimbolos(String nome, TipoAlguma tipo, Structure structure) {
            this.tipo = tipo;
            this.nome = nome;
            this.structure = structure;
        }
    }

    // HashMap que armazena as entradas da tabela de símbolos por nome
    private HashMap<String, EntradaTabelaDeSimbolos> tabela;

    // HashMap que armazena listas de entradas por tipo de dado
    private HashMap<String, ArrayList<EntradaTabelaDeSimbolos>> tipoTabela;

    // Verifica se um nome de entrada existe na tabela de símbolos
    public boolean existe(String nome) {
        return tabela.containsKey(nome);
    }

    // Retorna o tipo da entrada com o nome fornecido
    public TipoAlguma verificar(String nome) {
        if(tabela.containsKey(nome))
            return tabela.get(nome).tipo;
        return null;
    }
    
    public TabelaDeSimbolos(){
        tabela = new HashMap<>();
        tipoTabela = new HashMap<>();
    }
    
    // Construtor da classe TabelaDeSimbolos
    public TabelaDeSimbolos(TipoAlguma tipo) {
        tabela = new HashMap<>();
        tipoTabela = new HashMap<>();
        this.tipo = tipo;
    }

    // Adiciona uma nova entrada na tabela de símbolos
    public void adicionar(String nome, TipoAlguma tipo, Structure structure) {
        EntradaTabelaDeSimbolos entrada = new EntradaTabelaDeSimbolos(nome, tipo, structure);
        tabela.put(nome, entrada);
    }

    // Adiciona uma entrada existente na tabela de símbolos
    public void adicionar(EntradaTabelaDeSimbolos entrada) {
        tabela.put(entrada.nome, entrada);
    }

    // Adiciona uma entrada à lista de entradas associadas a um nome de tipo
    public void adicionar(String tipoNome, EntradaTabelaDeSimbolos entrada) {
        if (tipoTabela.containsKey(tipoNome)) {
            tipoTabela.get(tipoNome).add(entrada);
        } else {
            ArrayList<EntradaTabelaDeSimbolos> lista = new ArrayList<>();
            lista.add(entrada);
            tipoTabela.put(tipoNome, lista);
        }
    }

    // Retorna a lista de entradas associadas a um nome de tipo
    public ArrayList<EntradaTabelaDeSimbolos> retornaTipo(String nome) {
        return tipoTabela.get(nome);
    }
}
