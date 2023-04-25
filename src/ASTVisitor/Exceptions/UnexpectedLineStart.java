package ASTVisitor.Exceptions;

import ASTVisitor.Lexer.TokenType;

public class UnexpectedLineStart extends RuntimeException {
    public UnexpectedLineStart(TokenType tokenType, int line) {
        super("Ukendt begyndelse på ny linje. Forventede 'gem', 'sæt', " +
                "'gentag', 'kør', 'rutine', 'hvis' eller 'print'. Fik: " + tokenType + " (Linje " + line + ")");
    }
}
