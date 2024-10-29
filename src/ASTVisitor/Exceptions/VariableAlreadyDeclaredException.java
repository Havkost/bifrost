package ASTVisitor.Exceptions;

import ASTVisitor.ANSI;

public class VariableAlreadyDeclaredException extends CustomException {
    public VariableAlreadyDeclaredException(String id, int line) {
        super("Variablen " + ANSI.blue(id) + " er allerede deklareret. (Linje " + line + ")", line);
    }
}
