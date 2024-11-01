package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class TidDcl extends VariableDcl {

    public TidDcl(AST value, IdNode id, int line) {
        super(value, id, line);
    }

    public TidDcl(AST value, IdNode id) {
        super(value, id);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TidDcl object)) return false;
        return this.getId().equals(object.getId()) && this.getValue().equals(object.getValue());
    }
}
