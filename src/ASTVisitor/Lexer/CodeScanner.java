package ASTVisitor.Lexer;

//TODO: check for illegal symbols mid-string


import static ASTVisitor.Lexer.TokenType.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeScanner {
    private static final String BLANK = "\t \r";
    private static final Pattern identifierPattern = Pattern.compile("[a-zæøå_]+[a-zæøå_0-9]*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private static CharStream charStream;

    public static void initialize(CharStream charStream) {
        CodeScanner.charStream = charStream;
    }

    public static Token scan() {
        Token res;
        while (BLANK.indexOf(charStream.peek()) != -1) {
            charStream.advance();
        }
        if (charStream.getEOF()) return new Token(EOF);
        // Literals
        if (isDigit(charStream.peek())) return scanDigits();
        if (isQuote(charStream.peek())) return scanString();

        // Read whole word
        String nextWord = getNextWord();

        // Match keywords
        if(tokenTypeMap.get(nextWord) != null) {
            return new Token(tokenTypeMap.get(nextWord), nextWord);
        }

        // Nextword must be identifier
        if(!identifierPattern.matcher(nextWord).matches()){
            throw new Error("Failed to identify symbol '"+nextWord+"'.");
        }

        return new Token(ID, nextWord);
    }

    private static String getNextWord() {
        StringBuilder res = new StringBuilder();
        while (BLANK.indexOf(charStream.peek()) == -1) {
            res.append(charStream.advance());
        }

        return res.toString();
    }

    private static Token scanDigits() {
        StringBuilder num = new StringBuilder();
        TokenType type;

        while(isDigit(charStream.peek())){
            num.append(charStream.advance());
        }

        if(charStream.peek() != ',') {
            type = DECIMALTAL_LIT;
        }

        else {
            type = HELTAL_LIT;
        }
        return new Token(type, num.toString());
    }

    private static Token scanString() {
        StringBuilder val = new StringBuilder();
        char quoteType = charStream.advance();
        char nextC;
        while((nextC = charStream.advance()) != quoteType) {
            val.append(nextC);
        }
        return new Token(TEKST, val.toString());
    }

    private static boolean isDigit(char in) {
        return '0' <= in && in <= '9';
    }

    public static boolean isQuote(char in) {
        return in == '"' || in == '\'';
    }

}
