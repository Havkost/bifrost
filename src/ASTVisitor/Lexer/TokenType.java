package ASTVisitor.Lexer;

import java.util.HashMap;
import java.util.Map;

public enum TokenType {
    ID(null),
    GEM("gem"),
    SOM("som"),
    RUTINE("rutine"),
    KOLON(":"),
    BLOCKSLUT("."),
    NEWLINE("\n"),
    SAET("sæt"),
    FOR("for"),
    TIL("til"),
    GENTAG("gentag"),
    GANGE("gange"),
    KOER("kør"),
    HVIS("hvis"),
    PRINT("print"),
    ELLER("eller"),
    OG("og"),
    IKKE("ikke"),
    ER("er"),
    TEKST_DCL("tekst"),
    HELTAL_DCL("heltal"),
    DECIMALTAL_DCL("decimaltal"),
    BOOLSK_DCL("boolsk"),
    TID_DCL("tid"),
    TEKST_LIT(null),
    HELTAL_LIT(null),
    DECIMALTAL_LIT(null),
    BOOLSK_LIT(null),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    PLUS("+"),
    MINUS("-"),
    TIMES("*"),
    DIVIDE("/"),
    LPAREN("("),
    RPAREN(")"),
    EOF("\u001a"),
    DEVICE_DCL("enhed"),
    MED("med"),
    KLOKKEN("klokken"),
    TID(null),
    COMMENT(null);

    final String name;

    TokenType(String name) {
        this.name = name;
    }

    public static final Map<String, TokenType> tokenTypeMap = new HashMap<>();

    static {
        for(final TokenType tokenType : TokenType.values()) {
            tokenTypeMap.put(tokenType.name, tokenType);
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

