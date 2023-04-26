package ASTVisitor.Exceptions;

import ASTVisitor.ANSI;
import ASTVisitor.Parser.AST;

public class IllegalOperationTypeException extends CustomException {
    public IllegalOperationTypeException(AST.Operators operation, AST.DataTypes type, int line) {
        super("Kan ikke bruge operationen '" + ANSI.yellow(operation.textual) + "' på typen " + ANSI.green(type.toString()) + ". (Linje " + line + ")", line);
    }
}
