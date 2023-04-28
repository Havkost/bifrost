package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import ASTVisitor.Exceptions.IllegalOperationTypeException;
import ASTVisitor.Exceptions.IllegalTypeAssignmentException;
import ASTVisitor.Exceptions.MissingTypeException;
import ASTVisitor.Exceptions.UnexpectedTypeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestTypechecker {

    public void binaryComputingTemplate(AST operand, AST.Operators op, AST.DataTypes type) {
        BinaryComputing bin;
        bin = new BinaryComputing(op.textual, operand, operand);
        ProgramNode ast = new ProgramNode(asList(bin));
        if (AST.getOperationResultType(op, type) == null) {
            assertThrows(IllegalOperationTypeException.class, () ->
                    ast.accept(new TypeChecker()));
        } else {
            assertDoesNotThrow(() ->
                    ast.accept(new TypeChecker())
            );
        }
    }

    public void unaryComputingTemplate(AST operand, AST.Operators op, AST.DataTypes type) {
        UnaryComputing una;
        una = new UnaryComputing(op.textual, operand);
        ProgramNode ast = new ProgramNode(asList(una));
        if (AST.getOperationResultType(op, type) == null) {
            assertThrows(MissingTypeException.class, () ->
                    ast.accept(new TypeChecker()));
        } else {
            assertDoesNotThrow(() ->
                    ast.accept(new TypeChecker())
            );
        }
    }

    // TYPE DCLS
    @Test
    public void testHeltalDclTypecheck() {

        HeltalDcl dcl = new HeltalDcl(new HeltalLiteral("4"), "a");
        ProgramNode ast = new ProgramNode(asList(dcl));
        assertDoesNotThrow(() ->
            ast.accept(new TypeChecker())
        );
    }

    @Test
    public void testHeltalDclTypecheckThrows() {
        HeltalDcl dcl1 = new HeltalDcl(new DecimaltalLiteral("4,2"), "b");
        HeltalDcl dcl2 = new HeltalDcl(new TekstLiteral("\"hej\""), "c");
        HeltalDcl dcl3 = new HeltalDcl(new BoolskLiteral("sandt"), "d");
        ProgramNode ast = new ProgramNode(asList(dcl1, dcl2, dcl3));
        assertThrows(Error.class, () ->
                ast.accept(new TypeChecker())
        );
    }

    @Test
    public void testDecimaltalDclTypecheck() {

        DecimaltalDcl dcl = new DecimaltalDcl(new DecimaltalLiteral("4,2"), "a");
        ProgramNode ast = new ProgramNode(asList(dcl));
        assertDoesNotThrow(() ->
                ast.accept(new TypeChecker())
        );
    }

    @Test
    public void testDecimaltalTypecheckThrows() {

        DecimaltalDcl dcl1 = new DecimaltalDcl(new HeltalLiteral("4"), "b");
        DecimaltalDcl dcl2 = new DecimaltalDcl(new TekstLiteral("\"hej\""), "c");
        DecimaltalDcl dcl3 = new DecimaltalDcl(new BoolskLiteral("sandt"), "d");
        ProgramNode ast1 = new ProgramNode(asList(dcl1, dcl2, dcl3));
        assertThrows(Error.class, () ->
                ast1.accept(new TypeChecker())
        );
    }

    @Test
    public void testTekstDclTypecheck() {

        TekstDcl dcl = new TekstDcl(new TekstLiteral("\"hejsa\""), "a");
        ProgramNode ast = new ProgramNode(asList(dcl));
        assertDoesNotThrow(() ->
                ast.accept(new TypeChecker())
        );
    }

    @Test
    public void testTekstDclTypecheckThrows() {

        TekstDcl dcl1 = new TekstDcl(new DecimaltalLiteral("4,2"), "b");
        TekstDcl dcl2 = new TekstDcl(new HeltalLiteral("2"), "c");
        TekstDcl dcl3 = new TekstDcl(new BoolskLiteral("sandt"), "d");
        ProgramNode ast1 = new ProgramNode(asList(dcl1, dcl2, dcl3));
        assertThrows(Error.class, () ->
                ast1.accept(new TypeChecker())
        );
    }

    @Test
    public void testBoolskDclTypecheck() {

        BoolskDcl dcl = new BoolskDcl(new BoolskLiteral("sandt"), "a");
        ProgramNode ast = new ProgramNode(asList(dcl));
        assertDoesNotThrow(() ->
                ast.accept(new TypeChecker())
        );
    }

    @Test
    public void testBoolskDclTypecheckThrows() {

        BoolskDcl dcl1 = new BoolskDcl(new DecimaltalLiteral("4,2"), "b");
        BoolskDcl dcl2 = new BoolskDcl(new TekstLiteral("\"hej\""), "c");
        BoolskDcl dcl3 = new BoolskDcl(new HeltalLiteral("4"), "d");
        ProgramNode ast1 = new ProgramNode(asList(dcl1, dcl2, dcl3));
        assertThrows(Error.class, () ->
                ast1.accept(new TypeChecker())
        );
    }

    // BINARY COMPUTING


    @Test
    public void testBinaryComputing() {
        DecimaltalDcl dec = new DecimaltalDcl(new DecimaltalLiteral("1,5"), "a");
        HeltalDcl heltal = new HeltalDcl(new HeltalLiteral("2"), "b");
        TekstDcl tekst = new TekstDcl(new TekstLiteral("\"hej\""), "c");
        BoolskDcl bool = new BoolskDcl(new BoolskLiteral("sandt"), "d");
        FuncDclNode funcDcl = new FuncDclNode("hej", null);
        for (AST.Operators op : AST.Operators.values()) {
            for (AST.DataTypes type : AST.DataTypes.values()) {
                if (type == AST.DataTypes.HELTAL) {
                    binaryComputingTemplate(heltal.getValue(), op, type);
                } else if (type == AST.DataTypes.DECIMALTAL) {
                    binaryComputingTemplate(dec.getValue(), op, type);
                } else if (type == AST.DataTypes.BOOLSK) {
                    binaryComputingTemplate(bool.getValue(), op, type);
                } else if (type == AST.DataTypes.TEKST) {
                    binaryComputingTemplate(tekst.getValue(), op, type);
                } else if (type == AST.DataTypes.RUTINE) {
                    binaryComputingTemplate(funcDcl, op, type);
                }
            }
        }
    }


    @Test
    public void testAssignNodeTypeCheckDecimal() {
        DecimaltalLiteral dec = new DecimaltalLiteral("1,5");
        DecimaltalDcl decDcl = new DecimaltalDcl(new DecimaltalLiteral("2,5"), "a");

        AssignNode assDec = new AssignNode(new IdNode(decDcl.getId()), dec);

        ProgramNode ast = new ProgramNode(asList(decDcl, assDec));
        ast.accept(new SymbolTableFilling());
        assertDoesNotThrow(() ->
                ast.accept(new TypeChecker())
        );
    }

    @Test
    public void testAssignNodeTypeCheckDecimalThrows() {
        HeltalLiteral hel = new HeltalLiteral("1");
        DecimaltalDcl decDcl = new DecimaltalDcl(new DecimaltalLiteral("4,2"), "a");

        AssignNode assDec = new AssignNode(new IdNode(decDcl.getId()), hel);

        ProgramNode ast = new ProgramNode(asList(decDcl, assDec));
        ast.accept(new SymbolTableFilling());
        assertThrows(IllegalTypeAssignmentException.class, () ->
                ast.accept(new TypeChecker())
        );
    }

    @Test
    public void testAssignNodeTypeCheckHeltalThrows() {
        DecimaltalLiteral dec = new DecimaltalLiteral("1,2");
        HeltalDcl helDcl = new HeltalDcl(new HeltalLiteral("4"), "a");

        AssignNode assDec = new AssignNode(new IdNode(helDcl.getId()), dec);

        ProgramNode ast = new ProgramNode(asList(helDcl, assDec));
        ast.accept(new SymbolTableFilling());
        assertThrows(IllegalTypeAssignmentException.class, () ->
                ast.accept(new TypeChecker())
        );
    }

    @Test
    public void testAssignNodeTypeCheckTekstThrows() {
        HeltalLiteral hel = new HeltalLiteral("1");
        TekstDcl tekstDcl = new TekstDcl(new TekstLiteral("\"Hejsa\""), "a");

        AssignNode assDec = new AssignNode(new IdNode(tekstDcl.getId()), hel);

        ProgramNode ast = new ProgramNode(asList(tekstDcl, assDec));
        ast.accept(new SymbolTableFilling());
        assertThrows(IllegalTypeAssignmentException.class, () ->
                ast.accept(new TypeChecker())
        );
    }

    @Test
    public void testAssignNodeTypeCheckHeltal() {
        HeltalLiteral hel = new HeltalLiteral("2");
        HeltalDcl helDcl = new HeltalDcl(new HeltalLiteral("1"), "b");

        AssignNode assHeltal = new AssignNode(new IdNode(helDcl.getId()), hel);

        ProgramNode ast = new ProgramNode(asList(helDcl, assHeltal));
        ast.accept(new SymbolTableFilling());
        assertDoesNotThrow(() ->
                ast.accept(new TypeChecker())
        );
    }

    @Test
    public void testAssignNodeTypeCheckTekst() {
        TekstLiteral tekst = new TekstLiteral("\"hej\"");
        TekstDcl tekstDcl = new TekstDcl(new TekstLiteral("\"øf\""), "c");

        AssignNode assTekst = new AssignNode(new IdNode(tekstDcl.getId()), tekst);

        ProgramNode ast = new ProgramNode(asList(tekstDcl, assTekst));
        ast.accept(new SymbolTableFilling());
        assertDoesNotThrow(() ->
                ast.accept(new TypeChecker())
        );
    }

    @Test
    public void testAssignNodeTypeCheckBool() {
        BoolskLiteral bool = new BoolskLiteral("sandt");
        BoolskDcl boolDcl = new BoolskDcl(new BoolskLiteral("falsk"), "d");

        AssignNode assBool = new AssignNode(new IdNode(boolDcl.getId()), bool);

        ProgramNode ast = new ProgramNode(asList(boolDcl, assBool));
        ast.accept(new SymbolTableFilling());
        assertDoesNotThrow(() ->
                ast.accept(new TypeChecker())
        );
    }

    @Test
    public void testBinaryComputingHeltalWithHeltal() {
        HeltalDcl dcl = new HeltalDcl(new HeltalLiteral("1"), "a");
        HeltalDcl dcl1 = new HeltalDcl(new HeltalLiteral("2"),"b");
        BinaryComputing bin = new BinaryComputing(">", dcl.getValue(), dcl1.getValue());
        ProgramNode ast = new ProgramNode(asList(bin));
        assertDoesNotThrow(() ->
                ast.accept(new TypeChecker())
        );
    }

    @Test
    public void testBinaryComputingTekstWithTekstThrows() {
        TekstDcl dcl = new TekstDcl(new TekstLiteral("\"hej\""), "a");
        TekstDcl dcl1 = new TekstDcl(new TekstLiteral("\"med dig\""), "b");
        BinaryComputing bin = new BinaryComputing(">", dcl.getValue(), dcl1.getValue());
        ProgramNode ast = new ProgramNode(asList(bin));
        assertThrows(IllegalOperationTypeException.class, () ->
                ast.accept(new TypeChecker())
        );
    }

    @Test
    public void testBinaryComputingTekstWithTekst() {
        TekstDcl dcl = new TekstDcl(new TekstLiteral("\"hej\""), "a");
        TekstDcl dcl1 = new TekstDcl(new TekstLiteral("\"med dig\""), "b");
        BinaryComputing bin = new BinaryComputing("er", dcl.getValue(), dcl1.getValue());
        ProgramNode ast = new ProgramNode(asList(bin));
        assertDoesNotThrow(() ->
                ast.accept(new TypeChecker())
        );
    }

    @Test
    public void testBinaryComputingBoolskWithBoolskThrows() {
        BoolskDcl dcl = new BoolskDcl(new BoolskLiteral("sandt"), "a");
        HeltalDcl dcl1 = new HeltalDcl(new HeltalLiteral("2"), "b");
        BinaryComputing bin = new BinaryComputing(">", dcl.getValue(), dcl1.getValue());
        ProgramNode ast = new ProgramNode(asList(bin));
        assertThrows(Error.class, () ->
                ast.accept(new TypeChecker())
        );
    }

    @Test
    public void testBinaryComputingBoolskWithBoolsk() {
        TekstDcl dcl = new TekstDcl(new TekstLiteral("\"hej\""), "a");
        TekstDcl dcl1 = new TekstDcl(new TekstLiteral("\"med dig\""), "b");
        BinaryComputing bin = new BinaryComputing("er", dcl.getValue(), dcl1.getValue());
        ProgramNode ast = new ProgramNode(asList(bin));
        assertDoesNotThrow(() ->
                ast.accept(new TypeChecker())
        );
    }

    @Test
    public void testIfNodeTypeCheck() {
        BoolskDcl bool = new BoolskDcl(new BoolskLiteral("sandt"), "yesBool");
        IfNode ifNode = new IfNode(bool.getValue(), new ArrayList<>(asList(bool)));
        ProgramNode ast = new ProgramNode(asList(ifNode));
        assertDoesNotThrow(() ->
            ast.accept(new TypeChecker())
        );
    }


    @Test
    public void testIfNodeTypeCheckThrows() {
        TekstDcl tekst = new TekstDcl(new TekstLiteral("\"bob\""), "noBool");
        IfNode ifNode = new IfNode(tekst.getValue(), new ArrayList<>(asList(tekst)));
        ProgramNode ast = new ProgramNode(asList(ifNode));
        assertThrows(Error.class, () ->
                ast.accept(new TypeChecker())
        );
    }


    @Test
    public void testLoopNodeTypeCheck() {
        FuncDclNode func = new FuncDclNode("hej", new ArrayList<>());
        LoopNode loop = new LoopNode(func.getId(), new HeltalLiteral("2"));
        ProgramNode ast = new ProgramNode(asList(loop, func));
        ast.accept(new SymbolTableFilling());
        assertDoesNotThrow(() ->
                ast.accept(new TypeChecker())
        );
    }

    @Test
    void testFuncDcl() {
        TekstDcl tekstDcl = new TekstDcl(new TekstLiteral("test"), "a");
        FuncDclNode func = new FuncDclNode("func", List.of(new AssignNode(new IdNode("a"), new TekstLiteral("test2"))));
        ProgramNode prog = new ProgramNode(List.of(tekstDcl, func));
        prog.accept(new SymbolTableFilling());

        assertDoesNotThrow(() -> prog.accept(new TypeChecker()));
    }

    @Test
    public void testLoopNodeTypeCheckRepeatsThrows() {
        TekstDcl tekstDcl = new TekstDcl(new TekstLiteral("Test"), "a");
        FuncDclNode func = new FuncDclNode("hej", List.of(new AssignNode(new IdNode("a"), new TekstLiteral("test"))));
        LoopNode loop = new LoopNode(func.getId(), new DecimaltalLiteral("2,5"));
        ProgramNode ast = new ProgramNode(asList(tekstDcl, loop, func));
        ast.accept(new SymbolTableFilling());
        assertThrows(Error.class, () ->
                ast.accept(new TypeChecker())
        );
    }

    @Test
    public void testLoopNodeTypeCheckRutineThrows() {
        TekstDcl tekst = new TekstDcl(new TekstLiteral("babooi"), "bla");
        LoopNode loop = new LoopNode(tekst.getId(), new HeltalLiteral("2"));
        ProgramNode ast = new ProgramNode(asList(loop, tekst));
        ast.accept(new SymbolTableFilling());
        assertThrows(Error.class, () ->
                ast.accept(new TypeChecker())
        );
    }

    @Test
    public void testUnaryComputing() {
        DecimaltalDcl dec = new DecimaltalDcl(new DecimaltalLiteral("1,5"), "a");
        HeltalDcl heltal = new HeltalDcl(new HeltalLiteral("2"), "b");
        TekstDcl tekst = new TekstDcl(new TekstLiteral("\"hej\""), "c");
        BoolskDcl bool = new BoolskDcl(new BoolskLiteral("sandt"), "d");
        FuncDclNode funcDcl = new FuncDclNode("hej", List.of(new AssignNode(new IdNode("a"), new DecimaltalLiteral("2,5"))));
        for (AST.Operators op : AST.Operators.values()) {
            for (AST.DataTypes type : AST.DataTypes.values()) {
                if (type == AST.DataTypes.HELTAL) {
                    unaryComputingTemplate(heltal.getValue(), op, type);
                } else if (type == AST.DataTypes.DECIMALTAL) {
                    unaryComputingTemplate(dec.getValue(), op, type);
                } else if (type == AST.DataTypes.BOOLSK) {
                    unaryComputingTemplate(bool.getValue(), op, type);
                } else if (type == AST.DataTypes.TEKST) {
                    unaryComputingTemplate(tekst.getValue(), op, type);
                } else if (type == AST.DataTypes.RUTINE) {
                    unaryComputingTemplate(funcDcl, op, type);
                }
            }
        }
    }

    @Test
    void testFuncNode() {
        FuncDclNode funcDclNode = new FuncDclNode("func", List.of());
        FuncNode funcNode = new FuncNode("func");
        ProgramNode prog = new ProgramNode(List.of(funcDclNode, funcNode));

        prog.accept(new SymbolTableFilling());

        assertDoesNotThrow(() -> funcNode.accept(new TypeChecker()));
    }

    @Test
    void testFuncNodeIllegal() {
        FuncNode funcNode = new FuncNode("func");
        ProgramNode prog = new ProgramNode(List.of(funcNode));

        prog.accept(new SymbolTableFilling());

        assertThrows(UnexpectedTypeException.class, () -> funcNode.accept(new TypeChecker()));
    }

    @Test
    void testFindCommonDataTypeConversion() {
        assertEquals(AST.DataTypes.DECIMALTAL, new TypeChecker().findCommonDataType(AST.DataTypes.HELTAL,
                AST.DataTypes.DECIMALTAL, AST.Operators.PLUS));
    }

    @Test
    void testFindCommonDataTypeIllegal() {
        assertThrows(Error.class, () -> new TypeChecker().findCommonDataType(AST.DataTypes.BOOLSK,
                AST.DataTypes.DECIMALTAL, AST.Operators.PLUS));
    }

    @Test
    void testFieldnode() {

        DeviceNode deviceNode = new DeviceNode("lampe1", List.of(),"ip:port/endpoint");
        FieldNode fieldNode = new FieldNode("lysstyrke", "lampe1");
        ProgramNode prog = new ProgramNode(List.of(deviceNode, fieldNode));

        prog.accept(new SymbolTableFilling());

        assertDoesNotThrow(() -> fieldNode.accept(new TypeChecker()));
    }

    @Test
    void testFieldNodeIllegal() {
        HeltalDcl fieldDclNode = new HeltalDcl(new HeltalLiteral("80"), "lampe1.lysstyrke");
        DeviceNode deviceNode = new DeviceNode("lampe1", List.of(fieldDclNode),"ip:port/endpoint");
        FieldNode fieldNode = new FieldNode("lysstyrke", "lampe1");
        ProgramNode prog = new ProgramNode(List.of(deviceNode, fieldNode));

        prog.accept(new SymbolTableFilling());

       // assertThrows, () -> fieldNode.accept(new TypeChecker()));
    }

}