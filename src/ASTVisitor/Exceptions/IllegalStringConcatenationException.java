package ASTVisitor.Exceptions;

public class IllegalStringConcatenationException extends CustomException {
    public IllegalStringConcatenationException(int line) {
        super("Operationen '+' kan ikke bruges på typen tekst i boolske udtryk og printkommandoer. (Linje " + line + ")", line);
    }
}
