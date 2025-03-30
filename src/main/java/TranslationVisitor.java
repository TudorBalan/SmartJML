import parser.*;

import com.github.javaparser.ast.type.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.*;

import java.util.stream.Collectors;



public class TranslationVisitor extends SmartMLBaseVisitor<Node> {

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
        //Datatype declarations are always final
        NodeList<Modifier> modifiers = new NodeList<>();
        modifiers.add(Modifier.finalModifier());

        //The name is changed to SL_"name"
        String name = "SL_" + ((SimpleName) this.visit(ctx.id())).asString();

        ClassOrInterfaceDeclaration datatypes = new ClassOrInterfaceDeclaration(modifiers, false, name);

        /*
        //Idea for Implementation of datatype fields:
        modifiers.remove(Modifier.finalModifier());
        ctx.dataTypeConstr().typeParams().forEach(x -> {
            VariableDeclarator vars = (VariableDeclarator) this.visit(x);
            datatypes.addMember(new FieldDeclaration(modifiers, vars));
        });
        */

        return datatypes;
    }

    @Override
    public Node visitDataTypeConstr(SmartMLParser.DataTypeConstrContext ctx) {
        return visitChildren(ctx);
    }

    /*------------------------------------------------------------------
     * RESOURCES
     *------------------------------------------------------------------*/
    @Override
    public Node visitResourceDec(SmartMLParser.ResourceDecContext ctx) {
        //No modifiers
        NodeList<Modifier> modifiers = new NodeList<>();

        //The name is changed to SL_"name"
        String name = "SL_" + ((SimpleName) this.visit(ctx.id())).asString();

        ClassOrInterfaceDeclaration resources = new ClassOrInterfaceDeclaration(modifiers, false, name);

        ctx.field().forEach(x -> {
            VariableDeclarator vars = (VariableDeclarator) this.visit(x);
            resources.addMember(new FieldDeclaration(modifiers, vars));
        });

        return resources;
    }

    /*------------------------------------------------------------------
     * CONTRACTS
     *------------------------------------------------------------------*/
    @Override
    public Node visitContractDec(SmartMLParser.ContractDecContext ctx) {
        //No modifiers
        NodeList<Modifier> modifiers = new NodeList<>();

        //The name is changed to SL_"name"
        String name = "SL_" + ((SimpleName) this.visit(ctx.contractId)).asString();

        ClassOrInterfaceDeclaration contracts = new ClassOrInterfaceDeclaration(modifiers, false, name);

        ctx.body().field().forEach(x -> {
            VariableDeclarator vars = (VariableDeclarator) this.visit(x);
            contracts.addMember(new FieldDeclaration(modifiers, vars));
        });

        return contracts;
    }

    @Override
    public Node visitTypeParams(SmartMLParser.TypeParamsContext ctx) {
        //TODO add datatype call possibility
        return new VariableDeclarator((Type) this.visit(ctx.type()), (this.visit(ctx.id())).toString());
    }

    @Override
    public Node visitField(SmartMLParser.FieldContext ctx) {
        return new VariableDeclarator((Type) this.visit(ctx.type()), (this.visit(ctx.id())).toString());
    }

    @Override
    public Node visitId(SmartMLParser.IdContext ctx){
        return new SimpleName(ctx.getText());
    }

    @Override
    public Node visitType(SmartMLParser.TypeContext ctx) {
        switch (ctx.getText()) {
            case "address":
                //TODO: Write address Class and add other types
                return new ClassOrInterfaceType(null, "Address");
            case "bool":
                return PrimitiveType.booleanType();
            case "string":
                return new ClassOrInterfaceType(null, "String");
            case "int":
                return PrimitiveType.intType();
            default:
                return new ClassOrInterfaceType(null, "ERROR_TYPE");
        }
    }

}
