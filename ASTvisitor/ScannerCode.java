package ASTvisitor;

public class ScannerCode {

    private static CharStream s;

    public static void init(CharStream s) {
        ScannerCode.s = s;
    }

    public static Token Scanner() {
        Token ans;
        while (s.peek() == ' ')
            s.advance();
        if (s.EOF()) return new Token(EOF);
        if (isDigit(s.peek()))
            ans = ScanDigits();
        else {
            char ch = s.advance();

            switch(representativeChar(ch)) {
                case 'a':  // matches {a, b, ..., z} - {f, i, p}
                    ans = new Token(ID, ""+ch); break;
                case 'f':
                    ans = new Token(FLTDCL);  break;
                case 'i':
                    ans = new IntDcl();    break;
                case 'p':
                    ans = new Token(PRINT);     break;
                case '=':
                    ans = new Token(ASSIGN);    break;
                case '+':
                    ans = new Token(PLUS);      break;
                case '-':
                    ans = new Token(MINUS);     break;
                default:
                    throw new Error("Lexical error on character with decimal value: " + (int)ch);

            }
        }
        return ans;
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
