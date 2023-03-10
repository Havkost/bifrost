package ASTVisitor.Lexer;

import java.io.CharArrayReader;

public class Main {

    public static void main(String[] args) {

        String example =
                "gem heltal 3 som a\n" +
                "hvis 10 er 10: \n" +
                "   sæt a til 2.";


        try {
            System.out.println("Parse: " + example);
            CharArrayReader reader = new CharArrayReader(example.toCharArray());
            CharStream charStream = new CharStream(reader);

            CodeScanner.initialize(charStream);

            while(!charStream.getEOF()) {
                System.out.println(CodeScanner.scan());
            }

        } catch (Throwable e) {
            System.out.println("Ended with error: " + e);
            System.out.println("Stack trace:\n");
            e.printStackTrace(System.out);
        }
    }
}
