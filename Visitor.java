public abstract class Visitor {
    
    public void visit(AST n) {
        n.accept(this);
    }

    abstract void visit(FloatDcl n);
    abstract void visit(IntDcl n);
    abstract void visit(SymDeclaring n);
}
