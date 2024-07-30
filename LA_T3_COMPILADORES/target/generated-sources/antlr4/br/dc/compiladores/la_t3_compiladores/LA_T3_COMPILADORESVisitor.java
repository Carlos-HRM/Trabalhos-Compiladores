// Generated from java-escape by ANTLR 4.11.1
package br.dc.compiladores.la_t3_compiladores;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LA_T3_COMPILADORESParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LA_T3_COMPILADORESVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#programa}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrograma(LA_T3_COMPILADORESParser.ProgramaContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#declaracoes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracoes(LA_T3_COMPILADORESParser.DeclaracoesContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#decl_local_global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecl_local_global(LA_T3_COMPILADORESParser.Decl_local_globalContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#declaracao_local}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracao_local(LA_T3_COMPILADORESParser.Declaracao_localContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#declaracao_tipo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracao_tipo(LA_T3_COMPILADORESParser.Declaracao_tipoContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#declaracao_var}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracao_var(LA_T3_COMPILADORESParser.Declaracao_varContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#declaracao_const}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracao_const(LA_T3_COMPILADORESParser.Declaracao_constContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#variavel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariavel(LA_T3_COMPILADORESParser.VariavelContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#identificador}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentificador(LA_T3_COMPILADORESParser.IdentificadorContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#dimensao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDimensao(LA_T3_COMPILADORESParser.DimensaoContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#tipo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipo(LA_T3_COMPILADORESParser.TipoContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#tipo_basico}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipo_basico(LA_T3_COMPILADORESParser.Tipo_basicoContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#tipo_basico_ident}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipo_basico_ident(LA_T3_COMPILADORESParser.Tipo_basico_identContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#tipo_estendido}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipo_estendido(LA_T3_COMPILADORESParser.Tipo_estendidoContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#valor_constante}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValor_constante(LA_T3_COMPILADORESParser.Valor_constanteContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#registro}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegistro(LA_T3_COMPILADORESParser.RegistroContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#declaracao_global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracao_global(LA_T3_COMPILADORESParser.Declaracao_globalContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#parametro}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParametro(LA_T3_COMPILADORESParser.ParametroContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#parametros}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParametros(LA_T3_COMPILADORESParser.ParametrosContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#corpo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCorpo(LA_T3_COMPILADORESParser.CorpoContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmd(LA_T3_COMPILADORESParser.CmdContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdLeia}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLeia(LA_T3_COMPILADORESParser.CmdLeiaContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdEscreva}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdEscreva(LA_T3_COMPILADORESParser.CmdEscrevaContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdSe}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSe(LA_T3_COMPILADORESParser.CmdSeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdCaso}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdCaso(LA_T3_COMPILADORESParser.CmdCasoContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdPara}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdPara(LA_T3_COMPILADORESParser.CmdParaContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdEnquanto}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdEnquanto(LA_T3_COMPILADORESParser.CmdEnquantoContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdFaca}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdFaca(LA_T3_COMPILADORESParser.CmdFacaContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdAtribuicao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdAtribuicao(LA_T3_COMPILADORESParser.CmdAtribuicaoContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdChamada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdChamada(LA_T3_COMPILADORESParser.CmdChamadaContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdRetorne}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdRetorne(LA_T3_COMPILADORESParser.CmdRetorneContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#selecao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelecao(LA_T3_COMPILADORESParser.SelecaoContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#item_selecao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitItem_selecao(LA_T3_COMPILADORESParser.Item_selecaoContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#constantes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantes(LA_T3_COMPILADORESParser.ConstantesContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#numero_intervalo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumero_intervalo(LA_T3_COMPILADORESParser.Numero_intervaloContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#op_unario}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp_unario(LA_T3_COMPILADORESParser.Op_unarioContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#exp_aritmetica}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp_aritmetica(LA_T3_COMPILADORESParser.Exp_aritmeticaContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#termo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermo(LA_T3_COMPILADORESParser.TermoContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#fator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFator(LA_T3_COMPILADORESParser.FatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#op1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp1(LA_T3_COMPILADORESParser.Op1Context ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#op2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp2(LA_T3_COMPILADORESParser.Op2Context ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#op3}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp3(LA_T3_COMPILADORESParser.Op3Context ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#parcela}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParcela(LA_T3_COMPILADORESParser.ParcelaContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#parcela_unario}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParcela_unario(LA_T3_COMPILADORESParser.Parcela_unarioContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#parcela_nao_unario}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParcela_nao_unario(LA_T3_COMPILADORESParser.Parcela_nao_unarioContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#exp_relacional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp_relacional(LA_T3_COMPILADORESParser.Exp_relacionalContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#op_relacional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp_relacional(LA_T3_COMPILADORESParser.Op_relacionalContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#expressao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressao(LA_T3_COMPILADORESParser.ExpressaoContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#termo_logico}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermo_logico(LA_T3_COMPILADORESParser.Termo_logicoContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#fator_logico}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFator_logico(LA_T3_COMPILADORESParser.Fator_logicoContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#parcela_logica}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParcela_logica(LA_T3_COMPILADORESParser.Parcela_logicaContext ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#op_logico_1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp_logico_1(LA_T3_COMPILADORESParser.Op_logico_1Context ctx);
	/**
	 * Visit a parse tree produced by {@link LA_T3_COMPILADORESParser#op_logica_2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp_logica_2(LA_T3_COMPILADORESParser.Op_logica_2Context ctx);
}