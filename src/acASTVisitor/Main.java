package acASTVisitor;

import java.io.CharArrayReader;

// $Id: Main.java 26 2010-01-07 17:14:40Z cytron $

public class Main {

	/**
	 * Exercise the parser and scanner on some examples.
	 * @param args
	 */
	
	
	
	
	public static void main(String[] args) throws Throwable {
		//
		// Add more examples to this array
		//   Each will be tried through the parser
		//
		String[] examples = new String[] {
		/* 1 */ 		"f b   i a   a = 5   b = a + 3.2  p b",
		/* 2 */	        "i d d = 3.2",
		/* 3  */		"a a",
		/* 4 */ 		"f f",
		/* 5  */		"a = 5  f b",
		/* 6 */ 		"% %" 
		};

		
		for (String example : examples) {
			try {
				System.out.println("Parsing: " + example);
				CharArrayReader reader = new CharArrayReader(example.toCharArray());
				CharStream s = new CharStream(reader);

				ASTParser p = new ASTParser(s);
				AST ast = p.Prog();
				System.out.println("   Parse successful");
				ast.accept(new Prettyprinting());
				System.out.println(" Pretty Printing successful");
				ast.accept(new SymbolTableFilling());
				System.out.println(" Symbol Table filling successful");
				System.out.println(AST.SymbolTable.toString());
				ast.accept(new TypeChecker());
				System.out.println(" Type Checking successful");
				ast.accept(new Prettyprinting());
				System.out.println(" Pretty Printing successful");
				ast.accept(new CodeGenerator());
				System.out.println(" Code Generation successful");
				ast.accept(new CCodeGenerator());
				System.out.println(" C Code Generation successful");
				AST.SymbolTable.clear();
				
			}
			catch (Throwable t) {
				System.out.println("   Compiler ended with error: " + t);
				System.out.println("Stack trace: ");
				t.printStackTrace(System.out);
			}
		}

	}

}
