package ASTvisitor;

public class TokenStream {
        private Token nextToken;

        public TokenStream(CharStream s) {
            ScannerCode.init(s);
            advance();
        }

        public int peek() {
            return nextToken.type.ordinal();
        }

        public Token advance() {
            Token ans = nextToken;
            nextToken = ScannerCode.Scanner();
            return ans;
    }
}
