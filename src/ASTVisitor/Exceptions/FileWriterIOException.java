package ASTVisitor.Exceptions;

public class FileWriterIOException extends RuntimeException {
    public FileWriterIOException(String message) {
        super(message);
    }
}
