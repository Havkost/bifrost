package ASTVisitor.Lexer;

import java.io.Reader;

public class CharStream {

    private final Reader reader;
    private char nextChar;
    private boolean eof;

    /**
     * @param reader readable stream
     */
    public CharStream(Reader reader) {
        this.reader = reader;
        this.eof = false;
        this.nextChar = 0;
        advance();
    }

    /**
     * View next character in stream
     * @return next character in stream
     */
    public char peek() {
        return nextChar;
    }

    /**
     * Signals if stream has reached end of file
     * @return <strong>true</strong> if EOF is reached, else <strong>false</strong>
     */
    public boolean getEOF() {
        return eof;
    }

    /**
     * Read next character in stream, and advance in the stream
     * @return next character in stream
     */
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

    public Reader getReader() {
        return reader;
    }
}
