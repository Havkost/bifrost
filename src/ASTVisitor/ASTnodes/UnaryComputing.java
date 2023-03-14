package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class UnaryComputing extends AST {
    String operation;
    AST child;

    public UnaryComputing(String operation, AST child) {
        this.operation = operation;
        this.child = child;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
