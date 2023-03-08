package ASTVisitor.Parser;
import ASTVisitor.Lexer.*;


public class TokenStream {
        private Token nextToken;

        public TokenStream(CharStream s) {
            ScannerCode.init(s);
            advance();
        }

        public TokenType peek() {
            return nextToken.type;
        }

        public Token advance() {
            Token ans = nextToken;
            nextToken = ScannerCode.Scanner();
            return ans;
    }
}
