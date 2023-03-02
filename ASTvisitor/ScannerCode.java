package ASTvisitor;
import static ASTvisitor.Token.*;

public class ScannerCode {

    private static CharStream s;

    public static void init(CharStream s) {
        ScannerCode.s = s;
    }

    public static Token Scanner() {
        Token ans;
        while (s.peek() == ' ')
            s.advance();
        if (s.EOF()) return new Token(tokens.EOF);
        if (isDigit(s.peek()))
            ans = ScanDigits();
        else {
            String str = s.advance();

            switch(representativeString(str)) {
                case "gem":  // matches {a, b, ..., z} - {f, i, p}
                    ans = new Token(tokens.GEM); break;
                case "som":
                    ans = new Token(tokens.SOM); break;
                case "rutine":
                    ans = new Token(tokens.RUTINE); break;
                case ':':
                    ans = new Token(tokens.COLON);         break;
                    // VED IKKE OM NEWLINE VIRKER
                case '\n':
                    ans = new Token(tokens.NEWLINE);        break;
                case "set":
                    ans = new Token(tokens.SET);          break;
                case "til":
                    ans = new Token(tokens.TIL);         break;
                case "gentag":
                    ans = new Token(tokens.GENTAG);         break;
                case "gange":
                    ans = new Token(tokens.GANGE);         break;
                case "kor":
                    ans = new Token(tokens.KOR);         break;
                case "hvis":
                    ans = new Token(tokens.HVIS);         break;
                case "eller":
                    ans = new Token(tokens.ELLER);         break;
                case "og":
                    ans = new Token(tokens.OG);         break;
                case "ikke":
                    ans = new Token(tokens.IKKE);         break;
                case "er":
                    ans = new Token(tokens.ER);         break;
                case "tekst":
                    ans = new Token(tokens.TEKST);         break;
                case "heltal":
                    ans = new Token(tokens.HELTAL); break;
                case "decimaltal":
                    ans = new Token(tokens.DECIMALTAL); break;
                case "boolsk":
                    ans = new Token(tokens.BOOLSK); break;
                case '.':
                    ans = new Token(tokens.DOT); break;
                case '_':
                    ans = new Token(tokens.UNDERSCORE); break;
                case "sandt":
                    ans = new Token(tokens.SANDT); break;
                case "falsk":
                    ans = new Token(tokens.FALSK); break;
                case "tab":
                    ans = new Token(tokens.TAB); break;
                case "\"":
                    ans = new Token(tokens.QUOTE); break;
                default:
                    throw new Error("Lexical error on character with decimal value: " + (int)str);

            }
        }
        return ans;
    }

    private static Token ScanDigits() {
        String val = "";
        tokens type;
        while (isDigit(s.peek())) {
            val = val + s.advance();
        }
        if (s.peek() != '.')
            type = tokens.DECIMALTAL;
        else {
            type = tokens.HELTAL;
            val = val + s.advance();
            while (isDigit(s.peek())) {
                val = val + s.advance();
            }
        }
        return new Token(type, val);
    }

    private static char representativeString(String in) {
        if (
                "a" <= in && in <= "z"
                        && in != "f"
                        && in != "i"
                        && in != "p"
        )
            return "a";
        else return in;
    }

    private static boolean isDigit(char in) {
        return '0' <= in && in <= '9';
    }

}
