package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class DecimaltalDcl extends VariableDcl {

    public DecimaltalDcl(AST value, IdNode id, int line) {
        super(value, id, line);
    }

    public DecimaltalDcl(AST value, IdNode id) {
        super(value, id);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof DecimaltalDcl)) return false;
        DecimaltalDcl object = (DecimaltalDcl) obj;
        if (this.getId().equals(object.getId()) && this.getValue().equals(object.getValue())) {
            return true;
        }
        return false;
    }

}
