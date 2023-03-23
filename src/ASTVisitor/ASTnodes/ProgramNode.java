package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.*;

import java.util.ArrayList;

public class ProgramNode extends AST {
    private ArrayList<AST> children;

    public ProgramNode(ArrayList<AST> child) {
        this.children = child;
    }

    public ArrayList<AST> getChild() {
        return children;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return "ProgramNode{" +
                "child=" + children +
                '}';
    }
}
