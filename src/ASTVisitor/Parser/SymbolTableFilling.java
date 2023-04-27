package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import static ASTVisitor.Parser.AST.DataTypes.*;

public class SymbolTableFilling extends Visitor {

    public SymbolTableFilling() {
        AST.clearSymbolTable();
    }

    @Override
    public void visit(FieldNode n) {
    }

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
        else error("Variablen " + n.getId() + " er allerede deklareret.");
        for (AST child : n.getBody()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(FuncNode n) {
    }

    @Override
    public void visit(IdNode n) {
    }

    @Override
    public void visit(IfNode n) {
        n.getExpr().accept(this);
        if (n.getBody() != null) {
            for (AST child : n.getBody()) {
                child.accept(this);
            }
        }
    }

    @Override
    public void visit(LoopNode n) {}

    @Override
    public void visit(PrintNode n) {}

    @Override
    public void visit(TekstDcl n) {
        if (AST.SymbolTable.get(n.getId()) == null) AST.SymbolTable.put(n.getId(),TEKST);
        else error("Variablen " + n.getId() + " er allerede deklareret.");
        n.getValue().accept(this);
    }

    @Override
    public void visit(HeltalDcl n) {
        if (AST.SymbolTable.get(n.getId()) == null) AST.SymbolTable.put(n.getId(),HELTAL);
        else error("Variablen " + n.getId() + " er allerede deklareret.");
        n.getValue().accept(this);
    }

    @Override
    public void visit(DecimaltalDcl n) {
        if (AST.SymbolTable.get(n.getId()) == null) AST.SymbolTable.put(n.getId(),DECIMALTAL);
        else error("Variablen " + n.getId() + " er allerede deklareret.");
        n.getValue().accept(this);
    }

    @Override
    public void visit(BoolskDcl n) {
        if (AST.SymbolTable.get(n.getId()) == null) AST.SymbolTable.put(n.getId(),BOOLSK);
        else error("Variablen " + n.getId() + " er allerede deklareret.");
        n.getValue().accept(this);
    }

    @Override
    public void visit(FieldDclNode n) {
        String id = n.getParentId() + "." + n.getId();

        if (AST.getSymbolTable().get(id) == null) {
            n.getValue().accept(this);
            AST.getSymbolTable().put(id, n.getValue().getType());
        }
        else error("Variablen " + n.getId() + " er allerede deklareret.");
    }

    @Override
    public void visit(DeviceNode n) {
        if (AST.SymbolTable.get(n.getId()) == null) AST.SymbolTable.put(n.getId(),DEVICE);
        else error("Variablen " + n.getId() + " er allerede deklareret.");
        n.getFields().forEach((field) -> {
            field.accept(this);
        });
    }

    @Override
    public void visit(DecimaltalLiteral n) {
        n.type = AST.DataTypes.DECIMALTAL;
    }

    @Override
    public void visit(BoolskLiteral n) {
        n.type = AST.DataTypes.BOOLSK;
    }

    @Override
    public void visit(HeltalLiteral n) {
        n.type = AST.DataTypes.HELTAL;
    }

    @Override
    public void visit(TekstLiteral n) {
        n.type = AST.DataTypes.TEKST;
    }

    private void error(String message) {
        throw new Error(message);
    }
}
