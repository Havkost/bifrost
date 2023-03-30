package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


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
        ProgramNode ast1 = new ProgramNode(asList(dcl1, dcl2, dcl3));
        assertThrows(Error.class, () ->
                ast1.accept(new TypeChecker())
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

    /* //TODO FIX DENNE
    @Test
    public void testBinaryComputing() {
        HeltalDcl dcl = new HeltalDcl(new HeltalLiteral("1"), "a");
        HeltalDcl dcl1 = new HeltalDcl(new HeltalLiteral("2"),"b");
        BinaryComputing bin = new BinaryComputing(">", dcl, dcl1);
        ProgramNode ast = new ProgramNode(asList(bin));
        assertDoesNotThrow(() ->
                ast.accept(new TypeChecker())
        );
    } */

}