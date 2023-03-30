package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.*;

import java.util.ArrayList;
import java.util.List;

public class ProgramNode extends AST {
    private List<AST> children;

    public ProgramNode(List<AST> child) {
        this.children = child;
    }

    public List<AST> getChild() {
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
