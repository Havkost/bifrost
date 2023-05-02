package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import ASTVisitor.Exceptions.VariableAlreadyDeclaredException;

import static ASTVisitor.Parser.AST.DataTypes.*;

public class SymbolTableFilling extends Visitor {

    public SymbolTableFilling() {
        AST.clearSymbolTable();
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
        else throw new VariableAlreadyDeclaredException(n.getId(), n.getLine());
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

    public String getId(VariableDcl n) {
        if(n.getId().getParentId() != null)
            return n.getId().getParentId() + "." + n.getId().getValue();
        return n.getId().getValue();
    }

    @Override
    public void visit(TekstDcl n) {
        String id = getId(n);
        if (AST.SymbolTable.get(id) == null) AST.SymbolTable.put(id, TEKST);
        else throw new VariableAlreadyDeclaredException(id, n.getLine());
        n.getValue().accept(this);
        n.type = TEKST;
    }

    @Override
    public void visit(HeltalDcl n) {
        String id = getId(n);
        if (AST.SymbolTable.get(id) == null) AST.SymbolTable.put(id, HELTAL);
        else throw new VariableAlreadyDeclaredException(id, n.getLine());
        n.getValue().accept(this);
        n.type = HELTAL;
    }

    @Override
    public void visit(DecimaltalDcl n) {
        String id = getId(n);
        if (AST.SymbolTable.get(id) == null) AST.SymbolTable.put(id, DECIMALTAL);
        else throw new VariableAlreadyDeclaredException(id, n.getLine());
        n.getValue().accept(this);
        n.type = DECIMALTAL;
    }

    @Override
    public void visit(BoolskDcl n) {
        String id = getId(n);
        if (AST.SymbolTable.get(id) == null) AST.SymbolTable.put(id, BOOLSK);
        else throw new VariableAlreadyDeclaredException(id, n.getLine());
        n.getValue().accept(this);
        n.type = BOOLSK;
    }
    @Override
    public void visit(DeviceNode n) {
        if (AST.SymbolTable.get(n.getId()) == null) AST.SymbolTable.put(n.getId(),DEVICE);
        else throw new VariableAlreadyDeclaredException(n.getId(), n.getLine());
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
}
