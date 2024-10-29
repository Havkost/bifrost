package ASTVisitor.Lexer;

import java.util.Objects;

public class Token {

    private final TokenType type;
    private final String val;

    public Token(TokenType type) {
        this.type = type;
        this.val = "";
    }

    /**
     * @param type the type of literal, keyword, or identifier
     * @param val the value of the token (e.g. the literal identifier)
     */
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

    @Override
    public String toString() {
        if(!val.equals("")) {
            return "Token{" +
                    "type=" + type + ", " +
                    "value=" + val + "}";
        }

        return "Token{" +
                "type=" + type + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return type == token.type && Objects.equals(val, token.val);
    }
}
