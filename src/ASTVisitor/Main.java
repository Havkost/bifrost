package ASTVisitor;

import ASTVisitor.ASTDrawing.TreeDrawing;
import ASTVisitor.Lexer.CharStream;
import ASTVisitor.Lexer.CodeScanner;
import ASTVisitor.Parser.*;

import java.io.CharArrayReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        StringBuilder sourceString = new StringBuilder();
        List<String> inputPaths = new ArrayList<>();
        String outName = null;
        boolean debug = false;
        boolean astDraw = false;

        // Read text from source file into sourceString

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.charAt(0) == '-') {
                switch (arg) {
                    case "-o", "--out":
                        try {
                            outName = args[++i];
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("[FEJL] Outputfil blev ikke specificeret.");
                        }
                        break;
                    case "-d", "--debug":
                        debug = true;
                        break;
                    case "-h", "--help":
                        break;
                    case "-a", "--ast":
                        astDraw = true;
                }
            } else {
                inputPaths.add(arg);
            }

            for (String fileName : inputPaths) {
                try {
                    File sourceFile = new File(fileName);
                    Scanner scanner = new Scanner(sourceFile);
                    while (scanner.hasNextLine()) {
                        sourceString.append(scanner.nextLine()).append("\n");
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("[FEJL] Kunne ikke finde filen '" + fileName + "'.");
                    return;
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("[FEJL] Kildefil blev ikke specificeret.");
                    return;
                }

                try {


                    CharArrayReader reader = new CharArrayReader(sourceString.toString().toCharArray());
                    CharStream charStream = new CharStream(reader);

                    CodeScanner.initialize(charStream);

                    ASTParser p = new ASTParser(charStream);
                    AST ast = p.prog();

                    if (debug) {

                    }

                    ast.accept(new SymbolTableFilling());

                    /*System.out.println("\n=======================");
                    System.out.println("Typechecker");
                    System.out.println("=======================");*/
                    ast.accept(new TypeChecker());

                    AST.clearSymbolTable();
                    sourceString.setLength(0);

                    if (debug) {
                        System.out.println("\n=======================");
                        System.out.println("Input program");
                        System.out.println("=======================");
                        System.out.println(sourceString);

                        System.out.println("\n=======================");
                        System.out.println("Pretty printing");
                        System.out.println("=======================");
                        ast.accept(new Prettyprinting(true));

                        System.out.println("\n=======================");
                        System.out.println("Symbol table");
                        System.out.println("=======================");
                        for (Map.Entry<String, AST.DataTypes> entry : AST.getSymbolTable().entrySet()) {
                            System.out.println(entry.getKey() + ":" + entry.getValue());
                        }

                        System.out.println("\n=======================");
                        System.out.println("C code");
                        System.out.println("=======================");
                    }

                    ast.accept(new CCodeGenerator(debug));

                    if (astDraw) {
                        TreeDrawing panel = new TreeDrawing(ast);
                        panel.draw();
                    }

                } catch (Throwable e) {
                    System.out.println("[FEJL]: " + e);
                    System.out.println("Stack trace:\n");
                    e.printStackTrace(System.out);
                }
            }
        }
        // TODO: Evt. skriv bedre error handling og fejlbeskeder
    }
}
