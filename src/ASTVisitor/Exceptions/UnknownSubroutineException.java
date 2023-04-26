package ASTVisitor.Exceptions;

import ASTVisitor.ANSI;

public class UnknownSubroutineException extends RuntimeException {
    public UnknownSubroutineException(String id) {
        super("Kan ikke finde rutinen " + ANSI.purple(id) + ".");
    }
}
