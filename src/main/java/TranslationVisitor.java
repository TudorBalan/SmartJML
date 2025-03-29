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

        contracts.addAll(datatypes);
        contracts.addAll(resources);

        return new CompilationUnit(null, new NodeList<ImportDeclaration>(), contracts, null);
    }

    /*------------------------------------------------------------------
     * DATATYPES
     *------------------------------------------------------------------*/
    @Override
    public Node visitDatatypeDec(SmartMLParser.DatatypeDecContext ctx){
        //Datatype declarations are always final and public
        NodeList<Modifier> modifiers = new NodeList<>();
        modifiers.add(Modifier.publicModifier());
        modifiers.add(Modifier.finalModifier());

        //The name is changed to SL_"name"
        String name = "SL_" + ((SimpleName) this.visit(ctx.id())).asString();

        return new ClassOrInterfaceDeclaration(modifiers, false, name);
    }

    /*------------------------------------------------------------------
     * RESOURCES
     *------------------------------------------------------------------*/
    @Override
    public Node visitResourceDec(SmartMLParser.ResourceDecContext ctx) {
        //Datatype declarations are always final and public
        NodeList<Modifier> modifiers = new NodeList<>();
        modifiers.add(Modifier.publicModifier());

        //The name is changed to SL_"name"
        String name = "SL_" + ((SimpleName) this.visit(ctx.id())).asString();

        return new ClassOrInterfaceDeclaration(modifiers, false, name);
    }

    /*------------------------------------------------------------------
     * CONTRACTS
     *------------------------------------------------------------------*/
    @Override
    public Node visitContractDec(SmartMLParser.ContractDecContext ctx) {
        //Contracts declarations are always public
        NodeList<Modifier> modifiers = new NodeList<>();
        modifiers.add(Modifier.publicModifier());

        //The name is changed to SL_"name"
        String name = "SL_" + ((SimpleName) this.visit(ctx.contractId)).asString();

        return new ClassOrInterfaceDeclaration(modifiers, false, name);
    }

    @Override
    public Node visitId(SmartMLParser.IdContext ctx){
        return new SimpleName(ctx.getText());
    }

    @Override
    public Node visitType(SmartMLParser.TypeContext ctx) {
        switch (ctx.getText()) {
            case "address":
                //TODO: Add address Type
                return PrimitiveType.intType();
            case "bool":
                return PrimitiveType.booleanType();
            case "string":
                return new ClassOrInterfaceType(null, "String");
            case "int":
                return PrimitiveType.intType();
            default:
                return PrimitiveType.intType();
        }
    }

}
