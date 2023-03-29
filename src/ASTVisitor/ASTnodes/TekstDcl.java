package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class TekstDcl extends SymDeclaring {

    public TekstDcl(AST value, String id) {
        this.id = id;
        this.value = value;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TekstDcl)) return false;
        TekstDcl object = (TekstDcl) obj;
        if (this.id.equals(object.getId()) && this.getValue().equals(object.getValue())) {
            return true;
        }
        return false;
    }
}
