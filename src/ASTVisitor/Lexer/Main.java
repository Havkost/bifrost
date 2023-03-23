package ASTVisitor.Lexer;

import ASTVisitor.Parser.*;

import java.io.CharArrayReader;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        String example =
                """
                        gem tekst "Hello world" som a
                        gem heltal 42 som b
                        gem decimaltal 56,34 som c
                        hvis 10 er 10:\s
                            hvis b < 10:
                                sæt a til "Nej"..\s
                        print a
                        print b
                        print c
                """;


        try {
            System.out.println("\n=======================");
            System.out.println("Input program");
            System.out.println("=======================");
            System.out.println(example);

            CharArrayReader reader = new CharArrayReader(example.toCharArray());
            CharStream charStream = new CharStream(reader);

            CodeScanner.initialize(charStream);

            /*while(!charStream.getEOF()) {
                System.out.println(CodeScanner.scan());
            }*/

            ASTParser p = new ASTParser(charStream);
            AST ast = p.prog();

            System.out.println("\n=======================");
            System.out.println("Pretty printing");
            System.out.println("=======================");
            ast.accept(new Prettyprinting());

            ast.accept(new SymbolTableFilling());

            System.out.println("\n=======================");
            System.out.println("C code");
            System.out.println("=======================");
            ast.accept(new CCodeGenerator());

            System.out.println("\n=======================");
            System.out.println("Symbol table");
            System.out.println("=======================");
            for (Map.Entry<String, AST.DataTypes> entry : AST.getSymbolTable().entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
            }

        } catch (Throwable e) {
            System.out.println("Ended with error: " + e);
            System.out.println("Stack trace:\n");
            e.printStackTrace(System.out);
        }
    }
}
