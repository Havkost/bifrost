package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestPrettyPrinting {

    Prettyprinting prettyPrinter;
    @BeforeEach
    void setUp() {
        prettyPrinter = new Prettyprinting(false);
    }

    @Test
    void testEmit() {
        prettyPrinter.emit("test");
        assertEquals("test", prettyPrinter.getCode());
    }

    @Test
    void testIndent() {
        prettyPrinter.indent(2);
        assertEquals("        ", prettyPrinter.getCode());
    }

    @Test
    void testBoolskDcl() {
        BoolskDcl boolskDcl = new BoolskDcl(new BoolskLiteral("sandt"), new IdNode("a"));
        boolskDcl.accept(prettyPrinter);

        assertEquals("gem boolsk sandt som a", prettyPrinter.getCode());
    }

    @Test
    void testTekstDcl() {
        TekstDcl tekstDcl = new TekstDcl(new TekstLiteral("test"), new IdNode("a"));
        tekstDcl.accept(prettyPrinter);

        assertEquals("gem tekst \"test\" som a", prettyPrinter.getCode());
    }

    @Test
    void testHeltalDcl() {
        HeltalDcl heltalDcl = new HeltalDcl(new HeltalLiteral("37"), new IdNode("a"));
        heltalDcl.accept(prettyPrinter);

        assertEquals("gem heltal 37 som a", prettyPrinter.getCode());
    }

    @Test
    void testDecimaltalDcl() {
        DecimaltalDcl decimaltalDcl = new DecimaltalDcl(new DecimaltalLiteral("3,39"), new IdNode("a"));
        decimaltalDcl.accept(prettyPrinter);

        assertEquals("gem decimaltal 3,39 som a", prettyPrinter.getCode());
    }

    @Test
    void testBinaryComputing() {
        BinaryComputing binaryComputing = new BinaryComputing("er",
                new BinaryComputing("+", new HeltalLiteral("3"), new HeltalLiteral("8")),
                new HeltalLiteral("11"));

        binaryComputing.accept(prettyPrinter);
        assertEquals("3 + 8 er 11", prettyPrinter.getCode());
    }

    @Test
    void testUnaryComputing() {
        UnaryComputing unaryComputing = new UnaryComputing("ikke",
                new UnaryComputing("paren", new BoolskLiteral("sandt")));
        unaryComputing.accept(prettyPrinter);

        assertEquals("ikke (sandt)", prettyPrinter.getCode());
    }

    @Test
    void testAssign() {
        AssignNode assignNode = new AssignNode(new IdNode("a"), new HeltalLiteral("5"));
        assignNode.accept(prettyPrinter);

        assertEquals("sæt a til 5", prettyPrinter.getCode());
    }

    @Test
    void testAssignField() {
        AssignNode assignNode = new AssignNode(new IdNode("lysstyrke", "lampe1"),
                new HeltalLiteral("50"));
        assignNode.accept(prettyPrinter);

        assertEquals("sæt lysstyrke for lampe1 til 50", prettyPrinter.getCode());
    }

    @Test
    void testPrintNode() {
        PrintNode printNode = new PrintNode(new IdNode("a"));
        printNode.accept(prettyPrinter);

        assertEquals("print a", prettyPrinter.getCode());
    }

    @Test
    void testLoopNode() {
        LoopNode loopNode = new LoopNode("id", new HeltalLiteral("3"));
        loopNode.accept(prettyPrinter);

        assertEquals("gentag id 3 gange", prettyPrinter.getCode());
    }

    @Test
    void testIfNode() {
        IfNode ifNode = new IfNode(new UnaryComputing("ikke", new BoolskLiteral("falsk")),
                    List.of(new AssignNode(new IdNode("a"), new HeltalLiteral("3")), new FuncNode("func"))
                );
        ifNode.accept(prettyPrinter);

        assertEquals("""
                hvis ikke falsk
                    sæt a til 3
                    kør func
                .""", prettyPrinter.getCode());
    }

    @Test
    void testFuncNode() {
        FuncNode funcNode = new FuncNode("a");
        funcNode.accept(prettyPrinter);

        assertEquals("kør a", prettyPrinter.getCode());
    }

    @Test
    void testFuncDclNode() {
        FuncDclNode funcDclNode = new FuncDclNode("func", List.of(
                new AssignNode(new IdNode("a"), new HeltalLiteral("5")),
                new AssignNode(new IdNode("b"), new TekstLiteral("Test"))
        ));
        funcDclNode.accept(prettyPrinter);

        assertEquals("""
                
                rutine func
                    sæt a til 5
                    sæt b til \"Test\"
                .""", prettyPrinter.getCode());
    }

    @Test
    void testDeviceDcl() {
        DeviceNode deviceNode = new DeviceNode("lampe1", List.of(
                new TekstDcl(new TekstLiteral("test"), new IdNode("besked", "lampe1")),
                new HeltalDcl(new HeltalLiteral("3"), new IdNode("lysstyrke", "lampe1")),
                new DecimaltalDcl(new DecimaltalLiteral("11,35"), new IdNode("temperatur", "lampe1")),
                new BoolskDcl(new BoolskLiteral("sandt"), new IdNode("tændt", "lampe1"))
        ), "test");
        deviceNode.accept(new SymbolTableFilling());
        deviceNode.accept(new TypeChecker());
        deviceNode.accept(prettyPrinter);

        assertEquals("""
                gem enhed "test" med
                    tekst "test" som besked
                    heltal 3 som lysstyrke
                    decimaltal 11,35 som temperatur
                    boolsk sandt som tændt
                som lampe1
                """, prettyPrinter.getCode());
    }

    @Test
    void testProgramNode() {
        ProgramNode prog = new ProgramNode(List.of(
                new TekstDcl(new TekstLiteral("Tekst"), new IdNode("b")),
                new FuncDclNode("func", List.of(
                        new AssignNode(new IdNode("a"), new HeltalLiteral("5")),
                        new AssignNode(new IdNode("b"), new TekstLiteral("Test"))
                )),
                new HeltalDcl(new HeltalLiteral("3"), new IdNode("a")),
                new IfNode(new BinaryComputing("er", new DecimaltalLiteral("3,45"),
                                new DecimaltalLiteral("5,34")),
                        List.of(new AssignNode(new IdNode("b"), new TekstLiteral("Hello world")))
                )
        ));
        prog.accept(prettyPrinter);

        assertEquals("""
                gem tekst "Tekst" som b
                
                rutine func
                    sæt a til 5
                    sæt b til "Test"
                .
                gem heltal 3 som a
                hvis 3,45 er 5,34
                    sæt b til "Hello world"
                .
                """, prettyPrinter.getCode());
    }

    @Test
    void testIdNode() {
        IdNode idNode1 = new IdNode("test");
        idNode1.accept(prettyPrinter);

        assertEquals("test", prettyPrinter.getCode());
    }
    @Test
    void testFieldIdNode() {
        IdNode idNode1 = new IdNode("lysstyrke", "lampe1");
        idNode1.accept(prettyPrinter);

        assertEquals("lysstyrke for lampe1", prettyPrinter.getCode());
    }

    @Test
    void testKlokken() {
        new KlokkenNode().accept(prettyPrinter);

        assertEquals("klokken", prettyPrinter.getCode());
    }

    @Test
    void testTidNode() {
        new TidNode(11, 30).accept(prettyPrinter);

        assertEquals("11:30", prettyPrinter.getCode());
    }

    @Test
    void testTidDclNode() {
        new TidDcl(new TidNode(11, 30), new IdNode("test")).accept(prettyPrinter);

        assertEquals("gem tid 11:30 som test", prettyPrinter.getCode());
    }

    @Test
    void testTidDclFieldNode() {
        new TidDcl(new TidNode(11, 30), new IdNode("id", "parent")).accept(prettyPrinter);

        assertEquals("tid 11:30 som id", prettyPrinter.getCode());
    }
}
