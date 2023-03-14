package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.*;

import java.util.ArrayList;

public class ProgramNode extends AST {
    ArrayList<AST> program;

    public ProgramNode(ArrayList<AST> prg) {
        program = prg;}

    public void accept(Visitor v) {v.visit(this);}
}
