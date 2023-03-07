package ASTVisitor;

public enum TokenType {
    GEM("gem"),
    SOM("som"),
    RUTINE("rutine"),
    COLON(":"),
    NEWLINE("\n"),
    SET("sæt"),
    TIL("til"),
    GENTAG("gentag"),
    GANGE("gange"),
    KOR("kør"),
    HVIS("hvis"),
    ELLER("eller"),
    OG("og"),
    IKKE("ikke"),
    ER("er"),
    TEKST("tekst"),
    HELTAL("heltal"),
    DECIMALTAL("decimaltal"),
    BOOLSK("boolsk"),
    DOT("."),
    UNDERSCORE("_"),
    SANDT("sandt"),
    FALSK("falsk"),
    TAB("\t"),
    QUOTE("\""),
    /** TODO: LETTERS **/
    LETTER("any string that contains letters"),
    IKKEER("ikke er"),
    GREATER(">"),
    LESSER("<"),
    PLUS("+"),
    MINUS("-"),
    TIMES("*"),
    DIVIDE("/"),
    LPAREN("("),
    RPAREN(")"),
    DIGIT("any digit"),

    EOF("$");


    String val;
    TokenType(String val) {
        this.val = val;
    }
}

