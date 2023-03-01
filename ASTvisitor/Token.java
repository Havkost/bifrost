package ASTvisitor;

public class Token {

    public final int type;
    public final String val;

    public Token(int type, String val) {
        this.type = type;
        this.val = val;
    }

    public Token(int type) {
        this(type,"");
    }

    public enum tokens {
        GEM, SOM, RUTINE, COLON, NEWLINE, SET,
        TIL, GENTAG, GANGE, KOR, HVIS, ELLER,
        OG, IKKE, ER, TEKST, HELTAL, DECIMALTAL,
        BOOLSK, DOT, UNDERSCORE, SANDT, FALSK, TAB, QUOTE
    }

    public final static String[] token2str = new String[] {
            "gem", "som", "rutine", ":", "\n", "set", "til",
            "gentag", "gange", "kor", "hvis", "eller", "og",
            "ikke", "er", "tekst", "heltal", "decimaltal",
            "boolsk", ".", "_", "sandt", "falsk", "tab", "\""
    };

    public String toString() {
        return "Token{" +
                "type=" + token2str[type] +
                ", val='" + val + '\'' +
                '}';
    }
}