import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import parser.*;

import com.github.javaparser.ast.type.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.*;

import java.util.List;
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
        String name = "SL_" + ((SimpleName) this.visit(ctx.id())).asString();

        ClassOrInterfaceDeclaration datatypes = new ClassOrInterfaceDeclaration(modifiers, false, name);

        return datatypes;
    }

    /*------------------------------------------------------------------
     * RESOURCES
     *------------------------------------------------------------------*/
    @Override
    public Node visitResourceDec(SmartMLParser.ResourceDecContext ctx) {
        //Class modifiers
        NodeList<Modifier> modifiers = new NodeList<>();

        //The name is changed to SL_"name"
        String name = "SL_" + ((SimpleName) this.visit(ctx.id())).asString();
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
        String name = "SL_" + ((SimpleName) this.visit(ctx.contractId)).asString();
        constructorName = name;

        ClassOrInterfaceDeclaration contracts = new ClassOrInterfaceDeclaration(modifiers, false, name);

        //Fields
        ctx.body().field().forEach(x -> {
            VariableDeclarator vars = (VariableDeclarator) this.visit(x);
            contracts.addMember(new FieldDeclaration(modifiers, vars));
        });

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
            AssignExpr expr = (AssignExpr) this.visit(x);
            body.addStatement(new ExpressionStmt(expr));
        });
        constructor.setBody(body);

        return constructor;
    }


    @Override
    public Node visitTypeParams(SmartMLParser.TypeParamsContext ctx) {
        return new Parameter((Type) this.visit(ctx.type()), (this.visit(ctx.id())).toString());
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
        ctx.vardec().forEach(x -> params.add(new Parameter((Type) this.visit(x.type()), this.visit(x.id()).toString())));

        return new MethodDeclaration(modifiers, name, returnType, params);
    }

    @Override
    public Node visitFunction(SmartMLParser.FunctionContext ctx) {
        return this.visit(ctx.functionDec());
    }

    @Override
    public Node visitAssign(SmartMLParser.AssignContext ctx) {
        return new AssignExpr(new NameExpr(this.visit(ctx.vardec().id()).toString()), new NameExpr(this.visit(ctx.expr().left.left.left.id()).toString()), AssignExpr.Operator.ASSIGN);
    }

    @Override
    public Node visitVardec(SmartMLParser.VardecContext ctx) {
        if (ctx.type() != null)
            return new VariableDeclarator((Type) this.visit(ctx.type()), (this.visit(ctx.id())).toString());
        else
            return new NameExpr((SimpleName) visit(ctx.id()));
    }

    @Override
    public Node visitId(SmartMLParser.IdContext ctx){
        return new SimpleName(ctx.getText());
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

}
