# Projeto de Compiladores - T3

## Alunos:
- Carlos Henrique Rennó Matos Filho - RA: 793241
- Pedro Cassiano Coleone - RA: 793249
- Pietro Minghini Moralles - RA: 792238

## Dependências necessárias:
- Java 17.0.10
- Apache Maven 4.0.0

## Executando o código:

Para executar o código no terminal, utilize o seguinte comando, passando 3 argumentos:
- Arg1: Caminho para o compilador.jar
- Arg2: Caminho para o arquivo de entrada
- Arg3: Caminho para o arquivo de saída

```bash
java -jar /caminho/para/o/compilador.jar /caminho/para/o/arquivo_de_entrada /caminho/para/o/arquivo_de_saida
```

## Executando o testador automático:

Para rodar o testador automático, utilize o comando `java -jar` com 7 argumentos no terminal:
- Arg1: Instalação do corretor (.jar do corretor)
- Arg2: Caminho executável do seu compilador (.jar do programa feito)
- Arg3: Compilador GCC (simplesmente digitar gcc no terminal)
- Arg4: Pasta temporária qualquer
- Arg5: Arquivo dos casos de teste
- Arg6: RA dos alunos (Nesse caso, 793241, 793249, 792238)
- Arg7: O trabalho correspondente (t3)

```bash
java -jar /caminho/para/o/corretor.jar "java -jar /caminho/para/o/compilador.jar" gcc /caminho/para/a/pasta_temporaria /caminho/para/o/arquivo_de_testes "793241, 793249, 792238" t3
```

Lembre-se sempre de utilizar os caminhos correspondentes no seu computador!
