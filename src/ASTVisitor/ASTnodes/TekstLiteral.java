package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class TekstLiteral extends AST {

    private String val;

    public TekstLiteral(String value) {
        this.val = value;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public String getVal() {
        return val;
    }
}
