package ASTVisitor.Lexer;


public class TokenStream {
        private Token nextToken;

        public TokenStream(CharStream s) {
            CodeScanner.initialize(s);
            advance();
        }

        public TokenType peek() {
            return nextToken.getType();
        }

        public Token advance() {
            Token ans = nextToken;
            nextToken = CodeScanner.scan();
            return ans;
    }
}
