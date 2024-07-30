/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.dc.compiladores.la_t3_compiladores;

import java.util.HashMap;

public class TabelaDeSimbolos {
    public enum TipoAlguma {
        INTEIRO,
        REAL,
        INVALIDO,
        LOGICO,
        IDENT,
        TIPO,
        CADEIA
    }

    private  HashMap<String, EntradaTabelaDeSimbolos> tabelaDeSimbolos;

    public TabelaDeSimbolos() {
        tabelaDeSimbolos = new HashMap<>();
    }

    public void adicionar(String nome, TipoAlguma tipo) {
        tabelaDeSimbolos.put(nome, new EntradaTabelaDeSimbolos(nome, tipo));
    }

    public boolean existe(String nome) {
        return tabelaDeSimbolos.containsKey(nome);
    }

    public TipoAlguma verificar(String nome) {
        EntradaTabelaDeSimbolos entrada = tabelaDeSimbolos.get(nome);
        return (entrada != null) ? entrada.tipo : TipoAlguma.INVALIDO;
    }
}

