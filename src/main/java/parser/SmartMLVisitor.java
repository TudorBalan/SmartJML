// Generated from C:/Users/Tudor/Documents/SmartJML/src/main/antlr/SmartML.g4 by ANTLR 4.13.2

    package parser;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SmartMLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SmartMLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(SmartMLParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#datatypeDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatatypeDec(SmartMLParser.DatatypeDecContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#dataTypeConstr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataTypeConstr(SmartMLParser.DataTypeConstrContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#adtFunctionDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdtFunctionDec(SmartMLParser.AdtFunctionDecContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#adtStatBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdtStatBlock(SmartMLParser.AdtStatBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#adtExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdtExpression(SmartMLParser.AdtExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#dataTypeCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataTypeCall(SmartMLParser.DataTypeCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#ifExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfExpression(SmartMLParser.IfExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#adtCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdtCall(SmartMLParser.AdtCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#switchExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchExpr(SmartMLParser.SwitchExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#caseExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseExpr(SmartMLParser.CaseExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#adtAssign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdtAssign(SmartMLParser.AdtAssignContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#exceptionDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExceptionDec(SmartMLParser.ExceptionDecContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#resourceDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResourceDec(SmartMLParser.ResourceDecContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#interfaceDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceDec(SmartMLParser.InterfaceDecContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#contractDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContractDec(SmartMLParser.ContractDecContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBody(SmartMLParser.BodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#constructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructor(SmartMLParser.ConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#typeParams}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeParams(SmartMLParser.TypeParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#field}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitField(SmartMLParser.FieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#functionDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDec(SmartMLParser.FunctionDecContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(SmartMLParser.FunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#funType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunType(SmartMLParser.FunTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(SmartMLParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(SmartMLParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#exprStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprStat(SmartMLParser.ExprStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoop(SmartMLParser.LoopContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#assign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign(SmartMLParser.AssignContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#funCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunCall(SmartMLParser.FunCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#internalCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInternalCall(SmartMLParser.InternalCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#externalCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExternalCall(SmartMLParser.ExternalCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#assertError}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssertError(SmartMLParser.AssertErrorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#letExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetExpr(SmartMLParser.LetExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#transaction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransaction(SmartMLParser.TransactionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#returnStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStat(SmartMLParser.ReturnStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#tryStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryStatement(SmartMLParser.TryStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#statBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatBlock(SmartMLParser.StatBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#resources}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResources(SmartMLParser.ResourcesContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#params}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParams(SmartMLParser.ParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(SmartMLParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(SmartMLParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor(SmartMLParser.FactorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(SmartMLParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#resourceAccess}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResourceAccess(SmartMLParser.ResourceAccessContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#newVal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewVal(SmartMLParser.NewValContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#thisVal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThisVal(SmartMLParser.ThisValContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#vardec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVardec(SmartMLParser.VardecContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#qualifiedAccess}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedAccess(SmartMLParser.QualifiedAccessContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#adtFunCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdtFunCall(SmartMLParser.AdtFunCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(SmartMLParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#returnType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnType(SmartMLParser.ReturnTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(SmartMLParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#bool}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool(SmartMLParser.BoolContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(SmartMLParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by {@link SmartMLParser#address}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddress(SmartMLParser.AddressContext ctx);
}