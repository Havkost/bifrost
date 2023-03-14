package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.*;

import java.util.ArrayList;

public class ProgramNode extends AST {
    private ArrayList<AST> child;

    public ProgramNode(ArrayList<AST> child) {
        this.child = child;
    }

    public ArrayList<AST> getChild() {
        return child;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
