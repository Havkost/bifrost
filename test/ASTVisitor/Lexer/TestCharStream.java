package ASTVisitor.Lexer;

import org.junit.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.CharArrayReader;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestCharStream {
    @Test
    public void testPeek(){
        String newInput = "This is a test";
        CharArrayReader reader = new CharArrayReader(newInput.toCharArray());
        CharStream newCharStream = new CharStream(reader);
        CodeScanner.initialize(newCharStream);
        assertEquals('T', newCharStream.peek());
    }

    @Test
    public void testAdvance(){
        String newInput = "This is a test";
        CharArrayReader reader = new CharArrayReader(newInput.toCharArray());
        CharStream newCharStream = new CharStream(reader);
        CodeScanner.initialize(newCharStream);
        assertEquals('T',newCharStream.advance());
        assertEquals('h', newCharStream.peek());
    }

    @Test
    public void testAdvance2(){
        String newInput = "";
        CharArrayReader reader = new CharArrayReader(newInput.toCharArray());
        CharStream newCharStream = new CharStream(reader);
        CodeScanner.initialize(newCharStream);
        newCharStream.advance();
        assertEquals(0,newCharStream.peek());
    }
}
