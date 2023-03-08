package BentsCompiler;

// $Id: ScannerCode.java 17 2010-01-07 16:47:29Z cytron $

import static BentsCompiler.CharStream.BLANK;
import static BentsCompiler.Token.*;

/**
 * Scanner and ScanDigits from chapter 2
 *   These methods are implemented as static methods to mesh with
 *      the simplicity of chapter 2 code.
 *      Ideally, they would be instance methods of a
 *      Scanner class, and would be spelled according to 
 *      Java standards.
 * @author cytron
 * 
 */
public class ScannerCode {

	//
	//  The Stream s provides the actual charcter-by-character input
	//
	private static CharStream s;

	
	/**
	 * Initialize the input stream
	 * @param s The input Stream
	 */
	public static void init(CharStream s) {
		ScannerCode.s = s;
	}
	
	/**
	 * Figure 2.5 code, processes the input stream looking
	 *   for the next Token.
	 * @return the next input Token
	 */
	public static Token Scanner() {
		Token ans;
		while (s.peek() == BLANK)
			s.advance();
		if (s.EOF())
			ans = new Token(EOF);
		else {
			if (isDigit(s.peek()))
				ans = ScanDigits();
			else {
				char ch = s.advance();

				switch(representativeChar(ch)) {
				case 'a':  // matches {a, b, ..., z} - {f, i, p}
					ans = new Token(ID, ""+ch); break;
				case 'f':
					ans = new Token(FLTDCL);  break;
				case 'i':
					ans = new Token(INTDCL);    break;
				case 'p':
					ans = new Token(PRINT);     break;
				case '=':
					ans = new Token(ASSIGN);    break;
				case '+':
					ans = new Token(PLUS);      break;
				case '-':
					ans = new Token(MINUS);     break;
				default:
					throw new Error("Lexical error on character with decimal value: " + (int)ch);

				}
			}

		}
		return ans;
	}
	
	/**
	 * Figure 2.6 code, processes the input stream to form
	 *    a float or int constant.
	 * @return the Token representing the discovered constant
	 */

	private static Token ScanDigits() {
		String val = "";
		int   type;
		while (isDigit(s.peek())) {
			val = val + s.advance();
		}
		if (s.peek() != '.')
			type = INUM;
		else {
			type = FNUM;
			val = val + s.advance();
			while (isDigit(s.peek())) {
				val = val + s.advance();
			}
		}
		return new Token(type, val);
	}
	
	/**
	 * Represents all nonreserved letters by 'a'.
	 * @param in the character to be considered
	 * @return if nonreserved, then 'a', else same as in
	 */
	private static char representativeChar(char in) {
		if (
				'a' <= in && in <= 'z'
					&& in != 'f'
						&& in != 'i'
							&& in != 'p'
		)
			return 'a';
		else return in;
	}
	
	/**
	 * Helper method to check if a given character is a decimal digit.
	 * @param in the character to be tested
	 * @return true iff the character is in the set { 0, 1, ..., 9 }
	 */
	private static boolean isDigit(char in) {
		return '0' <= in && in <= '9';
	}

}
