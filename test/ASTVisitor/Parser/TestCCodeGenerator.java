package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import ASTVisitor.Lexer.CharStream;
import ASTVisitor.Lexer.CodeScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.CharArrayReader;

import static org.junit.jupiter.api.Assertions.*;

public class TestCCodeGenerator {

    CCodeGenerator generator;
    @BeforeEach
    void beforeEach() {
        generator = new CCodeGenerator();
    }
    @Test
    void testEmit() {
        generator.emit("Test text");
        generator.emit(" added.");
        assertEquals("Test text added.", generator.getCode());
    }

    @Test
    void testExpr1() {
        BinaryComputing expr = new BinaryComputing("er",
                new BinaryComputing("+", new HeltalLiteral("5"),
                        new DecimaltalLiteral("4,3")),
                new BinaryComputing("-", new HeltalLiteral("10"),
                        new DecimaltalLiteral("0,7")));
        generator.visit(expr);
        assertEquals("5 + 4.3 == 10 - 0.7", generator.getCode());
    }

    @Test
    void testExpr2() {
        UnaryComputing expr = new UnaryComputing("paren",
                new BinaryComputing("+", new HeltalLiteral("5"),
                        new DecimaltalLiteral("4,3")));
        generator.visit(expr);
        assertEquals("(5 + 4.3)", generator.getCode());
    }

    @Test
    void testStrings() {
        String str = """
                      gem tekst "Haj" som a
                      gem tekst "hallo" som b
                      
                      sæt a til "hej"
                      
                      print a
                      print b
                      """;

        CharArrayReader reader = new CharArrayReader(str.toCharArray());
        CharStream charStream = new CharStream(reader);

        CodeScanner.initialize(charStream);

        ASTParser p = new ASTParser(charStream);

        AST program = p.prog();

        program.accept(new SymbolTableFilling());
        program.accept(new TypeChecker());

        program.accept(generator);
        assertEquals("""
                    #include <string.h>
                    #include <stdlib.h>
                    #include <stdio.h>
                                         
                    char* a;
                    char* b;
                                         
                    int free_memory () {
                        free(a);
                        free(b);
                    }
                                         
                    int main() {
                        a = malloc(4 * sizeof(char));
                        strcpy(a, "Haj");
                        b = malloc(6 * sizeof(char));
                        strcpy(b, "hallo");
                        a = "hej";
                        printf("%s\\n", a);
                        printf("%s\\n", b);
                        free_memory();
                        return 0;
                    }
                    
                    """, generator.getCode());
    }

    @Test
    void testPrint() {
        PrintNode print = new PrintNode(new HeltalLiteral("3"));
        print.accept(new SymbolTableFilling());
        print.accept(new TypeChecker());
        generator.visit(print);

        assertEquals("printf(\"%d\\n\", 3);", generator.getCode());
    }

}
