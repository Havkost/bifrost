package ASTVisitor.Parser;

public class SymReferencing extends Node {
    String id;

    public SymReferencing(String id) {
        this.id = id;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
