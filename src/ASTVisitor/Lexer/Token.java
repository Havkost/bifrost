package ASTVisitor.Lexer;


import java.util.Objects;

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
        if (type == TokenType.NEWLINE) {
            return "Token{" +
                    "type=" + type + "}\n";
        }
        if(!val.equals("")) {
            return "Token{" +
                    "type=" + type + ", " +
                    "value=" + val + "}";
        }

        return "Token{" +
                "type=" + type + "}";
    }
}
