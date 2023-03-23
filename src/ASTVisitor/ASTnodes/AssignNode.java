package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class AssignNode extends AST {

    private String id;
    private AST value;

    public AssignNode(String id, AST value) {
        this.id = id;
        this.value = value;
    }

    public void setValue(AST value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public String getId() {
        return id;
    }

    public AST getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "AssignNode{" +
                "id='" + id + '\'' +
                ", val=" + value +
                '}';
    }
}
