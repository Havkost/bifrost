package ASTVisitor.Exceptions;

import ASTVisitor.Lexer.TokenType;

import java.util.List;

public class UnexpectedTokenException extends CustomException {
    public UnexpectedTokenException(TokenType expected, TokenType actual, int line) {
        super("Forventede " +
                expected +
                " men fik " +
                actual + "." + " (Linje " + line + ")", line);
    }

    public UnexpectedTokenException(List<String> expected, TokenType actual, int line) {
        super("Forventede " + expectedTokensToString(expected)
                + " men fik " +
                actual + "." + " (Linje " + line + ")", line);
    }

    public static String expectedTokensToString(List<String> list) {
        StringBuilder res = new StringBuilder();
        list.forEach((token) -> res.append(token).append(", "));
        res.deleteCharAt(res.length()-1); res.deleteCharAt(res.length()-1);
        res.replace(res.lastIndexOf(","), res.lastIndexOf(",")+1, " eller");
        return res.toString();
    }
}
