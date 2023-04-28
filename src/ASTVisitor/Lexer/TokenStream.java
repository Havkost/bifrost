package ASTVisitor.Lexer;


import java.util.ArrayList;
import java.util.List;

public class TokenStream {
    private long index;
    private final List<Token> tokenList;
    private Token nextToken;

    public TokenStream(CharStream s) {
        index = 0;
        tokenList = new ArrayList<>();
        CodeScanner.initialize(s);
        advance();
    }

    /**
     * @return the type of the next <strong><code>Token</code></strong>
     */
    public TokenType peek() {
        return nextToken.getType();
    }

    /**
     * Retrieves the next <code>Token</code> in the stream, and advances in the stream
     * via the <code>.scan()</code> method
     * @return the next <strong><code>Token</code></strong>
     */
    public Token advance() {
        Token ans = nextToken;
        nextToken = CodeScanner.scan();
        tokenList.add(nextToken);
        index++;
        return ans;
    }

    /**
     * For debugging purposes
     * @return a list of all tokens in the <strong><code>TokenStream</code></strong>
     */
    public List<Token> getTokenList() {
        return tokenList;
    }
}
