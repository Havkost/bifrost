package ASTVisitor.Exceptions;

import ASTVisitor.Lexer.TokenType;

public class UnexpectedDeclarationToken extends CustomException {
    public UnexpectedDeclarationToken(TokenType token, int line) {
        super("Forventede type deklaration (tekst, heltal, decimaltal eller boolsk) men fik " + token +
                ". (Linje " + line + ")", line);
    }
}
