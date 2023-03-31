package ASTVisitor.Exceptions;

import ASTVisitor.Lexer.TokenType;

public class UnexpectedTokenException extends RuntimeException {
    public UnexpectedTokenException(TokenType expected, TokenType actual) {
        super("Forventede " +
                expected +
                " men fik " +
                actual);
    }
}
