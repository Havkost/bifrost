package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class BinaryComputing extends AST {
    private String operation;
    private AST child1;
    private AST child2;

    public BinaryComputing(String operation, AST child1, AST child2) {
        this.operation = operation;
        this.child1 = child1;
        this.child2 = child2;
    }

    public String getOperation() {
        return operation;
    }

    public AST getChild1() {
        return child1;
    }

    public AST getChild2() {
        return child2;
    }

    public void accept(Visitor v) {v.visit(this);}

    @Override
    public String toString() {
        return "BinaryComputing{" +
                "operation='" + operation + '\'' +
                ", child1=" + child1 +
                ", child2=" + child2 +
                '}';
    }
}
