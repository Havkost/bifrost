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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UnaryComputing)) return false;
        UnaryComputing object = (UnaryComputing) obj;
        if (this.operation.equals(object.getOperation()) && this.child.equals(object.getChild())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "UnaryComputing{" +
                "operation='" + operation + '\'' +
                ", child=" + child.toString() +
                '}';
    }
}
