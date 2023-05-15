package ASTVisitor.Lexer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

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

    public void initCodeScanner(String inputStr) {
        CharArrayReader reader = new CharArrayReader(inputStr.toCharArray());
        CharStream charStream = new CharStream(reader);
        CodeScanner.initialize(charStream);
    }

    @Test
    public void testScanString() {
        initCodeScanner("\"this is a test string\"");
        assertEquals("this is a test string", CodeScanner.scanString().getVal());
    }

    @Test
    public void testScan() {
        initCodeScanner(input);
        Token token = CodeScanner.scan();

        assertEquals(GEM, token.getType());
        assertEquals("", token.getVal());
    }

    @Test
    public void testScan2() {
        initCodeScanner(input);
        CodeScanner.scan();
        Token token = CodeScanner.scan();

        assertEquals(HELTAL_DCL, token.getType());
        assertEquals("", token.getVal());
    }

    @Test
    public void testScanBool() {
        initCodeScanner("gem boolsk sandt som a");
        CodeScanner.scan();
        CodeScanner.scan();
        Token token = CodeScanner.scan();

        assertEquals(BOOLSK_LIT, token.getType());
        assertEquals("sandt", token.getVal());
    }

    @Test
    public void testScanId() {
        initCodeScanner("gem boolsk sandt som a");
        for (int i = 0; i < 4; i++) {
            CodeScanner.scan();
        }
        Token token = CodeScanner.scan();

        assertEquals(ID, token.getType());
        assertEquals("a", token.getVal());
    }

    @Test
    public void testScan_String() {
        initCodeScanner("gem tekst \"hej\" som a");
        CodeScanner.scan();
        CodeScanner.scan();
        Token token = CodeScanner.scan();

        assertEquals(TEKST_LIT, token.getType());
        assertEquals("hej", token.getVal());
    }

    @Test
    public void testScanIllegalChar() {
        initCodeScanner("asd?kp");
        Token token = CodeScanner.scan();

        assertEquals(ID, token.getType());
        assertEquals("asd", token.getVal());

        assertThrows(Error.class, CodeScanner::scan);
    }

    @Test
    public void testScanDigitsInt() {
        initCodeScanner("483");
        Token token = CodeScanner.scanDigits();
        assertEquals(HELTAL_LIT, token.getType());
        assertEquals("483", token.getVal());
    }

    @Test
    public void testScanDigitsIntLeadingZeros() {
        initCodeScanner("007");
        Token token = CodeScanner.scanDigits();
        assertEquals(HELTAL_LIT, token.getType());
        assertEquals("007", token.getVal());
    }

    @Test
    public void testScanDigitsDbl() {
        initCodeScanner("123879,328");
        Token token = CodeScanner.scanDigits();
        assertEquals(DECIMALTAL_LIT, token.getType());
        assertEquals("123879,328", token.getVal());
    }

    @Test
    void testTimeScan() {
        initCodeScanner("gem tid 11:30 som a");
        CodeScanner.scan();
        CodeScanner.scan();
        Token token = CodeScanner.scan();

        assertEquals("11:30", token.getVal());
        assertEquals(TID, token.getType());
    }

    @Test
    public void testComments() {
        initCodeScanner("# hej");
        Token token1 = CodeScanner.scan();
        assertEquals(COMMENT, token1.getType());
    }
}
