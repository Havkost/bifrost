package ASTvisitor;

public class TokenStream {
        private Token nextToken;

        public TokenStream(CharStream s) {
            ScannerCode.init(s);
            advance();
        }

        public Token peek() {
            return nextToken;
        }

        public Token advance() {
            Token ans = nextToken;
            nextToken = ScannerCode.Scanner();
            return ans;
    }
}
