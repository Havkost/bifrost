package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class FuncDclNode extends AST {

    private String id;
    private AST child1;

    @Override
    public void accept(Visitor v) {

    }

    public FuncDclNode(String id, AST child1) {
        this.id = id;
        this.child1 = child1;
    }

    public String getId() {
        return id;
    }

    public AST getChild1() {
        return child1;
    }
}
