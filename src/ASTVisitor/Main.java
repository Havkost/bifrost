package ASTVisitor;

import ASTVisitor.ASTDrawing.TreeDrawing;
import ASTVisitor.Lexer.CharStream;
import ASTVisitor.Lexer.CodeScanner;
import ASTVisitor.Parser.*;

import java.io.*;
import java.util.*;

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
        }

        for (int i = 0; i < inputPaths.size(); i++) {
            try {
                File sourceFile = new File(inputPaths.get(i));
                Scanner scanner = new Scanner(sourceFile);
                while (scanner.hasNextLine()) {
                    sourceString.append(scanner.nextLine()).append("\n");
                }
            } catch (FileNotFoundException e) {
                System.out.println("[FEJL] Kunne ikke finde filen '" + inputPaths.get(i) + "'.");
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

                ast.accept(new SymbolTableFilling());
                ast.accept(new TypeChecker());

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

                if (outName != null) {
                    try {
                        FileWriter writer;
                        if (inputPaths.size() > 1) {
                            writer = new FileWriter(outName + i);
                            System.out.println(outName + i);
                        } else {
                            writer = new FileWriter(outName);
                            System.out.println(outName);
                        }
                        ast.accept(new CCodeGenerator(debug, writer));
                    } catch (IOException e) {
                        System.out.println("[FEJL] Filen " + outName + " kunne ikke oprettes.");
                    }
                } else {
                    List<String> names = Arrays.stream(inputPaths.get(i).split("/")).toList();
                    try {
                        FileWriter writer = new FileWriter(names.get(names.size()-1));
                        System.out.println(names.get(names.size()-1));
                        ast.accept(new CCodeGenerator(debug, writer));
                    } catch (IOException e) {
                        System.out.println("[FEJL] Filen " + names.get(names.size()-1));
                    }
                }


                if (astDraw) {
                    TreeDrawing panel = new TreeDrawing(ast);
                    panel.draw();
                }

                AST.clearSymbolTable();
                sourceString.setLength(0);

            } catch (Throwable e) {
                System.out.println("[FEJL]: " + e);
                System.out.println("Stack trace:\n");
                e.printStackTrace(System.out);
            }
        }
    }
    // TODO: Evt. skriv bedre error handling og fejlbeskeder
}
