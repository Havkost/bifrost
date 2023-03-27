package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import static ASTVisitor.Parser.AST.DataTypes.*;

public class SymbolTableFilling extends Visitor {

    @Override
    public void visit(ProgramNode n) {
        for(AST ast : n.getChild()){
            ast.accept(this);
        }
    }

    @Override
    public void visit(AssignNode n) {
        n.getValue().accept(this);
    }

    @Override
    public void visit(BinaryComputing n) {
        n.getChild1().accept(this);
        n.getChild2().accept(this);
    }

    @Override
    public void visit(UnaryComputing n) {
        n.getChild().accept(this);
    }

    @Override
    public void visit(FuncDclNode n) {
        if (AST.SymbolTable.get(n.getId()) == null) AST.SymbolTable.put(n.getId(), RUTINE);
        else error("variable " + n.getId() + " is already declared");
    }

    @Override
    public void visit(FuncNode n) {
    }

    @Override
    public void visit(TypeNode n) {
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
    public void visit(SymReferencing n) {
    }

    @Override
    public void visit(SymDeclaring n) {
    }

    @Override
    public void visit(TekstDcl n) {
        if (AST.SymbolTable.get(n.getId()) == null) AST.SymbolTable.put(n.getId(),TEKST);
        else error("variable " + n.getId() + " is already declared");
    }

    @Override
    public void visit(HeltalDcl n) {
        if (AST.SymbolTable.get(n.getId()) == null) AST.SymbolTable.put(n.getId(),HELTAL);
        else error("variable " + n.getId() + " is already declared");
    }

    @Override
    public void visit(DecimaltalDcl n) {
        if (AST.SymbolTable.get(n.getId()) == null) AST.SymbolTable.put(n.getId(),DECIMALTAL);
        else error("variable " + n.getId() + " is already declared");
    }

    @Override
    public void visit(BoolskDcl n) {
        if (AST.SymbolTable.get(n.getId()) == null) AST.SymbolTable.put(n.getId(),BOOLSK);
        else error("variable " + n.getId() + " is already declared");
    }

    @Override
    public void visit(DecimaltalLiteral n) {
    }

    @Override
    public void visit(BoolskLiteral n) {
    }

    @Override
    public void visit(HeltalLiteral n) {
    }

    @Override
    public void visit(TekstLiteral n) {
    }
    private void error(String message) {
        throw new Error(message);
    }
}
