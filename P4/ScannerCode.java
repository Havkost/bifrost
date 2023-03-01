package P4Parser;

import static acASTVisitor.Token.FNUM;
import static acASTVisitor.Token.INUM;

public class ScannerCode {

    private static CharStream s;

    public static void init(CharStream s) {
        ScannerCode.s = s;
    }

    public static Token Scanner() {

    }

    private static Token ScanDigits() {
        String val = "";
        int   type;
        while (isDigit(s.peek())) {
            val = val + s.advance();
        }
        if (s.peek() != '.')
            type = INUM;
        else {
            type = FNUM;
            val = val + s.advance();
            while (isDigit(s.peek())) {
                val = val + s.advance();
            }
        }
        return new Token(type, val);
    }

    private static char representativeChar(char in) {
        if (
                'a' <= in && in <= 'z'
                        && in != 'f'
                        && in != 'i'
                        && in != 'p'
        )
            return 'a';
        else return in;
    }

    private static boolean isDigit(char in) {
        return '0' <= in && in <= '9';
    }

}
