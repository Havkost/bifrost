package ASTVisitor.Lexer;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.ASTParser;
import ASTVisitor.Parser.CCodeGenerator;
import ASTVisitor.Parser.Prettyprinting;

import java.io.CharArrayReader;

public class Main {

    public static void main(String[] args) {

        String example =
                "gem heltal 3 som a\n" +
                "hvis 10 er 10: \n" +
                "   sæt a til 2. \n" +
                "print a";


        try {
            System.out.println("Get Token stream from: \n" + example + "\n");
            CharArrayReader reader = new CharArrayReader(example.toCharArray());
            CharStream charStream = new CharStream(reader);

            CodeScanner.initialize(charStream);

            /*while(!charStream.getEOF()) {
                System.out.println(CodeScanner.scan());
            }*/

            ASTParser p = new ASTParser(charStream);
            AST ast = p.prog();

            //ast.accept(new CCodeGenerator());


        } catch (Throwable e) {
            System.out.println("Ended with error: " + e);
            System.out.println("Stack trace:\n");
            e.printStackTrace(System.out);
        }
    }
}
