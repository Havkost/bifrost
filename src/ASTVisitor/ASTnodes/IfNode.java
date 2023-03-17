package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class IfNode extends AST {

    private AST child1;
    private AST child2;


    @Override
    public void accept(Visitor v) {

    }

    public IfNode(AST child1, AST child2) {
        this.child1 = child1;
        this.child2 = child2;
    }

    public AST getChild1() {
        return child1;
    }

    public AST getChild2() {
        return child2;
    }
}
