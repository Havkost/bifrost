package ASTVisitor.Lexer;


import ASTVisitor.Parser.CharStream;

public class TokenStream {
        private Token nextToken;

        public TokenStream(CharStream s) {
            CodeScanner.init(s);
            advance();
        }

        public TokenType peek() {
            return nextToken.type;
        }

        public Token advance() {
            Token ans = nextToken;
            nextToken = CodeScanner.Scanner();
            return ans;
    }
}
