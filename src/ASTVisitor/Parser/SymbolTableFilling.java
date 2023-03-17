package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;

import java.util.Base64;

public class SymbolTableFilling extends Visitor {

    @Override
    public void visit(ProgramNode n) {

    }

    @Override
    public void visit(SymDeclaring n) {

    }

    @Override
    public void visit(BinaryComputing n) {
        n.getChild1().accept(this);
        n.getChild2().accept(this);
    }

    private void error(String message) {
        throw new Error(message);
    }

    @Override
    public void visit(FuncDclNode n) {

    }

    @Override
    public void visit(FuncNode n) {

    }

    @Override
    public void visit(IfNode n) {

    }

    @Override
    public void visit(LoopNode n) {

    }

    @Override
    public void visit(AssignNode n) {

    }

    @Override
    public void visit(UnaryComputing n) {

    }

    @Override
    public void visit(SymReferencing n) {

    }
}
