package acASTVisitor;

// $Id: TokenStream.java 22 2010-01-07 16:50:05Z cytron $

public class TokenStream {
	
	private Token nextToken;
	
	public TokenStream(CharStream s) {
		ScannerCode.init(s);
		advance();
	}
	
	public int peek() {
		return nextToken.type;
	}
	
	public Token advance() {
		Token ans = nextToken;
		nextToken = ScannerCode.Scanner();
		return ans;
	}
	
	

}
