package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class UnaryComputing extends AST {
    private String operation;
    private AST child;

    public UnaryComputing(String operation, AST child) {
        this.operation = operation;
        this.child = child;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String getOperation() {
        return operation;
    }

    public AST getChild() {
        return child;
    }
}
