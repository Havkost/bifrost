package ASTVisitor.Exceptions;

import ASTVisitor.Lexer.TokenType;

public class IllegalStatementException extends RuntimeException {
    public IllegalStatementException(TokenType token, int line) {
        super("Forventede sætning (sæt, gentag, kør eller hvis). Fik '" + token + "'." + " (Linje " + line + ")");
    }
}
