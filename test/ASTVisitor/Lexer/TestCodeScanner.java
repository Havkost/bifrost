package ASTVisitor.Lexer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.function.Executable;

import java.io.CharArrayReader;

import static ASTVisitor.Lexer.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestCodeScanner
{
    String input =
            """
            gem heltal 3 som a
            sæt a til 2
            print a
            """;

    @Test
    public void testIsDigitTrue() {
        assertTrue(CodeScanner.isDigit('5'));
    }

    @Test
    public void testIsDigitLetter() {
        assertFalse(CodeScanner.isDigit('e'));
    }

    @Test
    public void testIsDigitSpecial() {
        assertFalse(CodeScanner.isDigit('!'));
    }

    @Test
    public void testIsQuote1() {
        assertTrue(CodeScanner.isQuote('\''));
    }

    @Test
    public void testIsQuote2() {
        assertTrue(CodeScanner.isQuote('\"'));
    }

    @Test
    public void testIsQuoteFalse1() {
        assertFalse(CodeScanner.isQuote('3'));
    }

    @Test
    public void testIsQuoteFalse2() {
        assertFalse(CodeScanner.isQuote('e'));
    }

    @Test
    public void testIsQuoteFalse3() {
        assertFalse(CodeScanner.isQuote('?'));
    }


    // Integration tests

    @Test
    public void testScanString() {
        String newInput = "\"this is a test string\"";
        CharArrayReader reader2 = new CharArrayReader(newInput.toCharArray());
        CharStream newCharStream = new CharStream(reader2);
        CodeScanner.initialize(newCharStream);
        assertEquals("this is a test string", CodeScanner.scanString().getVal());
    }

    @Test
    public void testScan() {
        CharArrayReader reader = new CharArrayReader(input.toCharArray());
        CharStream charStream = new CharStream(reader);
        CodeScanner.initialize(charStream);
        Token token = CodeScanner.scan();

        assertEquals(GEM, token.getType());
        assertEquals("", token.getVal());
    }

    @Test
    public void testScan2() {
        CharArrayReader reader = new CharArrayReader(input.toCharArray());
        CharStream charStream = new CharStream(reader);
        CodeScanner.initialize(charStream);
        CodeScanner.scan();
        Token token = CodeScanner.scan();

        assertEquals(HELTAL_DCL, token.getType());
        assertEquals("", token.getVal());
    }

    @Test
    public void testScanBool() {
        CharArrayReader reader = new CharArrayReader("gem boolsk sandt som a".toCharArray());
        CharStream charStream = new CharStream(reader);
        CodeScanner.initialize(charStream);
        CodeScanner.scan();
        CodeScanner.scan();
        Token token = CodeScanner.scan();

        assertEquals(BOOLSK_LIT, token.getType());
        assertEquals("sandt", token.getVal());
    }

    @Test
    public void testScanId() {
        CharArrayReader reader = new CharArrayReader("gem boolsk sandt som a".toCharArray());
        CharStream charStream = new CharStream(reader);
        CodeScanner.initialize(charStream);
        for (int i = 0; i < 4; i++) {
            CodeScanner.scan();
        }
        Token token = CodeScanner.scan();

        assertEquals(ID, token.getType());
        assertEquals("a", token.getVal());
    }

    @Test
    public void testScan_String() {
        CharArrayReader reader = new CharArrayReader("gem tekst \"hej\" som a".toCharArray());
        CharStream charStream = new CharStream(reader);
        CodeScanner.initialize(charStream);
        CodeScanner.scan();
        CodeScanner.scan();
        Token token = CodeScanner.scan();

        assertEquals(TEKST_LIT, token.getType());
        assertEquals("hej", token.getVal());
    }

    @Test
    public void testScanIllegalChar() {
        CharArrayReader reader = new CharArrayReader("asd?kp".toCharArray());
        CharStream charStream = new CharStream(reader);
        CodeScanner.initialize(charStream);
        Token token = CodeScanner.scan();

        assertEquals(ID, token.getType());
        assertEquals("asd", token.getVal());

        assertThrows(Error.class, CodeScanner::scan);
    }

    @Test
    public void testScanDigitsInt() {
        CharArrayReader reader = new CharArrayReader("483".toCharArray());
        CharStream charStream = new CharStream(reader);
        CodeScanner.initialize(charStream);
        Token token = CodeScanner.scanDigits();
        assertEquals(HELTAL_LIT, token.getType());
        assertEquals("483", token.getVal());
    }

    @Test
    public void testScanDigitsIntLeadingZeros() {
        CharArrayReader reader = new CharArrayReader("007".toCharArray());
        CharStream charStream = new CharStream(reader);
        CodeScanner.initialize(charStream);
        Token token = CodeScanner.scanDigits();
        assertEquals(HELTAL_LIT, token.getType());
        assertEquals("007", token.getVal());
    }

    @Test
    public void testScanDigitsDbl() {
        CharArrayReader reader = new CharArrayReader("123879,328".toCharArray());
        CharStream charStream = new CharStream(reader);
        CodeScanner.initialize(charStream);
        Token token = CodeScanner.scanDigits();
        assertEquals(DECIMALTAL_LIT, token.getType());
        assertEquals("123879,328", token.getVal());
    }
}
