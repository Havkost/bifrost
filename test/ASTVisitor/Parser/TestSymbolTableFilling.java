package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestSymbolTableFilling {
    @Test
    void testHeltalDcl() {
        HeltalDcl heltalDcl = new HeltalDcl(new HeltalLiteral("3"), "a");
        heltalDcl.accept(new SymbolTableFilling());

        assertEquals(AST.DataTypes.HELTAL, AST.getSymbolTable().get("a"));
    }

    @Test
    void testHeltalAlreadyDeclared() {
        BoolskDcl boolskDcl = new BoolskDcl(new BoolskLiteral("sandt"), "a");
        HeltalDcl heltalDcl = new HeltalDcl(new HeltalLiteral("3"), "a");
        ProgramNode prog = new ProgramNode(Arrays.asList(boolskDcl, heltalDcl));

        assertThrows(Error.class, () -> {prog.accept(new SymbolTableFilling());});
    }

    @Test
    void testDecimaltalDcl() {
        DecimaltalDcl decimaltalDcl = new DecimaltalDcl(new DecimaltalLiteral("3.123"), "c");
        decimaltalDcl.accept(new SymbolTableFilling());

        assertEquals(AST.DataTypes.DECIMALTAL, AST.getSymbolTable().get("c"));
    }

    @Test
    void testDecimaltalAlreadyDeclared() {
        DecimaltalDcl decimaltalDcl1 = new DecimaltalDcl(new DecimaltalLiteral("3,132"), "a");
        DecimaltalDcl decimaltalDcl2 = new DecimaltalDcl(new DecimaltalLiteral("8.31"), "a");
        ProgramNode prog = new ProgramNode(Arrays.asList(decimaltalDcl1, decimaltalDcl2));

        assertThrows(Error.class, () -> {prog.accept(new SymbolTableFilling());});
    }

    @Test
    void testTekstDcl() {
        TekstDcl tekstDcl = new TekstDcl(new TekstLiteral("Test"), "a");
        tekstDcl.accept(new SymbolTableFilling());

        assertEquals(AST.DataTypes.TEKST, AST.getSymbolTable().get("a"));
    }

    @Test
    void testTekstAlreadyDeclared() {
        BoolskDcl boolskDcl = new BoolskDcl(new BoolskLiteral("sandt"), "a");
        TekstDcl tekstDcl = new TekstDcl(new TekstLiteral("test"), "a");
        ProgramNode prog = new ProgramNode(Arrays.asList(boolskDcl, tekstDcl));

        assertThrows(Error.class, () -> {prog.accept(new SymbolTableFilling());});
    }

    @Test
    void testBoolskDcl() {
        BoolskDcl boolskDcl = new BoolskDcl(new BoolskLiteral("sandt"), "a");
        boolskDcl.accept(new SymbolTableFilling());

        assertEquals(AST.DataTypes.BOOLSK, AST.getSymbolTable().get("a"));
    }

    @Test
    void testBoolskAlreadyDeclared() {
        BoolskDcl boolskDcl1 = new BoolskDcl(new BoolskLiteral("sandt"), "a");
        BoolskDcl boolskDcl2 = new BoolskDcl(new BoolskLiteral("falsk"), "a");
        ProgramNode prog = new ProgramNode(Arrays.asList(boolskDcl1, boolskDcl2));

        assertThrows(Error.class, () -> {prog.accept(new SymbolTableFilling());});
    }

    @Test
    void testFuncDcl() {
        BoolskDcl dcl = new BoolskDcl(new BoolskLiteral("sandt"), "b");
        AssignNode ass = new AssignNode("b", new BoolskLiteral("falsk"));
        IfNode ifNode = new IfNode(new UnaryComputing("ikke", new BinaryComputing("+",
                new HeltalLiteral("3"), new IdNode("b"))), List.of(new IfNode(new BoolskLiteral("sandt"), null)));
        FuncNode funcNode = new FuncNode("a");
        LoopNode loopNode = new LoopNode("a", new HeltalLiteral("5"));
        FuncDclNode funcDclNode = new FuncDclNode("a", List.of(ass, ifNode, funcNode, loopNode));
        ProgramNode prog = new ProgramNode(Arrays.asList(dcl, funcDclNode));
        prog.accept(new SymbolTableFilling());

        assertEquals(AST.DataTypes.RUTINE, AST.getSymbolTable().get("a"));
    }

    @Test
    void testFuncAlreadyDeclared() {
        DecimaltalDcl decimaltalDcl = new DecimaltalDcl(new DecimaltalLiteral("3,32"), "a");

        FuncDclNode funcDclNode = new FuncDclNode("a", null);
        ProgramNode prog = new ProgramNode(Arrays.asList(decimaltalDcl, funcDclNode));

        assertThrows(Error.class, () -> {prog.accept(new SymbolTableFilling());});
    }

}
