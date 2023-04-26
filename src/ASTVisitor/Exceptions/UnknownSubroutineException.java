package ASTVisitor.Exceptions;

import ASTVisitor.ANSI;

public class UnknownSubroutineException extends CustomException {
    public UnknownSubroutineException(String id, int line) {
        super("Kan ikke finde rutinen " + ANSI.purple(id) + ". (Linje " + line + ")", line);
    }
}
