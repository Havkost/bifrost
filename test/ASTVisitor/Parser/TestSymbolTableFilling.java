package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import ASTVisitor.Exceptions.VariableAlreadyDeclaredException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestSymbolTableFilling {
    @Test
    void testHeltalDcl() {
        HeltalDcl heltalDcl = new HeltalDcl(new HeltalLiteral("3"), new IdNode("a"));
        heltalDcl.accept(new SymbolTableFilling());

        assertEquals(AST.DataTypes.HELTAL, AST.getSymbolTable().get("a"));
    }

    @Test
    void testHeltalAlreadyDeclared() {
        BoolskDcl boolskDcl = new BoolskDcl(new BoolskLiteral("sandt"), new IdNode("a"));
        HeltalDcl heltalDcl = new HeltalDcl(new HeltalLiteral("3"), new IdNode("a"));
        ProgramNode prog = new ProgramNode(Arrays.asList(boolskDcl, heltalDcl));

        assertThrows(VariableAlreadyDeclaredException.class, () -> {prog.accept(new SymbolTableFilling());});
    }

    @Test
    void testDecimaltalDcl() {
        DecimaltalDcl decimaltalDcl = new DecimaltalDcl(new DecimaltalLiteral("3.123"), new IdNode("c"));
        decimaltalDcl.accept(new SymbolTableFilling());

        assertEquals(AST.DataTypes.DECIMALTAL, AST.getSymbolTable().get("c"));
    }

    @Test
    void testDecimaltalAlreadyDeclared() {
        DecimaltalDcl decimaltalDcl1 = new DecimaltalDcl(new DecimaltalLiteral("3,132"), new IdNode("a"));
        DecimaltalDcl decimaltalDcl2 = new DecimaltalDcl(new DecimaltalLiteral("8.31"), new IdNode("a"));
        ProgramNode prog = new ProgramNode(Arrays.asList(decimaltalDcl1, decimaltalDcl2));

        assertThrows(VariableAlreadyDeclaredException.class, () -> {prog.accept(new SymbolTableFilling());});
    }

    @Test
    void testTekstDcl() {
        TekstDcl tekstDcl = new TekstDcl(new TekstLiteral("Test"), new IdNode("a"));
        tekstDcl.accept(new SymbolTableFilling());

        assertEquals(AST.DataTypes.TEKST, AST.getSymbolTable().get("a"));
    }

    @Test
    void testTekstAlreadyDeclared() {
        BoolskDcl boolskDcl = new BoolskDcl(new BoolskLiteral("sandt"), new IdNode("a"));
        TekstDcl tekstDcl = new TekstDcl(new TekstLiteral("test"), new IdNode("a"));
        ProgramNode prog = new ProgramNode(Arrays.asList(boolskDcl, tekstDcl));

        assertThrows(VariableAlreadyDeclaredException.class, () -> {prog.accept(new SymbolTableFilling());});
    }

    @Test
    void testBoolskDcl() {
        BoolskDcl boolskDcl = new BoolskDcl(new BoolskLiteral("sandt"), new IdNode("a"));
        boolskDcl.accept(new SymbolTableFilling());

        assertEquals(AST.DataTypes.BOOLSK, AST.getSymbolTable().get("a"));
    }

    @Test
    void testBoolskAlreadyDeclared() {
        BoolskDcl boolskDcl1 = new BoolskDcl(new BoolskLiteral("sandt"), new IdNode("a"));
        BoolskDcl boolskDcl2 = new BoolskDcl(new BoolskLiteral("falsk"), new IdNode("a"));
        ProgramNode prog = new ProgramNode(Arrays.asList(boolskDcl1, boolskDcl2));

        assertThrows(VariableAlreadyDeclaredException.class, () -> {prog.accept(new SymbolTableFilling());});
    }

    @Test
    void testFuncDcl() {
        BoolskDcl dcl = new BoolskDcl(new BoolskLiteral("sandt"), new IdNode("b"));
        AssignNode ass = new AssignNode(new IdNode("b"), new BoolskLiteral("falsk"));
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
        DecimaltalDcl decimaltalDcl = new DecimaltalDcl(new DecimaltalLiteral("3,32"), new IdNode("a"));

        FuncDclNode funcDclNode = new FuncDclNode("a", null);
        ProgramNode prog = new ProgramNode(Arrays.asList(decimaltalDcl, funcDclNode));

        assertThrows(VariableAlreadyDeclaredException.class, () -> {prog.accept(new SymbolTableFilling());});
    }

    @Test
    void testDeviceDcl() {
        DeviceNode deviceNode = new DeviceNode("enhed1", List.of(), "test");
        deviceNode.accept(new SymbolTableFilling());

        assertEquals(AST.DataTypes.DEVICE, AST.SymbolTable.get("enhed1"));
    }

    @Test
    void testDeviceDclAlreadyAssigned() {
        DeviceNode deviceNode1 = new DeviceNode("enhed1", List.of(), "test");
        DeviceNode deviceNode2 = new DeviceNode("enhed1", List.of(), "test");
        ProgramNode prog = new ProgramNode(List.of(deviceNode1, deviceNode2));


        assertThrows(VariableAlreadyDeclaredException.class, () -> prog.accept(new SymbolTableFilling()));

        try {
            prog.accept(new SymbolTableFilling());
        } catch (VariableAlreadyDeclaredException e) {
            assertEquals(0, e.getLine());
        }
    }

    @Test
    void testVariableAlreadyAssignedTidnode() {
        TidNode klok = new TidNode(10,10);
        TidDcl tid = new TidDcl(klok, new IdNode("hej"));
        TidDcl tid1 = new TidDcl(klok, new IdNode("hej"));
        ProgramNode prog = new ProgramNode(List.of(tid, tid1));

        assertThrows(VariableAlreadyDeclaredException.class, () -> prog.accept(new SymbolTableFilling()));

        try {
            prog.accept(new SymbolTableFilling());
        } catch (VariableAlreadyDeclaredException e) {
            assertEquals(0, e.getLine());
        }
    }

    @Test
    void testTidNodeDataType() {
        TidNode tid = new TidNode(10, 30);
        tid.accept(new SymbolTableFilling());
        tid.accept(new TypeChecker());
        assertEquals(tid.type, AST.DataTypes.TID);
    }

    @Test
    void testKlokkenNodeDataType() {
        KlokkenNode klokken = new KlokkenNode();
        klokken.accept(new SymbolTableFilling());
        klokken.accept(new TypeChecker());
        assertEquals(klokken.type, AST.DataTypes.TID);
    }
}
