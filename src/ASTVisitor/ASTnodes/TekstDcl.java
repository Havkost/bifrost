package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class TekstDcl extends VariableDcl {
    public TekstDcl(AST value, IdNode id, int line) {
        super(value, id, line);
    }

    public TekstDcl(AST value, IdNode id) {
        super(value, id);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TekstDcl object)) return false;
        return this.getId().equals(object.getId()) && this.getValue().equals(object.getValue());
    }
}
