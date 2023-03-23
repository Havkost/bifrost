package ASTVisitor.Lexer;

import ASTVisitor.Parser.*;

import java.io.CharArrayReader;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


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
        StringBuilder sourceString = new StringBuilder();

        // Read text from source file into sourceString
        // TODO: Evt. skriv bedre error handling og fejlbeskeder
        try {
            File sourceFile = new File(args[0]);
            Scanner scanner = new Scanner(sourceFile);
            while (scanner.hasNextLine()) {
                sourceString.append(scanner.nextLine()).append("\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("[FEJL] Kunne ikke finde filen '"+args[0]+"'.");
            return;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("[FEJL] Kildefil blev ikke specificeret.");
            return;
        }

        try {
            System.out.println("\n=======================");
            System.out.println("Input program");
            System.out.println("=======================");
            System.out.println(sourceString);

            CharArrayReader reader = new CharArrayReader(sourceString.toString().toCharArray());
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
            System.out.println("[FEJL]: " + e);
            System.out.println("Stack trace:\n");
            e.printStackTrace(System.out);
        }
    }
}
