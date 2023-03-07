package ASTvisitor;
import java.io.Reader;

public class CharStream {

    private final static char BLANK = ' ';
    private final Reader is;
    private char nextChar;
    private boolean eof;

    public CharStream(Reader is) {
        this.is = is;
        this.nextChar = 0;
        this.eof = false;
    }

    public char peek() {
        return nextChar;
    }
    public boolean EOF() {
        return eof;
    }

    public char advance() {
        char ans = nextChar;
        try {
            int next = is.read();
            //  If end of file, read will return -1
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

