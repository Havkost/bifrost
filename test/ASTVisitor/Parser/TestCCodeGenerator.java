package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.BinaryComputing;
import ASTVisitor.ASTnodes.DecimaltalLiteral;
import ASTVisitor.ASTnodes.HeltalLiteral;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        BinaryComputing expr = new BinaryComputing("er",
                new BinaryComputing("+", new HeltalLiteral("5"),
                        new DecimaltalLiteral("4,3")),
                new BinaryComputing("-", new HeltalLiteral("10"),
                        new DecimaltalLiteral("0,7")));
        generator.visit(expr);
        assertEquals("5 + 4.3 == 10 - 0.7", generator.getCode());
    }
}
