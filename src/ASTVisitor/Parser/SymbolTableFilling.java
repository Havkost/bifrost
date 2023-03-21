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
    public void visit(TekstLiteral n) {

    }

    @Override
    public void visit(TypeNode n) {

    }

    @Override
    public void visit(BinaryComputing n) {
        n.getChild1().accept(this);
        n.getChild2().accept(this);
    }

    @Override
    public void visit(BoolskLiteral n) {

    }

    @Override
    public void visit(DecimaltalLiteral n) {

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
    public void visit(HeltalLiteral n) {

    }

    @Override
    public void visit(IdNode n) {

    }

    @Override
    public void visit(IfNode n) {

    }

    @Override
    public void visit(LoopNode n) {

    }

    @Override
    public void visit(PrintNode n) {

    }

    @Override
    public void visit(AssignNode n) {

    }

    @Override
    public void visit(UnaryComputing n) {

    }

    @Override
    public void visit(TekstDcl n) {

    }

    @Override
    public void visit(HeltalDcl n) {

    }

    @Override
    public void visit(DecimaltalDcl n) {

    }
    @Override
    public void visit(SymReferencing n) {

    }

    @Override
    public void visit(BoolskDcl n) {

    }


}
