package ASTVisitor.Exceptions;

import ASTVisitor.Lexer.TokenType;

import java.util.List;

public class UnexpectedTokenException extends RuntimeException {
    public UnexpectedTokenException(TokenType expected, TokenType actual) {
        super("Forventede " +
                expected +
                " men fik " +
                actual + ".");
    }

    public UnexpectedTokenException(List<String> expected, TokenType actual) {
        super("Forventede " + expectedTokensToString(expected)
                + " men fik " +
                actual + ".");
    }

    public static String expectedTokensToString(List<String> list) {
        StringBuilder res = new StringBuilder();
        list.forEach((token) -> res.append(token).append(", "));
        res.deleteCharAt(res.length()-1); res.deleteCharAt(res.length()-1);
        res.replace(res.lastIndexOf(","), res.lastIndexOf(",")+1, " eller");
        return res.toString();
    }
}
