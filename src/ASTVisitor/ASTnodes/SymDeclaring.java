package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class SymDeclaring extends AST {

    String id;
    AST value;

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String getId() {
        return id;
    }

    public AST getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SymDeclaring)) return false;
        SymDeclaring object = (SymDeclaring) obj;
        if (this.id.equals(object.getId()) && this.value.equals(object.getValue())) {
            return true;
        }
        return false;
    }
}