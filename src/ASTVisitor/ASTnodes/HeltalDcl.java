package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class HeltalDcl extends VariableDcl{

    public HeltalDcl(AST value, IdNode id, int line) {
        super(value, id, line);
    }

    public HeltalDcl(AST value, IdNode id) {
        super(value, id);
    }


    public void accept(Visitor v) {
        v.visit(this);
    }



    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HeltalDcl object)) return false;
        return this.getId().equals(object.getId()) && this.getValue().equals(object.getValue());
    }
}
