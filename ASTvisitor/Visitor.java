package ASTvisitor;

public abstract class Visitor {

    public void visit(AST n) {
        n.accept(this);
    }

    abstract void visit(Assigning n);
    abstract void visit(FloatDcl n);
    abstract void visit(IntDcl n);
    abstract void visit(SymDeclaring n);
    abstract void visit(Computing n);
}
