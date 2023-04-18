package ASTVisitor.Exceptions;

import ASTVisitor.Lexer.TokenType;

public class UnexpectedExpressionToken extends RuntimeException {
    public UnexpectedExpressionToken(TokenType token) {
        super("Forventede udtryk. Fik " + token + ".");
    }
}
