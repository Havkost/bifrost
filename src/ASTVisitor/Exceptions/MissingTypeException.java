package ASTVisitor.Exceptions;

import ASTVisitor.ASTnodes.BinaryComputing;

public class MissingTypeException extends CustomException {
    public MissingTypeException(BinaryComputing comp, int line) {
        super("Kunne ikke finde type i udregningen af " + comp + " på linje " + line + ".", line);
    }
}
