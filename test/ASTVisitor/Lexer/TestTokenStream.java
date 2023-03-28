package ASTVisitor.Lexer;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;


import java.io.CharArrayReader;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class TestTokenStream {

    TokenStream tokenStream;
    @BeforeEach
    void setup() {
        String newInput = "Gem x som 3";
        CharArrayReader reader = new CharArrayReader(newInput.toCharArray());
        CharStream newCharStream = new CharStream(reader);
        CodeScanner.initialize(newCharStream);
        tokenStream = new TokenStream(newCharStream);
    }
/*
    @Test
    public void testPeek(){
        assertEquals("gem", tokenStream.peek().name);
    }

    @Test
    public void testAdvance(){
        tokenStream.advance();
        assertEquals("som", tokenStream.peek().name);
    }
*/
}
