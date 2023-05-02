package ASTVisitor.Exceptions;

import ASTVisitor.Lexer.TokenType;

import java.util.List;

public class UnexpectedTokenException extends CustomException {
    public UnexpectedTokenException(TokenType expected, TokenType actual, int line) {
        super("Forventede " +
                expected +
                " men fik " +
                actual + "." + " (Linje " + line + ")", line);
    }
}
