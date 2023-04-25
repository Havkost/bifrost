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

    public TokenType peek() {
        return nextToken.getType();
    }

    public Token advance() {
        Token ans = nextToken;
        nextToken = CodeScanner.scan();
        tokenList.add(nextToken);
        index++;
        return ans;
    }

    public long getIndex() {
        return index;
    }

    public List<Token> getTokenList() {
        return tokenList;
    }
}
