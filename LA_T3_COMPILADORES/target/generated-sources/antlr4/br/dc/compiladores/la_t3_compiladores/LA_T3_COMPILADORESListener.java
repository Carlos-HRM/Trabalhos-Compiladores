// Generated from java-escape by ANTLR 4.11.1
package br.dc.compiladores.la_t3_compiladores;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LA_T3_COMPILADORESParser}.
 */
public interface LA_T3_COMPILADORESListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#programa}.
	 * @param ctx the parse tree
	 */
	void enterPrograma(LA_T3_COMPILADORESParser.ProgramaContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#programa}.
	 * @param ctx the parse tree
	 */
	void exitPrograma(LA_T3_COMPILADORESParser.ProgramaContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#declaracoes}.
	 * @param ctx the parse tree
	 */
	void enterDeclaracoes(LA_T3_COMPILADORESParser.DeclaracoesContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#declaracoes}.
	 * @param ctx the parse tree
	 */
	void exitDeclaracoes(LA_T3_COMPILADORESParser.DeclaracoesContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#decl_local_global}.
	 * @param ctx the parse tree
	 */
	void enterDecl_local_global(LA_T3_COMPILADORESParser.Decl_local_globalContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#decl_local_global}.
	 * @param ctx the parse tree
	 */
	void exitDecl_local_global(LA_T3_COMPILADORESParser.Decl_local_globalContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#declaracao_local}.
	 * @param ctx the parse tree
	 */
	void enterDeclaracao_local(LA_T3_COMPILADORESParser.Declaracao_localContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#declaracao_local}.
	 * @param ctx the parse tree
	 */
	void exitDeclaracao_local(LA_T3_COMPILADORESParser.Declaracao_localContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#declaracao_tipo}.
	 * @param ctx the parse tree
	 */
	void enterDeclaracao_tipo(LA_T3_COMPILADORESParser.Declaracao_tipoContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#declaracao_tipo}.
	 * @param ctx the parse tree
	 */
	void exitDeclaracao_tipo(LA_T3_COMPILADORESParser.Declaracao_tipoContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#declaracao_var}.
	 * @param ctx the parse tree
	 */
	void enterDeclaracao_var(LA_T3_COMPILADORESParser.Declaracao_varContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#declaracao_var}.
	 * @param ctx the parse tree
	 */
	void exitDeclaracao_var(LA_T3_COMPILADORESParser.Declaracao_varContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#declaracao_const}.
	 * @param ctx the parse tree
	 */
	void enterDeclaracao_const(LA_T3_COMPILADORESParser.Declaracao_constContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#declaracao_const}.
	 * @param ctx the parse tree
	 */
	void exitDeclaracao_const(LA_T3_COMPILADORESParser.Declaracao_constContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#variavel}.
	 * @param ctx the parse tree
	 */
	void enterVariavel(LA_T3_COMPILADORESParser.VariavelContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#variavel}.
	 * @param ctx the parse tree
	 */
	void exitVariavel(LA_T3_COMPILADORESParser.VariavelContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#identificador}.
	 * @param ctx the parse tree
	 */
	void enterIdentificador(LA_T3_COMPILADORESParser.IdentificadorContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#identificador}.
	 * @param ctx the parse tree
	 */
	void exitIdentificador(LA_T3_COMPILADORESParser.IdentificadorContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#dimensao}.
	 * @param ctx the parse tree
	 */
	void enterDimensao(LA_T3_COMPILADORESParser.DimensaoContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#dimensao}.
	 * @param ctx the parse tree
	 */
	void exitDimensao(LA_T3_COMPILADORESParser.DimensaoContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#tipo}.
	 * @param ctx the parse tree
	 */
	void enterTipo(LA_T3_COMPILADORESParser.TipoContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#tipo}.
	 * @param ctx the parse tree
	 */
	void exitTipo(LA_T3_COMPILADORESParser.TipoContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#tipo_basico}.
	 * @param ctx the parse tree
	 */
	void enterTipo_basico(LA_T3_COMPILADORESParser.Tipo_basicoContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#tipo_basico}.
	 * @param ctx the parse tree
	 */
	void exitTipo_basico(LA_T3_COMPILADORESParser.Tipo_basicoContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#tipo_basico_ident}.
	 * @param ctx the parse tree
	 */
	void enterTipo_basico_ident(LA_T3_COMPILADORESParser.Tipo_basico_identContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#tipo_basico_ident}.
	 * @param ctx the parse tree
	 */
	void exitTipo_basico_ident(LA_T3_COMPILADORESParser.Tipo_basico_identContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#tipo_estendido}.
	 * @param ctx the parse tree
	 */
	void enterTipo_estendido(LA_T3_COMPILADORESParser.Tipo_estendidoContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#tipo_estendido}.
	 * @param ctx the parse tree
	 */
	void exitTipo_estendido(LA_T3_COMPILADORESParser.Tipo_estendidoContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#valor_constante}.
	 * @param ctx the parse tree
	 */
	void enterValor_constante(LA_T3_COMPILADORESParser.Valor_constanteContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#valor_constante}.
	 * @param ctx the parse tree
	 */
	void exitValor_constante(LA_T3_COMPILADORESParser.Valor_constanteContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#registro}.
	 * @param ctx the parse tree
	 */
	void enterRegistro(LA_T3_COMPILADORESParser.RegistroContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#registro}.
	 * @param ctx the parse tree
	 */
	void exitRegistro(LA_T3_COMPILADORESParser.RegistroContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#declaracao_global}.
	 * @param ctx the parse tree
	 */
	void enterDeclaracao_global(LA_T3_COMPILADORESParser.Declaracao_globalContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#declaracao_global}.
	 * @param ctx the parse tree
	 */
	void exitDeclaracao_global(LA_T3_COMPILADORESParser.Declaracao_globalContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#parametro}.
	 * @param ctx the parse tree
	 */
	void enterParametro(LA_T3_COMPILADORESParser.ParametroContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#parametro}.
	 * @param ctx the parse tree
	 */
	void exitParametro(LA_T3_COMPILADORESParser.ParametroContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#parametros}.
	 * @param ctx the parse tree
	 */
	void enterParametros(LA_T3_COMPILADORESParser.ParametrosContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#parametros}.
	 * @param ctx the parse tree
	 */
	void exitParametros(LA_T3_COMPILADORESParser.ParametrosContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#corpo}.
	 * @param ctx the parse tree
	 */
	void enterCorpo(LA_T3_COMPILADORESParser.CorpoContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#corpo}.
	 * @param ctx the parse tree
	 */
	void exitCorpo(LA_T3_COMPILADORESParser.CorpoContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#cmd}.
	 * @param ctx the parse tree
	 */
	void enterCmd(LA_T3_COMPILADORESParser.CmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#cmd}.
	 * @param ctx the parse tree
	 */
	void exitCmd(LA_T3_COMPILADORESParser.CmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdLeia}.
	 * @param ctx the parse tree
	 */
	void enterCmdLeia(LA_T3_COMPILADORESParser.CmdLeiaContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdLeia}.
	 * @param ctx the parse tree
	 */
	void exitCmdLeia(LA_T3_COMPILADORESParser.CmdLeiaContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdEscreva}.
	 * @param ctx the parse tree
	 */
	void enterCmdEscreva(LA_T3_COMPILADORESParser.CmdEscrevaContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdEscreva}.
	 * @param ctx the parse tree
	 */
	void exitCmdEscreva(LA_T3_COMPILADORESParser.CmdEscrevaContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdSe}.
	 * @param ctx the parse tree
	 */
	void enterCmdSe(LA_T3_COMPILADORESParser.CmdSeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdSe}.
	 * @param ctx the parse tree
	 */
	void exitCmdSe(LA_T3_COMPILADORESParser.CmdSeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdCaso}.
	 * @param ctx the parse tree
	 */
	void enterCmdCaso(LA_T3_COMPILADORESParser.CmdCasoContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdCaso}.
	 * @param ctx the parse tree
	 */
	void exitCmdCaso(LA_T3_COMPILADORESParser.CmdCasoContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdPara}.
	 * @param ctx the parse tree
	 */
	void enterCmdPara(LA_T3_COMPILADORESParser.CmdParaContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdPara}.
	 * @param ctx the parse tree
	 */
	void exitCmdPara(LA_T3_COMPILADORESParser.CmdParaContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdEnquanto}.
	 * @param ctx the parse tree
	 */
	void enterCmdEnquanto(LA_T3_COMPILADORESParser.CmdEnquantoContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdEnquanto}.
	 * @param ctx the parse tree
	 */
	void exitCmdEnquanto(LA_T3_COMPILADORESParser.CmdEnquantoContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdFaca}.
	 * @param ctx the parse tree
	 */
	void enterCmdFaca(LA_T3_COMPILADORESParser.CmdFacaContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdFaca}.
	 * @param ctx the parse tree
	 */
	void exitCmdFaca(LA_T3_COMPILADORESParser.CmdFacaContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdAtribuicao}.
	 * @param ctx the parse tree
	 */
	void enterCmdAtribuicao(LA_T3_COMPILADORESParser.CmdAtribuicaoContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdAtribuicao}.
	 * @param ctx the parse tree
	 */
	void exitCmdAtribuicao(LA_T3_COMPILADORESParser.CmdAtribuicaoContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdChamada}.
	 * @param ctx the parse tree
	 */
	void enterCmdChamada(LA_T3_COMPILADORESParser.CmdChamadaContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdChamada}.
	 * @param ctx the parse tree
	 */
	void exitCmdChamada(LA_T3_COMPILADORESParser.CmdChamadaContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdRetorne}.
	 * @param ctx the parse tree
	 */
	void enterCmdRetorne(LA_T3_COMPILADORESParser.CmdRetorneContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#cmdRetorne}.
	 * @param ctx the parse tree
	 */
	void exitCmdRetorne(LA_T3_COMPILADORESParser.CmdRetorneContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#selecao}.
	 * @param ctx the parse tree
	 */
	void enterSelecao(LA_T3_COMPILADORESParser.SelecaoContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#selecao}.
	 * @param ctx the parse tree
	 */
	void exitSelecao(LA_T3_COMPILADORESParser.SelecaoContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#item_selecao}.
	 * @param ctx the parse tree
	 */
	void enterItem_selecao(LA_T3_COMPILADORESParser.Item_selecaoContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#item_selecao}.
	 * @param ctx the parse tree
	 */
	void exitItem_selecao(LA_T3_COMPILADORESParser.Item_selecaoContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#constantes}.
	 * @param ctx the parse tree
	 */
	void enterConstantes(LA_T3_COMPILADORESParser.ConstantesContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#constantes}.
	 * @param ctx the parse tree
	 */
	void exitConstantes(LA_T3_COMPILADORESParser.ConstantesContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#numero_intervalo}.
	 * @param ctx the parse tree
	 */
	void enterNumero_intervalo(LA_T3_COMPILADORESParser.Numero_intervaloContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#numero_intervalo}.
	 * @param ctx the parse tree
	 */
	void exitNumero_intervalo(LA_T3_COMPILADORESParser.Numero_intervaloContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#op_unario}.
	 * @param ctx the parse tree
	 */
	void enterOp_unario(LA_T3_COMPILADORESParser.Op_unarioContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#op_unario}.
	 * @param ctx the parse tree
	 */
	void exitOp_unario(LA_T3_COMPILADORESParser.Op_unarioContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#exp_aritmetica}.
	 * @param ctx the parse tree
	 */
	void enterExp_aritmetica(LA_T3_COMPILADORESParser.Exp_aritmeticaContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#exp_aritmetica}.
	 * @param ctx the parse tree
	 */
	void exitExp_aritmetica(LA_T3_COMPILADORESParser.Exp_aritmeticaContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#termo}.
	 * @param ctx the parse tree
	 */
	void enterTermo(LA_T3_COMPILADORESParser.TermoContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#termo}.
	 * @param ctx the parse tree
	 */
	void exitTermo(LA_T3_COMPILADORESParser.TermoContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#fator}.
	 * @param ctx the parse tree
	 */
	void enterFator(LA_T3_COMPILADORESParser.FatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#fator}.
	 * @param ctx the parse tree
	 */
	void exitFator(LA_T3_COMPILADORESParser.FatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#op1}.
	 * @param ctx the parse tree
	 */
	void enterOp1(LA_T3_COMPILADORESParser.Op1Context ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#op1}.
	 * @param ctx the parse tree
	 */
	void exitOp1(LA_T3_COMPILADORESParser.Op1Context ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#op2}.
	 * @param ctx the parse tree
	 */
	void enterOp2(LA_T3_COMPILADORESParser.Op2Context ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#op2}.
	 * @param ctx the parse tree
	 */
	void exitOp2(LA_T3_COMPILADORESParser.Op2Context ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#op3}.
	 * @param ctx the parse tree
	 */
	void enterOp3(LA_T3_COMPILADORESParser.Op3Context ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#op3}.
	 * @param ctx the parse tree
	 */
	void exitOp3(LA_T3_COMPILADORESParser.Op3Context ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#parcela}.
	 * @param ctx the parse tree
	 */
	void enterParcela(LA_T3_COMPILADORESParser.ParcelaContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#parcela}.
	 * @param ctx the parse tree
	 */
	void exitParcela(LA_T3_COMPILADORESParser.ParcelaContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#parcela_unario}.
	 * @param ctx the parse tree
	 */
	void enterParcela_unario(LA_T3_COMPILADORESParser.Parcela_unarioContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#parcela_unario}.
	 * @param ctx the parse tree
	 */
	void exitParcela_unario(LA_T3_COMPILADORESParser.Parcela_unarioContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#parcela_nao_unario}.
	 * @param ctx the parse tree
	 */
	void enterParcela_nao_unario(LA_T3_COMPILADORESParser.Parcela_nao_unarioContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#parcela_nao_unario}.
	 * @param ctx the parse tree
	 */
	void exitParcela_nao_unario(LA_T3_COMPILADORESParser.Parcela_nao_unarioContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#exp_relacional}.
	 * @param ctx the parse tree
	 */
	void enterExp_relacional(LA_T3_COMPILADORESParser.Exp_relacionalContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#exp_relacional}.
	 * @param ctx the parse tree
	 */
	void exitExp_relacional(LA_T3_COMPILADORESParser.Exp_relacionalContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#op_relacional}.
	 * @param ctx the parse tree
	 */
	void enterOp_relacional(LA_T3_COMPILADORESParser.Op_relacionalContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#op_relacional}.
	 * @param ctx the parse tree
	 */
	void exitOp_relacional(LA_T3_COMPILADORESParser.Op_relacionalContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#expressao}.
	 * @param ctx the parse tree
	 */
	void enterExpressao(LA_T3_COMPILADORESParser.ExpressaoContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#expressao}.
	 * @param ctx the parse tree
	 */
	void exitExpressao(LA_T3_COMPILADORESParser.ExpressaoContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#termo_logico}.
	 * @param ctx the parse tree
	 */
	void enterTermo_logico(LA_T3_COMPILADORESParser.Termo_logicoContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#termo_logico}.
	 * @param ctx the parse tree
	 */
	void exitTermo_logico(LA_T3_COMPILADORESParser.Termo_logicoContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#fator_logico}.
	 * @param ctx the parse tree
	 */
	void enterFator_logico(LA_T3_COMPILADORESParser.Fator_logicoContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#fator_logico}.
	 * @param ctx the parse tree
	 */
	void exitFator_logico(LA_T3_COMPILADORESParser.Fator_logicoContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#parcela_logica}.
	 * @param ctx the parse tree
	 */
	void enterParcela_logica(LA_T3_COMPILADORESParser.Parcela_logicaContext ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#parcela_logica}.
	 * @param ctx the parse tree
	 */
	void exitParcela_logica(LA_T3_COMPILADORESParser.Parcela_logicaContext ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#op_logico_1}.
	 * @param ctx the parse tree
	 */
	void enterOp_logico_1(LA_T3_COMPILADORESParser.Op_logico_1Context ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#op_logico_1}.
	 * @param ctx the parse tree
	 */
	void exitOp_logico_1(LA_T3_COMPILADORESParser.Op_logico_1Context ctx);
	/**
	 * Enter a parse tree produced by {@link LA_T3_COMPILADORESParser#op_logica_2}.
	 * @param ctx the parse tree
	 */
	void enterOp_logica_2(LA_T3_COMPILADORESParser.Op_logica_2Context ctx);
	/**
	 * Exit a parse tree produced by {@link LA_T3_COMPILADORESParser#op_logica_2}.
	 * @param ctx the parse tree
	 */
	void exitOp_logica_2(LA_T3_COMPILADORESParser.Op_logica_2Context ctx);
}