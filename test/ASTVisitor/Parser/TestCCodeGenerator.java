package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import ASTVisitor.Lexer.CharStream;
import ASTVisitor.Lexer.CodeScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.CharArrayReader;
import java.util.Arrays;

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
    void testExpr3() {
        UnaryComputing expr = new UnaryComputing("ikke",
                new BoolskLiteral("falsk"));
        generator.visit(expr);
        assertEquals("!false", generator.getCode());
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

        TekstDcl line1 = new TekstDcl(new TekstLiteral("Haj"), "a");
        TekstDcl line2 = new TekstDcl(new TekstLiteral("hallo"), "b");

        AssignNode line3 = new AssignNode("a", new TekstLiteral("hej"));

        PrintNode line4 = new PrintNode(new IdNode("a"));
        PrintNode line5 = new PrintNode(new IdNode("b"));

        ProgramNode prog = new ProgramNode(Arrays.asList(line1, line2, line3, line4, line5));

        System.out.println(AST.getSymbolTable());

        prog.accept(new SymbolTableFilling());
        prog.accept(new TypeChecker());

        prog.accept(generator);

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

    @Test
    void testDcls() {
        HeltalDcl dcl1 = new HeltalDcl(new HeltalLiteral("3"), "a");
        TekstDcl dcl2 = new TekstDcl(new TekstLiteral("test"), "b");
        DecimaltalDcl dcl3 = new DecimaltalDcl(new DecimaltalLiteral("11,5"), "c");
        BoolskDcl dcl4 = new BoolskDcl(new BoolskLiteral("falsk"), "d");

        ProgramNode prog = new ProgramNode(Arrays.asList(dcl1, dcl2, dcl3, dcl4));

        prog.accept(new SymbolTableFilling());
        prog.accept(new TypeChecker());
        generator.visit(prog);
        assertEquals("""
                    #include <string.h>
                    #include <stdlib.h>
                    #include <stdio.h>
                                         
                    int a;
                    char* b;
                    double c;
                    boolean d;
                                         
                    int free_memory () {
                        free(b);
                    }
                                         
                    int main() {
                        a = 3;
                        b = malloc(5 * sizeof(char));
                        strcpy(b, "test");
                        c = 11.5;
                       \s
                        free_memory();
                        return 0;
                    }
                    
                     """, generator.getCode());
    }
}
