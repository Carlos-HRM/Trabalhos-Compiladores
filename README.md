# Projeto de Compiladores

## Alunos:
- Carlos Henrique Rennó Matos Filho
- Pedro Cassiano Coleone
- Pietro Minghini Moralles

## Dependências necessárias:
- Java 17.0.10
- Apache Maven 4.0.0

## Executando o código:

Para executar o código no terminal, utilize o seguinte comando, passando 3 argumentos:
- Arg1: Caminho para o compilador.jar
- Arg2: Caminho para o arquivo de entrada
- Arg3: Caminho para o arquivo de saída

```bash
java -jar /home/charles/NetBeansProjects/la_t1_compiladores/target/la_t1_compiladores-1.0-SNAPSHOT-jar-with-dependencies.jar /home/charles/Área\ de\ Trabalho/casos-de-teste/1.casos_teste_t1/entrada/1-algoritmo_2-2_apostila_LA.txt Downloads/Saida
```

## Executando o testador automático:

Para rodar o testador automático, utilize o comando `java -jar` com 7 argumentos no terminal:
- Arg1: Instalação do corretor (.jar do corretor)
- Arg2: Caminho executável do seu compilador (.jar do programa feito)
- Arg3: Compilador GCC (simplesmente digitar gcc no terminal)
- Arg4: Pasta temporária qualquer
- Arg5: Arquivo dos casos de teste
- Arg6: RA dos alunos (Nesse caso, 793241, 793249, 792238)
- Arg7: O trabalho correspondente (t1)

```bash
java -jar /Users/Pietro/Desktop/Compiladores/T1/LA_T1_COMPILADORES/compiladores-corretor-automatico-1.0-SNAPSHOT-jar-with-dependencies.jar "java -jar /Users/Pietro/Desktop/Compiladores/T1/LA_T1_COMPILADORES/target/LA_T1_COMPILADORES-1.0-SNAPSHOT-jar-with-dependencies.jar" gcc /Users/Pietro/Desktop/temp /Users/Pietro/Desktop/casos-de-teste "793241, 792238, " t1
```

Lembre-se sempre de utilizar os caminhos correspondentes no seu computador.
