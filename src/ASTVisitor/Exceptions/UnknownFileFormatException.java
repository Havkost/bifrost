package ASTVisitor.Exceptions;

import ASTVisitor.ANSI;

public class UnknownFileFormatException extends CustomException {
    public UnknownFileFormatException(String fileType) {
        super("Ukendt filtype: " + ANSI.red(fileType) +
                ". Benyt venligst kun " + ANSI.green(".iot") + " filer.", 0);
    }
}
