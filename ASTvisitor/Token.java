package ASTVisitor;

public class Token {

    public final TokenType type;

    public Token(TokenType type) {
        this.type = type;
    }

    public String toString() {
        return "Token{" +
                "type=" + type.val + "}";
    }
}
