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
}
