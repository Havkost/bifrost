package ASTVisitor;

public class Assigning extends AST {
    String id;
    AST child1;

    //TODO Overvej om first child skal være en SymReferencing node
    public Assigning(String id, AST child1) {
        this.id = id;
        this.child1 = child1;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
