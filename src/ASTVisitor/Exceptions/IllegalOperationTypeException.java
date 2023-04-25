package ASTVisitor.Exceptions;

import ASTVisitor.ANSI;
import ASTVisitor.Parser.AST;

public class IllegalOperationTypeException extends RuntimeException {
    public IllegalOperationTypeException(AST.Operators operation, AST.DataTypes type) {
        super("Kan ikke bruge operationen '" + ANSI.yellow(operation.textual) + "' på typen " + ANSI.green(type.toString()) + ".");
    }
}
