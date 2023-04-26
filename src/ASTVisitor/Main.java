package ASTVisitor;

import ASTVisitor.ASTDrawing.TreeDrawing;
import ASTVisitor.Exceptions.*;
import ASTVisitor.Lexer.CharStream;
import ASTVisitor.Lexer.CodeScanner;
import ASTVisitor.Parser.*;

import static ASTVisitor.ANSI.*;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        StringBuilder sourceString = new StringBuilder();
        List<String> inputPaths = new ArrayList<>();
        StringBuilder outName = null;
        boolean debug = false;
        boolean astDraw = false;

        String absPath = System.getProperty("user.dir");
        if(System.getProperty("os.name").startsWith("Windows")) {
            absPath += "\\";
        } else {
            absPath += "/";
        }

        // Read text from source file into sourceString

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.charAt(0) == '-') {
                switch (arg) {
                    case "-o", "--out":
                        try {
                            outName = new StringBuilder(absPath + args[++i]);
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
                    if (outName.toString().contains("."))
                        outName = new StringBuilder(Arrays.stream(outName.toString().split("\\.")).toList().get(0));
                    if (inputPaths.size() > 1)
                        outName.append(i).append(".c");
                    try {
                        FileWriter writer;
                        writer = new FileWriter("" + outName + ".c");
                        ast.accept(new CCodeGenerator(debug, writer));
                        if (debug) System.out.println("Genererer fil: " + outName + ".c");
                    } catch (FileWriterIOException e) {
                        errorPrint("Filen " + outName + " kunne ikke oprettes.");
                    }
                } else {
                    List<String> names;
                    if (System.getProperty("os.name").startsWith("Windows")) {
                        names = Arrays.stream(inputPaths.get(i).split("\\\\")).toList();
                    } else {
                        names = Arrays.stream(inputPaths.get(i).split("/")).toList();
                    }
                    String name = Arrays.stream(names.get(names.size()-1).split("\\.")).toList().get(0) + ".c";
                    name = absPath + name;
                    try {
                        FileWriter writer = new FileWriter(name);
                        ast.accept(new CCodeGenerator(debug, writer));
                        if (debug) System.out.println("Genererer fil: " + name);
                    } catch (FileWriterIOException e) {
                        errorPrint("Kan ikke skrive til filen " + name);
                    }

                }

                if (astDraw) {
                    TreeDrawing panel = new TreeDrawing(ast);
                    panel.draw();
                }

                AST.clearSymbolTable();
                sourceString.setLength(0);

            } catch (CustomException e) {
                if (debug) {
                    StringBuilder stackTrace = new StringBuilder();
                    for (StackTraceElement line: e.getStackTrace()) {
                        stackTrace.append("                ").append(line).append("\n");
                    }
                    errorPrint(e + "\n            " + ANSI.cyan("Linje " + e.getLine() + ": ") +
                            Arrays.stream(sourceString.toString().split("\n")).toList().get(e.getLine()-1).trim() + "\n            Stack trace:\n" + stackTrace);
                } else {
                    errorPrint(e.getMessage() + "\n            " + ANSI.cyan("Linje " + e.getLine() + ": ") +
                            Arrays.stream(sourceString.toString().split("\n")).toList().get(e.getLine()-1).trim());
                }

            } catch (IOException e) {
                if (debug) {
                    StringBuilder stackTrace = new StringBuilder();
                    for (StackTraceElement line: e.getStackTrace()) {
                        stackTrace.append("                ").append(line).append("\n");
                    }
                    errorPrint(e + "\n            Stack trace:\n" + stackTrace);
                } else {
                    errorPrint(e.getMessage());
                }
            }
        }
    }

    public static void errorPrint(String msg) {
        System.out.println(ANSI.red("[FEJL]: ") + msg);
    }
    // TODO: Evt. skriv bedre error handling og fejlbeskeder
}
