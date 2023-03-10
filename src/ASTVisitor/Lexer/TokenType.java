package ASTVisitor.Lexer;

import java.util.Map;
import static java.util.Map.entry;

// TODO: Vi blander engelsk og dansk i vores enum, kan vi gøre det konsekvent?

public enum TokenType {
    ID(null),
    GEM("gem"),
    SOM("som"),
    RUTINE("rutine"),
    BLOCKSTART(":"),
    BLOCKSLUT("."),
    NEWLINE("\n"),
    SAET("sæt"),
    TIL("til"),
    GENTAG("gentag"),
    GANGE("gange"),
    KOER("kør"),
    HVIS("hvis"),
    ELLER("eller"),
    OG("og"),
    IKKE("ikke"),
    ER("er"),
    TEKST_DCL("tekst"),
    HELTAL_DCL("heltal"),
    DECIMALTAL_DCL("decimaltal"),
    BOOLSK_DCL("boolsk"),
    TEKST_LIT(null),
    HELTAL_LIT(null),
    DECIMALTAL_LIT(null),
    BOOLSK_LIT(null),
    /** TODO: LETTERS **/
    GREATER(">"),
    LESSER("<"),
    PLUS("+"),
    MINUS("-"),
    TIMES("*"),
    DIVIDE("/"),
    LPAREN("("),
    RPAREN(")"),
    EOF("\u001a");

    final String name;
    TokenType(String name) {
        this.name = name;
    }

    public static final Map<String, TokenType> tokenTypeMap = Map.ofEntries(
        entry(ID.name, ID),
        entry(GEM.name, GEM),
        entry(SOM.name, SOM),
        entry(RUTINE.name, RUTINE),
        entry(BLOCKSTART.name, BLOCKSTART),
        entry(BLOCKSLUT.name, BLOCKSLUT),
        entry(NEWLINE.name, NEWLINE),
        entry(SAET.name, SAET),
        entry(TIL.name, TIL),
        entry(GENTAG.name, GENTAG),
        entry(GANGE.name, GANGE),
        entry(KOER.name, KOER),
        entry(HVIS.name, HVIS),
        entry(ELLER.name, ELLER),
        entry(OG.name, OG),
        entry(IKKE.name, IKKE),
        entry(ER.name, ER),
        entry(TEKST_DCL.name, TEKST_DCL),
        entry(HELTAL_DCL.name, HELTAL_DCL),
        entry(DECIMALTAL_DCL.name, DECIMALTAL_DCL),
        entry(BOOLSK_DCL.name, BOOLSK_DCL),
        entry(TEKST_LIT.name, TEKST_LIT),
        entry(HELTAL_LIT.name, HELTAL_LIT),
        entry(DECIMALTAL_LIT.name, DECIMALTAL_LIT),
        entry(BOOLSK_LIT.name, BOOLSK_LIT),
        entry(GREATER.name, GREATER),
        entry(LESSER.name, LESSER),
        entry(PLUS.name, PLUS),
        entry(MINUS.name, MINUS),
        entry(TIMES.name, TIMES),
        entry(DIVIDE.name, DIVIDE),
        entry(LPAREN.name, LPAREN),
        entry(RPAREN.name, RPAREN),
        entry(EOF.name, EOF)
    );
}

