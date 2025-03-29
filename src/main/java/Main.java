import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.IOException;
import java.nio.file.*;
import parser.*;

public class Main {

    public static void main(String[] args) {
        try {
            CharStream input = CharStreams.fromFileName("src/main/java/testInput.sl");
            System.out.println("INPUT:\n" + input);

            SmartMLLexer lexer = new SmartMLLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            SmartMLParser parser = new SmartMLParser(tokens);
            ParseTree tree = parser.program();
            System.out.println("SmartML AST:\n" + tree.toStringTree(parser));
            TranslationVisitor visitor = new TranslationVisitor();
            Node translation = visitor.visit(tree);
            Files.write(Paths.get("src/main/java/testoutput.sl"), translation.toString().getBytes());

        } catch (IOException e) {
            System.out.println("ERROR while processing file:" + e.getMessage());
        }
    }
}
