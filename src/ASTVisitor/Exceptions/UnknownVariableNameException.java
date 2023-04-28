package ASTVisitor.Exceptions;

public class UnknownVariableNameException extends CustomException {
    public UnknownVariableNameException(String id, int line) {
        super("Kan ikke finde variablen " + id + ". Har du deklareret variablen? (Linje " + line + ")", line);
    }
}
