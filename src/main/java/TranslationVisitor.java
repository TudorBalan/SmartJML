import parser.*;

import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.*;

import java.util.stream.Collectors;

public class TranslationVisitor extends SmartMLBaseVisitor<Node> {

    String constructorName;
    boolean privateConstructor;

    /*------------------------------------------------------------------
     * PROGRAMS
     *------------------------------------------------------------------*/
    @Override
    public Node visitProgram(SmartMLParser.ProgramContext ctx) {
        //TODO: add exceptions
        NodeList datatypes = new NodeList(ctx.datatypeDec().stream().map(this::visit).collect(Collectors.toList()));
        NodeList resources = new NodeList(ctx.resourceDec().stream().map(this::visit).collect(Collectors.toList()));
        NodeList contracts = new NodeList(ctx.contractDec().stream().map(this::visit).collect(Collectors.toList()));

        datatypes.addAll(resources);
        datatypes.addAll(contracts);

        return new CompilationUnit(null, new NodeList<ImportDeclaration>(), datatypes, null);
    }

    /*------------------------------------------------------------------
     * DATATYPES
     *------------------------------------------------------------------*/
    @Override
    public Node visitDatatypeDec(SmartMLParser.DatatypeDecContext ctx){
        //Class modifiers
        NodeList<Modifier> modifiers = new NodeList<>();
        modifiers.add(Modifier.finalModifier());

        //The name is changed to SL_"name"
        String name = "SL_" + this.visit(ctx.id()).toString();
        constructorName = name;

        ClassOrInterfaceDeclaration datatypes = new ClassOrInterfaceDeclaration(modifiers, false, name);

        //Fields

        //Constructor
        datatypes.addMember((ConstructorDeclaration) this.visit(ctx.dataTypeConstr()));

        return datatypes;
    }

    @Override
    public Node visitDataTypeConstr(SmartMLParser.DataTypeConstrContext ctx){
        //Class modifiers
        NodeList<Modifier> modifiers = new NodeList<>();
        modifiers.add(Modifier.publicModifier());
        ConstructorDeclaration constructor = new ConstructorDeclaration(modifiers, constructorName);

        //Parameters and Body
        NodeList<Parameter> parameters= new NodeList<>();
        BlockStmt body = new BlockStmt();
        constructor.setParameters(parameters);
        constructor.setBody(body);

        return constructor;
    }

    /*------------------------------------------------------------------
     * RESOURCES
     *------------------------------------------------------------------*/
    @Override
    public Node visitResourceDec(SmartMLParser.ResourceDecContext ctx) {
        //Class modifiers
        NodeList<Modifier> modifiers = new NodeList<>();

        //The name is changed to SL_"name"
        String name = "SL_" + this.visit(ctx.id()).toString();
        constructorName = name;

        ClassOrInterfaceDeclaration resources = new ClassOrInterfaceDeclaration(modifiers, false, name);

        //Fields
        ctx.field().forEach(x -> {
            VariableDeclarator vars = (VariableDeclarator) this.visit(x);
            resources.addMember(new FieldDeclaration(modifiers, vars));
        });

        //Constructor
        privateConstructor = true;
        resources.addMember((ConstructorDeclaration) this.visit(ctx.constructor()));

        //Functions
        ctx.function().forEach(x -> resources.addMember((MethodDeclaration) this.visit(x)));

        return resources;
    }

    /*------------------------------------------------------------------
     * CONTRACTS
     *------------------------------------------------------------------*/
    @Override
    public Node visitContractDec(SmartMLParser.ContractDecContext ctx) {
        //Class modifiers
        NodeList<Modifier> modifiers = new NodeList<>();

        //The name is changed to SL_"name"
        String name = "SL_" + this.visit(ctx.contractId).toString();
        constructorName = name;

        ClassOrInterfaceDeclaration contracts = new ClassOrInterfaceDeclaration(modifiers, false, name);

        //Fields
        ctx.body().field().forEach(x -> {
            VariableDeclarator vars = (VariableDeclarator) this.visit(x);
            contracts.addMember(new FieldDeclaration(modifiers, vars));
        });
        //sl_address
        contracts.addMember(new FieldDeclaration(modifiers, new VariableDeclarator(new ClassOrInterfaceType("Address"), "address", new ObjectCreationExpr(null, new ClassOrInterfaceType("Address"), new NodeList<>()))));

        //Constructor
        privateConstructor = false;
        contracts.addMember((ConstructorDeclaration) this.visit(ctx.body().constructor()));

        //Functions
        ctx.body().function().forEach(x -> contracts.addMember((MethodDeclaration) this.visit(x)));

        return contracts;
    }


    @Override
    public Node visitConstructor(SmartMLParser.ConstructorContext ctx) {
        //Constructor Declaration
        NodeList<Modifier> modifiers = new NodeList<>();
        if (privateConstructor) modifiers.add(Modifier.privateModifier()); else modifiers.add(Modifier.publicModifier());
        ConstructorDeclaration constructor = new ConstructorDeclaration(modifiers, constructorName);

        //Parameters
        NodeList<Parameter> parameters= new NodeList<>();
        ctx.varParams.forEach(x -> parameters.add(new Parameter((Type) this.visit(x.type()), this.visit(x.id()).toString())));
        constructor.setParameters(parameters);

        //Body
        BlockStmt body = new BlockStmt();
        ctx.assign().forEach(x -> {
            body.addStatement((Statement) this.visit(x));
        });
        constructor.setBody(body);

        return constructor;
    }

    @Override
    public Node visitTypeParams(SmartMLParser.TypeParamsContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Node visitField(SmartMLParser.FieldContext ctx) {
        return new VariableDeclarator((Type) this.visit(ctx.type()), (this.visit(ctx.id())).toString());
    }

    @Override
    public Node visitFunctionDec(SmartMLParser.FunctionDecContext ctx) {
        //Modifiers
        NodeList<Modifier> modifiers = new NodeList<>();
        modifiers.add(Modifier.publicModifier());

        //Return Type
        Type returnType;
        if (ctx.returnType() != null) {
            returnType = (Type) this.visit(ctx.returnType());
        } else {
            returnType = new VoidType();
        }
        //Name
        String name = this.visit(ctx.id()).toString();

        //Parameters
        NodeList<Parameter> params = new NodeList<>();
        params.add(new Parameter(PrimitiveType.intType(), "amount"));
        params.add(new Parameter(new ClassOrInterfaceType("CoinInfo"), "res"));
        params.add(new Parameter(new ClassOrInterfaceType("Address"), "address"));
        ctx.vardec().forEach(x -> params.add(new Parameter((Type) this.visit(x.type()), this.visit(x.id()).toString())));

        return new MethodDeclaration(modifiers, name, returnType, params);
    }

    @Override
    public Node visitFunction(SmartMLParser.FunctionContext ctx) {
        MethodDeclaration function = (MethodDeclaration) this.visit(ctx.functionDec());
        function.setBody((BlockStmt) this.visit(ctx.statBlock()));
        return function;
    }

    @Override
    public Node visitStatement(SmartMLParser.StatementContext ctx) {
        if (ctx.assign() != null) {
            return this.visit(ctx.assign());
        } else if (ctx.assertError() != null) {
            return this.visit(ctx.assertError());
        } else if (ctx.funCall() != null) {
            return this.visit(ctx.funCall());
        } else {
            return visitChildren(ctx);
        }
    }

    @Override
    public Node visitIfStatement(SmartMLParser.IfStatementContext ctx) {
        if (ctx.elseBlock != null) {
            return new IfStmt((Expression) this.visit(ctx.cond), (Statement) this.visit(ctx.block), (Statement) this.visit(ctx.elseBlock));
        } else {
            return new IfStmt((Expression) this.visit(ctx.cond), (Statement) this.visit(ctx.block), null);
        }
    }

    @Override
    public Node visitExprStat(SmartMLParser.ExprStatContext ctx) {
        return new ExpressionStmt((Expression) this.visit(ctx.expr()));
    }

    @Override
    public Node visitLoop(SmartMLParser.LoopContext ctx) {
        Expression cond = (Expression) this.visit(ctx.expr());
        System.out.println("cond: " + cond);
        return new WhileStmt((Expression) this.visit(ctx.expr()), (Statement) this.visit(ctx.statBlock()));
    }

    @Override
    public Node visitAssign(SmartMLParser.AssignContext ctx) {
        return new ExpressionStmt(new AssignExpr((Expression) this.visit(ctx.vardec()), (Expression) this.visit(ctx.expr()), AssignExpr.Operator.ASSIGN));
    }

    @Override
    public Node visitInternalCall(SmartMLParser.InternalCallContext ctx) {
        if (ctx.params() != null) {
            NodeList<Expression> parameters = new NodeList<>();
            parameters.add(new IntegerLiteralExpr(0));
            parameters.add(new ObjectCreationExpr(null, new ClassOrInterfaceType("CoinInfo"), new NodeList<>()));
            parameters.add(new ObjectCreationExpr(null, new ClassOrInterfaceType("Address"), new NodeList<>()));
            ctx.params().expr().forEach(x -> parameters.add((Expression) this.visit(x)));
            return new ExpressionStmt(new MethodCallExpr(new ThisExpr(), this.visit(ctx.id()).toString(), parameters));
        } else {
            return new ExpressionStmt(new MethodCallExpr(new ThisExpr(), this.visit(ctx.id()).toString()));
        }
    }

    @Override
    public Node visitExternalCall(SmartMLParser.ExternalCallContext ctx) {
        NodeList<Expression> parameters = new NodeList<>();
        if (ctx.resources() != null) {
            parameters.add((Expression) this.visit(ctx.resources()));
            parameters.add((Expression) this.visit(ctx.idName));
            parameters.add(new NameExpr("sl_address"));
        }
        if (ctx.params() != null) {
            ctx.params().expr().forEach(x -> parameters.add((Expression) this.visit(x)));
            return new ExpressionStmt(new MethodCallExpr((Expression) this.visit(ctx.idName), this.visit(ctx.funName).toString(), parameters));
        } else {
            if (ctx.resources() != null) {
                return new ExpressionStmt(new MethodCallExpr((Expression) this.visit(ctx.idName), this.visit(ctx.funName).toString(), parameters));
            } else {
                return new ExpressionStmt(new MethodCallExpr((Expression) this.visit(ctx.idName), this.visit(ctx.funName).toString()));
            }
        }
    }

    @Override
    public Node visitAssertError(SmartMLParser.AssertErrorContext ctx) {
        return new AssertStmt((Expression) this.visit(ctx.expr()));
    }

    @Override
    public Node visitReturnStat(SmartMLParser.ReturnStatContext ctx) {
        return new ReturnStmt((Expression) this.visit(ctx.expr()));
    }

    @Override
    public Node visitStatBlock(SmartMLParser.StatBlockContext ctx) {
        BlockStmt block = new BlockStmt();
        ctx.statement().forEach(x -> block.addStatement((Statement) this.visit(x)));
        return block;
    }

    @Override
    public Node visitExpr(SmartMLParser.ExprContext ctx) {
        if (ctx.operator == null){
            return this.visit(ctx.left);
        } else {
            BinaryExpr.Operator op;
            switch (ctx.operator.getText()){
                case "+": op = BinaryExpr.Operator.PLUS; break;
                case "-": op = BinaryExpr.Operator.MINUS; break;
                case "||": op = BinaryExpr.Operator.OR; break;
                case "=": return new AssignExpr((Expression) this.visit(ctx.left), (Expression) this.visit(ctx.right), AssignExpr.Operator.ASSIGN);
                default: op = BinaryExpr.Operator.UNSIGNED_RIGHT_SHIFT;
            }
            return new BinaryExpr((Expression) this.visit(ctx.left), (Expression) this.visit(ctx.right), op);
        }
    }

    @Override
    public Node visitTerm(SmartMLParser.TermContext ctx) {
        if (ctx.operator == null){
            return this.visit(ctx.left);
        } else {
            BinaryExpr.Operator op;
            switch (ctx.operator.getText()){
                case "*": op = BinaryExpr.Operator.MULTIPLY; break;
                case "/": op = BinaryExpr.Operator.DIVIDE; break;
                case "&&": op = BinaryExpr.Operator.AND; break;
                default: op = BinaryExpr.Operator.UNSIGNED_RIGHT_SHIFT;
            }
            return new BinaryExpr((Expression) this.visit(ctx.left), (Expression) this.visit(ctx.right), op);
        }
    }

    @Override
    public Node visitFactor(SmartMLParser.FactorContext ctx) {
        if (ctx.operator == null){
            return this.visit(ctx.left);
        } else {
            BinaryExpr.Operator op;
            switch (ctx.operator.getText()){
                case "==": op = BinaryExpr.Operator.EQUALS; break;
                case "<": op = BinaryExpr.Operator.LESS; break;
                case ">": op = BinaryExpr.Operator.GREATER; break;
                case "<=": op = BinaryExpr.Operator.LESS_EQUALS; break;
                case ">=": op = BinaryExpr.Operator.GREATER_EQUALS; break;
                case "!=": op = BinaryExpr.Operator.NOT_EQUALS; break;
                default: op = BinaryExpr.Operator.UNSIGNED_RIGHT_SHIFT;
            }
            return new BinaryExpr((Expression) this.visit(ctx.left), (Expression) this.visit(ctx.right), op);
        }
    }

    @Override
    public Node visitValue(SmartMLParser.ValueContext ctx) {
        if (ctx.INTEGER() != null){
            return new IntegerLiteralExpr(ctx.INTEGER().getText());
        } else if (ctx.expr() != null){
            return this.visit(ctx.expr());
        } else {
            return visitChildren(ctx);
        }
    }

    @Override
    public Node visitVardec(SmartMLParser.VardecContext ctx) {
        if (ctx.type() != null) {
            return new VariableDeclarationExpr((Type) this.visit(ctx.type()), (this.visit(ctx.id())).toString());
        }
        else {
            return this.visit(ctx.id());
        }
    }

    @Override
    public Node visitId(SmartMLParser.IdContext ctx){
        return new NameExpr(new SimpleName(ctx.getText()));
    }

    @Override
    public Node visitReturnType(SmartMLParser.ReturnTypeContext ctx) {
        switch (ctx.getText()) {
            case "bool":
                return PrimitiveType.booleanType();
            case "string":
                return new ClassOrInterfaceType(null, "String");
            case "int":
                return PrimitiveType.intType();
            default:
                return new VoidType();
        }
    }

    @Override
    public Node visitType(SmartMLParser.TypeContext ctx) {
        switch (ctx.getText()) {
            case "address":
                return new ClassOrInterfaceType(null, "Address");
            case "bool":
                return PrimitiveType.booleanType();
            case "string":
                return new ClassOrInterfaceType(null, "String");
            case "int":
                return PrimitiveType.intType();
            default:
                return new ClassOrInterfaceType(null, "SL_" + ctx.getText());
        }
    }

    @Override
    public Node visitBool(SmartMLParser.BoolContext ctx) {
        if (ctx.getText().equals("true")) {
            return new BooleanLiteralExpr(true);
        } else {
            return new BooleanLiteralExpr(false);
        }
    }

    @Override
    public Node visitString(SmartMLParser.StringContext ctx) {
        return new StringLiteralExpr(ctx.getText().replaceAll("\"" ,"").replaceAll("'", ""));
    }

}
