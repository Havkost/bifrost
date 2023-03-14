package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class AssignAST extends AST {

    private AST child1;
    private AST child2;

    public AssignAST(AST child1, AST child2) {
        this.child1 = child1;
        this.child2 = child2;
    }

    @Override
    public void accept(Visitor v) {

    }
}
