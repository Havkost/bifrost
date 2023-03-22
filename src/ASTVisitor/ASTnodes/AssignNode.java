package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class AssignNode extends AST {

    private String id;
    private AST val;

    public AssignNode(String id, AST val) {
        this.id = id;
        this.val = val;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public String getId() {
        return id;
    }

    public AST getVal() {
        return val;
    }

    @Override
    public String toString() {
        return "AssignNode{" +
                "id='" + id + '\'' +
                ", val=" + val +
                '}';
    }
}
