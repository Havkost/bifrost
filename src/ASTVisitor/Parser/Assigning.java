package ASTVisitor.Parser;

public class Assigning extends Node {
    String id;
    Node child1;

    //TODO Overvej om first child skal være en SymReferencing node
    public Assigning(String id, Node child1) {
        this.id = id;
        this.child1 = child1;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
