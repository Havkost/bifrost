package ASTVisitor.Exceptions;

import ASTVisitor.ANSI;
import ASTVisitor.ASTnodes.BinaryComputing;
import ASTVisitor.ASTnodes.UnaryComputing;
import ASTVisitor.Parser.AST;

public class MissingTypeException extends CustomException {
    public MissingTypeException(BinaryComputing comp, int line) {
        super("Kunne ikke finde type i udregningen af " + comp + " på linje " + line + ".", line);
    }

    public MissingTypeException(UnaryComputing comp, int line) {
        super("Kunne ikke finde type i udregningen af " + comp + " på linje " + line + ".", line);
    }

    public MissingTypeException(AST object, int line) {
        super("Kunne ikke finde type for " + ANSI.blue(object.toString()) + "på linje " + line + ".", line);
    }
}
