package ASTVisitor.Lexer;

import java.util.Map;
import static java.util.Map.entry;

public enum TokenType {
    ID(null),
    GEM("gem"),
    SOM("som"),
    RUTINE("rutine"),
    BLOCKSTART(":"),
    BLOCKSLUT("."),
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
    DECIMAL(","),
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
        entry(SET.name, SET),
        entry(TIL.name, TIL),
        entry(GENTAG.name, GENTAG),
        entry(GANGE.name, GANGE),
        entry(KOR.name, KOR),
        entry(HVIS.name, HVIS),
        entry(ELLER.name, ELLER),
        entry(OG.name, OG),
        entry(IKKE.name, IKKE),
        entry(ER.name, ER),
        entry(TEKST.name, TEKST),
        entry(HELTAL.name, HELTAL),
        entry(DECIMALTAL.name, DECIMALTAL),
        entry(BOOLSK.name, BOOLSK),
        entry(DECIMAL.name, DECIMAL),
        entry(UNDERSCORE.name, UNDERSCORE),
        entry(SANDT.name, SANDT),
        entry(FALSK.name, FALSK),
        entry(TAB.name, TAB),
        entry(QUOTE.name, QUOTE),
        entry(LETTER.name, LETTER),
        entry(IKKEER.name, IKKEER),
        entry(GREATER.name, GREATER),
        entry(LESSER.name, LESSER),
        entry(PLUS.name, PLUS),
        entry(MINUS.name, MINUS),
        entry(TIMES.name, TIMES),
        entry(DIVIDE.name, DIVIDE),
        entry(LPAREN.name, LPAREN),
        entry(RPAREN.name, RPAREN),
        entry(DIGIT.name, DIGIT),
        entry(EOF.name, EOF)
    );
}

