package ASTVisitor.Exceptions;

public class CustomException extends RuntimeException {

    private int line;
    public CustomException(String message, int line) {
        super(message);
        this.line = line;
    }

    public int getLine() {
        return line;
    }
}
