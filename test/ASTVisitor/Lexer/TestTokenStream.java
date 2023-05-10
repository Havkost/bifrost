package ASTVisitor.Lexer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.io.CharArrayReader;
import java.util.List;

import static ASTVisitor.Lexer.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestTokenStream {

    public TokenStream makeCodeScanner(String inputStr) {
        CharArrayReader reader = new CharArrayReader(inputStr.toCharArray());
        CharStream charStream = new CharStream(reader);
        return new TokenStream(charStream);
    }

    @Test
    public void testPeek(){
        TokenStream tokenStream = makeCodeScanner("Gem heltal 3 som x");
        assertEquals(GEM, tokenStream.peek());
    }

    @Test
    public void testAdvance(){
        TokenStream tokenStream = makeCodeScanner("gem heltal 3 som x");
        assertEquals(GEM, tokenStream.advance().getType());
        assertEquals(HELTAL_DCL, tokenStream.peek());
    }

    @Test
    void testGetTokenList() {
        TokenStream tokenStream = makeCodeScanner("Gem heltal 3 som x");
        for (int i = 0; i < 4; i++) {
            tokenStream.advance();
        }
        assertEquals(List.of(new Token(GEM), new Token(HELTAL_DCL), new Token(HELTAL_LIT, "3"),
                new Token(SOM), new Token(ID, "x")), tokenStream.getTokenList());
    }
}