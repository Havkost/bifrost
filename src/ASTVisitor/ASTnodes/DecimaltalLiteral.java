package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class DecimaltalLiteral extends AST {

    private String val;

    public DecimaltalLiteral(String val) {
        this.val = val;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public String getVal() {
        return val;
    }
}
