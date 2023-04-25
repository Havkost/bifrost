package ASTVisitor.Exceptions;

import ASTVisitor.ANSI;
import ASTVisitor.Parser.AST;

public class IllegalTypeAssignmentException extends RuntimeException {
    public IllegalTypeAssignmentException(String id, AST.DataTypes type, AST newObject) {
        super("Kan ikke gemme typen "+ ANSI.green(newObject.getType().toString()) + " i variablen '"
                + ANSI.blue(id) + "' med typen " + ANSI.green(type.toString()) + ".");
    }
}
