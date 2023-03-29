package ASTVisitor.ASTnodes;

import ASTVisitor.Parser.AST;
import ASTVisitor.Parser.Visitor;

public class HeltalLiteral extends AST {

    private String val;

    public HeltalLiteral(String value) {
        this.val = value;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public String getVal() {
        return val;
    }

    @Override
    public String toString() {
        return val;
    }
}
