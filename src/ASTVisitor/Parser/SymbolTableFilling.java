package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.ProgramNode;
import ASTVisitor.ASTnodes.SymDeclaring;

public class SymbolTableFilling extends Visitor {
    @Override
    void visit(Assigning n) {
        n.child1.accept(this);
    }

    @Override
    void visit(ProgramNode n) {

    }

    @Override
    void visit(SymDeclaring n) {

    }

    @Override
    void visit(Computing n) {
        n.child1.accept(this);
        n.child2.accept(this);
    }

    private void error(String message) {
        throw new Error(message);
    }
}
