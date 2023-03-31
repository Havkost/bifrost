package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import ASTVisitor.ASTnodes.ProgramNode;
import ASTVisitor.Exceptions.UnexpectedLineStart;
import ASTVisitor.Exceptions.UnexpectedTokenException;
import ASTVisitor.Lexer.CharStream;
import ASTVisitor.Lexer.CodeScanner;
import ASTVisitor.Lexer.Token;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.CharArrayReader;
import java.util.Arrays;

import static ASTVisitor.Lexer.TokenType.*;
import static java.util.Arrays.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class testASTParser {

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

        assertThrows(UnexpectedLineStart.class, parser::lines);
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
                                             gentag is 3 gange
                                             hvis 3 > 3:
                                                sæt x til 3.
                                             kør test
                                             .
                                             """);
        AST dcl = new AssignNode("tekstTest", new TekstLiteral("holy"));
        AST ass = new AssignNode("tekstTest", new TekstLiteral("grail"));
        AST print = new PrintNode(new IdNode("tekstTest"));
        AST gentag = new LoopNode("is", new HeltalLiteral("3"));
        AST hvis = new IfNode(new BinaryComputing(">", new HeltalLiteral("3"),
                new HeltalLiteral("3")),
                Arrays.asList(new AssignNode("x", new HeltalLiteral("3"))));
        AST func = new FuncNode("test");
        assertEquals(Arrays.asList(dcl, ass, print, gentag, hvis, func), parser.stmts());
    }

    @Test
    public void testStmtsIllegal() {
        ASTParser parser = makeASTParser("gem heltal 3 som x");
        assertThrows(Error.class, parser::stmts);
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
    public void testStmtIllegal(){
        ASTParser parser = makeASTParser("""
                                             gem heltal 3 som x     
                                             """);
        assertThrows(Error.class, parser::stmt);
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
    public void testExprIllegal() {
        ASTParser parser = makeASTParser("gem");
        assertThrows(Error.class, parser::expr);
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

    @Test
    public void testVarDclStr() {
        ASTParser parser = makeASTParser("tekst \"test\" som testTekst");
        AST dcl = new TekstDcl(new TekstLiteral("test"), "testTekst");
        assertEquals(dcl, parser.varDcl());
    }

    @Test
    public void testVarDclStrIllegal1() {
        ASTParser parser = makeASTParser("\"test\" om testTekst");
        assertThrows(Error.class, parser::varDcl);
    }

    @Test
    public void testVarDclInt() {
        ASTParser parser = makeASTParser("heltal 56 som number");
        AST dcl = new HeltalDcl(new HeltalLiteral("56"), "number");
        assertEquals(dcl, parser.varDcl());
    }

    @Test
    public void testVarDclIntIllegal() {
        ASTParser parser = makeASTParser("hetal 56 som number");
        assertThrows(Error.class, parser::varDcl);
    }

    @Test
    public void testVarDclFlt() {
        ASTParser parser = makeASTParser("decimaltal 56,132 som number");
        AST dcl = new DecimaltalDcl(new DecimaltalLiteral("56,132"), "number");
        assertEquals(dcl, parser.varDcl());
    }

    @Test
    public void testVarDclFltIllegal() {
        ASTParser parser = makeASTParser("decimaltl 56,45 som number");
        assertThrows(Error.class, parser::varDcl);
    }

    @Test
    public void testVarDclBool() {
        ASTParser parser = makeASTParser("boolsk falsk som val");
        AST dcl = new BoolskDcl(new BoolskLiteral("falsk"), "val");
        assertEquals(dcl, parser.varDcl());
    }

    @Test
    public void testVarDclBoolIllegal() {
        ASTParser parser = makeASTParser("boolsk nej som test");
        assertThrows(UnexpectedTokenException.class, parser::varDcl);
    }

    @Test
    public void testFunc() {
        ASTParser parser = makeASTParser("kør oogaBooga");
        AST func = new FuncNode("oogaBooga");
        assertEquals(func, parser.func());
    }

    @Test
    public void testFuncIllegal() {
        ASTParser parser = makeASTParser("kør oogaBooga s");
        assertThrows(Error.class, parser::varDcl);
    }

    @Test
    public void testPrintId() {
        ASTParser parser = makeASTParser("print hast");
        AST print = new PrintNode(new IdNode("hast"));

        assertEquals(print, parser.print());

    }

    @Test
    public void testPrintLit() {
        ASTParser parser = makeASTParser("print \"hast\"");
        AST print = new PrintNode(new TekstLiteral("hast"));

        assertEquals(print, parser.print());
    }

    @Test
    public void testPrintIllegal() {
        ASTParser parser = makeASTParser("print gem");

        assertThrows(Error.class, parser::print);
    }

    @Test
    public void testRepeat() {
        ASTParser parser = makeASTParser("gentag test 4 gange");
        AST gentag = new LoopNode("test", new HeltalLiteral("4"));
        assertEquals(gentag, parser.repeat());
    }

    @Test
    public void testRepeatIllegal() {
        ASTParser parser = makeASTParser("test 4 gange");

        assertThrows(UnexpectedTokenException.class, parser::repeat);
    }

    @Test
    public void testExpect() {
        ASTParser parser = makeASTParser("gem heltal 3 som x");
        Token token = new Token(GEM);
        assertEquals(token, parser.expect(GEM));
    }

    @Test
    public void testExpectIllegal() {
        ASTParser parser = makeASTParser("gem heltal 3 som x");

        assertThrows(UnexpectedTokenException.class, () -> parser.expect(SAET));
    }
}
