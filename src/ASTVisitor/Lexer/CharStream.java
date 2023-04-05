package ASTVisitor.Lexer;

import java.io.Reader;

public class CharStream {

    private final Reader reader;
    private char nextChar;
    private boolean eof;

    public CharStream(Reader reader) {
        this.reader = reader;
        this.eof = false;
        this.nextChar = 0;
        advance();
    }

    public char peek() {
        return nextChar;
    }

    public boolean getEOF() {
        return eof;
    }

    public char advance() {
        char ans = nextChar;
        try {
            int next = reader.read();
            if (next == -1) {
                eof = true;
                nextChar = 0;
            }
            else {
                nextChar = (char) next;
            }
        }

        //  On any problem, just assume end of input
        catch (Throwable t) {
            System.out.println("Error encountered " + t);
            eof = true;
            return 0;
        }
        return ans;
    }
}
