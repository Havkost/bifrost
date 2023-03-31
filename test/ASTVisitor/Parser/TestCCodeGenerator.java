package ASTVisitor.Parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestCCodeGenerator {
    @Test
    void testEmit() {
        CCodeGenerator generator = new CCodeGenerator();

        generator.emit("Test text");
        generator.emit(" added.");
        assertEquals("Test text added.", generator.getCode());
    }


}
