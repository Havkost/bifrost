package ASTVisitor.Exceptions;

public class IllegalStringConcatenationException extends RuntimeException {
    public IllegalStringConcatenationException() {
        super("Operationen '+' kan ikke bruges på typen tekst i boolske udtryk.");
    }
}
