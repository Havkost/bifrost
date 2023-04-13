package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class BinaryComputing extends AST {

    private Operators operation;
    private AST child1;
    private AST child2;

    public BinaryComputing(String operation, AST child1, AST child2) {
        this.child1 = child1;
        this.child2 = child2;
        for(Operators operator : Operators.values()) {
            if(operator.textual.equals(operation.toLowerCase())) {
                this.operation = operator;
                return;
            }
        }
    }

    public BinaryComputing(Operators operation, AST child1, AST child2) {
        this.operation = operation;
        this.child1 = child1;
        this.child2 = child2;
    }

    public Operators getOperation() {
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
                ", child1=" + child1.toString() +
                ", child2=" + child2.toString() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof BinaryComputing)) return false;
        BinaryComputing object = (BinaryComputing) obj;
        if (this.operation.equals(object.getOperation()) && this.child1.equals(object.getChild1())
            && this.child2.equals(object.getChild2())) {
            return true;
        }

        return false;
    }
}
