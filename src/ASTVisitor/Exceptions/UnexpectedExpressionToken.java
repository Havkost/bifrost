package ASTVisitor.Exceptions;

import ASTVisitor.Lexer.TokenType;

public class UnexpectedExpressionToken extends CustomException {
    public UnexpectedExpressionToken(TokenType token, int line) {
        super("Forventede udtryk. Fik " + token + ". (Linje " + line + ")", line);
    }
}
