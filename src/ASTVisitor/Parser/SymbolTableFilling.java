package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;

import java.util.Base64;

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
    void visit(BinaryComputing n) {
        n.getChild1().accept(this);
        n.getChild2().accept(this);
    }

    private void error(String message) {
        throw new Error(message);
    }
}
