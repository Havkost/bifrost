package ASTVisitor.Lexer;

import ASTVisitor.Visitor;

public class Prog extends AST {
    ArrayList<AST> prog;

    Prog(ArrayList<AST> prg) {prog = prg;}

    public void accept(Visitor v) {v.visit(this);}
}
