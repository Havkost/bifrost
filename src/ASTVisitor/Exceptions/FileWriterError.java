package ASTVisitor.Exceptions;

public class FileWriterError extends RuntimeException {
    public FileWriterError(String message) {
        super(message);
    }
}
