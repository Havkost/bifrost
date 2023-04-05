package ASTVisitor.Lexer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.io.CharArrayReader;

import static ASTVisitor.Lexer.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestTokenStream {

    @Test
    public void testPeek(){
        String newInput = "Gem heltal 3 som x";
        CharArrayReader reader = new CharArrayReader(newInput.toCharArray());
        CharStream newCharStream = new CharStream(reader);
        CodeScanner.initialize(newCharStream);
        TokenStream tokenStream = new TokenStream(newCharStream);
        assertEquals(GEM, tokenStream.peek());
    }

    @Test
    public void testAdvance(){
        String newInput = "gem heltal 3 som x";
        CharArrayReader reader = new CharArrayReader(newInput.toCharArray());
        CharStream newCharStream = new CharStream(reader);
        CodeScanner.initialize(newCharStream);
        TokenStream tokenStream = new TokenStream(newCharStream);
        assertEquals(GEM, tokenStream.advance().getType());
        assertEquals(HELTAL_DCL, tokenStream.peek());
    }

}
