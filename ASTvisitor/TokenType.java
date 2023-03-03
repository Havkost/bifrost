package ASTvisitor;

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
    EOF("$");

    String val;
    TokenType(String val) {
        this.val = val;
    }
}

