package ASTVisitor.Lexer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestToken {

    @Test
    void testConstructors() {
        Token a = new Token(TokenType.HELTAL_LIT);
        Token b = new Token(TokenType.HELTAL_LIT, "123");
        assertEquals("", a.getVal());
        assertEquals("123", b.getVal());
    }

    @Test
    void testToString() {
        Token token = new Token(TokenType.GEM);

        assertEquals("Token{" +
                "type=" + TokenType.GEM + "}", token.toString());
    }

    @Test
    void testToString2() {
        Token token = new Token(TokenType.ID, "a");

        assertEquals("Token{" +
                "type=" + TokenType.ID +
                ", value=a" + "}", token.toString());
    }
}
