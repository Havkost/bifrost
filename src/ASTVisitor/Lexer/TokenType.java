package ASTVisitor.Lexer;

import java.util.HashMap;
import java.util.Map;

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
    PRINT("print"),
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

    public static final Map<String, TokenType> tokenTypeMap = new HashMap<>();

    static {
        for(final TokenType tokenType : TokenType.values()) {
            tokenTypeMap.put(tokenType.name, tokenType);
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

