package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import ASTVisitor.ASTnodes.ProgramNode;
import ASTVisitor.Lexer.CharStream;
import ASTVisitor.Lexer.CodeScanner;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.CharArrayReader;
import java.util.Arrays;

import static ASTVisitor.Lexer.TokenType.*;
import static java.util.Arrays.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class testASTParser {

    CharArrayReader reader;
    CharStream charStream;
    ASTParser p;

    String inputString = """
            gem heltal 3 som x
            gem decimaltal 5,5 som y
            gem tekst "test" som eksempel
            gem boolsk falsk som livsLyst
            
            hvis x er 3:
                sæt x til 1
                print eksempel
            .
            
            hvis y > 5:
                kør test
                sæt y til 8
            .
            
            hvis y < 5:
                gentag test 2 gange
                sæt y til 2
            .
            
            hvis livsLyst ikke er falsk:
                sæt y til 3
                sæt x til 9
            .    
            
            hvis livsLyst er falsk:
                gentag test 2 gange
            .
            
            
            rutine test:
                print x
                print y
            .
            """;

    public ASTParser makeASTParser(String str){
        CharArrayReader reader = new CharArrayReader(str.toCharArray());
        CharStream charStream = new CharStream(reader);

        CodeScanner.initialize(charStream);

        ASTParser p = new ASTParser(charStream);

        return p;
    }

    @Test
    public void testProg(){
        ASTParser parser = makeASTParser("""
                                             gem heltal 3 som a
                                             
                                             
                                             print a
                                             """);
        AST dcl = new HeltalDcl(new HeltalLiteral("3"), "a");
        AST print = new PrintNode(new IdNode("a"));
        AST ast = new ProgramNode(asList(dcl, print));
        assertEquals(ast, parser.prog());
    }

    @Test
    public void testLines() {
        ASTParser parser = makeASTParser("""
                                             gem heltal 3 som x
                                             sæt x til 2
                                             print x
                                             """);
        AST dcl = new HeltalDcl(new HeltalLiteral("3"), "x");
        AST ass = new AssignNode("x", new HeltalLiteral("2"));
        AST print = new PrintNode(new IdNode("x"));

        assertEquals(Arrays.asList(dcl, ass, print), parser.lines());
    }

    @Test
    public void testLinesIllegal() {
        ASTParser parser = makeASTParser("x er 2");

        assertThrows(Error.class, parser::lines);
    }

    @Test
    public void testLine() {
        ASTParser parser = makeASTParser("""
                                             rutine test:
                                                sæt x til 3
                                             .
                                             """);
        AST rutine = new FuncDclNode("test", Arrays.asList(
                new AssignNode("x", new HeltalLiteral("3"))));

        assertEquals(rutine, parser.line());
    }

    @Test
    public void testDcl() {
        ASTParser parser = makeASTParser("""
                                             gem heltal 3 som x
                                             """);
        AST dcl = new HeltalDcl(new HeltalLiteral("3"), "x");
        assertEquals(dcl, parser.dcl());
    }

    @Test
    public void testStmts() {
        ASTParser parser = makeASTParser("""
                                             sæt tekstTest til "holy"
                                             sæt tekstTest til "grail"
                                             print tekstTest
                                             
                                             
                                             .
                                             """);
        AST dcl = new AssignNode("tekstTest", new TekstLiteral("holy"));
        AST ass = new AssignNode("tekstTest", new TekstLiteral("grail"));
        AST print = new PrintNode(new IdNode("tekstTest"));

        assertEquals(Arrays.asList(dcl, ass, print), parser.stmts());
    }

    @Test
    public void testStmt(){
        ASTParser parser = makeASTParser("""
                                             sæt x til 5     
                                             """);
        AST dcl = new AssignNode( "x", new HeltalLiteral("5"));
        assertEquals(dcl, parser.stmt());
    }

    @Test
    public void testExpr(){
        ASTParser parser = makeASTParser("""
                                             ikke (x < (3 + (10-5) * 2) / 2,5 eller y > x og x ikke er sandt) er falsk:
                                             """);
        AST neq = new BinaryComputing("ikke", new IdNode("x"), new BoolskLiteral("sandt"));
        AST gt = new BinaryComputing(">", new IdNode("y"), new IdNode("x"));
        AST og = new BinaryComputing("OG", gt, neq);

        AST minus = new BinaryComputing("-", new HeltalLiteral("10"), new HeltalLiteral("5"));
        AST times = new BinaryComputing("*", minus, new HeltalLiteral("2"));
        AST plus = new BinaryComputing("+", new HeltalLiteral("3"), times);
        AST div = new BinaryComputing("/", plus, new DecimaltalLiteral("2,5"));
        AST lt = new BinaryComputing("<", new IdNode("x"), div);

        AST eller = new BinaryComputing("ELLER", lt, og);
        AST not = new UnaryComputing("IKKE", eller);
        AST er = new BinaryComputing("er", not, new BoolskLiteral("falsk"));

        assertEquals(er, parser.expr());
    }

    @Test
    public void testOr_Expr2Illegal(){
        ASTParser parser = makeASTParser("""
                                             x er 3 eller < y
                                             """);
        assertThrows(Error.class, parser::or_expr2);
    }

    @Test
    public void testAnd_Expr2Illegal(){
        ASTParser parser = makeASTParser("""
                                             x er 3 og < y
                                             """);
        assertThrows(Error.class, parser::and_expr2);
    }

    @Test
    public void testEquality_Expr2Illegal(){
        ASTParser parser = makeASTParser("""
                                             x er
                                             """);
        assertThrows(Error.class, parser::equalityExpr2);
    }

    @Test
    public void testEquality_Expr2(){
        ASTParser parser = makeASTParser("""
                                             x ikke er 2:
                                             """);
        assertDoesNotThrow(parser::equalityExpr);
    }

    @Test
    public void testRel_Expr2Illegal(){
        ASTParser parser = makeASTParser("""
                                             s <
                                             """);
        assertThrows(Error.class, parser::relExpr2);
    }

    @Test
    public void testSum_Expr2Illegal(){
        ASTParser parser = makeASTParser("""
                                             3 +
                                             """);
        assertThrows(Error.class, parser::sumExpr2);
    }

    @Test
    public void testProduct_Expr2Illegal(){
        ASTParser parser = makeASTParser("""
                                             3 * 
                                             """);
        assertThrows(Error.class, parser::productExpr2);
    }

    @Test
    public void testFactorIllegal(){
        ASTParser parser = makeASTParser("""
                                             """);
        assertThrows(Error.class, parser::factor);
    }
}
