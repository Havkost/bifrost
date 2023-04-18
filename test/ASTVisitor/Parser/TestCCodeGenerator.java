package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import ASTVisitor.Exceptions.FileWriterIOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestCCodeGenerator {

    CCodeGenerator generator;
    @BeforeEach
    public void beforeEach() {
        AST.clearSymbolTable();
        generator = new CCodeGenerator(false);
    }
    @Test
    public void testEmit() {
        generator.emit("Test text");
        generator.emit(" added.");
        assertEquals("Test text added.", generator.getCode());
    }

    @Test
    public void testExpr1() {
        BinaryComputing expr = new BinaryComputing("er",
                new BinaryComputing("+", new HeltalLiteral("5"),
                        new DecimaltalLiteral("4,3")),
                new BinaryComputing("-", new HeltalLiteral("10"),
                        new DecimaltalLiteral("0,7")));
        generator.visit(expr);
        assertEquals("5 + 4.3 == 10 - 0.7", generator.getCode());
    }

    @Test
    public void testExpr2() {
        UnaryComputing expr = new UnaryComputing("paren",
                new BinaryComputing("+", new HeltalLiteral("5"),
                        new DecimaltalLiteral("4,3")));
        generator.visit(expr);
        assertEquals("(5 + 4.3)", generator.getCode());
    }

    @Test
    public void testExpr3() {
        UnaryComputing expr = new UnaryComputing("ikke",
                new BoolskLiteral("falsk"));
        generator.visit(expr);
        assertEquals("!false", generator.getCode());
    }

    @Test
    public void testStrings() {
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

        prog.accept(new SymbolTableFilling());
        prog.accept(new TypeChecker());

        prog.accept(generator);

        assertEquals("""
                    #include <string.h>
                    #include <stdlib.h>
                    #include <stdio.h>
                    #include <stdbool.h>
                                         
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
                        realloc(a, 3 * sizeof(char));
                        a = "hej";
                        printf("%s\\n", a);
                        printf("%s\\n", b);
                        free_memory();
                        return 0;
                    }
                    
                    """, generator.getCode());
    }

    @Test
    public void testPrint() {
        PrintNode print = new PrintNode(new HeltalLiteral("3"));
        print.accept(new SymbolTableFilling());
        print.accept(new TypeChecker());
        generator.visit(print);

        assertEquals("printf(\"%d\\n\", 3);", generator.getCode());
    }

    @Test
    public void testDcls() {
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
                    #include <stdbool.h>
                                         
                    int a;
                    char* b;
                    double c;
                    bool d;
                                         
                    int free_memory () {
                        free(b);
                    }
                                         
                    int main() {
                        a = 3;
                        b = malloc(5 * sizeof(char));
                        strcpy(b, "test");
                        c = 11.5;
                        d = false;
                        free_memory();
                        return 0;
                    }
                    
                     """, generator.getCode());
    }

    @Test
    public void testIfNode() {
        IfNode ifNode = new IfNode(new BinaryComputing("er",
                            new HeltalLiteral("3"), new HeltalLiteral("3")),
                            List.of(new FuncNode("test")));
        ifNode.accept(generator);

        assertEquals("""
                    if (3 == 3) {
                        test();
                    }""", generator.getCode());
    }

    @Test
    public void testFuncDcl() {
        FuncDclNode funcDclNode = new FuncDclNode("test", List.of(
                new IfNode(new BinaryComputing("ikke er", new IdNode("a"),
                        new HeltalLiteral("3")),
                    List.of(new AssignNode("a", new HeltalLiteral("5"))))));
        funcDclNode.accept(generator);

        assertEquals("""
                void test() {
                    if (a != 3) {
                        a = 5;
                    }
                }""", generator.getCode());
    }

    @Test
    void testLoopNode() {
        LoopNode loopNode = new LoopNode("test", new HeltalLiteral("5"));

        loopNode.accept(generator);
        assertEquals("""
                for(int __i = 0; __i < (5); __i++) {
                    test();
                }""", generator.getCode());
    }

    @Test
    void testProg() {
        ProgramNode programNode = new ProgramNode(List.of(
                new HeltalDcl(new HeltalLiteral("3"), "a"),
                new FuncDclNode("func", List.of(
                        new AssignNode("a", new HeltalLiteral("8")),
                        new IfNode(new BinaryComputing("er", new HeltalLiteral("3"),
                                new HeltalLiteral("3")), List.of(new AssignNode("a", new HeltalLiteral("5"))))
                ))
        ));

        programNode.accept(new SymbolTableFilling());
        programNode.accept(new TypeChecker());

        programNode.accept(generator);

        assertEquals("""
                #include <string.h>
                #include <stdlib.h>
                #include <stdio.h>
                #include <stdbool.h>
                                
                int a;
                void func();
                                
                int free_memory () {
                }
                                
                int main() {
                    a = 3;
                    free_memory();
                    return 0;
                }
                                
                void func() {
                    a = 8;
                    if (3 == 3) {
                        a = 5;
                    }
                }
                """, generator.getCode());
    }

    @Test
    void testFileWrite() throws IOException {
        File file = new File("file.c");
        file.createNewFile();
        FileWriter writer;
        writer = new FileWriter(file);

        generator = new CCodeGenerator(false, writer);
        ProgramNode programNode = new ProgramNode(List.of(
                new HeltalDcl(new HeltalLiteral("3"), "a"),
                new FuncDclNode("func", List.of(
                        new AssignNode("a", new HeltalLiteral("8")),
                        new IfNode(new BinaryComputing("er", new HeltalLiteral("3"),
                                new HeltalLiteral("3")), List.of(new AssignNode("a", new HeltalLiteral("5"))))
                ))
        ));

        programNode.accept(generator);

        assertEquals("""
                #include <string.h>
                #include <stdlib.h>
                #include <stdio.h>
                #include <stdbool.h>
                                
                                
                int free_memory () {
                }
                                
                int main() {
                    a = 3;
                    free_memory();
                    return 0;
                }
                                
                void func() {
                    a = 8;
                    if (3 == 3) {
                        a = 5;
                    }
                }
                """, Files.readString(file.toPath()));
    }

    @Test
    void testFileWriteError() throws IOException {
        File file = new File("file.c");
        assertTrue(file.createNewFile());
        FileWriter writer;
        writer = new FileWriter(file);

        writer.close();

        generator = new CCodeGenerator(false, writer);
        ProgramNode programNode = new ProgramNode(List.of(
                new HeltalDcl(new HeltalLiteral("3"), "a"),
                new FuncDclNode("func", List.of(
                        new AssignNode("a", new HeltalLiteral("8")),
                        new IfNode(new BinaryComputing("er", new HeltalLiteral("3"),
                                new HeltalLiteral("3")), List.of(new AssignNode("a", new HeltalLiteral("5"))))
                ))
        ));

        assertThrows(FileWriterIOException.class, () -> programNode.accept(generator));

        file.deleteOnExit();
    }
}
