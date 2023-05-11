package ASTVisitor.Lexer;

//TODO: check for illegal symbols mid-string

import static ASTVisitor.Lexer.TokenType.*;
import java.util.regex.Pattern;

public class CodeScanner {
    private static final String BLANK = "\t \r";
    private static final Pattern identifierPattern = Pattern.compile("[a-zæøå_]+[a-zæøå_0-9]*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private static final Pattern nonSymbolPattern = Pattern.compile("[a-zæøå_0-9]", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private static CharStream charStream;

    /**
     * Initializes the <strong>CodeScanner</strong> with a <strong>CharStream</strong>
     * @param charStream input stream
     */
    public static void initialize(CharStream charStream) {
        CodeScanner.charStream = charStream;
    }

    /**
     * Scans the next word in the stream, ignoring whitespaces
     * @return <strong>Token</strong> based on the read word
     */
    public static Token scan() {
        Token res;

        // Skip whitespace
        while (BLANK.indexOf(charStream.peek()) != -1) {
            charStream.advance();
        }

        // Return end of file token when reaching the end
        if (charStream.getEOF()) return new Token(EOF);

        if(charStream.peek() == '#') {
            charStream.advance();
            while (charStream.peek() == ' ')
                charStream.advance();
            StringBuilder comment = new StringBuilder();
            while (charStream.peek() != '\n') {
                comment.append(charStream.advance());
            }
            charStream.advance();

            return new Token(COMMENT, comment.toString());
        }

        // Number literals (integers and floats)
        if (isDigit(charStream.peek())) return scanDigits();
        // String literals
        if (isQuote(charStream.peek())) return scanString();

        String nextWord = getNextWord();

        // Boolean literals
        if (nextWord.equalsIgnoreCase("sandt") || nextWord.equalsIgnoreCase("falsk")) {
            return new Token(BOOLSK_LIT, nextWord.toLowerCase());
        }

        // Match keywords
        if(tokenTypeMap.get(nextWord.toLowerCase()) != null) {
            return new Token(tokenTypeMap.get(nextWord.toLowerCase()));
        }

        // Check for non-allowed characters
        if(!identifierPattern.matcher(nextWord).matches()){
            throw new Error("Failed to identify symbol '"+nextWord+"'.");
        }

        // If this line is reached, the word must be an identifier
        return new Token(ID, nextWord);
    }

    /**
     * Registers the next word in the stream, seperated by whitespace or a
     * non-alphanumeric symbol, not including underscore ('_')
     * @return next word as a <strong>String</strong>
     */
    private static String getNextWord() {
        StringBuilder res = new StringBuilder();
        res.append(charStream.advance());
        if(!nonSymbolPattern.matcher(res).matches()) return res.toString();

        while (nonSymbolPattern.matcher(String.valueOf(charStream.peek())).matches()) {
            res.append(charStream.advance());
        }

        return res.toString();
    }

    /**
     * Scans a string of digits and optionally comma-seperated decimals. Building a token
     * based on either an integer (comma absent) or a floating-point decimal (with comma)
     * @return <strong>Token</strong> of either integer- or floating-point literal type
     */
    public static Token scanDigits() {
        StringBuilder num = new StringBuilder();
        TokenType type;

        while(isDigit(charStream.peek())) {
            num.append(charStream.advance());
        }

        if(charStream.peek() == ',') {
            num.append(charStream.advance());
            while(isDigit(charStream.peek())) {
                num.append(charStream.advance());
            }
            type = DECIMALTAL_LIT;
        } else if (charStream.peek() == ':') {
            num.append(charStream.advance());
            while (isDigit(charStream.peek())) {
                num.append(charStream.advance());
            }
            type = TID;
        } else {
            type = HELTAL_LIT;
        }

        return new Token(type, num.toString());
    }

    /**
     * Scans a string delimited by double quotes or single quotes
     * @return <Strong>Token</Strong> of type string literal
     */
    public static Token scanString() {
        StringBuilder val = new StringBuilder();
        char quoteType = charStream.advance();
        char nextC;
        while((nextC = charStream.advance()) != quoteType) {
            val.append(nextC);
        }
        return new Token(TEKST_LIT, val.toString());
    }

    /**
     * Determines if a character is a digit based on ASCII value
     * @param in character to be checked
     * @return <strong>true</strong> if digit, else <strong>false</strong>
     */
    public static boolean isDigit(char in) {
        return '0' <= in && in <= '9';
    }

    /**
     * Determines if a character is either a single quote- or double quote-character
     * @param in character to be checked
     * @return <strong>true</strong> if either type of quote, else <strong>false</strong>
     */
    public static boolean isQuote(char in) {
        return in == '"' || in == '\'';
    }

}
