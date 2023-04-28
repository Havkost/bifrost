package ASTVisitor.Exceptions;

import ASTVisitor.ANSI;
import ASTVisitor.Parser.AST;

public class UnexpectedTypeException extends CustomException {
    public UnexpectedTypeException(String id, AST.DataTypes actType, AST.DataTypes expType, int line) {
        super("Forventede typen " + expType + "men fik variablen " + id + " med typen " + actType
                + ". (Linje " + line + ")", line);
    }
}
