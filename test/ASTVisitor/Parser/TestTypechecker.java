package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.awt.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestTypechecker {

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

    //TODO FIX DENNE
    @Test
    public void testBinaryComputing() {
        DecimaltalDcl dec = new DecimaltalDcl(new DecimaltalLiteral("1,5"), "a");
        HeltalDcl heltal = new HeltalDcl(new HeltalLiteral("2"), "b");
        TekstDcl tekst = new TekstDcl(new TekstLiteral("\"hej\""), "c");
        BoolskDcl bool = new BoolskDcl(new BoolskLiteral("sandt"), "d");
        BinaryComputing bin = null;
        for (AST.Operators op : AST.Operators.values()) {
            for (AST.DataTypes type : AST.DataTypes.values()) {
                if (type == AST.DataTypes.HELTAL) {
                    bin = new BinaryComputing(op.textual, heltal.getValue(), heltal.getValue());
                    ProgramNode ast = new ProgramNode(asList(bin));
                    if (AST.getOperationResultType(op, type) == null) {
                        assertThrows(Error.class, () ->
                                ast.accept(new TypeChecker()));
                    } else {
                        assertDoesNotThrow(() ->
                                ast.accept(new TypeChecker())
                        );
                    }
                } else if (type == AST.DataTypes.DECIMALTAL) {
                    bin = new BinaryComputing(op.textual, dec.getValue(), dec.getValue());
                    ProgramNode ast = new ProgramNode(asList(bin));
                    if (AST.getOperationResultType(op, type) == null) {
                        assertThrows(Error.class, () ->
                                ast.accept(new TypeChecker()));
                    } else {
                        assertDoesNotThrow(() ->
                                ast.accept(new TypeChecker())
                        );
                    }
                } else if (type == AST.DataTypes.BOOLSK) {
                    bin = new BinaryComputing(op.textual, bool.getValue(), bool.getValue());
                    ProgramNode ast = new ProgramNode(asList(bin));
                    if (AST.getOperationResultType(op, type) == null) {
                        assertThrows(Error.class, () ->
                                ast.accept(new TypeChecker()));
                    } else {
                        assertDoesNotThrow(() ->
                                ast.accept(new TypeChecker())
                        );
                    }
                } else if (type == AST.DataTypes.TEKST) {
                    bin = new BinaryComputing(op.textual, tekst.getValue(), tekst.getValue());
                    ProgramNode ast = new ProgramNode(asList(bin));
                    if (AST.getOperationResultType(op, type) == null) {
                        assertThrows(Error.class, () ->
                                ast.accept(new TypeChecker()));
                    } else {
                        assertDoesNotThrow(() ->
                                ast.accept(new TypeChecker())
                        );
                    }
                } else if (type == AST.DataTypes.RUTINE) {
                    //TODO lav case for denne 
                    System.out.println("FEJL, DER ER INGEN CASE FOR RUTINE!!");
                }
            }
        }
    }

    /*
    @Test
    public void testAssignNodeTypeCheck() {
        DecimaltalDcl dec = new DecimaltalDcl(new DecimaltalLiteral("1,5"), "a");
        HeltalDcl heltal = new HeltalDcl(new HeltalLiteral("2"), "b");
        TekstDcl tekst = new TekstDcl(new TekstLiteral("\"hej\""), "c");
        BoolskDcl bool = new BoolskDcl(new BoolskLiteral("sandt"), "d");
        AssignNode assDec = new AssignNode(dec.getId(), dec.getValue());
        AssignNode assHeltal = new AssignNode(heltal.getId(), heltal.getValue());
        AssignNode assTekst = new AssignNode(tekst.getId(), tekst.getValue());
        AssignNode assBool = new AssignNode(bool.getId(), bool.getValue());
        ProgramNode ast = new ProgramNode(asList(assDec, assHeltal, assTekst, assBool));
        assertDoesNotThrow(() ->
                ast.accept(new TypeChecker())
        );
    }
    */


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
        assertThrows(Error.class, () ->
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

    // TODO Ting skal pushes...
    /*
    @Test
    public void testAssignNode() {
        TekstDcl dcl = new TekstDcl(new TekstLiteral("\"Yoyo\""), "a");
        TekstDcl dcl1 = new TekstDcl(new TekstLiteral("\"bøf\""), "b");
        AssignNode ass = new AssignNode(dcl.getId(), dcl1.getValue());
        ProgramNode ast = new ProgramNode(asList(ass));
        assertDoesNotThrow(() ->
            ast.accept(new TypeChecker())
        );
    }
    */


    /*
    @Test
    public void testAssignNodeThrows() {
        //TODO
    }*/

    @Test
    public void testIfNodeTypeCheck() {
        BoolskDcl yesBool = new BoolskDcl(new BoolskLiteral("sandt"), "yesBool");
        TekstDcl noBool = new TekstDcl(new TekstLiteral("bababooi"), "noBool");
        IfNode ifNodeYes = new IfNode(yesBool.getValue(), new ArrayList<>(asList(yesBool)));
        IfNode ifNodeNo = new IfNode(noBool.getValue(), new ArrayList<>(asList(noBool)));
        List<IfNode> ifNodeList = new ArrayList<>(asList(ifNodeYes, ifNodeNo));
        for (IfNode ifNode: ifNodeList) {
            ProgramNode ast = new ProgramNode(asList(ifNode));
            System.out.println(ifNode.getExpr().getClass().isInstance(BoolskDcl.class));
            if (ifNode.getExpr().getType() == AST.DataTypes.BOOLSK) {

                assertDoesNotThrow(() ->
                    ast.accept(new TypeChecker())
                );
            } /*else {
                assertThrows(Error.class, () ->
                    ast.accept(new TypeChecker())
                );
            } */
        }
    }

    /*
    @Test
    public void testLoopNodeTypeCheck() {
        //FuncDclNode func = new FuncDclNode("isFunc", new ArrayList<>(asList()));
        FuncNode func = new FuncNode("isFunc");
        LoopNode yesLoop = new LoopNode(func.getId(), new HeltalLiteral("2"));
        LoopNode noLoop = new LoopNode("noFunc", new HeltalLiteral("2"));
        LoopNode noLoop1 = new LoopNode(func.getId(), new HeltalLiteral("2,5"));
        List<LoopNode> loopNodeList = new ArrayList<>(asList(yesLoop, noLoop, noLoop1));
        for (LoopNode loopNode: loopNodeList) {
            ProgramNode ast = new ProgramNode(asList(loopNode));
            System.out.println(loopNode.getId().getClass().getSuperclass().isInstance(FuncNode.class));
            if (loopNode.getId().getClass().getSuperclass().isInstance(FuncNode.class)) {
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAH");
            }
        }
    }
    */

}