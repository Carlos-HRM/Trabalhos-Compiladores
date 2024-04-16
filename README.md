##Alunos:
-Carlos Henrique Rennó Matos Filho
-Pedro Cassiano Coleone
-Pietro Minghini Moralles


Depedências necessárias:
Java 17.0.10
Apache Maven 4.0.0


##Rodando o nosso código

Para rodar o código no terminal digite o seguinte comando,teremos que usar o comando java -jar com 3 argumentos, sendo eles:
-Arg1:Caminho para o compilador.jar
-Arg2:Caminho para o arquivo de entrada
-Arg3:Caminho para o arquivo de saída

Aqui está um exemplo:
java -jar /home/charles/NetBeansProjects/la_t1_compiladores/target/la_t1_compiladores-1.0-SNAPSHOT-jar-with-dependencies.jar /home/charles/Área\ de\ Trabalho/casos-de-teste/1.casos_teste_t1/entrada/1-algoritmo_2-2_apostila_LA.txt Downloads/Saida


##Rodando o testador automático
Para rodar o testador automático, teremos que usar o comando java -jar com  7 argumentos no terminal, sendo eles:
-Arg1: Instalação do corretor(.jar do corretor)
-Arg2: Caminho executável do seu compilador(.jar do programa feito)
-Arg3: Compilador GCC(simplesmente digitar gcc no terminal)
-Arg4: Uma pasta temporaria qualquer
-Arg5: Arquivo dos casos de teste
-Arg6: RA dos alunos(Nesse caso, 793241, 793249,792238)
-Arg7: O trabalho correspondente(t1)


Aqui está um exemplo:

java -jar /Users/Pietro/Desktop/Compiladores/T1/LA_T1_COMPILADORES/compiladores-corretor-automatico-1.0-SNAPSHOT-jar-with-dependencies.jar "java -jar /Users/Pietro/Desktop/Compiladores/T1/LA_T1_COMPILADORES/target/LA_T1_COMPILADORES-1.0-SNAPSHOT-jar-with-dependencies.jar" gcc /Users/Pietro/Desktop/temp /Users/Pietro/Desktop/casos-de-teste "793241, 792238, " t1

Lembrando sempre de usar os caminhos correspondentes no seu computador.


