package ASTVisitor.Parser;

import java.util.ArrayList;

public class Program extends AST {
    ArrayList<AST> program;

    Program(ArrayList<AST> prg) {program = prg;}

    public void accept(Visitor v) {v.visit(this);}
}
