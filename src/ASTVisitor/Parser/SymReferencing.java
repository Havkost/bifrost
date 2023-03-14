package ASTVisitor.Parser;

public class SymReferencing extends AST {
    String id;

    public SymReferencing(String id) {
        this.id = id;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
