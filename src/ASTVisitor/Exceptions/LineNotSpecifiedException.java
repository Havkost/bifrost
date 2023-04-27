package ASTVisitor.Exceptions;

import ASTVisitor.Parser.AST;

public class LineNotSpecifiedException extends CustomException {
    public LineNotSpecifiedException(AST object) {
        super("Linjenummeret er ikke specificeret for objektet: " + object, 0);
    }
}
