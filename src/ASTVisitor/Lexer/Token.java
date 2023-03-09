package ASTVisitor.Lexer;


public class Token {

    private final TokenType type;
    private final String val;

    public Token(TokenType type) {
        this.type = type;
        this.val = "";
    }
    public Token(TokenType type, String val) {
        this.type = type;
        this.val = val;
    }

    public TokenType getType() {
        return type;
    }

    public String getVal() {
        return val;
    }

    public String toString() {
        return "Token{" +
                "type=" + type.name +
                "value=" + val + "}";
    }
}
