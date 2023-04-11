package ASTVisitor.Lexer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.CharArrayReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestCharStream {

    public CharStream makeCharStream(String input) {
        CharArrayReader reader = new CharArrayReader(input.toCharArray());
        CharStream charStream = new CharStream(reader);
        CodeScanner.initialize(charStream);
        return charStream;
    }

    @Test
    public void testPeek(){
        CharStream charStream = makeCharStream("This is a test");
        assertEquals('T', charStream.peek());
    }

    @Test
    public void testAdvance(){
        CharStream charStream = makeCharStream("This is a test");
        assertEquals('T', charStream.advance());
        assertEquals('h', charStream.peek());
    }

    @Test
    public void testAdvance2(){
        CharStream charStream = makeCharStream("");
        charStream.advance();
        assertEquals(0,charStream.peek());
    }

    @Test
    void testAdvanceError() {
        CharStream charStream = makeCharStream("");
        assertEquals(0, charStream.advance());
    }
}
