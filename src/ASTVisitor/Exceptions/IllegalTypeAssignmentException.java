package ASTVisitor.Exceptions;

import ASTVisitor.ANSI;
import ASTVisitor.ASTnodes.FieldNode;
import ASTVisitor.Parser.AST;

public class IllegalTypeAssignmentException extends CustomException {
    public IllegalTypeAssignmentException(String id, AST.DataTypes type, AST newObject, int line) {
        super("Kan ikke gemme typen "+ ANSI.green(newObject.getType().toString()) + " i variablen '"
                + ANSI.blue(id) + "' med typen " + ANSI.green(type.toString()) + ".Linje " + line + ")", line);
    }

    public IllegalTypeAssignmentException(FieldNode field, AST.DataTypes type, AST newObject, int line) {
        super("Kan ikke gemme typen "+ ANSI.green(newObject.getType().toString()) + " i variablen '"
                + ANSI.blue(field.getId()) + " for " + ANSI.blue(field.getParentId()) + "' med typen " + ANSI.green(type.toString()) + ".Linje " + line + ")", line);
    }
}
