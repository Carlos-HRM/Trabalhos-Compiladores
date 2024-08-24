/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.dc.compiladores.la_t5_compiladores;

import java.util.LinkedList;
import java.util.List;

public class Escopos {

    private LinkedList<TabelaDeSimbolos> pilhaDeTabelas;

    public Escopos(TabelaDeSimbolos.TipoAlguma tipo) {
        pilhaDeTabelas = new LinkedList<>();
        criarNovoEscopo(tipo);
    }

    // Cria um novo escopo e o adiciona à pilha de tabelas
    public void criarNovoEscopo(TabelaDeSimbolos.TipoAlguma tipo) {
        pilhaDeTabelas.push(new TabelaDeSimbolos(tipo));
    }

    // Obtém o escopo atual da pilha
    public TabelaDeSimbolos obterEscopoAtual() {
        return pilhaDeTabelas.peek();
    }

    // Retorna a pilha de tabelas para iteração
    public List<TabelaDeSimbolos> percorrerEscoposAninhados() {
        return pilhaDeTabelas;
    }

    // Remove o escopo atual da pilha
    public void abandonarEscopo() {
        pilhaDeTabelas.pop();
    }

    // Verifica se um identificador existe em algum escopo
    public boolean existeIdentificador(String nome) {
        for (TabelaDeSimbolos escopo : pilhaDeTabelas) {
            if (escopo.existe(nome)) {
                return true;
            }
        }
        return false;
    }
}
