package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.*;

import java.util.ArrayList;
import java.util.List;

public class ProgramNode extends AST {
    private List<AST> children;

    public ProgramNode(List<AST> children) {
        this.children = children;
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

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ProgramNode)) return false;
        ProgramNode object = (ProgramNode) obj;
        for (int i = 0; i < this.children.size(); i++) {
            if (!this.children.get(i).equals(object.children.get(i))) return false;
        }

        return true;
    }
}
